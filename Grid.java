   import java.io.*;
   import java.util.*;
   import javax.swing.*;
   import java.awt.*;
	
    public class Grid extends JPanel
   {
   
      private static final ImageIcon plainBlock=new ImageIcon("blank.jpg");
      private static final ImageIcon red=new ImageIcon("red.jpg");
      private static final ImageIcon black=new ImageIcon("black.jpg");
      public static final int MAX_ROWS=6;
      public static final int MAX_COLS=7;
      private ImageIcon[][] board;
      private static int PlayerR;	
      private static int PlayerC;
      private static int prevCol;
      private static int prevRow;
      private static String Color;
      private static boolean isRedTurn;
      private static final int CELL_SIZE=100;
      private int numBlackPieces=21;
      private int numRedPieces=21;
    
     //Grid Constructor
       public Grid()
      {
         board=new ImageIcon[MAX_ROWS][MAX_COLS];
         for(int r=0;r<board.length;r++)
         {
            for(int c=0;c<board[0].length;c++)
            {
               board[r][c]=plainBlock;
            
            }
         
         }
        
         board[0][0]=red;
         Color="red";
         isRedTurn=true;
      }
   	//accessor method-returns board
       public ImageIcon[][] getBoard()
      {
         return board;
      }
   	//accessor method-returns number of red pieces left
       public int getNumRedPieces()
      {
         return numRedPieces;
      }
   //accessor method- returns number of black pieces left
       public int getNumBlackPieces()
      {
         return numBlackPieces;
      }
   	//determines what to do for each key required
       public void move(String key)
      {
         if(key.equals("left")&& PlayerC>0)
         {
            
            if(isRedTurn)
            {
               prevCol=PlayerC;
               PlayerC--;  
               board[0][prevCol]=plainBlock;
               board[0][PlayerC]=red;
            	
            }
            if(!isRedTurn)
            {
               prevCol=PlayerC;
               PlayerC--;  
               board[0][prevCol]=plainBlock;
               board[0][PlayerC]=black;
            	
            }
         }
         if(key.equals("right")&& PlayerC<board[0].length-1)
         {
            if(isRedTurn)
            {
               prevCol=PlayerC;
               PlayerC++;
               board[0][prevCol]=plainBlock;
               board[0][PlayerC]=red;
            
            }
            if(!isRedTurn)
            {
               prevCol=PlayerC;
               PlayerC++;
               board[0][prevCol]=plainBlock;
               board[0][PlayerC]=black;
            
            }
         
         }
         if(key.equals("space"))
         {
            placePiece();
            if(isRedTurn)
               numRedPieces--;
            else 
               numBlackPieces--;
            isRedTurn=!isRedTurn;
             
                    
         }
         repaint();
         	
      
      }
   	//determines if a player has won
       public  String hasWon(ImageIcon[][]board)
      {
         
         //checks for four in a row across a row
         for(int r=0;r<board.length;r++)
         {
            for(int i=0;i<4;i++)
            {
               if((board[r][i].equals(red) )&& board[r][i+1].equals(red) && board[r][i+2].equals(red) && board[r][i+3].equals(red))
               {                 
                  return "Red";
               }
               if(board[r][i].equals(black) && board[r][i+1].equals(black) && board[r][i+2].equals(black) && board[r][i+3].equals(black))
               {
                  return "Black"; 
               }
            
            }
         }
            //checks up and down
         for(int c=0;c<board.length;c++)
         {
            for(int i=0;i<3;i++)
            {
               if(board[i][c].equals(red) && board[i+1][c].equals(red) && board[i+2][c].equals(red) && board[i+3][c].equals(red))
               {
                  return "Red"; 
               }
               if(board[i][c].equals(black) && board[i+1][c].equals(black) && board[i+2][c].equals(black) && board[i+3][c].equals(black))
               {
                  return "Black"; 
               }
               
            }
                  
         }
          
         for(int r=0;r<2;r++)
         {
            for(int c=0;c<1;c++)
            {
               if(board[r][c].equals(red) && board[r+1][c+1].equals(red) && board[r+2][c+2].equals(red) && board[r+3][c+3].equals(red))
               {
                  return "Red"; 
               }
               if(board[r][c].equals(black) && board[r+1][c+1].equals(black) && board[r+2][c+2].equals(black) && board[r+3][c+3].equals(black))
               {
                  return "Black"; 
               }
               
               
            }	
         }
      
         return "nobody";
      }
      //Drops the piece down to the lowest possible place
       public void placePiece()
      {
         PlayerR=0; 
         while(PlayerR<board.length)
         {
            
            if(PlayerR+1<board.length)
               if(board[PlayerR+1][PlayerC]!=plainBlock)
                  break;
               
            PlayerR++;
                        
         }
         if(PlayerR==board.length)
            PlayerR--;
         if(isRedTurn)
            board[PlayerR][PlayerC]=red;
         else
            board[PlayerR][PlayerC]=black;
         repaint();
            
              
          
      }
   	//shows the board
       public void showBoard(Graphics g)	
      {
         int y =MAX_ROWS/2 + 50, x=MAX_COLS/2+50;
         for(int r=0;r<board.length;r++)
         {
            x=MAX_COLS/2+50;
            for(int c=0;c<board[0].length;c++)
            {
               g.drawImage(board[r][c].getImage(), x, y, CELL_SIZE, CELL_SIZE, null);  
                          
               x+=CELL_SIZE;
            }
            y+=CELL_SIZE;
         }
         
      }
   
   	//paints the background screen
       public void paintComponent(Graphics g)
      {
         super.paintComponent(g); 
         
         g.fillRect(50, 50,(MAX_COLS*CELL_SIZE)+MAX_COLS ,(MAX_ROWS*CELL_SIZE)+MAX_ROWS+50);
         showBoard(g);					
      }
        
   
   
   }
