package Game;

import Game.GameConstants.ThingType;

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
	
	public Combatant(ThingType thingType, String name, int combatValue, String frontFileName)
	{
		super(thingType, name, frontFileName);
		
		this.combatValue = combatValue;
	}
	
	public int GetCombatValue(){
		return combatValue;
	}
	
	public boolean IsFlying(){
		return isFlying;
	}
	
	public boolean IsRange(){
		return isRange;
	}
	
	public boolean IsCharge(){
		return isCharge;
	}
	
	public boolean IsMagic(){
		return isMagic;
	}
}
