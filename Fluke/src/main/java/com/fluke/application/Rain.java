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
import java.util.ListIterator;

/**
 *
 * @author arshed
 */
public class Rain implements Strategy{
    public static ArrayList<String> rainLIst = new ArrayList<>();
    
    @Override
    public Trade openPosition(Equity eq, Index index) {
        if(Cloud.cloudLIst.contains(eq.getName())){
            Cloud.cloudLIst.remove(eq.getName());
            Trade trade = new Trade();
            trade.isAtMarketPrice = true;
            trade.target = Util.findTargetStopLoss(eq.intraday.getCurrentTicker().getClosePrice(), 0.3f);
            return trade;
        } else {
            TickerList intraday = eq.intraday;
            if(eq.eod.size()<2) return null;
            if(intraday.size()<30) return null;
            float prevsClose = eq.eod.getTickBeforeNdays(1).getClosePrice();
            float currentPrice = intraday.getCurrentTicker().getClosePrice();
            float priceGain = Util.findPercentageChange(currentPrice, prevsClose);
             if(priceGain<2.5) return null; //price change too less

            if(hasChnageInLowsAndHighs(intraday)) return null; // Check for no changes in highs

            float high = intraday.getCurrentTicker().getHighPrice();
            float low = intraday.getLowPrice(10, true).getLowPrice();
    //        float priceDeflection = Util.findPercentageChange(high, low);
            float allowedDeflection =maxAllowedDeflection(priceGain);
    //        if(priceDeflection>allowedDeflection) return null; //price is varying
    //      

            Trade trade = new Trade();
            trade.openPrice = Util.findTargetPrice(low, allowedDeflection+.01f);
            trade.isAtMarketPrice = false;
            trade.triggerPrice=Util.findTargetPrice(low, allowedDeflection);
//            System.out.println("Trigger Price Line At "+trade.triggerPrice+" "+intraday.getCurrentTicker().getDate()+ " "+eq.getName());
           //System.out.println("EQUITY "+eq.getName()+" time:"+eq.intraday.getCurrentTicker().getDate()+" deflection"+allowedDeflection);
            return trade;
        }
    }
    float maxAllowedDeflection(float change){
        //0.1 percentage deflection for every 3 percentage
        return (change/2)*.3f;
    }
    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint,Trade executedTrade) {
        float currentPrice = eq.intraday.getCurrentTicker().getClosePrice();
        float soldPrice = executedTrade.executedPrice;
        float stopLoss = Util.findTargetPrice(soldPrice, 0.4f);
        float minProfit = Util.findTargetStopLoss(soldPrice, .35f);
        
        if(currentPrice >= stopLoss){
            rainLIst.add(eq.getName());
            Trade trade = new Trade();
            trade.isAtMarketPrice=true;
            return trade;
        }
//        if(eq.intraday.size()-entryPoint<4) return null;
//        Ticker t1 = eq.intraday.getTickBeforeNdays(2);
//        Ticker t2 = eq.intraday.getTickBeforeNdays(1);
//        Ticker t3 = eq.intraday.getCurrentTicker();
//        
//        boolean priceIncrementing = currentPrice<minProfit&&(t1.getClosePrice() < t3.getClosePrice()||(!t2.isRed()&&!t3.isRed()));
//        if(priceIncrementing){
//            Trade trade = new Trade();
//            trade.isAtMarketPrice=true;
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
        return false;
        
    }

    private boolean hasChnageInLowsAndHighs(TickerList intraday) {
        ListIterator<Ticker> lastTickersIterator = intraday.listIterator(intraday.size() - 20);
        
        float highest = 0f;
        float lowest  =  0f;
//        boolean isChangeInHighPrice = false;
        boolean isChangeInLowPrice = false;
        while(lastTickersIterator.hasNext()) {
            
            Ticker ticker = lastTickersIterator.next();
//            if(highest == 0) {
//                highest = ticker.getHighPrice();
//            }
            if(lowest == 0) {
                lowest = ticker.getLowPrice();
            }
//            
//            float changeInHigh = Util.findPercentageChange(highest, ticker.getHighPrice());
//            if(!isChangeInHighPrice) {
//                isChangeInHighPrice = hasChangeInHighPrice(changeInHigh);
//            }
            
            float changeInLow = Util.findPercentageChange(lowest, ticker.getLowPrice());
            if(!isChangeInLowPrice) {
                isChangeInLowPrice = hasChangeInHighPrice(changeInLow);
            }
        }
        return isChangeInLowPrice;
    }
    


    private boolean hasChangeInHighPrice(float changeInHigh) {
        if ((changeInHigh > 0 && changeInHigh > 0.1) || (changeInHigh < 0 && changeInHigh < -0.1)) {
            return true;
        }
        return false;
    }
}