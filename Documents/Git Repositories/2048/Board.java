/* 
 * Name: Vylana Trang
 * Login: cs8sbaya
 * Date: April 26, 2016
 * File: Board.java
 * Sources of Help: Textbook
 */

import java.util.*;
import java.io.*;

/**
 * Name: Board.java
 * Purpose: To create a board for the 2048 game that gives us constructors to
 * create the boards, save the boards, add a random tile, roatate the board, 
 * check if the board can move, and move the board.
 */
public class Board {
    public final int NUM_START_TILES = 2;
    public final int TWO_PROBABILITY = 90;
    public final int GRID_SIZE;


    private final Random random;
    private int[][] grid;
    private int score;


    public Board(int boardSize, Random random) 
    {
        GRID_SIZE = boardSize;
        this.random = random;
        grid = new int[GRID_SIZE][GRID_SIZE];
        score = 0;
        
        // adds 2 random tiles to the board
        for (int count = 0; count < NUM_START_TILES; count++)
        {
           addRandomTile();
        }  
    }

    // Construct a board based off of an input file
    public Board(String inputBoard, Random random) throws IOException 
    {
            Scanner reader = new Scanner(new File(inputBoard)); 
            GRID_SIZE = reader.nextInt();
            this.random = random;
            score = reader.nextInt();
            grid = new int[GRID_SIZE][GRID_SIZE];
             
            //saves the grid from the inputted file into the grid instance field
            for (int row = 0; row < GRID_SIZE; row++)
            {
               for (int column = 0; column < GRID_SIZE; column++)
               {   
                     if(reader.hasNextInt())
                     {
                        int saver = reader.nextInt();
                        grid[row][column] = saver;
                     }   
               }
           
            }  
    }
    
    /*
     * Name: saveBoard
     * Purpose:To save the board into an inputted string.
     * Parameters:String outputBoard - the file name of the file to save the 
     *                                 board into
     * Return: None
     */
    public void saveBoard(String outputBoard) throws IOException 
    {
         //checks if string is null
         if (outputBoard == null)
            System.out.println("ERROR: String is null.");
         else //otherwise writes the board into a file
         {
            PrintWriter saver = new PrintWriter(outputBoard);
            saver.println(GRID_SIZE);
            saver.println(score);

            //writes the grid into the file
            for (int row = 0; row < GRID_SIZE; row++)
            {
               for (int column = 0; column < GRID_SIZE; column++)
               {
                  saver.print(grid[row][column]+ " ");
               }
               saver.println();     
            }
            saver.close();
         }
    }
    /*
     * Name: addRandomTile
     * Purpose: To add a random tile to the board
     * Parameters: None 
     * Return: None
     */
    public void addRandomTile() 
    {
         int count = 0; 
         int otherCount = 0;
         int location = 0;
         int value = 0;
         
         for (int row = 0; row < grid.length; row++)
         {
            for (int column = 0; column < grid[row].length; column++)
            {
               if ( grid[row][column] == 0) 
               {
                  count++; //counts how many empty spots in grid
               }
            }
         }
         
         if (count == 0) //if count is 0 then don't add the tile
            return;
         
         location = random.nextInt(count);
         value = random.nextInt(100);
         
         //runs through the entire grid
         for (int row = 0; row < grid.length; row++)
         {
            for (int column = 0; column < grid[row].length; column++)
            {
               if ( grid[row][column] == 0) //finds where the grid is zero
               {                            
                  if (otherCount == location) //finds where the random generator
                     {                        //wants to place the tile
                       if (value < TWO_PROBABILITY) //most likely places 2
                           grid[row][column] = 2;
                       else
                          grid[row][column] = 4;
                     }
                  otherCount++;
               }
            }
         }
       
    }
    /*
     * Name: rotate
     * Purpose:To rotate the entire board either clockwise or counterclockwise
     * Parameters: boolean rotateClockwise - checks to see if the rotation
     *                                       should be clockwise and 
     *                                       counter-clockwise 
     * Return: None
     */

    // Rotates the board by 90 degrees clockwise or 90 degrees counter-clockwise.
    // If rotateClockwise == true, rotates the board clockwise , else rotates
    // the board counter-clockwise
    public void rotate(boolean rotateClockwise) 
    {
         int saver = 0;
         int[][] tempGrid = new int [GRID_SIZE][GRID_SIZE];
         if (rotateClockwise == true) //to rotate clockwise...
         {
            for (int row = 0; row < GRID_SIZE; row++)
            {
               for (int column = 0; column < GRID_SIZE; column++)
               {
                  //sets rotated grid into a temporary grid
                  tempGrid[row][column] = grid[GRID_SIZE - 1 - column][row];
               }
            }
            this.grid = tempGrid; //sets temporary grid to grid instance field 
         }
         else if (rotateClockwise == false) //to rotate counterclockwise...
         {
            for (int row = 0; row < GRID_SIZE; row++)
            {
               for (int column = 0; column < GRID_SIZE; column++)
               {
                  //sets rotated grid to temporary grid
                  tempGrid[row][column] = grid[column][GRID_SIZE - 1 - row];
               }
            }
            this.grid = tempGrid; //sets temporary grid to grid instance field
         }
    }

    //Complete this method ONLY if you want to attempt at getting the extra credit
    //Returns true if the file to be read is in the correct format, else return
    //false
    public static boolean isInputFileCorrectFormat(String inputFile) 
    {
        //The try and catch block are used to handle any exceptions
        //Do not worry about the details, just write all your conditions inside the
        //try block
        try {
            //write your code to check for all conditions and return true if it satisfies
            //all conditions else return false
            Scanner reader = new Scanner(new File("inputFile"));
            
            if (reader.hasNextInt())
            while (reader.hasNext() == true)
            {
               
               if (reader.hasNextInt() == false)
                  return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /*
     * Name: move
     * Purpose: To move the board by figuring which direction to move it and
     *          then calling the appropriate move method.
     * Parameters: Direction direction - is the direction in which the user
     *                                   wants to move the board.
     * Return: boolean checker - if false, then can't move the board, if true,
     *                           means that a move was made
     */
    public boolean move(Direction direction) 
    {
        boolean checker = false;
        
        if (direction == Direction.LEFT) //checks if left direction
           checker = moveLeft();
        if (direction == Direction.RIGHT) //checks if right direction
           checker = moveRight();
        if (direction == Direction.UP) //checks if up direction
           checker = moveUp();
        if (direction == Direction.DOWN) //checks if down direction
           checker = moveDown();
        
        return checker;
    }

    /*
     * Name: moveLeft()
     * Purpose: Moves the entire board left
     * Parameters:None
     * Return: true once the method is complete
     */

    public boolean moveLeft()
    {
         for (int row = 0; row < GRID_SIZE; row++)
         {
            ArrayList<Integer> tiles = new ArrayList<Integer>(GRID_SIZE);
            int zeros = 0;
            //adds all the values from that row into an array
            for (int column = 0; column < GRID_SIZE; column++)
            {
                  tiles.add(grid[row][column]);
            }

            //removes all the zeros from the array
            for (int counter = 0; counter < tiles.size(); counter++)
            {
               if (tiles.get(counter).equals(0))
               {
                  tiles.remove(counter);
                  zeros++;
                  counter--;
               }
            }

            //adds together tiles that are the same
            for (int counter = 0; counter < tiles.size() - 1; counter++)
            {
               if (tiles.get(counter).equals(tiles.get(counter + 1)))
               {
                  int value = tiles.get(counter) * 2;
                  tiles.set(counter, value);
                  tiles.set(counter + 1, 0);
                  score = score + value; //increases the score
               }
            }

            //removes the zeros once again that are created from adding up tiles
            //in previous for loop
            for (int counter = 0; counter < tiles.size(); counter++)
            {
               if (tiles.get(counter).equals(0))
               {
                  tiles.remove(counter);
                  zeros++;
                  counter--;
               }
            }

            //adds together all the zeros that were removed to return back to
            //original arraylist length
            for (int index = 0; index < zeros; index++)
            {
               tiles.add(0);
            }

            //puts the arraylist back into the grid
            for (int column = 0; column < GRID_SIZE; column++)
            {
               grid[row][column] = tiles.get(column);
            }
         }
      return true;
    }

    /*
     * Name: moveRight()
     * Purpose: Moves the entire board right
     * Parameters: None
     * Return: true when the method finishes
     */


    public boolean moveRight()
    {
        
         for (int row = 0; row < GRID_SIZE; row++)
         {
            ArrayList<Integer> tiles = new ArrayList<Integer>(GRID_SIZE);
            int zeros = 0;

            //adds all the values from that row into an array
            for (int column = 0; column < GRID_SIZE; column++)
            {
                  tiles.add(grid[row][column]);
            }

            //remoes all the tiles that are zero
            for (int counter = 0; counter < tiles.size(); counter++)
            {
               if (tiles.get(counter).equals(0))
               {
                  tiles.remove(counter);
                  zeros++;
                  counter--;
               }
            }

            //adds together tiles that are the same
            for (int counter = tiles.size() - 1; counter > 0; counter--)
            {
               if (tiles.get(counter).equals(tiles.get(counter - 1)))
               {
                  int value = tiles.get(counter)*2;
                  tiles.set(counter, value);
                  tiles.set(counter - 1, 0);
                  score = score + value; //increases the score
               }
            }

            //removes the zeros created from adding together the tiles in the
            //previous for loop
            for (int counter = 0; counter < tiles.size(); counter++)
            {
               if (tiles.get(counter).equals(0))
               {
                  tiles.remove(counter);
                  zeros++;
                  counter--;
               }
            }

            //add all the zeros that were removed to the front of the arraylist
            for (int index = 0; index < zeros; index++)
            {
               tiles.add(0,0);
            }

            //puts the arraylist back into the grid
            for (int column = 0; column < GRID_SIZE; column++)
            {
               grid[row][column] = tiles.get(column);
            }
         }
      return true;
    }

    /*
     * Name: moveUp
     * Purpose: Moves the entire board up
     * Parameters:None
     * Return: true when the method is complete
     */
    public boolean moveUp()
    {
         for (int column = 0; column < GRID_SIZE; column++)
        {
            ArrayList<Integer> tiles = new ArrayList<Integer>(GRID_SIZE);
            int zeros = 0;
       
            //adds all the values from that row into an array
            for (int row = 0; row < GRID_SIZE; row++)
            {
                  tiles.add(grid[row][column]);
            }

            //removes all the tiles that are zero
            for (int counter = 0; counter < tiles.size(); counter++)
            {
               if (tiles.get(counter).equals(0))
               {
                  tiles.remove(counter);
                  zeros++;
                  counter--;
               }
            }

            //adds together the tiles that are the same
            for (int counter = 0; counter < tiles.size() - 1; counter++)
            {
               if (tiles.get(counter).equals(tiles.get(counter + 1)))
               {
                  int value = tiles.get(counter) * 2;
                  tiles.set(counter, value);
                  tiles.set(counter + 1, 0);
                  score = score + value; //increases the score
               }
            }

            //removes the tiles that are zero created by the previous for loop
            for (int counter = 0; counter < tiles.size(); counter++)
            {
               if (tiles.get(counter).equals(0))
               {
                  tiles.remove(counter);
                  zeros++;
                  counter--;
               }
            }

            //add the tiles that were zero back to the arraylist
            for (int index = 0; index < zeros; index++)
            {
               tiles.add(0);
            }

            //add the arraylist back into the grid
            for (int row = 0; row < GRID_SIZE; row++)
            {
               grid[row][column] = tiles.get(row);
            }
         }
           
     return true;
    }

    /*
     * Name: moveDown
     * Purpose: Moves the entire board down.
     * Parameters:None
     * Return: true when the method is complete
     */
    public boolean moveDown() 
    {
         for (int column = 0; column < GRID_SIZE; column++)
         {
            ArrayList<Integer> tiles = new ArrayList<Integer>(GRID_SIZE);
            int zeros = 0;
       
            //adds all the values from that row into an array
            for (int row = 0; row < GRID_SIZE; row++)
            {
                  tiles.add(grid[row][column]);
            }

            //gets rid of the tiles that are zero
            for (int counter = 0; counter < tiles.size(); counter++)
            {
               if (tiles.get(counter).equals(0))
               {
                  tiles.remove(counter);
                  zeros++;
                  counter--;
               }
            }

            //adds together the tiles that are the same
            for (int counter = tiles.size() - 1; counter > 0; counter--)
            {
               if (tiles.get(counter).equals(tiles.get(counter - 1)))
               {
                  int value = tiles.get(counter) * 2;
                  tiles.set(counter, value);
                  tiles.set(counter - 1, 0);
                  score = score + value; //increases the score
               }
            }

            //removes the tiles that are zero caused by adding the tiles in the
            //previous for loop
            for (int counter = 0; counter < tiles.size(); counter++)
            {
               if (tiles.get(counter).equals(0))
               {
                  tiles.remove(counter);
                  zeros++;
                  counter--;
               }
            }

            //adds the tiles that were zero back into the beginning of the array
            //list
            for (int index = 0; index < zeros; index++)
            {
               tiles.add(0,0);
            }

            //puts the arraylist back into the grid
            for (int row = 0; row < GRID_SIZE; row++)
            {
               grid[row][column] = tiles.get(row);
            }
         }
           
     return true;
    }

    /*
     * Name: isGameOver
     * Purpose: To check if the game is over by seeing if you can't make any
     *          more moves
     * Parameters: None
     * Return: boolean checker - if true makes the game over, if false, that
     *                           means the game isn't over
     */
    public boolean isGameOver() 
    {
        boolean checker = false;
        
        //checks if there's any valid moves left
        if (canMoveRight() == false && canMoveLeft() == false 
               && canMoveUp() == false && canMoveDown() == false)
            checker = true;
        
        return checker;
    }

    /*
     * Name: canMove
     * Purpose:Checks if the direction that the user wants to move the board is
     *         possible
     * Parameters:Direction direction - the direction in which the user wants to
     *            move the board
     * Return: boolean checker - true if the board can be moved in the direction
     *                           and false if the board can't be moved
     */
    public boolean canMove(Direction direction) 
    {
        boolean checker = false;

        if (direction.equals(Direction.LEFT)) //checks if can move left
           checker = canMoveLeft();
        else if (direction.equals(Direction.RIGHT)) //checks if can move right
           checker = canMoveRight();
        else if (direction.equals(Direction.UP)) //checks if can move up
            checker = canMoveUp();
        else if (direction.equals(Direction.DOWN)) //checks if can move down
            checker = canMoveDown();
  
        return checker;
    }

    /*
     * Name: canMoveLeft
     * Purpose:To see if the board can be moved left
     * Parameters: None
     * Return: boolean checker - true if the board can be moved left, otherwise
     *                           false
     */
    public boolean canMoveLeft()
    {
       boolean checker = false;
       
       //goes through all the tiles
       for (int row = 0; row < GRID_SIZE; row++)
       {
          for (int column = 1; column < GRID_SIZE; column++)
          {
            int tileValue = grid[row][column];
            //checks if tiles are equal to each other or zero
            if (tileValue == grid[row][column - 1] && tileValue != 0)
               checker = true; 
            else if (grid[row][column - 1] == 0 && tileValue != 0)
               checker = true; 
          }
        }
        return checker;
    }

    /*
     * Name: canMoveRight
     * Purpose: To see if the board can be moved right
     * Parameters:None
     * Return: boolean checker - true if the board can be moved right, otherwise
     *                           false
     */
    public boolean canMoveRight()
    {
       boolean checker = false;
       for (int row = 0; row < GRID_SIZE; row++)
       {
          for (int column = 0; column < GRID_SIZE - 1; column++)
          {
            int tileValue = grid[row][column];
           
            //checks if the tiles are equal to each other or zero
            if (tileValue == grid[row][column + 1] && tileValue != 0)
               checker = true;
            else if (grid[row][column + 1] == 0 && tileValue != 0) 
               checker = true;
          }
       }
      return checker; 
    }

     /*
     * Name: canMoveUp
     * Purpose: To see if the board can be moved up
     * Parameters: None
     * Return: boolean checker - true if the board can be moved up, otherwise
     *                           false
     */
    public boolean canMoveUp()
    {
       boolean checker = false;
       for (int row = 1; row < GRID_SIZE; row++)
       {
          for (int column = 0; column < GRID_SIZE; column++)
          {
            int tileValue = grid[row][column];
            
            //checks if tiles are equal to each other or zero
            if (tileValue == grid[row - 1][column] && tileValue != 0)
               checker = true;
            else if (grid[row - 1][column] == 0 && tileValue != 0)
               checker = true;
          }
       }
       return checker;
    }

     /*
     * Name: canMoveDown
     * Purpose: To see if the board can be moved down
     * Parameters: None
     * Return: boolean checker - true if the board can be moved down, otherwise
     *                           false
     */
    public boolean canMoveDown()
    {
       boolean checker = false;
       for (int row = 0; row < GRID_SIZE - 1; row++)
       {
          for (int column = 0; column < GRID_SIZE; column++)
          {
            int tileValue = grid[row][column];
            
            //checks if the tiles are the same or zero
            if (tileValue == grid[row + 1][column] && tileValue != 0)
               checker = true;
            else if (grid[row + 1][column] == 0 && tileValue != 0)
               checker = true;
          }
       }
       return checker;
    }
    
   /*
    * Name: setGrid
    * Purpose: To set the grid as a different grid
    * Parameters: int[][] newGrid - the new grid that the user wants to set the
    * grid instance field as
    * Return: None
    */
    public void setGrid(int[][] newGrid)
    {
       grid = newGrid;
    }

    // Return the reference to the 2048 Grid
    public int[][] getGrid() 
    {
        return grid;
    }

    // Return the score
    public int getScore() 
    {
        return score;
    }

    @Override
    public String toString() 
    {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -" :
                        String.format("%5d", grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }
}
