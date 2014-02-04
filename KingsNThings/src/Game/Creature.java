package Game;


import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;
import Game.Networking.Event;

/*
 * This class extends the Combatant class to add Creatures' need to be supported by 
 * specific Terrain.
 * Terrain is a global enum declared in the GameConstants Class
 */
public class Creature extends Combatant{
	private Terrain terrain;	//terrain required to support this creature
	
	public static int creatureNum = 0;
	public Creature(Terrain terrain, SpecialType specialType, String name, int attackValue, String frontFileName)
	{
		super(ThingType.CREATURE, specialType, name, attackValue, frontFileName);
		
		this.terrain = terrain;
	}
	
	
	
	
	public Creature IsFlying(boolean isFlying){
		super.isFlying = isFlying;
		return this;
	}
	
	public Creature IsMagic(boolean isMagic){
		super.isMagic = isMagic;
		return this;
	}
	
	public Creature IsRange(boolean isRange){
		super.isRange = isRange;
		return this;
	}
	
	public Creature IsCharge(boolean isCharge){
		super.isCharge = isCharge;
		return this;
	}
	
	public String toString()
	{
		String s = "";
		
		s+= terrain + " " + name + " " + combatValue + " " + frontFileName+ " " + backFileName + 
				" " + "Flying: " + isFlying+
				" " +"Magic: " + isMagic +
				" " +"Range: " + isRange +
				" " +"Charge: " + isCharge;
		
		return s;
	}
}
