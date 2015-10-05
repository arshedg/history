/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fluke.util;

import com.fluke.application.Vulture;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author agulshan
 */
public class Util {
    private static final String format ="yyyy-MM-dd";
    private static final String YAHOO_FORMAT ="dd MMM, yyyy";
    private static final String YAHOO_FORMAT_ALTERNATIVE ="dd-MMM-yyyy";
    private static final String MARKET_TIME_FORMATTER ="EEE, dd MMM, yyyy hh:mma";
    private static final String REDIFF_TIME = "";
    private static final String IEOD_FORMAT="dd-MM-yyyy,HH:mm";
    public static Date getDate(String date) throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(date);
    }
    public static Date getIEODDate(String date){
        SimpleDateFormat formatter = new SimpleDateFormat(IEOD_FORMAT);
        try {
            return formatter.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static Timestamp getTimeStamp(String date) {
        try {
            return new Timestamp(getDate(date).getTime());
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static Date getDate(long time){
        return new Date(time*1000);
    }
    public static String getDate(Date date){
         SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
    public static Date getDateFromMarketTime(String date) throws ParseException{
        String filteredDate = date.replaceAll("-", "").trim();
        SimpleDateFormat formatter = new SimpleDateFormat(MARKET_TIME_FORMATTER);
        return formatter.parse(filteredDate);
    }
    public static Date getYahooDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(YAHOO_FORMAT);
        try {
            return formatter.parse(date);
        } catch (ParseException ex) {
           formatter = new SimpleDateFormat(YAHOO_FORMAT_ALTERNATIVE);
           return formatter.parse(date);
        }
    }
    public static String getTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);

    }
    public static String addDate(Date date,int noOfDays){
        Calendar c = Calendar.getInstance();
        c.setTime(date); 
        c.add(Calendar.DATE, noOfDays); // Adding 5 days
        return getDate(c.getTime());

    }
    /*
    a<b
    */
    public static float findPercentage(float a,float b){
        float per = a/b;
        return  per*100;
    }
    /*
    a should be greated than b
    */
    public static float findPercentageChange(float a,float b){
       if(a<b){
           return ((b/a)*100-100)*-1;
       }
       float per = a/b;
       return per*100-100;
    }
    public static float findTargetPrice(float price,float expectedPecentageGain){
        return price + price*(expectedPecentageGain/100);
        
    }
    public static float findTargetStopLoss(float price,float expectedPecentageloss){
        return price - price*(expectedPecentageloss/100);
        
    }
    public static void print(String string){
        System.out.println(string);
    }
    public static int getMinutes(Date date){
        String time = Util.getTime(date);
        String parts[] = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int min = Integer.parseInt(parts[1]);
        return hour*60+min;
    }
    public static void placeBuy(String eq,int quantity,float price,float trigger){
         HttpClient httpClient = HttpClientBuilder.create().build();
        String command = String.format(BUY, eq,quantity,price,trigger);
        try {
        HttpPost request = new HttpPost(MUBARAK);
        StringEntity params =new StringEntity(command);
        request.setEntity(params);
        request.addHeader("content-type", "application/json");
        request.addHeader("Accept","application/json");
        httpClient.execute(request);
        System.out.println("BUY ORDER PLACE FOR " +eq+" PRICE:"+price+" Trigger:"+trigger);
        // handle response here...
    }catch (Exception ex) {
        throw new RuntimeException("Failed to pace order", ex);
    }
        
    }
      public static void placeBuy(String eq,int quantity,float price){
         HttpClient httpClient = HttpClientBuilder.create().build();
        String command = String.format(BUY_NO_TRIGGER, eq,quantity,price);
        try {
        HttpPost request = new HttpPost(MUBARAK);
        StringEntity params =new StringEntity(command);
        request.setEntity(params);
        request.addHeader("content-type", "application/json");
        request.addHeader("Accept","application/json");
        httpClient.execute(request);
    System.out.println("BUY ORDER PLACEd FOR WITHOUT TRIGGER" +eq+" PRICE:"+price);
        // handle response here...
    }catch (Exception ex) {
        throw new RuntimeException("Failed to pace order", ex);
    }
        
    }
    public static void placeSell(String eq,int quantity,float price,float trigger){
        HttpClient httpClient = HttpClientBuilder.create().build();
        String command = String.format(SELL, eq,quantity,price,trigger);
        try {
        HttpPost request = new HttpPost(MUBARAK);
        StringEntity params =new StringEntity(command);
        request.setEntity(params);
        request.addHeader("content-type", "application/json");
        request.addHeader("Accept","application/json");
       httpClient.execute(request);
            System.out.println("SELL ORDER PLACE FOR " +eq+" PRICE:"+price+" Trigger:"+trigger);
        // handle response here...
    }catch (Exception ex) {
        throw new RuntimeException("Failed to pace order", ex);
    }
        
    }
    public static void placeSell(String eq,int quantity,float price){
        HttpClient httpClient = HttpClientBuilder.create().build();
        String command = String.format(SELL_NO_TRIGGER, eq,quantity,price);
        try {
        HttpPost request = new HttpPost(MUBARAK);
        StringEntity params =new StringEntity(command);
        request.setEntity(params);
        request.addHeader("content-type", "application/json");
        request.addHeader("Accept","application/json");
        httpClient.execute(request);
        System.out.println("SELL ORDER PLACE without trigger " +eq+" PRICE:"+price);
        // handle response here...
    }catch (Exception ex) {
        throw new RuntimeException("Failed to pace order", ex);
    }
        
    }
    
    
    static String MUBARAK="http://192.168.0.107:3000/orders";
    static String BUY = "{\n" +
        "\"scrip_code\":\"%s\",\n" +
        "\"quantity\": %s,\n" +
        "\"rate\": %s,\n" +
        "\"trigger_rate\": %s,\n" +
        "\"is_stoploss\": true,\n" +
        "\"place_at_market\": false,\n" +
        "\"order_type\": \"buy\"\n" +
        "}";
      static String BUY_NO_TRIGGER = "{\n" +
        "\"scrip_code\":\"%s\",\n" +
        "\"quantity\": %s,\n" +
        "\"rate\": %s,\n" +
        "\"trigger_rate\": 0,\n" +
        "\"is_stoploss\": false,\n" +
        "\"place_at_market\": false,\n" +
        "\"order_type\": \"buy\"\n" +
        "}";
    static String SELL = "{\n" +
        "\"scrip_code\":\"%s\",\n" +
        "\"quantity\": %s,\n" +
        "\"rate\": %s,\n" +
        "\"trigger_rate\": %s,\n" +
        "\"is_stoploss\": true,\n" +
        "\"place_at_market\": false,\n" +
        "\"order_type\": \"sell\"\n" +
        "}";
      static String SELL_NO_TRIGGER = "{\n" +
        "\"scrip_code\":\"%s\",\n" +
        "\"quantity\": %s,\n" +
        "\"rate\": %s,\n" +
        "\"trigger_rate\": 0,\n" +
        "\"is_stoploss\": false,\n" +
        "\"place_at_market\": false,\n" +
        "\"order_type\": \"sell\"\n" +
        "}";
}
