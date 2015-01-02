/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.db;

import com.rhino.data.Ticker;
import com.rhino.data.history.util.Util;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author agulshan
 */
public class TickerDao {
    
    public Ticker getTicker(String equity,Date date) throws SQLException{    
        List<Ticker> tickers = this.getTickers(equity, date, date);
        if(tickers.size()>0){
            return tickers.get(0);
        }
        else{
            return null;
        }
    }
    public List<Ticker> getTickers(String equity) throws SQLException{
        try {
            return this.getTickers(equity,Util.getDate("1900-1-1") ,Calendar.getInstance().getTime());
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    public List<Ticker> getTickers(String equity,Date fromDate,Date toDate) throws SQLException{
        QueryRunner run = new QueryRunner( DataSourceFactory.getDataSource() );
        String sql = "select * from ticker  where equity = ? and date between ? and ? order by date";
        String from = Util.getDate(fromDate);
        String to =   Util.getDate(toDate);
        Object[] params = new Object[]{equity, from,to};
        ResultSetHandler rsh = new BeanListHandler(Ticker.class);
        return (List<Ticker>)run.query(sql, rsh,params); 
    }
    public int insertTicker(String equity,Ticker ticker) throws SQLException{
        String sql="insert into ticker (equity,openPrice,closePrice,highPrice,lowPrice,adjustedClose,volume,date) values(?,?,?,?,?,?,?,?)";
        String date = Util.getDate(ticker.getDate());
        Object[] params = new Object[]{equity,ticker.getOpenPrice(),ticker.getClosePrice(),ticker.getHighPrice(),
                            ticker.getLowPrice(),ticker.getAdjustedClose(),ticker.getVolume(),date};
        QueryRunner run = new QueryRunner( DataSourceFactory.getDataSource() );
        return run.update(sql, params);
    }
    
    public int deleteTicker(String equity) throws SQLException{
        String sql = "delete from ticker where equity=?";
        QueryRunner run = new QueryRunner( DataSourceFactory.getDataSource() );
        return run.update(sql, equity);
    }
}
