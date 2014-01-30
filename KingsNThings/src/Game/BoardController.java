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
		
		for(int i = 0; i < gameBoard.dimensions; i++){
			for(int j = 0; j < gameBoard.dimensions; j++){
				int numPlayers = 0;
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
}
