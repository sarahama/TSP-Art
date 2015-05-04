
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


public class Tsp4 {

   // ****** constants to be personalized for your picture
    public static final Color BACKGROUND_COLOR = new Color( 255, 255, 255 );

    // ****** convenience constant
    public static final int RGB = BufferedImage.TYPE_INT_RGB;

    // method main(): program starting point
    public static void main( String[] args ) {




    //obtain image in the file
    BufferedImage img = null;

    JFrame frame = new JFrame("InputDialog");
    String fileName = (String)JOptionPane.showInputDialog(frame, "Enter the image's filepath", "Picture Input",
      JOptionPane.PLAIN_MESSAGE);
    String inputPath = fileName;
    String outputPath = fileName.substring(0, fileName.length()-4)+"grayedout.jpg";


    img = convertImage(inputPath);
    img = grayOut(img);
    writeImage(img, outputPath, "jpg");

    /*try{ img = ImageIO.read(new File("clock.jpg"));}
    catch(IOException e){
    }*/




    //turn the image gray




    //get the image height and width
    int w = img.getWidth();
    int h = img.getHeight();

   //create a list to hold nodes
   ArrayList<Node> nodes = new ArrayList<Node>();

   //use these to determine the values int the adjacency matrix
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
         Node n = new Node(j, i, red);
          int ns = nodes.size();
          n.setIndex(ns);
          nodes.add(n);

         j = j+ (int)(w*.01); nodesW++;} i = i + (int)(w*.01); nodesH ++;}



  //[right][left][up][down]
  //[is+1] [is-1] [is-nodesW] [is+nodesW]

  int[][] adjac = new int[nodes.size()][4];

  //fill the matrix with each edge weight, the sum of the two nodes' weights
  for(int i = 0; i < nodesH; i++){
      int is = i*nodesW; //used to calculate the desired index in nodes
      for(int j = 0; j < nodesW; j++){
          
          //if there is a node to the right, set edge to the right
          if(j< nodesW-1){
          //we need to subtract the answer from 518 so that light colors have lower weights    
          int W1 = nodes.get(is+j).getWeight() + nodes.get(is+j+1).getWeight();
          W1 = 512-W1;
          //undirected
          adjac[is+j][0] = W1; //right edge
          adjac[is+j+1][1] = W1; //left edge
          }
          //if there is an edge below, set edge below
          if(i < nodesH-1){
          //we need to subtract the answer from 518 so that light colors have lower weights    
          int W2 = nodes.get(is+j).getWeight() + nodes.get(is +j + nodesW).getWeight();
          W2 = 518-W2;
          //undirected
          adjac[is+j+nodesW][2] = W2; //above
          adjac[is+j][3] = W2; //below
          
          }
      }
  }


  ArrayList<Node> stip  = buildStipples(adjac, nodesW, nodes);
  //stip holds the nodes and coordinates of all selected stipples
  //we will use this list "stip" to build the MST
  //use the indices in stip
    for(int i = 0; i <stip.size(); i++){
      stip.get(i).setIndex(i);
    }





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
          


          //prints the dots
          for(int i =0; i< stip.size(); i++){
              Color black = new Color(0,0,0);
              g.setColor(black);
              g.fillOval(stip.get(i).getX(), stip.get(i).getY(),2 , 2);
          }
     



        //build a path, order to visit the nodes in
        ArrayList<Integer> path = buildEdges(w, h, stip);
    

        //go through the edges in the list and print them to the window

        Color black = new Color(0,0,0);
        g.setColor(black);
        Graphics2D g2 = (Graphics2D) g;  

        ArrayList<Line2D> lines = new ArrayList<Line2D>();      


/*        for(int i = 0; i < path.size(); i++){
           System.out.println(path.get(i));
        }*/
        System.out.println("nodes : " + nodes.size() + " " + "stips: " + stip.size());

        //using the path, create all of the lines and add them to the list lines
        for(int i =0; i< path.size()-1; i++){
            //draw a line using the coordinates of the start and end node
            int ind = path.get(i);
            int ind2 = path.get(i+1);
            Line2D line = new Line2D.Double((double)stip.get(ind).getX(), (double)stip.get(ind).getY(), 
              (double)stip.get(ind2).getX(), (double)stip.get(ind2).getY());
            lines.add(line);
        }





        int last = path.size()-1;
        Line2D line = new Line2D.Double((double)stip.get(path.get(0)).getX(), (double)stip.get(path.get(0)).getY(), 
              (double)stip.get(path.get(last)).getX(), (double)stip.get(path.get(last)).getY());
            lines.add(line);
 
      
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
              
             



        //****** explicit refresh to make sure we can see the result
        pane.repaint();

    }





    //********** Prims *******************
     public static ArrayList<Integer> buildEdges(int width, int height, ArrayList<Node> nodes){
        //start a priority queue, add an arbitrary node to the queue
        //find the shortest distance to an unvisited node from any node in the queue
        //whichever is the shortest, add that one to the queue
        //create the corresponding edge and add it to the list of edges
        //repeat until all of the nodes have been added

        //make a copy
        ArrayList<Node> unvisited = new ArrayList<Node>(nodes);
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


      
        //build the path
        ArrayList<Integer> path = new ArrayList<Integer>();
        findPath2(edges, path, 0);
        //path now holds the order of nodes to be visited
        System.out.println("path done");
        
        return path;
        }//end of build edges




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






public static void findPath2(ArrayList<Edge> edges, ArrayList<Integer> path, int ind){
          
          //push the index
          path.add(ind);
          for(int i = 0; i < edges.size(); i++){
            //if our current node is the start node and the end node is not already in the path
            //make the recursive call
            if(edges.get(i).getStartIndex() == ind && !path.contains(edges.get(i).getEndIndex())){
                findPath2(edges, path, edges.get(i).getEndIndex());
            }
          }


        }



public static BufferedImage convertImage(String filename){

  BufferedImage read = null;
  try{
    read = ImageIO.read(new File(filename));
  } catch (IOException e) {
    //TODO Auto-generated catch block
    e.printStackTrace();
  }
  return read;
}



public static void writeImage(BufferedImage img, String filename, String extension) {
  BufferedImage x = img;
  File OutputFile = new File(filename);
  try{
    ImageIO.write(x, extension, OutputFile);
  }
  catch(IOException e) {
    //TODO Auto-generated catch block
    e.printStackTrace();
  }
}



public static BufferedImage grayOut(BufferedImage img) {
  ColorConvertOp colorConvert = new ColorConvertOp(
        ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
  colorConvert.filter(img, img);

  return img;      
}



}
