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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arsh
 */
public class Horizon implements Strategy{
    List<String> list = new ArrayList<>();
    List<String> bought = new ArrayList<>();
    @Override
    public Trade openPosition(Equity eq, Index index) {
        float last=0f;
        float max=Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        //System.out.println("Analysing date "+eq.intraday.getCurrentTicker().getDate());
       // if(eq.eod.size()<8) return null;
         last = eq.getPrevsPrice();
        for(Ticker tick:eq.eod){
           
               float close = Util.findPercentageChange(tick.getClosePrice(), last);
               float high = Util.findPercentageChange(tick.getHighPrice(), last);
               float low = Util.findPercentageChange(tick.getLowPrice(), last);
               System.out.println(tick.getDate()+"\t"+close+"\t"+high+"\t"+low);
            last = tick.getClosePrice();
        }
        
        return null;
    }
    
    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
        float stopLoss = Util.findTargetStopLoss(executedTrade.executedPrice, .5f);
        if(currentPrice<stopLoss){
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
       return true;
    }
    
}
