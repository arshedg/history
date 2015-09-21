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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author arshed
 */
public class Cloud implements Strategy{

    public static List<String> cloudLIst = new ArrayList<>();
    @Override
    public Trade openPosition(Equity eq, Index index) {
        TickerList intraday = eq.intraday;
        //if(eq.eod.size()<2) return null;
        if(intraday.size()<15) return null;
        int mins = Util.getMinutes(intraday.getCurrentTicker().getDate());
        if(mins>14*60+30) return null;
//        float prevsClose = eq.eod.getTickBeforeNdays(1).getClosePrice();
//        float currentPrice = intraday.getCurrentTicker().getClosePrice();
//        float priceGain = Util.findPercentageChange(currentPrice, prevsClose);
//        if(priceGain<1.5) return null; //price change too less
        float high = intraday.getHighPrice(10, true).getHighPrice();
        if(high<200) return null;
//        float low = intraday.getLowPrice(10, true).getLowPrice();
//        float priceDeflection = Util.findPercentageChange(high, low);
//        float allowedDeflection = maxAllowedDeflection(priceGain);
//        if(priceDeflection>allowedDeflection) return null; //price is varying
        boolean canBuy = underAllowedDeflection(eq.intraday.subList(eq.intraday.size()-10, eq.intraday.size()));
        if(canBuy) return null;
        Trade trade = new Trade();
        trade.openPrice = Util.findTargetPrice(high, .5f);
        trade.isAtMarketPrice = false;
        trade.triggerPrice=Util.findTargetPrice(high, .15f);
       //System.out.println("EQUITY "+eq.getName()+" time:"+eq.intraday.getCurrentTicker().getDate()+" deflection"+allowedDeflection);
        return trade;
    }
    float maxAllowedDeflection(float change){
        //0.1 percentage deflection for every 3 percentage
        return (change/2)*.1f;
    }
    boolean underAllowedDeflection(List<Ticker> list){
        float min = list.stream().map(Ticker::getLowPrice).max(Float::compare).get();
        float max = list.stream().map(Ticker::getLowPrice).max(Float::compare).get();
        float deflection = Util.findPercentageChange(max, min);
        if(deflection>0.07) return true;
        min = list.stream().map(Ticker::getLowPrice).max(Float::compare).get();
        max = list.stream().map(Ticker::getLowPrice).max(Float::compare).get();
        deflection = Util.findPercentageChange(max, min);
        if(deflection>0.07) return true;
        return false;
    }
  @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint,Trade executedTrade) {
//        float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
//        float boughtPrice = executedTrade.executedPrice;
//        float stopLoss = Util.findTargetStopLoss(boughtPrice, 0.2f);
//        float minProfit = Util.findTargetPrice(boughtPrice, .3f);
//        Ticker t0 = eq.intraday.getTickBeforeNdays(3);
//        Ticker t1 = eq.intraday.getTickBeforeNdays(2);
//        Ticker t2 = eq.intraday.getTickBeforeNdays(1);
//        Ticker t3 = eq.intraday.getCurrentTicker();
//        if(currentPrice<stopLoss){
//            Rain.rainLIst.add(eq.getName());
//            Trade trade = new Trade();
//            trade.isAtMarketPrice = true;
//            return trade;
//        }
        return null;
    }

    @Override
    public boolean isLong() {
        return false;
    }

    @Override
    public boolean cancelPosition(Equity eq, Index index, int entryPoint, Trade placeTrade) {
           int mins = Util.getMinutes(eq.intraday.getCurrentTicker().getDate());
        if(mins>14*60+30) return true;
        return false;
        
    }
    
}
