/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.realtime.data;

import com.fluke.database.dataservice.EquityDao;
import java.sql.SQLException;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author arsh
 */
public class RediffParserTest {
    
 
    @Test
    public void testRun() throws SQLException {
        
        RediffParser parser = new RediffParser("INFY", "2015-09-23");
        new Thread(parser).start();
      
    }
    
}
