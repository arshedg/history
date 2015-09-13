/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.db;

import com.rhino.data.Ticker;
import com.rhino.data.history.util.Util;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author agulshan
 */
public class TickerDaoTest {
    
    private TickerDao dao;
    @Before
    public void setUp() throws SQLException{
        dao = new TickerDao();
        dao.deleteTicker("infy");
    }
    @Test
    public void testGetTicker() throws Exception {   
        Date date = Util.getDate("1989-12-31"); 
        Ticker  ticker = dao.getTicker("abcd", date);
        Assert.assertEquals(1f, ticker.getOpenPrice());
        /*
        to do assert all values
        */
    }
    @Test
    public void testInsertion() throws SQLException{
        Ticker tick = new Ticker();
        tick.setLowPrice(100);
        tick.setDate(Calendar.getInstance().getTime());
        dao.insertTicker("infy", tick,"gr","nse");
        Ticker ticker = dao.getTicker("infy",Calendar.getInstance().getTime() );
        Assert.assertNotNull(ticker);
        dao.deleteTicker("infy");
    }
    
}
