//get the value of a pixel in grayscale image
import java.awt.color.ColorSpace;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;


public class Dot{
public static void main(String[] args){

//obtain image in the file
BufferedImage img = null;
try{ img = ImageIO.read(new File("drizzy4.jpg"));}
catch(IOException e){
}



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
int ind = nodes.size();
n.setIndex(ind);
nodes.add(n);

j = j+ (int)(w*.004); nodesW++;} i = i + (int)(w*.004); nodesH ++;}



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
//stip holds the nodes and their coordinates all selected to be stipples

/*for(int i =0; i< stip.size(); i++){
    System.out.println("("+ stip.get(i).getX() + ", " + stip.get(i).getY() + ")");
}*/


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
        
        for(int i =0; i< stip.size(); i++){
            //g.drawOval(stip.get(i).getX(), stip.get(i).getY(),1 , 1);
            Color black = new Color(0,0,0);
            g.setColor(black);
            g.fillOval(stip.get(i).getX(), stip.get(i).getY(),2 , 2);
        }
        
        
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

}




