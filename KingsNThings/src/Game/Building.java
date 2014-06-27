package Game;

import Game.GameConstants.ThingType;

/*
 * This abstract class adds functionality to the Combatant class to facilitate a building's ability
 * to take multiple hits. All buildings also generate income
 */
public abstract class Building extends Combatant {
	private int hits; //number of hits currently applied to the building
	public boolean neutralized;
	
	public Building(ThingType thingType, String name, int combatValue, String backFileName, String frontFileName)
	{
		super(thingType, name, combatValue, frontFileName);
		
		this.backFileName = backFileName;
		
		this.hits = 0;
		
		isFlipped = false;
	}
	
	public void resetCounters() {
		hits = 0;
		neutralized = false;
		isFlipped = false;
	}
	
	public void neutralize()
	{
		hits = combatValue;
		neutralized = true;
		isFlipped = true;
	}

	public void takeHit() {
		hits++;
		
		if(hits >= combatValue)
			neutralize();
	}

	public boolean isNeutralized() {
		return neutralized;
	}
}
