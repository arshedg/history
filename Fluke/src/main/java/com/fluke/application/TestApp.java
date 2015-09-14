/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.application;

import com.fluke.data.processor.TradeExecutor;
import com.fluke.model.StrategyManager;
import com.fluke.util.Configuration;
import java.sql.SQLException;
import java.util.Arrays;

/**
 *
 * @author arshed
 */
public class TestApp {
    public static void main(String[] args) throws SQLException {
        TradeExecutor executor = new TradeExecutor();
        Configuration config = Configuration.getDefaultConfiguration(executor, "2012-7-26", "2013-2-1");
       // config.equities=Arrays.asList("GRASIM");
        for(int i=0;i<1;i++){
            StrategyManager manager = new StrategyManager(config, new SampleS());
            config.executor.setOrderListener(manager);
            manager.run();
            config.extendEndDate();
        }
        config.executor.status();
        
        
    }
}
