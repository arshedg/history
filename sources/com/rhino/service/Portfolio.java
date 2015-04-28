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
public class Portfolio implements TickerChangeListener{
    
    //todo use enums for position status
    private static final Boolean POSITION_TAKEN =true;
    private static final Boolean NO_POSITION =false;
    Map<Equity,Boolean> watch = new HashMap<>();
    Map<Equity,Integer> entryDetails = new HashMap<>();
    List<Equity> openPositions = new ArrayList<>();
    private Strategy strategy;
    private  float gain=0f;
    private int totalEntry,totalExit,profitExit,lostExit;
    private float maxGain=Float.MIN_VALUE;
    private float maxLoss = Float.MAX_VALUE;
    
    public void addToWatch(Equity equity){
        //todo add equity ticker listener
        watch.put(equity,NO_POSITION);
    }

    @Override
    public void handleTickerChange(Equity equity,Ticker ticker) {
        canOpenPosition(equity);
        if(watch.get(equity)==POSITION_TAKEN){
            canClosePosition(equity);
        }
    }
    
    
    private void canOpenPosition(Equity equity) {        
        if(strategy.canEnter(equity)){
            totalEntry++;
            watch.put(equity, POSITION_TAKEN);
        }
    }
    
    private void canClosePosition(Equity equity) {
        if(strategy.canExit(equity)){
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
        System.out.println("GROSS PROFIT IN PERCENTAGE :"+gain);
        
    }
    private void computeProfit(Equity equity) {
        int pointer = entryDetails.get(equity);
        Ticker entryTicker = equity.getList().get(pointer);
        Ticker currentTicker = equity.getTicker();
        if(strategy.getStrategyType()== StrategyType.LONG){
            computeProfiteFromLong(equity,entryTicker,currentTicker);
        }else{
            computeProfiteFromShort(equity,entryTicker,currentTicker);
        }
    }

    private void computeProfiteFromLong(Equity equity, Ticker entryTicker, Ticker exitTicker) {
        float boughtPrice = strategy.getPrice(entryTicker);
        float sellPrice = strategy.getPrice(exitTicker);
        Util.print("Bought "+equity.getId()+" at price "+boughtPrice+" on "+entryTicker.getDate()+"\tSold at price "+exitTicker.getClosePrice()+ " on "+exitTicker.getDate());
        float change=Util.findPercentageChange(boughtPrice ,sellPrice);
        gain = gain+change;
        if(change>maxGain){
            maxGain = change;
        }
        if(change<maxLoss){
            maxLoss = change;
        }
        if(change>0){
            profitExit++;
        }else{
            lostExit++;
        }
        
    }

    private void computeProfiteFromShort(Equity equity, Ticker entryTicker, Ticker currentTicker) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
