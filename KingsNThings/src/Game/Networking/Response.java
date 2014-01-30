package Game.Networking;

public class Response {
	public String message;
	
	public Response(String message)
	{
		this.message = message;
	}
	
	public int castToInt()
	{
		return Integer.parseInt(message.trim());
	}
}
