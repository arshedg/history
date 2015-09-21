/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.data.processor;

import com.fluke.model.Trade;
import org.junit.Test;

/**
 *
 * @author arsh
 */
public class TradeExecutorTest {
    
    public TradeExecutorTest() {
    }

    @Test
    public void testCancelChildTrade() {
        Trade t1 = new Trade();
        t1.parentTrade = 1;
    }
    
}
