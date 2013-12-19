/*
 * 2013
 * Author : Archie Gunasekara
 */

package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FoodItem {
	
	private int xLoc;
	private int yLoc;
	private static FoodItem instance = null;
	private boolean eaten = false;

	public synchronized static FoodItem getFoodItemInstance() {
		
		if(instance == null) {
			
			instance = new FoodItem();
		}
		
		return instance;
	}
	
	private FoodItem(){
		
		resetFoodItem();
	}
	
	public synchronized void resetFoodItem() {
		
		setupNewFoodItem();
		setEaten(false);
	}
	
	private void setupNewFoodItem() {

		ArrayList<Integer> takenXLocs = new ArrayList<Integer>();
		ArrayList<Integer> takenYLocs = new ArrayList<Integer>();

		SnakeData snakeData = SnakeData.getSnakeDataInstance();
		
		for(HashMap<String, String> hMap : snakeData.getSnakes().values()) {
		
			String[] headData = hMap.get("HEAD").split(",");
			takenXLocs.add(Integer.parseInt(headData[0]));
			takenYLocs.add(Integer.parseInt(headData[1]));
			
			String[] otherBodyData = hMap.get("OTHER").split("T");
			
			for(int i = 0; i < otherBodyData.length; i++) {
				
				String[] eachBodyData = otherBodyData[i].split(",");
				takenXLocs.add(Integer.parseInt(eachBodyData[0]));
				takenYLocs.add(Integer.parseInt(eachBodyData[1]));
			}
		}
		
		int maxXloc = Board.getBoard().getRows();
		int maxYloc = Board.getBoard().getCols();

		xLoc = getRandomNumNotInList(takenXLocs, maxXloc);
		yLoc = getRandomNumNotInList(takenYLocs, maxYloc);
	}
	
	private int getRandomNumNotInList(ArrayList<Integer> list, int max) {
		
		Random random = new Random();
		int num = random.nextInt(max) + 1;
		
		while(list.contains(num)) {

			num = random.nextInt(max) + 1;
		}

		return num;
	}

	public synchronized String getLoc() {
		
		return (xLoc + "," + yLoc);
	}

	public synchronized boolean isEaten() {
		
		return eaten;
	}

	public synchronized void setEaten(boolean eaten) {
		
		this.eaten = eaten;
	}
}