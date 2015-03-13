/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.history.dao;

import com.history.data.Equity;
import com.history.data.Stock;
import com.history.data.Tick;
import com.rhino.data.db.DataSourceFactory;
import com.rhino.data.history.util.Util;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 *
 * @author agulshan
 */
public class StockDao {
    public List<Equity> getStock(int limit) throws SQLException{
        String sql = "select distinct listed_company_id as id from company_histories limit "+limit;
        QueryRunner run = new QueryRunner( DataSourceFactory.getDataSource() );
        ResultSetHandler rsh = new BeanListHandler(com.history.data.Equity.class);
        return (List<Equity>)run.query(sql, rsh);
    }
    public List<Tick> getStock(String id,Date dat) throws SQLException{
        String sql = "select company_value as price,volume_change as volume from company_histories where listed_company_id=? and created_at between ? and ? order by created_at";
        String currentDate = Util.getDate(dat);
        String endDate = Util.addDate(dat,1);
        Object[] params = new Object[]{id, currentDate,endDate};
        QueryRunner run = new QueryRunner( DataSourceFactory.getDataSource() );
        ResultSetHandler rsh = new BeanListHandler(Tick.class);
        return (List<Tick>)run.query(sql, rsh,params);       
    }
    
    
}
