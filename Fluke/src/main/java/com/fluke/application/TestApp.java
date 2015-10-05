/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.application;

import com.fluke.data.processor.ReatimeDBReader;
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
       ReatimeDBReader reader = new ReatimeDBReader();
       // reader.startThread();
    Configuration config = Configuration.getDefaultConfiguration(executor, "2015-1-26", "2015-9-16");
    config.dataSource = reader;
   // Configuration config = Configuration.getDefaultConfiguration(executor, "2013-1-26", "2013-2-2");
  //    config.equities=reader.getAllEquity();
  // config.equities=Arrays.asList("HAVELLS");
        for(int i=0;i<2;i++){
            StrategyManager manager = new StrategyManager(config,new Lion(),new LionHealer(),new Vulture(),new VultureHealer());
            config.executor.setOrderListener(manager);
            manager.run();
            config.extendEndDate();
        }
        config.executor.status();
        
//        
    }
}
