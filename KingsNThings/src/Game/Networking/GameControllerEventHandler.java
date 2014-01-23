package Game.Networking;

public class GameControllerEventHandler {
	public static Response sendEvent(Event e)
	{	
		String s = "";
		
		for(GameRouter i : GameController.servers)
		{
			Response rollResponse = i.sendEvent(new Event(e.eventId));
			System.out.println("A DICE ROLL EVENT HAS BEEN HANDLED");
			s += rollResponse.message + " ";
		}
		
		System.out.println(s);
		
		return new Response("DIE ROLL COMPLETE");				
	}
}
