/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codingchallenge.rules;

import codingchallenge.Cell;

/**
 *
 * @author agulshan
 */
public class LeaveHimAloneRule implements Rule{


    private boolean doesApply(Cell cell) {
        int neighBourCount = cell.getAllNeighBours().size();
        return neighBourCount==2 || neighBourCount == 3;
    }

    @Override
    public void apply(Cell cell) {
        if(doesApply(cell)){
            //do nothing
        }
    }

}
