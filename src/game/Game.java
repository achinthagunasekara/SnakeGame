/*
 * @date: 2013
 * @author: Achintha Gunasekara
 */

package game;

public class Game {

	private static boolean gameStarted = false;
	private static boolean gameSetupComplete = false;
	private static int maxPlayers = 0;
	private static int numberOfPlayers = 0;
	private static int numberOfPlayersReadyToStart = 0;
	
	public synchronized static boolean isGameStarted() {

		return gameStarted;
	}
	
	public synchronized static void setGameStarted(boolean gameStarted) {
	
		Game.gameStarted = gameStarted;
	}
	
	public synchronized static boolean isGameSetupComplete() {

		return gameSetupComplete;
	}

	public synchronized static void setGameSetupComplete(boolean gameSetupComplete) {
		
		Game.gameSetupComplete = gameSetupComplete;
	}
	
	public synchronized static int getMaxPlayers() {
	
		return maxPlayers;
	}
	
	public synchronized static void setMaxPlayers(int maxPlayers) {
	
		Game.maxPlayers = maxPlayers;
	}
	
	public synchronized static boolean isGameFull() {
	
		return (numberOfPlayers >= maxPlayers);
	}
	
	public synchronized static int getNumberOfPlayers() {
		
		return numberOfPlayers;
	}
	
	public synchronized static void setNumberOfPlayers(int numberOfPlayers) {
	
		Game.numberOfPlayers = numberOfPlayers;
	}

	public static int getNumberOfPlayersReadyToStart() {
		
		return numberOfPlayersReadyToStart;
	}

	public static void setNumberOfPlayersReadyToStart(int i) {
		
		numberOfPlayersReadyToStart = i;
	}
}
