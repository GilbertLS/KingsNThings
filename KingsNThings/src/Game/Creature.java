package Game;


import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;

/*
 * This class extends the Combatant class to add Creatures' need to be supported by 
 * specific Terrain.
 * Terrain is a global enum declared in the GameConstants Class
 */
public class Creature extends Combatant{
	private Terrain terrain;	//terrain required to support this creature
	
	public static int creatureNum = 0;
	
	public Creature(Terrain terrian)
	{
		super(ThingType.CREATURE, "creature"+creatureNum++, (int)Math.ceil(Math.random()*6), GameConstants.PlaceHolderImageFront);
		
		this.terrain = terrian;
	}
	
	public Creature(Terrain terrain, int combatValue){
		super(ThingType.CREATURE, "creature"+creatureNum++, combatValue, GameConstants.PlaceHolderImageFront);
		
		this.terrain = terrain;
	}
	
	public Creature(Terrain terrain, String name, int attackValue, String frontFileName)
	{
		super(ThingType.CREATURE, name, attackValue, frontFileName);
		
		this.terrain = terrain;
	}
	
	public Creature Flying(boolean isFlying){
		super.isFlying = isFlying;
		return this;
	}
	
	public Creature Magic(boolean isMagic){
		super.isMagic = isMagic;
		return this;
	}
	
	public Creature Ranged(boolean isRange){
		super.isRange = isRange;
		return this;
	}
	
	public Creature Charge(boolean isCharge){
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
