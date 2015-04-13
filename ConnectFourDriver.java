   import java.io.*;
   import java.util.*;
   import javax.swing.*;
   import java.awt.event.*;
   import java.awt.*;

    public class ConnectFourDriver 
   {
      public static Grid screen;
       public static class listen implements KeyListener 
      {
         
          public void keyTyped(KeyEvent e)
         {
            
         }
         
          public void keyPressed(KeyEvent e)
         {
            
         }
         
         //checks to see if any key is released
          public void keyReleased(KeyEvent e)
         {
         	hello
            int k=e.getKeyCode();
            if(!screen.hasWon(screen.getBoard()).equals("nobody"))  
            {
               System.out.println("Congratulations "+screen.hasWon(screen.getBoard())+"! You won!!");
               System.exit(1);
            }
            else
               if(screen.getNumBlackPieces()==0 && screen.getNumRedPieces()==0)
               {
                  System.out.println("Cat's game!");
                  System.exit(1);
               }
               else
               {
                  if(k==KeyEvent.VK_RIGHT)
                     screen.move("right");
                  else
                     if(k==KeyEvent.VK_LEFT)
                        screen.move("left");
                     else
                                    
                        if(k==KeyEvent.VK_SPACE)
                           screen.move("space");
                                      
               }
                                      
                 
         }
      }            
    
   
      
   
       public static void main(String[]args)
      {
		//instantiates the grid
         screen= new Grid();
          //gets the screen ready
         JFrame display = new JFrame("Connect Four Game");	//window title
         display.setSize(800, 800);					//Size of game window
         display.setLocation(100, 50);				//location of game window on the screen
         display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         display.setContentPane(screen);		
         display.setVisible(true);
         display.addKeyListener(new listen());		//Get input from the keyboard
      
      
      
      }
   }
