/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author arsh
 */
public class Util {
    public static String getCurrentTime(){
        Date today = new Date();
      
        //displaying this date on IST timezone
        DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return df.format(today);

    }
     public static String getIndianTime(Timestamp time){
        Date today = Date.from(time.toInstant());
        //displaying this date on IST timezone
        DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return df.format(today);

    }
}
