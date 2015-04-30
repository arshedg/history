/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data;

import com.rhino.data.Equity;
import com.rhino.data.history.util.Util;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author agulshan
 */
public class EquityTest {
    @Test
    public void testPriceAfter(){
        Equity eq = Equity.loadEquity("tcs", "2010-04-12", "2010-12-12");
        Assert.assertEquals(true,eq.hasNext());
        Ticker tick = eq.getNextTicker();
        Util.print("date "+tick.getDate());
        Util.print("date "+eq.getTickerAfterNDays(5).getDate());
    }
    @Test
    public void highPrice(){
          Equity eq = Equity.loadEquity("tcs", "2010-04-12", "2010-12-12");
          eq.getNextTicker();eq.getNextTicker();eq.getNextTicker();eq.getNextTicker();
          Util.print("Highest price in next 5 days after 2010-april-12 is "+eq.getHighPrice(5,false).getClosePrice());
          Util.print("Highest price in next 5 days after 2010-april-12 is "+eq.getHighPrice(5,true).getClosePrice());
    }
            
}
