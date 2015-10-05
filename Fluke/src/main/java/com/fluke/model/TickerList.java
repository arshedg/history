/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.model;


import com.fluke.model.ticker.Ticker;
import com.fluke.data.processor.TickerDataSource;
import com.fluke.realtime.data.DataStreamLostException;
import com.fluke.util.Util;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author arshed
 * @param <T>
 */
public class TickerList extends ArrayList<Ticker>{
    int pointer=-1; 
    private final TickerDataSource source;
    private String name;
    boolean isMarketOpen=false;
    boolean isMaketClosed=false;
    int volume=0;

    public TickerList(String equity,TickerDataSource source){
        this.source = source;
        this.name = equity;
    }
   void movePointerToEnd(){
       this.pointer=this.size()-1;
   }
   void setPointer(int ptr){
       this.pointer = ptr;
   }
   Ticker getPreviousTicker(){
       if(pointer>0){
           return this.get(pointer-1);
       }
       return null;
   }
     Ticker getNextTicker(){
         if(isMaketClosed||!source.hasNext(name)){
             throw new DataStreamLostException();
         }
         if(pointer<this.size()-1){
             return this.get(pointer++);
         }
         Ticker tick ;
         if(!isMarketOpen){
             tick = waitForMarket();
         }else{
             tick =  source.getNextTicker(name);
            
         }
        if(tick == null){
            return null;
        }
        volume+=tick.getVolume();
        if(isMarketClosed(tick.getDate())){
            return null;
        }
        this.add(tick);
        pointer++;
        return tick;
    }
    public int getCurrentVolume(){
        return volume;
    }
    boolean isMarketClosed(Date date){
        String hourMin[] = Util.getTime(date).split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int min = Integer.parseInt(hourMin[1]);
        return hour>=15&&min>29;
    }
    void rewind(){
        pointer--;
        if(pointer<0){ 
            throw new RuntimeException("cannot rewind further");
        
        }
    }
    int getPointer(){
        return pointer;
    }
    public Ticker waitForMarket(){
        Ticker ticker ;
        int hour=0,minute=0;
        do{
            ticker = source.getNextTicker(name);
            if(ticker==null) return null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ticker.getDate());
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }while(!(hour>=9&&minute>=15));
        isMarketOpen=true;
        return ticker;
    }
    public Ticker getTickBeforeNdays(int days){
        return this.get(pointer-days);
    }
    public boolean hasMore(){
        if(source==null||isMaketClosed){
            return false;
        }
        return source.hasNext(name);
    }
    public Ticker getCurrentTicker(){
        if(this.isEmpty()) return null;
        return this.get(pointer);
    }
      public Ticker getHighPrice(){
        return getHighPrice(this.size(), isMarketOpen);
    }
    public Ticker getLeastPrice(){
        return getLowPrice(this.size(), isMarketOpen);
    }
    public Ticker getHighPrice(int days,boolean highest){
        int ptr = pointer;
        int count=0;
         Ticker highTicker = getCurrentTicker();
        while(count<days){
            Ticker ticker = this.get(ptr--);
            if(highest&&(ticker.getHighPrice()>highTicker.getHighPrice())){
                highTicker = ticker;
            }
            else if(!highest&&ticker.getClosePrice()>highTicker.getClosePrice()){
                highTicker = ticker;
            }
            count++;
        }
        return highTicker;
    }
    /*
    if lowest is false, then it will be based on close price
    */
    public Ticker getLowPrice(int days,boolean lowest){
        int ptr = pointer;
        int count=0;
         Ticker lowTicker = getCurrentTicker();
        while(count<days){
            Ticker ticker = this.get(ptr--);
            if(lowest&&(ticker.getLowPrice()<lowTicker.getLowPrice())){
                lowTicker = ticker;
            }
            else if(!lowest&&ticker.getClosePrice()<lowTicker.getClosePrice()){
                lowTicker = ticker;
            }
            count++;
        }
        return lowTicker;
    }
    /*
    ROC = [(Close - Close n periods ago) / (Close n periods ago)] * 100
    */
    public  float rateOfChange(int noOfDaysAgo){
        int ptr = pointer;
        int count=0;
        Ticker currentTicker = getCurrentTicker();
        float currentClosePrice = currentTicker.getClosePrice();
        Ticker tickerAfterNDays = null;
        while(count<noOfDaysAgo){
            tickerAfterNDays = this.get(ptr--);
            count++;
        }
        float closeNPeriodsAgo = tickerAfterNDays.getClosePrice();
        return 100*((currentClosePrice-closeNPeriodsAgo)/closeNPeriodsAgo);
        
    }
    public float rateOfChangeOfVolume(int days){
        int prevsPtr = pointer-days;
        float volumeOfNthDay = this.get(prevsPtr).getVolume();
        float currentVolume = this.getCurrentTicker().getVolume();
        return 100*((currentVolume-volumeOfNthDay)/volumeOfNthDay);
    }

    public float simpleMovingAverage(int days){
        int startDay = 1+(pointer-days-1);
        float sum=0f;
        for(int i=startDay;i<pointer;i++){
            sum+=this.get(i).getClosePrice();
        }
        return sum/days;
    }
    public float simpleAverageVolume(int days){
        int startDay = 1+(pointer-days-1);
        float sum=0f;
        for(int i=startDay;i<pointer;i++){
            sum+=this.get(i).getVolume();
        }
        return sum/days;
    }
}
