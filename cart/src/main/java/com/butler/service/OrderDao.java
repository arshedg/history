/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.service;

import com.butler.data.OrderDetails;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 *
 * @author arsh
 */
public class OrderDao extends SimpleJdbcDaoSupport{
    public void placeOrder(String number,String order,float quantity,boolean isImmediate){
        String status="TODO";
        this.getSimpleJdbcTemplate().update("insert into orders(number,product,quantity,status,immediate) values(?,?,?,?,?)", number,order,quantity,status,isImmediate);
    }
    public void updateFeedBack(String number,String feedback){
        String sql = "update orders set feedback=? where number=? and status='TODO'";
        this.getSimpleJdbcTemplate().update(sql, feedback,number);
    }
    public List<OrderDetails> getPendingOrders(){
        String sql = "select name, o.number, address, o.feedback,o.product, o.quantity, o.immediate,o.stamp as time from orders o,(select name,address,number from user)as y where o.number = y.number and o.status='TODO' order by time desc";
        //return this.getJdbcTemplate().queryForList(sql, OrderDetails.class);
        return this.getJdbcTemplate().query(sql, new RowMapper() {

            public Object mapRow(ResultSet rs, int i) throws SQLException {
                OrderDetails details = new OrderDetails();
                details.setName(rs.getString("name"));
                details.setNumber(rs.getString("number"));
                details.setAddress(rs.getString("address"));
                details.setProduct(rs.getString("product"));
                details.setQuantity(rs.getString("quantity"));
                Timestamp time = rs.getTimestamp("time");
                details.setTime(Util.getIndianTime(time));
                details.setFeedback(rs.getString("feedback"));
                details.setImmediate(rs.getBoolean("immediate"));
                return details;
            }
        });
    }
    
    public Collection<OrderDetails> getPendingOrdersGroupedByCustomer(){
       List<OrderDetails> orders = this.getPendingOrders();
       Map<String,OrderDetails> groupedOrder = new HashMap<>();
       orders.stream().forEach(order->{
            if(groupedOrder.containsKey(order.getNumber())){
                OrderDetails details = groupedOrder.get(order.getNumber());
                details.setProduct(details.getProduct()+","+order.getProduct());
            }else{
                groupedOrder.put(order.getNumber(), order);
            }
        });
       return groupedOrder.values();
    }
    

    
}
