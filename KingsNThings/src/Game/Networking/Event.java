package Game.Networking;

public class Event {
	public int eventId;
	public String[] eventParams;
	public boolean[] intendedPlayers;
	public static String delimiter = "|";
	public static String elementDelimiter = ",";
	public boolean expectsResponseEvent;
	
	public Event(
		int eventId
	) {
		this.eventId = eventId;
		this.eventParams = new String[0];
		this.intendedPlayers = new boolean[4];
		this.expectsResponseEvent = false;
		
		for(int i=0; i<4; i++){intendedPlayers[i] = true;}
	}
	
	public Event(
		int eventId,
		String[] eventParams,
		boolean[] intendedPlayers
	) {
		this.eventId = eventId;
		this.eventParams = eventParams;
		this.intendedPlayers = intendedPlayers;
		this.expectsResponseEvent = false;
	}
	
	public Event(
			int eventId,
			boolean[] intendedPlayers
		) {
			this.eventId = eventId;
			this.eventParams = new String[0];
			this.intendedPlayers = intendedPlayers;
			this.expectsResponseEvent = false;
		}
	
	public Event(int eventId, String[] args) {
		this.eventId = eventId;
		this.eventParams = args;
		this.intendedPlayers = new boolean[4];
		this.expectsResponseEvent = false;
		
		for(int i=0; i<4; i++){intendedPlayers[i] = true;}
	}
	
	public Event(
			int eventId,
			String[] eventParams,
			boolean[] intendedPlayers,
			boolean expectsResponseEvent
		) {
			this.eventId = eventId;
			this.eventParams = eventParams;
			this.intendedPlayers = intendedPlayers;
			this.expectsResponseEvent = expectsResponseEvent;
		}

	public static Event Destringify (
		String stringifiedEvent
	){
		String[] eventFields = stringifiedEvent.split("\\" + delimiter);
		
		int eventId = -1;
		String[] eventParams = new String[0];
		String[] intendedPlayersString = new String[0];
		
		if (eventFields.length > 0) {
			eventId = Integer.parseInt(eventFields[0]);
		} 
		if (eventFields.length > 1) {
			eventParams = eventFields[1].split("\\" + elementDelimiter);
		}
		if (eventFields.length > 2) {
			intendedPlayersString = eventFields[2].split("\\" + elementDelimiter);
		}
		
		boolean[] intendedPlayers = new boolean[intendedPlayersString.length];
		
		for(int i = 0 ; i < intendedPlayersString.length; i++){
			intendedPlayers[i] = Boolean.parseBoolean(intendedPlayersString[i]);
		}
		
		Event e = new Event(
			eventId,
			eventParams,
			intendedPlayers
		);
		
		return e;
	}
	
	public String toString(){
		String ret = eventId + delimiter;
		
		for (String s : eventParams){
			ret += s + elementDelimiter;
		}
		
		ret += delimiter;
		
		for (boolean s : intendedPlayers){
			ret += s + elementDelimiter;
		}
		
		ret += delimiter;
		
		return ret;
	}
	
	public String getEventParameters()
	{
		String s = "";
		for(int i=0; i<eventParams.length; i++)
		{
			s += eventParams[i] + " ";
		}
		
		return s;
	}
}
