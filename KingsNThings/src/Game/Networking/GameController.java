package Game.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameController implements Runnable {
	
	public static boolean gameStarted = false;
	public static boolean gameEnded = false; 
	public String address;
	private int numClients;
	
	public static Queue<GameRouter> servers = new ConcurrentLinkedQueue<GameRouter>();
	
	public GameController() throws UnknownHostException {
		this.address = InetAddress.getLocalHost().getHostAddress();
	}
	
	public String GetAddress(){
		return address;
	}
	
	public void StartServer(){
	    try {
	    	 ServerSocket serverSocket = new ServerSocket(Protocol.GAMEPORT);
	    	 while (!checkStartGame()) {
		        Socket clientSocket = serverSocket.accept();
		        System.out.println("A client has joined the game");
		        
		        GameRouter gameRouter = new GameRouter(clientSocket);
		        Thread thread = new Thread(gameRouter);
		        thread.start();
		        
		        while(!gameRouter.ready()){}      
	    	 }
	    	 
	    	 numClients = servers.size();
	    	 
	 		String[] args = new String[1];
			args[0] = Integer.toString(numClients);	
			GameControllerEventHandler.sendEvent(new Event(EventList.SET_NUM_PLAYERS, args));
	    	 
	    	 //GAME START
	    	 System.out.println("GAME HAS BEGUN!");
	    	 
	    	 initialSetup();
	    	 
	    	 //player setup logic
	    	 
	    	 //play game turns while game is not won
	    } catch (IOException e) {
	    	System.out.println("IO Error");
	    }
	}
	
	private boolean checkStartGame() {
		return servers.size() == 4;
	}

	public static void AddClient( GameRouter c ){
		servers.add(c);
	}
	
	public void run(){
		this.StartServer();
	}
	
	private void initialSetup() {
		//perform initial setup of the game via the model
		//call the view to update when necessary	
		
		//Vector applicablePlayers = new Vector(1);
		//for(int i=0; i<playerCount; i++)
		//{
			//applicablePlayers.add(i);
		//}
		//Response initialHexesResponse = EventHandler.sendEvent(GET_CURRENT_CUP, applicablePlayers, null);
		//initialHexesResponse.getHexTiles();
		
		//gameModel.randomizePlayingCup();
		
		//gameModel.randomizeSpecialCharacters();
		
		//gameModel.setUpHexTiles();
		
		//gameView.drawInitGame();
		
		determineInitialPlayerOrder();
		
	}
	
	private void determineInitialPlayerOrder() {
		
		//array to hold player rolls
		boolean[] playerRolling = new boolean[numClients];	
		for(int i=0; i<numClients; i++)
		{
			playerRolling[i] = true;
		}
		
		//number of player currently rolling (all roll initially)
		int numPlayersRolling = numClients;
		
		int highestRoll;
		int highestRollPlayerIndex;
		do	//roll until only a single player has the highest roll
		{		
			highestRoll = 0;
			highestRollPlayerIndex = -1;
			
			boolean[] intendedPlayers = new boolean[numClients];
			for(int i=0; i<numClients; i++)
			{
				if(playerRolling[i] != false)
					intendedPlayers[i] = true;
				else
					intendedPlayers[i] = false;
			}
			
			Event e = new Event(EventList.ROLL_DICE, new String[0],intendedPlayers, true);
			Response playerRollsString = GameControllerEventHandler.sendEvent(e);
			
			String[] responses = playerRollsString.message.split(" ");
			
			System.out.println(playerRollsString.message);
			
			for(int i=0; i<responses.length; i++)
			{
				int currentIndex = Character.getNumericValue(responses[i].charAt(0));
				int currentRoll = Character.getNumericValue(responses[i].charAt(1));
				
				if(currentRoll > highestRoll)
				{
					highestRoll = currentRoll;
					highestRollPlayerIndex = currentIndex;
				}
			}	
			
			for(int i=0; i<responses.length; i++)
			{
				int currentIndex = Character.getNumericValue(responses[i].charAt(0));
				int currentRoll = Character.getNumericValue(responses[i].charAt(1));
				
				if (currentRoll < highestRoll)
				{
					playerRolling[currentIndex] = false;
					numPlayersRolling--;
				}
			}
		}while(numPlayersRolling > 1);
		
		System.out.println("First Player is player with index: " + highestRollPlayerIndex);
		
		assignInitialPlayerOrder(highestRollPlayerIndex);
	}

	private void assignInitialPlayerOrder(int startPlayerIndex) {
		
		String[] args = new String[2];
		args[0] = Integer.toString(startPlayerIndex);
		args[1] = Integer.toString(numClients);
		
		GameControllerEventHandler.sendEvent(new Event(EventList.SET_PLAYER_ORDER, args));
		
	}
}
