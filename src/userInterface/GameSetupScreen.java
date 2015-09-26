/*
 * @date: 2013
 * @author: Achintha Gunasekara
 */

package userInterface;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


import messages.MessageManager;
import messages.MessageTypes;
import modules.GameData;

public class GameSetupScreen extends  JInternalFrame implements ActionListener {
	
	private int numPlayersEntered;
	private int numRowsEntered;
	private int numColsEntered;
	private String locEntered;
	private MainWindow mw;
	private boolean showAll;
	
	private JLabel numPlayersLabel;
	private JComboBox<String> numPlayers;
	private JLabel locLabel;
	private JComboBox<String> loc;
	private JLabel numRowsLabel;
	private JTextField numRows;
	private JLabel numColsLabel;
	private JTextField numCols;
	private JButton enter;
	
	public GameSetupScreen(MainWindow mw, boolean showAll, String[] locations) {
		
		super("Game Setup", false, false, false, false);
		this.mw = mw;
		this.showAll = showAll;
	    setLocation(10, 10);
	    setSize(400, 400);
	    setBackground(Color.GRAY);
	    setLayout(new GridLayout(9, 2));
	    
	    locLabel = new JLabel("Players Location");
	    
	    String[] locList;
	    
	    if(showAll) {
	    
	    	locList = new String[]{"TOP_LEFT", "TOP_RIGHT", "BOTTOM_LEFT", "BOTTOM_RIGHT"};
	    }
	    else {
	    	
	    	locList = locations;
	    }
	    
	    loc = new JComboBox<String>(locList);
	       
	    numPlayersLabel = new JLabel("Number of Players (2 - 4)");
	    String[] numPlayersVals = {"2", "3", "4"};
	    numPlayers = new JComboBox<String>(numPlayersVals);
	    
	    numRowsLabel = new JLabel("Number of rows (10 - 40)");
	    numRows = new JTextField("10");
	    
	    numColsLabel = new JLabel("Number of cols (10 - 40)");
	    numCols = new JTextField("10");
	    
	    enter = new JButton("Enter");
	    enter.addActionListener(this);
	    

	    add(locLabel);
	    add(loc);
	    add(new JLabel("")); //empty cell
	    add(new JLabel("")); //empty cell   

	    if(showAll) {
	    	
		    add(numPlayersLabel);
		    add(numPlayers);
		    add(new JLabel("")); //empty cell
		    add(new JLabel("")); //empty cell   
		    
		    add(numRowsLabel);
		    add(numRows);
		    add(new JLabel("")); //empty cell
		    add(new JLabel("")); //empty cell
	
		    add(numColsLabel);
		    add(numCols);
		    add(new JLabel("")); //empty cell
		    add(new JLabel("")); //empty cell
		    
	    }
	    
	    add(new JLabel("")); //empty cell
	    add(enter);
	    
	    setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		locEntered = loc.getSelectedItem().toString();
		GameData.getGameDataInstance().setMyStartPos(locEntered);
		
		if(showAll) {
			
			sendFullGameSetup();
		}
		else {
			
			justLocationSetup();
		}
	}
	
	private void sendFullGameSetup() {
	
		try {
			
			numPlayersEntered = Integer.parseInt(numPlayers.getSelectedItem().toString());
			numRowsEntered = Integer.parseInt(numRows.getText());
			numColsEntered = Integer.parseInt(numCols.getText());
			
			if(numRowsEntered < 10 || numRowsEntered > 40 || numColsEntered < 10 || numColsEntered > 40) {
				
				throw new Exception("Minimum value rows and columns is 10 and max value is 40");
			}
			
			GameData gameData = GameData.getGameDataInstance();
			gameData.setNumRows(numRowsEntered);
			gameData.setNumCols(numColsEntered);
			
			sendFullGameSetupData();
			this.setVisible(false);
			mw.setBoardSetupComplete(true);
			mw.validate();
		}
		catch(NumberFormatException nfEx) {
			
			JOptionPane.showMessageDialog(this, "Invalid Values");
		}
		catch(IOException ioEx) {
			
			JOptionPane.showMessageDialog(this, "Failed to communitate with game server!\n" + ioEx.toString());
		}
		catch(Exception ex) {
			
			JOptionPane.showMessageDialog(this, "Minimum value rows and columns is 10 and max value is 40");
		}
	}
	
	private void justLocationSetup() {
		
		try {
			
			sendJustLocationGameData();
			this.setVisible(false);
			mw.setBoardSetupComplete(true);
			mw.validate();
		}
		catch(IOException ioEx) {
			
			JOptionPane.showMessageDialog(this, "Failed to communitate with game server!\n" + ioEx.toString());
		}
	}
	
	private void sendJustLocationGameData() throws IOException {
		
		MessageManager.getMessageManager().writeToServer(MessageTypes.clientMessageTypes.SENDING_LOCATION + "#" + locEntered);
	}
	
	private void sendFullGameSetupData() throws IOException{
		
		MessageManager.getMessageManager().writeToServer(numPlayersEntered + "," + numRowsEntered + "," + numColsEntered + "," + locEntered);
	}
}
