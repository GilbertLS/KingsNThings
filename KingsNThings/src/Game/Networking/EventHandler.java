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
		
	}
}
