package Game;

import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;

/*
 * This class adds the specific Terrain Lord Functionality to the Special Character class
 */
public class TerrainLord extends SpecialCharacter {
	private Terrain terrain;		//Terrain type that this Terrain Lord supports
	
	public TerrainLord(Terrain terrain, String backFileName, String frontFileName)
	{
		//force overloaded constructor
		super(ThingType.TERRAIN_LORD, backFileName, frontFileName);
		
		this.terrain = terrain;
	}
}
