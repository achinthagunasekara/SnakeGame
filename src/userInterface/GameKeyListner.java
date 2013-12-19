/*
 * 2013
 * Author : Archie Gunasekara
 */

package userInterface;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import modules.Direction;
import modules.GameData;
import modules.Snake;


class GameKeyListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		
		//do nothing
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == 38) {
			
			changeSnakeSpeed(Direction.KeyTypes.UP);
		}
		else if(e.getKeyCode() == 39) {
			
			changeSnakeDirection(Direction.KeyTypes.RIGHT);
		}
		else if(e.getKeyCode() == 40) {
			
			changeSnakeSpeed(Direction.KeyTypes.DOWN);
		}
		else if(e.getKeyCode() == 37) {

			changeSnakeDirection(Direction.KeyTypes.LEFT);
		}
		else {
			//do nothing
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		//do nothing		
	}
	
	private void changeSnakeSpeed(Direction.KeyTypes k) {
	
		Snake mySnake = getMySnake();
		
		if(k == Direction.KeyTypes.UP) {
			
			mySnake.speedUp();
		}
		else if(k == Direction.KeyTypes.DOWN) {
			
			mySnake.resetSpeed();
		}
	}

	private void changeSnakeDirection(Direction.KeyTypes k) {
		
		Snake mySnake = getMySnake();
		mySnake.setMySnakeDirection(getSelectedDirection(mySnake.getDirection(), k));
	}

	private Snake getMySnake() {
		
		GameData gameData = GameData.getGameDataInstance();
		
		for(Snake currentSnake : gameData.getSnakes().values()) {
			
			if(currentSnake.isMySnake()) {
				
				return currentSnake;
			}
		}
		
		return null;
	}
	
	private Direction.DirectionTypes getSelectedDirection(Direction.DirectionTypes d, Direction.KeyTypes k) {

		if(d == Direction.DirectionTypes.NORTH) {
			
			if(k == Direction.KeyTypes.RIGHT) {
				
				return Direction.DirectionTypes.EAST;
			}
			else {
				
				return Direction.DirectionTypes.WEST;
			}
		}
		else if(d == Direction.DirectionTypes.EAST) {
			
			if(k == Direction.KeyTypes.RIGHT) {
				
				return Direction.DirectionTypes.SOUTH;
			}
			else {
				
				return Direction.DirectionTypes.NORTH;
			}
		}
		else if(d == Direction.DirectionTypes.SOUTH) {
			
			if(k == Direction.KeyTypes.RIGHT) {
				
				return Direction.DirectionTypes.EAST;
			}
			else {
				
				return Direction.DirectionTypes.WEST;
			}
		}
		else {
			
			if(k == Direction.KeyTypes.RIGHT) {
				
				return Direction.DirectionTypes.SOUTH;
			}
			else {
				
				return Direction.DirectionTypes.NORTH;
			}
		}
	}
}