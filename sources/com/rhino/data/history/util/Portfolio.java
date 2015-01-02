/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.history.util;

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
public class Portfolio {

    List<Equity> equities = new ArrayList<>();
    Map<Equity,Integer> map = new HashMap<>();
    public static float grossProfit=0f;
    public void add(Equity equity){
        equities.add(equity);
        map.put(equity,equity.getPointer());
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
    

    
}
