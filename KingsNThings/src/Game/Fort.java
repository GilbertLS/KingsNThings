package Game;

import Game.GameConstants.Level;
import Game.GameConstants.ThingType;
import Game.GameConstants;

/*
 * This class extends the Building class to add specific Fort functionality
 * The Level enum is defined in the GameConstants class
 */
public class Fort extends Building implements IIncomable{
	private Level level;	//The current level of the Fort
	
	public Fort(ThingType thingType, String name, int attackValue)
	{
		super(thingType,  "Fort", 1, GameConstants.TowerImageBack, GameConstants.TowerImageFront);
		level = Level.TOWER;
		isRange = true;
	}
	
	public int getIncome()
	{
		return combatValue;
	}
}
