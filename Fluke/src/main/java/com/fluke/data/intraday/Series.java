/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.data.intraday;

import com.fluke.util.Util;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import java.sql.Timestamp;

@Generated("org.jsonschema2pojo")
public class Series {

    @Expose
    private Long Timestamp;
    @Expose
    private  Float close;
    @Expose
    private  Float high;
    @Expose
    private  Float low;
    @Expose
    private  Float open;
    @Expose
    private Long volume;

    /**
     *
     * @return The Timestamp
     */
    public Timestamp getTimestamp() {
        return new Timestamp(Timestamp*1000);
    }

    /**
     *
     * @param Timestamp The Timestamp
     */
    public void setTimestamp(Long timestamp) {
        this.Timestamp = timestamp;
    }

    /**
     *
     * @return The close
     */
    public  Float getClose() {
        return close;
    }

    /**
     *
     * @param close The close
     */
    public void setClose( Float close) {
        this.close = close;
    }

    /**
     *
     * @return The high
     */
    public  Float getHigh() {
        return high;
    }

    /**
     *
     * @param high The high
     */
    public void setHigh( Float high) {
        this.high = high;
    }

    /**
     *
     * @return The low
     */
    public  Float getLow() {
        return low;
    }

    /**
     *
     * @param low The low
     */
    public void setLow( Float low) {
        this.low = low;
    }

    /**
     *
     * @return The open
     */
    public  Float getOpen() {
        return open;
    }

    /**
     *
     * @param open The open
     */
    public void setOpen( Float open) {
        this.open = open;
    }

    /**
     *
     * @return The volume
     */
    public Long getVolume() {
        return volume;
    }

    /**
     *
     * @param volume The volume
     */
    public void setVolume(Long volume) {
        this.volume = volume;
    }

}
