package Game.Networking;

public class Event {
	public int eventId;
	public String[] eventParams;
	public String[] intendedPlayers;
	public static String delimiter = "|";
	public static String elementDelimiter = ",";
	
	public Event(
		int eventId
	) {
		this.eventId = eventId;
		this.eventParams = new String[0];
		this.intendedPlayers = new String[0];
	}
	
	public Event(
		int eventId,
		String[] eventParams,
		String[] intendedPlayers
	) {
		this.eventId = eventId;
		this.eventParams = eventParams;
		this.intendedPlayers = intendedPlayers;
	}
	
	public static Event Destringify (
		String stringifiedEvent
	){
		String[] eventFields = stringifiedEvent.split("\\" + delimiter);
		
		int eventId = -1;
		String[] eventParams = new String[0];
		String[] intendedPlayers = new String[0];
		
		if (eventFields.length > 0) {
			eventId = Integer.parseInt(eventFields[0]);
		} 
		if (eventFields.length > 1) {
			eventParams = eventFields[1].split("\\" + elementDelimiter);
		}
		if (eventFields.length > 2) {
			intendedPlayers = eventFields[2].split("\\" + elementDelimiter);
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
		
		for (String s : intendedPlayers){
			ret += s + elementDelimiter;
		}
		
		ret += delimiter;
		
		return ret;
	}
}
