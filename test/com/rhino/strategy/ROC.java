/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhino.strategy;

import com.rhino.listener.strategy.ExitStrategy;
import com.rhino.data.Equity;
import com.rhino.data.history.util.Portfolio;
import org.junit.Test;

/**
 *
 * @author arshed
 */
public class ROC {
    @Test
    public void rateOfChangeIndicator(){
        //http://docs.rizm.io/rate-of-change
        
        Equity equity = Equity.loadEquity("WIPRO", "2007-07-05", "2009-10-01");
        int rocPeriod = 12;
        moveCursor(equity, rocPeriod);
        Portfolio pf = new Portfolio();
        pf.setExitStrategy(new ExitImplementation());
        int size = equity.getList().size();
        while(equity.getNextTicker()!=null){
            if(size<=equity.getPointer()+1) break;
            float rate = equity.rateOfChange(rocPeriod);
            if(rate>-2.7&&rate<0){
                pf.add(equity);
            }
        }
        pf.status();
    }
    private void moveCursor(Equity eq,int daysToMove){
        for(int i=0;i<daysToMove;i++){
            eq.getNextTicker();
        }
    }
    
}



class ExitImplementation implements ExitStrategy{

    @Override
    public boolean canExit(Equity equity) {
      
        if(equity.rateOfChange(12)>3.2){
           return true;
       }else{
           return false;
       }
    }
    
}
