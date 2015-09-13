/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.database.dataservice;

import com.fluke.data.intraday.IntradayDetails;
import com.fluke.model.ticker.IntradayTicker;
import com.fluke.data.intraday.Meta;
import com.fluke.data.intraday.Series;
import com.fluke.database.DatabaseProperty;
import com.fluke.util.Util;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author arshed
 */
public class IntraDayDaoTest {
    

    @Before @After
    public void setup() throws SQLException{
         QueryRunner runner = new QueryRunner( DatabaseProperty.getDataSource() );
         runner.update("delete from intraday where equity='abcd'");
         
    }
    @Test
    public void testBatchUpdate(){
        IntraDayDao dao = new IntraDayDao();
        IntradayTicker ticker1 = new IntradayTicker();
        ticker1.setOpenPrice(100);
        ticker1.setTime(new Timestamp(1438746899l));
        IntradayTicker ticker2 = new IntradayTicker();
        ticker2.setOpenPrice(100);
        ticker2.setTime(new Timestamp(1438777899l));
        dao.insertBatch("xyz", Arrays.asList(ticker1,ticker2));
        
    }
  //  @Test
    public void testInsert() {
        IntraDayDao dao = new IntraDayDao();
        Meta meta = new Meta();
        meta.setTicker("abcd.ns");
        Series series = new Series();
        series.setOpen(15f);
        series.setHigh(20f);
        series.setLow(10f);
        series.setClose(16f);
        series.setVolume(55l);
        series.setTimestamp(1438746899l);
        IntradayDetails details = new IntradayDetails();
        details.setMeta(meta);
        details.setSeries(Arrays.asList(series));
        dao.insert(details);
        List<IntradayTicker> tickers = dao.getIntraday("abcd", "2015-08-5");      
        Assert.assertTrue(!tickers.isEmpty());
        IntradayTicker ticker = tickers.get(0);
        Assert.assertEquals(15f, ticker.getOpenPrice(),.01);
        Assert.assertEquals(16f, ticker.getClosePrice(),.01);
        Assert.assertEquals(10f, ticker.getLowPrice(),.01);
        Assert.assertEquals(20f, ticker.getHighPrice(),.01);
        Assert.assertEquals(55l, ticker.getVolume());
        Assert.assertEquals(1438746899l*1000, ticker.getTime().getTime());
        
    }
    @Test
    public void testTimeStampConversion(){
        IntraDayDao dao = new IntraDayDao();
        List<IntradayTicker> tickers = dao.getIntraday("PARAL", "2010-7-27"); 
        System.out.println(tickers.get(0).getTime());
        System.out.println(tickers.get(0).getDate());
    }
    
}
