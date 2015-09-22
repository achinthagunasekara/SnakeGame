/*
 * @date: 2013
 * @author: Achintha Gunasekara
 */

package messages;

public class MessageTypes {
	
	public enum clientMessageTypes {
		JOIN,
		GET_BOARD_SIZE,
		GET_LOCATIONS,
		GET_ID,
		SENDING_LOCATION,
		SENDING_SNAKE_LOC,
		SNAKE_ATE_FOOD,
		SNAKE_LOST,
		SNAKE_WON,
		READY_TO_START,
		READY,
	}
	
	public enum serverMessageTypes {
		SETUP_GAME,
		BEING_SETUP,
		WELCOME,
		WAIT,
		START,
		FULL,
		OK,
		OK_WON,
	}
}
