package Game.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class EventReceiver implements Runnable {
	private Socket connection; 
	private String fromServer;
	
	public EventReceiver( Socket c ){
		connection = c;
	}
	
	public void run(){
		try { 
			BufferedReader in = new BufferedReader(
				new InputStreamReader(
					connection.getInputStream()
				)
	    	);

			//!!!! GameController is Static, thus not initialized for joined client !!!!!!
			while(true) {
				System.out.println("GameClient's Event Reciver is waiting for an event to handle");
				fromServer = in.readLine();
					
				//second line is never read
				
				System.out.println("ATTEMPTING TO HANDLE A DICE ROLL");
				EventHandler.HandleEvent(fromServer);
				
			}
		} catch (IOException e){}
	}
}
