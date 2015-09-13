/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.util;

import org.junit.Test;

/**
 *
 * @author arshed
 */
public class UtilTest {
    
  // @Test
   public void testDateToTimeStamp(){
       System.out.println(Util.getTimeStamp("2014-1-1").toString());
   }
 //  @Test
   public void timeStampToDate(){
       System.out.println(Util.getDate(1438746899).toString());
   }
   @Test
   public void ieodDate(){
       System.out.println(Util.getIEODDate("10-6-2012,15:16").toString());
   }
}
