package Game;
import java.util.Vector;

/*
 * Represents one of the player of a Kings N' Things Game.
 */
public class Player {
	private PlayerRack playerRack;		//Rack to hold currently unplayed Things
	private int playerNum;				//Number identifying player
	private int gold;					//current gold stash of this player
	private int playerOrder;			//order within current order of play
	private Vector<Thing> heldThings;	//Things held by the player (e.g. during movement)
}
