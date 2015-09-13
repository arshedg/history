/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.application;

import com.fluke.database.dataservice.IntraDayDao;
import com.fluke.model.ticker.IntradayTicker;
import com.fluke.util.Util;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author arshed
 */
public class IEODReader {
    
   static List<String> list;
   static Map<String,String> nameMap = new HashMap<>();
    static
    {
        nameMap.put("INFOSYSTCH", "INFY");
        nameMap.put("HEROMOTOCO", "HEROHONDA");
    }
    public static void main(String[] args) throws IOException {
        String location="/home/arshed/Pictures/sample";
        list = IOUtils.readLines(ClassLoader.getSystemResourceAsStream("config/stocks"));
        File folder = new File(location);
        processFolder(Arrays.asList(folder));
        
    }
    static void processChildren(File[] files,List<File> newFolders) throws IOException{
        for(File file:files){
          if(file.isDirectory()){
              newFolders.add(file);
          }else {
              processFile(file);
          } 
        }
    }
    static void processFolder(List<File> folders) throws IOException{
        List<File> newFolders = new ArrayList<>();
        for(File file:folders){
            processChildren(file.listFiles(), newFolders);
        }
       if(!newFolders.isEmpty()){
           processFolder(newFolders);
       }
    }
    private static void processFile(File file) throws IOException{
        String name = file.getName().split("\\.")[0];
        name = nameMap.get(name)==null?name:nameMap.get(name);
        if(name.startsWith("_")||list.contains(name.toUpperCase())){
            List<String> lines = FileUtils.readLines(file);
            process(name, lines);
        }
    }
    private static void process(String stockName,List<String> lines){
        List<IntradayTicker> tickers = new ArrayList<>();
        for(String line:lines){
            if(line.toLowerCase().contains("<ticker>")){
                continue;
            }
            String parts[] = line.split(",");
            IntradayTicker ticker = new IntradayTicker();
            ticker.setTime(getTimestamp(parts[1], parts[2]));
            ticker.setOpenPrice(Float.parseFloat(parts[3]));
            ticker.setHighPrice(Float.parseFloat(parts[4]));
            ticker.setLowPrice(Float.parseFloat(parts[5]));
            ticker.setClosePrice(Float.parseFloat(parts[6]));
            ticker.setVolume(Integer.parseInt(parts[7]));
            tickers.add(ticker);
            stockName=parts[0];
        }
        new IntraDayDao().insertBatch(stockName, tickers);
    }
    static Timestamp getTimestamp(String date,String time){
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6,8);
            String timeParts[]=time.split(":");
            
            Date givenDate = Util.getIEODDate(day+"-"+month+"-"+year+","+timeParts[0]+":"+timeParts[1]);
            return new Timestamp(givenDate.getTime());
    }
}
