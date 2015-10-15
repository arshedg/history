/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service.launch;

import com.rhino.data.db.TickerDao;
import com.rhino.data.fetcher.YahooParser;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author arshed
 */
public class Launcher {
    public static void main(String[] args) throws SQLException, Exception {
        System.out.println("ALWAYS RUN THE SOFTWARE UPDATE AFTER 4PM and before 11:30PM");
        System.out.println("1. UPDATE SOFTWARE");
        System.out.println("2. RUN STRATEGY 'RIVER FLOW'");
        System.out.println("3. EXIT");
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try{
            input = scanner.nextInt();
        }catch(Exception ex){
            System.out.println("INVALID OPTION");
            main(null);
        }
        switch(input){
            case 1: update();
                    break;
            case 2: runRiverFlow();
                    break;
            case 3: System.exit(0);
        }
        main(null);
    }
    public static void runRiverFlow() throws SQLException{
        System.out.println("Running strategy river flow. Please wait.....");
        //RiverGrowth.runStrategy();
    }

    private static void update() throws Exception {
        System.out.println("About to update.. Are you sure ? Y/N");
        Scanner scan1 = new Scanner(System.in);
        String val = scan1.next();
        if(val.toLowerCase().equals("n")){
            System.out.println("User ask for cancellation. Updation cancelled");
            return;
        }else if(!val.toLowerCase().equals("y")){
            System.out.println("Invalid input cancelling updation");
            return;
        }
        
       Scanner scan = new Scanner(ClassLoader.getSystemResourceAsStream("stocks"));
        TickerDao ticker = new TickerDao();
        List<String> list = new ArrayList<>();
        while(scan.hasNext()){
            String name = scan.next().trim();
            if(name.equals("")){
                continue;
            }
            list.add(name);

        }
        add(list.toArray(new String[0]));
    } 
        
     private static void add(String... names) throws Exception{
       for(String name:names){
           YahooParser parser = new YahooParser(name);
           parser.setExchange("NSE");
           parser.grade="FUTURE";
           parser.process();
       }
    } //FileInputStream file = new FileInputStream("stocks");}
     
        
}
