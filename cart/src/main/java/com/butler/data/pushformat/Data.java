/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.butler.data.pushformat;

import java.io.Serializable;

/**
 *
 * @author arsh
 */
public class Data implements Serializable{
    private String messsage="A new order is placed";
    private String tickerText="order";
    private String title="New order";
    private String subtitle="Another order is recieved";
    private int vibrate=1;
    private String sound="default";

    /**
     * @return the messsage
     */
    public String getMesssage() {
        return messsage;
    }

    /**
     * @return the tickerText
     */
    public String getTickerText() {
        return tickerText;
    }

    /**
     * @return the vibrate
     */
    public int getVibrate() {
        return vibrate;
    }

    /**
     * @return the sound
     */
    public String getSound() {
        return sound;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }
}
