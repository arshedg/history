/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.fishcartserver;

import com.butler.data.Product;
import com.butler.service.ProductDao;
import com.butler.service.analytics.AsyncWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author arsh
 */
@RestController
public class ProductControler {

    @Autowired
    ProductDao dao;
    @Autowired
    AsyncWriter vistRegiser;
     @RequestMapping(value="/product/list",
               method=RequestMethod.GET) 
    public @ResponseBody List<Product> getAllProducts(@RequestParam(value="number",required = false) String number) {
        vistRegiser.registerVisit(number);
        return dao.getAllProducts();
    }

    @RequestMapping(value="/product/image",
            method=RequestMethod.GET,
            produces="image/jpg")
    public @ResponseBody
    byte[] getImage(@RequestParam(value = "name") final String product) {
        try {
            InputStream is = dao.getImage(product);
            BufferedImage img = ImageIO.read(is); 
            
            // Create a byte array output stream.
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            
            // Write to output stream
            ImageIO.write(img, "jpg", bao);
            
            return bao.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(ProductControler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
     
     
    }
    @RequestMapping(value="/product/listall",
               method=RequestMethod.GET)
    public @ResponseBody List<Product> getCompleteProducts() {
        return dao.getCompeteProducts();
    }
 @RequestMapping(value="/product/update",
               method=RequestMethod.POST)
    public @ResponseBody int updateProduct( @RequestBody Product product) {
        return dao.updateProduct(product);
    }
 @RequestMapping(value="/product/insert",
               method=RequestMethod.POST)
    public @ResponseBody boolean insertProduct( @RequestBody Product product) {
         dao.insertProduct(product);
         return true;
    }
    
}
