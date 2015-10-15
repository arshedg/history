/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.model;

import com.fluke.database.dataservice.EODDao;
import com.fluke.database.dataservice.IntraDayDao;
import com.fluke.model.ticker.EODTicker;
import com.fluke.model.ticker.Ticker;
import com.fluke.util.Configuration;
import com.fluke.util.Util;
import java.text.ParseException;
import java.util.List;

/**
 *
 * @author arshed
 */
public final class Equity {
    public TickerList intraday;
    public TickerList eod;
    private float prevsPrice;
    String name;
    Configuration config;
    public Equity(String equity,Configuration config){
        try {
            EODDao dao = new EODDao();
            this.name= equity;
            this.config = config;
            List<EODTicker> tickers;
            try {
                tickers  = dao.getEODTickers(equity, Util.getDate(config.startDate), Util.getDate(config.endDate));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            eod = new TickerList(equity, null);
            //System.out.println(equity);
           
            intraday = new TickerList(equity, config.dataSource);
            if(!intraday.isEmpty()){ 
                prevsPrice = getPrevsPrice(config.endDate);
            }
            eod.addAll(tickers);
            if(!tickers.isEmpty()&&tickers.get(tickers.size()-1).getDate().equals(Util.getDate(config.endDate))){
                eod.remove(tickers.get(tickers.size()-1));
                eod.add(new EODTicker());
            }
            eod.movePointerToEnd();
            
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        
    }
    public float getPrevsPrice(){
        if(prevsPrice==0f){
            prevsPrice = getPrevsPrice(config.endDate);
        }
        return prevsPrice;
    }
    private float getPrevsPrice(String date){
        IntraDayDao dao = new IntraDayDao();
        return dao.getPrevsDayPrice(name, date);
        
    }
    public String getName(){
        return this.name;
    }

    /**
     * @return the intraday
     */
    public TickerList getIntraday() {
        return intraday;
    }

    /**
     * @return the eod
     */
    public TickerList getEod() {
        return eod;
    }


}
