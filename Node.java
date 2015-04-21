

public class Node {

    // attributes
    int xValue;      // represents the x-coordinate of the dot
    int yValue;      // represents the y-coordinate of the dot
    int weight;     // represents the color of the dot
    boolean v;      //true if the node has been visited
    int sum;        //sum of the distance to that node
    int index;      //the index of the node in the node list


    // specific constructor
    public Node( int x, int y, int w){
    
       this.xValue = x;
       this.yValue = y;
       this.weight = w;
       this.v = false;
       this.index = -1;
       this.sum = 600; //make sure this starts at a value greater than the threshold
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
}
