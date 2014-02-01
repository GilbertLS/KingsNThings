package Game.Networking;

import Game.GameController;

public class GameControllerEventHandler {
	public static Response[] sendEvent(Event e)
	{
		
		int numResponses = 0;
		
		for(GameRouter i : GameController.servers){
			if(e.intendedPlayers[i.myID] == true && e.expectsResponseEvent){
				numResponses++;
			}
		}
		
		Response[] responses = new Response[numResponses];
		
		int i = 0;
		
		for(GameRouter gr : GameController.servers)
		{
			if(e.intendedPlayers[gr.myID] == true)
			{
				if(e.expectsResponseEvent)
				{
					
					Response rollResponse = gr.sendEvent(
							new Event()
								.EventId(e.eventId)
								.EventParameters(e.eventParams)
								.IntendedPlayers(e.intendedPlayers)
								.ExpectsResponse(e.expectsResponseEvent)
					);
					
					rollResponse.fromPlayer = gr.myID;
					responses[i++] = rollResponse;
				}
				else
				{
					gr.sendEvent(new Event()
						.EventId( e.eventId )
						.EventParameters( e.eventParams )
					);
				}
			}
		}
		for (int j = 0; j < responses.length; j++){
			System.out.println("Response from player: " 
								+ responses[j].fromPlayer
								+ " response: "
								+ responses[j].message);
		}
		return responses;			
	}
}
