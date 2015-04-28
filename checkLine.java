
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


public class checkLine {

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



    

        //go through the edges in the list and print them to the window

        Color black = new Color(0,0,0);
        g.setColor(black);
        Graphics2D g2 = (Graphics2D) g;  

        Line2D line = new Line2D.Double(10, 10, 
              200, 200);
            

        Line2D line2 = new Line2D.Double(50, 10, 
              50, 200);
            

        if(line.intersectsLine(line2)){
            double iX1 = line.getX1();
            double iY1 = line.getY1();
            double iX2 = line.getX2();
            double iY2 = line.getY2();
            double jX1 = line2.getX1();
            double jY1 = line2.getY1();
            double jX2 = line2.getX2();
            double jY2 = line2.getY2();
            System.out.println(iX1 + " " + iY1 + " " + iX2 + " " + iY2 + " " + jX1 + " " + jY1 + " " +jX2 + " " + jY2);
            //swap
            line.setLine(iX1, iY1, jX1, jY1);
            line2.setLine(iX2, iY2, jX2, jY2);
            System.out.println(line.getX1()+ " " + line.getY1() + " " + line.getX2() + " " + line.getY2() + " " + line2.getX1() + " " + line2.getY1() + " " +line2.getX2() + " " + line2.getY2());
        }
      g2.draw(line2);
      g2.draw(line);
      System.out.println(line.intersectsLine(line2));
        }
}
