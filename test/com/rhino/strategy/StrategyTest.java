/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhino.strategy;

import com.rhino.data.Equity;
import com.rhino.data.Ticker;
import com.rhino.data.db.EquityDao;
import com.rhino.service.Portfolio;
import com.rhino.service.StrategyManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author arshed
 */
public class StrategyTest {

    
    private List<Equity> getAll() throws SQLException{
        String from = "2015-04-15";
        String to="2015-05-10";
        List<Equity> loaded = new ArrayList<>();
        for(String name:new EquityDao().getAllEquity()){
            loaded.add(Equity.loadEquity(name, from, to));
        }
        return loaded;
    }
    @Test
    public void runStrategy() throws SQLException {
        Strategy strategy = new ROCStrategy();
        StrategyManager manager = new StrategyManager();
        Portfolio pf = new Portfolio();
        pf.setStrategy(strategy);
        String from = "2015-04-15";
        String to="2015-05-10";
        manager.addStock(getAll());
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
