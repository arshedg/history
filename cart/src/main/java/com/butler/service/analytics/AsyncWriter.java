/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.service.analytics;

import com.butler.service.UservisitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author arsh
 */
@Component
public class AsyncWriter {
    @Autowired
    UservisitDao visitDao;
    public void registerVisit(String number){
        if(number!=null&&number.length()>=10){
            new AsyncOperator(visitDao, number).start();
        }
    }
}
class AsyncOperator extends Thread{

    private UservisitDao dao;
    private String number;
    public AsyncOperator(UservisitDao dao,String number) {
        this.dao = dao;
        this.number = number;
    }

    @Override
    public void run() {
       dao.saveVisit(number);
    }
    
    
}
