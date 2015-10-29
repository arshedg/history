/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.model;

import com.fluke.data.processor.IDGenerator;

/**
 *
 * @author arshed
 */
public class Trade {
    
    public long id = IDGenerator.generateId();
    public String equity;
    public float openPrice;
    public boolean isLong;
    public boolean isAtMarketPrice = true;
    public String strategy;
    public float target;
    public float triggerPrice;
    public float executedPrice;
    public long parentTrade;
    public boolean cancelRelatedTrade = false;
    public boolean isPaused=false;
    public float exitPrice=0f;
    public static Trade limitTrade(float openPrice,float target,float stopLoss){
        Trade trade = new Trade();
        trade.openPrice = openPrice;
        trade.isAtMarketPrice = false;
        trade.target = target;
        trade.exitPrice = stopLoss;
        return trade;
    }
    /*
    do we really need encapsulation everywhere
    */
}

