/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data;

import com.rhino.data.db.TickerDao;
import com.rhino.data.history.util.Util;
import java.sql.SQLException;
import java.text.ParseException;
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
    
    private Equity(){
        
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
        return pointer-1;
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
    public boolean hasNext(){
        return pointer<list.size();
    }
    public Ticker getNextTicker(){
        if(!hasNext()){
            return null;
        }
        Ticker ticker =list.get(pointer++);
        while(ticker.getOpenPrice()==ticker.getClosePrice()&&ticker.getVolume()==0){
            if(hasNext()){
                ticker = list.get(pointer++);
            }
            else{
                return null;
            }
        }
        return ticker;
    }
    public Ticker getTicker(){
        return list.get(pointer);
    }
    public Ticker getNextTicker(int ptr){
        if(!hasNext()){
            return null;
        }
        Ticker ticker =list.get(ptr++);
        while(ticker.getOpenPrice()==ticker.getClosePrice()&&ticker.getVolume()==0){
            if(hasNext()){
                ticker = list.get(ptr++);
            }
            else{
                return null;
            }
        }
        ticker.setPointer(ptr);
        return ticker;
    }

    public Ticker getHighPrice(int days,boolean highest){
        int ptr = pointer;
        int count=0;
         Ticker highTicker = getTicker();
        while(count<days){
            if(ptr==0){
                break;
            }
            Ticker ticker = list.get(ptr--);
            if(isInvalidTicker(ticker)){
                continue;
            }
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
            if(ptr==0){
                break;
            }
            Ticker ticker = list.get(ptr--);
            if(isInvalidTicker(ticker)){
                continue;
            }
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
    private boolean isInvalidTicker(Ticker ticker){
        return ticker.getOpenPrice()==ticker.getClosePrice()&&ticker.getVolume()==0;
    }
    
    public Ticker getTickerAfter(int days){
        int ptr = this.getPointer();
        Ticker tick=null;
        for(int i=0;i<days;i++){
            tick = this.getNextTicker(ptr);
            if(tick==null){
                return null;
            }
            ptr = tick.getPointer();
        }
        return tick;
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
}
