/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.fetcher;


import com.rhino.data.Ticker;
import com.rhino.data.db.TickerDao;
import com.rhino.data.history.util.Util;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Locale;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author agulshan
 */
public class YahooParser {
    
    String equity;
    String url;
    TickerDao tickerOperation;
    public YahooParser(String equity){
        this.equity = equity;
        tickerOperation = new TickerDao();
        url = url="http://in.finance.yahoo.com/q/hp?s="+equity+".NS&z=66&y=";
    }
    void process() throws IOException, ParseException, SQLException{
         for(int i =0;i<10000;i=i+66){
            Document doc =Jsoup.connect(url+i).get();
            Element table = doc.select("table[class=yfnc_datamodoutline1]").first();
            if(table==null){
                return;
            }
            Iterator<Element> ite = table.select("td[class=yfnc_tabledata1]").iterator();
            while(ite.hasNext()){
                Ticker ticker = processRow(ite);
                if(ticker!=null){
                    tickerOperation.insertTicker(equity, ticker);
                    System.out.println("processes data date :"+ticker.getDate()+"  name:"+equity);
                }
            }
        }
    }
     Ticker processRow(Iterator<Element> ite ) throws ParseException{
        Ticker details = new Ticker();
        Element element = ite.next();
        if(element.text().indexOf("Close price adjusted for divid")>=0){
            return null;
        }
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);   
        details.setDate(Util.getYahooDate(element.text()));
        element = ite.next();
        if(element.text().indexOf("Dividend")>=0){
            return null;
        }
        if(element.text().indexOf("Stock Split")>=0){
            return null;
        }
        details.setOpenPrice(nf.parse(element.text()).floatValue());
        element = ite.next();
        details.setHighPrice(nf.parse(element.text()).floatValue());
        element = ite.next();
        details.setLowPrice(nf.parse(element.text()).floatValue());
        element = ite.next();
        details.setClosePrice(nf.parse(element.text()).floatValue());
        element = ite.next();
        details.setVolume( nf.parse(element.text()).intValue());
        element = ite.next();
        details.setAdjustedClose(nf.parse(element.text()).floatValue());
        return details;
    }
}
