/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.data.processor;

import com.fluke.connector.Ventura;
import com.fluke.model.OrderExecuteListener;
import com.fluke.model.Trade;
import com.fluke.model.ticker.Ticker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author arsh
 */
public class RealTimeExecutor implements ITradeExecutor{

    Map<Integer,Trade> placedOrders = new HashMap<>();
    Map<Integer,Trade> openPosition = new HashMap<>();
    public RealTimeExecutor() {
    }
    Map<Integer,LossAndTarget> lt = new HashMap<>();
    Map<Integer,Integer> relatedTrade = new HashMap<>();
    OrderExecuteListener lisener;
    Ventura connector = new Ventura();
    @Override
    public void placeOrder(Trade trade) {
        System.out.println("Placing order for "+trade.equity);
        int tradeID = placeOrder(trade.equity, trade.openPrice, trade.triggerPrice, trade.isLong);
        trade.id = tradeID;
        placedOrders.put(tradeID, trade);
        System.out.println("Order placed for "+trade.equity);
    }

    @Override
    public void cancelTrade(Trade trade) {
        cancelTrade((int) trade.id);
    }

    @Override
    public void setOrderListener(OrderExecuteListener listener) {
       this.lisener = listener;
    }

    @Override
    public void status() {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void listen(String equity, Ticker eq) {
       List<Integer> ids = new ArrayList<>();
       for(Entry<Integer,Trade> entry:placedOrders.entrySet()){
           if(entry.getValue().equity.equals(equity)){
               ids.add(entry.getKey());
           }
       }
       for(Entry<Integer,Integer> entry:relatedTrade.entrySet()){
           int parentId = entry.getValue();
           if(openPosition.get(parentId).equity.equals(equity)){
               ids.add(entry.getKey());
           }
       }
       ids.forEach(id->{
           if(connector.isExecuted(id)){
               onExecute(id);
           }
       });
                                     
    }

    @Override
    public void endOfData(String id, Ticker eq) {
       
    }
    private int  placeOrder(String equity,float price,float triggerPrice,boolean isBuy){
        if(isBuy){          
            return connector.placeBuy(equity, price,triggerPrice);
        }else{
            return connector.placeSell(equity, price, triggerPrice);
        }
            
    }
    public void onExecute(int id){
        System.out.println("Order executed "+id);
        if(placedOrders.containsKey(id)){
            Trade trade = placedOrders.get(id);
            trade.executedPrice = trade.openPrice;
            setStopLossAndTarget(id,trade);
            lisener.onTradeExecuted(trade);
            placedOrders.remove(id);
            openPosition.put(id, trade);
        }else if(relatedTrade.containsKey(id)){
            int originalId = relatedTrade.get(id);
            cancelRelatedTrade(id);
            lisener.onTargetAchieved(openPosition.get(originalId));
            openPosition.remove(originalId);
        }
    }
    private void cancelRelatedTrade(int relatedID){
        int originalId = relatedTrade.get(relatedID);
        LossAndTarget tl = lt.get(originalId);
        if(tl.slId==relatedID){
            cancelTrade(tl.targetId);
        }else{
            cancelTrade(tl.slId);
        }
        relatedTrade.remove(tl.slId);
        relatedTrade.remove(tl.targetId);
        lt.remove(originalId);
    }
    private void cancelTrade(int id){
        connector.cancelTrade(id);
    }
    private void setStopLossAndTarget(int id, Trade trade) {
        LossAndTarget tl = new LossAndTarget();
        if(trade.exitPrice>0){
            float triggerPrice = getExitTriggerPrice( trade.exitPrice,trade.isLong);
            System.out.println("stop loss trigger price  "+triggerPrice);
            tl.slId = placeOrder(trade.equity, trade.exitPrice, triggerPrice, !trade.isLong);
            relatedTrade.put(tl.slId, id);
        }
        if(trade.target>0){
             System.out.println("Targt price  "+trade.target);
            tl.targetId = placeOrder(trade.equity, trade.target, 0, !trade.isLong);
            relatedTrade.put(tl.targetId, id);
        }
        lt.put(id, tl);
        
    }

    private float getExitTriggerPrice(float exitPrice, boolean buy) {
        float add=0f;
        if(exitPrice>2000){
           add=0.20f;
       }else if(exitPrice>800){
           add = 0.10f;
       }else {
           add = .05f;
       }
        if(buy){
            return exitPrice+add;
        }else{
            return exitPrice-add;
        }
    }
    
}
class LossAndTarget{
    int slId;
    int targetId;
}