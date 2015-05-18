/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.fetcher;

import com.rhino.data.Ticker;
import com.rhino.data.db.EquityDao;
import com.rhino.data.db.TickerDao;
import com.rhino.data.history.util.Util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 *
 * @author agulshan
 */
public class YahooParserTest {
    
    
    //@Test
    public void testProcess() throws Exception {

        add("ASIANPAINT");
    }
    @Test
    public void readFromFile() throws FileNotFoundException, IOException, ParseException, SQLException{
       // FileInputStream file = new FileInputStream("stocks");
        Scanner scan = new Scanner(ClassLoader.getSystemResourceAsStream("stocks"));
        TickerDao ticker = new TickerDao();
        while(scan.hasNext()){
            String name = scan.next().trim();
            if(name.equals("")){
                continue;
            }
            System.out.println("Name: "+name);
            List<Ticker> tick = ticker.getTickers(name);
            if(tick!=null&&tick.size()>10){
                System.err.println("Skipping "+name+" as it is already processed");
                continue;
            }
            tick = null;
            YahooParser parser = new YahooParser(name){
                
                @Override
               public boolean filter(Ticker ticker,java.sql.Date lastUpdatedDate){
                    try {
                        return ticker.getDate().before(Util.getDate("2011-1-1"));
                    } catch (ParseException ex) {
                        Logger.getLogger(YahooParserTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return true;
                }
            };
            parser.grade="A2";
            parser.process();
            tick = ticker.getTickers(name);
            if(tick==null||tick.size()<10){
                System.err.println("Process failed for "+name);
            }
            
        }
    }
    private void add(String... names) throws Exception{
       for(String name:names){
           YahooParser parser = new YahooParser(name);
           parser.process();
       }
    }
    
 //  @Test
    public void updateAll() throws SQLException, IOException, ParseException{
        for(String name:new EquityDao().getAllEquity()){
            YahooParser parser = new YahooParser(name);
            parser.process();
        }
    }

  
    
}
