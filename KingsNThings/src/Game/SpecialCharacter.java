package Game;

import Game.GameConstants.ThingType;

/*
 * This class extends the Combatant class to add specific Special Character functionality
 */
public class SpecialCharacter extends Combatant implements IIncomable{

	//temp
	private static int specialCharacterID = 0;
	public SpecialCharacter(String backFileName, String frontFileName)
	{
		super(ThingType.SPECIAL_CHARACTER, "specialCharacter" + specialCharacterID++, (int)Math.ceil(Math.random()*6), frontFileName);

		this.backFileName = backFileName;
	}
	
	public SpecialCharacter(ThingType thingType, String backFileName, String frontFileName)
	{
		super(thingType, "terrianLord" + specialCharacterID++, (int)Math.ceil(Math.random()*6), frontFileName);
		
		this.backFileName = backFileName;
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
