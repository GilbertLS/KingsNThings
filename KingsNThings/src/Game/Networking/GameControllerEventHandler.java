package Game.Networking;

public class GameControllerEventHandler {
	public static Response sendEvent(Event e)
	{	
		String s = "";
		
		for(GameRouter i : GameController.servers)
		{
			int rollResponse = i.sendEvent(new Event(e.eventId)).castToInt();
			System.out.println("A DICE ROLL EVENT HAS BEEN HANDLED");
			s += rollResponse + " ";
		}
		
		System.out.println(s);
		
		return new Response("DIE ROLL COMPLETE");				
	}
}
