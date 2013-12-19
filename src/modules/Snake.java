/*
 * 2013
 * Author : Archie Gunasekara
 */

package modules;

import java.util.ArrayList;

import modules.Direction.DirectionTypes;

import userInterface.GameGrid;
import userInterface.Square;

public class Snake {

	private String head;
	private ArrayList<String> otherSquares;
	private boolean mySnake = false;
	private Direction.DirectionTypes mySnakeDirection;
	private int clientId;
	private boolean snakeLost = false;
	private long lastMoveTime = 0;
	private int speed = 1; //default speed
	
	public Snake(String head, ArrayList<String> squares, int clientId) {
		
		this.head = head;
		this.otherSquares = squares;
		this.clientId = clientId;
	}

	public Boolean isMySnake() {
		
		return mySnake;
	}

	public void setMySnake(Boolean mySnake) {
		
		this.mySnake = mySnake;
	}
	
	public String getHead() {
		
		return head;
	}
	
	public ArrayList<String> getOtherSquares() {
		
		return otherSquares;
	}
	
	public Direction.DirectionTypes getDirection() {

		return mySnakeDirection;
	}
	
	public void setMySnakeDirection(Direction.DirectionTypes d) {
		
		mySnakeDirection = d;
	}
	
	public void speedUp() {

		if(speed < 3) {

			speed++;
		}
	}
	
	public void resetSpeed() {
		
		speed = 1;
	}
	
	private boolean speedTick() {
		
		if(lastMoveTime == 0) {
		
			lastMoveTime = System.nanoTime();
		}
		
		long now = System.nanoTime();
		
		if(speed == 3) {

			if((now - lastMoveTime) > 100000000) {

				lastMoveTime = now;
				return true;
			}
		}
		else if ((speed == 2)) {

			if((now - lastMoveTime) > 500000000) {

				lastMoveTime = now;
				return true;
			}
			
		}
		else if ((speed == 1)) {

			if((now - lastMoveTime) > 900000000) {

				lastMoveTime = now;
				return true;
			}
		}
		
		return false;
	}
	
	public void moveOneSpot(GameGrid gg) {
		
		if(speedTick()) {
			
			moveSnakeByOneSpot(gg);
		}
	}
	
	public void moveSnakeByOneSpot(GameGrid gg) {

		String previousHead = head;
		Square headSquare = gg.getSquares().get(head);
		
		if(mySnakeDirection == DirectionTypes.NORTH) {
			
			moveNorth(headSquare, previousHead, gg);
		}
		else if(mySnakeDirection == DirectionTypes.EAST) {
			
			moveEast(headSquare, previousHead, gg);
		}
		else if(mySnakeDirection == DirectionTypes.SOUTH) {
			
			moveSouth(headSquare, previousHead, gg);
		}
		else if(mySnakeDirection == DirectionTypes.WEST) {
			
			moveWest(headSquare, previousHead, gg);
		}
		else {
			//unknown direction do nothing
		}
	}
	
	private void moveNorth(Square headSquare, String previousHead, GameGrid gg) {
		
		int newRow = headSquare.getRow() - 1;
		int newCol = headSquare.getCol();
		
		if(newRow < 1) {
			
			newRow = gg.getRowNum();
		}
		
		moveSnake(previousHead, newRow, newCol);
	}
	
	private void moveEast(Square headSquare, String previousHead, GameGrid gg) {

		int newRow = headSquare.getRow();
		int newCol = headSquare.getCol() + 1;
		
		if(newCol > gg.getColNum()) {
			
			newCol = 1;
		}
		
		moveSnake(previousHead, newRow, newCol);
	}
	
	private void moveSouth(Square headSquare, String previousHead, GameGrid gg) {

		int newRow = headSquare.getRow() + 1;
		int newCol = headSquare.getCol();
		
		if(newRow > gg.getRowNum()) {
			
			newRow = 1;
		}
		
		moveSnake(previousHead, newRow, newCol);
	}
	
	private void moveWest(Square headSquare, String previousHead, GameGrid gg) {

		int newRow = headSquare.getRow();
		int newCol = headSquare.getCol() - 1;
		
		if(newCol < 1) {
			
			newCol = gg.getColNum();
		}
		
		moveSnake(previousHead, newRow, newCol);
	}
	
	private void moveSnake(String previousHead, int newHeadRow, int newHeadCol) {

		head = newHeadRow + "," + newHeadCol;
		
		ArrayList<String> tempSquares = new ArrayList<String>();
		
		int j;
		
		if(checkIfHeadOnFood()) {

			j = 0;
		}
		else {
			
			j = 1;
		}
		
		for(int i = j; i < otherSquares.size(); i++) {
			
			tempSquares.add(otherSquares.get(i));
		}
		
		
		tempSquares.add(previousHead);
		otherSquares = tempSquares;
		checkIfOnOtherSnake();
	}
	
	private boolean checkIfHeadOnFood() {
		
		if(head.equals(CurrentFoodItem.getxLoc() + "," + CurrentFoodItem.getyLoc())) {

			CurrentFoodItem.setIsEaten(true);
			return true;
		}
		
		return false;
	}
	
	private void checkIfOnOtherSnake() {
		
		GameData gameData = GameData.getGameDataInstance();
		
		for(Snake snake : gameData.getSnakes().values()) {

			if(!snake.isMySnake()) {

				if(head.equals(snake.getHead().toString())) {
	
					checkForWinnerWhenHeadCollapse(snake);
				}
				else {
					
					if(snake.getOtherSquares().contains(head)) {
						
						this.setSnakeLost(true);
					}
				}
			}
		}
	}
	
	private void checkForWinnerWhenHeadCollapse(Snake snake) {

		if(this.mySnakeDirection == Direction.DirectionTypes.NORTH && snake.mySnakeDirection == Direction.DirectionTypes.SOUTH) {
			
			setSnakeLost(this.otherSquares.size() <= snake.otherSquares.size());
		}
		else if(this.mySnakeDirection == Direction.DirectionTypes.SOUTH && snake.mySnakeDirection == Direction.DirectionTypes.NORTH) {

			setSnakeLost(this.otherSquares.size() <= snake.otherSquares.size());
		}
		else if(this.mySnakeDirection == Direction.DirectionTypes.EAST && snake.mySnakeDirection == Direction.DirectionTypes.WEST) {
			
			setSnakeLost(this.otherSquares.size() <= snake.otherSquares.size());
		}
		else if(this.mySnakeDirection == Direction.DirectionTypes.WEST && snake.mySnakeDirection == Direction.DirectionTypes.EAST) {
			
			setSnakeLost(this.otherSquares.size() <= snake.otherSquares.size());
		}
		else {
			
			setSnakeLost(true);
		}
	}

	public int getClientId() {
		
		return clientId;
	}

	public boolean isSnakeLost() {
		
		return snakeLost;
	}

	private void setSnakeLost(boolean b) {
		
		this.snakeLost = b;
	}
	
	public void setSpeed(int i) {
		
		this.speed = i;
	}
	
	public int getSpeed() {
		
		return speed;
	}
	
	public void setLastMoveTime(long l) {
		
		this.lastMoveTime = l;
	}
	
	public long getLastMoveTime() {
		
		return lastMoveTime;
	}
}