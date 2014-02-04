package Game;

import Game.GameConstants.SpecialType;
import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;

/*
 * This class extends the Combatant class to add Creatures' need to be supported by 
 * specific Terrain.
 * Terrain is a global enum declared in the GameConstants Class
 */
public class Creature extends Combatant{
	private Terrain terrain;	//terrain required to support this creature
	
	//temp
	private static int creatureNum = 0;
	
	public Creature(Terrain terrain)
	{
		super(ThingType.CREATURE, "Creature" + creatureNum, SpecialType.NONE, (int)Math.ceil(Math.random()*6));
		
		this.terrain = terrain;
	}
	
	public Creature(Terrain terrain, SpecialType specialType, int combatValue){
		super(ThingType.CREATURE, "Creature" + creatureNum, specialType, combatValue );
	}
}
