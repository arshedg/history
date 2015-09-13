/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.genie.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author agulshan
 */
public class UserDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final String SELECT_USER_BY_ID="select * from Users where id=?;";
    private static final String INSERT_USER="insert into Users (id,name,password) values(?,?,?);";
    private static final String UPDATE_USER_BY_ID="update Users set name=? and password=? where id=?;";
     private static final String DELETE_USER_BY_ID="delete from Users where id=?;";
    public User getUser(String id){
        List<User> users = this.jdbcTemplate.query(
            SELECT_USER_BY_ID, 
            (ResultSet rs, int rowNum) -> {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setCreatedTime(rs.getTimestamp("created_at"));
                user.setUpdatedTime(rs.getTimestamp("updated_at"));
                return user;
        },
            id
        );
        return users.isEmpty()?null:users.get(0);
    }
    
    public boolean createUser(User user){
        int rowsUpdated = this.jdbcTemplate.update(INSERT_USER, user.getId(),user.getName(),user.getPassword());
        return rowsUpdated>0;
    }
    public boolean updateUser(User user){
        return this.jdbcTemplate.update(UPDATE_USER_BY_ID,user.getName(),user.getPassword(),user.getId())>0;
    }
    public boolean deleteUser(String id){
        return this.jdbcTemplate.update(DELETE_USER_BY_ID,id)>0;
    }
    
}
