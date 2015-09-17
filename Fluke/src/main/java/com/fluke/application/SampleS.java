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
        if(eq.intraday.size()<50) return null;
       
        Ticker prevsDayIndex = index.nifty.eod.getTickBeforeNdays(1);
        Ticker currentIndexPrice = index.nifty.intraday.getCurrentTicker();
        if(prevsDayIndex.getClosePrice()>currentIndexPrice.getLowPrice()){
            //index on red
            return null;
        }
        float avgVolume = eq.intraday.simpleAverageVolume(15);
        float currentVol = eq.intraday.getCurrentTicker().getVolume();
        float volDecline = Util.findPercentageChange(avgVolume,currentVol);
        
        float last15 = eq.intraday.simpleMovingAverage(15);
        float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
        float change = Util.findPercentageChange(last15,currentPrice);
        boolean canEnter =  change>-1.5&&change<2&&volDecline>100; 
        if(canEnter){
            Trade trade = new Trade();
            trade.isAtMarketPrice=false;
            trade.openPrice = currentPrice;
            return trade;
        }
        return null;
    }

    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint,Trade executedTrade) {
        Ticker open = eq.intraday.get(entryPoint);
        if(Util.findPercentageChange(eq.intraday.getCurrentTicker().getHighPrice(),open.getClosePrice())>.5){
            Trade trade = new Trade();
            trade.isAtMarketPrice=false;
            trade.openPrice=eq.intraday.getCurrentTicker().getHighPrice();
            return trade;
        }else if(Util.findPercentageChange(open.getHighPrice(),eq.intraday.getCurrentTicker().getHighPrice())>1){
            Trade trade = new Trade();
            trade.isAtMarketPrice=true;
            trade.openPrice=eq.intraday.getCurrentTicker().getLowPrice();
            return trade;
        }
        return null;
    }

    @Override
    public boolean isLong() {
        return true;
    }

    @Override
    public boolean cancelPosition(Equity eq, Index index, int entryPoint, Trade placeTrade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
