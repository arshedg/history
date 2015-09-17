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

/**
 *
 * @author arshed
 */
public class Cloud implements Strategy{

    @Override
    public Trade openPosition(Equity eq, Index index) {
        TickerList intraday = eq.intraday;
        if(eq.eod.size()<2) return null;
        if(intraday.size()<30) return null;
        float prevsClose = eq.eod.getTickBeforeNdays(1).getClosePrice();
        float currentPrice = intraday.getCurrentTicker().getClosePrice();
        float priceGain = Util.findPercentageChange(currentPrice, prevsClose);
        if(priceGain<1.5) return null; //price change too less
        float high = intraday.getHighPrice(40, true).getHighPrice();
        float low = intraday.getLowPrice(40, true).getLowPrice();
        float priceDeflection = Util.findPercentageChange(high, low);
        float allowedDeflection =0.15f;// maxAllowedDeflection(priceGain);
        if(priceDeflection>allowedDeflection) return null; //price is varying
        
        Trade trade = new Trade();
        trade.openPrice = Util.findTargetPrice(high, (allowedDeflection)+.5f);
        trade.isAtMarketPrice = false;
        trade.triggerPrice=Util.findTargetPrice(high, allowedDeflection+.4f);
        trade.target = Util.findTargetPrice(trade.openPrice, .45f);
       //System.out.println("EQUITY "+eq.getName()+" time:"+eq.intraday.getCurrentTicker().getDate()+" deflection"+allowedDeflection);
        return trade;
    }
    float maxAllowedDeflection(float change){
        //0.1 percentage deflection for every 3 percentage
        return (change/2)*.1f;
    }
    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint,Trade executedTrade) {
       float boughtPrice = executedTrade.executedPrice;
       float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
       float prevsPrice = eq.getEod().getTickBeforeNdays(1).getClosePrice();
        float priceOnTrade =executedTrade.openPrice;
        float deflection = maxAllowedDeflection(Util.findPercentageChange(priceOnTrade, prevsPrice));
       
       if(Util.findPercentageChange(boughtPrice, currentPrice)>1.5){
           Trade trade = new Trade();
           trade.isAtMarketPrice=true;
           return trade;
       }
       return null;
    }

    @Override
    public boolean isLong() {
        return true;
    }

    @Override
    public boolean cancelPosition(Equity eq, Index index, int entryPoint, Trade placeTrade) {
        float prevsPrice = eq.getEod().getTickBeforeNdays(1).getClosePrice();
        float priceOnTrade =placeTrade.openPrice;
        float deflection = maxAllowedDeflection(Util.findPercentageChange(priceOnTrade, prevsPrice));
        float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
        if(currentPrice<priceOnTrade){
            float change = Util.findPercentageChange(priceOnTrade, currentPrice);
            if(change>deflection*25){
                return true;
            }
        }
        int time = Util.getMinutes(eq.intraday.getCurrentTicker().getDate());
        return time>(14*60)+40;
        
    }
    
}
