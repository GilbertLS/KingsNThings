package Game;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import Game.GameConstants.ControlledBy;
import Game.GameConstants.CurrentPhase;
import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;
import Game.Phases.Phase;
import Game.Networking.Event;
import Game.Networking.EventHandler;
import Game.Networking.EventList;
import Game.Networking.GameClient;
import gui.EditStateWindow;
import gui.GameView;
import gui.ThingView;
import gui.Tile;

public class GameClientController {
	public GameModel gameModel; 	//conceptual model of a Kings N' things Game
	public GameView gameView;		//View to display the game
	
	public GameClientController(GameView g)
	{
		gameModel = new GameModel();
		gameView = g;
	}

	public void setPlayerOrders(int firstPlayerIndex) {
		gameModel.setPlayerOrder(firstPlayerIndex);
	}

	public void setPlayerCount(int numPlayers) {
		gameModel.setPlayerCount(numPlayers);
	}

	public void updatePlayerOrder() {
		gameModel.updatePlayerOrder();
		
	}

	public void parsePlayedThingsStrings(
			String[] thingsPlayedStrings, ArrayList<HexTile> hexTiles, ArrayList<Integer> thingIDs, int playerIndex) {
		
		for(String s: thingsPlayedStrings)
		{
			String[] paramsString = s.split("~");
			String[] hexParamsString = paramsString[0].split("SPLIT");
				
			int x = Integer.parseInt(hexParamsString[0]);
			int y = Integer.parseInt(hexParamsString[1]);
			HexTile currTile = gameModel.gameBoard.getTile(x, y);
			
			int id = Integer.parseInt(paramsString[1]);
			if(thingIDs.contains(id))
			{
				int idIndex = thingIDs.indexOf(id);
				thingIDs.remove(idIndex);
				hexTiles.remove(idIndex);
			}
			thingIDs.add(id);
			hexTiles.add(currTile);
		}
		for(int i=0; i<thingIDs.size(); i++)
		{
			HexTile h = hexTiles.get(i);
			System.out.println("HEXTILE: x-" + h.x + " y-" +h.y+ " THINGID: " + thingIDs.get(i));
		}
	}	
	
	public ArrayList<HexTile> parseToUniqueHexTiles(ArrayList<HexTile> hexTiles) {
		ArrayList<HexTile> seenTiles = new ArrayList<HexTile>();
		
		for(HexTile h: hexTiles)
		{
			if(!seenTiles.contains(h))
				seenTiles.add(h);
		}
		
		return seenTiles;
	}

	public void parseMovedThingsStrings(String thingsPlayedStrings,
			ArrayList<HexTile> hexTiles,
			ArrayList<Integer> thingIDs) {
		
		String[] paramsString = thingsPlayedStrings.split("~");
		String[] fromTileParamsString = paramsString[0].split("SPLIT");
		String[] toTileParamsString = paramsString[1].split("SPLIT");
				
		int tileFromX = Integer.parseInt(fromTileParamsString[0]);
		int tileFromY = Integer.parseInt(fromTileParamsString[1]);
		hexTiles.add(gameModel.gameBoard.getTile(tileFromX, tileFromY));
			
		int tileToX = Integer.parseInt(toTileParamsString[0]);
		int tileToY = Integer.parseInt(toTileParamsString[1]);
		hexTiles.add(gameModel.gameBoard.getTile(tileToX, tileToY));
			
		String[] IDStrings = paramsString[2].split("/");
			
		for(String s: IDStrings)
		{
			thingIDs.add(Integer.parseInt(s));
		}
	}
	
	public void sendMessageToView(final String message)
	{
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	gameView.displayMessage(message);		        	
	        }
		});
	}
	
	public void clearMessageOnView()
	{
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	gameView.clearMessage();		        	
	        }
		});
	}

	public boolean rollForCreatures(int playerIndex, int x, int y) {
		int[] roll = Dice.rollDice(1);
		
		boolean defendingCreatures = false;
		if(roll[0] != 1 && roll[0] != 6)
		{
			createDefenseCreatures(roll[0], x, y);
			sendCreateDefenseCreaturesEvent(playerIndex, roll[0], x, y);
			
			defendingCreatures = true;
		}
		else
		{
			gameModel.claimNewTile(playerIndex, x, y);
		}

		
		return defendingCreatures;
	}

	private void sendCreateDefenseCreaturesEvent(int playerIndex, int roll, int x, int y) {
		String[] args = {""+ playerIndex, ""+ roll, ""+x, ""+y};
		
		boolean[] intendedPlayers = new boolean[GameClient.game.gameModel.PlayerCount()];
		for(int i=0; i<intendedPlayers.length; i++)
			if (i != GameClient.game.gameModel.getCurrPlayerNumber())
				intendedPlayers[i] = true;
		
		Event gameEvent = new Event()
			.IntendedPlayers(intendedPlayers)
			.EventId(EventList.CREATE_DEFENSE_CREATURES)
			.EventParameters(args);
	
		Game.Networking.EventHandler.SendEvent(gameEvent);	
	}

	public void createDefenseCreatures(int roll, int x, int y) {
		HexTile h = gameModel.gameBoard.getTile(x, y);
		ArrayList<Thing> things = gameModel.getThingsFromCup(roll);
		
		//eliminate invalid defense creatrues
		things = h.enforceValidDefense(things);
		
		for(Thing t: things)
			if(t.isFlipped())
				t.setFlipped(false);
		
		for(Thing t: things)
			h.AddThingToTile(4, t);
	}

	public boolean isValidMove(HexTile originalTile, HexTile tileRef,
			ArrayList<Thing> things) {
		
		//invalid if not land and not all flying
		if(!tileRef.isLand() && !GameClient.game.areAllFlying(things))
			return false;
		
		if(!tileRef.hasRoomForThings(things))
			return false;
		
		if(!originalTile.isAdjacent(tileRef)
				|| (!tileRef.isLand() && !areAllFlying(things))
				|| (!allThingsAbleToMove(tileRef, things)))
			return false;
			
		return true;
	}

	private boolean areAllFlying(ArrayList<Thing> things) {
		for(Thing t: things)
		{
			if(!((Combatant)t).isFlying)
				return false;
		}
		
		return true;
	}

	private boolean allThingsAbleToMove(HexTile tileRef, ArrayList<Thing> things) {		
		for(Thing t: things)
		{
			if(t.numMoves + tileRef.moveValue > GameConstants.MAX_MOVES_PER_TURN)
				return false;
		}
	
		return true;
	}
	
	public void clearThingMoves()
	{
		gameModel.clearThingMoves();
	}

	public void cashTreasure() {
		// TODO Auto-generated method stub
		
	}

	public boolean isValidPlacement(HexTile tileRef, ArrayList<Thing> things, int playerIndex) {
		ControlledBy tileControl = tileRef.getControlledBy();
		
		//invalid if not land and not all flying
		if(!tileRef.isLand() && !GameClient.game.areAllFlying(things))
			return false;
		
		//invalid if thing not controlled
		if(!things.get(0).isControlledBy(tileControl))
			return false;
		
		if(!tileRef.hasRoomForThings(things))
			return false;
		
		//check for multiple special incomes
		int numIncomes =0;		
		for(Thing t: things)
		{
			if(t.thingType == ThingType.SPECIAL_INCOME || t.thingType == ThingType.SETTLEMENT)
			{
				numIncomes++;
				
				if(tileRef.hasSpecialIncome() || tileRef.hasSettlement() || numIncomes > 1) 
					return false;
				
				if(t.thingType == ThingType.SPECIAL_INCOME && tileRef.terrain != ((SpecialIncome)t).getTerrain())
					return false;
				
			}
		}
			
		return true;
	}

	public HexTile parseConstructionString(String constructionString) {
		String[] params = constructionString.split("SPLIT");
		int x = Integer.parseInt(params[0]);
		int y = Integer.parseInt(params[1]);
		
		return gameModel.gameBoard.getTile(x, y);
	}

	public void augmentRoll(int amount,
			int playerIndex) {
		GameClient.game.gameModel.augmentRoll(amount, playerIndex);
		GameClient.game.gameView.updateGold(gameModel.playerFromIndex(playerIndex).getGold(), playerIndex);			
	}

	public void sendSpendGoldEvent(int amount, int playerIndex) {
		
		boolean[] intendedPlayers = new boolean[GameClient.game.gameModel.PlayerCount()];
		
		for(int i=0; i<GameClient.game.gameModel.PlayerCount(); i++)
			if(i != playerIndex)
				intendedPlayers[i] = true;
		
		String[] args = {""+amount,""+playerIndex};
		
		Event gameEvent = new Event()
			.EventId(EventList.HANDLE_SPEND_GOLD)
			.IntendedPlayers(intendedPlayers)
			.EventParameters(args);
	
		Game.Networking.EventHandler.SendEvent(gameEvent);	
	}

	public void sendRecruitSpecialCharacterEvent(
			int thingID, int playerIndex) {
		
		String[] args = {""+thingID,""+playerIndex};
		
		Event gameEvent = new Event()
			.EventId(EventList.HANDLE_RECRUIT_CHARACTER)
			.EventParameters(args);
	
		Game.Networking.EventHandler.SendEvent(gameEvent);	
	}

	public void updatePlayerRack(final int playerIndex) {
		final Player playerToUpdate = GameClient.game.gameModel.playerFromIndex(playerIndex);
		final boolean isCurrentPlayer = playerToUpdate == GameClient.game.gameModel.GetCurrentPlayer();
		
		final ArrayList<Thing> thingsInRack = playerToUpdate.getPlayerRack().getThings();
		
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	GameClient.game.gameView.updateRackCount(thingsInRack.size(), playerIndex);
	        	if(isCurrentPlayer)
	        		GameClient.game.gameView.updatePlayerRack(playerToUpdate.playerRack.getThings());
	        }
		});		
	}

	public ArrayList<HexTile> parseInitialHexTiles(String[] boardHexTileStrings) {
		ArrayList<HexTile> tiles = new ArrayList<HexTile>();
		
		for(String s: boardHexTileStrings)
		{
			tiles.add(new HexTile(Terrain.valueOf(s)));
		}
		
		return tiles;
	}
	
	public boolean validDragStart(GameView gv, ListView<ThingView> listView, ArrayList<Integer> selectedIds) {
		HexTile hexTile = gv.tilePreview.getTile().getTileRef();
		
		//invalid if play things phase, and source isn't rack
		if(!listView.equals(gv.rack) && gv.currentPhase == CurrentPhase.PLAY_THINGS)
			return false;
		
		//invalid if a thing isn't owned by current player
		ObservableList<ThingView> items = listView.getItems();
		for(Integer i: selectedIds)
			if(items.get(i).thingRef.getControlledByPlayerNum() != GameClient.game.gameView.getCurrentPlayer())
				return false;
		
		
		ArrayList<Thing> movingThings = new ArrayList<Thing>();
		for(ThingView tv: items)
			movingThings.add(tv.thingRef);
		
		ArrayList<Thing> flyers = getFlyers(hexTile.GetThings(gv.getCurrentPlayer()));
		int numLeft = flyers.size() - movingThings.size();
		
		int numFlyingDefend =0;	//total flying defenders
		for(int i=0; i<5; i++)
		{
			if(i != gv.getCurrentPlayer()){
				numFlyingDefend += getFlyers(hexTile.GetThings(i)).size();
			}
		}
		//invalid if movement phase and player isn't alone on the tile
		//unless all are flying and no flying enemies
		if(gv.currentPhase == CurrentPhase.MOVEMENT
			&& !hexTile.isOnlyCombatantPlayerOnTile(gv.getCurrentPlayer())
			&& !(GameClient.game.areAllFlying(movingThings) && numLeft >= numFlyingDefend))
				return false;
		
		//invalid if recruit character phase and the dragged items
		//does not consist of exactly 1 special character
		if(gv.currentPhase == CurrentPhase.RECRUIT_CHARACTER)
			if(selectedIds.size() != 1
				|| !items.get(selectedIds.get(0)).thingRef.isSpecialCharacter())
				return false;
		
		return true;
	}

	private ArrayList<Thing> getFlyers(ArrayList<Thing> things) {
		ArrayList<Thing> flyers = new ArrayList<Thing>();
		
		for(Thing t: things){
			if(((Combatant)t).isFlying);
			flyers.add(t);
		}
		
		return flyers;
	}

	public void sendPlayThingEvent(String param) {
		String[] params = {"" + gameModel.getCurrPlayerNumber(), param};
		
		//if so, send event out to update other players
		Event gameEvent = new Event()
			.EventId(EventList.HANDLE_PLAY_THINGS)
			.EventParameters(params);

		Game.Networking.EventHandler.SendEvent(gameEvent);	
	}
	
	public void parseEdit(final EditStateWindow edit) {
		if (edit.addThingButton.isSelected()) {
			Thread t = new Thread( new Runnable() {
				@Override
				public void run() {
					hideMenu();
					Tile selectedTile = GameClient.game.gameView.chooseTileFromEditState();
					
					int combatValue = edit.combatValueDropDown.getSelectionModel().getSelectedItem();
					Terrain terrain = edit.hexTypeDropDown.getSelectionModel().getSelectedItem();		
					ControlledBy controlledBy = edit.controlledByDropDown.getSelectionModel().getSelectedItem();
				
					int extraSize = 0;
					if(edit.magicButton.isSelected() || edit.rangedButton.isSelected()) { extraSize++; }
					if(edit.chargeButton.isSelected()) { extraSize++; }
					if(edit.flyingButton.isSelected()) { extraSize++; }
				
					String[] params = new String[5 + extraSize];
					params[0] = "" + selectedTile.getTileRef().x;
					params[1] = "" + selectedTile.getTileRef().y;
					params[2] = "" + combatValue;
					params[3] = terrain.name();
					params[4] = "" + GameConstants.GetPlayerNumber(controlledBy);
 					
					int currParam = 5;
					if(edit.magicButton.isSelected()) { params[currParam++] = "Magic"; } 
					else if(edit.rangedButton.isSelected()) { params[currParam++] = "Ranged"; }
	
					if (edit.chargeButton.isSelected()) { params[currParam++] = "Charge"; } 
					if (edit.flyingButton.isSelected()) { params[currParam++] = "Flying"; }	
					
					Event e = new Event().EventId(EventList.ADD_THING).EventParameters(params);
					EventHandler.SendEvent(e);
				}
			});
			
			t.start();
		} else if (edit.removeThingButton.isSelected()) {
			Thread t = new Thread( new Runnable() {
				@Override
				public void run() {
					hideMenu();
					Tile selectedTile = GameClient.game.gameView.chooseTileFromEditState();
					GameView.selectedThings = new ArrayList<Thing>();
					while(GameView.selectedThings.size() != 1) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {}
					}
					Thing thing = GameView.selectedThings.get(0);
					
					String[] params = new String[4];
					params[0] = "" + thing.thingID;
					params[1] = "" + selectedTile.getTileRef().x;
					params[2] = "" + selectedTile.getTileRef().y;
					params[3] = "" + GameConstants.GetPlayerNumber(thing.getControlledBy());
					
					Event e = new Event()
								.EventId(EventList.REMOVE_THING)
								.EventParameters(params);
					
					EventHandler.SendEvent(e);
				}
			});
			
			t.start();
		} else if (edit.setPhaseButton.isSelected()) {
			Thread t = new Thread( new Runnable() {
				@Override
				public void run() {
					Phase phase = edit.changePhaseDropDown.getSelectionModel().getSelectedItem();
					hideMenu();
					
					Event e = new Event()
						.EventId(EventList.SET_PHASE)
						.EventParameter("" + phase.name());
					
					EventHandler.SendEvent(e);
				}
			});
			
			t.start();
		} else {
			System.out.println("Error in GameClientController.parseEdit");
		}
	}
	
	private void hideMenu() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				GameClient.game.gameView.escapeMenu.hideMenu();
			}
		});
	}

	public void sendBribeCreaturesEvent(HexTile h, ArrayList<Thing> selectedThings, int playerIndex) {
		String param = h.x + "SPLIT" + h.y + "~";
		
		for(Thing t: selectedThings){
			param += t.GetThingId() + " ";
		}			
		
		String[] params = {""+playerIndex, param};
		
		boolean[] intendedPlayers = new boolean[gameModel.PlayerCount()];
		for(int i=0; i<intendedPlayers.length; i++){
			intendedPlayers[i]=true;
		}
		intendedPlayers[playerIndex] = false;
		
		Event gameEvent = new Event()
			.EventId(EventList.BRIBE_CREATURES)
			.IntendedPlayers(intendedPlayers)
			.EventParameters(params);

		Game.Networking.EventHandler.SendEvent(gameEvent);	
		
	}

	public boolean validBribe(ArrayList<Thing> selectedThings, HexTile h) {
		Player currentPlayer = gameModel.GetCurrentPlayer();
		int cost = gameModel.calculateBribe(selectedThings, h);
		
		//valid if current player can afford
		if(currentPlayer.canAfford(cost))
			return true;
		
		return false;
	}

	public HexTile parseBribeString(String eventParam, ArrayList<Thing> things) {
		String[] params = eventParam.split("~");
		
		String[] hexParams = params[0].split("SPLIT");
		int x = Integer.parseInt(hexParams[0]);
		int y = Integer.parseInt(hexParams[1]);
		HexTile h = gameModel.gameBoard.getTile(x, y);
		
		String[] thingIDParams = params[1].trim().split(" ");
		for(String s: thingIDParams)
		{
			int id = Integer.parseInt(s);
			Thing t = h.getThingFromTileByID(id);
			
			things.add(t);
		}
		
		return h;
	}
}
