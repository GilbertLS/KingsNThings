package Game.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Game.GameController;
import Game.Phases.Phase;

public class GameRouter implements Runnable, Comparable<GameRouter> {
	
	private Socket connection;
	private boolean ready = false;
	private static int id = 0;
	private static int order =0;
	public int myID;
	public int myPlayerOrder;
	private PrintWriter out;
	private BufferedReader in;
	
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
		} catch (IOException e) {
  	        System.out.println("Exception caught when trying to listen on port "
  	            + connection.getPort() + " or listening for a connection");
  	        System.out.println(e.getMessage());
  	    } 
		
		GameServer.AddClient( this );
	}
	
	public void run(){
 	    	ready = true;
 	        /////////////////////// GAME START ////////////////////////////
 	    	while(!GameController.gameEnded) {
 	    		try {
					String event = in.readLine();
					
					Event e = Event.Destringify(event);
					
					if (GameControllerEventHandler.waitingEvent != null && 
						(GameControllerEventHandler.waitingEvent.eventId == e.eventId ||
						 e.eventId == EventList.NULL_EVENT )
					) {
						Response r = new Response(e);
						r.fromPlayer = myID;
						synchronized(GameControllerEventHandler.waitingEvents) {
							GameControllerEventHandler.waitingEvents.add(r);
							GameControllerEventHandler.waitingEvents.notify();
						}
					} else if (e.eventId == EventList.SET_PHASE) {
						Phase phase = Phase.valueOf(e.eventParams[0]);
						System.out.println("DEBUG---- " + phase.name());
						GameController.SetPhase(phase);
					} else {
						/*boolean[] intendedPlayers = new boolean[4];
						
						for (int i = 0; i < 4; i++){
							if (i == myID) {
								intendedPlayers[i] = false;
							} else {
								intendedPlayers[i] = true;
							}
						}*/
						
						Event d = new Event()
									.EventId(e.eventId)
									.EventParameters(e.eventParams)
									//.IntendedPlayers(intendedPlayers)
									.ExpectsResponse(false);
						
						GameControllerEventHandler.sendEvent(e);
					}
				} catch (IOException e) {}
 	    	}
	}
	
	public boolean ready(){
		return ready;
	}
	
	public void sendEvent(Event e)
	{
		//send the event to the GameClient
		System.out.println("SENDING EVENT " + e.toString() + " TO GAME CLIENT - " + myID);
		out.println(e.toString());
		
		if(e.eventId == EventList.SET_PLAYER_ORDER || e.eventId == EventList.UPDATE_PLAYER_ORDER)
			updateGameRouterOrder(e);
		
	}

	private void updateGameRouterOrder(Event e) {
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
