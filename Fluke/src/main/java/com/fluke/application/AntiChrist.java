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
import com.fluke.strategy.Strategy;
import com.fluke.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author arsh
 */
public class AntiChrist implements Strategy{

    List<String> watch = new ArrayList<>();
    List<String> ignore = new ArrayList<>();
    Map<String,Integer> duration = new HashMap<>();

    public AntiChrist() {
    }
    private boolean minDataAcquired(Equity eq){
        int time = Util.getMinutes(eq.intraday.getCurrentTicker().getDate());
        if(time<11*60+52||time>14*60) return false;
        return true;
                
    }

    @Override
    public Trade openPosition(Equity eq, Index index) {
        if(ignore.contains(eq.getName())) return null;
        if(!minDataAcquired(eq)) return null;
        
        Equity nifty = index.nifty;
        if(watch.contains(eq.getName())) return canBuy(eq,index);
       // System.out.println(eq.getName());
        if(nifty.intraday.percentile()<8&&eq.intraday.percentile()>90){
//            System.out.println("Added to watch "+eq.getName()+" at"+eq.intraday.getCurrentTicker().getDate());
//            System.out.println("NSE at "+eq.getName()+" at"+nifty.intraday.percentile());
//            System.out.println("Equity at "+eq.getName()+" at"+eq.intraday.percentile());
            watch.add(eq.getName());
        }
        return null;
        
    }
    int getDuration(Equity eq){
        Integer time = duration.get(eq.getName());
        if(time==null){
            time = 0;
        }else{
            duration.put(eq.getName(), time+1);
        }
        return time;
    }
    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
       // if(getDuration(eq)<3) return null;
        float sma1 = eq.intraday.simpleMovingAverage(1);
        float sma20 =  eq.intraday.simpleMovingAverage(10);
        boolean surpassed = Util.findPercentageChange(sma20, sma1)>.07;
        float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
        float stopLoss = Util.findTargetStopLoss(executedTrade.executedPrice, .5f);
        if(currentPrice<stopLoss){
             Trade trade = new Trade();
             trade.openPrice = stopLoss;
             return trade;
        }
//        if(surpassed){
//            duration.remove(eq.getName());
//           Trade trade = new Trade();
//           trade.openPrice = eq.intraday.getCurrentTicker().getClosePrice();
//           trade.target = Util.findTargetPrice(trade.openPrice, .2f);
//           trade.isAtMarketPrice = false;
//           return trade;
//        }
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

    private Trade canBuy(Equity eq, Index index) {
        float change = Util.findPercentageChange(eq.getIntraday().getCurrentTicker().getClosePrice(),eq.getPrevsPrice());
        if(change>.6){
            return null;
        }
       if(isEndTriangle(index.nifty.intraday)){
           Trade trade = new Trade();
           trade.openPrice = eq.intraday.getCurrentTicker().getClosePrice();
           trade.target = Util.findTargetPrice(trade.openPrice, .8f);
           watch.remove(eq.getName());
           ignore.add(eq.getName());
           return trade;
       }
       return null;
    }
    boolean isEndTriangle(TickerList nifty){
        return nifty.simpleMovingAverage(4)>nifty.simpleMovingAverage(12);
    }
}
