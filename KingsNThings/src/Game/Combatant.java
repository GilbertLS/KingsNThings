package Game;

import Game.GameConstants.ThingType;
import Game.Networking.GameClient;

/*
 * This class adds functionality to the Thing class to facilitate combat.
 */
public abstract class Combatant extends Thing{
	//The damage dealt by this combatant
	protected int combatValue;	

	protected boolean isFlying = false;
	protected boolean isRange = false;
	protected boolean isCharge = false;
	protected boolean isMagic = false;
	
	public Combatant(ThingType thingType, String name, SpecialType specialType, int combatValue, String frontFileName)
	{
		super(thingType, name, frontFileName);
		
		this.combatValue = combatValue;
		this.specialType = specialType;
	}
	public int GetCombatValue(){
		return combatValue;
	}
	public SpecialType GetType(){
		return specialType;
	}
}
