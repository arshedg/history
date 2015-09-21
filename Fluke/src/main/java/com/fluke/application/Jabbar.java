/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.application;

import com.fluke.model.Equity;
import com.fluke.model.Index;
import com.fluke.model.Trade;

/**
 *
 * @author arsh
 */
public class Jabbar extends Cloud {

    @Override
    public Trade closePosition(Equity eq, Index index, int entryPoint, Trade executedTrade) {
        if(Rain.rainLIst.contains(eq.getName())){
            Rain.rainLIst.remove(eq.getName());
            Trade trade = new Trade();
            trade.isAtMarketPrice = true;
            return trade;
        }
        return null;
    }
    
}
