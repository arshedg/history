/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.history.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author agulshan
 */
public class Util {
    private static final String format ="yyyy-MM-dd";
    private static final String YAHOO_FORMAT ="dd MMM, yyyy";
     private static final String YAHOO_FORMAT_ALTERNATIVE ="dd-MMM-yyyy";
    public static Date getDate(String date) throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(date);
    }
    
    public static String getDate(Date date){
         SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
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
       float per = a/b;
       return per*100-100;
    }
    public static void print(String string){
        System.out.println(string);
    }
}
