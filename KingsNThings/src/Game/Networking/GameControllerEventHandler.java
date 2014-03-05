package Game.Networking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import Game.GameController;

public class GameControllerEventHandler {
	public static List<Response> waitingEvents = new ArrayList<Response>();
	public static Event waitingEvent = null;
	
	public static Response[] sendEvent(Event e)
	{
		int numResponses = 0;
		
		for(GameRouter i : GameServer.servers){
			if(e.intendedPlayers[i.myID] == true && e.expectsResponseEvent){
				numResponses++;
			}
		}
		
		if (e.expectsResponseEvent) {
			waitingEvent = new Event().EventId(e.eventId);
		}
		
		Response[] responses = new Response[numResponses];
		
		for(GameRouter gr : GameServer.servers) {
			if(e.intendedPlayers[gr.myID] == true) {
				gr.sendEvent(e);
			}
		}
		
		if(e.expectsResponseEvent) {
			synchronized(waitingEvents) {
				while(waitingEvents.size() < numResponses) {
					try {
						waitingEvents.wait();
					} catch (InterruptedException e1) {}
				}
			}
			
			int i = 0;
			
			for (Response reply : waitingEvents) {
				responses[i++] = reply;
			}
			
			waitingEvents = new ArrayList<Response>();
			waitingEvent = null;
		
			
			for (int j = 0; j < responses.length; j++){
				System.out.println("Response from player " 
									+ responses[j].fromPlayer
									+ " event: " + responses[j].eventId + " message: "
									+ responses[j].message);
			}
		} 
		
		return responses;			
	}
}
