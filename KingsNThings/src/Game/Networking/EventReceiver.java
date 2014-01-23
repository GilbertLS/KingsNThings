package Game.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class EventReceiver implements Runnable {
	private Socket connection; 
	
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

			while( !GameHost.gameEnded ) {
				String fromServer = in.readLine();
					
				EventHandler.HandleEvent(fromServer);
				
			}
		} catch (IOException e){}
	}
}
