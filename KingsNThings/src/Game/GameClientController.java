package Game;

import java.util.ArrayList;

import javafx.application.Platform;
import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;
import Game.Networking.GameClient;
import gui.GameView;

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
		int roll = 1; //hard coded for iteration 1
		
		boolean defendingCreatures = false;
		if(roll != 1 && roll != 6)
		{
			defendingCreatures = true;
		}
		else
		{
			gameModel.updateTileFaction(playerIndex, x, y);
		}

		
		return defendingCreatures;
	}

	public boolean isValidMove(HexTile originalTile, HexTile tileRef,
			ArrayList<Thing> things) {
		
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

	public boolean isValidPlacement(HexTile tileRef, ArrayList<Thing> things) {
		if(tileRef.controlledBy != things.get(0).controlledBy)
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
}
