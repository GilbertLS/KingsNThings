package Game.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
	
	public boolean isReady = false;
	public Socket hostConnection;
	private static int playerNum = 0;
	public String name;
	private boolean gameOver = false;

	public GameClient(){
		this.name = "Player" + playerNum++;
	}
	
	public void ConnectToHost( String address ){
		this.isReady = true;
		
		try { 
			hostConnection = new Socket(address, Protocol.GAMEPORT);
			PrintWriter out = new PrintWriter(hostConnection.getOutputStream(), true);
	        BufferedReader in = new BufferedReader(
	        	new InputStreamReader(
	        		hostConnection.getInputStream()
	        	)
	        );
	        
	        Event ready = new Event(EventList.READY);
	        
	        out.println(EventList.READY);
	        while (!gameOver) {
	        	String fromServer = in.readLine();
	        	System.out.println(fromServer);
	        }
		} catch (IOException e){
			System.out.print("Unable to connect to game");
		}
	}
}
