package Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import Game.GameConstants.SpecialType;
import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;
import Game.Networking.Event;
import Game.Networking.EventHandler;
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
	    	
	    	for(GameRouter gr : servers){
	    		boolean[] intendedPlayers = new boolean[4];
	    		
	    		intendedPlayers[gr.myID] = true;
	    		
	    		String[] eventParams = new String[]{ "" + (gr.myID + 1) };
	    		
	    		Event e = new Event()
	    					.EventId(EventList.SET_CURRENT_PLAYER)
	    					.EventParameters(eventParams)
	    					.IntendedPlayers(intendedPlayers);
	    		
	    		GameControllerEventHandler.sendEvent(e);
	    	}
	    	 
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
		// REMOVE
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "1", "1", "-1", "-1" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "2", "2",  "-1", "-1" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Ranged", "3", "1", "-1", "-1" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Other", "4", "1", "-1", "-1" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "5", "2", "-1", "-1" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "6", "1", "0", "0" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "7", "2", "0", "0" })
		);
		//
		
		Response[] r = GameControllerEventHandler.sendEvent(
			new Event().EventId(EventList.GET_CONTESTED_ZONES)
				.ExpectsResponse(true)
				.IntendedPlayers(new boolean[]{ true, false, false, false})
		);
		
		
		String[] contestedZones = r[0].castToStringArray();
		for(String s : contestedZones){
			
			// TODO: Remove things that do not have terrain controlled
			
				
			String[] coordinates = s.split("SPLIT");
			
			GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.BEGIN_BATTLE)
					.EventParameters(coordinates)
			);
			// 1 turn
			boolean battleOver = false;
			do {
				Response[] targetedPlayers = GameControllerEventHandler.sendEvent(
						new Event()
							.EventId( EventList.CHOOSE_PLAYER )
							.EventParameters( coordinates )
							.ExpectsResponse(true)
				);
				
				int[] attackedPlayers = new int[4];
				Arrays.fill(attackedPlayers, -1);
				
				for (Response target : targetedPlayers ){
					if (target.IsNullEvent()){
						continue;
					}
					attackedPlayers[target.fromPlayer] = target.castToInt() - 1;
				}
				
				for (int i = 0; i < attackedPlayers.length; i++){
					System.out.println("Player " + i + " is attacking player " + attackedPlayers[i]);
				}
				
				String[] combatTypes = new String[]{ "Magic", "Ranged", "Other" };
				// 1 magic, ranged, other roll sequence
				for (String combatType : combatTypes) {
					String[] getRollParams = new String[3];
					
					getRollParams[0] = coordinates[0];
					getRollParams[1] = coordinates[1];
					getRollParams[2] = combatType;
					
					Response[] magicRolls = GameControllerEventHandler.sendEvent(
							new Event()
								.EventId( EventList.GET_CREATURE_ROLLS)
								.EventParameters( getRollParams )
								.ExpectsResponse(true)
					);
					
					String[] hits = new String[numClients+2];
					int numActualHits = 0;
					for (Response rolls : magicRolls){
						if (!rolls.message.equals("") && Integer.parseInt(rolls.message.trim()) > 0){
							numActualHits++;
						}
						if (rolls.IsNullEvent()){
							hits[rolls.fromPlayer] = "0";
						} else {
							hits[attackedPlayers[rolls.fromPlayer]] = rolls.message;
						}
					}
					
					hits[numClients] = coordinates[0];
					hits[numClients+1] = coordinates[1];
		 			
					if (numActualHits > 0){
						Response[] removedThingsResponse = GameControllerEventHandler.sendEvent(
							new Event()
								.EventId( EventList.INFLICT_HITS )
								.EventParameters( hits )
								.ExpectsResponse(true)
						);
						
						String[] removedThings = new String[numClients+2];
						
						for( Response response : removedThingsResponse ){
							removedThings[response.fromPlayer] = response.message;
						}
						
						removedThings[numClients] = coordinates[0];
						removedThings[numClients+1] = coordinates[1];
						
						if (GameControllerEventHandler.sendEvent(
							new Event()
								.EventId( EventList.REMOVE_THINGS )
								.EventParameters(removedThings)
								.ExpectsResponse()
						)[0].eventId == EventList.BATTLE_OVER){
							battleOver = true;
						} else {
							battleOver = false;
						}
					}
					
					if (battleOver){
						break;
					}
				} 
				
			} while (!battleOver);

			GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.BATTLE_OVER )
			);
		}
	}
	
	private void Stop(){
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		try {
			bufferRead.readLine();
		} catch (Exception e){};
	}
}
