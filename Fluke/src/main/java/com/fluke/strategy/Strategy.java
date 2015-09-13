/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.strategy;

import com.fluke.model.Equity;
import com.fluke.model.Index;
import com.fluke.model.Trade;

/**
 *
 * @author arshed
 */
public interface Strategy {
    
    Trade openPosition(Equity eq,Index index);
    Trade closePosition(Equity eq,Index index,int entryPoint);
    boolean isLong();
}
