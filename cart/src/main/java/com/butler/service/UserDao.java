/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.service;


import com.butler.data.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 *
 * @author arsh
 */
public class UserDao extends SimpleJdbcDaoSupport {


    public int saveUser(String name, long number) {
        return this.getSimpleJdbcTemplate().update("insert into user(name,number) values(?,?) ", name,number);
    }
    public String getName(String number){
        
        try{
            List<String> names = this.getJdbcTemplate().query("select name from user where number=?",new String[]{number}, new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("name");
            }
        });
        return names.isEmpty()?"UNKNOWN":names.get(0);
       // return this.getSimpleJdbcTemplate().queryForObject("select name from user where number=?", String.class, number);
        }catch(Throwable e){
            return "UNKNOWN";
        }
    }
    public String getAddress(String number){
        try{
        return this.getSimpleJdbcTemplate().queryForObject("select address from user where number=?", String.class, number);
        }catch(Throwable e){
            return "Address not found";
        }
    }
    public void updateUser(String name,String address,String number){
        this.getSimpleJdbcTemplate().update("update user set name=?,address=? where number=?", name,address,number);
    }
    public boolean doesNumberExist(long number){
        try{
         this.getSimpleJdbcTemplate().queryForLong("select id from user where number=?", number);
        }catch(EmptyResultDataAccessException exception){
            return false;
        }
        catch(IncorrectResultSizeDataAccessException exception){
            if(exception.getActualSize()>0){
                return true;
            }else{
                return false;
            }     
        }
        return true;
    }
}
