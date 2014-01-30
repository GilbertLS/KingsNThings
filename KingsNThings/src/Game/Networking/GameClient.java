package Game.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
	public static Socket connection;
	public static GameClientController game;
	
	private boolean isReady = false;

	public GameClient(){
		
	}
	
	public void ConnectToHost( String address ){
		this.isReady = true;
		
		try { 
			connection = new Socket(address, Protocol.GAMEPORT);
	        
	        game = new GameClientController();
	        
	        System.out.println("CREATED NEW GAME CLIENT CONTROLLER");
	        
	        Runnable eventReceiver = new EventReceiver( connection );
	        Thread thread = new Thread(eventReceiver);
	        
	        thread.start();
	        
	        System.out.println("CREATING EVENT RECIEVER");	        
	        
		} catch (IOException e){
			System.out.print("Unable to connect to game");
		}
	}
}
