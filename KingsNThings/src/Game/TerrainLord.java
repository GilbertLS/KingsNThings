package Game;

import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;

/*
 * This class adds the specific Terrain Lord Functionality to the Special Character class
 */
public class TerrainLord extends SpecialCharacter {
	private Terrain terrain;		//Terrain type that this Terrain Lord supports
	
	public Terrain getTerrain() { return terrain; }
	
	public TerrainLord(Terrain terrain, String name, int combatValue, String frontFileName)
	{
		//force overloaded constructor
		super(ThingType.TERRAIN_LORD, name, combatValue, frontFileName);
		
		this.terrain = terrain;
	}
}
