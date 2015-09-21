package com.fluke.model;


import com.fluke.model.ticker.IntradayTicker;

public class RealtimeTicker extends IntradayTicker{
    long id;
    public long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id = id;
    }
}