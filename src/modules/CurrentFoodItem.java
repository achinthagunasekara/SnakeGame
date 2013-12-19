/*
 * 2013
 * Author : Archie Gunasekara
 */

package modules;

public class CurrentFoodItem {
	
	private static int xLoc;
	private static int yLoc;
	private static boolean isEaten = false;
	private static boolean initialized = false;
	
	public static int getxLoc() {
		
		return xLoc;
	}
	
	public static void setxLoc(int i) {
		
		xLoc = i;
	}
	
	public static int getyLoc() {
		
		return yLoc;
	}
	
	public static void setyLoc(int i) {
		
		yLoc = i;
	}

	public static boolean isEaten() {
		
		return isEaten;
	}

	public static void setIsEaten(boolean b) {
		
		isEaten = b;
	}

	public static boolean isInitialized() {
		
		return initialized;
	}

	public static void setHasInitialized(boolean b) {
		
		initialized = b;
	}
}