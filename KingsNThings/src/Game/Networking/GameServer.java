package Game.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer implements Runnable {
	
	private Socket connection;
	private static boolean gameEnded = false;
	private static boolean gameStarted = false;
	private static int ready = 0;
	
	private static List<Socket> clients = new ArrayList<Socket>();
	
	public GameServer(Socket connection){
		clients.add(connection);
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
 	    	
 	        while ((inputLine = in.readLine()) != null){
	        	if (!HandleClientReponse( inputLine, in, out )){
	        		break;
	        	}
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
		
		
		if (!gameStarted && event.eventId == EventList.READY){
			ready++;
			System.out.println(ready);
			CheckStartGame();
		}
		
				
		return true;
	}
	
	private void CheckStartGame(){
		if (clients.size() == ready){
			System.out.println("Ready to start");
			System.out.println("Playing with " + clients.size() + " players");
		}
	}
}
