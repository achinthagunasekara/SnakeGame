/*
 * 2013
 * Author : Archie Gunasekara
 */

package userInterface;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import javax.swing.JInternalFrame;

import messages.MessageManager;
import messages.MessageTypes;
import modules.CurrentFoodItem;
import modules.Direction;
import modules.GameData;
import modules.Snake;


public class GameGrid extends  JInternalFrame {

	private int rowNum;
	private int colNum;
	private HashMap<String, Square> map;
	private Timer timer;
	private GameData gameData;
	
	public GameGrid() {

		super("Game Grid", false, false, false, false);
		
		this.gameData = GameData.getGameDataInstance();
		this.rowNum = gameData.getNumRows();
		this.colNum = gameData.getNumCols();
		
	    setLocation(10, 10);
	    setSize(680, 680);
	    setBackground(Color.GRAY);
		setLayout(new GridLayout(rowNum, colNum));
		addKeyListener(new GameKeyListener());
		
		map = new HashMap<String, Square>();

		for(int i = 1; i <= rowNum; i++) {
		
			for(int j = 1; j <= colNum; j++) {

				Square s = new Square(i, j);
				s.addKeyListener(new GameKeyListener());
				map.put(s.getName(), s);
				add(s);
			}
		}
		
		ActionListener listener = new ActionListener(){

			public void actionPerformed(ActionEvent event){

				refreshScreen();   
			}	  
		};
		
		timer = new Timer(100, listener);
	    setVisible(true);
	}
	
	public void startGame() {
		
		timer.start();
	}
	
	private void refreshScreen() {
		
		for(Square s : map.values()){
			
			s.setColour(Color.gray);
			s.resetHead();
		}
	
		for(Snake currentSnake : gameData.getSnakes().values()) {
			
			drawSnakeAndMoveMySnake(currentSnake);
			
			//player lost
			if(currentSnake.isMySnake() && currentSnake.isSnakeLost()) {
				
				String score = sendSnakeLostMessageToServer();
				JOptionPane.showMessageDialog(this, "You Lost!\nYour score is " + score);
				System.exit(0);
			}
		}
		
		for(Snake currentSnake : gameData.getSnakes().values()) {
			
			if(currentSnake.isMySnake()) {
				
				sendMySnakeLocToServer(currentSnake);
			}
		}
	}
	
	private void drawSnakeAndMoveMySnake(Snake snake) {
		
		System.out.println("Drawing Snake : " + snake.getClientId());
		
		Color c;
		
		if(snake.isMySnake()) {
			
			c = Color.green;
			snake.moveOneSpot(this);
		}
		else {
			
			c = Color.red;
		}

		map.get(snake.getHead()).setColour(c);
		map.get(snake.getHead()).drawHead();
		
		for(int j = 0; j < snake.getOtherSquares().size(); j++) {
			
			map.get(snake.getOtherSquares().get(j)).setColour(c);
		}
	}
	
	private void sendMySnakeLocToServer(Snake snake) {
		
		String s = MessageTypes.clientMessageTypes.SENDING_SNAKE_LOC.toString();
		s += "#" + gameData.getMyID() + "#" + snake.getHead() + "#";
		
		for(int i = 0; i < snake.getOtherSquares().size(); i++) {
			
			s += snake.getOtherSquares().get(i) + "T";
		}
		
		s += "#" + snake.getDirection() + "#" + snake.getSpeed() + "#" + snake.getLastMoveTime();
	
		try {
			
			sendFoodItemStatsToServer();
			String serverSends = MessageManager.getMessageManager().writeToServer(s);
			processServerSendData(serverSends);
		}
		catch(IOException ioEx) {
			
			System.out.println("Error sending snake location to server : " + ioEx.toString());
		}
	}
	
	private void sendFoodItemStatsToServer() {
		
		if(CurrentFoodItem.isEaten()) {
			
			try {
				
				MessageManager.getMessageManager().writeToServer(MessageTypes.clientMessageTypes.SNAKE_ATE_FOOD.toString());
			}
			catch(IOException ioEx) {
				
				System.out.println("Error - did not send food item status to the server : " + ioEx.toString());
			}
		}
	}
	
	private String sendSnakeLostMessageToServer() {
		
		String score = null;
		
		try {
			String lostMessage = MessageTypes.clientMessageTypes.SNAKE_LOST.toString() + "#" + gameData.getMyID();
			score = MessageManager.getMessageManager().writeToServer(lostMessage);
		}
		catch(IOException ioEx) {
			
			System.out.println("Error - did not send snake lost message to the server : " + ioEx.toString());
			score = "Unknown - reading message from the server failed!";
		}
		
		return score;
	}
	
	private void sendSnakeWonMessageToServer() {
		
		try {
			String wonMessage = MessageTypes.clientMessageTypes.SNAKE_WON.toString() + "#" + gameData.getMyID();
			String[] resArray = MessageManager.getMessageManager().writeToServer(wonMessage).split(",");
			String res = resArray[0];
			
			if(MessageTypes.serverMessageTypes.valueOf(res) == MessageTypes.serverMessageTypes.OK_WON) {
				
				JOptionPane.showMessageDialog(this, "You Won!\n Your Score is "+resArray[1]);
				System.exit(0);
			}
		}
		catch(IOException ioEx) {
			
			System.out.println("Error - did not send snake lost message to the server : " + ioEx.toString());
		}
	}
	
	/*
	 * Sample data format sent from the server is - 1,1#1#1,1#1,10T#EAST!#1#1,2#1,10T,1,11T#EAST
	 * First part of the string contains the location of the food item
	 * Each snake data is separated by a "!" character
	 * Each information about a snake is separated by a "#" character
	 * Information for snake is "ID#HEAD#BODY_DATA#DIRECTION
	 */
	private void processServerSendData(String serverSends) {
		
		try {
			
			gameData.resetData();
			
			String[] data = serverSends.split("!");
			
			//i = 0 contains the location of the food item			
			for(int i = 1; i < data.length; i++) {
	
				String[] eachItem = data[i].split("#");		
				String[] snakeOtherSquaresArray = eachItem[2].split("T");
				
				ArrayList<String> snakeOtherSquares = new ArrayList<String>();
				
				for(int j = 0; j < snakeOtherSquaresArray.length; j++) {
					
					snakeOtherSquares.add(snakeOtherSquaresArray[j]);
				}

				String snakeHead = eachItem[1];
				int snakeId = Integer.parseInt(eachItem[0]);
				
				Snake snake = new Snake(snakeHead, snakeOtherSquares, snakeId);
				snake.setMySnakeDirection(Direction.DirectionTypes.valueOf(eachItem[3]));
				snake.setSpeed(Integer.parseInt(eachItem[4]));
				snake.setLastMoveTime(Long.parseLong(eachItem[5]));
				
				if(snakeId == gameData.getMyID()) {
					
					snake.setMySnake(true);
				}
				
				gameData.addSnake(snake);
			}
			
			if(gameData.getSnakes().size() == 1) {

				sendSnakeWonMessageToServer();
			}
			
			//set food item
			String[] foodLoc = data[0].split(",");
			CurrentFoodItem.setxLoc(Integer.parseInt(foodLoc[0]));
			CurrentFoodItem.setyLoc(Integer.parseInt(foodLoc[1]));
			CurrentFoodItem.setHasInitialized(true);
			CurrentFoodItem.setIsEaten(false);
			drawFoodItem();
		}
		catch(ArrayIndexOutOfBoundsException aIoBe) {
			
			//data may not be ready yet from other clients
			System.out.println("Error processing server data : " + aIoBe.toString());
		}
	}
	
	private void drawFoodItem() {
		
		if(CurrentFoodItem.isInitialized() && !CurrentFoodItem.isEaten()) {
		
			try {
				
				Square foodSquare = map.get(CurrentFoodItem.getxLoc() + "," + CurrentFoodItem.getyLoc());
				foodSquare.setBackground(Color.pink);
			}
			catch(NullPointerException npEx) {
				
				System.out.println("Error drawing food item : " + npEx.toString());
			}
		}
	}
	
	public int getRowNum() {
		
		return rowNum;
	}
	
	public int getColNum() {
		
		return colNum;
	}
	
	public HashMap<String, Square> getSquares() {
		
		return map;
	}
}