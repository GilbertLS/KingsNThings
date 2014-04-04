package Game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import Game.GameConstants.BattleTurn;
import Game.Phases.Phase;
import Game.Networking.Event;
import Game.Networking.EventList;
import Game.Networking.GameClient;
import Game.Networking.GameControllerEventHandler;
import Game.Networking.GameRouter;
import Game.Networking.GameServer;
import Game.Networking.Protocol;
import Game.Networking.Response;

public class GameController {
	
	public static boolean gameStarted = false;
	public static boolean gameEnded = false; 
	//public String address;
	public static int numClients;
	private static Phase currentPhase = Phase.NONE;
	private static boolean changedPhase = false;
	
	public static void SetPhase(Phase phase) {
		currentPhase = phase;
		changedPhase = true;
	}
	
	public GameController(
			int numberOfClients
	) {
		numClients = numberOfClients;
	}
	
	public void StartGame(){

    	for(GameRouter gr : GameServer.servers){
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
    	 
    	GameController.currentPhase = Phase.SETUP;
    	initialSetup();
    	 
    	//play game turns while game is not won
    	playPhases();
	}	
	
	private void initialSetup() {
		/*begin need to happen even with DEVMODE:*/
		determineInitialPlayerOrder();
		
		initializeHexTiles();
		
		randomizeHexTiles();
		
		randomizePlayingCup();
		
		intializeSpecialCharacters();
		/*end need to happen even with DEVMODE*/
		
		initializeGold();
		
		
		
		/*begin need to happen even with DEVMODE:*/
		placeThingsOnTile(1, "Control_Marker");
		
		//revealHexTiles();
		
		//allowTileSwap();
		/*end need to happen even with DEVMODE*/
		
		
		
		if(GameController.currentPhase == Phase.SETUP){ placeThingsOnTile(2, "Control_Marker"); }
		
		if(GameController.currentPhase == Phase.SETUP){ placeThingsOnTile(1, "Tower"); }
		
		if(GameController.currentPhase == Phase.SETUP){ assignInitialThings(); }
		
		if(GameController.currentPhase == Phase.SETUP){ tradeInitialThings(); }
		
		if(GameController.currentPhase == Phase.SETUP){ playThings(); }
		
	}
	
	private void allowTileSwap() {		
		Response[] responses = GameControllerEventHandler.sendEvent(
				new Event()
					.EventId(EventList.CHECK_TILE_SWAP)
					.ExpectsResponse(true)
			);		
	}

	private void revealHexTiles() {
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId(EventList.REVEAL_HEX_TILES)
			);
	}

	private void randomizeHexTiles() {
		//ask first player to randomize unused Tiles
		boolean[] intendedPlayers = new boolean[numClients];
		intendedPlayers[0] = true;

		Response[] responses = GameControllerEventHandler.sendEvent(
				new Event()
					.EventId(EventList.RANDOMIZE_UNUSED_TILES)
					.IntendedPlayers(intendedPlayers)
					.ExpectsResponse(true)
			);
		
		//get unused tile order response
		String thingIDs = "";
		for(Response r: responses)
		{
			if(r.fromPlayer == 0)
				thingIDs = r.message;	
		}
		
		//update remaining clients with new tile order
		for(int i=1; i<numClients; i++)
		{
			intendedPlayers[i] = true;
		}
		intendedPlayers[0] = false;
		
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId(EventList.UPDATE_UNUSED_TILES)
					.EventParameter(thingIDs)
					.IntendedPlayers(intendedPlayers)
			);
	}

	private void tradeInitialThings() {
		for(GameRouter gr: GameServer.servers)
		{			
			String[] args  = {""+gr.myID, "" + true, ""};
			Response[] responses = GameControllerEventHandler.sendEvent(
					new Event()
						.EventId(EventList.TRADE_THINGS)
						.ExpectsResponse(true)
						.EventParameters(args)
				);		
			
			for(Response r: responses)
			{
				if(r.fromPlayer == gr.myID)
					args[2] = r.message;	
			}
			
			GameControllerEventHandler.sendEvent(
					new Event()
						.EventId(EventList.HANDLE_TRADE_THINGS)
						.EventParameters(args)
				);	
		}
	}

	private void intializeSpecialCharacters() {
		//ask first player to randomize special characters
		boolean[] intendedPlayers = new boolean[numClients];
		intendedPlayers[0] = true;

		Response[] responses = GameControllerEventHandler.sendEvent(
				new Event()
					.EventId(EventList.RANDOMIZE_SPECIAL_CHARACTERS)
					.IntendedPlayers(intendedPlayers)
					.ExpectsResponse(true)
			);
		
		//get new order response
		String thingIDs = "";
		for(Response r: responses)
		{
			if(r.fromPlayer == 0)
				thingIDs = r.message;	
		}
		
		//update all clients with new order
		for(int i=0; i<numClients; i++)
		{
			intendedPlayers[i] = true;
		}
		
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId(EventList.CREATE_SPECIAL_CHARACTERS)
					.EventParameter(thingIDs)
					.IntendedPlayers(intendedPlayers)
			);
		
	}

	private void playThings() {
		for(GameRouter gr: GameServer.servers)
		{
			boolean playDone = false;
			
			do
			{
				String[] args  = {""+gr.myID, ""};
	    		Event e = new Event()
	    				.EventId(EventList.PLAY_THINGS)
	    				.ExpectsResponse(true)
	    				.EventParameters(args);
	    		
	    		Response[] responses = GameControllerEventHandler.sendEvent(e);
	
				for (int j=0; j<responses.length; j++){
					if(responses[j].fromPlayer == gr.myID)
					{
						String[] responseStrings = responses[j].message.split(" ");
						
						if(responseStrings.length == 2)
							args[1] = responseStrings[1];

						playDone = Boolean.parseBoolean(responseStrings[0]);
					}
				}
	    		
				if(!args[1].equals(""))
				{
		    		e = new Event()
						.EventId(EventList.HANDLE_PLAY_THINGS)
		    		    .EventParameters(args);
		    		
		    		GameControllerEventHandler.sendEvent(e);
				}
			}while(!playDone);
		}
		handleRackOverload();
	}

	private void handleRackOverload() {
		for(GameRouter gr: GameServer.servers)
		{
			GameControllerEventHandler.sendEvent(new Event()
			.EventId(EventList.HANDLE_RACK_OVERLOAD)
			.EventParameter(""+gr.myID));
		}
	}

	private void placeThingsOnTile(int numIter, String pieceToPlace) {
		for(int i=0; i<numIter; i++)
		{
			for(GameRouter gr : GameServer.servers){
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
    	for(GameRouter gr : GameServer.servers){
    		
    		String[] eventParams = new String[]{ "" + gr.myID, "10"};
    		
    		Event e = new Event()
    					.EventId(EventList.GET_THINGS_FROM_CUP)
    					.EventParameters(eventParams);
    		
    		GameControllerEventHandler.sendEvent(e);
    	}
	}

	private void initializeHexTiles() {
		String initializeHexTilesStrings = GameModel.initializeHexTiles();
		
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.SET_HEX_TILES )
					.EventParameter(initializeHexTilesStrings)
			);
		
	}
	
	private void randomizePlayingCup() {
		
		//ask first player to randomize playing cup
		boolean[] intendedPlayers = new boolean[numClients];
		intendedPlayers[0] = true;
		

		Response[] responses = GameControllerEventHandler.sendEvent(
				new Event()
					.EventId(EventList.RANDOMIZE_THINGS)
					.IntendedPlayers(intendedPlayers)
					.ExpectsResponse(true)
			);
		
		//get new cup order response
		String thingIDs = "";
		for(Response r: responses)
		{
			if(r.fromPlayer == 0)
				thingIDs = r.message;	
		}
		
		
		//update remaining clients with new cup order
		for(int i=1; i<numClients; i++)
		{
			intendedPlayers[i] = true;
		}
		intendedPlayers[0] = false;
		
		
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId(EventList.UPDATE_CUP_ORDER)
					.EventParameter(thingIDs)
					.IntendedPlayers(intendedPlayers)
			);
	}

	private void determineInitialPlayerOrder() {
		
		//array to hold whether player is currently rolling
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
				//send events to participating players
				intendedPlayers[i] = playerRolling[i];
			}
			
			Event e = new Event()
				.EventId(EventList.ROLL_TWO_DICE)
				.IntendedPlayers(intendedPlayers)
				.ExpectsResponse(true);
			
			Response[] playerRolls = GameControllerEventHandler.sendEvent(e);
			
			//find highest roll and player index
			for(int i=0; i<numClients; i++)
			{
				int currentIndex = playerRolls[i].fromPlayer;
				int currentRoll = Integer.parseInt(playerRolls[i].message);
				
				if(currentRoll > highestRoll)
				{
					highestRoll = currentRoll;
					highestRollPlayerIndex = currentIndex;
				}
			}	
			
			//remove players who did not tie for highest roll
			for(int i=0; i<numClients; i++)
			{
				int currentIndex = playerRolls[i].fromPlayer;
				int currentRoll = Integer.parseInt(playerRolls[i].message);
				
				if (currentRoll < highestRoll)
				{
					playerRolling[currentIndex] = false;
					numPlayersRolling--;
				}
			}
		}while(numPlayersRolling > 1);
		
		System.out.println("First Player is player with index: " + highestRollPlayerIndex);
		
		assignInitialPlayerOrder(highestRollPlayerIndex);
		sortServersByOrder();
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
		boolean gameWon = false;
		if (currentPhase == Phase.SETUP) {
    		currentPhase = Phase.RECRUIT_SPECIAL_CHARACTERS; 
    	}
		
		do
		{
			incrementCitadelRounds();
			if (currentPhase == Phase.RECRUIT_SPECIAL_CHARACTERS) {
				distributeIncome();
			}
			
			if (currentPhase == Phase.RECRUIT_SPECIAL_CHARACTERS) { 
				if (changedPhase) { changedPhase = false; }
				recruitSpecialCharacter();
				if (!changedPhase) { currentPhase = Phase.RECRUIT_THINGS; }
			}
			if (currentPhase == Phase.RECRUIT_THINGS) {
				if (changedPhase) { changedPhase = false; }
				recruitThings();
				tradeThings();
				if (!changedPhase) { currentPhase = Phase.PLAY_THINGS; }
			}
			
			if (currentPhase == Phase.PLAY_THINGS) {
				if (changedPhase) { changedPhase = false; }
				playThings();
				if (!changedPhase) { currentPhase = Phase.MOVE_THINGS; }
			}
			
			//randomEventsPhase();
			
			if (currentPhase == Phase.MOVE_THINGS) {
				if (changedPhase) { changedPhase = false; }
				moveThings();
				if (!changedPhase) { currentPhase = Phase.BATTLE; }
			}
			if (currentPhase == Phase.BATTLE) {
				if (changedPhase) { changedPhase = false; }
				PlayBattlePhase();
				gameWon = checkWin();
				if (!changedPhase) { currentPhase = Phase.CONSTRUCTION; }
			}

			if (currentPhase == Phase.CONSTRUCTION) {
				if (changedPhase) { changedPhase = false; }
				playConstructionPhase();
				gameWon = checkWin();
				if (!changedPhase) { currentPhase = Phase.RECRUIT_SPECIAL_CHARACTERS; }
			}
			
			performSpecialPowersPhase();
		
			ChangePlayerOrder();
		
		} while(!gameEnded);
	}
	
	private void performSpecialPowersPhase() {
		for(GameRouter gr: GameServer.servers){
			GameControllerEventHandler.sendEvent(
					new Event()
						.EventId( EventList.PERFORM_SPECIAL_POWERS)
						.EventParameter(""+gr.myID)
				);	
		}
	}

	private void incrementCitadelRounds() {
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.INCREMENT_CITADEL_ROUNDS)
			);	
	}

	private boolean checkWin() {
		String[] args = {"",""};
		
		boolean[] intendedPlayers = new boolean[numClients];
		intendedPlayers[0] = true;
		
		//ask first player to determine whether a win has occured
		Response[] responses = GameControllerEventHandler.sendEvent(
				new Event()
					.EventId(EventList.CHECK_WIN)
					.ExpectsResponse(true)
					.IntendedPlayers(intendedPlayers)
			);	
		
		for(Response r: responses)
		{
			if(r.fromPlayer == 0)
				args = r.message.split("SPLIT");
		}
		
		boolean ret = Boolean.parseBoolean(args[0]);
		
		if(ret)
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId(EventList.HANDLE_WIN)
					.EventParameter(args[1])
			);
		
		return ret;
	}

	private void tradeThings() {
		for(GameRouter gr: GameServer.servers)
		{			
			String[] args  = {""+gr.myID, "" + false, ""};
			Response[] responses = GameControllerEventHandler.sendEvent(
					new Event()
						.EventId(EventList.TRADE_THINGS)
						.ExpectsResponse(true)
						.EventParameters(args)
				);		
			
			for(Response r: responses)
			{
				if(r.fromPlayer == gr.myID)
					args[2] = r.message;	
			}
			
			GameControllerEventHandler.sendEvent(
					new Event()
						.EventId(EventList.HANDLE_TRADE_THINGS)
						.EventParameters(args)
				);	
		}
	}

	private void playConstructionPhase() {
		Event e = new Event()
			.EventId(EventList.DO_CONSTRUCTION)
			.ExpectsResponse(true);
				    		
		GameControllerEventHandler.sendEvent(e);	
			
		e = new Event()
		.EventId(EventList.CLEAR_CONSTRUCTION);
	
		GameControllerEventHandler.sendEvent(e);
	}
	
	private void recruitSpecialCharacter() {
		for(GameRouter gr: GameServer.servers)
		{
			String arg = ""+ gr.myID;
			Event e = new Event()
				.EventId(EventList.RECRUIT_CHARACTER)
				.ExpectsResponse(true)
				.EventParameter(arg);
				    		
			GameControllerEventHandler.sendEvent(e);	
		}
	}

	private void moveThings() {
		for(GameRouter gr: GameServer.servers)
		{
			boolean moveDone = false;
			
			do
			{
				String[] args  = {""+gr.myID, ""};
	    		Event e = new Event()
	    				.EventId(EventList.MOVE_THINGS)
	    				.ExpectsResponse(true)
	    				.EventParameters(args);
	    		
	    		Response[] responses = GameControllerEventHandler.sendEvent(e);
	
				for (int j=0; j<responses.length; j++){
					if(responses[j].fromPlayer == gr.myID)
					{
						String[] responseStrings = responses[j].message.split(" ");
						
						if(responseStrings.length == 2)
							args[1] = responseStrings[1];

						moveDone = Boolean.parseBoolean(responseStrings[0]);
					}
				}
	    		
				if(!args[1].equals(""))
				{
		    		e = new Event()
						.EventId(EventList.HANDLE_MOVE_THINGS)
		    		    .EventParameters(args);
		    		
		    		GameControllerEventHandler.sendEvent(e);
				}
			}while(!moveDone);
			
			boolean[] intendedPlayers = new boolean[numClients];
			intendedPlayers[gr.myID] = true;
			
			Event e = new Event()
						.EventId(EventList.CLEAR_THING_MOVES)
						.IntendedPlayers(intendedPlayers);
			
			GameControllerEventHandler.sendEvent(e);
			
		}
		//eliminate any things which ended their turn on a sea hex
		Event e = new Event()
					.EventId(EventList.ELIMINATE_SEA_HEX_THINGS);
		GameControllerEventHandler.sendEvent(e);
	}

	private void recruitThings() {
		for(GameRouter gr: GameServer.servers)
		{
			int numPaidRecruits = 0;
			
			String[] args = {"" + gr.myID,"Paid Recruits",""};
			
			//ask for number of paid recruits
			Response[] responses = GameControllerEventHandler.sendEvent(
					new Event()
					.EventId(EventList.ENTER_NUMBER)
					.EventParameters(args)
					.ExpectsResponse(true)
					);
			
			for (int j=0; j<responses.length; j++){
				if(responses[j].fromPlayer == gr.myID)
				{
					numPaidRecruits = Integer.parseInt(responses[j].message.trim());
				}
			}
			
			args[1] = Integer.toString(numPaidRecruits*GameConstants.GOLD_PER_RECRUIT);			
			//pay for recruits
			GameControllerEventHandler.sendEvent(new Event()
					.EventId(EventList.PAY_GOLD)
					.EventParameters(args)
					);

			boolean[] intendedPlayers = new boolean[numClients];
			intendedPlayers[gr.myID] = true;
			
			args[1] = Integer.toString(numPaidRecruits);
			args[2] = "0";
			responses = GameControllerEventHandler.sendEvent(
					new Event()
					.EventId(EventList.DETERMINE_TOTAL_NUM_RECRUITS)
					.IntendedPlayers(intendedPlayers)
					.EventParameters(args)
					.ExpectsResponse(true)
					);
			
			String totalNumRecruits = "";
			for (int j=0; j<responses.length; j++){
				if(responses[j].fromPlayer == gr.myID)
				{
					 totalNumRecruits = responses[j].message.trim();
				}
			}
			
    		
			args[1] = totalNumRecruits;
    		
    		Event e = new Event()
    					.EventId(EventList.GET_THINGS_FROM_CUP)
    					.EventParameters(args);
    		
    		GameControllerEventHandler.sendEvent(e);
		}
		

	}

	private void distributeIncome() {
		String[] args = {"" + numClients};
		
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.AWARD_INCOME)
					.EventParameters(args)
					.ExpectsResponse(true)
			);			
	}

	private void ChangePlayerOrder(){
		GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.UPDATE_PLAYER_ORDER)
			);	
		
		if(GameServer.servers.size() != 2)	//player order stays the same in 2 player game
			sortServersByOrder();
	}
	
	private void sortServersByOrder()
	{
		Collections.sort(GameServer.servers, new ServerComparator());
	}
	
	private void PlayBattlePhase(){
		DoBattle();
		//DoTestBattle();
	}
	
	private void DoBattle(){
		// REMOVE
		//AddTestThingsToTile();
		//
		
		Response[] r = GameControllerEventHandler.sendEvent(
			new Event().EventId(EventList.GET_CONTESTED_ZONES)
				.ExpectsResponse(true)
				.IntendedPlayers(new boolean[]{ true, false, false, false})
		);
		
		
		String[] contestedZones = r[0].castToStringArray();
		
		System.out.println("CONTESTED ZONES:" + contestedZones);
		
		if(contestedZones[0].equals(""))
			return;
			
		for(String s : contestedZones){
			String[] coordinates = s.split("SPLIT");
			
			GameControllerEventHandler.sendEvent(
				new Event()
					.EventId( EventList.BEGIN_BATTLE)
					.EventParameters(coordinates)
			);
			
			boolean battleOver = false;
			
			if (GameControllerEventHandler.sendEvent(
					new Event()
					.EventId( EventList.REMOVE_BLUFFS )
					.EventParameters(coordinates)
					.ExpectsResponse()
			)[0].eventId == EventList.REMOVE_BLUFFS){
				battleOver = true;
			}
			
			// 1 turn
			if (!battleOver) {
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
					int totalHitsInRound = 0;
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
							)[0].eventId == EventList.REMOVE_THINGS){
								battleOver = true;
							} else {
								battleOver = false;
							}
						}
						
						totalHitsInRound += numActualHits;
						
						if (battleOver){
	
							break;
						}
					}
					
					if (!battleOver && totalHitsInRound != 0){
						Response[] retreats = GameControllerEventHandler.sendEvent(
							new Event()
								.EventId( EventList.GET_RETREAT )
								.EventParameters( coordinates )
								.ExpectsResponse()
						);
						
						int numLeft = 0;
						for (Response retreat : retreats) {
							if (retreat.eventId == EventList.NULL_EVENT){
								continue;
							}
							if (retreat.message.equals("n")){
								numLeft++;
							} 
						}
						if (numLeft < 2){
							battleOver = true;
						}
					}
					
				} while (!battleOver);
			}
			
			boolean[] intendedPlayers = new boolean[GameServer.servers.size()];
			intendedPlayers[0] = true;
			//get builidng eliminations
			Response[] responses = GameControllerEventHandler.sendEvent(
					new Event()
						.EventId( EventList.GET_POST_BATTLE_BUILDING_ELIMINATIONS)
						.IntendedPlayers(intendedPlayers)
						.ExpectsResponse(true)
						.EventParameters(coordinates)
				);
			
			String[] params = new String[5];
			params[0] = coordinates[0];
			params[1] = coordinates[1];
			
			for(Response response: responses){
				if (response.fromPlayer == 0){
					String[] messageParams = response.message.trim().split("~");
					params[2] = messageParams[0];
					params[3] = messageParams[1];
					params[4] = messageParams[2];
				}
			}
			
			GameControllerEventHandler.sendEvent(
					new Event()
						.EventId( EventList.BATTLE_OVER)
						.EventParameters(params)
				);
		}
	}
}
