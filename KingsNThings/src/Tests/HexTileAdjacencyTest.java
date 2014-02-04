package Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Game.Dice;
import Game.GameBoard;
import Game.GameConstants.Terrain;
import Game.HexTile;

public class HexTileAdjacencyTest {

	@Before
	public void setUp() throws Exception {

	}

	// This test may fail, but it is very unlikely
	@Test
	public void TestAdjacencies(){
		GameBoard myBoard = new GameBoard();
		
		for(int i=-3; i<4; i++)
		{
			for(int j=-3; j<4; j++)
			{
				myBoard.addHexTile(new HexTile(Terrain.SWAMP), i, j);
			}
		}
		
		HexTile centerTile = myBoard.getTile(0, 0);
		
		assertEquals(myBoard.isAdjacent(centerTile, myBoard.getTile(1,1)), true);
		assertEquals(myBoard.isAdjacent(centerTile, myBoard.getTile(1,0)), true);
		assertEquals(myBoard.isAdjacent(centerTile, myBoard.getTile(0,-1)), true);
		assertEquals(myBoard.isAdjacent(centerTile, myBoard.getTile(-1,-1)), true);
		assertEquals(myBoard.isAdjacent(centerTile, myBoard.getTile(-1,0)), true);
		assertEquals(myBoard.isAdjacent(centerTile, myBoard.getTile(0,1)), true);
		
		assertEquals(myBoard.isAdjacent(centerTile, myBoard.getTile(-1,1)), false);
		assertEquals(myBoard.isAdjacent(centerTile, myBoard.getTile(1,-1)), false);
		
		
		HexTile edgeTile = myBoard.getTile(3,3);
		assertEquals(myBoard.isAdjacent(edgeTile, myBoard.getTile(2,3)), true);
		assertEquals(myBoard.isAdjacent(edgeTile, myBoard.getTile(2,2)), true);
		assertEquals(myBoard.isAdjacent(edgeTile, myBoard.getTile(3,2)), true);
	}

}
