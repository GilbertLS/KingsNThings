package Game;

import Game.GameConstants.SpecialType;
import Game.GameConstants.ThingType;

/*
 * This class extends the Combatant class to add specific Special Character functionality
 */
public class SpecialCharacter extends Combatant implements IIncomable{

	//temp
	private static int specialCharacterID = 0;
	public SpecialCharacter()
	{
		super(ThingType.SPECIAL_CHARACTER, "specialCharacter" + specialCharacterID, SpecialType.NONE, (int)Math.ceil(Math.random()*6));
		this.currentTile = null;
	}
	
	//temp (for terrain lord)
	private static int terrainLordID = 0;
	public SpecialCharacter(int temp)
	{
		super(ThingType.TERRAIN_LORD, "terrainLord" + terrainLordID, SpecialType.NONE, (int)Math.ceil(Math.random()*6));
		this.currentTile = null;
	}
	
	//need method for special ability
}
