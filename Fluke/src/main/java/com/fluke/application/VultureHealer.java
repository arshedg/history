/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.application;

import com.fluke.model.Equity;
import com.fluke.model.Index;
import com.fluke.model.Trade;
import com.fluke.strategy.Strategy;
import com.fluke.util.Util;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arsh
 */
public class VultureHealer implements Strategy{
    public static Map<String,Float> order = new HashMap<>();
    Map<String,Float> exec = new HashMap<>();
    @Override
    public Trade openPosition(Equity eq, Index index) {
        if(eq.intraday.size()<5){
            order = new HashMap<>();
        }
        int minutes = Util.getMinutes(eq.intraday.getCurrentTicker().getDate());
        if(minutes<12*60+30&&order.containsKey(eq.getName())){
            Trade trade= new Trade();
            trade.isAtMarketPrice = false;
            trade.openPrice = eq.intraday.getCurrentTicker().getClosePrice();
            trade.target = Util.findTargetPrice(trade.openPrice, .6f);
            float sl = order.get(eq.getName());
            trade.exitPrice = sl;
            exec.put(eq.getName(), sl);
            order.remove(eq.getName());
            return trade;
        }
        return null;
    }

    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        float stopLoss = exec.get(eq.getName());
        if(stopLoss>eq.intraday.getCurrentTicker().getClosePrice()){
            Trade trade = new Trade();
            trade.isAtMarketPrice = false;
            trade.openPrice = stopLoss;
            return trade;
        }
        int minutes = Util.getMinutes(eq.intraday.getCurrentTicker().getDate());
        if(minutes>13*60){
            Trade trade = new Trade();
            trade.isAtMarketPrice = false;
            trade.openPrice = eq.intraday.getCurrentTicker().getClosePrice();
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

