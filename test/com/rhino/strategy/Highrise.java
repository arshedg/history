/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.strategy;

import com.rhino.data.Equity;
import com.rhino.data.Ticker;
import com.rhino.data.history.util.Portfolio;
import com.rhino.data.history.util.Util;
import static com.rhino.data.history.util.Util.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author agulshan
 */
public class Highrise {
    
    
   // @Test
    public void suddenGrowthStock(){
        Equity tcs = Equity.loadEquity("tcs", "2010-04-12", "2010-12-12");
        Equity infy = Equity.loadEquity("rushil", "2010-1-1", "2014-12-12");
        Equity curr = infy;
        Ticker prevs = tcs.getNextTicker();
        while(curr.hasNext()){
            Ticker current = curr.getNextTicker();
            float closeDiff= findPercentage(prevs.getClosePrice(), current.getClosePrice());
            float highDiff= findPercentage(prevs.getHighPrice(), current.getHighPrice());
            float diff= (closeDiff+highDiff)/2;
            float volDiff = findPercentage(prevs.getVolume(), current.getVolume());
            if(diff>2&&volDiff>30){
                print("buy signal");
                Portfolio pf = new Portfolio();
                pf.add(curr);
                pf.calculateProfit(2);
            }
            prevs = current;
        }
        System.out.println("gross profit :"+Portfolio.grossProfit);
    }
    @Test
    public void volumeDiminisher(){
       /*
        a buy signal
        ->  yesterday price has gone up, than day before yesterday.
        ->  yesterday volume has increased atleast 40% than day before yesterday
        ->  Today price has gone up but volume dropped by atleast 30%
        $$ Prediction:-> Tomorow price will decrease
        */
        
        Equity eq = Equity.loadEquity("DELTACORP", "2002-04-12", "2014-12-12");
        Ticker prevs = eq.getNextTicker();
        boolean check3Activated=false;
        boolean sell = false;
        float count=0f;
        while(eq.hasNext()){
            Ticker current = eq.getNextTicker();
            if(sell){
                Portfolio pf = new Portfolio();
                pf.add(eq);
                pf.calculateProfit(7);
//                print("stock shorted on "+Util.getDate(prevs.getDate())+" at price "+prevs.getClosePrice());
//                print("stock bought back  on "+current.getDate()+" at price "+current.getClosePrice());
//                print("gain "+Util.findPercentageChange(prevs.getClosePrice(),current.getClosePrice()));
//                count+=Util.findPercentageChange(prevs.getClosePrice(),current.getClosePrice());
                sell=false;
            }
            if(check3Activated){
                 check3Activated=false;
               if(check3(current,prevs)){
                    sell = true;
               }
            }
            if(isChanged(current, prevs)&&isVolumeIncreased(current,prevs)){
               check3Activated=true;
            }
            prevs = current;
        }
        print("GRAND TOTAL   :"+Portfolio.grossProfit);
        
    }
    private void print(String string){
        System.out.println(string);
    }
    private boolean check3(Ticker current,Ticker prevs){
        //Today price has gone up but volume dropped by atleast 30% 
        if(current.getClosePrice()<prevs.getClosePrice()){
            return false;
        }
        return isVolumeIncreased(prevs, current);
    }
    private boolean isChanged(Ticker current,Ticker prevs){
        float required = current.getClosePrice()*(.015f);
        if(prevs.getClosePrice()>current.getClosePrice()+required){
            return true;
        }
        return false;
    }

    private boolean isVolumeIncreased(Ticker current, Ticker prevs) {
        if(current.getVolume()>prevs.getVolume()){
            float volChange = Util.findPercentageChange(current.getVolume(),prevs.getVolume());
            if(volChange>50){
                return true;
            }
        }
        return false;
       
    }
    
}
