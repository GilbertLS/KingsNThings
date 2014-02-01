package Game;

import java.util.ArrayList;
import java.util.List;

public class BoardController {
	private GameBoard gameBoard;
	
	public BoardController( 
			GameBoard gameBoard
	) {
		this.gameBoard = gameBoard;
	}
	
	public List<int[]> GetContestedZones(){
		List<int[]> conflictedTiles = new ArrayList<int[]>();
		int[] addTiles = new int[2];
		
		for(int i = -3; i < 4; i++){
			for(int j = -3; j < 4; j++){
				int numPlayers = 0;
	
				if (gameBoard.getTile(i, j) == null){ continue; }
				
				if (!gameBoard.getTile(i, j).player1Things.isEmpty()) { numPlayers++; }
				if (!gameBoard.getTile(i, j).player2Things.isEmpty()) { numPlayers++; }
				if (!gameBoard.getTile(i, j).player3Things.isEmpty()) { numPlayers++; }
				if (!gameBoard.getTile(i, j).player4Things.isEmpty()) { numPlayers++; }
				
				if(numPlayers > 1){
					conflictedTiles.add(new int[]{ i, j });
				}
			}
		}
		
		return conflictedTiles;
	}
	
	public void AddThingToTile(Thing thing, Player player, int tileX, int tileY){
		gameBoard.getTile(tileX, tileY).AddThingToTile(player, thing);
	}
}
