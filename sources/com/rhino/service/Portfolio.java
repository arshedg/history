/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.service;

import com.rhino.data.Equity;
import com.rhino.data.Ticker;
import com.rhino.data.history.util.Util;
import com.rhino.listener.TickerChangeListener;
import com.rhino.strategy.Strategy;
import com.rhino.strategy.StrategyType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author agulshan
 */
class Result{
    int profit=0;
    int loss=0;
}
public class Portfolio implements TickerChangeListener{
    
    //todo use enums for position status
    private static final Boolean POSITION_TAKEN =true;
    private static final Boolean NO_POSITION =false;
    Map<Equity,Boolean> watch = new HashMap<>();
    Map<String,Integer> entryDetails = new HashMap<>();
    List<Equity> openPositions = new ArrayList<>();
    private Strategy strategy;
    private  float gain=0f;
    private int totalEntry,totalExit,profitExit,lostExit;
    private float maxGain=Float.MIN_VALUE;
    private float maxLoss = Float.MAX_VALUE;
    private float gainTrade=0;
    private float lossTrade=0;
    private float tradeDuration;
    private Map<Equity,Result> result = new HashMap<>();
    public void addToWatch(Equity equity){
        //todo add equity ticker listener
        watch.put(equity,NO_POSITION);
    }
    public Strategy getStrategy(){
        return strategy;
    }
    public void setStrategy(Strategy  str){
        this.strategy = str;
    }
    @Override
    public void handleTickerChange(Equity equity,Ticker ticker) {
        if(ticker==null){
            return;
        }
        if(watch.get(equity)==POSITION_TAKEN){
            canClosePosition(equity);
        }
        canOpenPosition(equity);
        
    }
    
    
    private void canOpenPosition(Equity equity) {        
        if(watch.get(equity)==NO_POSITION&&strategy.canEnter(equity)){
            totalEntry++;
            watch.put(equity, POSITION_TAKEN);
            entryDetails.put(equity.getId(), equity.getPointer());
        }
    }
    
    private void canClosePosition(Equity equity) {
        if(strategy.canExit(equity,entryDetails.get(equity.getId()))){
            watch.put(equity, NO_POSITION);
            totalExit++;
            computeProfit(equity);
        }
    }
    public void status(){
        System.out.println("TOTAL ENTRY :"+totalEntry);
        System.out.println("TOTAL EXIT :"+totalExit);
        System.out.println("TOTAL PROFIT TRADE :"+profitExit);
        System.out.println("MAXIMUM PROFIT TRADE :"+maxGain);
        System.out.println("MAXIMUM LOST TRADE :"+maxLoss);
        System.out.println("TOTAL LOSS TRADE :"+lostExit);
        System.out.println("AVERAGE HOLD DURATION :"+tradeDuration/totalExit);
        System.out.println("AVERAGE GAIN FROM WON TRADE:"+gainTrade/profitExit);
        System.out.println("AVERAGE loss FROM lost TRADE:"+lossTrade/lostExit);
        System.out.println("GROSS PROFIT IN PERCENTAGE :"+gain);
        System.out.println("PROFIT RATIO: "+100*((float)profitExit/(float)(profitExit+lostExit)));
        stocksHolding();
//        printResult();
    }
    private void stocksHolding(){
        System.out.println("**********HOLDINGS****************");
        for(Equity eq:watch.keySet()){
            if(watch.get(eq).equals(POSITION_TAKEN)){
                Ticker bought = eq.getList().get(entryDetails.get(eq.getId()));
                System.out.println("HOLDS stock "+eq.getName()+" from "+bought.getDate()+" current price:"+bought.getClosePrice());
                System.out.println("Target:"+Util.findTargetPrice(bought.getClosePrice(), .8f));
            }
        }
    }
    private void computeProfit(Equity equity) {
        int pointer = entryDetails.get(equity.getId());
        Ticker entryTicker = equity.getList().get(pointer);
        Ticker currentTicker = equity.getTicker();
        if(entryTicker.getDate().equals(currentTicker.getDate())) return; //same date comparison
        if(strategy.getStrategyType()== StrategyType.LONG){
            computeProfiteFromLong(equity,entryTicker,currentTicker);
        }else{
            computeProfitFromShort(equity,entryTicker,currentTicker);
        }
        tradeDuration +=equity.getPointer()-pointer;
    }

    private void computeProfiteFromLong(Equity equity, Ticker entryTicker, Ticker exitTicker) {
        float boughtPrice = strategy.getOpenPrice(equity,entryDetails.get(equity.getId()));
        float sellPrice = strategy.getClosePrice(equity,entryDetails.get(equity.getId()));
        Util.print("Bought "+equity.getId()+" at price "+boughtPrice+" on "+entryTicker.getDate()+"\nSold at price "+sellPrice+ " on "+exitTicker.getDate());
        float change=Util.findPercentageChange(sellPrice ,boughtPrice);
        System.out.println("Profit/Loss from "+equity.getName()+" "+change);
        gain = gain+change;
        if(change>maxGain){
            maxGain = change;
        }
        if(change<maxLoss){
            maxLoss = change;
        }
        if(change>.1){
            addToProfit(equity,true);
            gainTrade+=change;
            profitExit++;
        }else{
            addToProfit(equity,false);
            lossTrade+=change;
            lostExit++;
        }
        
    }

    private void computeProfitFromShort(Equity equity, Ticker entryTicker, Ticker exitTicker) {
        float soldPrice = strategy.getOpenPrice(equity,entryDetails.get(equity.getId()));
        float buybackprice = strategy.getClosePrice(equity,entryDetails.get(equity.getId()));
        Util.print("Sold "+equity.getId()+" at price "+soldPrice+" on "+entryTicker.getDate()+" bought back at price "+buybackprice+ " on "+exitTicker.getDate());
        float change=Util.findPercentageChange(soldPrice,buybackprice);
        System.out.println("Profit/Loss from "+equity.getName()+" "+change);
        gain = gain+change;
        if(change>maxGain){
            maxGain = change;
        }
        if(change<maxLoss){
            maxLoss = change;
        }
        if(change>0.5){
            addToProfit(equity,true);
            gainTrade+=change;
            profitExit++;
        }else{
            addToProfit(equity,false);
            lossTrade+=change;
            lostExit++;
        }
    }

    private void addToProfit(Equity equity, boolean isProfit) {
       Result res = result.get(equity);
       if(res==null){
           res = new Result();
           result.put(equity, res);
       }
       if(isProfit){
           res.profit++;
       }else{
           res.loss++;
       }
    }

    private void printResult() {
        System.out.println("\n\tSTATISTICS\n");
        System.out.println("NAME\t\tProfit\t\tLoss");
        result.entrySet().stream().forEach((item) -> {
            String name = item.getKey().getName();
            int pCount = item.getValue().profit;
            int lCount = item.getValue().loss;
            System.out.println(name+"\t\t\t"+pCount+"\t\t"+lCount);
        });
    }
    
    
}
