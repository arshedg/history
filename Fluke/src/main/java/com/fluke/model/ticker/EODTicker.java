/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.model.ticker;

/**
 *
 * @author arshed
 */
public class EODTicker extends Ticker {

    private float adjustedClose;
    private boolean dividend;

    public float getAdjustedClose() {
        return adjustedClose;
    }

    public boolean isDividend() {
        return dividend;
    }

    public void setDividend(boolean dividend) {
        this.dividend = dividend;
    }

    public void setAdjustedClose(float adjustedClose) {
        this.adjustedClose = adjustedClose;
    }
}
