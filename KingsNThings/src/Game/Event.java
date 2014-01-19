package Game;

import Game.GameConstants.ThingType;

/*
 * This Class represents a random event "Thing" in the Game,
 * providing specialized Event functionality
 */
public class Event extends Thing {

	//temp
	private static int eventID = 0;
	public Event()
	{
		super(ThingType.EVENT, "Event" + eventID);
	}
}
