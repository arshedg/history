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
public class VPattern implements Strategy{

    Map<String,Float> watch = new HashMap<>();
    Map<String,Float> toBuy = new HashMap<>(); 
    @Override
    public Trade openPosition(Equity eq, Index index) {
        if(watch.containsKey(eq.getName())){
            return isV(eq);
        }
        TickerList intra = eq.intraday;
        
        if(intra.size()<20) return null;
        float open = intra.get(0).getHighPrice();
        Ticker current = intra.getCurrentTicker();
      //  if(current.getClosePrice()<open||Util.findPercentageChange(current.getClosePrice(), open)<0) return null;
        int mins = Util.getMinutes(current.getDate());
        if(mins>14*60+30) return null;
        float body = findBodySize(current);
        float highPrice = intra.getHighPrice(18, true).getHighPrice();
        float lowestPrice = intra.getLowPrice(18, true).getLowPrice();
        float deflection = Util.findPercentageChange(highPrice, lowestPrice);
        if(deflection<.1){
            float hh = intra.stream().map(Ticker::getHighPrice).max(Float::compare).get();
            float ll = intra.stream().map(Ticker::getLowPrice).min(Float::compare).get();
            if(Util.findPercentageChange(hh, ll)<2) return null;
            watch.put(eq.getName(), highPrice);
        }
       // System.out.println("time:"+current.getDate()+" Body:"+body+" deflection:"+deflection);
        return null;
        
    }
    float calculateDeflection(TickerList intra){
        float highPrice = intra.getHighPrice(18, true).getHighPrice();
        float lowestPrice = intra.getLowPrice(18, true).getLowPrice();
        float deflection = Util.findPercentageChange(highPrice, lowestPrice);
        return deflection;
    }
    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
        float stopLoss = Util.findTargetStopLoss(executedTrade.executedPrice, .5f);
        if(currentPrice<stopLoss){
            Trade trade = new Trade();
            trade.isAtMarketPrice = false;
            trade.openPrice = stopLoss+0.01f;
            watch.remove(eq.getName());
            return trade;
        }
        return null;
    }

    @Override
    public boolean cancelPosition(Equity eq, Index index, int entryPoint, Trade placeTrade) {
        int mins = Util.getMinutes(eq.intraday.getCurrentTicker().getDate());
        if(mins>14*60+30) return true;
        float price = watch.get(eq.getName());
        float current = eq.intraday.getCurrentTicker().getLowPrice();
        if(Util.findPercentageChange(price, current)>.3f){ 
            watch.remove(eq.getName());
            return true;
        }
        return false;
    }

    @Override
    public boolean isLong() {
        return true;
    }

    private float findBodySize(Ticker ticker) {
        return Math.abs(ticker.getClosePrice() - ticker.getOpenPrice());
    }

    private Trade isV(Equity eq) {
        float highPrice = watch.get(eq.getName());
        float currentDeflection = calculateDeflection(eq.intraday);
        Ticker currentTicker = eq.intraday.getCurrentTicker();
        float prevsPrice = eq.intraday.getTickBeforeNdays(1).getLowPrice();
        float gain = Util.findPercentageChange(currentTicker.getHighPrice(), highPrice);
        if(gain>.1){
            watch.remove(eq.getName());
            return null;
        }
        if(currentTicker.getLowPrice()>prevsPrice&&currentTicker.getHighPrice()<highPrice&&currentDeflection>0.16f){
            Trade trade = new Trade();
            trade.isAtMarketPrice = false;
            trade.triggerPrice=highPrice;
            trade.openPrice = highPrice+.01f;
            trade.target = Util.findTargetPrice(highPrice, .4f);
            return trade;
        }
        return null;
    }
    
}
