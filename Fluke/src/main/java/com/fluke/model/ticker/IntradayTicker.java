/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.model.ticker;

import com.fluke.util.Util;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author arshed
 */
public class IntradayTicker extends Ticker {
   private Timestamp time;

    /**
     * @return the time
     */
    public Timestamp getTime() {
        return time;
    }
    
    
    /**
     * @param time the time to set
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }
    public Date getDate() {
        return Date.from(time.toInstant());
    }

  
}
