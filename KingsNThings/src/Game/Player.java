package Game;
import java.util.ArrayList;
import java.util.Vector;

import Game.GameConstants.ControlledBy;

/*
 * Represents one of the player of a Kings N' Things Game.
 */
public class Player {
	public PlayerRack playerRack;		//Rack to hold currently unplayed Things
	private int playerNum;				//Number identifying player
	public int gold;					//current gold stash of this player
	private int playerOrder;			//order within current order of play
	public ControlledBy faction;
	public ArrayList<Fort> forts;
	public ArrayList<SpecialCharacter> specialCharacters;
	public ArrayList<SpecialIncome> specialIncomes;
	public ArrayList<HexTile> ownedHexTiles;
	
	
	public int GetPlayerNum(){
		return playerNum;
	}
	
	public Player(int playerNum)
	{
		this.playerRack = new PlayerRack();
		
		this.playerNum = playerNum;
		this.playerOrder = playerNum;
		
		switch(playerNum)
		{
		case 0:
			faction = ControlledBy.PLAYER1;
			break;
		case 1:
			faction = ControlledBy.PLAYER2;
			break;
		case 2:
			faction = ControlledBy.PLAYER3;
			break;
		default:
			faction = ControlledBy.PLAYER4;
			break;
			
		}
		
		forts = new ArrayList<Fort>();
		specialCharacters = new ArrayList<SpecialCharacter>();
		specialIncomes = new ArrayList<SpecialIncome>();
		ownedHexTiles = new ArrayList<HexTile>();
		
		this.gold = 0;
	}

	public void setPlayerOrder(int startIndex) {
		this.playerOrder = startIndex;
		
	}

	public int getPlayerOrder() {
		return playerOrder;
	}

	public void updatePlayerOrder(int playerCount) {
		playerOrder = (playerOrder++)%playerCount;
		
	}

	public void addThingToRack(Thing currentThing) {
		playerRack.addThing(currentThing);
	}
	
	public PlayerRack getPlayerRack() {
		return playerRack;
	}

	public void addGold(int amount) {
		gold += amount;
	}

	public void addTower(Fort f) {
		forts.add(f);
	}

	public void addHexTile(HexTile h) {
		ownedHexTiles.add(h);
	}
	
	public void removeHexTile(HexTile h){
		ownedHexTiles.remove(h);
	}

	public boolean rackTooFull() {
		return playerRack.tooFull();
	}

	public int removeExcessFromRack() {
		return playerRack.removeExcessFromRack();
	}

	public boolean canTradeForRecruits(int numRecruits) {
		return playerRack.hasThings(numRecruits * 2);
	}

	public boolean canAffordRecruits(int numRecruits) {
		return gold >= numRecruits*GameConstants.GOLD_PER_RECRUIT;
	}

	public void payGold(int gold) {
		this.gold -= gold;
	}

	public int determineNumRecruits(int numPaidRecruits, int numTradeRecruits) {
			return (int)Math.ceil(ownedHexTiles.size()/2.0) + numPaidRecruits + numTradeRecruits;
	}

	public boolean hasInRack(int thingID) {
		return playerRack.hasThing(thingID);
	}

	public void removeFromRack(int thingID) {
		playerRack.removeFromRack(thingID);
	}

	public Thing getThingByID(int thingID) {
		return playerRack.getThing(thingID);
	}
}
