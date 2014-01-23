package Game.Networking;

public class Event {
	public int eventId;
	public String[] eventParams;
	public int[] intendedPlayers;
	public static String delimiter = "|";
	public static String elementDelimiter = ",";
	
	public Event(
		int eventId
	) {
		this.eventId = eventId;
		this.eventParams = new String[0];
		this.intendedPlayers = new int[0];
	}
	
	public Event(
		int eventId,
		String[] eventParams,
		int[] intendedPlayers
	) {
		this.eventId = eventId;
		this.eventParams = eventParams;
		this.intendedPlayers = intendedPlayers;
	}
	
	public Event(
			int eventId,
			int[] intendedPlayers
		) {
			this.eventId = eventId;
			this.eventParams = new String[0];
			this.intendedPlayers = intendedPlayers;
		}
	
	public Event(int eventId, String[] args) {
		this.eventId = eventId;
		this.eventParams = args;
		this.intendedPlayers = new int[0];
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
		int[] intendedPlayers = new int[intendedPlayersString.length];
		
		for(int i = 0 ; i < intendedPlayersString.length; i++){
			intendedPlayers[i] = Integer.parseInt(intendedPlayersString[i]);
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
		
		for (int s : intendedPlayers){
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
