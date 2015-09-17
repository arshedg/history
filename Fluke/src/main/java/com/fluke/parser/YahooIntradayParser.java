package com.fluke.parser;

import com.fluke.data.intraday.IntradayDetails;
import com.google.gson.Gson;
import java.io.InputStream;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author arshed
 */
public class YahooIntradayParser {
   private static final String URL="http://chartapi.finance.yahoo.com/instrument/1.0/%s.NS/chartdata;type=quote;range=1d/json";
   protected Object parseData(String data) {
       Gson gson = new Gson();
       return gson.fromJson(data, IntradayDetails.class);
   }
   public IntradayDetails getIntradayDetails(String name) {
       return (IntradayDetails) parseData(fetchData(name));
   }
   protected String fetchData(String name) {
       name = name.replaceAll("&", "%26");
       String completeUrl = String.format(URL, name);
        InputStream in = null;
        try {
              in = new URL( completeUrl ).openStream();
              return filter(IOUtils.toString( in )); 
        }catch(Exception ex){
            //error while geting historic data, no need to log
            throw new RuntimeException(ex);
        }
        finally {
            IOUtils.closeQuietly(in);
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
