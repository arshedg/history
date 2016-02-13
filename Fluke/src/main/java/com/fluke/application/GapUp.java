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
import java.util.Date;

/**
 *
 * @author arsh
 */
public class GapUp implements Strategy{

    float gapDiff=4f;
    float target = .4f;
    float allowedLoss = 1f;
    @Override
    public Trade openPosition(Equity eq, Index index) {
        Ticker current = eq.intraday.getCurrentTicker();
        Date time =current.getDate();
        boolean isBefore918 = Util.getMinutes(time)<9*60+18;
        if(eq.getPrevsPrice()==null) return null;
        if(isBefore918){
           
           float priceDiff = Util.findPercentageChange(current.getClosePrice(), eq.getPrevsPrice());
           if(priceDiff>gapDiff){
               Trade trade = new Trade();
               trade.target = Util.findTargetStopLoss(current.getClosePrice(), target);
               return trade;
           }
        }
        return null;
    }

    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
        float stopLoss = Util.findTargetPrice(executedTrade.executedPrice, allowedLoss);
        if(currentPrice>stopLoss){
            Trade trade = new Trade();
            trade.openPrice = stopLoss;
            trade.isAtMarketPrice = false;
            return trade;
        }
        return null;
    }

    @Override
    public boolean cancelPosition(Equity eq, Index index, int entryPoint, Trade placeTrade) {
        return false;
    }

    @Override
    public boolean isLong() {
       return false; 
    }
    
}
