package Game;

import Game.GameConstants.ThingType;

/*
 * This Class represents a random event "Thing" in the Game,
 * providing specialized Event functionality
 */
public class RandomEvent extends Thing {

	public RandomEvent(String name, String frontFileName)
	{
		super(ThingType.RANDOM_EVENT, name, frontFileName);
	}

	public String performRandomEvent() {
		switch(name){
		case "Defection":
			return performDefection();
		}
		return "";
	}

	private String performDefection() {
		//do defection stuff
		String ret = "";
				
		ret += "Defection";
		
		return ret;
	}
}
