package Game;

import Game.GameConstants.ThingType;

/*
 * This class extends the Combatant class to add specific Special Character functionality
 */
public class SpecialCharacter extends Combatant implements IIncomable{

	public SpecialCharacter(String name, int combatValue, String frontFileName)
	{
		super(ThingType.SPECIAL_CHARACTER, name, combatValue, frontFileName);
	}
	
	public SpecialCharacter IsFlying(boolean isFlying){
		super.isFlying = isFlying;
		return this;
	}
	
	public SpecialCharacter IsMagic(boolean isMagic){
		super.isMagic = isMagic;
		return this;
	}
	
	public SpecialCharacter IsRange(boolean isRange){
		super.isRange = isRange;
		return this;
	}
	
	public SpecialCharacter IsCharge(boolean isCharge){
		super.isCharge = isCharge;
		return this;
	}
	
	public int getIncome()
	{
		return 1;
	}
	
	//need method for special ability
}
