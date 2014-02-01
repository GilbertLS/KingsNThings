package Game;

import Game.GameConstants.Terrain;

/*
 * This class adds the specific Terrain Lord Functionality to the Special Character class
 */
public class TerrainLord extends SpecialCharacter {
	private Terrain terrain;		//Terrain type that this Terrain Lord supports
	
	public TerrainLord(Terrain terrain)
	{
		//force overloaded constructor
		super(1);
		
		this.terrain = terrain;
	}
}