/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.data.processor;

import com.fluke.database.DatabaseProperty;
import com.fluke.database.dataservice.EquityDao;
import com.fluke.model.ticker.Ticker;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fluke.model.RealtimeTicker;
import com.fluke.realtime.data.DataStreamLostException;
import com.fluke.realtime.data.RediffParser;
import com.fluke.util.Util;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

/**
 *
 * @author arsh
 */
public class ReatimeDBReader implements TickerDataSource,Runnable{

    QueryRunner runner = new QueryRunner(DatabaseProperty.getDataSource());
    private String query="select * from realtime   where equity=? and id > ?   and id<(select max(id) from realtime) order by id asc limit 1";
    Map<String,Long> ids = new HashMap<>();
    // QueryRunner run = new QueryRunner( DatabaseProperty.getDataSource() );
    private List excludeList = new ArrayList<>();
    @Override
    
    public Ticker getNextTicker(String equity) {
        Long id = ids.get(equity);
        if(equity.startsWith(".NSE")) return null;
        if(id==null||id==0){
            id = 0l;
        }
        List<RealtimeTicker> list = null;
        Object[] params = new Object[]{equity, id};
        ResultSetHandler rsh = new BeanListHandler(RealtimeTicker.class);
        int counter=0;
        try {
            list = (List<RealtimeTicker>)runner.query(query, rsh,params);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
       if(list!=null&&!list.isEmpty()){
            ids.put(equity, list.get(0).getId());
            return list.get(0);
       }
       return null;

       
        
    }
    public Timestamp getLastTimeStamp(String equity){
        try {
            String query="select max(time) from realtime where equity='"+equity+"'";
           
            ScalarHandler rsh = new ScalarHandler();
            return (Timestamp) runner.query(query, rsh);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        
    }
    public Timestamp getMaxtimeNoted(){
        try {
            String query="select max(time) from realtime ";
            ScalarHandler rsh = new ScalarHandler();
            return (Timestamp) runner.query(query, rsh);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    @Override
    public boolean hasNext(String equity) {
        if(equity.startsWith(".NSE")||equity.startsWith("ADANIENT")) return false;
        return true;
    }
        public List<String> getAllEquity() throws SQLException{
        String sql = "select distinct equity from realtime";
        QueryRunner run = new QueryRunner( DatabaseProperty.getDataSource() );
        ResultSetHandler rsh = new ColumnListHandler();
       return (List<String>) run.query(sql, rsh);
    }
    List<String> list;
    @Override
    public void run() {

        try {
            this.getAllEquity().stream()
                    .forEach(v->{
                        RediffParser parser = new RediffParser(v, "2015-09-23");
                        Thread thread = new Thread(parser);
                        thread.setName(v);
                        thread.start();
                    });
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        
    
    }
    public ReatimeDBReader(){
        try {
          list = new EquityDao().getAllEquity();
        
        } catch (SQLException ex) {
           throw new RuntimeException(ex);
        }
    }
    public void startThread(){
          Thread t = new Thread(this);
          t.start();
    }

    private boolean tryAgain(String eq,List<RealtimeTicker> list) {
        if(list!=null&&!list.isEmpty()){
            return false;
        }
        Timestamp maxTime = getMaxtimeNoted();
        if(maxTime==null){
            return true;
            //probably the starting phase
        }
        Timestamp lastUpdated = getLastTimeStamp(eq);
        if(lastUpdated==null){
            throw new DataStreamLostException();
        }
        int minsReached = Util.getMinutes(Date.from(maxTime.toInstant()));
        int minUpdated = Util.getMinutes(Date.from(lastUpdated.toInstant()));
        if(minsReached-minUpdated>2){
            throw new DataStreamLostException();
        }
        return true;
    }
}
