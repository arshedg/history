package com.genie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserServiceHandler {

        @Autowired
        private UserDao userDao;
      
	@RequestMapping(value="/{id}", method = RequestMethod.GET, produces={"application/json"})
        @ResponseBody
	public User getUser(@PathVariable String id) {
             return userDao.getUser(id);

	}
        
        @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
        @ResponseBody
        public String deleteUser(@PathVariable String id){
            if(userDao.deleteUser(id)){
                return "{success:true}";
            }else{
                return "{success:false}";
            }
            
        }
        @RequestMapping(value="/",method=RequestMethod.POST,  consumes = {"application/json"})
        public String createUser( @RequestBody  User user){
            System.out.println(user.getId());
            return "a";
        }
	
}
