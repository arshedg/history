package com.fluke.parser;

import com.fluke.data.intraday.IntradayDetails;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author arshed
 */
public class YahooIntradayParser {
   private static  String URL="http://chartapi.finance.yahoo.com/instrument/1.0/%s.NS/chartdata;type=quote;range=1d/json";
   protected Object parseData(String data) {
       Gson gson = new Gson();
  //     System.out.println(data);
       return gson.fromJson(data, IntradayDetails.class);
   }
   public IntradayDetails getIntradayDetails(String name) {
       return (IntradayDetails) parseData(fetchData(name));
   }
   protected String fetchData(String name) {
       name = name.replaceAll("&", "%26");
       if(name.equals(".NSEI")){
           name="^NSEI";
       }
       if(name.contains("^")){
           URL="http://chartapi.finance.yahoo.com/instrument/1.0/%s/chartdata;type=quote;range=1d/json";
       }
       String completeUrl = String.format(URL, name);
        InputStream in = null;
        URL uri;
        try {
              uri = new URL(completeUrl);
              in = uri.openStream();
              return filter(IOUtils.toString( in )); 
        }catch(Exception ex){
            //error while geting historic data, no need to log
            throw new RuntimeException(ex);
        }
        finally {
           if(in!=null){
               try {
                   in.close();
               } catch (IOException ex) {
                   throw new RuntimeException(ex);
               }
           }
        
        }
        
   }
   protected String filter(String data){
       String unwantedString[]={"finance_charts_json_callback\\(","\\)"};
       for(String remove:unwantedString){
           data = data.replaceAll(remove, StringUtils.EMPTY);
       }
       return data;
   }
   
   
   
   
}
