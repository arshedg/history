/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.application;

import com.fluke.data.processor.RealTimeExecutor;
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
      Configuration config = Configuration.getDefaultConfiguration(executor, "2015-10-1", "2015-10-29");
      config.dataSource = reader;
    //   config.executor = new RealTimeExecutor();
     //     Configuration config = Configuration.getDefaultConfiguration(executor, "2013-1-26", "2013-3-2");
        //    config.equities=reader.getAllEquity();
//config.equities = Arrays.asList("PETRONEt");
        for (int i = 0; i < 1; i++) {
            StrategyManager manager = new StrategyManager(config, new S1());
            config.executor.setOrderListener(manager);
            System.out.println("Running for "+config.endDate);
            manager.run();
            config.extendEndDate();
        }
        config.executor.status();

//        
    }
}
