/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.strategy;

import com.rhino.data.Equity;
import com.rhino.data.Ticker;

/**
 *
 * @author agulshan
 */
public interface Strategy {
    
     boolean canEnter(Equity equity);
     /*
     pointer 
     */
     boolean canExit(Equity equity,int entryPointer);
    /*
    it defines from what point of the start date the stragtegy should run
    if the strategy needs previous day value to compute the strategy, then least data requirments will be one 1.
    Or if it require 3 days moving average then the least data requirments is 3. That is strategy starts from the 4th day
    */
     int  leastDataRequired();
    /*
     This is required for plotting graphs. The entry strategy can be based on this value.
     In an ideal situation the graph plotted by this value looks similar to the chart of the equity
     */ 
    float computeValue();
    
    StrategyType getStrategyType();

    /*
    Strategy should handle which price is under observation for example openPrice, closePrice etc..
    */
    float getOpenPrice(Equity equity,int entryPointer);
    float getClosePrice(Equity equity,int entryPointer);
}
