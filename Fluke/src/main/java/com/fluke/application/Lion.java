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
import java.util.List;
import java.util.Map;

/**
 *
 * @author arsh
 */
class Entry{
    float price;
    int volume;
}
public class Lion implements Strategy{

    Map<String,Entry> watch = new HashMap<>();
    Map<String,Integer> timer = new HashMap<>();
    @Override
    public Trade openPosition(Equity eq, Index index) {
        if(watch.containsKey(eq.getName())){
            return confirmer(eq,index);
        }
        
        
        TickerList intra = eq.intraday;
        if(intra.size()<30) return null;
        float highest = intra.getHighPrice().getHighPrice();
        if(highest<100) return null;
        Ticker currentTicker = intra.getCurrentTicker();
        if(Util.getMinutes(currentTicker.getDate())>14*60) return null;
        float currentVolume = currentTicker.getVolume();
        int maxVolume = intra.stream().map(t->t.getVolume()).max(Integer::compare).get();
        if(maxVolume==0) return null;
         double turnover = maxVolume*highest;
      
        if(turnover>5000000&&currentVolume>=maxVolume&&highest<=currentTicker.getHighPrice()){
           //   
           // System.out.println(eq.getName()+" found at "+currentTicker.getDate()+" buy on further rise");
            
              int secondMaxVolume = getSecondHighestVolume(maxVolume, intra);
                 float volumeDiff = Util.findPercentageChange(maxVolume, secondMaxVolume);
            if(volumeDiff>8&&getPriceMomentum(highest, intra)>.7){
              
                Entry e = new Entry();
                e.price = highest;
                e.volume = maxVolume;
                watch.put(eq.getName(), e);
            }
        }
        return null;
    }
    int getSecondHighestVolume(int highestVolume,TickerList intra){
        return intra.stream().filter(t->t.getVolume()!=highestVolume).map(t->t.getVolume()).max(Integer::compare).get();
    }
    float getPriceMomentum(float price,TickerList intra){
        float nextPrice = intra.subList(0, intra.size()-3).stream().map(t->t.getHighPrice()).max(Double::compare).get();
        return Util.findPercentageChange(price, nextPrice);
    }
    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        float price = eq.intraday.getCurrentTicker().getClosePrice();
        float stopLoss = Util.findTargetStopLoss(executedTrade.executedPrice, .8f);
        if(stopLoss>price){
            Trade trade= new Trade();
            trade.isAtMarketPrice = false;
            trade.openPrice=stopLoss;
            return trade;
        }
        
        return null;
        
    }

    @Override
    public boolean cancelPosition(Equity eq, Index index, int entryPoint, Trade placeTrade) {
        if(Util.getMinutes(eq.getIntraday().getCurrentTicker().getDate())>14*60-30) return true;
        return !isBullish(index);

    }

    @Override
    public boolean isLong() {
        return true;
    }
    boolean isBullish(Index index){
        return true;
//        TickerList nifty = index.nifty.intraday;
//        if(nifty==null||index.nifty.eod==null||index.nifty.eod.isEmpty()) return true;
//        Ticker prvsNifty = index.nifty.eod.getTickBeforeNdays(1);
//        float openPrice = prvsNifty.getClosePrice();
//        float currentPrice = nifty.getCurrentTicker().getClosePrice();
//        if(currentPrice<openPrice) return false;
//        float change = Util.findPercentageChange(currentPrice, openPrice);
//        return change>0;
    }
     Trade confirmer(Equity eq,Index index) {
       // if(!isBullish(index)) return null;
       Entry e = watch.get(eq.getName());
       Ticker currentTicker = eq.intraday.getCurrentTicker();
       if(currentTicker.getVolume()>e.volume){
           watch.remove(eq.getName());
           return null;
       }
       float change = Util.findPercentageChange(e.price, currentTicker.getHighPrice());
       Integer time = timer.get(eq.getName());
       if(time==null&&currentTicker.getHighPrice()>e.price){
           time=0;
           watch.remove(eq.getName());
           timer.put(eq.getName(), 0);
           return null;
       }else if(time==null){
           time=0;
           timer.put(eq.getName(), 0);
           return null;
       }
      
       if(change>.01){
           Trade trade = new Trade();
           trade.isAtMarketPrice=false;
           trade.openPrice = Util.findTargetPrice(e.price,.21f);
           trade.triggerPrice = trade.openPrice-0.01f;
           trade.target = Util.findTargetPrice(e.price, .7f);
           watch.remove(eq.getName());
           timer.remove(eq.getName());
           return trade;
       }else{
           timer.put(eq.getName(), ++time);
       }
       return null;
    }

     boolean isPassingVolumeDesity(TickerList intra) {
       int currentVol = intra.getCurrentTicker().getVolume();
       List<Ticker> sub = intra.subList(intra.size()-3, intra.size()-1);
       long sum = sub.stream().mapToInt(t->t.getVolume()).sum();
       float avg = sum/sub.size();
       return currentVol>1.5*avg;
    }
    
}
