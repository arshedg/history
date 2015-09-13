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
import org.junit.Test;

/**
 *
 * @author arshed
 */
public class BackFromDeath implements Strategy {

   
       private List<Equity> getAll() throws SQLException{
        String from = "2010-01-5";
        String to="2015-12-25";
        List<Equity> loaded = new ArrayList<>();
        for(String name:new EquityDao().getAllEquity("A2")){
            loaded.add(Equity.loadEquity(name, from, to));
        }
        return loaded;
    }

    @Test
    public void runStrategy() throws SQLException {
        Strategy strategy = new BackFromDeath();
        StrategyManager manager = new StrategyManager();
        Portfolio pf = new Portfolio();
        pf.setStrategy(strategy);
        manager.addStock(getAll());
       // manager.addStock(Equity.loadEquity("INFY", "2010-04-1", "2015-05-17"));
        manager.setPortfolio(pf);
        manager.run();
    }
    
    
    @Override
    public boolean canEnter(Equity equity) {
        float day1 = equity.getTickerBeforeNDays(2).getClosePrice();
        float day2 = equity.getTickerBeforeNDays(1).getClosePrice();
        float profit = Util.findPercentageChange(day2, day1);
        if(profit<-4){
            float currentClose= equity.getTicker().getClosePrice();
            float newProfit = Util.findPercentageChange(currentClose, day2);
            if(newProfit<2&&newProfit>-.5){
                float pV = equity.getTickerBeforeNDays(1).getVolume();
               // return true;
                return 100<Util.findPercentageChange(equity.getTicker().getVolume(), pV);
            }    
        }
        
        return false;
    }

    @Override
    public boolean canExit(Equity equity, int entryPointer) {
        int holdDays = equity.getPointer()-entryPointer;
        if(holdDays<1) return false;
        if(holdDays>=2) return true;
         Ticker boughtDayTicker = equity.getList().get(entryPointer);
         return doesPriceExceedsTarget(equity.getTicker(), boughtDayTicker);
    }
 static final float TARGET=3f;
    @Override
    public int leastDataRequired() {
        return 4;
    }

    @Override
    public float computeValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StrategyType getStrategyType() {
        return StrategyType.LONG;
    }

    @Override
    public float getOpenPrice(Equity equity, int entryPointer) {
        return equity.getList().get(entryPointer).getClosePrice();
    }

    @Override
    public float getClosePrice(Equity equity, int entryPointer) {
         Ticker boughtDayTicker = equity.getList().get(entryPointer);
         boolean targetAcieved = doesPriceExceedsTarget(equity.getTicker(), boughtDayTicker);
         if(targetAcieved){
              return Util.findTargetPrice(boughtDayTicker.getClosePrice(), TARGET); 
         }
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
