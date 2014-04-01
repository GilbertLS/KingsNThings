package Game;

import Game.GameConstants.ControlledBy;
import Game.GameConstants.ThingType;

/*
 * An abstract "Thing" in a Kings N' Things Game
 */
public abstract class Thing {
	public int thingID;		//unique identifier
	private static int thingIDCount = 0; //current unique identifier
	protected Boolean isFlipped;	//whether the Thing is flipped over currently
	ThingType thingType;		//type of thing (enum defined in GameConstants class
	String name;				//display name of this Thing
	HexTile currentTile;
	public String backFileName, frontFileName;
	private ControlledBy controlledBy;		//Faction currently controlling
	public int numMoves = 0;
	
	public Thing(ThingType thingType, String name, String frontFileName)
	{
		this.thingID = thingIDCount++;
		
		this.thingType = thingType;
		this.name = name;
		
		this.isFlipped = true;
		
		this.backFileName = GameConstants.ThingImageBack;
		this.frontFileName = frontFileName;
		
		this.currentTile = null;
	}
	
	public void setControlledBy(ControlledBy controlledBy)	{this.controlledBy = controlledBy;}
	public ControlledBy getControlledBy(){return controlledBy;}
	public boolean isControlledBy(ControlledBy controlledBy){return this.controlledBy == controlledBy;}
	
	
	public ThingType getThingType()
	{
		return thingType;
	}

	public int GetThingId(){
		return thingID;
	}
	
	public String GetName(){
		return this.name;
	}
	
	// REMOVE THIS, ONYL FOR TESTING PURPOSES
	public void SetThingId(int thingId){
		this.thingID = thingId;
	}
	
	public boolean IsCombatant(){
		return thingType == ThingType.CREATURE ||
			   thingType == ThingType.SPECIAL_CHARACTER ||
			   thingType == ThingType.TERRAIN_LORD ||
			   thingType == ThingType.FORT ||
			   thingType == ThingType.SETTLEMENT;
		
	}
	
	public boolean isSpecialCharacter(){
		return thingType == ThingType.SPECIAL_CHARACTER ||
			   thingType == ThingType.TERRAIN_LORD;
		
	}
	
	public boolean isSpecialIncome(){
		return thingType == ThingType.SPECIAL_INCOME ||
			   thingType == ThingType.SETTLEMENT;
		
	}
	
	public int getControlledByPlayerNum() {
		if(controlledBy == null)
			return -1;
		
		switch(controlledBy) {
			case PLAYER1: return 0;
			case PLAYER2: return 1;
			case PLAYER3: return 2;
			case PLAYER4: return 3;
			default: return 0;
		}
	}

	public void clearMoves() {
		numMoves = 0;
	}

	public String getFrontImage() {
		return frontFileName;
		
	}
	
	public String getBackImage() {
		return backFileName;
	}
	
	public boolean isFlipped() {
		return this.isFlipped;
	}

	public void setFlipped(boolean b) {
		isFlipped = b;
		
	}

	public void setMovementFinished() {
		numMoves = GameConstants.MAX_MOVES_PER_TURN;
	}
}
