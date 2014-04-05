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
			waitingEvents = new ArrayList<Response>();
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
				if (i >= numResponses) {
					System.out.println("ERROR ------- NumExpected: " + numResponses + " NumActual: " + i);
					System.out.print("ERROR ------- Responses intended for event: " + waitingEvent.eventId 
							+ " but got for event(s) ");
					for(Response r : responses) {
						System.out.print(r.eventId + " ");
					}
					System.out.println();

				} else {
					responses[i++] = reply;
				}
			}
			
			int waitingEventId = waitingEvent.eventId;
			waitingEvents = new ArrayList<Response>();
			waitingEvent = null;
		
			
			for (int j = 0; j < responses.length; j++){
				System.out.print("Response from player " 
									+ responses[j].fromPlayer
									+ " event: " + waitingEventId);
				if (responses[j].IsNullEvent()) {
					System.out.println(" returned NULL EVENT");
				} else {
					System.out.println(" message: " + responses[j].message);
				}
			}
		} 
		
		return responses;			
	}
}
