package Game;

import Game.GameConstants.ThingType;

/*
 * This Class represents a random magic "Thing" in the Game,
 * providing specialized Magic functionality
 */
public class Magic extends Thing{

	public Magic(String name, String frontFileName)
	{
		super(ThingType.MAGIC, name, frontFileName);
	}
}
