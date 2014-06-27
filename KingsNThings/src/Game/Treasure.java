package Game;

import Game.GameConstants.ThingType;

/*
 * This Class represents a random treasure "Thing" in the Game,
 * providing specialized Treasure functionality
 */
public class Treasure extends Thing{
	int value;
	
	public Treasure(String name, int value, String frontFileName)
	{
		super(ThingType.TREASURE, name, frontFileName);
		
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	
	public Treasure copy() {
		return new Treasure(name, value, frontFileName);
	}
}
