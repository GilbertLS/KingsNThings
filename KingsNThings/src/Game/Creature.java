package Game;

import Game.GameConstants.Terrain;

/*
 * This class extends the Combatant class to add Creatures' need to be supported by 
 * specific Terrain.
 * Terrain is a global enum declared in the GameConstants Class
 */
public class Creature extends Combatant{
	private Terrain terrain;	//terrain required to support this creature
}
