package Game;

import Game.GameConstants.ThingType;

/*
 * This Class represents a random magic "Thing" in the Game,
 * providing specialized Magic functionality
 */
public class Magic extends Thing{

	//temp
	private static int magicID = 0;
	public Magic(String frontFileName)
	{
		super(ThingType.MAGIC, "Magic" + magicID++, frontFileName);
	}
}
