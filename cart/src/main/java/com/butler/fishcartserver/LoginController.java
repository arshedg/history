/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.fishcartserver;

import com.butler.service.RevisionDao;
import com.butler.service.UserDao;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author arsh
 */
@RestController
public class LoginController {
    @Autowired
    RevisionDao revisionDao;
    @Autowired
    UserDao userDao;
    @RequestMapping("/appversion")
    public String versionInfo(){
        return revisionDao.getAppVersion()+"";
    }
    @RequestMapping("/register")
    public String register(@RequestParam(value="name") String name,@RequestParam(value="no") String no){
        if(name==null) return "Please enter your name";
        if(no==null||no.trim().length()!=10){
            return "Numbered doesn't looks correct, please try again";
        }
        long number;
        try{
           number = Long.parseLong(no);
        }catch(Exception ex){
            return "Numbered doesn't looks correct, please try again";
        }        
        int response = userDao.saveUser(name, number);
        if(response==1)
        {
            return "SUCCESS";
        }
        return "something went wrong. Please call us on 7204368605";
        
    }
    @RequestMapping("/updateuser")
    public String updateUser(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="name") String name,@RequestParam(value="address") String address,
                                @RequestParam(value="number") String number) throws IOException, ServletException{
        userDao.updateUser(name, address, number);
        return "update successfull.Refresh the page to see changes";
    }
}