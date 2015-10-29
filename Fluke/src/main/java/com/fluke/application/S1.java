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
public class S1 implements Strategy{

    boolean infy=true;
    Map<String,Float> watch = new HashMap<>();
    @Override
    public Trade openPosition(Equity eq, Index index) {
        if(eq.intraday.size()<35||Util.getMinutes(eq.intraday.getCurrentTicker().getDate())>13*60+30) return null;
       //if(Util.getMinutes(eq.intraday.getCurrentTicker().getDate())<12*60+35) return null;
        Ticker t1 = eq.intraday.getCurrentTicker();
        Float prevs = eq.getPrevsPrice();
        if(prevs==null){
            return null;
        }
     // if(Util.findPercentageChange(t1.getHighPrice(), prevs)<0) return null;
        if(t1.getClosePrice()<50) return null;
        if(watch.containsKey(eq.getName())){
            if(canSell(eq.getName(),eq.intraday)&&getNiftyChange(index)<10){
                System.out.println(eq.getName()+" time:"+t1.getDate()+" current:"+t1.getClosePrice());
                System.out.println(" nifty change "+getNiftyChange(index));
                return Trade.limitTrade(t1.getClosePrice(), Util.findTargetStopLoss(t1.getClosePrice(), .4f),Util.findTargetPrice(t1.getClosePrice(), .5f));
            }
        }
        Ticker t2 = eq.intraday.getTickBeforeNdays(1);
        float avgPrice = pastSMA(eq);
        if(Util.findPercentageChange(t1.getClosePrice(), avgPrice)>.3f){
                 watch.put(eq.getName(),t1.getClosePrice());

        }
           


        return null;
    }
    private float pastSMA(Equity eq){
        return (float) eq.intraday.subList(eq.intraday.size()-12, eq.intraday.size()-5).stream()
                .mapToDouble(t->t.getClosePrice()).average().getAsDouble();
    }
    private float getNiftyChange(Index index){
        int niftyPosition = index.nifty.intraday.getPointer();
        if(niftyPosition<30) return 0;
        float avgNifty = (float) index.nifty.intraday.subList(niftyPosition-30, niftyPosition).stream().mapToDouble(n->n.getClosePrice()).average().getAsDouble();
        float prevsNifty = index.nifty.getPrevsPrice();
        return Util.findPercentageChange(avgNifty, prevsNifty);
    }
    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
        float stopLoss = Util.findTargetPrice(executedTrade.executedPrice, .5f);
        if(currentPrice>stopLoss){
           Trade trade = new Trade();
           trade.openPrice = stopLoss;
           trade.isAtMarketPrice = false;
           Support.support.add(eq.getName());
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

    private boolean canSell(String equity,TickerList intraday) {
        Ticker t1 = intraday.getCurrentTicker();
        Ticker t2 = intraday.getTickBeforeNdays(2);
        if(t1.getClosePrice()<t2.getClosePrice()){
            if(isGain(watch.get(equity), t1)){
                watch.remove(equity);
                return true;
            }
            watch.remove(equity);
        }
        return false;
    }
    private boolean isGain(float price,Ticker tick){
       float change = Util.findPercentageChange(tick.getClosePrice(), price);
       if(change>0){
           return true;
       }else{
           return false;
       }
    }
}
