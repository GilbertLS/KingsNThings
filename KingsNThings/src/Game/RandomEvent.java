package Game;

import Game.GameConstants.CurrentPhase;
import Game.GameConstants.ThingType;
import Game.Networking.GameClient;

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
		
		//get selection of special character from view
		GameClient.game.gameView.performPhaseWithUserFeedback(CurrentPhase.CHOOSE_DEFECTION_ACTION, "Select a Special Character");

		//auto-open a view of all special characters not owned by current player
		
		//select character, 
		
		return ret;
	}
	
	public RandomEvent copy() {
		RandomEvent r = new RandomEvent(name, frontFileName);
		
		return r;
	}
}
