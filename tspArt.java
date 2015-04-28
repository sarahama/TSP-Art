//get the value of a pixel in grayscale image
import java.awt.color.ColorSpace;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.lang.Math;
import java.awt.geom.Line2D;


public class TspArt {
public static void main(String[] args){

    //obtain image in the file
BufferedImage img = null;
try{ img = ImageIO.read(new File("clock.jpg"));}
catch(IOException e){
}



//get the image height and width
int w = img.getWidth();
int h = img.getHeight();



//Set up graphics section ******************

        JFrame window = new JFrame( "TSP Art" );

        window.setAlwaysOnTop( true );
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        // ****** set up the surface for painting the image
        BufferedImage surface = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        // ****** get the window use the surface as its content pane
        ImageIcon icon = new ImageIcon( surface );
        JLabel pane = new JLabel( icon );
        window.setContentPane( pane );

        // ****** size and display the window
        window.pack();
        window.setVisible( true );

        // ******  get a renderer to manipulate the surface's display
        Graphics g = surface.getGraphics();

        // paint the entire surface the background color
        Color white = new Color(255,255,255);
        g.setColor(white);
        g.fillRect( 0, 0, w, h );



        Color black = new Color(0,0,0);
        g.setColor(black);
        Graphics2D g2 = (Graphics2D) g;

//**********************************




//create a list to hold the "test nodes" laid on the image and their RGB value
ArrayList<Node> nod = new ArrayList<Node>();

//use these to determine the values in the adjacency matrix
int nodesH = 0;
int nodesW = 0;

//find the rgb value in a lattice structure over the image
for(int i = 0; i < h;){
    nodesW = 0;
    for(int j = 0; j < w;){

    int sRbgColor = img.getRGB(j, i);

    Color c = new Color(sRbgColor);
    int red = c.getRed(); //r,g,b are all equal in gray scale, we only need one

    //create a node at that coordinate, save its weight and its index
    //add it to the list of nodes
    Node n = new Node(j, i, red);

    n.setIndex(nod.size());
    nod.add(n);

    //this statement determines how we increment j and i
    //i.e. it determines the density of "test" nodes laid on the image
    j = j+ (int)(w*.05); nodesW++;} i = i + (int)(w*.05); nodesH ++;}

int nodSize = nod.size();


//adjac:  an adjacency matrix for each test node and its four adjacent nodes
//[right][left][up][down]
//[is+1] [is-1] [is-nodesW] [is+nodesW]
int[][] adjac = new int[nod.size()][4];


//fill the matrix with each edge weight, the sum of the two nodes' weights
for(int i = 0; i < nodesH; i++){
    int is = i*nodesW; //used to calculate the desired index in nodes
    for(int j = 0; j < nodesW; j++){
        
        //if there is a node to the right, set edge to the right
        if(j< nodesW-1){
        //we need to subtract the answer from 518 so that light colors have lower weights
        //and darker colors have higher values    
        int W1 = nod.get(is+j).getWeight() + nod.get(is+j+1).getWeight();
        W1 = 512-W1;
        //undirected
        adjac[is+j][0] = W1; //right edge
        adjac[is+j+1][1] = W1; //left edge
        }
        //if there is an edge below, set edge below
        if(i < nodesH-1){
        //we need to subtract the answer from 518 so that light colors have lower weights 
        //and darker colors have higher values     
        int W2 = nod.get(is+j).getWeight() + nod.get(is +j + nodesW).getWeight();
        W2 = 518-W2;
        //undirected
        adjac[is+j+nodesW][2] = W2; //above
        adjac[is+j][3] = W2; //below
        
        }
    }
}



ArrayList<Node> stip  = buildStipples(adjac, nodesW, nod);
//stip holds the nodes and coordinates of all selected stipples, the nodes we will continue to use


//make a copy of stip to hand to the buildEdges method
ArrayList<Node> unvisited = new ArrayList<Node>(stip);

System.out.println(stip.size() + " " + unvisited.size());


//buildEdges will return the Hamiltonian path for our list of stipples
ArrayList<Integer> path = buildEdges(w, h, stip, unvisited, nodSize);

System.out.println(path.size());

//a list to hold all of the graphic lines
ArrayList<Line2D> lines = new ArrayList<Line2D>();      

        for(int i =0; i< path.size()-1; i++){
            //draw a line using the coordinates of the start and end node
            int ind = path.get(i);
            int ind2 = path.get(i+1);
            Line2D line = new Line2D.Double((double)nod.get(ind).getX(), (double)nod.get(ind).getY(), 
              (double)nod.get(ind2).getX(), (double)nod.get(ind2).getY());
            lines.add(line);
            //g2.draw(line);

        }

        //draw the line connecting the first and last nodes in the path
        int last = path.size()-1;
        Line2D line = new Line2D.Double((double)nod.get(path.get(0)).getX(), (double)nod.get(path.get(0)).getY(), 
              (double)nod.get(path.get(last)).getX(), (double)nod.get(path.get(last)).getY());
            lines.add(line);
            //g2.draw(line);
      
        boolean part = allEdges(lines);
        System.out.println(part);



      //print the final graph
      for(int i = 0; i < lines.size(); i++){
        g2.draw(lines.get(i));
      }

        

       //********prints the stipples
        for(int i =0; i< stip.size(); i++){
            //g.drawOval(stip.get(i).getX(), stip.get(i).getY(),1 , 1);
            //Color black = new Color(0,0,0);
            //g.setColor(black);
            g.fillOval(stip.get(i).getX(), stip.get(i).getY(),2 , 2);
        }
        
        
}














//Methods available for use







//Dijkstra's *********************************************************************************************************


public static ArrayList<Node> buildStipples(int[][] adjac, int nodesW, ArrayList<Node> nodes){
    
    //select the stipples, holds the index of the node in list nodes
    ArrayList<Node> stipples = new ArrayList<Node>();

    //queue that holds a node's index in list nodes
    LinkedList<Node> q = new LinkedList<Node>();
    //adjacent nodes are i+1 (right), i-1 (left), i- nodesW (above) and i + nodesW(below)
    
    //while there are unvisited nodes in the nodes list
    for(int j = 0; j < nodes.size(); j ++){
            //get a random index in nodes as a new start node
            //generate a random start node
            Random dice = new Random();
            int i = dice.nextInt(nodes.size());
            for(int z = i; z < nodes.size(); z++){
                if(nodes.get(z).getV()){
                    break;
                }
            }
        //if a node is unvisited, select it as a new start node
        if(!nodes.get(i).getV()){
        
            //add the first node as a stipple on both the queue and stipples list
            stipples.add(nodes.get(i));
            q.addLast(nodes.get(i));
            
            //set sum at start node to 0
            nodes.get(i).setSum(0);
            //*********************************************************************************
            while(!q.isEmpty()){
            //get the sum to the node at the head of the queue
            int sum = q.getFirst().getSum();
            //get the index of the node at the head of the queue
            int in = q.removeFirst().getIndex(); //this also removes the head of the queue
            

            int thresh = 255*3;


            //an edge must be valid and the end node unvisited
            //if both are true, update the nodes sum if less than its current sum, and add it to the queue
            //right edge
            //if( n == 0){


            //above
            if(in < nodes.size()-nodesW && !nodes.get(in+nodesW).getV()){
                int sum3 = sum + adjac[in][2];
                //update shortest path if...
                if(sum3 < nodes.get(in+nodesW).getSum()){
                    nodes.get(in+nodesW).setSum(sum3);
                }
                //if the sum passes the threshold, add the end node to stipples, clear the queue
                if(sum3 > thresh){
                    int ix = in+nodesW;
                    //check incase of out of bounds
                    //then check if the nodes neighbors are already nodes
                    boolean contin = false;
                    if(ix+1 <nodes.size()-1 && ix > nodesW && ix+nodesW<nodes.size()){
                        contin = nodes.get(ix).stippable(nodes.get(ix+1), nodes.get(ix-1), nodes.get(ix+nodesW), nodes.get(ix-nodesW));
                    }
                    if(contin){
                    stipples.add(nodes.get(in+nodesW));
                    //mark the node as stippled
                    nodes.get(in+nodesW).setStip();
                    } 
                    q.clear();
                }
                //else add the node to the queue
                else{
                    q.addLast(nodes.get(in+nodesW));
                }
                //mark as visited
                nodes.get(in+nodesW).visited();
            }
            
            //below
            if(in > nodesW && !nodes.get(in-nodesW).getV()) {
                int sum4 = sum + adjac[in][3];
                //update shortest path if...
                if(sum4 < nodes.get(in-nodesW).getSum()){
                    nodes.get(in-nodesW).setSum(sum4);
                }
                //if the sum passes the threshold, add the end node to stipples, clear the queue
                if(sum4 > thresh){
                    int ix = in-nodesW;
                    //check incase of out of bounds
                    //then check if the nodes neighbors are already nodes
                    boolean contin1 = false;
                    if(ix+1 <nodes.size()-1 && ix > nodesW && ix+nodesW<nodes.size()){
                        contin1 = nodes.get(ix).stippable(nodes.get(ix+1), nodes.get(ix-1), nodes.get(ix+nodesW), nodes.get(ix-nodesW));
                    }
                    if(contin1){
                    stipples.add(nodes.get(in-nodesW));
                    //mark the node as stippled
                    nodes.get(in-nodesW).setStip(); 
                }
                    q.clear();
                }
                //else add the node to the queue
                else{
                    q.addLast(nodes.get(in-nodesW));
                }
                //mark as visited
                nodes.get(in-nodesW).visited();
            }
            //right
            if(in < nodesW-1 && !nodes.get(in+1).getV()){
                int sum1 = sum + adjac[in][0];
                //update shortest path if...
                if(sum1 < nodes.get(in+1).getSum()){
                    nodes.get(in+1).setSum(sum1);
                }
                //if the sum passes the threshold, add the end node to stipples, clear the queue
                if(sum1 > thresh){
                    int ix = in+1;
                    //check incase of out of bounds
                    //then check if the nodes neighbors are already nodes
                    boolean contin2 = false;
                    if(ix+1 <nodes.size()-1 && ix > nodesW && ix+nodesW<nodes.size()){
                        contin2 = nodes.get(ix).stippable(nodes.get(ix+1), nodes.get(ix-1), nodes.get(ix+nodesW), nodes.get(ix-nodesW));
                    }
                    if(contin2){
                    stipples.add(nodes.get(in+1));
                    //mark the node as stippled
                    nodes.get(in+1).setStip();
                }
                    q.clear();
                }
                //else add the node to the queue
                else{
                    q.addLast(nodes.get(in+1));
                }
                //mark as visited
                nodes.get(in+1).visited();
            }
            
            //left
            if(in > 0 && !nodes.get(in-1).getV()){
                int sum2 = sum + adjac[in][1];
                //update shortest path if...
                if(sum2 < nodes.get(in-1).getSum()){
                    nodes.get(in-1).setSum(sum2);
                }
                //if the sum passes the threshold, add the end node to stipples, clear the queue
                if(sum2 > thresh){
                    int ix = in-1;
                    //check incase of out of bounds
                    //then check if the nodes neighbors are already nodes
                    boolean contin3 = false;
                    if(ix+1 <nodes.size()-1 && ix > nodesW && ix+nodesW<nodes.size()){
                        contin3 = nodes.get(ix).stippable(nodes.get(ix+1), nodes.get(ix-1), nodes.get(ix+nodesW), nodes.get(ix-nodesW));
                    }
                    if(contin3){
                    stipples.add(nodes.get(in-1));
                    //mark the node as stippled
                    nodes.get(in-1).setStip(); 
                }
                    q.clear();
                }
                //else add the node to the queue
                else{
                    q.addLast(nodes.get(in-1));
                }
                //mark as visited
                nodes.get(in-1).visited();
            }

            }}//end while loop
    }//end for loop 
    //all of the nodes have been visited, the selected ones are now in the stipple list
    return stipples;
}







//********** Prims *******************
public static ArrayList<Integer> buildEdges(int width, int height, ArrayList<Node> nodes, ArrayList<Node> unvisited, int nodSize){
        //start a priority queue, add an arbitrary node to the queue
        //find the shortest distance to an unvisited node from any node in the queue
        //whichever is the shortest, add that one to the queue
        //create the corresponding edge and add it to the list of edges
        //repeat until all of the nodes have been added
        ArrayList<Edge> edges = new ArrayList<Edge>();
        ArrayList<Node> queue = new ArrayList<Node>();

        
        
        //push the root node onto the queue
        queue.add(nodes.get(0));
        unvisited.remove(0);
        //while there are unvisited nodes
        

        while(unvisited.size()>0){
            //the max possible distance in this graph
          double min = Math.sqrt(width*width + height*height); 
          int indexQ = 0;
          int indexU = 0;
          for(int i = 0; i < queue.size(); i++){
            //get the min distance to an unvisited node from the nodes in the queue
            if(queue.get(i).getCount() < 2){
              //if the node's count is less than 2
              for(int j = 0; j < unvisited.size(); j++){
                double z = queue.get(i).distance(unvisited.get(j));
                if (z < min){
                  min = z;
                  //save the indices of the min edge
                  indexQ = i;
                  indexU = j;
            }}}}//make an edge for the min distace and add it to edges, add the end node to the queue
            Edge e = new Edge(queue.get(indexQ), unvisited.get(indexU));
            
            
            edges.add(e);
            //add to the node counts
            queue.get(indexQ).addCount();
            unvisited.get(indexU).addCount();
            queue.add(unvisited.get(indexU));
            //remove the end node from unvisited
            unvisited.remove(indexU);
          }

        //return edges;
        //make an adjacency list for the tree assuming each node has only two edges
        int[][] edg = new int[nodSize][2];
        //initialize all values to -1
        for(int i =0; i < nodSize; i++){
          edg[i][0] = -1;
          edg[i][1] = -1;
        }

        //go through all of the edges and add the indices to an adjacency matrix
        for(int i = 0; i < edges.size(); i++){
          int s = edges.get(i).getStartIndex();
          int e = edges.get(i).getEndIndex();
          
          //System.out.println(s + " " + e);
          
          //fill in s's adjacency slots
          if(edg[s][0] == -1){
            edg[s][0] = e;
          }
          else if(edg[s][1] == -1){
            edg[s][1] = e;
          }
          //fill in e's adjacency slots
          if(edg[e][0] == -1){
            edg[e][0] = s;
          }

          else if(edg[e][1] == -1){
            edg[e][1] = s;
          }
        }//matrix filled

        //print the adjacency matrix
    for(int i = 0; i < nodSize; i++){
        System.out.println(edg[i][0] + ", " + edg[i][1]);
    }

      
        //build the path
        ArrayList<Integer> path = new ArrayList<Integer>();
        findPath(edg, 0, path);
        //path now holds the order of nodes to be visited
        
        return path;
        }//end of build edges






//fills an adjacency matrix based on the hamiltonian path in "path"
public static void findPath(int[][] edg, int d, ArrayList<Integer> path){
          //add the index p
            path.add(d);
            //if there is an adjacent node call find path on it 
            if(edg[d][0] != -1 && !path.contains(edg[d][0])){
              findPath(edg, edg[d][0], path);
            }

            if(edg[d][1] != -1 && !path.contains(edg[d][1])){
              findPath(edg, edg[d][1], path);
            }
            else{
              return;
            }
        }







//returns true if a loop stored in "lines" intersects itself
public static boolean hasIntersections(ArrayList<Line2D> lines){
      for (int i = 0; i < lines.size(); i++){
        for (int j = 0; j < lines.size(); j++){
            double iX1 = lines.get(i).getX1();
            double iY1 = lines.get(i).getY1();
            double iX2 = lines.get(i).getX2();
            double iY2 = lines.get(i).getY2();
            double jX1 = lines.get(j).getX1();
            double jY1 = lines.get(j).getY1();
            double jX2 = lines.get(j).getX2();
            double jY2 = lines.get(j).getY2();
            //checks if the lines share an endpoint
            boolean a = iX1 == jX1 && iY1 == jY1;
            boolean b = iX1 == jX2 && iY1 == jY2;
            boolean c = iX2 == jX1 && iY2 == jY1;
            boolean d = iX2 == jX2 && iY2 == jY2;
            if(!a && !b && !c && !d && lines.get(i).intersectsLine(lines.get(j))){
            return true;
          }
        }
      }
      return false;
    }









//returns true if all edges in the image are connected, i.e. if the image is a single loop
public static boolean allEdges(ArrayList<Line2D> lines){

  //get the size of the list
  int s = lines.size();
  //make a copy of lines
  ArrayList<Line2D> copLines = new ArrayList<Line2D>(lines);

  //get the coordinates for the line
  double x1 = copLines.get(0).getX1();
  double y1 = copLines.get(0).getY1();
  double x2 = copLines.get(0).getX2();
  double y2 = copLines.get(0).getY2();

  //remove the line from copLines
  copLines.remove(0);
  double iX1 = 0;
  double iY1 = 0;
  double iX2 = 0;
  double iY2 = 0;

  //find a line, still in copLines, with a matching endpoint
  while(!(copLines.isEmpty())){
    boolean match = false;
    for(int i = 0; i < copLines.size(); i++){
      iX1 = copLines.get(i).getX1();
      iY1 = copLines.get(i).getY1();
      iX2 = copLines.get(i).getX2();
      iY2 = copLines.get(i).getY2();
      if((iX1==x1 && iY1==y1) || (iX2==x1 && iY2==y1) || (iX1==x2 && iY1==y2) || (iX2==x2 && iY2==y2)){
        //remove the line from copLines
        copLines.remove(i);
        //set the x and y's to the new values
        x1 = iX1;
        y1 = iY1;
        x2 = iX2;
        y2 = iY2;
        //a match was found
        match = true;
        //break from the for loop
        break;
      }
  }//if there was not a match, break from the while loop, otherwise continue
  if (!match){
    break;
  }
}
//if the lines are empty, then all lines were connected and the loop is one piece, return true
return copLines.isEmpty();


}

}




