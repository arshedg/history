/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.application;

import com.fluke.parser.YahooEODParser;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author arshed
 */
public class EODReader {
    public static void main(String[] args) throws IOException, ParseException, SQLException {
        List<String> list = IOUtils.readLines(ClassLoader.getSystemResourceAsStream("config/stocks"));
        for(String eq:list){
            System.out.println(""+eq);
            YahooEODParser parser = new YahooEODParser(eq);
            parser.process();
        }
    }
}
