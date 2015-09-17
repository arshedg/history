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
 * @author arshed
 */
public class Rain implements Strategy{

    @Override
    public Trade openPosition(Equity eq, Index index) {
         TickerList intraday = eq.intraday;
        if(eq.eod.size()<2) return null;
        if(intraday.size()<30) return null;
        float prevsClose = eq.eod.getTickBeforeNdays(1).getClosePrice();
        float currentPrice = intraday.getCurrentTicker().getClosePrice();
        float priceGain = Util.findPercentageChange(currentPrice, intraday.get(0).getLowPrice());
        if(priceGain<1.5) return null; //price change too less
        float high = intraday.getHighPrice(10, true).getHighPrice();
        float low = intraday.getLowPrice(10, true).getLowPrice();
        float priceDeflection = Util.findPercentageChange(high, low);
        float allowedDeflection =0.7f;// maxAllowedDeflection(priceGain);
        if(priceDeflection>allowedDeflection) return null; //price is varying
        
        Trade trade = new Trade();
        trade.openPrice = Util.findTargetStopLoss(low, (allowedDeflection)+.1f);
        trade.isAtMarketPrice = false;
        trade.triggerPrice=Util.findTargetStopLoss(high, allowedDeflection);
        trade.target = Util.findTargetStopLoss(trade.openPrice, .45f);
       // System.out.println("EQUITY:"+eq.getName()+" target:"+trade.target);
       //System.out.println("EQUITY "+eq.getName()+" time:"+eq.intraday.getCurrentTicker().getDate()+" deflection"+allowedDeflection);
        return trade;
    }
    
    float maxAllowedDeflection(float change){
        //0.1 percentage deflection for every 3 percentage
        return (change/2)*.1f;
    }
    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        float boughtPrice = executedTrade.executedPrice;
       float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
       float prevsPrice = eq.getEod().getTickBeforeNdays(1).getClosePrice();
        float priceOnTrade =executedTrade.openPrice;
        float deflection = maxAllowedDeflection(Util.findPercentageChange(priceOnTrade, prevsPrice));
       
       if(Util.findPercentageChange(currentPrice,boughtPrice)>1.5){
           Trade trade = new Trade();
           trade.isAtMarketPrice=true;
           return trade;
       }
       return null;
    }

    @Override
    public boolean cancelPosition(Equity eq, Index index, int entryPoint, Trade placeTrade) {
                int time = Util.getMinutes(eq.intraday.getCurrentTicker().getDate());
        return time>(14*60)+40;
    }

    @Override
    public boolean isLong() {
        return false;
    }
    
}
