package Game;

import java.util.ArrayList;

import javafx.application.Platform;
import Game.Networking.GameClient;
import gui.GameView;

public class GameClientController {
	public GameModel gameModel; 	//conceptual model of a Kings N' things Game
	public GameView gameView;		//View to display the game
	
	public GameClientController(GameView g)
	{
		gameModel = new GameModel();
		gameView = g;
		gameView.setController(this);
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
			String[] paramsString = s.split(" ");
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

	public void parseMovedThingsStrings(String[] thingsPlayedStrings,
			ArrayList<HexTile> tilesFrom, ArrayList<HexTile> tilesTo,
			ArrayList<Integer> thingIDs, int playerIndex) {
		
		for(String s: thingsPlayedStrings)
		{
			String[] paramsString = s.split(" ");
			String[] fromTileParamsString = paramsString[0].split("SPLIT");
			String[] toTileParamsString = paramsString[1].split("SPLIT");
				
			int tileFromX = Integer.parseInt(fromTileParamsString[0]);
			int tileFromY = Integer.parseInt(fromTileParamsString[1]);
			
			int tileToX = Integer.parseInt(toTileParamsString[0]);
			int tileToY = Integer.parseInt(toTileParamsString[1]);
			
			HexTile fromTile = gameModel.gameBoard.getTile(tileFromX, tileFromY);
			HexTile toTile = gameModel.gameBoard.getTile(tileToX, tileToY);
			
			int id = Integer.parseInt(paramsString[2]);
			
			/*
			if(thingIDs.contains(id))
			{
				int idIndex = thingIDs.indexOf(id);
				tilesTo.set(idIndex, toTile);
			}
			else
			{*/
				thingIDs.add(id);
				tilesFrom.add(fromTile);
				tilesTo.add(toTile);
			//}
		}
	}

	public ArrayList<HexTile> amalgamateHexTiles(ArrayList<HexTile> tilesFrom,
			ArrayList<HexTile> tilesTo) {
		ArrayList<HexTile> returnTiles = new ArrayList<HexTile>();
		
		for(HexTile h: tilesFrom)
		{
			returnTiles.add(h);
		}
		
		for(HexTile h: tilesTo)
		{
			returnTiles.add(h);
		}
		
		return returnTiles;
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
}
