package Game;

import Game.GameConstants.ThingType;

/*
 * This Class represents a random event "Thing" in the Game,
 * providing specialized Event functionality
 */
public class Event extends Thing {

	public Event(String name, String backFileName, String frontFileName)
	{
		super(ThingType.EVENT, name, backFileName, frontFileName);
	}
}
