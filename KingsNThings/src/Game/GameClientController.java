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

	public ArrayList<HexTile> parsePlayedThingsStrings(
			String[] thingsPlayedStrings) {
		
		ArrayList<HexTile> hexTiles = new ArrayList<HexTile>();
		
		for(String s: thingsPlayedStrings)
		{
			String[] paramsString = s.split(" ");
			String[] hexParamsString = paramsString[0].split("SPLIT");
				
			int x = Integer.parseInt(hexParamsString[0]);
			int y = Integer.parseInt(hexParamsString[1]);
				
			int thingID = Integer.parseInt(paramsString[1]);
				
			hexTiles.add(gameModel.gameBoard.getTile(x, y));
		}
		
		return hexTiles;
	}	
}
