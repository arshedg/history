/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.realtime.data;

import com.fluke.data.intraday.IntradayDetails;
import com.fluke.database.dataservice.IntraDayDao;
import com.fluke.model.ticker.IntradayTicker;
import com.google.gson.Gson;

/**
 *
 * @author arsh
 */
public class RealtimeParser implements Runnable{
    
    IntraDayDao dao = new IntraDayDao();
    String name;
    public void insert(IntradayTicker ticker){
        dao.insert(name,ticker);
    }
   String clean(String data){
       String trimmed = data.trim();
       if(data.startsWith("[")){
           trimmed = trimmed.substring(1, trimmed.length()-1);
       }
       return trimmed;
   }
    protected Object parseData(String data,Class claz){
        
        Gson gson = new Gson();
        return gson.fromJson(clean(data), claz);
        
    }

    @Override
    public void run() {
    }
}
