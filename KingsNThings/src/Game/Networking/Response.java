package Game.Networking;

public class Response {
	public String message;
	public int fromPlayer;
	public int eventId;
	
	public Response(Event e)
	{
		this.message = e.getEventParameters();
		this.fromPlayer = -1;
		this.eventId = e.eventId;
	}
	
	public int castToInt()
	{
		return Integer.parseInt(message.trim());
	}
	
	public String[] castToStringArray(){
		return message.split("\\" + " ");
	}
}
