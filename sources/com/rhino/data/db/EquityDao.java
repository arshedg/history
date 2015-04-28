/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.db;

import com.rhino.data.Equity;
import com.rhino.data.Ticker;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author agulshan
 */
public class EquityDao {
    
    public List<Equity> getAllEquity() throws SQLException{
        String sql = "select distinct equity from ticker";
         QueryRunner run = new QueryRunner( DataSourceFactory.getDataSource() );
        ResultSetHandler rsh = new BeanListHandler(Equity.class);
        return (List<Equity>)run.query(sql, rsh);  
    }
}
