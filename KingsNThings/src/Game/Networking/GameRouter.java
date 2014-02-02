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

import Game.GameController;

public class GameRouter implements Runnable, Comparable<GameRouter> {
	
	private Socket connection;
	private boolean ready = false;
	private static int id = 0;
	private static int order =0;
	public int myID;
	public int myPlayerOrder;
	PrintWriter out;
	BufferedReader in;
	
	public GameRouter(Socket connection){
		this.connection = connection;
		this.myID = id++;
		
		this.myPlayerOrder = order++;
		
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
		System.out.println("SENDING EVENT " + e.toString() + " TO GAME CLIENT - " + myID);
		out.println(e.toString());
		
		if(e.eventId == EventList.SET_PLAYER_ORDER || e.eventId == EventList.UPDATE_PLAYER_ORDER)
			updateGameRouterID(e);
		
		//if necessary, wait for a response
		if(e.expectsResponseEvent)
		{
			String returnEvent;
			try {
				//get the message back from the GameClient 
				returnEvent = in.readLine();
				
				return new Response(Event.Destringify(returnEvent));
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	private void updateGameRouterID(Event e) {
		int numPlayers = Integer.parseInt(e.eventParams[0]);
		
		if(e.eventId == EventList.SET_PLAYER_ORDER)
			myPlayerOrder = (myPlayerOrder + (numPlayers - Integer.parseInt(e.eventParams[1]))) % numPlayers;
		else if(e.eventId == EventList.UPDATE_PLAYER_ORDER)
			myPlayerOrder = (myPlayerOrder + 1) % numPlayers;
	
		System.out.println("GAME ROUTER WITH INDEX " + myID + " HAS SET ITS PLAYER ORDER TO - " + myPlayerOrder);
	}
	
	public int compareTo(GameRouter g){
		return this.myPlayerOrder - ((GameRouter)g).myPlayerOrder;
	}
}
