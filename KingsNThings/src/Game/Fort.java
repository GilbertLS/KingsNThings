package Game;

import Game.GameConstants.Level;
import Game.GameConstants.SpecialType;
import Game.GameConstants.ThingType;

/*
 * This class extends the Building class to add specific Fort functionality
 * The Level enum is defined in the GameConstants class
 */
public class Fort extends Building {
	private Level level;	//The current level of the Fort
	
	//temp
	private static int fortID = 0;
	public Fort()
	{
		super(ThingType.FORT, "Fort" + fortID, SpecialType.RANGED, (int)Math.ceil(Math.random()*6));
	}
}
