/*
 * @date: 2013
 * @author: Achintha Gunasekara
 */

package userInterface;

import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame {

	private Container content;
	private JDesktopPane desktop;
	private GameGrid gameGrid = null;
	private boolean boardSetupComplete;
	
	public MainWindow() {
		
		super("Snakes Game");
	    content = getContentPane();
	    content.setBackground(Color.white);
	    
	    desktop = new JDesktopPane();
	    desktop.setBackground(Color.white);
	    content.add(desktop, BorderLayout.CENTER);
	    setSize(750, 750);
	}
	
	public void showGameSetup(boolean showAll, String[] locations) {

		GameSetupScreen gsc = new GameSetupScreen(this, showAll, locations);
		desktop.add(gsc);
	    this.validate();
	}
	
	public void showMessage(String s) {
		
		JOptionPane.showMessageDialog(this, s);
	}
	
	public void showGameGrid() {

		this.gameGrid = new GameGrid();
		desktop.add(gameGrid);
		this.validate();
	}
	
	public void runGame() {
		
		gameGrid.startGame();
	}

	public boolean isBoardSetupComplete() {
		
		return boardSetupComplete;
	}

	public void setBoardSetupComplete(boolean boardSetupComplete) {
		
		this.boardSetupComplete = boardSetupComplete;
	}
}
