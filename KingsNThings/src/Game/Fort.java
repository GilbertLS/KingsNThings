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
	private static int fortIndex =0;
	
	public Fort()
	{
		super(ThingType.FORT,  "Fort" + fortIndex++, 1, GameConstants.TowerImageBack, GameConstants.TowerImageFront);
		level = Level.TOWER;
	}
	
	public int getIncome()
	{
		return combatValue;
	}
	
	public Level getLevel() {
		return level;
	}
}
