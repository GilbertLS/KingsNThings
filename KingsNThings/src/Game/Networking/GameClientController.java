package Game.Networking;

import java.util.Vector;

import Game.GameView;

public class GameClientController {
	private GameModel gameModel; 	//conceptual model of a Kings N' things Game
	private GameView gameView;		//View to display the game
	
	public GameClientController()
	{
		gameModel = new GameModel();
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
}
