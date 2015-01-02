/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.history.util;

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
    
}
