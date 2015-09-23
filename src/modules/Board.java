/*
 * @date: 2013
 * @author: Achintha Gunasekara
 */

package modules;

import java.util.HashMap;

public class Board {
	
	private HashMap<String, String> availableLocations;
	private int rows;
	private int cols;
	private static Board instance = null;
	
	public synchronized static Board getBoard() {
		
		if(instance == null) {
			
			instance = new Board();
		}
		
		return instance;
	}
	
	private Board() {

		setupLocations();
	}
	
	public synchronized void setupLocations() {
		
		availableLocations = new HashMap<String, String>();
		availableLocations.put("TOP_LEFT", "TOP_LEFT");
		availableLocations.put("TOP_RIGHT", "TOP_RIGHT");
		availableLocations.put("BOTTOM_LEFT", "BOTTOM_LEFT");
		availableLocations.put("BOTTOM_RIGHT", "BOTTOM_RIGHT");
	}
	
	public synchronized void setupBoard(int rows, int cols) {
		
		this.rows = rows;
		this.cols = cols;
	}
	
	public synchronized HashMap<String, String> getAvailableLocations() {

		return availableLocations;
	}
	
	public synchronized void removeLocationFromList(String loc) {
		
		availableLocations.remove(loc);
	}
	
	public synchronized int getRows() {
		
		return rows;
	}

	public synchronized int getCols() {
		
		return cols;
	}
}
