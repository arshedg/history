/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.data.processor;


import com.fluke.model.OrderExecuteListener;
import com.fluke.model.Trade;
import com.fluke.model.ticker.Ticker;
import com.fluke.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 *
 * @author arshed
 */
public class TradeExecutor implements TickerListener{

    
    Map<String,List<Trade>> tradeMap = new HashMap<>();
    Map<String,OpenPosition> openPositions = new HashMap<>();
    Map<String,Perfomance> perfomance = new HashMap<>();
    List<Trade> tempTargetList = new ArrayList<>();
    OrderExecuteListener listener;
    @Override
    public void listen(String id, Ticker eq) {
        List<Trade> trades = tradeMap.get(id);
        if(trades==null||trades.isEmpty()){
            return;
        }
        List<Trade> fulfilled = new ArrayList<>();
        boolean isSquareOff=false;
        for(Trade trade:trades){
            isSquareOff=false;
            if(!isPassingTrigger(trade, eq)){
                continue;
            }
            boolean isExecuted=false;
            String key=trade.equity+":"+trade.strategy;
            OpenPosition position = openPositions.get(key);
            if(position!=null){
                isSquareOff = true;
            }
            if(trade.isLong){
               isExecuted = handleBuy(eq,trade);
            }else{
               isExecuted =  handleSell(eq,trade);
            }
            if(isExecuted){
                callListener(trade);
                fulfilled.add(trade);
            }
            if(isExecuted&&isSquareOff){
                openPositions.remove(key);
            }
        }
        for(Trade target:tempTargetList){
            addToTradeMap(target);
        }
        tempTargetList.clear();
        trades.removeAll(fulfilled);
    }
    boolean isPassingTrigger(Trade trade,Ticker ticker){
        if(trade.triggerPrice<=0){
            //no triggerr/stop loss
            return true;
        }
        if(trade.isLong){
            if(ticker.getHighPrice()>trade.triggerPrice){
                return true;
            }
        }else if(ticker.getLowPrice()<trade.triggerPrice){
            return true;
        }
        return false;
    }
    public void cancelTrade(Trade trade){
         tradeMap.remove(trade.equity);
    }
    @Override
    public void endOfData(String id, Ticker eq) {
        List<Trade> trades = tradeMap.get(id);
        if(trades!=null&&!trades.isEmpty()){
           trades.clear();
        }
        List<String> toRemove = new ArrayList<>();
        for(Entry<String,OpenPosition> entry:openPositions.entrySet()){
            if(entry.getKey().startsWith(id+":")){
                forceClose(entry.getKey(),entry.getValue(),eq);
                toRemove.add(entry.getKey());
                
            }
        }
        for(String key:toRemove){
            openPositions.remove(key);
        }

    }

    public void placeOrder(Trade trade) {
        addToTradeMap(trade);
    }
    public void setOrderListener(OrderExecuteListener listener){
        this.listener=listener;
    }
    private boolean handleBuy(Ticker ticker,Trade trade) {
        float price;
        if(trade.isAtMarketPrice){
           price=ticker.getClosePrice();
       }else if(trade.openPrice>ticker.getLowPrice()){
           price = trade.openPrice;
       }else{
           return false;
       }
       trade.executedPrice = price;
       String key=trade.equity+":"+trade.strategy;
       OpenPosition position = openPositions.get(key);
       if(position==null){
           //new trade, not square off
           position = new OpenPosition();
           position.price = price;
           position.trade = trade;
           position.ticker = ticker;
           openPositions.put(key, position);
           handleTarget(trade,position);
           
       }
       else{
            derivePerfomance(position, price, trade,ticker);
       }
       return true;
    }
     private void addToTradeMap(Trade trade){
        List<Trade> list = tradeMap.get(trade.equity);
        if(list==null){
            list = new ArrayList<>();
            tradeMap.put(trade.equity, list);
        }
        list.add(trade);
    }
    private boolean handleSell(Ticker ticker,Trade trade) {
        float price;
        if(trade.isAtMarketPrice){
           price=ticker.getClosePrice();
       }else if(trade.openPrice<ticker.getHighPrice()){
          price = trade.openPrice;
       }else{
           return false;
       }
       trade.executedPrice = price;
       String key=trade.equity+":"+trade.strategy;
       OpenPosition position = openPositions.get(key);
       if(position==null){
           position = new OpenPosition();
           position.price = price;
           position.trade = trade;
           position.ticker = ticker;
           openPositions.put(key, position);
       }else{
            derivePerfomance(position, price, trade,ticker);
       }
       return true;
    }

    private void derivePerfomance(OpenPosition position, float price, Trade trade,Ticker exitTicker) {
       if(trade.isLong){
           //last rrade buy= short sell
           System.out.println("Sold "+trade.equity+" at price "+position.price+" time:"+position.ticker.getDate());
           System.out.println("Bought "+trade.equity+" at price "+price+" time:"+exitTicker.getDate());
       }else{
           System.out.println("Bought "+trade.equity+" at price "+position.price+" time:"+position.ticker.getDate());
           System.out.println("Sold "+trade.equity+" at price "+price+" time:"+exitTicker.getDate());
       }
        Perfomance pf = perfomance.get(trade.strategy);
        if(pf==null){
            pf = new Perfomance();
            perfomance.put(trade.strategy, pf);
        }
        if(position.trade.isLong){
            float buyPrice = position.price;
            float change = Util.findPercentageChange(price, buyPrice);
            if(change>0){
                pf.profitCount++;
            }else{
                pf.lossCount++;
            }
            pf.percentageGain+=change;
            System.out.println("Percentage gain:"+change);
        }else{
             float soldPrice = position.price;
            float change = Util.findPercentageChange(soldPrice, price);
            if(change>0){
                pf.profitCount++;
            }else{
                pf.lossCount++;
            }
            pf.percentageGain+=change;
            System.out.println("Percentage gain:"+change);
        }
       
    }

    private void callListener(Trade trade) {
       if(listener!=null){
           listener.onTradeExecuted(trade);
       }
    }
    
    public void status(){
        float tp=0,tl=0,tg=0;
        for(Entry<String,Perfomance> entry:perfomance.entrySet()){
            System.out.println("Strategy: "+entry.getKey());
            Perfomance pf = entry.getValue();
            tp+=pf.profitCount;
            tl+=pf.lossCount;
            tg+=pf.percentageGain;
            System.out.println("Profit count: "+pf.profitCount+"\n loss count:"+pf.lossCount+"\n percentage gain:"+pf.percentageGain);
        }
        System.out.println("\nGROSS PERFOMANCE");
        System.out.println("Total entry"+(tp+tl)+"\nProfit count: "+tp+"\n loss count:"+tl+"\n percentage gain:"+tg);
    }

    private void forceClose(String key, OpenPosition position, Ticker ticker) {
       String parts[] = key.split(":");
       String stock = parts[0];
       Trade trade = new Trade();
       trade.isAtMarketPrice=true;
       trade.equity=stock;
       trade.strategy = position.trade.strategy;
       trade.isLong = !position.trade.isLong;
        if(trade.isLong){
           handleBuy(ticker, trade);
       }else{
           handleSell(ticker, trade);
       }
    }

    private void handleTarget(Trade trade, OpenPosition position) {
        if(trade.target<=0){
            return;
        }
        Trade target = new Trade();
        target.equity = trade.equity;
        target.isAtMarketPrice=false;
        target.isLong = !trade.isLong;
        target.strategy = trade.strategy;
        target.openPrice = trade.target;
        tempTargetList.add(target);
    }

    

    
    
}
class Perfomance
{
    int profitCount;
    int lossCount;
    float percentageGain;
    
}
class OpenPosition{
    float price;
    Trade trade;
    Ticker ticker;
}