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
public class KillForUnderPopulationRule implements Rule{
    

    private boolean doesApply(Cell cell){
        
        return cell.getAllNeighBours().size()<2;
        
    }
    public void apply(Cell cell) {
        if(doesApply(cell)){
            cell.kill();
        }
    }
}
