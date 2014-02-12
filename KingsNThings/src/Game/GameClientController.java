package Game;

import java.util.ArrayList;

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
}
