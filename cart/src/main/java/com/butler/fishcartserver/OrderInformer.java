/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.fishcartserver;

import com.butler.data.OrderDetails;
import com.butler.service.OrderDao;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author arsh
 */
@Controller
@RestController
public class OrderInformer {
    @Autowired
    OrderDao orderDao;
    @RequestMapping(value="/orders",method = RequestMethod.POST,produces = {"application/json"})
    
    public @ResponseBody List<OrderDetails> getOrderDetails(){
        return orderDao.getPendingOrders();
    }
    
    @RequestMapping(value="/ordersbycustomer",method = RequestMethod.POST,produces = {"application/json"})
    public @ResponseBody Collection<OrderDetails> getOrdersByCustomer(){
        return orderDao.getPendingOrdersGroupedByCustomer();
    }
    @RequestMapping(value="/updatefeedback",method = RequestMethod.POST)
    public @ResponseBody String updateFeedback(@RequestParam(value="number") String number,
                                 @RequestParam(value="feedback") String feedback){
         orderDao.updateFeedBack(number, feedback);
         return "success";
    }
}
