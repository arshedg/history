/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.model;

import com.fluke.model.ticker.Ticker;
import com.fluke.util.Configuration;
import com.fluke.util.Util;
import java.util.Date;

/**
 *
 * @author arshed
 */
public class Index {
    int maxMinBonus=0;
    public Equity nifty;
    public void loadNext(Date date){
        Ticker ticker = nifty.intraday.getNextTicker();
        if(ticker==null) return;
        String indexTime = Util.getTime(ticker.getDate());
        String equityTime = Util.getTime(date);
        if(!indexTime.equals(equityTime)){
            correctIndex(indexTime, equityTime);
        }
    }
    private boolean correctIndex(String index,String stock){
        int diff = getMinutes(stock)-getMinutes(index);
        int absDiff = Math.abs(diff);
        if(absDiff<=maxMinBonus){
            return true;
        }
        if(diff>maxMinBonus){
            setIndexTime(getMinutes(stock), true);
            lookBackCorrection(getMinutes(stock), true);
        }else{
            setIndexTime(getMinutes(stock), false);
            lookBackCorrection(getMinutes(stock), false);
        }
        return true;
    }
    private void lookBackCorrection(int minutes,boolean forward){
        int indexTime = getMinutes(Util.getTime(nifty.intraday.getCurrentTicker().getDate()));
        if(indexTime==minutes) return;
        int pointer = nifty.intraday.pointer;
        if(pointer==-1) return;
        int currentDiff = Math.abs(indexTime-minutes);
        if(forward){
            
            int prvsIndexTime = getMinutes(Util.getTime(nifty.intraday.get(pointer-1).getDate()));
            int olderDiff = Math.abs(prvsIndexTime-minutes);
            if(olderDiff<currentDiff){
                nifty.intraday.rewind();
            }
        }else if(pointer<nifty.intraday.size()-1){
             int prvsIndexTime = getMinutes(Util.getTime(nifty.intraday.get(pointer+1).getDate()));
             int olderDiff = Math.abs(prvsIndexTime-minutes);
             if(olderDiff<currentDiff){
                nifty.intraday.getNextTicker();
            }
        }
    }
    private void setIndexTime(int minute,boolean forward){
        
        if(forward){
            Ticker ticker;
            do{
                  ticker = nifty.intraday.getNextTicker();
                  if(ticker==null) return;//end of market
            }
            while(!isMatching(ticker, minute, forward));
        }else{
            for(int i=nifty.intraday.size()-1;i<=0;i--){
                Ticker tick = nifty.intraday.get(i);
                if(isMatching(tick, minute, false)){
                    return;
                }else{
                    nifty.intraday.rewind();
                }
            }
        }
    }
    public boolean isMatching(Ticker tick,int mins,boolean forward){
        int indexTime = getMinutes(Util.getTime(tick.getDate()));
        if(indexTime>=mins&&forward){
            return true;
        }else if(indexTime<=mins&&!forward){
            return true;
        }
        return false;
    }
    public Index(Configuration config){
        nifty = new Equity(".NSED", config);
    }
    private int getMinutes(String time){
        String parts[] = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int min = Integer.parseInt(parts[1]);
        return hour*60+min;
    }
}
