/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhino.Listener;

import com.rhino.data.Equity;

/**
 *
 * @author arshed
 */
 public interface TickerChangeListener {
     public void execute(Equity equity);
  }
