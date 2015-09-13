/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhino.data.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author agulshan
 */
public class DataSourceFactory {
    private static DataSource source;
    static{
        //init: Is this a bad design? Well, i dont care.
        getDataSource();
    }
    public static DataSource getDataSource() {
        if(source!=null){
            return source;
        }
        Properties props = new Properties();
        FileInputStream fis = null;
        
        BasicDataSource dataSource = new BasicDataSource();
        try {
        
            props.load(ClassLoader.getSystemResourceAsStream("mysql.properties"));
            dataSource.setDriverClassName(props.getProperty("MYSQL_DB_DRIVER_CLASS"));
            dataSource.setUrl(props.getProperty("MYSQL_DB_URL"));
            dataSource.setUsername(props.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
           
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return source = dataSource;
    }
    public static Connection getConnection(){
        try {
            return source.getConnection();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
