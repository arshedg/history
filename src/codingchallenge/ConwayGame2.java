/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codingchallenge;

/**
 *
 * @author agulshan
 */
public class ConwayGame2 {
     public static void main(String arg[]) throws Exception{
        Cell state[][] =    {
                                    {Cell.DeadCell(0,0),Cell.DeadCell(0,1),Cell.DeadCell(0,2),Cell.DeadCell(0,3),Cell.DeadCell(0,4)},
                                    {Cell.DeadCell(1,0),Cell.DeadCell(1,1),Cell.DeadCell(1,2),Cell.DeadCell(1,3),Cell.DeadCell(1,4)},
                                    {Cell.DeadCell(2,0),Cell.LiveCell(2,1),Cell.LiveCell(2,2),Cell.LiveCell(2,3),Cell.DeadCell(2,4)},
                                    {Cell.DeadCell(3,0),Cell.DeadCell(3,1),Cell.DeadCell(3,2),Cell.DeadCell(3,3),Cell.DeadCell(3,4)},
                                    {Cell.DeadCell(4,0),Cell.DeadCell(4,1),Cell.DeadCell(4,2),Cell.DeadCell(4,3),Cell.DeadCell(4,4)},
                            };
        Universe.setState(state);
        while(Universe.afterSunRise()){
           Universe.showUniverse();
        }
        
    }
}
