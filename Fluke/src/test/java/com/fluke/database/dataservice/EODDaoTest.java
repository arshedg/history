/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.database.dataservice;

import java.sql.SQLException;
import java.util.Date;
import org.junit.Test;

/**
 *
 * @author arshed
 */
public class EODDaoTest {
    @Test
    public void testGetnNextDay() throws SQLException{
        EODDao dao = new EODDao();
        Date date = dao.getNextTradingDay("2010-7-26");
        System.out.println("print date "+date);
    }
}
