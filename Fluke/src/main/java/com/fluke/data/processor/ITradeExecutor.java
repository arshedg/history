/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.data.processor;

import com.fluke.model.OrderExecuteListener;
import com.fluke.model.Trade;

/**
 *
 * @author arsh
 */
public interface ITradeExecutor extends TickerListener{
    public void placeOrder(Trade trade);
    public void cancelTrade(Trade trade);
    public void setOrderListener(OrderExecuteListener listener);
    public void status(); 
    
}
