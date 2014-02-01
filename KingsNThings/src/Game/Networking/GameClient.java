package Game.Networking;

import gui.GameView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Game.GameClientController;

public class GameClient {
	public static Socket connection;
	public static GameClientController game;
	
	public GameView gameView;
	
	private boolean isReady = false;

	public GameClient(GameView g){
		gameView = g;
	}
	
	public void ConnectToHost( String address ){
		this.isReady = true;
		
		try { 
			connection = new Socket(address, Protocol.GAMEPORT);
	        
	        game = new GameClientController(gameView);
	        
	        System.out.println("CREATED NEW GAME CLIENT CONTROLLER");
	        
	        Runnable eventReceiver = new EventReceiver( connection );
	        Thread thread = new Thread(eventReceiver);
	        
	        thread.start();
	        try {
	        	Thread.sleep(2000);
	        }
	        catch (InterruptedException e)
	        {
	        	
	        }
	        
	        EventHandler.HandleEvent((new Event(EventList.TEST_EVENT)).toString());
	        
	        System.out.println("CREATING EVENT RECIEVER");	        
	        
		} catch (IOException e){
			System.out.print("Unable to connect to game");
		}
	}
}
