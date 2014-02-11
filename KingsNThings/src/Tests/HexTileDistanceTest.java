package Tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Game.GameBoard;
import Game.HexTile;
import Game.GameConstants.Terrain;

public class HexTileDistanceTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDistances() {
		GameBoard myBoard = new GameBoard();
		
		for(int i=-3; i<4; i++)
		{
			for(int j=-3; j<4; j++)
			{
				myBoard.addHexTile(new HexTile(Terrain.SWAMP), i, j);
			}
		}
		
		HexTile centerTile = myBoard.getTile(0, 0);
		
		assertEquals(centerTile.distance(myBoard.getTile(1,1)), 1);
		assertEquals(centerTile.distance(myBoard.getTile(1,0)), 1);
		assertEquals(centerTile.distance(myBoard.getTile(0,-1)), 1);
		assertEquals(centerTile.distance(myBoard.getTile(-1,-1)), 1);
		assertEquals(centerTile.distance(myBoard.getTile(-1,0)), 1);
		assertEquals(centerTile.distance(myBoard.getTile(0,1)), 1);
		
		assertEquals(centerTile.distance(myBoard.getTile(-1,1)), 2);
		assertEquals(centerTile.distance(myBoard.getTile(1,-1)), 2);
	}

}
