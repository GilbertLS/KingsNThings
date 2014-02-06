package Game;
import java.util.Vector;

/*
 * This class is a container to hold up to 10 Things not currently played by a Player
 */
public class PlayerRack {
	private Vector<Thing> things;	//Things in Rack
	
	public PlayerRack()
	{
		this.things = new Vector<Thing>(GameConstants.MAX_NUM_THINGS_IN_RACK);
	}

	public void addThing(Thing currentThing) {
		things.add(currentThing);
	}
	
	public Vector<Thing> getThings() {
		return things;
	}

	public boolean tooFull() {
		return things.size()>10;
	}

	public int removeExcessFromRack() {
		//remove excess and return num removed
		
		return 0;
	}
}
