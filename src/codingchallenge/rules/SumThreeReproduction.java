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
public class SumThreeReproduction implements Rule{


    public boolean doesApply(Cell cell) {
        int neighBourCount = cell.getAllNeighBours().size();
        return neighBourCount==3;
    }

    @Override
    public void apply(Cell cell) {
        if(doesApply(cell))
        cell.makeAlive();
    }
    
}
