package Game.Networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import Game.Combatant;
import Game.Creature;
import Game.GameConstants;
import Game.GameConstants.ControlledBy;
import Game.GameConstants.BattleTurn;
import Game.GameConstants.Terrain;
import Game.HexTile;
import Game.Player;
import Game.Thing;
import Game.Utility;

public class EventHandler {
	public static Object lock = new Object();
	public static int numberOfSends = 0;
	
	public static void HandleEvent( String input ) throws Exception{
		Event e = Event.Destringify( input );
		synchronized(lock){
			HandleEvent( e );
		}
	}
	
	public static void SendEvent( Event e ){
		String eventString = e.toString();
		numberOfSends++;
		
		EventSender.Send( eventString );
	}
	
	public static void HandleEvent( Event e ) throws Exception{
		numberOfSends = 0;
		
		if (e.eventId != 14){
			System.out.println("Handling event " + e.eventId);
		}
		if (e.eventId == EventList.READY){}
		else if (e.eventId == EventList.ROLL_DICE)
		{
			System.out.println("CREATING DIE ROLL RESPONSE EVENT");
			
			String[] args = new String[1];
			args[0] = Integer.toString((int)Math.ceil(Math.random()*6));
			
			EventHandler.SendEvent(
					new Event()
						.EventId(EventList.ROLL_DICE)
						.EventParameters(args)
			);
		}
		else if(e.eventId == EventList.SET_PLAYER_ORDER)
		{
			System.out.println("HANDLING SET PLAYER ORDER EVENT");
			
			GameClient.game.setPlayerOrders(Integer.parseInt(e.eventParams[1]));
		}
		else if(e.eventId == EventList.SET_NUM_PLAYERS)
		{
			System.out.println("HANDLING SET NUM PLAYERS EVENT");
			
			GameClient.game.setPlayerCount(Integer.parseInt(e.eventParams[0]));
		}
		else if(e.eventId == EventList.UPDATE_PLAYER_ORDER)
		{
			System.out.println("HANDLING UPDATE PLAYER ORDER EVENT");
			
			GameClient.game.updatePlayerOrder();
		}
		else if(e.eventId == EventList.BEGIN_BATTLE)
		{
			int tileX = Integer.parseInt(e.eventParams[0]);
			int tileY = Integer.parseInt(e.eventParams[1]);
			
			System.out.println("Beginning battle on tile x:" + tileX + "y: " + tileY);
		}
		else if(e.eventId == EventList.GET_CONTESTED_ZONES)
		{
			List<int[]> contestedZones = GameClient.game.gameModel.boardController.GetContestedZones();
			
			String[] args = new String[contestedZones.size()];
			
			int i = 0;
			for(int[] zone : contestedZones){
				args[i++] = zone[0] + "SPLIT" + zone[1];
			}
			
			EventHandler.SendEvent(
				new Event()
					.EventId( EventList.GET_CONTESTED_ZONES )
					.EventParameters(args)
			);
		}
		else if(e.eventId == EventList.TEST_EVENT)
		{
			GameClient.game.gameView.playerList.getChildren().get(0).setStyle("-fx-background-color: 'red'");
		}
		else if (e.eventId == EventList.GET_CREATURE_ROLLS){
			int tileX = Integer.parseInt(e.eventParams[0]);
			int tileY = Integer.parseInt(e.eventParams[1]);
			
			
			String type = e.eventParams[2];
			boolean isMagicTurn = false, isRangedTurn = false;
			
			if (type.equals("Magic")) { isMagicTurn = true; }
			if (type.equals("Ranged")) { isRangedTurn = true; }
			
			if (GameClient.game.gameModel.boardController.HasThingsOnTile(
					GameClient.game.gameModel.GetCurrentPlayer(), 
					tileX, 
					tileY
				)) {
				
				HexTile battleTile = GameClient.game.gameModel
										.boardController
										.GetTile(tileX, tileY);
				Player currPlayer = GameClient.game.gameModel.GetCurrentPlayer();
				
				int rolls = 0;
				
				for (Thing thing : battleTile.GetThings( currPlayer )){
					if ( !thing.IsCombatant() ){
						continue;
					}
					
					BattleTurn turn;
					if (isMagicTurn){ turn = BattleTurn.MAGIC; }
					else if (isRangedTurn){ turn = BattleTurn.RANGED; }
					else { turn = BattleTurn.OTHER; }
					
					Combatant combatant = (Combatant)thing;
					
					rolls += combatant.GetCombatRoll(turn, true);
				}
				
				EventHandler.SendEvent(
					new Event()
						.EventId( EventList.GET_CREATURE_ROLLS )
						.EventParameter("" + rolls)
				);
				
			} else {
				SendNullEvent();
			}
		}
		else if (e.eventId == EventList.SET_CURRENT_PLAYER){
			int playerNum = Integer.parseInt(e.eventParams[0]);
			
			GameClient.game.gameModel.SetCurrentPlayer(playerNum);
		}
		else if (e.eventId == EventList.CHOOSE_PLAYER){
			int tileX = Integer.parseInt(e.eventParams[0]);
			int tileY = Integer.parseInt(e.eventParams[1]);
			
			Player currentPlayer = GameClient.game.gameModel.GetCurrentPlayer();
			List<Integer> playersOnTile = GameClient.game.gameModel.boardController
											.PlayersOnTile(tileX, tileY);
			
			if (playersOnTile.contains(currentPlayer.GetPlayerNum())) {
				int targetedPlayer = -1;
				if (playersOnTile.size() == 2){
					targetedPlayer = currentPlayer.GetPlayerNum() == playersOnTile.get(0) ?
										playersOnTile.get(1) :
										playersOnTile.get(0);
						
				} else {
					BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
					System.out.println("Enter a player to target (0,1,2,3)");
					try {
						targetedPlayer = Integer.parseInt(bufferRead.readLine());
					} catch (Exception ex){}
				}
				
				System.out.println("current player: " + currentPlayer.GetPlayerNum() + 
						" has targeted player: " + targetedPlayer);
				
				EventHandler.SendEvent(
					new Event()
						.EventId(EventList.CHOOSE_PLAYER)
						.EventParameter("" + targetedPlayer)
				);
			} else {
				SendNullEvent();
			}
		}
		else if (e.eventId == EventList.INFLICT_HITS){
			int playerCount = GameClient.game.gameModel.PlayerCount();
			int tileX = Integer.parseInt(e.eventParams[playerCount]);
			int tileY = Integer.parseInt(e.eventParams[playerCount+1]);
			
			Player currentPlayer = GameClient.game.gameModel.GetCurrentPlayer();
			
			if (e.eventParams[currentPlayer.GetPlayerNum()].equals("null")){
				SendNullEvent();
				return;
			}
			
			int numHitsTaken = Integer.parseInt(e.eventParams[currentPlayer.GetPlayerNum()].trim());
			
			if (numHitsTaken > 0){
				HexTile currTile = GameClient.game.gameModel.boardController.GetTile(tileX, tileY);
				ArrayList<Thing> things = currTile.GetThings(currentPlayer);
				
				System.out.println("Choose " + numHitsTaken + " thing(s) to remove:");
				System.out.print("Things:");
				for (Thing thing : things){
					System.out.print(thing.GetThingId() + " ");
				}
				
				int numHitsToApply = things.size() > numHitsTaken ? numHitsTaken : things.size();
				
				String[] thingsToRemove = new String[numHitsToApply];
				
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				if (things.size() > numHitsTaken){
					try {
						for (int i = 0; i < numHitsTaken; i++){
							thingsToRemove[i] = bufferRead.readLine();
						}
					} catch (Exception ex){}
				} else {
					int i = 0;
					for (Thing t : things){
						thingsToRemove[i++] = "" + t.thingID;
					}
				}
				EventHandler.SendEvent(
					new Event()
						.EventId(EventList.INFLICT_HITS)
						.EventParameters(thingsToRemove)
				);
			} else {
				SendNullEvent();
			}
		} 
		else if (e.eventId == EventList.REMOVE_THINGS){
			int players = GameClient.game.gameModel.PlayerCount();
			int tileX = Integer.parseInt(e.eventParams[players]);
			int tileY = Integer.parseInt(e.eventParams[players+1]);
			
			GameClient.game.gameModel.boardController.GetTile(tileX, tileY).Print();
			
			for(int i = 0; i < players; i++){
				String[] thingsToRemoveStrings = e.eventParams[i].split(" ");
				int[] thingsToRemove = Utility.CastToIntArray(thingsToRemoveStrings);
				
				Player player = GameClient.game.gameModel.GetPlayer(i);
				GameClient.game.gameModel.boardController.RemoveThings(thingsToRemove, player, tileX, tileY);
			}
			
			GameClient.game.gameModel.boardController.GetTile(tileX, tileY).Print();
			
			boolean battleOver = GameClient.game.gameModel.boardController.PlayersOnTile(tileX, tileY).size() <= 1;
			System.out.println(GameClient.game.gameModel.boardController.PlayersOnTile(tileX, tileY).size());
			
			if ( battleOver ){
				EventHandler.SendEvent(new Event().EventId(EventList.BATTLE_OVER));
			} else {
				SendNullEvent();
			}
		} else if (e.eventId == EventList.ADD_THING_TO_TILE){
			String type = e.eventParams[0];
			
			boolean isMagic = false, isRanged = false;
			
			if (type.equals("Magic")) { isMagic = true; }
			if (type.equals("Ranged")) { isRanged = true; }
		
			Creature creature = new Creature(Terrain.DESERT, 6).Magic(isMagic).Ranged(isRanged);
			creature.SetThingId(Integer.parseInt(e.eventParams[1]));
			Player player = GameClient.game.gameModel.GetPlayer(Integer.parseInt(e.eventParams[2]));
			
			int tileX = Integer.parseInt(e.eventParams[3]);
			int tileY = Integer.parseInt(e.eventParams[4]);
			GameClient.game.gameModel.boardController.AddThingToTile(creature, player, tileX, tileY);
		} else if (e.eventId == EventList.BATTLE_OVER) {
			System.out.println("Ending battle");
		} else if (e.eventId == EventList.SET_HEX_TILES) {
			String boardHexTilesString = e.eventParams[0];
			String unusedHexTileString = e.eventParams[1];
			
			String[] boardHexTileStrings = boardHexTilesString.split("/");
			String[] unusedHexTileStrings = unusedHexTileString.split("/");
			
			final HexTile[][] h = GameClient.game.gameModel.setInitialHexTiles(boardHexTileStrings);
			GameClient.game.gameModel.setInitialUnusedHexTiles(unusedHexTileStrings);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	GameClient.game.gameView.board.setTiles(h);
		        }
		    });
		}
		else if (e.eventId == EventList.SET_CREATURES) {
			String creaturesString = e.eventParams[0];
			
			String[] creaturesStrings = creaturesString.split("/");
			
			GameClient.game.gameModel.setPlayingCup(creaturesStrings);
			
			//GameClient.game.gameModel.printCurrentBoardTiles();
		}
		else if (e.eventId == EventList.GET_THINGS_FROM_CUP) {
			final int playerIndex = Integer.parseInt(e.eventParams[0]);
			final int numThings = Integer.parseInt(e.eventParams[1]);
			
			final boolean isCurrentPlayer = playerIndex == GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum();
			
			GameClient.game.gameModel.getThingsFromCup(playerIndex, numThings);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	GameClient.game.gameView.playerList.getPlayerPanel(playerIndex).setThings(numThings);
		        	if(isCurrentPlayer)
		        		GameClient.game.gameView.rack.setAllThings(GameClient.game.gameModel.GetCurrentPlayer().getPlayerRack().getThings());
		        }
			});
		}
		else if (e.eventId == EventList.DISTRIBUTE_INITIAL_GOLD)
		{
			final int numClients = Integer.parseInt(e.eventParams[0]);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	for(int i=0; i<numClients; i++)
		        	{
		        		GameClient.game.gameView.playerList.getPlayerPanel(i).setGold(10);
		        	}
		        }
			});
		}
		else if (e.eventId == EventList.AWARD_INCOME)
		{
			final int numClients = Integer.parseInt(e.eventParams[0]);
			
			final int[] goldUpdates = GameClient.game.gameModel.distributeIncome();
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	for(int i=0; i<numClients; i++)
		        	{
		        		GameClient.game.gameView.playerList.getPlayerPanel(i).addGold(goldUpdates[i]);
		        	}
		        }
			});
		}
		else if (e.eventId == EventList.PLACE_PIECE_ON_TILE)
		{
			int playerIndex = Integer.parseInt(e.eventParams[0]);
			String pieceBeingPlaced = e.eventParams[1];
			
			if(GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum() == playerIndex)
			{
				HexTile selectedTile;
				
				boolean validSelectionMade = false;
				String s;
				do
				{
					s = "Please select a tile to place a " + pieceBeingPlaced + " into.";
					GameClient.game.gameView.displayMessage(s);
					selectedTile = GameClient.game.gameView.chooseHexTile();
					
					if(pieceBeingPlaced.equals("Control_Marker"))
					{
						validSelectionMade = GameClient.game.gameModel.isValidControlMarkerPlacement(selectedTile);
					}
					else if(pieceBeingPlaced.equals("Tower"))
					{
						validSelectionMade = GameClient.game.gameModel.isValidTowerPlacement(selectedTile);
					}
					else
					{
						GameClient.game.gameView.displayMessage("The tile you selected is invalid, please choose a new tile");
					}
					
				}while(!validSelectionMade);
				
				int x = selectedTile.x;
				int y = selectedTile.y;
				
				if(pieceBeingPlaced.equals("Control_Marker"))
				{
					GameClient.game.gameModel.updateTileFaction(playerIndex, x, y);
				}
				else if(pieceBeingPlaced.equals("Tower"))
				{
					GameClient.game.gameModel.addTower(x, y, playerIndex);
				}
				
				//update view
				GameClient.game.gameView.updateHexTile(selectedTile);
				
				//respond with the coords and playerIndex of updated tile
				System.out.println("CREATING PLACE PIECE ON TILE RESPONSE EVENT FOR PIECE " + pieceBeingPlaced);
				
				String[] args = {selectedTile.x +"SPLIT"+selectedTile.y, Integer.toString(playerIndex), pieceBeingPlaced};
				
				EventHandler.SendEvent(
						new Event()
							.EventId(EventList.PLACE_PIECE_ON_TILE)
							.EventParameters(args)
				);
			}
			else
			{
				waitForOtherPlayer(playerIndex, "place a " + pieceBeingPlaced);
			}
		}
		else if(e.eventId == EventList.HANDLE_PLACE_PIECE_ON_TILE)
		{			
			String[] hexTileCoords = e.eventParams[0].split("SPLIT");
			
			int x = Integer.parseInt(hexTileCoords[0]);
			int y = Integer.parseInt(hexTileCoords[1]);
			
			int playerIndex = Integer.parseInt(e.eventParams[1]);
			
			String pieceBeingPlaced = e.eventParams[2];
			
			HexTile h = null;
			if(pieceBeingPlaced.equals("Control_Marker"))
			{
				h = GameClient.game.gameModel.updateTileFaction(playerIndex, x, y);
			}
			else if(pieceBeingPlaced.equals("Tower"))
			{
				h = GameClient.game.gameModel.addTower(x, y, playerIndex);
			}
			GameClient.game.gameView.updateHexTile(h);
		}
		else if(e.eventId == EventList.DETERMINE_NUM_PAID_THINGS)
		{
			int playerIndex = Integer.parseInt(e.eventParams[0]);
			
			if(playerIndex == GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum())
			{
				int numRecruits =0;
				do
				{
					GameClient.game.gameView.displayMessage("Please select the number of paid recruits you would like");
					numRecruits = GameClient.game.gameView.getNumPaidRecruits();
				}while(!GameClient.game.gameModel.GetCurrentPlayer().canAffordRecruits(numRecruits));
				
				//respond with number of paid things desired
				System.out.println("CREATING DETERMINE NUM PAID THINGS RESPONSE EVENT");
				
				String[] args = {Integer.toString(numRecruits)};
				
				EventHandler.SendEvent(
						new Event()
							.EventId(EventList.DETERMINE_NUM_PAID_THINGS)
							.EventParameters(args)
				);
			}
			else
			{
				waitForOtherPlayer(playerIndex, "select a number of paid things to take");
			}
		}
		else if(e.eventId == EventList.DETERMINE_NUM_TRADE_THINGS)
		{
			int playerIndex = Integer.parseInt(e.eventParams[0]);
			
			if(playerIndex == GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum())
			{
				int numRecruits =0;
				do
				{
					GameClient.game.gameView.displayMessage("Please select the number of recruits you would like to trade for");
					numRecruits = GameClient.game.gameView.getNumTradeRecruits();
				}while(!GameClient.game.gameModel.GetCurrentPlayer().canTradeForRecruits(numRecruits));
				
				//respond with number of paid things desired
				System.out.println("CREATING DETERMINE NUM TRADE THINGS RESPONSE EVENT");
				
				String[] args = {Integer.toString(numRecruits)};
				
				EventHandler.SendEvent(
						new Event()
							.EventId(EventList.DETERMINE_NUM_TRADE_THINGS)
							.EventParameters(args)
				);
			}
			else
			{
				waitForOtherPlayer(playerIndex, "select a number of things to trade");
			}
		}
		else if(e.eventId == EventList.DISTRIBUTE_RECRUITS)
		{
			int numPaidRecruits = Integer.parseInt(e.eventParams[0]);
			int numTradeRecruits = Integer.parseInt(e.eventParams[1]);
			int playerIndex = Integer.parseInt(e.eventParams[2]);
			
			System.out.println("PAID RECRUITS: " + numPaidRecruits);
			System.out.println("TRADE RECRUITS: " + numTradeRecruits);
			
			int totalNumRecruits = GameClient.game.gameModel.distributeRecruits(numPaidRecruits, numTradeRecruits);
			
			//pass thigns
			GameClient.game.gameView.updatePlayerRack();
			
			//send back num things removed, thing Ids put back into cup.
		}
		else if(e.eventId == EventList.HANDLE_DISTRIBUTE_RECRUITS)
		{
			//update all other players with recruit distribution
		}
		else if(e.eventId == EventList.PLAY_THINGS)
		{
			//allow player to play things
			//HexTile[] hexTiles = GameClient.game.gameView.playThings();
			//GameClient.game.gameModel.updatePlayedThings(hexTiles);
		}
		else if(e.eventId == EventList.HANDLE_PLAY_THINGS)
		{
			//update all other players with played things
		}
		else if(e.eventId == EventList.CHECK_PLAYER_RACK_OVERLOAD)
		{
			if(GameClient.game.gameModel.playerRackTooFull())
			{
				int numThingsRemoved = GameClient.game.gameModel.removeExcessFromRack();
				GameClient.game.gameView.displayMessage("You had more than 10 things on your rack. " + numThingsRemoved + " things have been removed.");
			}
		}
		else if(e.eventId == EventList.HANDLE_CHECK_PLAYER_RACK_OVERLOAD)
		{
			//update all other players with things placed back in the cup
		}
		
		if (e.expectsResponseEvent && numberOfSends != 1){
				throw new Exception("Expected event to be sent, but number of events sent was " + numberOfSends);
		} else if (!e.expectsResponseEvent && numberOfSends != 0){
				throw new Exception("Expected event to not be sent, but number of events sent was " + numberOfSends );
		}
		

	}

	
	private static void waitForOtherPlayer(int playerIndex, String actionBeingTaken) {
		String s = "Waiting for player with index " + playerIndex + " to " + actionBeingTaken + ".";
		
		GameClient.game.gameView.displayMessage(s);
		
		SendNullEvent();		
	}

	private static void SendNullEvent(){
		EventHandler.SendEvent( new Event().EventId( EventList.NULL_EVENT ) );
	}
}
