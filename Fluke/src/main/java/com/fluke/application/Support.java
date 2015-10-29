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
public class Support implements Strategy{

    static List<String> support = new ArrayList<>();
    @Override
    public Trade openPosition(Equity eq, Index index) {
        Ticker t1 = eq.intraday.getCurrentTicker();
        if(support.contains(eq.getName())){
            support.remove(eq.getName());
            
            return Trade.limitTrade(t1.getClosePrice(), Util.findTargetStopLoss(t1.getClosePrice(), .8f),Util.findTargetPrice(t1.getClosePrice(), .5f));
        }
        return null;
    }

    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
        float stopLoss = Util.findTargetPrice(executedTrade.executedPrice, .5f);
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
