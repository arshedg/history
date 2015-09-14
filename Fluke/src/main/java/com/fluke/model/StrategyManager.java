/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.model;

import com.fluke.data.processor.TickerListener;
import com.fluke.data.processor.TradeExecutor;
import com.fluke.model.Equity;
import com.fluke.model.Index;
import com.fluke.model.Trade;
import com.fluke.model.ticker.Ticker;
import com.fluke.strategy.Strategy;
import com.fluke.util.Configuration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author arshed
 */
public class StrategyManager implements OrderExecuteListener {
    List<Equity> equities = new ArrayList<>();
    Index index;
    Configuration config;
    List<Strategy> strategies;
    Map<String,Trade> orderPlaced = new HashMap<>();
    Map<String,Trade> executedOrders = new HashMap<>();
    Map<String,Integer> pointers = new HashMap<>();
    List<TickerListener> listeners;
    TradeExecutor executor;
    public StrategyManager(Configuration config,Strategy... strategies) {
        this.config = config;
        executor = config.executor;
        this.addListener(executor);
        this.strategies = Arrays.asList(strategies);
    }
    public void run(){
        equities = new ArrayList<>();
        orderPlaced = new HashMap<>();
        executedOrders = new HashMap<>();
        pointers = new HashMap<>();
        addEquties(config.equities);
        index = new Index(config);
        while(!equities.isEmpty()){
            
            equities = execute();
        }
            
    }
    public List<Equity> execute(){
        List<Equity> valid = new ArrayList<>();
         for(Equity equity:equities){
             
            Ticker currentTicker = equity.intraday.getNextTicker();
            if(currentTicker!=null){
               // System.out.println("Processing "+equity.getName()+" time:"+equity.intraday.getCurrentTicker().getDate());
                index.loadNext(currentTicker.getDate());
                callStrategies(equity,currentTicker);
                callListeners(equity.getName(),currentTicker);
                valid.add(equity);
            }else{
                alertEndOfData(equity.getName(),equity.intraday.getCurrentTicker());
            }
        }
         return valid;
    }
    private void callStrategies(Equity eq,Ticker ticker){
        for(Strategy strategy:strategies){
            String key = getOrderKey(eq.getName(), strategy.getClass().getName());
            if(orderPlaced.containsKey(key)){
                continue;
            }else if(executedOrders.containsKey(key)){
                Trade trade = strategy.closePosition(eq, index, pointers.get(key));
                if(trade!=null){
                    pointers.remove(key);
                    executedOrders.remove(key);
                    trade.strategy = strategy.getClass().getName();
                    trade.equity = eq.getName();
                    trade.isLong = !strategy.isLong(); //close positiom is opposite
                    executor.placeOrder(trade);
                }
            }else{
                Trade trade = strategy.openPosition(eq, index);
                if(trade!=null){
                    trade.isLong = strategy.isLong();
                    handleTrade(eq.getName(),trade,strategy.getClass().getName(),eq.getIntraday().size());
                }
            }
        }
    }
    
    private void addEquties(List<String> eq) {
        for(String equity:eq){
            equities.add(new Equity(equity,config));
        }
    }
    public void addListener(TickerListener listener){
        if(listeners==null){
            listeners = new ArrayList();
        }
        listeners.add(listener);
    }
    public Configuration getConfiguration(){
        return config;
    }

    private void callListeners(String name,Ticker currentTicker) {
       for(TickerListener listener:listeners){
           listener.listen(name,currentTicker);
       }
    }

    private void handleTrade(String stock,Trade trade, String strategy,int size) {

        orderPlaced.put(getOrderKey(stock, strategy), trade);
        pointers.put(getOrderKey(stock, strategy), size-1);
        trade.strategy = strategy;
        trade.equity = stock;
        executor.placeOrder(trade);
    }

    public String getOrderKey(String equity,String strategy){
        return equity+":"+strategy;
    }
    @Override
    public void onTradeExecuted(Trade trade) {
       String key = getOrderKey(trade.equity, trade.strategy);
       if(orderPlaced.containsKey(key)){
            orderPlaced.remove(key);
            executedOrders.put(key, trade);
       }
      
    }

    private void alertEndOfData(String name, Ticker currentTicker) {
          for(TickerListener listener:listeners){
           listener.endOfData(name,currentTicker);
       }
    }
}