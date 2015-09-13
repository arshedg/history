/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhino.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.After;

/**
 *
 * @author arshed
 */
public abstract class StrategyBase implements Strategy{
      void inc(Date date){
       Integer count =  counter.get(date);
       if(count==null){
           counter.put(date, 1);
       }else{
           counter.put(date, count+1);
       }
    }
      void addToLookup(Date date,String name){
          List<String> list = buffer.get(date);
          if(list==null){
              list = new ArrayList<>();
              buffer.put(date, list);
          }
          list.add(name);
      }
      boolean lookUp(Date date,String name){
          if(buffer.get(date)==null) return false;
          for(String eq:buffer.get(date)){
              if(eq.equals(name)){
                  return true;
              }
          }
          return false;
      }
      void init(Date date){
        Integer count = counter.get(date);
        if(count==null){
            counter.put(date, 0);
        }
    }
      void hit(Date date){
        Integer count = hitCount.get(date);
        if(count==null){
            hitCount.put(date, 1);
        }else{
            hitCount.put(date, count+1);
        }
        
    }
      void profit(Date date,float gain){
          if(getStrategyType()==StrategyType.SHORT){
             gain =-1*gain; 
          }
        Float pf = profit.get(date);
        if(pf==null){
            pf=0f;
        }
        profit.put(date, pf+gain);
    }
    @After
    public void print(){
        System.out.println("Day wise count");
        int total=0,pf=0;
        float totalPf=0f;
        float maxLoss=0f;
        for(Date date:counter.keySet()){
            Integer count = counter.get(date);
            if(count>4){
                System.out.println("skiping "+date);
                continue;
            }
            Integer hit = hitCount.get(date);
            if(hit==null) hit=0;
            pf+=hit;
            total+=count;
            float curPf = profit.getOrDefault(date, 0f);
            if(curPf<-4){
                curPf = -5.5f;
            }
            totalPf+=curPf;
            System.out.println(date+"\t"+counter.get(date)+"\t"+hit+"\t"+curPf);
        }
        System.out.println("total profit:"+totalPf);
        System.out.println("Total trade :"+total);
        System.out.println("Profit per trade"+(totalPf/total));
        if(total!=0)
            System.out.println("HIT ratio "+(100*pf/total));
        System.out.println("Max loss:"+maxLoss);
    }
    private  Map<Date,List<String>> buffer = new TreeMap<>();
    private  Map<Date,Integer> counter = new TreeMap<>();
    private  Map<Date,Float> profit = new HashMap<>();
     private  Map<Date,Integer> hitCount = new TreeMap<>();
}
