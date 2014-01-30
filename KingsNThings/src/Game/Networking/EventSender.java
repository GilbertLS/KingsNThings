package Game.Networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class EventSender implements Runnable {
	
	private Socket connection;
	private String sendString;
	
	public EventSender(
		Socket c,
		String s
	){
		connection = c;
		sendString = s;
	}
	
	public static void Send( String sendString ){
		Runnable eventSender = new EventSender( 
			GameClient.connection,
			sendString
		);
		
		Thread thread = new Thread(eventSender);
		
		thread.start();
	}
	
	public void run(){
		try { 
			PrintWriter out = new PrintWriter(
				connection.getOutputStream(), true
			);
			
			out.println(sendString);
		} catch (IOException e){}
	}
}
