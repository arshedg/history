/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.data.db;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

/**
 *
 * @author agulshan
 */
public class EquityDao {
    
    public List<String> getAllEquity() throws SQLException{
        String sql = "select distinct equity from ticker";
         QueryRunner run = new QueryRunner( DataSourceFactory.getDataSource() );
        ResultSetHandler rsh = new ColumnListHandler();
       return (List<String>) run.query(sql, rsh);
    }
    
    public Date getLastTickerDetails(String equity) throws SQLException{
        String sql = " select cast(max(date) as date) from ticker  where equity='"+equity+"'";
        QueryRunner run = new QueryRunner(DataSourceFactory.getDataSource());
        ResultSetHandler rsh = new ArrayHandler();
        Object[] query = (Object[]) run.query(sql, rsh);
        if(query==null||query.length==0){
            return null;
        }
        return (Date)query[0];
    }
}
