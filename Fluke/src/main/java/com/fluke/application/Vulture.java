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
import java.util.Optional;

/**
 *
 * @author arsh
 */
class EntryV{
    float price;
    
    int volume;
}
public class Vulture implements Strategy{
    Map<String,EntryV> watch = new HashMap<>();
    Map<String,Integer> timer = new HashMap<>();
    @Override
    public Trade openPosition(Equity eq, Index index) {
        if(watch.containsKey(eq.getName())){
            return confirmer(eq,index);
        }
        TickerList intra = eq.intraday;
        if(intra.size()<20) return null;
        float least = (float) intra.stream().mapToDouble(t->t.getLowPrice()).min().getAsDouble();//intra.getLeastPrice().getLowPrice();
        if(least<100) return null;
        Ticker currentTicker = intra.getCurrentTicker();
        if(Util.getMinutes(currentTicker.getDate())>14*60) return null;
        float currentVolume = currentTicker.getVolume();
        int maxVolume = intra.stream().map(t->t.getVolume()).max(Integer::compare).get();
        if(currentVolume>=maxVolume&&least==currentTicker.getLowPrice()){
            System.out.println(eq.getName()+" found at "+currentTicker.getDate()+" Sell on further loss");
                 int secondMaxVolume = getSecondHighestVolume(maxVolume, intra);
                 float volumeDiff = Util.findPercentageChange(maxVolume, secondMaxVolume);
                 float high = intra.getHighPrice().getHighPrice();
                 float change = Util.findPercentageChange(high, least);
            if(volumeDiff>25&&change>2){
               // System.out.println(eq.getName()+" found at "+currentTicker.getDate()+" density check passed");
                EntryV e = new EntryV();
                e.price = least;
              //  e.volume = maxVolume;
                watch.put(eq.getName(), e);
            }
        }
        return null;
    }
    int getSecondHighestVolume(int highestVolume,TickerList intra){
        Optional<Integer> val = intra.subList(2,intra.size()-2).stream().filter(t->t.getVolume()!=highestVolume).map(t->t.getVolume()).max(Integer::compare);
        if(val.isPresent()){
            return val.get();
        }
        return 0;
    }
    float getPriceMomentum(float price,TickerList intra){
        float nextPrice = intra.subList(0, intra.size()-3).stream().map(t->t.getLowPrice()).min(Double::compare).get();
        return Util.findPercentageChange(nextPrice, price);
    }
    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        float price = eq.intraday.getCurrentTicker().getClosePrice();
        float stopLoss = Util.findTargetPrice(executedTrade.executedPrice, .8f);
        if(stopLoss<price){
            Trade trade= new Trade();
            trade.isAtMarketPrice = false;
            trade.openPrice=stopLoss;
            return trade;
        }
        return null;
    }

    @Override
    public boolean cancelPosition(Equity eq, Index index, int entryPoint, Trade placeTrade) {
        if(Util.getMinutes(eq.intraday.getCurrentTicker().getDate())>13*60+30) {
          //  System.out.println("cancel position "+eq.getName()+" at "+eq.intraday.getCurrentTicker().getDate());
            return true;
        }
        return false;
    }

    @Override
    public boolean isLong() {
        return false;
    }

    private Trade confirmer(Equity eq, Index index) {
        EntryV e = watch.get(eq.getName());
       Ticker currentTicker = eq.intraday.getCurrentTicker();
       if(currentTicker.getVolume()>e.volume||currentTicker.getLowPrice()<e.price){
           watch.remove(eq.getName());
           return null;
       }
       float change = Util.findPercentageChange(currentTicker.getLowPrice(),e.price);
       Integer time = timer.get(eq.getName());
       if(time==null){
           time=0;
           timer.put(eq.getName(), 0);
       }
      
       if(time>2&&change>.31){
           Trade trade = new Trade();
           trade.isAtMarketPrice=false;
           trade.openPrice = Util.findTargetStopLoss(e.price,.45f);
           trade.triggerPrice = trade.openPrice+0.05f;
           trade.target = Util.findTargetStopLoss(e.price, 1.3f);
           trade.exitPrice = Util.findTargetStopLoss(trade.openPrice, .8f);
           watch.remove(eq.getName());
           timer.remove(eq.getName());
           VultureHealer.order.put(eq.getName(), Util.findTargetStopLoss(e.price,.4f));
           System.out.println("Sell position with trigger "+trade.triggerPrice+eq.getName()+" at "+eq.intraday.getCurrentTicker().getDate()+"\t target:"+trade.target);
    
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
       return currentVol>2*avg;
    }
    
}
