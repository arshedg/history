/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.util;

import com.fluke.data.processor.IntradayTickerFromDataBase;
import com.fluke.data.processor.TickerDataSource;
import com.fluke.data.processor.TradeExecutor;
import com.fluke.database.dataservice.EODDao;
import com.fluke.database.dataservice.EquityDao;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arshed
 */
public class Configuration {
    public String startDate;
    public String endDate;
    public boolean isRealTime;
    public TickerDataSource dataSource;
    public List<String> equities;
    float  tradeSize;
    public TradeExecutor executor;
    public boolean extendEndDate(){
        Date date;
        try {
             date = new EODDao().getNextTradingDay(endDate);
            if(date==null) return false;
        } catch (SQLException ex) {
            return false;
        }
        this.endDate = date.toString();
        this.dataSource = new IntradayTickerFromDataBase(endDate);
        return true;
    }
    public static Configuration getDefaultConfiguration(TradeExecutor executor,String start,String end){
        Configuration config = new Configuration();
        config.startDate = start;
        config.endDate = end;
        config.dataSource = new IntradayTickerFromDataBase(end);
        try {
            config.equities = new EquityDao().getAllEquity();
        } catch (SQLException ex) {
            throw new RuntimeException("dao.get equities failed", ex);
        }
        config.executor = executor;
        return config;
    }
}
