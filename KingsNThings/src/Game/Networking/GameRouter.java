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

public class GameRouter implements Runnable {
	
	private Socket connection;
	private boolean ready = false;
	private static int id = 0;
	public int myID;
	PrintWriter out;
	BufferedReader in;
	
	public GameRouter(Socket connection){
		this.connection = connection;
		this.myID = id++;
		
		try{
		 out = new PrintWriter(
				connection.getOutputStream(),
				true
 			);
         in = new BufferedReader(
        		new InputStreamReader(
    				connection.getInputStream()
				)
        	);
		}
         catch (IOException e) {
  	        System.out.println("Exception caught when trying to listen on port "
  	            + connection.getPort() + " or listening for a connection");
  	        System.out.println(e.getMessage());
  	    } 
		
		GameController.AddClient( this );
	}
	
	public void run(){
 	    	ready = true;
 	        /////////////////////// GAME START ////////////////////////////
	}
	
	public boolean ready(){
		return ready;
	}
	
	public Response sendEvent(Event e)
	{
		//send the event to the GameClient
		System.out.println("SENDING EVENT " + e.toString() + "TO GAME CLIENT");
		out.println(e.toString());
		String returnEvent;
		try {
			//get the message back from the GameClient (this fails for the second client)
			returnEvent = in.readLine();
			
			String responseString = (Event.Destringify(returnEvent)).getEventParameters();
			System.out.println("EVENT PARAM:" + responseString);
			return new Response(responseString);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
