/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.application;

import com.fluke.database.dataservice.EquityDao;
import com.fluke.realtime.data.RediffParser;
import java.sql.SQLException;

/**
 *
 * @author arsh
 */
public class Test {
    public static void main(String[] args) throws SQLException {
        new EquityDao().getAllEquity().parallelStream()
                .forEach(v->{

                
                     RediffParser parser = new RediffParser(v, "2015-10-5");
                    new Thread(parser).start();
                });
        
    }
}
