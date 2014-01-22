package Game.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameServer implements Runnable {
	
	private Socket connection;
	private static int ready = 0;
	
	public GameServer(Socket connection){
		GameHost.AddClient( connection );
		this.connection = connection;
	}
	
	public void run(){
		try {
			PrintWriter out = new PrintWriter(
				connection.getOutputStream(),
				true
 			);
            BufferedReader in = new BufferedReader(
        		new InputStreamReader(
    				connection.getInputStream()
				)
        	);
 	         
        	String inputLine;
        
        	out.println("");
 	    	
 	        while ((inputLine = in.readLine()) != null &&
 	        	   (HandleClientReponse( inputLine, in, out ))){
 	        	/////////////////////// GAME START ////////////////////////////
 	        
 	        }
 	    } catch (IOException e) {
 	        System.out.println("Exception caught when trying to listen on port "
 	            + connection.getPort() + " or listening for a connection");
 	        System.out.println(e.getMessage());
 	    } finally {
 	    	try {
 	    		connection.close();
 	    	}
 	    	catch (IOException e){}
 	    }
	}
	
	private boolean HandleClientReponse(
			String inputLine,
			BufferedReader in,
			PrintWriter out
	){
		Event event = Event.Destringify(inputLine);
		
		
		if (!GameHost.gameStarted && event.eventId == EventList.READY){
			ready++;
			System.out.println(ready);
			CheckStartGame();
		}
		
				
		return true;
	}
	
	private void CheckStartGame(){
		if (GameHost.clients.size() == ready){
			System.out.println("Ready to start");
			System.out.println("Playing with " + GameHost.clients.size() + " players");
		}
	}
}
