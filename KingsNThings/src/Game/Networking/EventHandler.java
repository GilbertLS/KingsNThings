package Game.Networking;

public class EventHandler {
	public static void HandleEvent( String input ){
		Event e = Event.Destringify( input );
		HandleEvent( e );
	}
	
	public static void SendEvent( Event e ){
		String eventString = e.toString();
		
		EventSender.Send( eventString );
	}
	
	private static void HandleEvent( Event e ){
		if (e.eventId == EventList.READY){}
		else if (e.eventId == EventList.ROLL_DICE)
		{
			System.out.println("CREATING DIE ROLL RESPONSE EVENT");
			String[] args = new String[1];
			args[0] = "3";
			EventHandler.SendEvent(new Event(EventList.ROLL_DICE, args));
		}
		
	}
}
