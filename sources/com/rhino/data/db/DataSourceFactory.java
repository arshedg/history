/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhino.data.db;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

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
        MysqlDataSource mysqlDS = new MysqlDataSource();
        try {
            fis = new FileInputStream("mysql.properties");
            props.load(fis);
            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
           
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return source = mysqlDS;
    }
    public static Connection getConnection(){
        try {
            return source.getConnection();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
