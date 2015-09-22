/*
 * @date: 2013
 * @author: Achintha Gunasekara
 */

package messages;

import helpers.ConfigFileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageManager {

	private final String HOST = ConfigFileReader.getConfigFileReaderInstance().getPropertyFor("HOST");
	private final int PORT = Integer.parseInt(ConfigFileReader.getConfigFileReaderInstance().getPropertyFor("PORT"));
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private static MessageManager instance = null;
	
	public static MessageManager getMessageManager() {
		
		if(instance == null) {
			
			instance = new MessageManager();
		}
		
		return instance;
	}
	
	private MessageManager() {
		
		startConnection();
	}
	
	private void startConnection() {
		
		try {
			
			socket = new Socket(HOST, PORT);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
		}
		catch(UnknownHostException e) {
			
            System.err.println("Don't know about host: " + HOST);
            System.exit(1);
        }
		catch(IOException e) {
            
			System.err.println("Couldn't get I/O for " + "the connection to: " + HOST);
            System.exit(1);
        }
	}
	
	public String writeToServer(String s) throws IOException {

		System.out.println("[CLIENT SAID] : " + s);
		out.println(s);
		String res = in.readLine();
		System.out.println("[SERVER SAID] : " + res);
		return res;
	}
	
	public String readFromServer() throws IOException{

		String res = in.readLine();
		System.out.println("[SERVER SAID] : " + res);
		return res;
	}
	
	public void closeConnection() {
		
		try {
			
			out.close();
			in.close();
			socket.close();
		}
		catch(IOException ioEx) {
			//do nothing - connection may have dropped out
		}
	}
}
