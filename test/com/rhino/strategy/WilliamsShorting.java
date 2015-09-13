/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhino.strategy;

import com.rhino.data.Equity;
import com.rhino.data.Ticker;
import com.rhino.data.db.EquityDao;
import com.rhino.data.history.util.Util;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arshed
 */
public class WilliamsShorting extends SimulWithLossControl {

    int getLeastVolume(List<Ticker> list, int ptr) {
        int least = Integer.MAX_VALUE;
        for (int i = 0; i < 14; i++) {
            int vol = list.get(ptr - i).getVolume();
            if (vol < least) {
                least = vol;
            }
        }
        return least;
    }

    Ticker getMaxChange(List<Ticker> list, int ptr) {
        float highest = 0;
        Ticker highTick=null;
        Ticker prevs = list.get(ptr-10);
        for (int i = ptr-9; i < ptr; i++) {
            Ticker today = list.get(i);
            float change = Util.findPercentageChange(today.getClosePrice(), prevs.getClosePrice());
            if(change>highest){
                highest=change;
                highTick = today;
            }
            prevs=today;
        }
        return highTick;
    }

    float getValue(List<Ticker> list, int ptr) {
        Ticker tick = list.get(ptr);
        float sum = tick.getOpenPrice() + tick.getHighPrice() + tick.getLowPrice() + tick.getClosePrice();
        float avg = sum / 4f;
        return (float) tick.getVolume() * avg;

    }

    protected List<Equity> getAll() throws SQLException {
        String from = "2010-4-17";
        String to = "2015-08-15";
        List<Equity> loaded = new ArrayList<>();
        for (String name : new EquityDao().getAllEquity("FUTURE")) {
            loaded.add(Equity.loadEquity(name, from, to));
        }
        return loaded;
    }

    @Override
    public boolean canEnter(Equity equity) {
        float low = equity.getTicker().getLowPrice();
        float high = equity.getTicker().getHighPrice();
        float open = equity.getTicker().getOpenPrice();
        float close = equity.getTicker().getClosePrice();
        Ticker gainer = getMaxChange(equity.getList(), equity.getPointer());
        if(gainer==null) return true;
        if(gainer.getClosePrice()>low&&open>gainer.getClosePrice()&&close<gainer.getClosePrice()&&high<gainer.getHighPrice())
            return true;
        return false;
    }

    @Override
    public boolean canExit(Equity equity, int entryPointer) {
        return equity.getPointer()-entryPointer>=2;
       
    }

    @Override
    public float getClosePrice(Equity equity, int entryPointer) {
       Ticker boughtDayTicker = equity.getList().get(entryPointer);
       float priceDecline = Util.findPercentageChange(equity.getTicker().getHighPrice(),boughtDayTicker.getClosePrice() );
       if(priceDecline>5){
           return Util.findTargetPrice(boughtDayTicker.getClosePrice(), 5);
       }
       return equity.getTicker().getClosePrice();
    }

    @Override
    public StrategyType getStrategyType() {
        //buy or sell
        return StrategyType.SHORT;
    }

    @Override
    public int leastDataRequired() {
        //minimum data required to start with
        //In this case we are calculating "equity.simpleMovingAverage(9)", which mean atleast 9 days of data is required to start with
        return 15;
    }

}
