package Game;

import Game.GameConstants.ThingType;

/*
 * An abstract "Thing" in a Kings N' Things Game
 */
public abstract class Thing {
	private int thingID;		//unique identifier
	private Boolean isFlipped;	//whether the Thing is flipped over currently
	ThingType thingType;		//type of thing (enum defined in GameConstants class
	String name;				//display name of this Thing
}
