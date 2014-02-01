package Game;

import Game.GameConstants.SpecialType;
import Game.GameConstants.ThingType;
import Game.Networking.GameClient;

/*
 * This class adds functionality to the Thing class to facilitate combat.
 */
public abstract class Combatant extends Thing{
	//The damage dealt by this combatant
	private int combatValue;	
	
	//Combatant variation
	private SpecialType specialType;
	
	public Combatant(ThingType thingType, String name, SpecialType specialType, int combatValue)
	{
		super(thingType, name);
		
		this.combatValue = combatValue;
		this.specialType = specialType;
	}
	
	//public int Attack(){
	
	//}
}
