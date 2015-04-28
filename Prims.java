
// MST with prims

// purpose: create an MST and print it to a java window

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.lang.Math;
import java.awt.color.ColorSpace;
import java.util.*;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.Line2D;


public class Prims {

    // ****** constants to be personalized for your picture
    public static final int PANE_WIDTH  = 475;
    public static final int PANE_HEIGHT = 500;
    public static final Color BACKGROUND_COLOR = new Color( 255, 255, 255 );

    // ****** convenience constant
    public static final int RGB = BufferedImage.TYPE_INT_RGB;

    // method main(): program starting point
    public static void main( String[] args ) {
        // ******  set up window
        JFrame window = new JFrame( "MST" );

        window.setAlwaysOnTop( true );
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        // ****** set up the surface for painting the image
        BufferedImage surface = new BufferedImage(PANE_WIDTH, PANE_HEIGHT, RGB);

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
        g.setColor( BACKGROUND_COLOR );
        g.fillRect( 0, 0, PANE_WIDTH, PANE_HEIGHT );

        // ******  your drawing commands here  *******

        //make a list of random nodes to build the MST from
        ArrayList<Node> nodes = new ArrayList<Node>();
        ArrayList<Node> unvisited = new ArrayList<Node>();
        for(int i =0; i< 400; i++){
          Random dice = new Random();
          int x = dice.nextInt(475);
          int y = dice.nextInt(500);
          Node n = new Node(x,y,0);
          n.setIndex(nodes.size());
          nodes.add(n);
          unvisited.add(n);
        }



        ArrayList<Integer> path = buildEdges(475, 500, nodes, unvisited);

    

        //go through the edges in the list and print them to the window

        Color black = new Color(0,0,0);
        g.setColor(black);
        Graphics2D g2 = (Graphics2D) g;  

        ArrayList<Line2D> lines = new ArrayList<Line2D>();      

        for(int i =0; i< path.size()-1; i++){
            //draw a line using the coordinates of the start and end node
            int ind = path.get(i);
            int ind2 = path.get(i+1);
            /*g2.draw(new Line2D.Double((double)nodes.get(ind).getX(), (double)nodes.get(ind).getY(), 
              (double)nodes.get(ind2).getX(), (double)nodes.get(ind2).getY()));*/
            Line2D line = new Line2D.Double((double)nodes.get(ind).getX(), (double)nodes.get(ind).getY(), 
              (double)nodes.get(ind2).getX(), (double)nodes.get(ind2).getY());
            lines.add(line);
            //g2.draw(line);

        }

        int last = path.size()-1;
        Line2D line = new Line2D.Double((double)nodes.get(path.get(0)).getX(), (double)nodes.get(path.get(0)).getY(), 
              (double)nodes.get(path.get(last)).getX(), (double)nodes.get(path.get(last)).getY());
            lines.add(line);
            //g2.draw(line);
      
      boolean part = allEdges(lines);
      System.out.println(part);
      
      //to test if a swap is valid, run depth first search, if you can return to the start node
      //and hit all other nodes, then the swap is valid, otherwise you've cut off a section
      //all lines are in the list lines, go through and check if any have intersections 
while(hasIntersections(lines)){
      for(int i = 0; i < lines.size(); i++){
        for(int j = 0; j < lines.size(); j++){
          //if there's an intersection, swap nodes
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

          //if the lines don't share an endpoint and they intersect, swap
          if( !a && !b && !c && !d && lines.get(i).intersectsLine(lines.get(j))){
            //swap1
            lines.get(i).setLine(iX1, iY1, jX2, jY2);
            lines.get(j).setLine(jX1, jY1, iX2, iY2);

            //if all the edges are not connected
            //do the other swap
            if(!allEdges(lines)){
            //swap2
            lines.get(i).setLine(iX1, iY1, jX1, jY1);
            lines.get(j).setLine(iX2, iY2, jX2, jY2);
            } 
            }
      }
      }
    }//end while loop

      System.out.println("has intersections: " + hasIntersections(lines));
      System.out.println("fully connected: " + allEdges(lines));
      
      //print the final graph
      for(int i = 0; i < lines.size(); i++){
        g2.draw(lines.get(i));
      }
              
             



        // ****** explicit refresh to make sure we can see the result
        //pane.repaint();

        // ******  capture drawing to file
        //Capturer.takeSnapshot( surface );
    }

    //********** Prims *******************
     public static ArrayList<Integer> buildEdges(int width, int height, ArrayList<Node> nodes, ArrayList<Node> unvisited){
        //start a priority queue, add an arbitrary node to the queue
        //find the shortest distance to an unvisited node from any node in the queue
        //whichever is the shortest, add that one to the queue
        //create the corresponding edge and add it to the list of edges
        //repeat until all of the nodes have been added
        ArrayList<Edge> edges = new ArrayList<Edge>();
        ArrayList<Node> queue = new ArrayList<Node>();

        //the max possible distance in this graph
        
        
        //push the root node onto the queue
        queue.add(nodes.get(0));
        unvisited.remove(0);
        //while there are unvisited nodes
        while(unvisited.size()>0){
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
        int[][] edg = new int[nodes.size()][2];
        //initialize all values to zero
        for(int i =0; i < nodes.size(); i++){
          edg[i][0] = -1;
          edg[i][1] = -1;
        }

        //go through all of the edges and add the indices to an adjacency matrix
        for(int i = 0; i < edges.size(); i++){
          int s = edges.get(i).getStartIndex();
          int e = edges.get(i).getEndIndex();
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
      
        //build the path
        ArrayList<Integer> path = new ArrayList<Integer>();
        findPath(edg, 0, path);
        //path now holds the order of nodes to be visited
        
        return path;
        }//end of build edges

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
