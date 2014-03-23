package Game;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import Game.GameConstants.ControlledBy;
import Game.GameConstants.CurrentPhase;
import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;
import Game.Networking.Event;
import Game.Networking.EventList;
import Game.Networking.GameClient;
import gui.GameView;
import gui.ThingView;

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
		int[] roll = Dice.rollDice(1); //hard coded for iteration 1
		
		boolean defendingCreatures = false;
		if(roll[0] != 1 && roll[0] != 6)
		{
			createDefenseCreatures(roll[0], x, y);
			sendCreateDefenseCreaturesEvent(playerIndex, roll[0], x, y);
			
			defendingCreatures = true;
		}
		else
		{
			gameModel.updateTileFaction(playerIndex, x, y);
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
		
		//creating defense creatures implies this tile should be neutral
		h.controlledBy = ControlledBy.NEUTRAL;
		
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
			if(t.numMoves + tileRef.moveValue > 4)
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
		ControlledBy tileControl = tileRef.controlledBy;
		
		if(tileControl != things.get(0).controlledBy)
			return false;
		
		if(!tileRef.hasRoomForThings(things))
			return false;
		
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

	public void handleRackOverload(Integer playerIndex) {
		//check if player has too many things in rack
		if(gameModel.playerRackTooFull(playerIndex))
		{			
			//if so, send event out to update other players
			Event gameEvent = new Event()
				.EventId(EventList.HANDLE_RACK_OVERLOAD)
				.EventParameter(""+playerIndex);
	
			Game.Networking.EventHandler.SendEvent(gameEvent);	
		}
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
		//invalid if play things phase, and source isn't rack
		if(!listView.equals(gv.rack) && gv.currentPhase == CurrentPhase.PLAY_THINGS)
			return false;
		
		//invalid if a thing isn't owned by current player
		ObservableList<ThingView> items = listView.getItems();
		for(Integer i: selectedIds)
			if(items.get(i).thingRef.getControlledByPlayerNum() != GameClient.game.gameView.getCurrentPlayer())
				return false;
		
		//invalid if movement phase and player isn't alone on the tile
		if(gv.currentPhase == CurrentPhase.MOVEMENT
			&& !gv.tilePreview.getTile().getTileRef().isOnlyPlayerOnTile(gv.getCurrentPlayer()))
				return false;
		
		//invalid if recruit character phase and the dragged items
		//does not consist of exactly 1 special character
		if(gv.currentPhase == CurrentPhase.RECRUIT_CHARACTER)
			if(selectedIds.size() != 1
				|| !items.get(selectedIds.get(0)).thingRef.isSpecialCharacter())
				return false;
		
		return true;
	}

	public void sendPlayThingEvent(String param) {
		String[] params = {"" + gameModel.getCurrPlayerNumber(), param};
		
		//if so, send event out to update other players
		Event gameEvent = new Event()
			.EventId(EventList.HANDLE_PLAY_THINGS)
			.EventParameters(params);

		Game.Networking.EventHandler.SendEvent(gameEvent);	
	}
}
