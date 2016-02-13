/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.service;

import com.butler.data.Product;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

/**
 *
 * @author arsh
 */
public class ProductDao extends SimpleJdbcDaoSupport{
    
    public List<Product> getAllProducts(){
        String sql = "select name,display_name,market_price,selling_price,size,type,booking_only from product where visible=1 order by display_position,booking_only";
        return this.getJdbcTemplate().query(sql, new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                Product product = new Product();
                product.setName(rs.getString("name"));
                product.setDisplayName(rs.getString("display_name"));
                product.setSellingPrice(rs.getFloat("selling_price"));
                product.setMarketPrice(rs.getFloat("market_price"));
                product.setSizeSpecification(rs.getString("size"));
                product.setType(rs.getString("type"));
                product.setBookingOnly(rs.getBoolean("booking_only"));
                return product;
            }
        });
    }
    public List<Product> getCompeteProducts(){
        String sql = "select name,display_name,display_position,market_price,selling_price,size,type,visible,booking_only from product  order by display_position";
        return this.getJdbcTemplate().query(sql, new RowMapper() {

            @Override
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                Product product = new Product();
                product.setName(rs.getString("name"));
                product.setDisplayName(rs.getString("display_name"));
                product.setSellingPrice(rs.getFloat("selling_price"));
                product.setMarketPrice(rs.getFloat("market_price"));
                product.setSizeSpecification(rs.getString("size"));
                product.setType(rs.getString("type"));
                product.setVisible(rs.getBoolean("visible"));
                product.setDisplayPosition(rs.getInt("display_position"));
                product.setBookingOnly(rs.getBoolean("booking_only"));
                return product;
            }
        });
    }
    public InputStream getImage(String product){
        String sql = "select pic from product where name='"+product+"'";
        return (InputStream) this.getJdbcTemplate().query(sql,  new ResultSetExtractor() {
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                rs.next();
                Blob imageBlob = rs.getBlob("pic");
                InputStream binaryStream = imageBlob.getBinaryStream(1, imageBlob.length());
                return binaryStream;
            }
        });
    
    }
    public int updateProduct(Product product){
        String sql="update product set name=?,display_name=?,selling_price=?,market_price=?,size=?,type=?,visible=?,booking_only=?,display_position=? where name=?";
        return this.getSimpleJdbcTemplate().update(sql, product.getName(),product.getDisplayName(),
                product.getSellingPrice(),product.getMarketPrice(),product.getSizeSpecification(),product.getType(),product.isVisible(),product.isBookingOnly(),product.getDisplayPosition(),product.getName());
    }
    public void insertProduct(Product p){
        String sql="insert into product(name,display_name,selling_price,market_price,size,type,visible) values "
                + "(?,?,?,?,?,?,?)";
        this.getSimpleJdbcTemplate().update(sql, p.getName(),p.getDisplayName(),p.getSellingPrice(),p.getMarketPrice(),
                    p.getSizeSpecification(),p.getType(),p.isVisible());
    }
}
