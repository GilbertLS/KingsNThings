package Game;
import java.util.ArrayList;
import java.util.Vector;

import Game.Networking.GameClient;

/*
 * This class is a container to hold up to 10 Things not currently played by a Player
 */
public class PlayerRack {
	private ArrayList<Thing> things;	//Things in Rack
	
	public PlayerRack()
	{
		this.things = new ArrayList<Thing>(GameConstants.MAX_NUM_THINGS_IN_RACK);
	}

	public void addThing(Thing currentThing) {
		things.add(currentThing);
	}
	
	public ArrayList<Thing> getThings() {
		return things;
	}

	public boolean hasThings(int i) {
		return things.size() >= i;
	}

	public void setThings(ArrayList<Thing> thingsInRack) {
		things = thingsInRack;
		
	}

	public boolean hasThing(int thingID) {
		for(Thing t: things)
		{
			if(t.thingID == thingID)
				return true;
		}
		
		return false;
	}

	public Thing removeFromRackByID(int thingID) {
		Thing thing = null;
		for(Thing t: things)
		{
			if(t.thingID == thingID)
				thing = t;
		}
		
		things.remove(thing);
		
		return thing;
	}

	public Thing getThing(int thingID) {
		for(Thing t: things)
		{
			if(t.thingID == thingID)
				return t;
		}
		return null;
	}

	public int size() {
		return things.size();
	}

	public void handleRackOverload() {
		for(int i=things.size()-1; i>9; i--){
			GameClient.game.gameModel.handleElimination(things.remove(i), null);
		}
	}

	public boolean tooFull() {
		return things.size() > GameConstants.MAX_NUM_THINGS_IN_RACK;
	}

	public Thing removeFromRackByIndex(int thingIndex) {
		return things.remove(thingIndex);
	}
	
	public void clearRack(){
		things.clear();
	}
}
