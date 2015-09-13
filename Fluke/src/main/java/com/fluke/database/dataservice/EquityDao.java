/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fluke.database.dataservice;

import com.fluke.database.DatabaseProperty;
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
        String sql = "select distinct equity from EOD";
        QueryRunner run = new QueryRunner( DatabaseProperty.getDataSource() );
        ResultSetHandler rsh = new ColumnListHandler();
       return (List<String>) run.query(sql, rsh);
    }
    public List<String> getAllEquity(String grade) throws SQLException{
        String sql = "select distinct equity from EOD";
        QueryRunner run = new QueryRunner( DatabaseProperty.getDataSource() );
        ResultSetHandler rsh = new ColumnListHandler();
       return (List<String>) run.query(sql, rsh);
    }
    public void changeGroup(String equity,String group) throws SQLException{
        String sql ="update ticker set grade ='"+group+"' where equity='"+equity+"'";
                QueryRunner run = new QueryRunner( DatabaseProperty.getDataSource() );
        run.update(sql);
        
    }
    public Date getLastTickerDetails(String equity) throws SQLException{
        String sql = " select cast(max(date) as date) from EOD  where equity='"+equity+"'";
        QueryRunner run = new QueryRunner(DatabaseProperty.getDataSource());
        ResultSetHandler rsh = new ArrayHandler();
        Object[] query = (Object[]) run.query(sql, rsh);
        if(query==null||query.length==0){
            return null;
        }
        return (Date)query[0];
    }
}
