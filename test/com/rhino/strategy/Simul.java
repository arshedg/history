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
public class Simul implements Strategy{

    private static final float TARGET=2.5f;
    private List<Equity> getAll() throws SQLException{
        String from = "2009-04-1";
        String to="2015-05-25";
        List<Equity> loaded = new ArrayList<>();
        for(String name:new EquityDao().getAllEquity("A2")){
            loaded.add(Equity.loadEquity(name, from, to));
        }
        return loaded;
    }
    private List<Equity> getProbable(){
        String from = "2015-04-7";
        String to="2015-05-18";
        List<Equity> loaded = new ArrayList<>();
        Scanner scan = new Scanner(ClassLoader.getSystemResourceAsStream("probable"));
        while(scan.hasNext()){
            String data=scan.next().trim();
            if(data.length()==1) continue;
            loaded.add(Equity.loadEquity(data, from, to));
        }
        return loaded;
    }
    @Test
    public void runStrategy() throws SQLException {
        Strategy strategy = new Simul();
        StrategyManager manager = new StrategyManager();
        Portfolio pf = new Portfolio();
        pf.setStrategy(strategy);
        manager.addStock(getAll());
        //manager.addStock(Equity.loadEquity("INFY", "2014-04-1", "2015-05-17"));
        manager.setPortfolio(pf);
        manager.run();
    }
    /*
    Buy Stocks when the canEnter condition satisfies
    */
    @Override
    public boolean canEnter(Equity equity) {
        
        float last9 = equity.simpleMovingAverage(9);
        float currentPrice = equity.getTicker().getClosePrice();
        float change = Util.findPercentageChange(last9,currentPrice);
        float volumeAvg = equity.simpleAverageVolume(9);
        float currentVolume = equity.getTicker().getVolume();
        float volDecline = Util.findPercentageChange(volumeAvg,currentVolume);
        boolean canEnter =  change>-.5&&change<1.5&&volDecline>500; 
       // System.out.println("Date:"+equity.getTicker().getDate()+" sma:"+last9+" current price"+currentPrice+" percentage chage:"+change+" voulume average:"+volumeAvg+" current vol:"+currentVolume+"vol decline"+volDecline);
        if(canEnter){
           // System.out.println("Bought "+equity.getId()+" on"+equity.getTicker().getDate()+" at price:"+equity.getTicker().getClosePrice());
        }
        return canEnter;
    }
    /*
    Close the position when this method return true and continue holding if it returns false
    */
    @Override
    public boolean canExit(Equity equity, int entryPointer) {
        Ticker boughtDayTicker = equity.getList().get(entryPointer);
        boolean targetAcieved = doesPriceExceedsTarget(equity.getTicker(), boughtDayTicker);
        if(targetAcieved){
            return true;
        }
        int holdPeriod = equity.getPointer() - entryPointer;
        if(holdPeriod>5) return true;
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
        return StrategyType.LONG;
    }



    @Override
    public float getOpenPrice(Equity equity, int entryPointer) {
        //Price of stock when position is opened(bought).
       return equity.getList().get(entryPointer).getClosePrice();
    }

    @Override
    public float getClosePrice(Equity equity, int entryPointer) {
        Ticker boughtDayTicker = equity.getList().get(entryPointer);
        boolean targetAcieved = doesPriceExceedsTarget(equity.getTicker(), boughtDayTicker);
        if(targetAcieved){
            /*
            high price is greater than target percentage, hence sell can be triggered at target price
            for example:
            buy price is 100, target is 4 and current highestprice is 106
            then one can sell at 104; Hence return 104 as the position close price
            */
            return Util.findTargetPrice(boughtDayTicker.getClosePrice(), TARGET);
        }
        //if target is not acheived, then the position will be closed only if the hold period exceeds 4 days
        // hence return current day close price
        return equity.getTicker().getClosePrice();
    }
    
    private boolean doesPriceExceedsTarget(Ticker currentDay,Ticker boughtDay){
        float currentMax = currentDay.getHighPrice();
        float boughtPrice = boughtDay.getClosePrice();
        float change = Util.findPercentageChange(currentMax,boughtPrice);
        if(change>TARGET){
            return true;
        }
        return false;
    }
    
    
}
