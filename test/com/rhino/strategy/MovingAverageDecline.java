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
public class MovingAverageDecline implements Strategy{

    protected List<Equity> getAll() throws SQLException{
         String from = "2010-1-1";
        String to="2015-8-1";
        List<Equity> loaded = new ArrayList<>();
        for(String name:new EquityDao().getAllEquity("FUTURE")){
            loaded.add(Equity.loadEquity(name, from, to));
        }
        return loaded;
    }

    @Test
    public void runStrategy() throws SQLException {
        Strategy strategy = this;
        StrategyManager manager = new StrategyManager();
        Portfolio pf = new Portfolio();
        pf.setStrategy(strategy);
        manager.addStock(getAll());
     //  manager.addStock(Equity.loadEquity("IDEA", " 2015-01-5", "2015-1-19"));
        manager.setPortfolio(pf);
        manager.run();
    }
    float getAvgPrice(Ticker tick) {
        float low = tick.getLowPrice();
        float high = tick.getHighPrice();
        float close = tick.getClosePrice();
        float avgPrice = (low + high + (2 * close)) / 4;
        return avgPrice;
    }

    private boolean trigger(Ticker tick, Ticker prevs) {
       if(tick.getClosePrice()>prevs.getClosePrice()||tick.getClosePrice()>tick.getOpenPrice()) return false;
       float hd = tick.getHighPrice()-prevs.getClosePrice();
       float ld = prevs.getClosePrice()-tick.getLowPrice();
       if(Math.abs(Util.findPercentageChange(hd, ld))>50){
           if(hd<ld){
               return true;
              // System.out.println("short sell on "+tick.getDate());
           }else{
               //System.out.println("Buy on "+tick.getDate());
           }
       }
       return false;
      //  System.out.println(" ratio of hd to ld"+Util.findPercentageChange(hd, ld));
    }

    @Override
    public boolean canEnter(Equity equity) {
        Ticker tick = equity.getTicker();
        Ticker prevs = equity.getTickerBeforeNDays(1);
        float volChange = Util.findPercentageChange(tick.getVolume(), prevs.getVolume());
        if(volChange>140){
               float priceChange = Util.findPercentageChange(tick.getClosePrice(), prevs.getClosePrice());
               if(Math.abs(priceChange)<1){
                  return trigger(tick,prevs);
               }
        }
        return false;
    }

    @Override
    public boolean canExit(Equity equity, int entryPointer) {
        Ticker tick = equity.getTicker();
        Ticker prevs = equity.getList().get(entryPointer);
        if(tick.getVolume()>prevs.getVolume()||Util.findPercentageChange(prevs.getVolume(), tick.getVolume())<100){
            return true;
        }
        return equity.getPointer()-entryPointer>0;
    }

    @Override
    public int leastDataRequired() {
        return 2;
    }

    @Override
    public float computeValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StrategyType getStrategyType() {
        return StrategyType.SHORT;
    }

    @Override
    public float getOpenPrice(Equity equity, int entryPointer) {
        return equity.getList().get(entryPointer).getClosePrice();
    }

    @Override
    public float getClosePrice(Equity equity, int entryPointer) {
        return equity.getTicker().getClosePrice();
    }

}
