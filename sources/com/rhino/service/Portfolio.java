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
    private float gain=0f;
    
    public void addToWatch(Equity equity){
        //todo add equity ticker listener
        watch.put(equity,NO_POSITION);
    }

    @Override
    public void handleTickerChange(Equity equity) {
        canOpenPosition(equity);
        if(watch.get(equity)==POSITION_TAKEN){
            canClosePosition(equity);
        }
    }

    private void canOpenPosition(Equity equity) {        
        if(strategy.canEnter(equity)){
            watch.put(equity, POSITION_TAKEN);
        }
    }
    
    private void canClosePosition(Equity equity) {
        if(strategy.canExit(equity)){
            watch.put(equity, NO_POSITION);
            computeProfit(equity);
        }
    }

    private void computeProfit(Equity equity) {
        int pointer = entryDetails.get(equity);
        Ticker entryTicker = equity.getList().get(pointer);
        Ticker currentTicker = equity.getTicker();
        if(strategy.getStrategyType()== StrategyType.LONG){
            computeProfiteFromLong(equity,entryTicker,currentTicker);
        }
    }

    private void computeProfiteFromLong(Equity equity, Ticker entryTicker, Ticker exitTicker) {
        float boughtPrice = strategy.getPrice(entryTicker);
        float sellPrice = strategy.getPrice(exitTicker);
        Util.print("Bought "+equity.getId()+" at price "+boughtPrice+" on "+entryTicker.getDate()+"\tSold at price "+exitTicker.getClosePrice()+ " on "+exitTicker.getDate());
        gain+=Util.findPercentageChange(boughtPrice ,sellPrice);
        
    }
    
    
}
