package Game;

/*
 * This class holds the HexTiles in play currently
 * Please see <document to explain coordinate system>
 * for an explanation of the coordinate system used
 */
public class GameBoard {
	private HexTile[][] boardPieces = new HexTile[7][7]; 	//the array of Hex Tiles in play
	private final int ARRAY_X_OFFSET = 3;
	private final int ARRAY_Y_OFFSET = 3;
	public int dimensions = 7;
	
	public GameBoard()
	{
	}
	
	public void addHexTile(HexTile hexTile, int x, int y)
	{
		hexTile.x = x;
		hexTile.y = y;
		boardPieces[x+ARRAY_X_OFFSET][y+ARRAY_Y_OFFSET] = hexTile;
	}

	public HexTile getTile(int x, int y) {
		return boardPieces[x+ARRAY_X_OFFSET][y+ARRAY_Y_OFFSET];
	}

	public void printCurrentTiles() {
		for(int i=0; i<dimensions; i++){
			for(int j=0; j<dimensions; j++){
				if (boardPieces[i][j] == null)
					continue;
				
				System.out.print(boardPieces[i][j].getTerrain()+" ");
			}
			System.out.println();
		}
		
	}

	public HexTile[][] getTiles() {
		return boardPieces;
	}
}
