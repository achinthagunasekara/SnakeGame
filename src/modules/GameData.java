/*
 * 2013
 * Author : Archie Gunasekara
 */

package modules;

import java.util.HashMap;

public class GameData {

	private HashMap<Integer, Snake> snakes;
	private int numRows;
	private int numCols;
	private String myStartPos;
	private int myID;
	private static GameData instance = null;

	public static GameData getGameDataInstance() {
		
		if(instance == null) {
			
			instance = new GameData();
		}
		
		return instance;
	}
	
	private GameData() {
		
		snakes = new HashMap<Integer, Snake>();
	}
	
	public void addSnake(Snake s) {
		
		snakes.put(s.getClientId(), s);
	}
	
	public HashMap<Integer, Snake> getSnakes() {
		
		return snakes;
	}
	
	public void resetData() {
		
		snakes = new HashMap<Integer, Snake>();
	}

	public int getNumRows() {
		
		return numRows;
	}

	public void setNumRows(int numRows) {
		
		this.numRows = numRows;
	}

	public int getNumCols() {
		
		return numCols;
	}

	public void setNumCols(int numCols) {
		
		this.numCols = numCols;
	}

	public String getMyStartPos() {
		
		return myStartPos;
	}

	public void setMyStartPos(String myStartPos) {
		
		this.myStartPos = myStartPos;
	}

	public int getMyID() {
		
		return myID;
	}

	public void setMyID(int myID) {
		
		this.myID = myID;
	}
}