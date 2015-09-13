/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.data.processor;

import com.fluke.database.dataservice.IntraDayDao;
import com.fluke.model.ticker.IntradayTicker;
import com.fluke.model.ticker.Ticker;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author arshed
 */
public class IntradayTickerFromDataBase implements TickerDataSource{

    Map<String,List<IntradayTicker>> tickerMap = new HashMap<>();
    IntraDayDao dao = new IntraDayDao();
    String date;
    public IntradayTickerFromDataBase(String date) {
        this.date = date;
    }
    
    @Override
    public Ticker getNextTicker(String equity) {
        List<IntradayTicker> tickers = tickerMap.get(equity);
        if(tickers==null){
            tickers = dao.getIntraday(equity, date);
            tickerMap.put(equity, tickers);
        }
        Ticker ticker = tickers.get(0);
        tickers.remove(0);
        return ticker;
    }


    @Override
    public boolean hasNext(String equity) {
        List<IntradayTicker> tickers = tickerMap.get(equity);
        if(tickers==null){
            tickers = dao.getIntraday(equity, date);
            tickerMap.put(equity, tickers);
        }
        return !tickers.isEmpty();
    }
    
}
