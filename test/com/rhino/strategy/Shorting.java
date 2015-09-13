/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhino.strategy;

import com.rhino.data.Equity;
import com.rhino.data.Ticker;
import com.rhino.data.db.EquityDao;
import com.rhino.data.history.util.Util;
import com.rhino.service.Portfolio;
import com.rhino.service.StrategyManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.Test;

/**
 *
 * @author arshed
 */
public class Shorting implements Strategy{

     private static final float TARGET=  2f;
    private List<Equity> getAll() throws SQLException{
        String from = "2015-6-31";
        String to="2015-08-25";
        List<Equity> loaded = new ArrayList<>();
        for(String name:new EquityDao().getAllEquity("FUTURE")){
            loaded.add(Equity.loadEquity(name, from, to));
        }
        return loaded;
    }
   
    @Test
    public void runStrategy() throws SQLException {
        Strategy strategy = new Shorting();
        StrategyManager manager = new StrategyManager();
        Portfolio pf = new Portfolio();
        pf.setStrategy(strategy);
        manager.addStock(getAll());
      // manager.addStock(Equity.loadEquity("CEATLTD", "2015-03-1", "2015-05-17"));
        manager.setPortfolio(pf);
        manager.run();
    }
   @Override
    public boolean canEnter(Equity equity) {
        float last9 = equity.simpleMovingAverage(9);
        float currentPrice=equity.getTicker().getLowPrice();
        Ticker twoDayBack=equity.getTickerBeforeNDays(3);
        float highPrice = equity.getHighPrice(8, false).getClosePrice();
        if(highPrice!=twoDayBack.getClosePrice()){
            return false;
        }
        float priceDeclineFromAverge = Util.findPercentageChange(last9,currentPrice);
        if(priceDeclineFromAverge>3){
            return true;
        }else{
            return false;
        }
            
     
    }

     private boolean doesPriceExceedsTarget(Ticker entryDay,Ticker currentDay){
        float currentLowPrice = currentDay.getLowPrice();
        float boughtPrice = entryDay.getClosePrice();
        float change = Util.findPercentageChange(currentLowPrice,boughtPrice);
        if(change<TARGET*-1){
            return true;
        }
        return false;
    }
    public boolean canExit(Equity equity, int entryPointer) {
        Ticker boughtDayTicker = equity.getList().get(entryPointer);
        boolean targetAcieved = doesPriceExceedsTarget(boughtDayTicker,equity.getTicker() );
        if(targetAcieved){
            return true;
        }
        int holdPeriod = equity.getPointer() - entryPointer;
        if(holdPeriod>=1) return true;
        return false;
    }

    @Override
    public int leastDataRequired() {
        //minimum data required to start with
        //In this case we are calculating "equity.simpleMovingAverage(9)", which mean atleast 9 days of data is required to start with
        return 10;
    }

    @Override
    public float computeValue() {
        //todo, this is to plot graph if required
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public StrategyType getStrategyType() {
        //buy or sell
        return StrategyType.SHORT;
    }



    @Override
    public float getOpenPrice(Equity equity, int entryPointer) {
        //Price of stock when position is opened(bought).
       return equity.getList().get(entryPointer).getClosePrice();
    }

    @Override
    public float getClosePrice(Equity equity, int entryPointer) {
     Ticker boughtDayTicker = equity.getList().get(entryPointer);
        boolean targetAcieved = doesPriceExceedsTarget(boughtDayTicker,equity.getTicker());
        if(targetAcieved){
            /*
            high price is greater than target percentage, hence sell can be triggered at target price
            for example:
            buy price is 100, target is 4 and current highestprice is 106
            then one can sell at 104; Hence return 104 as the position close price
            */
            return Util.findTargetPrice(boughtDayTicker.getClosePrice(), -1*TARGET);
        }
        //if target is not acheived, then the position will be closed only if the hold period exceeds 4 days
        // hence return current day close price
        return equity.getTicker().getClosePrice();
    }
    
 
    private boolean doesPriceExceedsTarget(Ticker currentDay,Ticker boughtDay,float target){
        
        float currentMax = currentDay.getLowPrice();
        float boughtPrice = boughtDay.getClosePrice();
        float change = Util.findPercentageChange(currentMax,boughtPrice);
        if(change<target){
            return true;
        }
        return false;
    }
   
    
}
