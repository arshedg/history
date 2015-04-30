/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhino.strategy;

import com.rhino.data.Equity;
import com.rhino.data.Ticker;
import com.rhino.data.history.util.Util;
import com.rhino.service.Portfolio;
import com.rhino.service.StrategyManager;
import org.junit.Test;

/**
 *
 * @author arshed
 */
public class StrategyTest {

    @Test
    public void runStrategy() {
        Strategy strategy = new ROCStrategy();
        StrategyManager manager = new StrategyManager();
        Portfolio pf = new Portfolio();
        pf.setStrategy(strategy);
        String from = "2002-03-05";
        String to="2015-03-01";
        manager.addStock(Equity.loadEquity("INFY", from, to));
        manager.setPortfolio(pf);
        manager.run();
    }
}

class ROCStrategy implements Strategy {
   //http://docs.rizm.io/rate-of-change
    @Override
    public boolean canEnter(Equity equity) {
        float rc = equity.rateOfChange(12);
        boolean rcAchieved = rc > -2.7 && rc < 0;
        return rcAchieved;

    }

    @Override
    public boolean canExit(Equity equity,int pointer) {
        boolean exitPoint = equity.rateOfChange(12) > 3.2;
 
        return exitPoint;
    }

    @Override
    public int leastDataRequired() {
        return 11;
    }

    @Override
    public float computeValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StrategyType getStrategyType() {
        return StrategyType.LONG;
    }

    @Override
    public float getPrice(Ticker ticker) {
        return ticker.getClosePrice();
    }

}
