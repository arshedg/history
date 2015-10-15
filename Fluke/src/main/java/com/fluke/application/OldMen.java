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

/**
 *
 * @author arsh
 */
public class OldMen implements Strategy {

    private boolean minDataReached(TickerList intra) {
        if (intra.getCurrentTicker().getClosePrice() < 200) {
            return false;
        }
        int mins = Util.getMinutes(intra.getCurrentTicker().getDate());
        if (mins > 11 * 60 && mins < 15 * 60) {
            return true;
        }
        return false;
    }

    private float getNiftyChange(Index index) {
        int niftyPosition = index.nifty.intraday.size();
        if (niftyPosition < 10) {
            return 0;
        }
        float avgNifty = (float) index.nifty.intraday.subList(niftyPosition - 10, niftyPosition).stream().mapToDouble(n -> n.getClosePrice()).average().getAsDouble();
        float prevsNifty = index.nifty.getPrevsPrice();
        return Util.findPercentageChange(avgNifty, prevsNifty);
    }

    @Override
    public Trade openPosition(Equity eq, Index index) {
        if (!minDataReached(eq.intraday)) {
            return null;
        }
        float chaangeInNifty = getNiftyChange(index);
        if (chaangeInNifty > .6f) {
            //nifty is -0.6 down
            return canBuy(eq.intraday, eq.getPrevsPrice(), chaangeInNifty);
        }
        return null;
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
        if(getNiftyChange(index)<.3f){
           Trade trade = new Trade();
           trade.openPrice = currentPrice;
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

    private Trade canBuy(TickerList intraday, float prevsPrice, float niftyDiff) {
       int size = intraday.size();
       float average = (float) intraday.subList(size-15, size).stream().mapToDouble(s->s.getClosePrice()).average().getAsDouble();
       float tOpenPrice = getOpenPrice(intraday, prevsPrice);
       float priceChange = Util.findPercentageChange(average, tOpenPrice);
       float diff = niftyDiff-priceChange;
       if(diff>1&&!isAtPeak(intraday)&&priceChange<-1){
           Trade trade = new Trade();
           trade.openPrice = intraday.getCurrentTicker().getClosePrice();
           trade.isAtMarketPrice=false;
           //trade.target = Util.findTargetStopLoss(trade.openPrice, 1f);
           return trade;
            
       }
       return null;
    }
    public float getOpenPrice(TickerList intra,float prevsPrice){
        if(intra.size()<3){ 
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
