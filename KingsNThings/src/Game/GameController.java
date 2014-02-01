package Game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;
import Game.Networking.Event;
import Game.Networking.EventList;
import Game.Networking.GameClient;
import Game.Networking.GameControllerEventHandler;
import Game.Networking.GameRouter;
import Game.Networking.Protocol;
import Game.Networking.Response;

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
	    	
	    	//Collections.sort(servers);
	    	serverSocket.close();
	    	 
	    	numClients = servers.size();
	    	 
	 		String[] args = new String[1];
			args[0] = Integer.toString(numClients);	
			GameControllerEventHandler.sendEvent(
				new Event()
					.EventId(EventList.SET_NUM_PLAYERS)
					.EventParameters(args)
				
			);
	    	 
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
		
		playPhases();
		
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
			
			Event e = new Event()
				.EventId(EventList.ROLL_DICE)
				.IntendedPlayers(intendedPlayers)
				.ExpectsResponse(true);
			
			Response[] playerRolls = GameControllerEventHandler.sendEvent(e);
			
			for(int i=0; i<playerRolls.length; i++)
			{
				int currentIndex = playerRolls[i].fromPlayer;
				int currentRoll = Character.getNumericValue(playerRolls[i].message.charAt(0));
				
				if(currentRoll > highestRoll)
				{
					highestRoll = currentRoll;
					highestRollPlayerIndex = currentIndex;
				}
			}	
			
			for(int i=0; i<playerRolls.length; i++)
			{
				int currentIndex = playerRolls[i].fromPlayer;
				int currentRoll = Character.getNumericValue(playerRolls[i].message.charAt(0));
				
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
		args[0] = Integer.toString(numClients);
		args[1] = Integer.toString(startPlayerIndex);
		
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.SET_PLAYER_ORDER )
					.EventParameters( args )
			);
	}
	
	private void playPhases(){
		ChangePlayerOrder();
		
		PlayBattlePhase();
	}
	
	private void ChangePlayerOrder(){
		
	}
	
	private void PlayBattlePhase(){
		DoTestBattle();
	}
	
	private void DoTestBattle(){
		Thing thing = new Creature(Terrain.DESERT);
		Thing otherThing = new Creature(Terrain.JUNGLE);
		Player player = GameClient.game.gameModel.GetPlayer(1);
		Player player2 = GameClient.game.gameModel.GetPlayer(2);
		
		GameClient.game.gameModel.boardController.AddThingToTile(thing, player, 0, 0);
		GameClient.game.gameModel.boardController.AddThingToTile(otherThing, player2, 0, 0);
		GameClient.game.gameModel.boardController.AddThingToTile(thing, player, -1, -1);
		GameClient.game.gameModel.boardController.AddThingToTile(otherThing, player2, -1, -1);
		
		Response[] r = GameControllerEventHandler.sendEvent(
			new Event().EventId(EventList.GET_CONTESTED_ZONES)
				.ExpectsResponse(true)
				.IntendedPlayers(new boolean[]{ true, false, false, false})
		);
		
		
		String[] contestedZones = r[0].castToStringArray();
		for(String s : contestedZones){
			String[] coordinateString = s.split("SPLIT");
			
			GameControllerEventHandler.sendEvent(
					new Event()
						.EventId( EventList.BEGIN_BATTLE)
						.EventParameters( coordinateString )
			);
			
			Response[] r2 = GameControllerEventHandler.sendEvent(
					new Event()
						.EventId( EventList.GET_MAGIC_ROLLS)
						.EventParameters( coordinateString )
			);
			
			// Parse magic rolls and send event to inflict magic dmg
		}
		
		System.out.println("CONTESTED ZONES:" + r[0].message);
	}

	
}
