package Game.Networking;

public class Event {
	public int eventId = -1;
	public String[] eventParams = new String[0];
	public boolean[] intendedPlayers = new boolean[]{ true, true, true, true };
	public static String delimiter = "|";
	public static String elementDelimiter = ",";
	public boolean expectsResponseEvent = false;
	
	public Event(){}
	
	public Event EventId(int eventId) {
		this.eventId = eventId;
		return this;
	}
	
	public Event EventParameters(String[] eventParams){
		String[] copy = new String[eventParams.length];
		for(int i = 0; i < eventParams.length; i++) {
			copy[i] = eventParams[i];
		}
		
		this.eventParams = copy;
		return this;
	}
	
	public Event EventParameter(String eventParam){
		this.eventParams = new String[]{ eventParam };
		return this;
	}
	
	public Event IntendedPlayers(boolean[] intendedPlayers){
		this.intendedPlayers = intendedPlayers;
		return this;
	}
	
	public Event ExpectsResponse(boolean expectsResponse){
		this.expectsResponseEvent = expectsResponse;
		return this;
	}
	
	public Event ExpectsResponse(){
		this.expectsResponseEvent = true;
		return this;
	}

	public static Event Destringify (
		String stringifiedEvent
	){
		String[] eventFields = stringifiedEvent.split("\\" + delimiter);
		
		int eventId = -1;
		String[] eventParams = new String[0];
		String[] intendedPlayersString = new String[0];
		boolean expectsResponse = false;
		
		if (eventFields.length > 0) {
			eventId = Integer.parseInt(eventFields[0]);
		} 
		if (eventFields.length > 1) {
			eventParams = eventFields[1].split("\\" + elementDelimiter);
		}
		if (eventFields.length > 2) {
			intendedPlayersString = eventFields[2].split("\\" + elementDelimiter);
		}
		if (eventFields.length > 3){
			expectsResponse = Boolean.parseBoolean(eventFields[3]);
		}
		
		boolean[] intendedPlayers = new boolean[intendedPlayersString.length];
		
		for(int i = 0 ; i < intendedPlayersString.length; i++){
			intendedPlayers[i] = Boolean.parseBoolean(intendedPlayersString[i]);
		}
		
		Event e = new Event()
					.EventId(eventId)
					.EventParameters(eventParams)
					.IntendedPlayers(intendedPlayers)
					.ExpectsResponse(expectsResponse);
		
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
		
		ret += expectsResponseEvent + delimiter;
		
		return ret;
	}
	
	public String getEventParameters()
	{
		String s = "";
		for(int i=0; i<eventParams.length; i++)
		{
			s += eventParams[i] + " ";
		}
		
		return s.trim();
	}
}
