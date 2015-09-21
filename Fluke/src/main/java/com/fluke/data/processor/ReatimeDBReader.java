/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.data.processor;

import com.fluke.database.DatabaseProperty;
import com.fluke.model.ticker.IntradayTicker;
import com.fluke.model.ticker.Ticker;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import com.fluke.model.RealtimeTicker;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author arsh
 */
public class ReatimeDBReader implements TickerDataSource{

    QueryRunner runner = new QueryRunner(DatabaseProperty.getDataSource());
    private String query="select * from realtime   where equity=? and id > ? order by id asc limit 1";
    Map<String,Long> ids = new HashMap<>();
    @Override
    public Ticker getNextTicker(String equity) {
        Long id = ids.get(equity);
        if(equity.startsWith(".NSE")||equity.startsWith("ADANIENT")) return null;
        if(id==null||id==0){
            id = 0l;
        }
//        System.out.println("equity:"+equity);
         List<RealtimeTicker> list = null;
        do{
        Object[] params = new Object[]{equity, id};
        ResultSetHandler rsh = new BeanListHandler(RealtimeTicker.class); 
            try {
                list = (List<RealtimeTicker>)runner.query(query, rsh,params);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }while(list==null||list.isEmpty());
  //      System.out.println("equity new id:"+list.get(0).getId());
        ids.put(equity, list.get(0).getId());
        return list.get(0);
        
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
    
}
