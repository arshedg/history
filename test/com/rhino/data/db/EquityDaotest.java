/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.db;

import com.rhino.data.Equity;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author agulshan
 */
public class EquityDaotest {
    EquityDao dao = new EquityDao();
    
    @Test
    public void getLastDate() throws SQLException{
        
        Date date = dao.getLastTickerDetails("infy");
        System.out.println(""+date);
    }
    @Test
    public void getAll() throws SQLException{
        List<String> list = dao.getAllEquity();
        System.out.println(""+list.toString());
    }
}
