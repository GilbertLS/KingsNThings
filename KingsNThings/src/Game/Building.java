package Game;

import Game.GameConstants.ThingType;

/*
 * This abstract class adds functionality to the Combatant class to facilitate a building's ability
 * to take multiple hits. All buildings also generate income
 */
public abstract class Building extends Combatant implements IIncomable {
	private int hits; //number of hits currently applied to the building
	
	public Building(ThingType thingType, String name, int combatValue, String backFileName, String frontFileName)
	{
		super(thingType, name, combatValue, backFileName, frontFileName);
		
		this.hits = 0;
	}
}
