/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data;

import com.rhino.data.history.util.Util;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author agulshan
 */
public class Ticker {
    private float openPrice;
    private float closePrice;
    private float highPrice;
    private float lowPrice;
    private float adjustedClose;
    private int volume;
    private Date date;
    private boolean dividend;
    private int pointer;
    public boolean dividendIssued(){
        if(closePrice!=adjustedClose){
            return true;
        }
        return false;
    }
    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    public float getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(float closePrice) {
        this.closePrice = closePrice;
    }

    public float getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(float highPrice) {
        this.highPrice = highPrice;
    }

    public float getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(float lowPrice) {
        this.lowPrice = lowPrice;
    }

    public float getAdjustedClose() {
        return adjustedClose;
    }

    public void setAdjustedClose(float adjustedClose) {
        this.adjustedClose = adjustedClose;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDividend() {
        return dividend;
    }

    public void setDividend(boolean dividend) {
        this.dividend = dividend;
    }

    public int getPointer() {
        return pointer;
    }

    public void setPointer(int pointer) {
        this.pointer = pointer;
    }
    
    /*
    you cant equals 
    */
    @Override
    public boolean equals(Object object){
        Ticker other = (Ticker)object;
        if(this.date.equals(other.date)){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Float.floatToIntBits(this.openPrice);
        hash = 47 * hash + Float.floatToIntBits(this.closePrice);
        hash = 47 * hash + Float.floatToIntBits(this.highPrice);
        hash = 47 * hash + Float.floatToIntBits(this.lowPrice);
        hash = 47 * hash + Float.floatToIntBits(this.adjustedClose);
        hash = 47 * hash + this.volume;
        hash = 47 * hash + Objects.hashCode(this.date);
        hash = 47 * hash + (this.dividend ? 1 : 0);
        hash = 47 * hash + this.pointer;
        return hash;
    }
    
    
}

