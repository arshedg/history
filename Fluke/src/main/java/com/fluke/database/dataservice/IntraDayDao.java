/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.database.dataservice;

import com.fluke.data.intraday.IntradayDetails;
import com.fluke.model.ticker.IntradayTicker;
import com.fluke.data.intraday.Series;
import com.fluke.database.DatabaseProperty;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author arshed
 */
public class IntraDayDao {
    
    private static String sql="insert into intraday (equity,openPrice,closePrice,highPrice,lowPrice,volume,time,date) values(?,?,?,?,?,?,?,?)";

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
        QueryRunner runner = new QueryRunner( DatabaseProperty.getDataSource() );
        try {
            
            runner.update(sql, id, ticker.getOpenPrice(),ticker.getClosePrice(),ticker.getHighPrice(),ticker.getLowPrice(),ticker.getVolume(),ticker.getTime());
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
          
            runner.update(sql, id, series.getOpen(),series.getClose(),series.getHigh(),series.getLow(),series.getVolume(),series.getTimestamp(),"2015-9-21");
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
}
