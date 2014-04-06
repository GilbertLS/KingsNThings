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
	private static int fortIndex = 0;
	
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

	public void decrementLevel() {
		switch(level)
		{
		case KEEP:
			level = Level.TOWER;
			combatValue = 1;
			frontFileName = GameConstants.TowerImageFront;
			backFileName = GameConstants.TowerImageBack;
			break;
		case CASTLE:
			level = Level.KEEP;
			combatValue = 2;
			frontFileName = GameConstants.KeepImageFront;
			backFileName = GameConstants.KeepImageBack;
			Ranged(false);
			break;
		}
	}


	public void upgrade() {
		switch(level)
		{
		case TOWER:
			level = Level.KEEP;
			combatValue = 2;
			frontFileName = GameConstants.KeepImageFront;
			backFileName = GameConstants.KeepImageBack;
			break;
		case KEEP:
			level = Level.CASTLE;
			combatValue = 3;
			frontFileName = GameConstants.CastleImageFront;
			backFileName = GameConstants.CastleImageBack;
			Ranged(true);
			break;
		case CASTLE:
			level = Level.CITADEL;
			combatValue = 4;
			frontFileName = GameConstants.CitadelImageFront;
			backFileName = GameConstants.CitadelImageBack;
			Ranged(false);
			Magic(true);
			break;
		default:
			break;
		}
	}

	public boolean isCitadel() {
		return level == Level.CITADEL;
	}
	
}
