/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data;

import com.rhino.Listener.TickerChangeListener;
import com.rhino.data.db.TickerDao;
import com.rhino.data.history.util.Util;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author agulshan
 */
public class Equity {
    private String id;
    private String name;
    private Group group;
    private List<Ticker> list;
    private int pointer;
    List<TickerChangeListener> liseteners = new ArrayList<>();
    private Equity(){
        
    }
    boolean isCallingListener = false;
    List<TickerChangeListener> toDelete = new ArrayList<>();
    public void addListener(TickerChangeListener listener){
        isCallingListener=true;
        liseteners.add(listener);
        isCallingListener=false;
        deleteListeners();
    }
    public void removeListener(TickerChangeListener listener){
        if(isCallingListener){
            toDelete.add(listener);
        }else{
            liseteners.remove(listener);
        }
    }
    public static Equity loadEquity(String id,String fromDate,String toDate){
        try {
            Equity equity = new Equity();
            equity.setId(id);
            equity.setName(id);
            TickerDao dao = new TickerDao();
            equity.list = dao.getTickers(id, Util.getDate(fromDate), Util.getDate(toDate));
            return equity;
        } catch (ParseException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public List<Ticker> getList(){
        return list;
    }
    public int getPointer(){
        return pointer;
    }
    public void resetToBegining(){
        pointer=0;
    }
    
    public void resetToEnd(){
        pointer=list.size()-1;
    }
    /*
    This will set the equity date to that date
    */
    public void setDate(Date date){
      
    }
    
    public Ticker getTickerBeforeNDays(int days){
       return list.get(pointer-days);
    }
    
    public Ticker getTickerAfterNDays(int days){
       return list.get(pointer+days);
    }
    
    public boolean hasNext(){
        return pointer+1<list.size();
    }
    public Ticker getNextTicker(){
        if(!hasNext()){
            return null;
        }
        Ticker ticker = list.get(++pointer);
        
        invokeListners();
        return ticker;
    }
    public Ticker getTicker(){
        return list.get(pointer);
    }

    public Ticker getHighPrice(int days,boolean highest){
        int ptr = pointer;
        int count=0;
         Ticker highTicker = getTicker();
        while(count<days){
            Ticker ticker = list.get(ptr--);
            if(highest&&(ticker.getHighPrice()>highTicker.getHighPrice())){
                highTicker = ticker;
            }
            else if(ticker.getClosePrice()>highTicker.getClosePrice()){
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
         Ticker lowTicker = getTicker();
        while(count<days){
            Ticker ticker = list.get(ptr--);
            if(lowest&&(ticker.getLowPrice()<lowTicker.getLowPrice())){
                lowTicker = ticker;
            }
            else if(ticker.getClosePrice()<lowTicker.getClosePrice()){
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
        Ticker currentTicker = getTicker();
        float currentClosePrice = currentTicker.getClosePrice();
        Ticker tickerAfterNDays = null;
        while(count<noOfDaysAgo){
            tickerAfterNDays = list.get(ptr--);
            count++;
        }
        float closeNPeriodsAgo = tickerAfterNDays.getClosePrice();
        return 100*((currentClosePrice-closeNPeriodsAgo)/closeNPeriodsAgo);
        
    }
    public float rateOfChangeOfVolume(int days){
        int prevsPtr = pointer-days;
        float volumeOfNthDay = list.get(prevsPtr).getVolume();
        float currentVolume = this.getTicker().getVolume();
        return 100*((currentVolume-volumeOfNthDay)/volumeOfNthDay);
    }
    private boolean isInvalidTicker(Ticker ticker){
        return ticker.getOpenPrice()==ticker.getClosePrice()&&ticker.getVolume()==0;
    }
    public float simpleMovingAverage(int days){
        int startDay = 1+(pointer-days);
        float sum=0f;
        for(int i=startDay;i<=pointer;i++){
            sum+=list.get(i).getClosePrice();
        }
        return sum/days;
    }
    public float simpleAverageVolume(int days){
        int startDay = 1+(pointer-days);
        float sum=0f;
        for(int i=startDay;i<=pointer;i++){
            sum+=list.get(i).getVolume();
        }
        return sum/days;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    private void invokeListners() {
        for(TickerChangeListener listener: liseteners){
            listener.execute(this);
        }
    }

    private void deleteListeners() {
       liseteners.removeAll(toDelete);
    }
}
