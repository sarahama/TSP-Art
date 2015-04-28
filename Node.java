import java.lang.Math;

public class Node {

    // attributes
    int xValue;      // represents the x-coordinate of the dot
    int yValue;      // represents the y-coordinate of the dot
    int weight;     // represents the color of the dot
    boolean v;      //true if the node has been visited
    int sum;        //sum of the distance to that node
    int index;      //the index of the node in the node list
    boolean stippled; //true if the node is being used as a stipple
    int count;      //the number of nodes the node is connected to in the MST


    // specific constructor
    public Node( int x, int y, int w){
    
       this.xValue = x;
       this.yValue = y;
       this.weight = w;
       this.v = false;
       this.index = -1;
       this.sum = 600; //make sure this starts at a value greater than the threshold
       this.stippled = false;
       this.count = 0;
    }


    // get the x-coordinate of the node *
    public int getX(){
    
        int r = this.xValue;
        return r;
    }

    // get the y-coordinate of the node *
    public int getY(){
    
        int r = this.yValue;
        return r;
    }

    // get the weight of the node *
    public int getWeight() {
    
        int r = this.weight;
        return r;
    }
    
    //return whether the node has been visited
    public boolean getV(){
        return this.v;
    }
    
    //set the node to visited
    public void visited(){
        this.v = true;
    }
    
   //return the value of the index 
    public int getIndex(){
        return this.index;
    }
    
    //set the index of the node to s
    public void setIndex(int s){
    
        this.index = s;
    }
    
    //return the sum for the node
    public int getSum(){
        return this.sum;
    }
    
    //set the sum for the node
    public void setSum(int s){
        this.sum = s;
    }

     //set stippled to true
    public void setStip(){
        this.stippled = true;
    }

     //return whether the node has been stippled
    public boolean getStip(){
        return this.stippled;
    }

    public boolean stippable(Node a, Node b, Node c, Node d){
        if(!a.getStip() && !b.getStip() && !c.getStip() && !d.getStip()){
            return true;
        }
        else return false;
    }


    public double distance(Node b){
        int x = this.getX() - b.getX();
        int y = this.getY()- b.getY();
        double z = Math.sqrt((x*x)+ (y*y));
        return z;
    }

    //returns the count for the node
    public int getCount(){
        return this.count;
    }

    //adds to the count of the node
    public void addCount(){
        this.count = this.count +1;
    }
}
