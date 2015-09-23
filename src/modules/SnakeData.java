/*
 * @date: 2013
 * @author: Achintha Gunasekara
 */

package modules;

import java.util.HashMap;
import game.Game;

public class SnakeData {
	
	private HashMap<String, HashMap<String, String>> snakes;
	private static SnakeData instance = null;
	private static int count = 0;
	private HashMap<String, Integer> scores;

	public synchronized static SnakeData getSnakeDataInstance() {
		
		if(instance == null) {
			
			instance = new SnakeData();
		}
		
		return instance;
	}
	
	private SnakeData() {
		
		scores = new HashMap<String, Integer>();
		reset();
	}
	
	public synchronized HashMap<String, HashMap<String, String>> getSnakes() {

		return snakes;
	}
	
	public synchronized void replaceSnake(String snakeId, String snakeHead, String otherSquares, String direction, String speed, String lastMoveTime) {
		
		HashMap<String, String> snakeInfo = new HashMap<String, String>();
		snakeInfo.put("HEAD", snakeHead);
		snakeInfo.put("OTHER", otherSquares);
		snakeInfo.put("DIRECTION", direction);
		snakeInfo.put("SPEED", speed);
		snakeInfo.put("LAST_MOVE_TIME", lastMoveTime);

		snakes.put(snakeId, snakeInfo);
	}
	
	public synchronized void removeSnake(String snakeId) {
		
		scores.put(snakeId, count);
		count++;
		snakes.remove(snakeId);
	}
	
	public synchronized String getSnakeScore(String snakeId) {
		
		String score;
		
		try {
			score =  scores.get(snakeId).toString();
		}
		catch(NullPointerException npEx) {
			
			score = (Game.getMaxPlayers() - 1) + "";
		}
		
		return score;		
	}

	public synchronized void reset() {
		
		snakes = new HashMap<String, HashMap<String, String>>();
	}
}
