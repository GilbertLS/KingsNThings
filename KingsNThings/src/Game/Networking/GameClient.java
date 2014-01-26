package Game.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Game.Game;

public class GameClient {
	public static Socket connection;
	public static Game game;
	
	public String name;
	
	private boolean isReady = false;
	
	private static int playerNum = 0;

	public GameClient(){
		this.name = "Player" + playerNum++;
	}
	
	public void ConnectToHost( String address ){
		this.isReady = true;
		
		try { 
			connection = new Socket(address, Protocol.GAMEPORT);
			//PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
	        //BufferedReader in = new BufferedReader(
	        //	new InputStreamReader(
	        //			connection.getInputStream()
	        //	)
	        //);
	        
	        //Event ready = new Event(EventList.READY);
	        
	        //out.println(EventList.READY);
	        
        	//String fromServer = in.readLine();
        	////////////////////////////////////// GAME START
        	//System.out.println(fromServer);
        	
        	//out.close();
	       // in.close();
	        
	        Runnable eventReceiver = new EventReceiver( connection );
	        Thread thread = new Thread(eventReceiver);
	        
	        thread.start();
	        
	        System.out.println("CREATING EVENT RECIEVER");
	        
	        //create GameClientController here
	        //game = new Game(3);
	       // game.playGame();
	        
	        //send ready
		} catch (IOException e){
			System.out.print("Unable to connect to game");
		}
	}
}
