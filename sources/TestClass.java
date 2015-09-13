
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author arshed
 */
public class TestClass {
    public static void main(String[] args) {
      
        ConsoleReader reader = new ConsoleReader();
        String string = reader.getLine();
        int t = reader.getInt();
        Map<String,String> map = createMap(string);
        for (int i = 0; i < t; i++) {
             solve(map,reader);
        }
       
        
    }
    private static Map<String,String>  createMap(String string){
        Map<String,String> map = new HashMap<>();
        for(int i=0;i<string.length();i++){
            for(int j=i;j<string.length();j++){
                map.put(i+":"+j, string.substring(i, j+1));
            }
        }
        return map;
    }
    private static void solve(Map<String,String> map, ConsoleReader reader) {
        int l1=reader.getInt()-1;
        int r1=reader.getInt()-1;
        int l2=reader.getInt()-1;
        int r2=reader.getInt()-1;
        if(l1-r1!=l2-r2){
            System.out.println("No");
            return;
        }
        if(map.get(l1+":"+r1).equals(map.get(l2+":"+r2))){
            System.out.println("Yes");
        }else{
            System.out.println("No");
        }
    }
 
   
static class ConsoleReader{
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    LinkedList<String> tokens = new LinkedList<>();
    public int getInt(){
        String token = getToken();
        return Integer.parseInt(token);
    }
    public long getLong(){
        return Long.parseLong(getToken());
    }
    public float getFloat(){
        return Float.parseFloat(getToken());
    }
    public String getLine(){
        try {
            tokens.clear();
            return reader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException("reading failed");
        }
    }
    private String getToken() {
  
        if(tokens.isEmpty()){
            try {
                String line = reader.readLine();
                String parts[] = line.split(" ");
                for(String part:parts){
                    tokens.add(part);
                }
            } catch (IOException ex) {
                throw new RuntimeException("not able to read");
            }
        }
        return tokens.poll();
    }
}
   
}
