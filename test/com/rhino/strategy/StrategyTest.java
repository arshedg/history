/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhino.strategy;

import com.rhino.data.Equity;
import com.rhino.data.Ticker;
import com.rhino.data.db.EquityDao;
import com.rhino.data.history.util.Util;
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
        String from = "2012-04-15";
        String to="2013-05-10";
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
        if(rcAchieved){
            System.out.println("Name "+equity.getName()+" date"+equity.getTicker().getDate()+" price:"+equity.getTicker().getClosePrice());
        }
        return rcAchieved;

    }

    @Override
    public boolean canExit(Equity equity,int pointer) {
        boolean exitPoint = equity.rateOfChange(12) > 3.2;
        int holdPeriod = equity.getPointer()-pointer;
        return exitPoint||holdPeriod>1;
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
    public float getOpenPrice(Equity equity,int pointer) {
        return equity.getList().get(pointer).getClosePrice();
    }
    @Override
    public float getClosePrice(Equity ticker,int pointer) {
        return ticker.getTicker().getClosePrice();
    }

}
