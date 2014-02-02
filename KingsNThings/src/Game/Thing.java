package Game;

import Game.GameConstants.ThingType;

/*
 * An abstract "Thing" in a Kings N' Things Game
 */
public abstract class Thing {
	private int thingID;		//unique identifier
	private static int thingIDCount = 0; //current unique identifier
	private Boolean isFlipped;	//whether the Thing is flipped over currently
	ThingType thingType;		//type of thing (enum defined in GameConstants class
	String name;				//display name of this Thing
	HexTile currentTile;
	public String backFileName, frontFileName;
	
	public Thing(ThingType thingType, String name, String backFileName, String frontFileName)
	{
		this.thingID = thingIDCount++;
		
		this.thingType = thingType;
		this.name = name;
		
		this.isFlipped = true;
		
		this.backFileName = backFileName;
		this.frontFileName = frontFileName;
		
		this.currentTile = null;
	}
}
