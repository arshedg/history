/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.model;

import com.fluke.database.dataservice.EODDao;
import com.fluke.model.ticker.EODTicker;
import com.fluke.util.Configuration;
import com.fluke.util.Util;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arshed
 */
public class Equity {
    public TickerList intraday;
    public TickerList eod;
    String name;
    public Equity(String equity,Configuration config){
        try {
            EODDao dao = new EODDao();
            List<EODTicker> tickers;
            try {
                tickers  = dao.getEODTickers(equity, Util.getDate(config.startDate), Util.getDate(config.endDate));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            eod = new TickerList(equity, null);
            intraday = new TickerList(equity, config.dataSource);
            eod.addAll(tickers);
            if(!tickers.isEmpty()&&tickers.get(tickers.size()-1).getDate().equals(Util.getDate(config.endDate))){
                eod.remove(tickers.get(tickers.size()-1));
                eod.add(new EODTicker());
            }
            eod.movePointerToEnd();
            this.name= equity;
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        
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
