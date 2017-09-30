/* Name: Vylana Trang
 * Login: cs8sbaya
 * Date: May 23 2016
 * File: Gui2048
 * Sources of Help: Textbook, Java Doc
 */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

/* Class Name: Gui2048
 * Purpose: To create a GUI to play 2048 on.
 */
public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    private static final int TILE_WIDTH = 106;

    private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
    private static final int TEXT_SIZE_MID = 45; // Mid value tiles 
                                                 //(128, 256, 512)
    private static final int TEXT_SIZE_HIGH = 35; // High value tiles 
                                                  //(1024, 2048, Higher)

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
    private static final Color COLOR_2 = Color.rgb(238, 228, 218);
    private static final Color COLOR_4 = Color.rgb(237, 224, 200);
    private static final Color COLOR_8 = Color.rgb(242, 177, 121);
    private static final Color COLOR_16 = Color.rgb(245, 149, 99);
    private static final Color COLOR_32 = Color.rgb(246, 124, 95);
    private static final Color COLOR_64 = Color.rgb(246, 94, 59);
    private static final Color COLOR_128 = Color.rgb(237, 207, 114);
    private static final Color COLOR_256 = Color.rgb(237, 204, 97);
    private static final Color COLOR_512 = Color.rgb(237, 200, 80);
    private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); 
                        // For tiles >= 8

    private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); 
                       // For tiles < 8

    private GridPane pane;
   
    /** My Instance Variables*/
    private Rectangle[][] recArr;
    private Text title;
    private Text score;
    private Text[][] textArr;
    private StackPane root;
   /* Name: start
    * Purpose: To start the GUI with 2 random tiles
    * Parameters: Stage primaryStage - the stage to set up the GUI 
    * Return: None
    */
    @Override
    public void start(Stage primaryStage)
    {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));

        // Create the pane that will hold all of the visual objects
        root = new StackPane();
        pane = new GridPane();
        root.getChildren().addAll(pane);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
        
        // Set the spacing between the Tiles
        pane.setHgap(15); 
        pane.setVgap(15);
        

        //Begins the GUI
        Scene scene = new Scene(root);
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());
        primaryStage.setTitle("Gui2048");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //Creating the title
        int gridSize =  board.GRID_SIZE;
        title = new Text();
        title.setText("2048");
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 
                        FontPosture.ITALIC, 30));
        title.setFill(Color.BLACK);
        pane.add(title, 0,0,2,1);
        GridPane.setHalignment(title,HPos.CENTER);
        
        //Creating the score
        score = new Text();
        score.setText("Score: "+ board.getScore());
        score.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        pane.add(score, gridSize - 2, 0, 2,1);
        GridPane.setHalignment(score, HPos.CENTER);
        
        //Gets the current grid
        int[][] origGrid = board.getGrid();
        int[][] grid = new int[gridSize][gridSize];
        for (int row = 0; row < gridSize; row++)
        {
           for (int col = 0; col < gridSize; col++)
           {
              grid[row][col] = origGrid[row][col];
           }
        }

        //Creates the board with the tiles and numbers
        recArr = new Rectangle[gridSize][gridSize];
        textArr = new Text[gridSize][gridSize];
        for (int row = 0; row < gridSize; row++)
        {
           for (int col = 0; col < gridSize; col++)
           {
              //Creates the tiles
              recArr[row][col] = new Rectangle();
              recArr[row][col].setWidth((int) scene.getWidth()/(gridSize));
              recArr[row][col].setHeight((int) 
                                    scene.getHeight()/gridSize);
              recArr[row][col].setFill(COLOR_EMPTY);
              
              //Creates the numbers to put on tiles
              int num = grid[row][col];
              textArr[row][col] = new Text();
              textArr[row][col].setText("" + num);
              textArr[row][col].setFont(Font.font("Times New Roman",
                                    FontWeight.BOLD, 20));
              GridPane.setHalignment(textArr[row][col], HPos.CENTER);
              textArr[row][col].setFill(COLOR_VALUE_DARK);
           
           }
        }

        //Adds the tile into the window
        for (int row = 0; row < gridSize; row++)
        {
           for (int col = 0; col < gridSize; col++)
           {
               pane.add(recArr[row][col], 0 + col, 2 + row);
               int num = grid[row][col];
               if ( num == 0) //if 0, then no tile is put
                  continue;
               else //otherwise adds a tile
               {
                  pane.add(textArr[row][col], 0 + col, 2 + row);
                  
                  if (num == 2) //sets the color of the tile based on number
                     recArr[row][col].setFill(COLOR_2);
                  else if (num == 4)
                     recArr[row][col].setFill(COLOR_4);
               }
           }
        }

        scene.setOnKeyPressed(new myKeyHandler());
     }

   /* Class Name: myKeyHandler
    * Purpose: To set up moving the board with the pressing of the keyboard
    * directions
    */
    private class myKeyHandler implements EventHandler<KeyEvent>
    {
    
    /* Name: handle
     * Purpose: To move the board according to what key is pressed
     * Parameters: KeyEvent event - the event that does whatever action to the
     * board
     * Return: None
     */
     @Override
     public void handle(KeyEvent event)
     {
         boolean checker = false;
         boolean end = false;
         switch(event.getCode())
         {
              //Saves the board
              case S:
                  try { //checks if can save the board
                     board.saveBoard(outputBoard);
                     System.out.println("Saving board to 2048.in");
                  } catch (IOException e) { //if not then prints out 
                                            //an error statement
                     System.out.println("saveBoard threw an Exception");
                  }
                  break;
              //Moves the board up
              case UP: 
                  checker = board.move(Direction.UP); //moves the board
                  if (checker == true) System.out.println("Moving UP"); 
                  checker = false;
                  if (this.resetGUI() == true)  //resets the board
                     return;
                  System.out.println(board);
                  break;
              //Moves the board down    
              case DOWN: 
                  checker = board.move(Direction.DOWN); //moves the board
                  if(checker == true) System.out.println("Moving DOWN"); 
                  checker = false;
                  if (this.resetGUI() == true) //resets the board
                     return;
                  System.out.println(board);
                  break;
              //Moves the board left
              case LEFT: 
                  checker = board.move(Direction.LEFT); //moves the board  
                  if (checker == true) System.out.println("Moving LEFT"); 
                  checker = false;
                  if (this.resetGUI() == true) //resets the board
                     return;
                  System.out.println(board);
                  break;
              //Moves the board right
              case RIGHT:
                  checker = board.move(Direction.RIGHT); //moves the board
                  if (checker == true) System.out.println("Moving RIGHT"); 
                  checker = false;
                  if (this.resetGUI() == true) //resets the board
                     return;
                  System.out.println(board);
                  break;
              //Rotates the board
              case R:
                  board.rotate(true); //rotates the board
                  System.out.println("Rotate Board");
                  checker = false;
                  if (this.resetGUI() == true) //resets the board
                     return;
                  System.out.println(board);
                  break;
              //Catches all the other key events    
              default: break;
         }
     }
   
     /* Name: resetGui()
      * Purpose: To reset the board into the GUI
      * Parameters:None
      * Return: None
      */
      private boolean resetGUI()
      {
         board.addRandomTile(); //adds random tile
         int[][] grid = board.getGrid();  
         int gridSize = board.GRID_SIZE;
         
         //removes all the other things in the grid
         for (int row = 0; row < gridSize; row++)
         {
            for (int col = 0; col < gridSize; col++)
            {
               Rectangle r = recArr[row][col];
               Text t = textArr[row][col];
               pane.getChildren().remove(r);
               pane.getChildren().remove(t);
            }
         }
         pane.getChildren().remove(score);

         //Creates the new title 
         Text title = new Text();
         title.setText("2048");
         title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 
                        FontPosture.ITALIC, 30));
         title.setFill(Color.BLACK);
         pane.add(title, 0, 0, 2, 1);
         GridPane.setHalignment(title, HPos.CENTER);
       
         //Creates the new score
         score = new Text();
         score.setText("Score: " + board.getScore());
         score.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
         pane.add(score, gridSize - 2, 0, 2, 1);
         GridPane.setHalignment(score,HPos.CENTER);
         
         //Adds in the new tiles
         for (int row = 0; row < gridSize; row++)
         {
            for (int col = 0; col < gridSize; col++)
            {
              recArr[row][col] = new Rectangle(); //sets the rectangles details
              recArr[row][col].setWidth(60);
              recArr[row][col].setHeight(60);
              recArr[row][col].setFill(COLOR_EMPTY);
              
              pane.add(recArr[row][col], 0 + col, 2 + row); //adds to pane
              int num = grid[row][col];
              if ( num == 0) //if zero, then doesn't add a tile
                 continue;
              else //otherwise adds a tile
              {
                 textArr[row][col] = new Text();
                 textArr[row][col].setText("" + num);
                 textArr[row][col].setFont(Font.font("Times New Roman",
                                    FontWeight.BOLD, 20));
                 
                 //sets the color of the tiles
                 if (num <=4)
                    {textArr[row][col].setFill(COLOR_VALUE_DARK);}
                 else 
                     {textArr[row][col].setFill(COLOR_VALUE_LIGHT); }
                 
                 pane.add(textArr[row][col], 0 + col, 2 + row);
                 GridPane.setHalignment(textArr[row][col], HPos.CENTER);
                 Color color = this.getRectColor(num);
                 recArr[row][col].setFill(color);
              }
            }
         }

         //checks if there's no more moves to be made, then makes game over
         if (board.isGameOver() == true)
         {  //creates the screen for the Game Over
            Rectangle rect = new Rectangle();
            rect.widthProperty().bind(pane.widthProperty());
            rect.heightProperty().bind(pane.heightProperty());
            rect.setX(0);
            rect.setY(0);
            rect.setFill(COLOR_GAME_OVER);
            rect.setOpacity(.25);
            root.getChildren().add(rect);
            
            //Creates the text that will say "Game Over"
            Text gameOver = new Text();
            gameOver.setText("Game Over");
            gameOver.setFont(Font.font("Times New Roman", FontWeight.BOLD,
                                75));
            gameOver.setFill(COLOR_VALUE_DARK);
            root.getChildren().add(gameOver);
            root.setAlignment(gameOver, Pos.CENTER);
            return true;
         }
         else
            return false;

      }

     /* Name: getRectColor
      * Purpose: To check what color tile the number needs and returns it
      * Parameters: int num - the number of the tile
      * Return: color of that number tile
      */
      private Color getRectColor(int num)
      {
         if (num == 2)
            return COLOR_2;
         else if (num == 4)
            return COLOR_4;
         else if (num == 8)
            return COLOR_8;
         else if (num == 16)     
            return COLOR_16;
         else if (num == 32)
            return COLOR_32;
         else if (num == 64)
            return COLOR_64;
         else if (num == 128)
            return COLOR_128;
         else if (num == 256)
            return COLOR_256;
         else if (num == 512)
            return COLOR_512;
         else if (num == 1024)
            return COLOR_1024;
         else if (num == 2048)
            return COLOR_2048;
         else
            return COLOR_OTHER;
      }
    }
                             
    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments 
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument 
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(inputBoard, new Random());
            else
                board = new Board(boardSize, new Random());
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + 
                               " was thrown while creating a " +
                               "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                               "Constructor is broken or the file isn't " +
                               "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message 
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the "+ 
                           "form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that " + 
                           "should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be " + 
                           "used to save the 2048 board");
        System.out.println("                If none specified then the " + 
                           "default \"2048.board\" file will be used");  
        System.out.println("  -s [size]  -> Specifies the size of the 2048" + 
                           "board if an input file hasn't been"); 
        System.out.println("                specified.  If both -s and -i" + 
                           "are used, then the size of the board"); 
        System.out.println("                will be determined by the input" +
                           " file. The default size is 4.");
    }
}
