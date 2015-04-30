/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rhino.service;

import com.rhino.data.Equity;
import com.rhino.data.Ticker;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author agulshan
 */
public class StrategyManager {
    
    List<Equity> equities = new ArrayList<>();
    Portfolio portfolio;
    public void addStock(Equity... stocks){
        for(Equity equity:stocks){
            equities.add(equity);
        }
    }
    
    public void setPortfolio(Portfolio portfolio){
        this.portfolio = portfolio;
    }
    
    public void run(){
        moveDays(portfolio.getStrategy().leastDataRequired());
        addStocksToPortfolio();
        List<Equity> toDelete = null;
        while(!equities.isEmpty()){
            toDelete = runEquities();
            if(toDelete!=null){
                equities.removeAll(toDelete);
            }
        }
        portfolio.status();
    }
    private void moveDays(int days){
        List<Equity> toDelete = new ArrayList<>();
        for(int i=0;i<days;i++){
            for(Equity eq:equities){
                if(!eq.hasNext()){
                    toDelete.add(eq);
                }
                Ticker next = eq.getNextTicker();
                portfolio.handleTickerChange(eq,next);
            }
        }
        equities.removeAll(toDelete);
    }
    private List<Equity> runEquities(){
        List<Equity> toDelete = new ArrayList<>();
        for(Equity eq:equities){
            if(!eq.hasNext()){
                toDelete.add(eq);
            }
            Ticker next = eq.getNextTicker();
            portfolio.handleTickerChange(eq,next);
        }
        return toDelete;
    }

    private void addStocksToPortfolio() {
        for(Equity eq:equities){
            portfolio.addToWatch(eq);
        }
    }
            
}
