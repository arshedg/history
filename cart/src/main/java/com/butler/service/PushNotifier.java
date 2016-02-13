package com.butler.service;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.butler.data.pushformat.Header;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author arsh
 */
public class PushNotifier implements Runnable{

    private PushNotifier(){
        
    }
    public static void sendNotification(){
        PushNotifier notifier = new PushNotifier();
        new Thread(notifier).start();
    }
    @Override
    public void run() {
        send();
    }

    private void send() {
        try {
            String url="https://android.googleapis.com/gcm/send";
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            request.addHeader("Authorization", "key=AIzaSyCBODt7lH-oPSKiZJ5MJlugTS0BV3U-KGc");
            request.addHeader("Content-Type", "application/json");
            Header header = new Header();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String rawData = ow.writeValueAsString(header);
            request.setEntity(new StringEntity(rawData));
            HttpResponse response = client.execute(request);
           // System.out.println("response"+IOUtils.toString(response.getEntity().getContent()));
        } catch (Exception ex) {
            Logger.getLogger(PushNotifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
