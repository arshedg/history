/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.service;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;


/**
 *
 * @author arsh
 */
public class RevisionDao extends SimpleJdbcDaoSupport{
    
    public int getAppVersion(){
        return this.getJdbcTemplate().queryForInt("select version from revision");
    }
}
