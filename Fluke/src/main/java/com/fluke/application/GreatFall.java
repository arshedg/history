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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arsh
 */
public class GreatFall implements Strategy{

    List<String> watch = new ArrayList<>();
    @Override
    public Trade openPosition(Equity eq, Index index) {
       if(eq.intraday.size()<45) return null;
       if(watch.contains(eq.getName())){
           return canShort(eq,index);
       }
       float avg = eq.intraday.simpleMovingAverage(30);
       float prevsPrice = eq.getPrevsPrice();
       float change = Util.findPercentageChange(avg, prevsPrice);
       if(change>1.5){
           watch.add(eq.getName());
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

    private Trade canShort(Equity eq, Index index) {
        if(Util.getMinutes(eq.intraday.getCurrentTicker().getDate())>14*60) return null;
        float avg = eq.intraday.simpleMovingAverage(3);
        float prevs = eq.getPrevsPrice();
        float change = Util.findPercentageChange(avg, prevs);
        if(change<.3&&getNiftyChange(index)<-.2){
            Trade trade = new Trade();
            trade.openPrice = eq.intraday.getCurrentTicker().getClosePrice();
            watch.remove(eq.getName());
            return trade;
        }
        return null;
    }
     private float getNiftyChange(Index index){
        int niftyPosition = index.nifty.intraday.size();
        if(niftyPosition<10) return 0;
        float avgNifty = (float) index.nifty.intraday.subList(niftyPosition-10, niftyPosition).stream().mapToDouble(n->n.getClosePrice()).average().getAsDouble();
        float prevsNifty = index.nifty.getPrevsPrice();
        
        return Util.findPercentageChange(avgNifty, prevsNifty);
    }
    
}
