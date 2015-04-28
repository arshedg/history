/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.listener;

import com.rhino.data.Equity;





/**
 *
 * @author agulshan
 */
public interface TickerChangeListener {
    
    void handleTickerChange(Equity equity);
    
}
