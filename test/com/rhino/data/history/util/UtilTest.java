/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.history.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author agulshan
 */
public class UtilTest {
    
    

    @Test
    public void testGetYahooDate() throws Exception {
        Date date = Util.getYahooDate("1 Aug, 1997");
        Assert.assertEquals("1997-08-01", Util.getDate(date));
    }
    @Test
    public void percentage(){
        Util.print(""+Util.findPercentage( 2145400f,4199200f));
    }
    @Test
    public void getDateFromMarketTime() throws ParseException{
        String marketDate="Mon,  4 May, 2015 6:27PM - ";
        System.out.println(Util.getDateFromMarketTime(marketDate));
        
    }
    
    @Test
    public void areOnSameDay() throws ParseException{
        String s1="Mon,  4 May, 2015 6:27PM";
        Date d1=Util.getDateFromMarketTime(s1);
        String s12="Mon,  4 May, 2015 6:27PM";
        Date d2=Util.getDateFromMarketTime(s1);
        LocalDate ld1 = LocalDate.now();
        LocalDate ld2 = LocalDate.parse(Util.getDate(d1));
        LocalDate.parse(Util.getDate(Util.getDateFromMarketTime(s1)));
        System.out.println("hi "+ld1.isEqual(ld2));
                
    }
    
}
