/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codingchallenge;

import codingchallenge.rules.Rule;
import java.util.ServiceLoader;




public class Universe {
    
    private static Cell[][] state;
    
    public static Cell[][] getUniverseSnapShot(){
        return state;
    }
    
    public static void setState(Cell[][] newState){
        state=newState;
    }
    
    private static void nextDay() throws Exception{
        Cell stateOfTomorrow[][] = new Cell[state.length][state[0].length];
         for(Cell cells[] : state){
            for(Cell cell:cells){
                Cell newCell = cell.clone();
                applyRules(newCell);
                stateOfTomorrow[newCell.xCordinate][newCell.yCordinate] = newCell;
            }
         }
         state = stateOfTomorrow;
    }
    
    

    
    public static void applyRules(Cell cell){
        ServiceLoader<Rule> rules = ServiceLoader.load(Rule.class);
        for(Rule rule:rules){
            rule.apply(cell);
        }
    }
    

    
    private static void printCell(Cell cell) {
        if(cell.isDead){
            System.out.print(" ");
        }else{
            System.out.print("*");
        }
    }

  public static boolean afterSunRise() throws Exception{
      Thread.sleep(1000);//length of a typical day
      nextDay();
      return true;
  }
  
      public  static void showUniverse(){
        System.out.println("#################################################");
        for(Cell cells[] : state){
            System.out.print("#\t\t\t");
            for(Cell cell:cells){
                printCell(cell);
            }
            System.out.println("\t\t\t#");
        }
        
    }
}
