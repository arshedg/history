/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.model;

import com.fluke.data.processor.IDGenerator;

/**
 *
 * @author arshed
 */
public class Trade {
    
    public long id = IDGenerator.generateId();
    public String equity;
    public float openPrice;
    public boolean isLong;
    public boolean isAtMarketPrice;
    public String strategy;
    
    /*
    do we really need encapsulation everywhere
    */
}

