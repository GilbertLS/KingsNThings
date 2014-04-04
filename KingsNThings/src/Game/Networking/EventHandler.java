package Game.Networking;

import gui.GameView;
import gui.Tile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import Game.BoardController;
import Game.Combatant;
import Game.Creature;
import Game.GameConstants;
import Game.GameConstants.CurrentPhase;
import Game.GameConstants.BattleTurn;
import Game.GameConstants.Terrain;
import Game.Dice;
import Game.HexTile;
import Game.Player;
import Game.Settlement;
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
		else if (e.eventId == EventList.ROLL_TWO_DICE)
		{
			System.out.println("CREATING DIE ROLL RESPONSE EVENT");
			
			String[] args = new String[1];
			
			int[] rolls = Dice.rollDice(2);
			args[0] = Integer.toString(rolls[0] + rolls[1]);
			
			EventHandler.SendEvent(
					new Event()
						.EventId(EventList.ROLL_TWO_DICE)
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
		else if (e.eventId == EventList.CHECK_TILE_SWAP){
			ArrayList<HexTile> newTiles = new ArrayList<HexTile>();
			ArrayList<HexTile> invalidHexTiles;
			
			
			GameClient.game.sendMessageToView("Invalid initial tiles will be swapped");
			GameClient.game.gameView.performPhase(CurrentPhase.SWAP_INITIAL_HEXES);
			GameClient.game.clearMessageOnView();
			
			do{
				invalidHexTiles = GameClient.game.gameModel.getInitTilesToSwap();
				
				newTiles = GameClient.game.gameModel.handleTileSwap(invalidHexTiles);
				
				final ArrayList<HexTile> newTilesCopy = newTiles;
				Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
			        	GameClient.game.gameView.changeHexes(newTilesCopy);
			        }
			    });
			}while(!invalidHexTiles.isEmpty());
			
			EventHandler.SendEvent(
					new Event()
						.EventId( EventList.CHECK_TILE_SWAP)
			);
		}
		else if(e.eventId == EventList.ELIMINATE_SEA_HEX_THINGS){
			final ArrayList<HexTile> hexTiles = GameClient.game.gameModel.eliminateSeaHexThings();
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	GameClient.game.gameView.updateTiles(hexTiles);
		        }
		    });
		}
		else if(e.eventId == EventList.BEGIN_BATTLE)
		{
			int tileX = Integer.parseInt(e.eventParams[0]);
			int tileY = Integer.parseInt(e.eventParams[1]);
			
			System.out.println("Beginning battle on tile x:" + tileX + "y: " + tileY);
			GameClient.game.gameView.StartBattle(tileX, tileY);
			while(!GameView.BattleOccuring()){
				Thread.sleep(1000);
			}
		} else if (e.eventId == EventList.BATTLE_OVER){
			int x = Integer.parseInt(e.eventParams[0]);
			int y = Integer.parseInt(e.eventParams[1]);
			boolean fortHit = Boolean.parseBoolean(e.eventParams[2]);
			boolean settlementHit = Boolean.parseBoolean(e.eventParams[3]);
			boolean specialIncomeHit = Boolean.parseBoolean(e.eventParams[4]);
			
			final HexTile h = GameClient.game.gameModel.boardController.GetTile(x, y);
			
			GameClient.game.gameModel.handlePostBattle(h, fortHit, settlementHit, specialIncomeHit);
			GameView.battleView.UpdateMessage("Battle is over");
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	GameClient.game.gameView.updateHexTile(h);
					GameClient.game.gameView.PreEndBattle();
		        }
		    });
			
			Thread.sleep(2000);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	GameClient.game.gameView.EndBattle();
		        }
		    });
			
			while(GameView.BattleOccuring()){
				Thread.sleep(1000);
			}
		} else if (e.eventId == EventList.GET_POST_BATTLE_BUILDING_ELIMINATIONS){
			int x = Integer.parseInt(e.eventParams[0]);
			int y = Integer.parseInt(e.eventParams[1]);
			
			final HexTile h = GameClient.game.gameModel.boardController.GetTile(x, y);
			
			boolean[] buldingsEliminated = h.getBuildingEliminations();
			
			String params = new String();
			for(boolean b: buldingsEliminated)
				params += Boolean.toString(b) + "~";
			
			EventHandler.SendEvent(
					new Event()
						.EventId( EventList.GET_POST_BATTLE_BUILDING_ELIMINATIONS )
						.EventParameter(params)
				);
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
		else if (e.eventId == EventList.GET_CREATURE_ROLLS){
			int tileX = Integer.parseInt(e.eventParams[0]);
			int tileY = Integer.parseInt(e.eventParams[1]);
			
			String message;
			
			String type = e.eventParams[2];
			boolean isMagicTurn = false, isRangedTurn = false;
			
			if (type.equals("Magic")) { 
				isMagicTurn = true; 
				message = "magic";
			}
			else if (type.equals("Ranged")) { 
				isRangedTurn = true;
				message = "ranged";
			}
			else {
				message = "other";
			}
			
			
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
				
				ArrayList<Thing> thingsBattling = new ArrayList<Thing>();
				for(Thing t :battleTile.GetThings( currPlayer ))
				{
					thingsBattling.add(t);
				}
				if(battleTile.hasFort() && battleTile.isControlledBy(currPlayer.faction))
				{
					thingsBattling.add(battleTile.getFort());
				}
				
				for (Thing thing : thingsBattling){
					if ( !thing.IsCombatant() ){
						continue;
					}
					
					BattleTurn turn;
					if (isMagicTurn){ turn = BattleTurn.MAGIC; }
					else if (isRangedTurn){ turn = BattleTurn.RANGED; }
					else { turn = BattleTurn.OTHER; }
					
					Combatant combatant = (Combatant)thing;
					
					rolls += combatant.GetCombatRoll(turn, rolls);
				}
				
				GameView.battleView.UpdateMessage("Applying " + rolls + " hits for this " + message + " rolls turn");
				
				EventHandler.SendEvent(
					new Event()
						.EventId( EventList.GET_CREATURE_ROLLS )
						.EventParameter("" + rolls)
				);
				
			} else {
				SendNullEvent();
			}
			

			//GameView.battleView.ClearMessage();
				
		}
		else if (e.eventId == EventList.SET_CURRENT_PLAYER){
			final int playerNum = Integer.parseInt(e.eventParams[0]);
			
			GameClient.game.gameModel.SetCurrentPlayer(playerNum);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	GameClient.game.gameView.setCurrentPlayer(playerNum);
		        }
		    });
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
					targetedPlayer = GameView.battleView.GetTargetPlayer();
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
				
				ArrayList<Thing> things = currTile.getCombatants(currentPlayer.GetPlayerNum());
				
				int numHitsToApply = things.size() > numHitsTaken ? numHitsTaken : things.size();
				
				/*should be handled by using getCombatants method above instead of getThings
				boolean hasFort = currTile.hasFort() && currTile.isControlledBy(currentPlayer.faction);
				
				//also need to handle settlements and any other combatants
				if(hasFort)
					numHitsToApply++;*/
				
				String[] thingsToRemove = new String[numHitsToApply];
				
				//need to change to take into account buildings takng multiple hits
				if (things.size() > numHitsTaken){
					int[] tilesToRemove = GameView.battleView.inflictHits(numHitsTaken);
					for (int i = 0; i < tilesToRemove.length; i++){
						thingsToRemove[i] = "" + tilesToRemove[i];
					}
				} else {
					int i = 0;
					for (Thing t : things){
						thingsToRemove[i++] = "" + t.thingID;
					}
					/*
					if(hasFort){
						thingsToRemove[i++] = "" + currTile.getFort().thingID;
					}*/
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
				GameView.battleView.RemoveThings(thingsToRemove, player.GetPlayerNum());
			}
			
			GameClient.game.gameModel.boardController.GetTile(tileX, tileY).Print();
			
			boolean battleOver = GameClient.game.gameModel.boardController.PlayersOnTile(tileX, tileY).size() <= 1;
			
			if ( battleOver ){
				EventHandler.SendEvent(new Event().EventId(EventList.REMOVE_THINGS));
			} else {
				SendNullEvent();
			}
		} 
		else if (e.eventId == EventList.SET_HEX_TILES) 
		{
			String[] boardHexTileStrings = e.eventParams[0].trim().split("/");
			
			ArrayList<HexTile> tiles = GameClient.game.parseInitialHexTiles(boardHexTileStrings);
			
			final HexTile[][] h = GameClient.game.gameModel.setInitialHexTiles(tiles);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	GameClient.game.gameView.board.setTiles(h);
		        }
		    });
		}
		else if (e.eventId == EventList.RANDOMIZE_UNUSED_TILES)
		{
			GameClient.game.gameModel.randomizeUnusedTiles();
			
			String thingIDs = GameClient.game.gameModel.getUnusedTileString();
			
			EventHandler.SendEvent(
					new Event()
						.EventId(EventList.RANDOMIZE_UNUSED_TILES)
						.EventParameter(thingIDs)
				);
		}
		else if (e.eventId == EventList.UPDATE_UNUSED_TILES)
		{
			String[] thingIDStrings = e.eventParams[0].split(" ");
			
			ArrayList<Terrain> tileTerrains = new ArrayList<Terrain>();
			
			for(String s: thingIDStrings)
			{
				tileTerrains.add(Terrain.valueOf(s));
			}
			
			GameClient.game.gameModel.setUnusedTiles(tileTerrains);
		}
		else if (e.eventId == EventList.REVEAL_HEX_TILES)
		{
			GameClient.game.gameView.showHideAllTiles(true);
		}
		else if (e.eventId == EventList.RANDOMIZE_THINGS)
		{
			GameClient.game.gameModel.randomizePlayingCup();
			
			String thingIDs = GameClient.game.gameModel.getCupIDsString();
			
			EventHandler.SendEvent(
					new Event()
						.EventId(EventList.RANDOMIZE_THINGS)
						.EventParameter(thingIDs)
				);
		}
		else if (e.eventId == EventList.UPDATE_CUP_ORDER)
		{
			String[] thingIDStrings = e.eventParams[0].split(" ");
			
			ArrayList<Integer> thingIDs = new ArrayList<Integer>();
			
			for(String s: thingIDStrings)
			{
				thingIDs.add(Integer.parseInt(s));
			}
			
			GameClient.game.gameModel.setPlayingCupOrder(thingIDs);
		}
		else if (e.eventId == EventList.RANDOMIZE_SPECIAL_CHARACTERS)
		{
			String specialCharacterString = GameClient.game.gameModel.randomizeSpecialCharactersString();

			EventHandler.SendEvent(
					new Event()
						.EventId(EventList.RANDOMIZE_SPECIAL_CHARACTERS)
						.EventParameter(specialCharacterString)
				);
		}
		else if (e.eventId == EventList.CREATE_SPECIAL_CHARACTERS)
		{
			String specialCharacterString = e.eventParams[0].trim();
			
			GameClient.game.gameModel.initializeSpecialCharacters(specialCharacterString);
		}
		else if (e.eventId == EventList.GET_THINGS_FROM_CUP) {
			final int playerIndex = Integer.parseInt(e.eventParams[0]);
			final int numThings = Integer.parseInt(e.eventParams[1]);
			
			ArrayList<Thing> things = GameClient.game.gameModel.getThingsFromCup(numThings);
			
			for(Thing t: things)
				GameClient.game.gameModel.playerFromIndex(playerIndex).addThingToRack(t);
			
			GameClient.game.updatePlayerRack(playerIndex);
		}
		else if(e.eventId == EventList.TRADE_THINGS){
			final int playerIndex = Integer.parseInt(e.eventParams[0]);
			final boolean isInitialTrade = Boolean.parseBoolean(e.eventParams[1]);
			
			if(GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum() == playerIndex){
				
				GameClient.game.sendMessageToView("Please trade your recruits if you desire");
				
				String thingIDsTraded = "";
				if(isInitialTrade)
					thingIDsTraded = GameClient.game.gameView.performPhase(CurrentPhase.INITIAL_TRADE_THINGS);
				else
					thingIDsTraded = GameClient.game.gameView.performPhase(CurrentPhase.TRADE_THINGS);
			
				EventHandler.SendEvent(
						new Event()
							.EventId(EventList.TRADE_THINGS)
							.EventParameter(thingIDsTraded)
					);
			}
			else{
				waitForOtherPlayer(e.expectsResponseEvent, playerIndex, "trade their things");
			}
		}
		else if(e.eventId == EventList.HANDLE_TRADE_THINGS)
		{
			final int playerIndex = Integer.parseInt(e.eventParams[0]);
			final boolean isInitialTrade = Boolean.parseBoolean(e.eventParams[1]);	
			
			if(e.eventParams.length >= 3)
			{
				String[] tradedThingIDStrings = e.eventParams[2].trim().split(" ");
				final ArrayList<Integer> tradedThingIDs = new ArrayList<Integer>();
				for(String s: tradedThingIDStrings)
				{
					tradedThingIDs.add(Integer.parseInt(s));
				}
				
				GameClient.game.gameModel.tradeThings(playerIndex, isInitialTrade, tradedThingIDs);
				
				GameClient.game.updatePlayerRack(playerIndex);
			}
		}
		else if (e.eventId == EventList.CHECK_WIN)
		{
			String args = GameClient.game.gameModel.checkWin();
			
			EventHandler.SendEvent(
					new Event()
						.EventId(EventList.CHECK_WIN)
						.EventParameter(args)
			);
		}
		else if (e.eventId == EventList.HANDLE_WIN)
		{
			int winningIndex = (Integer.parseInt(e.eventParams[0]));
			
			GameClient.game.gameView.handleWin(winningIndex);
		}
		else if (e.eventId == EventList.DISTRIBUTE_INITIAL_GOLD)
		{
			final int numClients = Integer.parseInt(e.eventParams[0]);
			
			GameClient.game.gameModel.distributeInitialGold();
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	for(int i=0; i<numClients; i++)
		        	{
		        		/*Update player list*/
		        		GameClient.game.gameView.playerList.updatePlayerNumber(numClients);
		        		/*Init Gold*/
		        		GameClient.game.gameView.updateGold(GameClient.game.gameModel.playerFromIndex(i).getGold(), i);	
		        	}
		        }
			});
		}
		else if (e.eventId == EventList.AWARD_INCOME)
		{
			final int numClients = Integer.parseInt(e.eventParams[0]);
			
			final int[] goldUpdates = GameClient.game.gameModel.distributeIncome();
			
			GameClient.game.sendMessageToView("You have been awarded " + goldUpdates[GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum()] + " gold pieces.");		   
			
			GameClient.game.gameView.performPhase(CurrentPhase.AWARD_INCOME);
			
			GameClient.game.clearMessageOnView();
			
			//send finished
			EventHandler.SendEvent(
					new Event()
						.EventId(EventList.AWARD_INCOME)
			);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	for(int i=0; i<numClients; i++)
		        	{
		        		GameClient.game.gameView.updateGold(GameClient.game.gameModel.playerFromIndex(i).getGold(), i);	
		        	}	
		        }
			});
		}
		else if (e.eventId == EventList.PLACE_PIECE_ON_TILE)
		{
			int playerIndex = Integer.parseInt(e.eventParams[0]);
			final String pieceBeingPlacedString;
			
			if(e.eventParams[1].equals("Control_Marker"))
				pieceBeingPlacedString = "Control Marker";
			else
				pieceBeingPlacedString = e.eventParams[1];
			
			if(GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum() == playerIndex)
			{
				Tile selectedTile;
				HexTile selectedHex;
				
				boolean validSelectionMade = false;
				
				GameClient.game.sendMessageToView("Please select a tile to place a " + pieceBeingPlacedString + " into.");
				EventHandler.SendEvent( 
					new Event()
					.EventId(EventList.SET_MESSAGE)
					.EventParameter("Player " + playerIndex + " selecting a tile to place")
				);
				
				do
				{										
					selectedTile = GameClient.game.gameView.chooseTile();
					selectedHex = selectedTile.getTileRef();
					
					if(pieceBeingPlacedString.equals("Control Marker"))
					{
						validSelectionMade = GameClient.game.gameModel.isValidControlMarkerPlacement(selectedHex);
					}
					else if(pieceBeingPlacedString.equals("Tower"))
					{
						validSelectionMade = GameClient.game.gameModel.isValidTowerPlacement(selectedHex);
					}
					else
					{
						GameClient.game.sendMessageToView("The tile you selected is invalid, please choose a new tile");
						
					}
					
				}while(!validSelectionMade);		
				GameClient.game.clearMessageOnView();
				
				int x = selectedHex.x;
				int y = selectedHex.y;
				
				if(pieceBeingPlacedString.equals("Control Marker"))
				{
					GameClient.game.gameModel.claimNewTile(GameClient.game.gameModel.GetCurrentPlayer(), x, y);
				}
				else if(pieceBeingPlacedString.equals("Tower"))
				{
					GameClient.game.gameModel.addTower(x, y, playerIndex);
				}
				
				//update view
				final Tile finalSelection = selectedTile;
				//update view
				Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
			        	GameClient.game.gameView.clearMessage();
			        	finalSelection.update();
			        }
				});
				
				//respond with the coords and playerIndex of updated tile
				System.out.println("CREATING PLACE PIECE ON TILE RESPONSE EVENT FOR PIECE " + pieceBeingPlacedString);
				
				String[] args = {selectedHex.x +"SPLIT"+selectedHex.y, Integer.toString(playerIndex), e.eventParams[1]};
				
				EventHandler.SendEvent(
						new Event()
							.EventId(EventList.PLACE_PIECE_ON_TILE)
							.EventParameters(args)
				);
			}
			else
			{
				waitForOtherPlayer(e.expectsResponseEvent, playerIndex, "place a " + pieceBeingPlacedString);
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
				h = GameClient.game.gameModel.claimNewTile(GameClient.game.gameModel.playerFromIndex(playerIndex), x, y);
			}
			else if(pieceBeingPlaced.equals("Tower"))
			{
				h = GameClient.game.gameModel.addTower(x, y, playerIndex);
			}
			
			//update tile
			final HexTile finalHex = h;
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	GameClient.game.gameView.updateHexTile(finalHex);
		        }
			});
		}
		else if(e.eventId == EventList.ENTER_NUMBER)
		{
			int playerIndex = Integer.parseInt(e.eventParams[0]);
			final String purposeForNumber = e.eventParams[1];
			
			if(playerIndex == GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum())
			{
				int number =0;
				boolean isValidSelection = false;
				GameClient.game.sendMessageToView("Please select the number of " + purposeForNumber + " you would like.");
				do
				{
					if(purposeForNumber.equals("Paid Recruits"))
					{
						final CountDownLatch latch = new CountDownLatch(1);
						final IntegerProperty output = new SimpleIntegerProperty();
						Platform.runLater(new Runnable() {
						    @Override public void run() {
						    	output.set(GameClient.game.gameView.getNumPaidRecruits());
						        latch.countDown();
						    }
						});
						latch.await();      
						
						number = output.get();
						
						isValidSelection = GameClient.game.gameModel.GetCurrentPlayer().canAffordRecruits(number);
					}
					
					if(!isValidSelection)
						GameClient.game.sendMessageToView("Invalid selection, please try again");

						
				}while(!isValidSelection);
				GameClient.game.clearMessageOnView();
				
				//respond with number of paid things desired
				System.out.println("CREATING DETERMINE ENTER NUMBER RESPONSE EVENT FOR "+ purposeForNumber);
				
				String[] args = {Integer.toString(number)};
				
				EventHandler.SendEvent(
						new Event()
							.EventId(EventList.ENTER_NUMBER)
							.EventParameters(args)
				);
			}
			else
			{
				waitForOtherPlayer(e.expectsResponseEvent, playerIndex, "select a number of "+ purposeForNumber+" to take.");
			}
		}
		else if(e.eventId == EventList.DETERMINE_TOTAL_NUM_RECRUITS)
		{
			int playerIndex = Integer.parseInt(e.eventParams[0]);
			int numPaidRecruits = Integer.parseInt(e.eventParams[1]);
			int numTradeRecruits = Integer.parseInt(e.eventParams[2]);
			
			int numRecruits = GameClient.game.gameModel.GetCurrentPlayer().determineNumRecruits(numPaidRecruits, numTradeRecruits);
			
			//respond with number of paid things desired
			System.out.println("CREATING DETERMINE TOTAL NUM RECRUITS EVENT");
			
			String[] args = {Integer.toString(numRecruits)};
			
			EventHandler.SendEvent(
					new Event()
						.EventId(EventList.DETERMINE_TOTAL_NUM_RECRUITS)
						.EventParameters(args)
			);
		}
		else if(e.eventId == EventList.PLAY_THINGS)
		{
			final int playerIndex = Integer.parseInt(e.eventParams[0]);
			
			if(playerIndex == GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum())
			{
				GameClient.game.sendMessageToView("Please play your Things");		        	
				
				//drag and drop things to tiles
				String thingsPlayedParamsString = GameClient.game.gameView.playIteration();
				
				GameClient.game.clearMessageOnView();
				
				String[] args = thingsPlayedParamsString.split(" ");
				
				//send changes
				EventHandler.SendEvent(
						new Event()
							.EventId(EventList.PLAY_THINGS)
							.EventParameters(args)
				);
			}
			else
			{
				waitForOtherPlayer(e.expectsResponseEvent, playerIndex, "play their things");
			}
		}
		else if(e.eventId == EventList.HANDLE_PLAY_THINGS)
		{
			final int playerIndex = Integer.parseInt(e.eventParams[0]);
			
			if(e.eventParams.length == 2)
			{
				final String[] thingsPlayedStrings = e.eventParams[1].trim().split("/");
				ArrayList<HexTile> hexTiles = new ArrayList<HexTile>();
				ArrayList<Integer> thingIDs = new ArrayList<Integer>();
				GameClient.game.parsePlayedThingsStrings(thingsPlayedStrings, hexTiles, thingIDs, playerIndex);
				
				GameClient.game.gameModel.updatePlayedThings(hexTiles, thingIDs, playerIndex);
				
				GameClient.game.gameModel.removeFromPlayerRack(thingIDs, playerIndex);
				
				final ArrayList<HexTile> hexTilesCopy = GameClient.game.parseToUniqueHexTiles(hexTiles);
				final ArrayList<Integer> thingIDsCopy = thingIDs;
				final int gold = GameClient.game.gameModel.GetPlayer(playerIndex).getGold();
				final int numThings = GameClient.game.gameModel.GetPlayer(playerIndex).getPlayerRack().size();
				Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
						GameClient.game.gameView.updateTiles(hexTilesCopy, playerIndex);
						GameClient.game.gameView.updateGold(gold, playerIndex);
			        	GameClient.game.gameView.playerList.getPlayerPanel(playerIndex).setThings(numThings);
			        }
				});
			}
		}
		else if(e.eventId == EventList.HANDLE_SPEND_GOLD)
		{
			int amount = Integer.parseInt(e.eventParams[0].trim());
			int playerIndex = Integer.parseInt(e.eventParams[1].trim());
			
			GameClient.game.gameModel.payGold(amount, playerIndex);
			GameClient.game.gameView.updateGold(GameClient.game.gameModel.playerFromIndex(playerIndex).getGold(), playerIndex);		
			
		}
		else if(e.eventId == EventList.RECRUIT_CHARACTER)
		{
			int playerIndex = Integer.parseInt(e.eventParams[0].trim());
			
			if(GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum() == playerIndex)
			{
				GameClient.game.sendMessageToView("Please Recruit a Special Character if you desire");		        	
				
				GameClient.game.gameView.performPhase(CurrentPhase.RECRUIT_CHARACTER);	
						
				GameClient.game.clearMessageOnView();
				
				//send finished
				EventHandler.SendEvent(
						new Event()
							.EventId(EventList.RECRUIT_CHARACTER)
				);
			}
			else
			{
				waitForOtherPlayer(e.expectsResponseEvent, playerIndex, "Recruit a Special Character");
			}
		}
		else if(e.eventId == EventList.HANDLE_RECRUIT_CHARACTER)
		{
			if(e.eventParams.length >= 2)
			{
				int thingID = Integer.parseInt(e.eventParams[0].trim());
				final int playerIndex = Integer.parseInt(e.eventParams[1].trim());
					
				GameClient.game.gameModel.recruitSpecialCharacter(thingID, playerIndex);
					
				GameClient.game.updatePlayerRack(playerIndex);
			}
		}
		else if(e.eventId == EventList.DO_CONSTRUCTION)
		{
			GameClient.game.sendMessageToView("Please construct or upgrade Forts");		        	
							
			GameClient.game.gameView.performPhase(CurrentPhase.CONSTRUCTION);	
					
			GameClient.game.clearMessageOnView();
				
			//send finished
			EventHandler.SendEvent(
					new Event()
						.EventId(EventList.DO_CONSTRUCTION)
			);
					
		}
		else if(e.eventId == EventList.HANDLE_CONSTRUCTION)
		{		
			if(e.eventParams.length >= 2)
			{
				final int playerIndex = Integer.parseInt(e.eventParams[0].trim());
				String constructionString = e.eventParams[1].trim();
				
				HexTile hexTile = GameClient.game.parseConstructionString(constructionString);
					
				GameClient.game.gameModel.updateConstruction(hexTile, playerIndex);
					
				final HexTile hexTileCopy = hexTile;
				final int gold = GameClient.game.gameModel.playerFromIndex(playerIndex).getGold();
					
				Platform.runLater(new Runnable() {
					@Override
			    	public void run() {
						GameClient.game.gameView.updateTiles(hexTileCopy, playerIndex);	
						GameClient.game.gameView.updateGold(gold, playerIndex);
						}
					});
			}
		}
		else if(e.eventId == EventList.CLEAR_CONSTRUCTION)
		{
			GameClient.game.gameModel.clearConstructions();
		}
		else if(e.eventId == EventList.INCREMENT_CITADEL_ROUNDS)
		{
			GameClient.game.gameModel.incrementCitadelRounds();
		}
		else if(e.eventId == EventList.MOVE_THINGS)
		{
			int playerIndex = Integer.parseInt(e.eventParams[0]);

			if(playerIndex == GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum())
			{
					
				GameClient.game.sendMessageToView("Please move your Things");		        	
						
				String thingsMovedParamsString = GameClient.game.gameView.moveIteration();
					
				String[] args = thingsMovedParamsString.split(" ");
						
				//send changes
				EventHandler.SendEvent(
						new Event()
							.EventId(EventList.MOVE_THINGS)
							.EventParameters(args)
				);
				
				GameClient.game.clearMessageOnView();
			}
			else
			{
				waitForOtherPlayer(e.expectsResponseEvent, playerIndex, "move their things");
			}
		}
		else if(e.eventId == EventList.HANDLE_MOVE_THINGS)
		{
			final int playerIndex = Integer.parseInt(e.eventParams[0]);
			
			if(e.eventParams.length == 2)
			{
				String thingsPlayedStrings = e.eventParams[1].trim();
				ArrayList<HexTile> hexTiles = new ArrayList<HexTile>();
				ArrayList<Integer> thingIDs = new ArrayList<Integer>();
				GameClient.game.parseMovedThingsStrings(thingsPlayedStrings, hexTiles, thingIDs);
				
				GameClient.game.gameModel.updateMovedThings(hexTiles, thingIDs, playerIndex);
				
				final ArrayList<HexTile> hexTilesCopy = hexTiles;
				
				Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
						GameClient.game.gameView.updateTiles(hexTilesCopy, playerIndex);	
			        }
				});
			}
		}
		else if(e.eventId == EventList.BRIBE_CREATURES)
		{
			final int playerIndex = Integer.parseInt(e.eventParams[0]);
			ArrayList<Thing> things = new ArrayList<Thing>();
			final HexTile h = GameClient.game.parseBribeString(e.eventParams[1], things);
			
			GameClient.game.gameModel.handleBribe(h, things, playerIndex);
			
			final int gold = GameClient.game.gameModel.GetPlayer(playerIndex).getGold();
			
			GameClient.game.updatePlayerRack(playerIndex);
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
					GameClient.game.gameView.updateTiles(h, 4);
					GameClient.game.gameView.updateGold(gold, playerIndex);
		        }
			});
		}
		else if(e.eventId == EventList.CREATE_DEFENSE_CREATURES)
		{
			int playerIndex = Integer.parseInt(e.eventParams[0]);
			int roll = Integer.parseInt(e.eventParams[1]);
			int x = Integer.parseInt(e.eventParams[2]);
			int y = Integer.parseInt(e.eventParams[3]);
			final HexTile h = GameClient.game.gameModel.gameBoard.getTile(x, y);
			
			GameClient.game.createDefenseCreatures(roll, x, y);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
					GameClient.game.gameView.updateTiles(h, 4);
		        }
			});
		}
		else if(e.eventId == EventList.HANDLE_RACK_OVERLOAD)
		{
			final int playerIndex = Integer.parseInt(e.eventParams[0]);
			
			GameClient.game.gameModel.handleRackOverload(playerIndex);
			
			GameClient.game.updatePlayerRack(playerIndex);
		}
		else if(e.eventId == EventList.PAY_GOLD)
		{
			final int playerIndex = Integer.parseInt(e.eventParams[0]);
			final int gold = Integer.parseInt(e.eventParams[1]);
			
			GameClient.game.gameModel.playerFromIndex(playerIndex).payGold(gold);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        		GameClient.game.gameView.playerList.getPlayerPanel(playerIndex).payGold(gold);
		        }
			});
		} else if (e.eventId == EventList.GET_RETREAT ){
			int tileX = Integer.parseInt(e.eventParams[0]);
			int tileY = Integer.parseInt(e.eventParams[1]);
			
			Player currentPlayer = GameClient.game.gameModel.GetCurrentPlayer();
			List<Integer> playersOnTile = GameClient.game.gameModel.boardController
											.PlayersOnTile(tileX, tileY);
			
			if (playersOnTile.contains(currentPlayer.GetPlayerNum())) {
				/*System.out.println("Would you like to retreat (y/n)?");
				char answer = 'a';
				
				do {
					BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
					try {
						answer = bufferRead.readLine().charAt(0);
					} catch (Exception ex){}
					
					System.out.println(answer);
				} while (answer != 'y' && answer != 'n');*/
				
				boolean retreat = GameView.battleView.GetSurrender();
				String answer;
				if (retreat == true){
					answer = "y";
				} else {
					answer = "n";
				}
				
				EventHandler.SendEvent(
					new Event()
						.EventId(EventList.GET_RETREAT)
						.EventParameter("" + answer)
				);
			} else {
				SendNullEvent();
			} 
		} else if (e.eventId == EventList.SET_MESSAGE) {
			final String message = e.eventParams[0];
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	GameClient.game.gameView.displayMessage(message);
		        }
			});
		}
		else if(e.eventId == EventList.CLEAR_THING_MOVES)
		{
			GameClient.game.clearThingMoves();
		} else if (e.eventId == EventList.REMOVE_THING) {
			int thingId = Integer.parseInt(e.eventParams[0]);
			int x = Integer.parseInt(e.eventParams[1]);
			int y = Integer.parseInt(e.eventParams[2]);
			int playerId = Integer.parseInt(e.eventParams[3]);
			
			HexTile tile = GameClient.game.gameModel.boardController.GetTile(x,y);
			
			Thing thing = tile.getThingFromTileByID(thingId);
			GameClient.game.gameModel.handleElimination(thing, tile);
			GameClient.game.gameView.board.getTileByHex(tile).updateThings(playerId);
		} else if (e.eventId == EventList.ADD_THING){
			int x = Integer.parseInt(e.eventParams[0]);
			int y = Integer.parseInt(e.eventParams[1]);
			int combatValue = Integer.parseInt(e.eventParams[2]);
			Terrain terrain = Terrain.valueOf(e.eventParams[3]);
			int playerNum = Integer.parseInt(e.eventParams[4]);
			
			Creature creature = new Creature(terrain, combatValue);
			creature.setControlledBy(GameConstants.controlledByFromIndex(playerNum));
			
			for(int i = 5; i < e.eventParams.length; i++) {
				if(e.eventParams[i].equals("Magic")) { creature.Magic(true); }
				if(e.eventParams[i].equals("Ranged")) { creature.Ranged(true); }
				if(e.eventParams[i].equals("Charge")) { creature.Charge(true); }
				if(e.eventParams[i].equals("Flying")) { creature.Flying(true); }
			}
			
			HexTile tile = GameClient.game.gameModel.boardController.GetTile(x, y);
			tile.AddThingToTile(playerNum, creature);
			GameClient.game.gameView.board.getTileByHex(tile).updateThings();
			
		} else if (e.eventId == EventList.REMOVE_BLUFFS) {
			int tileX = Integer.parseInt(e.eventParams[0]);
			int tileY = Integer.parseInt(e.eventParams[1]);
			
			for(int playerNum = 0; playerNum < GameClient.game.gameModel.PlayerCount(); playerNum++) {
				BoardController boardController = GameClient.game.gameModel.boardController;
				ArrayList<Thing> bluffs = boardController.GetBluffs(tileX, tileY, playerNum);
				Player player = GameClient.game.gameModel.GetPlayer(playerNum);
				
				int[] removeThings = new int[bluffs.size()];
				for(int i = 0; i < bluffs.size(); i++) {
					removeThings[i] = bluffs.get(i).thingID;
				}
				
				boardController.RemoveThings(removeThings, player, tileX, tileY);
				GameView.battleView.RemoveThings(removeThings, playerNum);
			}
			
			boolean battleOver = GameClient.game.gameModel.boardController.PlayersOnTile(tileX, tileY).size() <= 1;
			
			if ( battleOver ){
				EventHandler.SendEvent(new Event().EventId(EventList.REMOVE_BLUFFS));
			} else {
				SendNullEvent();
			}
		}
		
		if (e.expectsResponseEvent && numberOfSends == 0){
				throw new Exception("Expected event to be sent, but number of events sent was " + numberOfSends);
		} else if (!e.expectsResponseEvent && numberOfSends != 0){
				throw new Exception("Expected event to not be sent, but number of events sent was " + numberOfSends );
		}

	}

	
	private static void waitForOtherPlayer(boolean expectsResponse, final int playerIndex, final String actionBeingTaken) {
		waitForOtherPlayer(expectsResponse, Integer.toString(playerIndex+1), actionBeingTaken);
	}
	
	private static void waitForOtherPlayer(boolean expectsResponse, final String playerNumString, final String actionBeingTaken) {
		String s = "Waiting for player" + playerNumString + " to " + actionBeingTaken + ".";

		GameClient.game.sendMessageToView("Waiting for player" + playerNumString + " to " + actionBeingTaken + ".");
		
		if(expectsResponse)
			SendNullEvent();		
	}

	private static void SendNullEvent(){
		EventHandler.SendEvent( new Event().EventId( EventList.NULL_EVENT ) );
	}
}
