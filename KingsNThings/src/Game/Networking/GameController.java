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
	    	 
	    	 this.numClients = servers.size();
	    	 
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
		return servers.size() == 2;
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
		int[] playerRolls = new int[numClients];	
		for(int i=0; i<numClients; i++)
		{
			playerRolls[i] = 0;
		}
		
		//number of player currently rolling (all roll initially)
		int playersRolling = numClients;
		
		int highestRoll;
		int highestRollPlayerIndex;
		//do	//roll until only a single player has the highest roll
		//{		
			int[] intendedPlayers = new int[numClients];
			for(int i=0; i<playersRolling; i++)
			{
				if(playerRolls[i] != -1)
					intendedPlayers[i] = i;
					
			}
			Event e = new Event(EventList.ROLL_DICE, intendedPlayers);
			Response playerRollsString = GameControllerEventHandler.sendEvent(e);
			System.out.println(playerRollsString.message);
			//String[] returnArgs = playerRollsString.getString();
			//parse returnArgs
			
			//store index of player with the highest roll, along with the roll's value
			/*highestRoll = 0;
			highestRollPlayerIndex = -1;
			for(int i=0; i<playerCount; i++)
			{
				if(playerRolls[i] != -1)
				{
					if(playerRolls[i] < highestRoll)
					{
						playerRolls[i] = -1;
						playersRolling--;
					}
					else	//otherwise, they are the new highest roll(if its the same, they will both roll again)
					{
						highestRoll = playerRolls[i];
						highestRollPlayerIndex = i;
					}
				}
			}*/
		//}while(playersRolling > 1);
		
		//String[] args = new String[1];
		//args[0] = 
		//EventHandler.sendEvent(GameConstants.UPDATE_PLAYER_ORDER, null, null);
	}
}
