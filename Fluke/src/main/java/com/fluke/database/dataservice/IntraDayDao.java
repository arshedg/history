/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.database.dataservice;

import com.fluke.data.intraday.IntradayDetails;
import com.fluke.model.ticker.IntradayTicker;
import com.fluke.data.intraday.Series;
import com.fluke.data.processor.ReatimeDBReader;
import com.fluke.database.DatabaseProperty;
import com.fluke.model.ticker.Ticker;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

/**
 *
 * @author arshed
 */
public class IntraDayDao {
    
    private static String sql="insert into intraday (equity,openPrice,closePrice,highPrice,lowPrice,volume,time,date) values(?,?,?,?,?,?,?,Date(?))";

      QueryRunner runner = new QueryRunner( DatabaseProperty.getDataSource() );
    public void insertRealtimeData(IntradayDetails data){
      
        String id = data.getMeta().getId().toUpperCase();
        Timestamp time = new ReatimeDBReader().getLastTimeStamp(id);
        try{
            if(time==null){
                addRealtimeValues(runner, data.getSeries(), id);
            }else
            {
                addRealtimeValues(runner,data.getSeries().stream().filter(d->d.getTimestamp().after(time)).collect(Collectors.toList()),id);
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }
    public Float getPrevsDayPrice(String equity,String date){
        String query =" select closePrice from EOD where equity=? and date < ? order by date desc limit 1";
        ScalarHandler rsh = new ScalarHandler();
        try {
            //System.out.println(""+equity);
           return (Float) runner.query(query, rsh,equity,date);

        } catch (Throwable ex) {
            throw new RuntimeException();
        }
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
    public void insert(IntradayDetails data){ 
        QueryRunner runner = new QueryRunner( DatabaseProperty.getDataSource() );
        try {
            List<Series> values = data.getSeries();
            String id = data.getMeta().getId().toUpperCase();
            addValues(runner,values,id);
        } catch (SQLException ex) {
           throw new RuntimeException(ex);
        }
    }
    public void insert(String id,IntradayTicker ticker){

        String query="insert into realtime (equity,openPrice,closePrice,highPrice,lowPrice,volume,time,date) values(?,?,?,?,?,?,?,Date(?))";
        try {
            
            runner.update(query, id, ticker.getOpenPrice(),ticker.getClosePrice(),ticker.getHighPrice(),ticker.getLowPrice(),ticker.getVolume(),ticker.getTime(),ticker.getTime());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void insertBatch(String id,List<IntradayTicker> tickers){
        QueryRunner runner = new QueryRunner( DatabaseProperty.getDataSource() );
        Object params[][] = new Object[tickers.size()][7];
        int count=0;
        for(IntradayTicker ticker:tickers){
            params[count] = new Object[7];
            params[count][0] = id;
            params[count][1] = ticker.getOpenPrice();
            params[count][2] = ticker.getClosePrice();
            params[count][3] = ticker.getHighPrice();
            params[count][4] = ticker.getLowPrice();
            params[count][5] = ticker.getVolume();
            params[count][6] = ticker.getTime();
            count++;
        }
        try {
            runner.batch( sql, params);
        } catch (SQLException ex) {
           throw new RuntimeException(ex);
        }
    }
    private void addValues(QueryRunner runner, List<Series> values, String id) throws SQLException {
          if(id.contains("^")){
                id=id.replace("^", ".");
            }
        for(Series series:values){
          
            runner.update(sql, id, series.getOpen(),series.getClose(),series.getHigh(),series.getLow(),series.getVolume(),series.getTimestamp(),series.getTimestamp());
        }
    }
    public void insertRealtime(String id,IntradayTicker ticker){
        String insert="insert into realtime (equity,openPrice,closePrice,highPrice,lowPrice,volume,time,date) values(?,?,?,?,?,?,?,Date(?))"; 
        try {
            runner.update(insert, id, ticker.getOpenPrice(),ticker.getClosePrice(),ticker.getHighPrice(),ticker.getLowPrice(),ticker.getVolume(),ticker.getTime(),ticker.getTime());
        } catch (SQLException ex) {
           throw new RuntimeException(ex);
        }
    }
    private void addRealtimeValues(QueryRunner runner, List<Series> values, String id) throws SQLException {
        String insert="insert into realtime (equity,openPrice,closePrice,highPrice,lowPrice,volume,time,date) values(?,?,?,?,?,?,?,Date(?))";  
        if(id.contains("^")){
                id=id.replace("^", ".");
            }
        for(Series series:values){
          
            runner.update(insert, id, series.getOpen(),series.getClose(),series.getHigh(),series.getLow(),series.getVolume(),series.getTimestamp(),series.getTimestamp());
        }
    }
    public List<IntradayTicker> getIntraday(String name,String date) {
        try {
            QueryRunner run = new QueryRunner( DatabaseProperty.getDataSource() );
            String select = "select * from intraday  where equity = ? and  date=?  order by time asc";
            Object[] params = new Object[]{name, date};
            ResultSetHandler rsh = new BeanListHandler(IntradayTicker.class); 
            return (List<IntradayTicker>)run.query(select, rsh,params);
        } catch (SQLException ex) {
           throw new RuntimeException(ex);
        }
    }

    public void deleteRealTime(String name) {
        try {
            runner.update("delete from realtime where equity='"+name+"'");
        } catch (SQLException ex) {
           throw new RuntimeException(ex);
        }
    }
}
