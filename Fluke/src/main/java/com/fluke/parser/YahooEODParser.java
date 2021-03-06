/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fluke.parser;


import com.fluke.database.dataservice.EODDao;
import com.fluke.database.dataservice.EquityDao;
import com.fluke.model.ticker.EODTicker;
import com.fluke.util.Util;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author agulshan
 */
public class YahooEODParser {
    
    String equity;
    String url;
    EODDao tickerOperation;
    String todayUrl;
    public String grade="NA";
    private String exchange;
    
    public YahooEODParser(String equity){
        this.equity = equity;
        tickerOperation = new EODDao();
        equity = equity.replaceAll("&", "%26");
        if(equity.contains("NSEI")){
            equity = "%5ENSEI";
        }
        if(equity.contains("%5ENSEI")){
           url="http://in.finance.yahoo.com/q/hp?s="+equity+"&z=66&y=";
        todayUrl = "https://in.finance.yahoo.com/q?s="+equity+"&ql=1";
        }else{
           url="http://in.finance.yahoo.com/q/hp?s="+equity+".ns&z=66&y=";
        todayUrl = "https://in.finance.yahoo.com/q?s="+equity+".ns&ql=1"; 
        }
        
    }
    public void process() throws IOException, ParseException, SQLException{
         Date lastUpdatedDate = new EquityDao().getLastTickerDetails(equity);
         doUpdation(lastUpdatedDate);
         updateLatestValue(lastUpdatedDate);
    }
   public boolean filter(EODTicker ticker,Date lastUpdatedDate){
       return false;
   }
     private Document getDocument(String uri){
         for(int i=0;i<5;i++){
             //retry 5 times
             try{
                return Jsoup.connect(uri).get();
             }
             catch(Exception e){
                 
             }
         }
         System.out.println("NOT ABLE TO CONNECT TO URI "+uri+"\n Press 1 to retry");
         Scanner command = new Scanner(System.in);
               String cmd=  command.next();
         if("1".equals(cmd)){
             return getDocument(uri);
         }else{
             return null;
         }
         
     }
     EODTicker processRow(Iterator<Element> ite ) throws ParseException{
        EODTicker details = new EODTicker();
        Element element = ite.next();
        if(element.text().indexOf("Close price adjusted for divid")>=0){
            return null;
        }
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);  
         System.out.println("Processing for date "+element.text());
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

    private void doUpdation(Date lastUpdatedDate) throws ParseException, SQLException {
          for(int i =0;i<2000;i=i+66){
            Document doc =getDocument(url+i);
            Element table = doc.select("table[class=yfnc_datamodoutline1]").first();
            if(table==null){
                return;
            }
            Iterator<Element> ite = table.select("td[class=yfnc_tabledata1]").iterator();
            while(ite.hasNext()){
                EODTicker ticker = processRow(ite);
                if(ticker!=null&&filter(ticker,lastUpdatedDate)){
                    return;
                }
                if(ticker!=null&&isAlreadyProcessed(ticker,lastUpdatedDate)){
                    return;
                }
                if(ticker!=null){
                    tickerOperation.insertEODTicker(equity, ticker);
                   // System.out.println("processes data date :"+ticker.getDate()+"  name:"+equity);
                }
            }
        }
    }
    
    private boolean isAlreadyProcessed(EODTicker ticker,Date lastUpdatedDate){
        if(ticker==null||lastUpdatedDate==null){
            return false;//no need to handle this
        }
        return ticker.getDate()==null?false:ticker.getDate().before(lastUpdatedDate)
                ||ticker.getDate().equals(lastUpdatedDate);
    }

    private void updateLatestValue(Date lastUpdated) throws ParseException, SQLException {
        System.out.println("updating current value for "+equity);
        Document doc = getDocument(todayUrl);
       String marketTime = doc.getElementById("yfs_market_time").text().split("-")[0];
       if(doc.getElementById("yfs_l84_"+equity.toLowerCase()+".ns")==null){
           System.out.println("skipping "+equity+" not able to read current value");
           return;
       }
       String closePrice = doc.getElementById("yfs_l84_"+equity.toLowerCase()+".ns").text();
       String open = doc.getElementsByClass("yfnc_tabledata1").get(1).text();
       String volume = doc.getElementById("yfs_v53_"+equity.toLowerCase()+".ns").text();
       Element lowElement = doc.getElementById("yfs_g53_"+equity.toLowerCase()+".ns");
       if(lowElement==null){
           System.out.println("skipping "+equity+" not able to read current value");
           return;
       }
       String low = doc.getElementById("yfs_g53_"+equity.toLowerCase()+".ns").text();
       String high =  doc.getElementById("yfs_h53_"+equity.toLowerCase()+".ns").text();
       if(lastUpdated!=null){
            LocalDate ld1 = LocalDate.parse(Util.getDate(lastUpdated));
            LocalDate ld2 = LocalDate.parse(Util.getDate(Util.getDateFromMarketTime(marketTime)));
            if(ld1.isEqual(ld2)){
                return;
            }
       }
       EODTicker data = new EODTicker();
       NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
       data.setClosePrice(nf.parse(closePrice).floatValue());
       data.setOpenPrice(nf.parse(open).floatValue());
       data.setVolume(nf.parse(volume).intValue());
       data.setHighPrice(nf.parse(high).floatValue());
       data.setLowPrice(nf.parse(low).floatValue());
       data.setDate(Util.getDateFromMarketTime(marketTime));
       tickerOperation.insertEODTicker(equity, data);
        System.out.println("Latest value updated for "+equity+" last date:"+marketTime);
       
    }

    /**
     * @return the exchange
     */
    public String getExchange() {
        return exchange;
    }

    /**
     * @param exchange the exchange to set
     */
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
    
}
