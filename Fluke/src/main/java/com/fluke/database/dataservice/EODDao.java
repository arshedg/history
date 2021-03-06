/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.database.dataservice;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.fluke.database.DatabaseProperty;
import com.fluke.model.ticker.EODTicker;
import com.fluke.util.Util;
import com.fluke.util.Util;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author agulshan
 */
public class EODDao {
    
    public EODTicker getEODTicker(String equity,Date date) throws SQLException{    
        List<EODTicker> tickers = this.getEODTickers(equity, date, date);
        if(tickers.size()>0){
            return tickers.get(0);
        }
        else{
            return null;
        }
    }
    public List<EODTicker> getEODTickers(String equity) throws SQLException{
        try {
            return this.getEODTickers(equity,Util.getDate("1900-1-1") ,Calendar.getInstance().getTime());
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    public Date getNextTradingDay(String date) throws SQLException{
        String sql="select cast(min(date) as date) from intraday where date > '"+date+"'";
        QueryRunner run = new QueryRunner( DatabaseProperty.getDataSource() );
        ResultSetHandler rsh = new ArrayHandler();
        Object[] query = (Object[]) run.query(sql, rsh);
        if(query==null||query.length==0){
            return null;
        }
        return (java.sql.Date)query[0];
    }
    public void deleteLastData(){
        try {
            String sql ="  delete from EOD where date =  (select maxD from (select max(date) as maxD from EOD as x) as y) ;";
            QueryRunner run = new QueryRunner( DatabaseProperty.getDataSource() );
            run.update(sql);
        } catch (SQLException ex) {
            Logger.getLogger(EODDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public List<EODTicker> getEODTickers(String equity,Date fromDate,Date toDate) throws SQLException{
        QueryRunner run = new QueryRunner( DatabaseProperty.getDataSource() );
        String sql = "select * from EOD  where equity = ? and date between '%s' and '%s' order by date";
        String from = Util.getDate(fromDate);
        String to =   Util.getDate(toDate);
        sql = String.format(sql, from,to);
        Object[] params = new Object[]{equity};
        ResultSetHandler rsh = new BeanListHandler(EODTicker.class);
        return filterDupicateDate((List<EODTicker>)run.query(sql, rsh,params)); 
    }
    private List<EODTicker> filterDupicateDate(List<EODTicker> tickers){
        List<EODTicker> array = new ArrayList<>();
        Date old = new Date();
        for(EODTicker ticker:tickers){
            if(!ticker.getDate().equals(old)){
                array.add(ticker);
            }
            old = ticker.getDate();
        }
        return array;
        
    }
    public int insertEODTicker(String equity,EODTicker ticker) throws SQLException{
        String sql="insert into EOD (equity,openPrice,closePrice,highPrice,lowPrice,adjustedClose,volume,date) values(?,?,?,?,?,?,?,?)";
        String date = Util.getDate(ticker.getDate());
        Object[] params = new Object[]{equity,ticker.getOpenPrice(),ticker.getClosePrice(),ticker.getHighPrice(),
                            ticker.getLowPrice(),ticker.getAdjustedClose(),ticker.getVolume(),date};
        QueryRunner run = new QueryRunner( DatabaseProperty.getDataSource() );
        int updates= run.update(sql, params);
        //run.update("delete from EOD where volume=0");
        return updates;
    }
    
    public int deleteEODTicker(String equity) throws SQLException{
        String sql = "delete from EOD where equity=?";
        QueryRunner run = new QueryRunner( DatabaseProperty.getDataSource() );
        return run.update(sql, equity);
    }
}
