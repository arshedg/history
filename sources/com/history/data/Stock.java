/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.history.data;

import com.history.dao.StockDao;
import com.rhino.data.history.util.Util;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author agulshan
 */
public class Stock {
    static StockDao dao = new StockDao();
    private List<Tick> list;
    private Date currentDate;
    private int counter=0;
    private String id;
    private float highestPrice = -1;
    private float leastPrice = -1;
    private int volume =0;
    public static Stock loadStock(String id,String date) throws ParseException, SQLException{
        Stock stock = new Stock();
        Date currDate = Util.getDate(date);
        stock.currentDate = currDate;
        stock.id = id;
        stock.list=dao.getStock(id, currDate);
        if(stock.list.isEmpty()){
            return null;
        }
        stock.initCalculations();
        return stock;
    }

    public String getDate(){
        return Util.getDate(currentDate);
    }
    public Tick getNextTick(){
        if(counter<list.size()){
            return list.get(counter++);
        }
        return null;
    }
    public Tick getOldTick(int days){
        if(counter-days<0){
            return null;
        }
        return list.get(counter-days);
    }
    
    private void initCalculations(){
        float high=-1;
        int totalVolume=0;
        float least = Float.MAX_VALUE;
        for(Tick tick:list){
            float price=tick.getPrice();
            if(price>high){
                high=price;
            }
            if(price<least){
                least = price;
            }
            totalVolume+=tick.getVolume();
        }
        this.highestPrice = high;
        this.leastPrice = least;
        this.volume = totalVolume;
        
    }
    public Stock getNextDay() throws Exception{
        for(int i=1;i<7;i++){
            String nextDate = Util.addDate(currentDate, i);
            Stock stock = loadStock(id,nextDate);
            if(stock!=null){
                return stock;
            }
        }
        return null;
    }
    public boolean hasNext(){
        return counter<list.size() ;
    }
    public float getHighestPrice() {
        return highestPrice;
    }

    public float getLeastPrice() {
        return leastPrice;
    }

    public int getVolume() {
        return volume;
    }
}
