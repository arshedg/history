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
import java.util.OptionalDouble;

/**
 *
 * @author arsh
 */
public class GhostProtocol implements Strategy{

    private boolean minDataReached(TickerList intra){
        if(intra.getCurrentTicker().getClosePrice()<200) return false;
        int mins = Util.getMinutes(intra.getCurrentTicker().getDate());
        if(mins>11*60&&mins<15*60) return true;
        return false;
    }
    
    @Override
    public Trade openPosition(Equity eq, Index index) {
    //  System.out.println(eq.intraday.getCurrentTicker().getDate());
        if(!minDataReached(eq.intraday)) return null;
        float chaangeInNifty = getNiftyChange(index);
        if(chaangeInNifty<-.6f){
            //nifty is -0.6 down
           // System.out.println(eq.getName());
            Trade trade =  canShort(eq.intraday,eq.getPrevsPrice(),chaangeInNifty);
            if(trade!=null){
                System.out.println("SELL "+eq.getName()+" at price:"+trade.openPrice+" time:"+eq.intraday.getCurrentTicker().getDate());
            }
            return trade;
        }
        return null;
    }
    private float getNiftyChange(Index index){
        int niftyPosition = index.nifty.intraday.getPointer();
        if(niftyPosition<10) return 0;
        float avgNifty = (float) index.nifty.intraday.subList(niftyPosition-10, niftyPosition).stream().mapToDouble(n->n.getClosePrice()).average().getAsDouble();
        float prevsNifty = index.nifty.getPrevsPrice();
        return Util.findPercentageChange(avgNifty, prevsNifty);
    }
    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
        float stopLoss = Util.findTargetPrice(executedTrade.executedPrice, 1f);
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

    private Trade canShort(TickerList intraday,Float prevsPrice,float niftyDiff) {
        if(prevsPrice==null){
            return null;
        }
       int size = intraday.size();
       float average = (float) intraday.subList(size-15, size).stream().mapToDouble(s->s.getClosePrice()).average().getAsDouble();
       float tOpenPrice = getOpenPrice(intraday, prevsPrice);
       float priceChange = Util.findPercentageChange(average, tOpenPrice);
       float diff = niftyDiff-priceChange;
       if(diff<-1.4&&!isAtPeak(intraday)&&priceChange<2){
           Trade trade = new Trade();
           trade.openPrice = intraday.getCurrentTicker().getClosePrice();
           trade.isAtMarketPrice=false;
           
           trade.exitPrice = Util.findTargetPrice(trade.openPrice, 1f);
           trade.target = Util.findTargetStopLoss(trade.openPrice, .5f);
           return trade;
            
       }
       return null;
    }
    public float getOpenPrice(TickerList intra,float prevsPrice){
        if(intra.size()<31){ 
            return 0;
        }
        float openAvgPrice = (float) intra.subList(10, 30).stream().mapToDouble(t->t.getClosePrice()).average().getAsDouble();
        return prevsPrice>openAvgPrice?prevsPrice:openAvgPrice;
    }
    public boolean isAtPeak(TickerList intra){
        float highest = intra.getHighPrice().getHighPrice();
        int size = intra.size();
        float currentMax = (float) intra.subList(size-1, size).stream().mapToDouble(t->t.getHighPrice()).max().getAsDouble();
        if(Math.abs(Util.findPercentageChange(highest, currentMax))<.08f){
            return true;
        }
        return false;
    }
}
