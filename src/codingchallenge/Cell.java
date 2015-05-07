package codingchallenge;



import java.util.ArrayList;
import java.util.List;

public class Cell implements Cloneable{

    List<Cell> myNeighBours;
    boolean isDead;
    int xCordinate,yCordinate;

    
    public List<Cell> getAllNeighBours(){
        
        return findMyNeighbours();
        
    }
    
    public void kill(){
        isDead = true;
    }
    public void makeAlive(){
        isDead = false;
    }
    private Cell getNeighbour(Location location){
        return getNeighbour(location.getXCordinate(),location.getYCordinate());
    }
    private Cell getNeighbour(int xPos,int yPos){
        try{
            return Universe.getUniverseSnapShot()[xCordinate+xPos][yCordinate+yPos];
        }
        catch(ArrayIndexOutOfBoundsException oopsNoCell){
            return null;
        }
    }

    private void addToMyNeighBours(Cell neighbour) {
        if(neighbour!=null&&!neighbour.isDead){
            myNeighBours.add(neighbour);
        }
    }

    private List<Cell> findMyNeighbours() {
             
        
                /*   
                    Neighbour Map
        
                       North
                    NW   ^     NE
                         |
                WEST ____|____ East 
                         |
                    SW   |     SE 
                       South  
                   
                */
        myNeighBours = new ArrayList<Cell>();
        addToMyNeighBours(getNeighbour(Location.North));
        addToMyNeighBours(getNeighbour(Location.NorthEast));
        addToMyNeighBours(getNeighbour(Location.East));
        addToMyNeighBours(getNeighbour(Location.SouthEast));
        addToMyNeighBours(getNeighbour(Location.South)); 
        addToMyNeighBours(getNeighbour(Location.SouthWest));
        addToMyNeighBours(getNeighbour(Location.West));
        addToMyNeighBours(getNeighbour(Location.NorthWest));
        return myNeighBours;
    }
    
    public static Cell LiveCell(int x,int y){
        Cell cell = new Cell();
        cell.makeAlive();
        cell.xCordinate=x;
        cell.yCordinate=y;
        return cell;
    }
    public static Cell DeadCell(int x,int y){
        Cell cell = Cell.LiveCell(x,y);
        cell.kill();
        return cell;
    }
    
    @Override
    protected Cell clone() throws CloneNotSupportedException {
        return (Cell) super.clone();
    }
    
    
}

enum Location{
    North(-1,0),South(1,0),East(0,1),West(0,-1),NorthEast(-1,1),NorthWest(-1,-1),SouthEast(1,1),SouthWest(1,-1);
    int x,y;
    Location(int x,int y){
        this.x=x;
        this.y=y;
    }
    public int getXCordinate(){
        return x;
    }
    public int getYCordinate(){
        return y;
    }
    
}

