/*
 * 2013
 * Author : Archie Gunasekara
 */

package server;

import helpers.ConfigFileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import serverTools.ClientHandler;

public class Server {

	private final int PORT = Integer.parseInt(ConfigFileReader.getConfigFileReaderInstance().getPropertyFor("PORT"));
	private ServerSocket serverSocket;
	
	public static void main(String[] args) {
		
		Server s = new Server();
		s.start();
	}
	
	public void start() {
		
		try {
			
			serverSocket = new ServerSocket(PORT);
			System.out.println("Listening for connections on port " + PORT + "... ");
			
			while(true) {

				Socket client = serverSocket.accept();
				
				Thread t = new Thread(new ClientHandler(client));
				t.start();
				System.out.println("Connected - "+ client.getInetAddress());
			}
		}
		catch(IOException ioEx) {
			
			ioEx.printStackTrace();
		}
	}
}