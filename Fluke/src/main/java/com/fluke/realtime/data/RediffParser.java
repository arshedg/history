/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.realtime.data;

import com.fluke.data.intraday.IntradayDetails;
import com.fluke.data.intraday.Series;
import com.fluke.model.ticker.IntradayTicker;
import com.fluke.parser.YahooIntradayParser;
import com.fluke.util.Util;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author arsh
 */
public class RediffParser extends RealtimeParser {
    

    private final String baseUrl = "http://money.rediff.com/money1/current_status_new.php?companylist=%s";
    private String url;
    Class model = RediffData.class;
    private int counter = 0;
    IntradayTicker lastTicker;
    IntradayTicker one;
    IntradayTicker two;
    int totalVolume = 0;
    
    float maxPrice=0;
    float minPrice = Float.MAX_VALUE;
    String date;
    public RediffParser(String name,String date) {
        this.name = name;
        this.date = date;
        if(this.name.equals(".NSEI")){
            url = String.format(baseUrl, "17023929");
        }else{
            url = String.format(baseUrl, name);
        }
    }
    public void fillData(){
        Timestamp lastUpdated = dao.getLastTimeStamp(name);
        Timestamp currentTime = Timestamp.from(Calendar.getInstance().toInstant());
        int lastMins = lastUpdated!=null?Util.getMinutes(Date.from(lastUpdated.toInstant())):((9*60)+15);
        int currentMins = Util.getMinutes(Date.from(currentTime.toInstant()));
        if(currentMins-lastMins<4){
            //lost data of less than 4 mins. no need to process further
            return;
        }
        YahooIntradayParser parser = new YahooIntradayParser();
        IntradayDetails data = parser.getIntradayDetails(name);
        fillOldData(data);
    }
    public void worker(){
     //  String data = fetchData();
       init();
       while(true&&!exit){
            process(fetchData(),counter());
            sleep(18);
            process(fetchData(),counter());
            sleep(18);
            process(fetchData(),counter());
            sleep(18);
       }
    }
    @Override
    public void run() {
        try{
            fillData();
            worker();
        }
        catch(Exception e){
            if(exit!=true){
                sleep(20);
                RediffParser parser = new RediffParser(name, date);
                        Thread thread = new Thread(parser);
                        thread.setName(name+"-new");
                        thread.start();
            }
        }
    }
    private void sleep(int sec){
        try {
            Thread.sleep(1000*sec);
        } catch (InterruptedException ex) {
            Logger.getLogger(RediffParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    boolean exit=false;
    private void init(){
        String data = fetchData();
        RediffData rediff = (RediffData) parseData(data, model);
        if(rediff==null){
            System.out.println("Error processing "+name);
            exit=true;
            return;
        }
        IntradayTicker ticker = new IntradayTicker();
        ticker.setClosePrice(getFloat(rediff.getLastTradedPrice()));
        lastTicker = getTicker(rediff, ticker);
        sleep(10);
    }
    private String fetchData(){
          InputStream in = null;
        URL uri;
        try {
              uri = new URL(url);
              in = uri.openStream();
              return  IOUtils.toString( in );
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            IOUtils.closeQuietly(in);
        }
    }
    private void process(String data,int count) {
       RediffData rediff = (RediffData) parseData(data, model);
       switch(count){
           case 1:
               firstTicker(rediff);
               break;
           case 2:
               midTicker(rediff);
               break;
           case 3:
               finalTicker(rediff);
               break;
       }
    }
    private int counter(){
        if(counter==3){
            counter = 1;
        }else{
            counter++;
        }
        return counter;
    }
    void firstTicker(RediffData data){
        one = getTicker(data, lastTicker);
        
    }
    IntradayTicker getTicker(RediffData data,IntradayTicker compare){
        IntradayTicker ticker = new IntradayTicker();
        int  volume = getInt(data.getVolume())-totalVolume; 
        totalVolume =getInt(data.getVolume());
        float openPrice =compare.getClosePrice();
        float closePrice = getFloat(data.getLastTradedPrice());
        ticker.setClosePrice(closePrice);
        ticker.setOpenPrice(openPrice);
        ticker.setHighPrice(closePrice);
        ticker.setLowPrice(closePrice);
        ticker.setVolume(volume);
        ticker.setTime(getTime(data.getLastTradedTime()));
        float high = getFloat(data.getHigh());
        if(high>maxPrice){
            ticker.setHighPrice(high);
            maxPrice = high;
        }
        float low = getFloat(data.getLow());
        if(low<minPrice){
            ticker.setLowPrice(low);
            minPrice = low;
        }
        return ticker;
    }
    private int getInt(String val){
        try {
            return NumberFormat.getNumberInstance(java.util.Locale.US).parse(val).intValue();
        } catch (ParseException ex) {
           throw new RuntimeException(ex);
        }
    }
    private float getFloat(String val){
        try {
            return NumberFormat.getNumberInstance(java.util.Locale.US).parse(val).floatValue();
        } catch (ParseException ex) {
           throw new RuntimeException(ex);
        }
    }
    private Timestamp getTime(String time){
        String timepart = time.split(",")[1];
        return Timestamp.valueOf(date+" "+timepart+"");
    }

    private void midTicker(RediffData rediff) {
        two = getTicker(rediff, one);
    }

    private void finalTicker(RediffData rediff) {
        IntradayTicker third = getTicker(rediff, two);
        IntradayTicker last = new IntradayTicker();
        last.setOpenPrice(one.getOpenPrice());
        last.setClosePrice(third.getClosePrice());
        float high = (float) Arrays.asList(one,two,third).stream().mapToDouble(t->t.getHighPrice()).max().getAsDouble();
        float low = (float) Arrays.asList(one,two,third).stream().mapToDouble(t->t.getLowPrice()).min().getAsDouble();
        last.setHighPrice(high);
        last.setLowPrice(low);
        last.setTime(Timestamp.from(Calendar.getInstance().toInstant()));
        last.setVolume(one.getVolume()+two.getVolume()+third.getVolume());
        insert(last);
        lastTicker = last;
    }

    private void fillOldData(IntradayDetails data) {
        dao.deleteRealTime(name);
        data.getSeries().stream().
                forEach((Series d)->{
                    IntradayTicker ticker = new IntradayTicker();
                    ticker.setClosePrice(d.getClose());
                    ticker.setHighPrice(d.getHigh());
                    ticker.setLowPrice(d.getLow());
                    ticker.setOpenPrice(d.getOpen());
                    ticker.setTime(d.getTimestamp());
                    ticker.setVolume(d.getVolume());
                    insert(ticker);
                });
    }
}
