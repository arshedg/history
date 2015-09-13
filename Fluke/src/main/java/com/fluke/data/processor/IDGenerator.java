/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluke.data.processor;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author arshed
 */
public class IDGenerator {
    static AtomicLong AtomicLong = new AtomicLong(100);
    public static Long generateId(){
        return AtomicLong.addAndGet(2);
    }
    
}
