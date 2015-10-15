/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.application;

import com.fluke.model.Equity;
import com.fluke.model.Index;
import com.fluke.model.TickerList;
import com.fluke.model.Trade;
import com.fluke.model.ticker.Ticker;
import com.fluke.strategy.Strategy;
import com.fluke.util.Util;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author arsh
 */
public class DoubleTop implements Strategy{

    Map<String,Ticker> watch = new HashMap<>();
    @Override
    public Trade openPosition(Equity eq, Index index) {
       String name = eq.getName();
       if(watch.containsKey(name)){
           return canEnter(eq.intraday);
       }
       TickerList intra = eq.intraday;
       float highest = intra.getHighPrice().getHighPrice();
       float hVolume = intra.getHighestVolume();
       Ticker current = intra.getCurrentTicker();
       float requiredVolume = Util.findTargetStopLoss(hVolume, .2f);//ateast as high as .8 of top
       if(current.getVolume()>requiredVolume&&highest<=current.getHighPrice()){
           watch.put(name, current);
       }
       return null;
    }

    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cancelPosition(Equity eq, Index index, int entryPoint, Trade placeTrade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLong() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Trade canEnter(TickerList intraday) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
