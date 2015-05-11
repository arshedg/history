/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.fetcher;

import com.rhino.data.db.EquityDao;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import org.junit.Test;

/**
 *
 * @author agulshan
 */
public class YahooParserTest {
    
    
    //@Test
    public void testProcess() throws Exception {

        add("GRASIM","BANKBARODA","NMDC","HINDUNILVR","NTPC","GRASIM","POWERGRID");
    }
    
    private void add(String... names) throws Exception{
       for(String name:names){
           YahooParser parser = new YahooParser(name);
           parser.process();
       }
    }
    
   @Test
    public void updateAll() throws SQLException, IOException, ParseException{
        for(String name:new EquityDao().getAllEquity()){
            YahooParser parser = new YahooParser(name);
            parser.process();
        }
    }

  
    
}
