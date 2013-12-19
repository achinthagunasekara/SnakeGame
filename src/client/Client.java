package client;
/*
 * 2013
 * Author : Archie Gunasekara
 */

import java.io.IOException;
import java.util.ArrayList;

import userInterface.MainWindow;
import messages.*;
import modules.Direction;
import modules.GameData;
import modules.Snake;

public class Client {
	
	private MainWindow mw;
	private MessageManager mm;
	private GameData gameData;
	
	public static void main(String[] args) {
		
		Client c = new Client();
		c.runClient();
	}
	
	public void runClient() {
		
		gameData = GameData.getGameDataInstance();
		mm = MessageManager.getMessageManager();
		mw = new MainWindow();
		mw.setVisible(true);
		
		try {

			String responseJoin = mm.writeToServer(MessageTypes.clientMessageTypes.JOIN.toString());
		
			if(responseJoin.equals(MessageTypes.serverMessageTypes.SETUP_GAME.toString())) {
	
				mw.showGameSetup(true, new String[4]); //pass blank location list, will not be used
				
				//wait till board setup is complete
				while(!mw.isBoardSetupComplete()) {
				
					System.out.println("Waiting for player to setup the game");
				}
				
				System.out.println("Setup complete");
				responseJoin = MessageTypes.serverMessageTypes.WAIT.toString();
				
				//setup ID for this client
				gameData.setMyID(Integer.parseInt(mm.readFromServer()));
			}
			else if(responseJoin.equals(MessageTypes.serverMessageTypes.BEING_SETUP.toString())) {
				
				mw.showMessage("Game is being setup by another player, please try again bit later");
				System.exit(0);
			}
			else if(responseJoin.equals(MessageTypes.serverMessageTypes.WELCOME.toString())) {

				//setup ID for this client
				gameData.setMyID(Integer.parseInt(mm.readFromServer()));
				
				setupBoardForJoiningClient();
				
				responseJoin = MessageTypes.serverMessageTypes.WAIT.toString();
			}
			else {
				mw.showMessage("Game is full at this time!");
				System.exit(0);
			}

			responseJoin = mm.writeToServer(MessageTypes.clientMessageTypes.READY_TO_START.toString());
			
			while(responseJoin.equals(MessageTypes.serverMessageTypes.WAIT.toString())) {
				
				responseJoin = mm.writeToServer(MessageTypes.clientMessageTypes.READY.toString());
			}
			
			//Start the game when every one is ready
			createMySnake();
			mw.showGameGrid();
			mw.runGame();
		}
		catch (IOException ioEx) {
			
			mw.showMessage(ioEx.toString());
		}
	}
	
	private void setupBoardForJoiningClient() {
		
		try {
		
			String[] boardSize =  mm.writeToServer(MessageTypes.clientMessageTypes.GET_BOARD_SIZE.toString()).split(",");
			
			int rows = Integer.parseInt(boardSize[0]);
			int cols = Integer.parseInt(boardSize[1]);
			gameData.setNumRows(rows);
			gameData.setNumCols(cols);
			
			String[] locations =  mm.writeToServer(MessageTypes.clientMessageTypes.GET_LOCATIONS.toString()).split(",");

			mw.showGameSetup(false, locations);
			
			//wait till board setup is complete
			while(!mw.isBoardSetupComplete()) {
			
				System.out.println("Waiting for players to their snake : " + mw.isBoardSetupComplete());
			}
		}
		catch(IOException ioEx) {
			
			System.out.println("ERROR :" + ioEx.toString());
			System.exit(0);
		}
	}
	
	private void createMySnake() {
	
		Snake snake;
		ArrayList<String> list = new ArrayList<String>();
		String myPosition = gameData.getMyStartPos();
		
		if(myPosition.equals("TOP_LEFT")) {
			
			list.add("1,1");
			snake = new Snake("1,2", list, gameData.getMyID());
			snake.setMySnakeDirection(Direction.DirectionTypes.EAST);
		}
		else if(myPosition.equals("TOP_RIGHT")) {
			
			list.add("1," + gameData.getNumCols());
			snake = new Snake("1," + (gameData.getNumCols() - 1), list, gameData.getMyID());
			snake.setMySnakeDirection(Direction.DirectionTypes.WEST);
		}
		else if(myPosition.equals("BOTTOM_LEFT")) {
			
			list.add(gameData.getNumRows() + ",1");
			snake = new Snake(gameData.getNumRows() + ",2", list, gameData.getMyID());
			snake.setMySnakeDirection(Direction.DirectionTypes.EAST);
		}
		else {
			
			list.add(gameData.getNumRows() + "," + gameData.getNumCols());
			snake = new Snake(gameData.getNumRows() + "," + (gameData.getNumCols() - 1), list, gameData.getMyID());
			snake.setMySnakeDirection(Direction.DirectionTypes.WEST);
		}
		
		snake.setMySnake(true);
		gameData.addSnake(snake);
	}
}