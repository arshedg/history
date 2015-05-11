/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhino.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arshed
 */
public class Threads implements Runnable{
     
    counter c;
    public static void  main(String arg[]){
        counter c = new counter();
        Threads t1 = new Threads(c);
        Threads t2 = new Threads(c);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(t1);
        executor.submit(t2);
        executor.shutdown();
    }
    public Threads(counter c){
        this.c = c;
    }
    @Override
    public void run() {
        try {
            while(true)
            c.print();
        } catch (InterruptedException ex) {
            Logger.getLogger(Threads.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
class counter{
    volatile int count=1;
    Lock lock = new ReentrantLock();
    Condition con = lock.newCondition();
     void print() throws InterruptedException{
        lock.lock();
        con.signal();
        System.out.println("Thread:"+Thread.currentThread().getName()+"\tNumber :"+count++);
        if(count>10) System.exit(0);
        con.await();
    }

}
