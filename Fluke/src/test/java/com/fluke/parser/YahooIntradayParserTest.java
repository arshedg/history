/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.parser;

import com.fluke.data.intraday.IntradayDetails;
import java.io.FileNotFoundException;
import java.io.IOException;
import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author arshed
 */
public class YahooIntradayParserTest {
    
  

    @Test
    public void testParseData() throws FileNotFoundException, IOException {
        String data = IOUtils.toString(YahooIntradayParser.class.getClassLoader().getResourceAsStream("intraday_data"));   
        IntradayDetails details = (IntradayDetails) new YahooIntradayParser().parseData(data);
        assertNotNull(details);
    }
    @Test
    public void testFilter() throws IOException{
        String data = IOUtils.toString(YahooIntradayParser.class.getClassLoader().getResourceAsStream("raw_intraday_data"));  
        YahooIntradayParser parser = new YahooIntradayParser();
        String processedData = parser.filter(data);
        System.out.println(processedData);
    
    }
    @Test
    public void testGetData(){
        YahooIntradayParser parser = new YahooIntradayParser();
        IntradayDetails details = parser.getIntradayDetails("infy");
        Assert.assertEquals("INFOSYS LIMITED",details.getMeta().getCompanyName());
    }
    @Test
    public void testGetDataWithSpecialCharInName(){
        YahooIntradayParser parser = new YahooIntradayParser();
        IntradayDetails details = parser.getIntradayDetails("m&m");
        Assert.assertTrue(details.getMeta().getCompanyName().contains("MAHINDRA"));
    }
    
}
