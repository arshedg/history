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

/**
 *
 * @author arsh
 */
public class S1 implements Strategy{

    boolean infy=true;
    @Override
    public Trade openPosition(Equity eq, Index index) {
        if(Util.getMinutes(eq.intraday.getCurrentTicker().getDate())<13*60+28) return null;
        if(infy&&eq.getName().equals("INFY")){
            float price =1101.60f;// eq.intraday.getCurrentTicker().getClosePrice();
            Trade trade = new Trade();
            trade.openPrice = price;
            trade.isAtMarketPrice = false;
            trade.target = Util.findTargetStopLoss(price, .07f);
            trade.exitPrice = Util.findTargetPrice(price, .07f);
            infy=false;
            return trade;
        }
        return null;
    }

    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
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
