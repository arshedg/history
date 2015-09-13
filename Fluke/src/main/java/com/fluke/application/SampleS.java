/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.application;

import com.fluke.model.Equity;
import com.fluke.model.Index;
import com.fluke.model.Trade;
import com.fluke.model.ticker.Ticker;
import com.fluke.strategy.Strategy;
import com.fluke.util.Util;

/**
 *
 * @author arshed
 */
public class SampleS implements Strategy{

    @Override
    public Trade openPosition(Equity eq, Index index) {
        if(eq.eod.size()<4) return null;
        Ticker older = eq.eod.getTickBeforeNdays(2);
        Ticker prevsDay = eq.eod.getTickBeforeNdays(1);
        Ticker currentDay = eq.intraday.getCurrentTicker();
        if(older.getClosePrice()>prevsDay.getClosePrice()&&
           currentDay.getHighPrice()>prevsDay.getClosePrice()&&
            eq.intraday.getCurrentVolume()>prevsDay.getVolume())
        {
            Trade trade = new Trade();
            trade.isAtMarketPrice=true;
            return trade;
        }
        
        
        
        return null;
    }

    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint) {
        Ticker open = eq.intraday.get(entryPoint);
        if(Util.findPercentageChange(eq.intraday.getCurrentTicker().getHighPrice(),open.getHighPrice())>.8){
            Trade trade = new Trade();
            trade.isAtMarketPrice=false;
            trade.openPrice=eq.intraday.getCurrentTicker().getHighPrice();
            return trade;
        }else if(Util.findPercentageChange(open.getHighPrice(),eq.intraday.getCurrentTicker().getHighPrice())<-.5){
            Trade trade = new Trade();
            trade.isAtMarketPrice=true;
            return trade;
        }
        return null;
    }

    @Override
    public boolean isLong() {
        return true;
    }
    
}
