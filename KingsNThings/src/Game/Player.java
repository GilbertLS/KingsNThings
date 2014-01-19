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
	
	public Player(int playerNum)
	{
		this.playerRack = new PlayerRack();
		
		this.playerNum = playerNum;
		this.playerOrder = playerNum;
		
		this.heldThings = new Vector<Thing>(GameConstants.MAX_NUM_THINGS_IN_HAND);
		
		this.gold = 0;
	}
}
