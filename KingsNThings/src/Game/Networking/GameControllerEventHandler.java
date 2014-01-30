package Game.Networking;

public class GameControllerEventHandler {
	public static Response sendEvent(Event e)
	{	
		String s = "";
		
		
		for(GameRouter i : GameController.servers)
		{
			if(e.intendedPlayers[i.myID] == true)
			{
				if(e.expectsResponseEvent)
				{
					Response rollResponse = i.sendEvent(new Event(e.eventId, e.eventParams));
					s += Integer.toString(i.myID) + rollResponse.message;
				}
				else
				{
					i.sendEvent(new Event(e.eventId, e.eventParams));
				}
			}
		}
		System.out.println("HERE IS THE RETURN MESSAGE FROM ALL CLIENTS: " + s);
		return new Response(s);				
	}
}
