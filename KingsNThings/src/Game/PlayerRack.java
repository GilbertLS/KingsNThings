package Game;
import java.util.ArrayList;
import java.util.Vector;

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

	public boolean tooFull() {
		return things.size()>10;
	}

	public int removeExcessFromRack() {
		//remove excess and return num removed
		
		return 0;
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

	public void removeFromRack(int thingID) {
		for(Thing t: things)
		{
			if(t.thingID == thingID)
				things.remove(t);
		}
	}

	public Thing getThing(int thingID) {
		for(Thing t: things)
		{
			if(t.thingID == thingID)
				return t;
		}
		return null;
	}
}
