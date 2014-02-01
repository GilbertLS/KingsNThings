package Game.Networking;

public class Response {
	public String message;
	public int fromPlayer;
	
	public Response(String message)
	{
		this.message = message;
		this.fromPlayer = -1;
	}
	
	public int castToInt()
	{
		return Integer.parseInt(message.trim());
	}
	
	public String[] castToStringArray(){
		return message.split("\\" + " ");
	}
}
