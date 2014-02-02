package Game;

import Game.GameConstants.ThingType;

/*
 * This Class represents a random treasure "Thing" in the Game,
 * providing specialized Treasure functionality
 */
public class Treasure extends Thing{
	//temp
	private static int treasureID = 0;
	public Treasure(String backFileName, String frontFileName)
	{
		super(ThingType.TREASURE, "Treasure" + treasureID++, backFileName, frontFileName);
	}
}
