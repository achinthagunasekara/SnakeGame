/*
 * 2013
 * Author : Archie Gunasekara
 */

package userInterface;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Square extends JButton {

	private int row, col;
	private String name;
	
	public Square(int row, int col) {

		super();
		setColour(Color.gray);
		this.row = row;
		this.col = col;
		this.name = row + "," + col;
	}
	
	public void setColour(Color c) {
		
		this.setBackground(c);
	}
	
	public int getCol() {
		
		return col;
	}
	
	public int getRow() {

		return row;
	}
	
	public String getName() {
		
		return name;
	}
	
	public void drawHead() {
		
		Border thickBorder = new LineBorder(Color.black, 2);
		setBorder(thickBorder);
	}
	
	public void resetHead() {
		
		setBorder(null);
	}
}