/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.data.processor;

import com.fluke.model.ticker.Ticker;

/**
 *
 * @author arshed
 */
public interface TickerListener {
    public void listen(String id,Ticker eq);
    public void endOfData(String id,Ticker eq);
}
