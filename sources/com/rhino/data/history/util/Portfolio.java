/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.history.util;

import com.rhino.Listener.TickerChangeListener;
import com.rhino.Listener.strategy.ExitStrategy;
import com.rhino.data.Equity;
import com.rhino.data.Ticker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author agulshan
 */
public class Portfolio implements TickerChangeListener{

    List<Equity> equities = new ArrayList<>();
    Map<Equity,Integer> map = new HashMap<>();
    ExitStrategy strategy;
    boolean isLong;
    float percentageGain=0f;
    public static float grossProfit=0f;
    public void add(Equity equity){
        if(map.containsKey(equity)) return;
        equities.add(equity);
        equity.addListener(this);
        map.put(equity,equity.getPointer());
  
    }
    public void setExitStrategy(ExitStrategy strategy){
        this.strategy = strategy;
    }
    public void setType(boolean isLong){
        this.isLong = isLong;
    }
    public void calculateProfit(int days){
        float investment = 0f;
        float returns = 0f;
        for(Equity equity:map.keySet()){
            List<Ticker> list = equity.getList();
            int pointer = map.get(equity);
            Ticker ticker = list.get(pointer);
            float boughtPrice = ticker.getClosePrice();
            if(pointer+days>=list.size()) continue;
            Ticker endTicker = list.get(pointer+days);
            Util.print("Bought "+equity.getId()+" at price "+boughtPrice+" on "+ticker.getDate()+"\tSold at price "+endTicker.getClosePrice()+ " on "+endTicker.getDate());
            investment+=Util.findPercentageChange(endTicker.getClosePrice() ,boughtPrice);
        }
       grossProfit+=investment;
        System.out.println(" Profit/loss in the transaction:"+investment);
    }

    @Override
    public void execute(Equity equity) {
        if(map.containsKey(equity)&&strategy.canExit(equity)){
            computeProfit(equity);
            //equity.removeListener(this);
            map.remove(equity);
        }
    }

    private void computeProfit(Equity equity) {
        int ptr = map.get(equity);
        Ticker entryTicker = equity.getList().get(ptr);
        Ticker currentTicker = equity.getTicker();
        float currentProfit=0f;
        if(!isLong){
            float boughtPrice = entryTicker.getClosePrice();
            float currentPrice = currentTicker.getClosePrice();
            Util.print("Bought "+equity.getId()+" at price "+boughtPrice+" on "+entryTicker.getDate()+"\tSold at price "+currentTicker.getClosePrice()+ " on "+currentTicker.getDate());
            grossProfit+=currentProfit=Util.findPercentageChange(currentPrice ,boughtPrice);
            System.out.println("Profit/Loss from "+equity.getName()+" "+currentProfit);
         }
    }
    public void status(){
        Util.print("Stocks which are not yet exited");
        for(Equity equity:map.keySet()){
            System.out.println(equity.getId());
        }
        System.out.println("Gross profit/loss:"+grossProfit);
    }
    

    
}
