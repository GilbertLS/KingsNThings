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
	
	public Thing(ThingType thingType, String name)
	{
		this.thingID = thingIDCount;
		thingIDCount++;
		
		this.thingType = thingType;
		this.name = name;
		
		this.isFlipped = true;
		
		this.currentTile = null;
	}

	public int GetThingId(){
		return thingID;
	}
	
	// REMOVE THIS, ONYL FOR TESTING PURPOSES
	public void SetThingId(int thingId){
		this.thingID = thingId;
	}
	
	public boolean IsCombatant(){
		return thingType == ThingType.CREATURE ||
			   thingType == ThingType.SPECIAL_CHARACTER ||
			   thingType == ThingType.TERRAIN_LORD;
	}
}
