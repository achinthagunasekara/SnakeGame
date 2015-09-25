/*
 * @date: 2013
 * @author: Achintha Gunasekara
 */

package serverTools;

import game.Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import messages.MessageTypes;
import modules.Board;
import modules.FoodItem;
import modules.SnakeData;

public class ClientHandler implements Runnable {
	
	private BufferedReader reader; 
	private PrintWriter output;
	private final Socket socket;

	public ClientHandler(Socket socket) {
		
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		try {
		
			output = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String line;
			
			while((line = readFromClient()) != null) {

				if(line.equals(MessageTypes.clientMessageTypes.JOIN.toString())) {
					
					joinMessage();
				}
				else if(line.equals(MessageTypes.clientMessageTypes.GET_BOARD_SIZE.toString())) {
					
					sendBoardSize();
				}
				else if(line.equals(MessageTypes.clientMessageTypes.GET_LOCATIONS.toString())) {
					
					sendLocations();
				}
				else if(line.equals(MessageTypes.clientMessageTypes.READY_TO_START.toString())) {
					
					Game.setNumberOfPlayersReadyToStart(Game.getNumberOfPlayersReadyToStart() + 1); 
					writeToClient(MessageTypes.serverMessageTypes.WAIT.toString());
				}
				else if(line.equals(MessageTypes.clientMessageTypes.READY.toString())) {
					
					respondToReadyMessage();
				}
				else if (line.equals(MessageTypes.clientMessageTypes.SNAKE_ATE_FOOD.toString())) {
					
					FoodItem.getFoodItemInstance().setEaten(true);
					writeToClient(MessageTypes.serverMessageTypes.OK.toString());
				}
				else {
					
					String[] arr = line.split("#");
					System.out.println("Message type : " + arr[0]);
					
					if(arr[0].equals(MessageTypes.clientMessageTypes.SENDING_LOCATION.toString())) {
						
						//remove selected location from the list
						
						Board.getBoard().removeLocationFromList(arr[1]);
						writeToClient(MessageTypes.serverMessageTypes.WAIT.toString());
					}
					else if(arr[0].equals(MessageTypes.clientMessageTypes.SENDING_SNAKE_LOC.toString())) {
						
						sendSnakesLocationsToClient(arr);
					}
					else if(arr[0].equals(MessageTypes.clientMessageTypes.SNAKE_LOST.toString())) {
						
						SnakeData.getSnakeDataInstance().removeSnake(arr[1]);
						Game.setNumberOfPlayersReadyToStart(Game.getNumberOfPlayersReadyToStart() - 1);
						writeToClient(SnakeData.getSnakeDataInstance().getSnakeScore(arr[1]));
					}
					else if(arr[0].equals(MessageTypes.clientMessageTypes.SNAKE_WON.toString())) {
						
						if(Game.getNumberOfPlayersReadyToStart() == 1) {
						
							writeToClient(MessageTypes.serverMessageTypes.OK_WON.toString() + "," + SnakeData.getSnakeDataInstance().getSnakeScore(arr[1]));
						}
						else {
							
							writeToClient(MessageTypes.serverMessageTypes.OK.toString());
						}
					}
					else {
						
						//if command unknown, echo back to the client
						writeToClient("[ECHO] : " + line);
					}
				}
			}
		}
		catch(IOException ioEx) {
			
			System.out.println("Error setting up connection to client - " + ioEx.toString());
		}
	}
	
	private String readFromClient() {
	
		String line = null; 

		try {
			
			line = reader.readLine();
			System.out.println("[CLIENT SAID] :" + line);
		}
		catch(IOException ioEx) {
			
			System.out.println(ioEx.toString());
		}
		
		return line;
	}
	
	public void writeToClient(String s) {
		
		System.out.println("[SERVER SAID] :" + s);
		output.write(s + "\r\n");
		output.flush();
	}
	
	private void joinMessage() {
		
		if(!Game.isGameSetupComplete()) {
			
			writeToClient(MessageTypes.serverMessageTypes.SETUP_GAME.toString());
			Game.setGameSetupComplete(true);
			String[] clientRes = readFromClient().split(",");
			Game.setMaxPlayers(Integer.parseInt(clientRes[0])); //set max number of players
			Game.setNumberOfPlayers(Game.getNumberOfPlayers() + 1);
			
			//setup board
			Board board = Board.getBoard();
			board.setupBoard(Integer.parseInt(clientRes[1]), Integer.parseInt(clientRes[2]));
			board.removeLocationFromList(clientRes[3]);
				
			writeToClient(MessageTypes.serverMessageTypes.WAIT.toString());
			
			//send ID number to client
			writeToClient(Game.getNumberOfPlayers() + "");
		}
		else if(Game.getMaxPlayers() == 0) {
			
			writeToClient(MessageTypes.serverMessageTypes.BEING_SETUP.toString());
			
			//send ID number to client
			writeToClient("0");
		}
		else if(!Game.isGameFull()) {
			
			writeToClient(MessageTypes.serverMessageTypes.WELCOME.toString());
			Game.setNumberOfPlayers(Game.getNumberOfPlayers() + 1);
			System.out.println("New Player ID is " + Game.getNumberOfPlayers());
			//send ID number to client
			writeToClient(Game.getNumberOfPlayers() + "");
		}
		else {
			
			writeToClient(MessageTypes.serverMessageTypes.FULL.toString());
			
			//send ID number to client
			writeToClient("0");
		}
		
		System.out.println("Max number of players : " + Game.getMaxPlayers());
		System.out.println("Number of players set to : " + Game.getNumberOfPlayers());
	}
	
	private void sendBoardSize() {
	
		Board board = Board.getBoard();
		writeToClient(board.getRows() + "," + board.getCols());
	}
	
	private void sendLocations() {
		
		Board board = Board.getBoard();
		String output = "";
		
		for(String s : board.getAvailableLocations().values()) {
			
			output += s + ",";
		}
		
		writeToClient(output);
	}
	
	private void sendSnakesLocationsToClient(String[] arr) {
		
		FoodItem foodItem = FoodItem.getFoodItemInstance();

		if(foodItem.isEaten()) {
			
			foodItem.resetFoodItem();
		}

		//add food random food item to the list
		String sendData = foodItem.getLoc() + "!";

		String idOfClient = arr[1];
		SnakeData snakeData = SnakeData.getSnakeDataInstance();
		snakeData.replaceSnake(idOfClient, arr[2], arr[3], arr[4], arr[5], arr[6]);

		for(String s: snakeData.getSnakes().keySet()) {
			
			//s = snake ID
			sendData += s + "#" + 
						snakeData.getSnakes().get(s).get("HEAD") + "#" +
						snakeData.getSnakes().get(s).get("OTHER") + "#" +
						snakeData.getSnakes().get(s).get("DIRECTION") + "#" +
						snakeData.getSnakes().get(s).get("SPEED") + "#" +
						snakeData.getSnakes().get(s).get("LAST_MOVE_TIME") + "#" +
						"!"
						;
		}
		
		writeToClient(sendData);
	}
	
	private void respondToReadyMessage() {
		
		if(Game.getMaxPlayers() == Game.getNumberOfPlayers() && Game.getMaxPlayers() == Game.getNumberOfPlayersReadyToStart()) {
			
			Game.setGameStarted(true);
			writeToClient(MessageTypes.serverMessageTypes.START.toString());
		}
		else {
			
			writeToClient(MessageTypes.serverMessageTypes.WAIT.toString());
		}
	}
}
