//Edge.java



public class Edge {

    // attributes
    Node start;      // 
    Node end;      // represents the y-coordinate of the dot


    // specific constructor
    public Edge( Node s, Node e){
    	this.start = s;
    	this.end = e;

    }


    // get the x-coordinate of the start node *
    public int getStartX(){
    
   		int x = this.start.getX();
        return x;
    }

    // get the y-coordinate of the start node *
    public int getStartY(){
    
   		int y = this.start.getY();
        return y;
    }

    // get the x-coordinate of the end node *
    public int getEndX(){
    
   		int x = this.end.getX();
        return x;
    }

 	// get the y-coordinate of the end node *
    public int getEndY(){
    
   		int y = this.end.getY();
        return y;
    }

    // get the index of the end node *
    public int getEndIndex(){
    
   		int y = this.end.getIndex();
        return y;
    }

    // get the index of the start node *
    public int getStartIndex(){
    
   		int y = this.start.getIndex();
        return y;
    }
  

}
