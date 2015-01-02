/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.fetcher;

import com.rhino.data.db.TickerDao;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author agulshan
 */
public class YahooParserTest {
    
    
    @Test
    public void testProcess() throws Exception {

        add("DELTACORP","DISHMAN","ESCORTS","HINDOILEXP");
    }
    
    private void add(String... names) throws Exception{
       for(String name:names){
           YahooParser parser = new YahooParser(name);
        parser.process();
       }
    }

  
    
}
