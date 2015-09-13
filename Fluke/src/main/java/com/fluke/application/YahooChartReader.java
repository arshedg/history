/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.application;

import com.fluke.data.intraday.IntradayDetails;
import com.fluke.database.dataservice.IntraDayDao;
import com.fluke.parser.YahooIntradayParser;
import java.util.Scanner;

/**
 *
 * @author arshed
 */
public class YahooChartReader {
    public static void main(String arg[]){
        Scanner scan = new Scanner(ClassLoader.getSystemResourceAsStream("config/stocks"));
        YahooIntradayParser parser = new YahooIntradayParser();
        IntraDayDao dao = new IntraDayDao();
        while(scan.hasNextLine()){
            
            String name = scan.nextLine().trim();
            System.out.println("processing "+name);
            try{
                IntradayDetails details = parser.getIntradayDetails(name);
                dao.insert(details);
            }
            catch(Throwable tr){
                //throw new RuntimeException(tr);
                System.out.println("error parsing "+name);
            }
        }
    }
}
