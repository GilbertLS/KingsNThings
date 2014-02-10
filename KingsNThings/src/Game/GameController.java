package Game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Game.GameConstants.BattleTurn;
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
	    	
	    	for(GameRouter gr : servers){
	    		boolean[] intendedPlayers = new boolean[numClients];
	    		
	    		intendedPlayers[gr.myID] = true;
	    		
	    		String[] eventParams = new String[]{ "" + (gr.myID) };
	    		
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
		determineInitialPlayerOrder();
		
		initializeHexTiles();
		
		initializeCreaturesCup();
		
		initializeGold();
		
		//placeThingsOnTile(3, "Control_Marker");
		
		//placeThingsOnTile(1, "Tower");
		
		assignInitialThings();
		
		playPhases();
		
	}
	
	private void placeThingsOnTile(int numIter, String pieceToPlace) {
		for(int i=0; i<numIter; i++)
		{
			for(GameRouter gr : servers){
	    		String[] args = {""+gr.myID,pieceToPlace};
	    		
	    		Event e = new Event()
	    					.EventId(EventList.PLACE_PIECE_ON_TILE)
	    					.ExpectsResponse(true)
	    					.EventParameters(args);
	    		
	    		Response[] responses = GameControllerEventHandler.sendEvent(e);
	    		
	    		args = new String[3];
				for (int j=0; j<responses.length; j++){
					if(responses[j].fromPlayer == gr.myID)
					{
						args = responses[j].message.split(" ");
					}
				}
	    		
				boolean[] intendedPlayers = new boolean[4];
	    		for(int j=0; j<numClients; j++)
	    		{
	    			if(j == gr.myID)
	    				intendedPlayers[j] = false;
	    			else
	    				intendedPlayers[j] = true;
	    		}
	    		
	    		e = new Event()
					.EventId(EventList.HANDLE_PLACE_PIECE_ON_TILE)
					.IntendedPlayers(intendedPlayers)
	    			.EventParameters(args);
	    		
	    		GameControllerEventHandler.sendEvent(e);
			}
		}
	}

	private void initializeGold() {
		String[] args = {"" + numClients};
		
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.DISTRIBUTE_INITIAL_GOLD)
					.EventParameters(args)
			);	
	}

	private void assignInitialThings() {
    	for(GameRouter gr : servers){
    		
    		String[] eventParams = new String[]{ "" + gr.myID, "10"};
    		
    		Event e = new Event()
    					.EventId(EventList.GET_THINGS_FROM_CUP)
    					.EventParameters(eventParams);
    		
    		GameControllerEventHandler.sendEvent(e);
    	}
	}

	private void initializeHexTiles() {
		String[] initializeHexTilesStrings = GameModel.initializeHexTiles().split(" ");
		
		String[] args = new String[2];
		args[0] = initializeHexTilesStrings[0];
		args[1] = initializeHexTilesStrings[1];
		
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.SET_HEX_TILES )
					.EventParameters( args )
			);
		
	}
	
	private void initializeCreaturesCup() {
		String initializeCreaturesString = GameModel.initializeCreatures();
		
		String[] args = new String[1];
		args[0] = initializeCreaturesString;
		
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.SET_CREATURES )
					.EventParameters( args )
			);
		
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
		distributeIncome();
		
		//recruitThings();
		
		PlayBattlePhase();
		
		ChangePlayerOrder();
	}
	
	private void recruitThings() {
		for(GameRouter gr: servers)
		{
			String[] args = {"" + numClients};
			
			//ask for number of paid recruits
			Response[] responses = GameControllerEventHandler.sendEvent(
					new Event()
					.EventId(EventList.DETERMINE_NUM_PAID_THINGS)
					.EventParameters(args)
					.ExpectsResponse(true)
					);
			
			args = new String[2];
			for (int j=0; j<responses.length; j++){
				if(responses[j].fromPlayer == gr.myID)
				{
					args[0] = responses[j].message.trim();
				}
			}
			
			//ask for number of recruits to trade for
			responses = GameControllerEventHandler.sendEvent(
					new Event()
					.EventId(EventList.DETERMINE_NUM_TRADE_THINGS)
					.EventParameters(args)
					.ExpectsResponse(true)
					);
			
			for (int j=0; j<responses.length; j++){
				if(responses[j].fromPlayer == gr.myID)
				{
					args[1] = responses[j].message.trim();
				}
			}
			
			//distribute recruits
			responses = GameControllerEventHandler.sendEvent(
					new Event()
					.EventId(EventList.DISTRIBUTE_RECRUITS)
					.EventParameters(args)
					);
			
			boolean[] intendedPlayers = new boolean[4];
    		for(int j=0; j<numClients; j++)
    		{
    			if(j == gr.myID)
    				intendedPlayers[j] = false;
    			else
    				intendedPlayers[j] = true;
    		}
    		
    		GameControllerEventHandler.sendEvent(new Event()
				.EventId(EventList.HANDLE_DISTRIBUTE_RECRUITS)
				.IntendedPlayers(intendedPlayers)
				.EventParameters(args)
			);
			
			//place things
			
			//handle place things
			
			//deal with excess in player rack
			
			//handle dealing with excess in player rack
		}
		

	}

	private void distributeIncome() {
		String[] args = {"" + numClients};
		
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.AWARD_INCOME)
					.EventParameters(args)
			);			
	}

	private void ChangePlayerOrder(){
		
	}
	
	private void PlayBattlePhase(){
		DoBattle();
		//DoTestBattle();
	}
	
	private void DoTestBattle(){
		AddTestThingsToTile();
		
		boolean[] playerOne = new boolean[]{ true, false, false, false };
		
		Response[] r = GameControllerEventHandler.sendEvent(
			new Event().EventId(EventList.GET_CONTESTED_ZONES)
				.ExpectsResponse(true)
				.IntendedPlayers(playerOne)
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
			
			boolean battleOver = false;
			do {
				Response[] targetedPlayers = GameControllerEventHandler.sendEvent(
						new Event()
							.EventId( EventList.CHOOSE_PLAYER )
							.EventParameters( coordinates )
							.ExpectsResponse(true)
							.IntendedPlayers(playerOne)
				);
				
				int[] attackedPlayers = new int[4];
				Arrays.fill(attackedPlayers, -1);
				
				for (Response target : targetedPlayers ){
					if (target.IsNullEvent()){
						continue;
					}
					attackedPlayers[target.fromPlayer] = target.castToInt();
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
					
					Response[] playerRolls = GameControllerEventHandler.sendEvent(
							new Event()
								.EventId( EventList.GET_CREATURE_ROLLS)
								.EventParameters( getRollParams )
								.ExpectsResponse(true)
					);
					
					String[] hits = new String[numClients+2];
					int numActualHits = 0;
					for (Response rolls : playerRolls){
						if (!rolls.message.trim().equals("") && Integer.parseInt(rolls.message.trim()) > 0){
							numActualHits++;
						}
						if (rolls.IsNullEvent()){
							hits[rolls.fromPlayer] = "0";
						} else {
							hits[attackedPlayers[rolls.fromPlayer]] = rolls.message;
						}
					}
					
					/* bot roll */
					int tileX = Integer.parseInt(coordinates[0]);
					int tileY = Integer.parseInt(coordinates[1]);
					
					List<Integer> players = GameClient.game.gameModel.boardController.PlayersOnTile(tileX, tileY);
					
					int botNum = -1;
					
					for (Integer i : players){
						if (i != 0){
							botNum = i;
						}
					}
					
					Player bot = GameClient.game.gameModel.GetPlayer(botNum);
					int botRolls = 0;
					
					
					for (Thing t : GameClient.game.gameModel.boardController.GetTile(tileX, tileY).GetThings(bot)){
						if ( !t.IsCombatant() ){
							continue;
						}
						
						BattleTurn turn;
						
						if(combatType.equals("Magic")){ turn = BattleTurn.MAGIC; }
						else if(combatType.equals("Ranged")){ turn = BattleTurn.RANGED; }
						else { turn = BattleTurn.OTHER; }
						
						botRolls += ((Combatant)t).GetCombatRoll(turn, false);
						if (botRolls > 0){ numActualHits++; }
					}
					
					hits[0] = "" + botRolls;
					
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
						
						/* REMOVE BOT THINGS */
						String botHitsTakenString = hits[botNum];
						int botHitsTaken = Integer.parseInt(botHitsTakenString.trim());
						String thingsToRemoveEvent = "";
						
						if (botHitsTaken > 0){
							HexTile currTile = GameClient.game.gameModel.boardController.GetTile(tileX, tileY);
							ArrayList<Thing> things =  currTile.GetThings(bot);
							
							for (int i = 0; i < botHitsTaken; i++){
								if (i < things.size()) {
									thingsToRemoveEvent += things.get(i).thingID + " ";
								} 
							}
							System.out.println(thingsToRemoveEvent);
							
						}
						
						removedThings[botNum] = thingsToRemoveEvent; 
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
	
	private void DoBattle(){
		// REMOVE
		AddTestThingsToTile();
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
					attackedPlayers[target.fromPlayer] = target.castToInt();
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
					
					Response[] playerRolls = GameControllerEventHandler.sendEvent(
							new Event()
								.EventId( EventList.GET_CREATURE_ROLLS)
								.EventParameters( getRollParams )
								.ExpectsResponse(true)
					);
					
					String[] hits = new String[numClients+2];
					int numActualHits = 0;
					for (Response rolls : playerRolls){
						if (!rolls.message.trim().equals("") && Integer.parseInt(rolls.message.trim()) > 0){
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
	private void AddTestThingsToTile(){
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "1", "0", "-1", "-1" })
		);
		/*GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "8", "0", "-1", "-1" })
		);*/
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "2", "1",  "-1", "-1" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Ranged", "3", "0", "-1", "-1" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Other", "4", "0", "-1", "-1" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "5", "1", "-1", "-1" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "6", "0", "0", "0" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "7", "2", "0", "0" })
		);
		GameControllerEventHandler.sendEvent(
				new Event().EventId(EventList.ADD_THING_TO_TILE).EventParameters( new String[]{ "Magic", "9", "2", "0", "0" })
		);
	}
}
