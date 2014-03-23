package Game;
import java.util.ArrayList;
import java.util.Vector;

import Game.GameConstants.ControlledBy;
import Game.GameConstants.Level;
import Game.GameConstants.Terrain;

/*
 * Represents one of the player of a Kings N' Things Game.
 */
public class Player {
	public PlayerRack playerRack;		//Rack to hold currently unplayed Things
	private int playerNum;				//Number identifying player
	private int gold;					//current gold stash of this player
	private int playerOrder;			//order within current order of play
	public ControlledBy faction;
	public ArrayList<Fort> ownedForts;
	public ArrayList<SpecialCharacter> specialCharacters;
	public ArrayList<SpecialIncome> ownedSpecialIncomes;
	public ArrayList<Settlement> ownedSettlements;
	public ArrayList<HexTile> ownedHexTiles;
	private boolean citadelConstructed = false;
	
	
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
		
		ownedForts = new ArrayList<Fort>();
		specialCharacters = new ArrayList<SpecialCharacter>();
		ownedSpecialIncomes = new ArrayList<SpecialIncome>();
		ownedHexTiles = new ArrayList<HexTile>();
		ownedSettlements = new ArrayList<Settlement>();
		
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
		currentThing.controlledBy = faction;
	}
	
	public PlayerRack getPlayerRack() {
		return playerRack;
	}

	public void addGold(int amount) {
		gold += amount;
	}

	public void addFort(Fort f) {
		ownedForts.add(f);
		f.controlledBy = faction;
	}

	public void addHexTile(HexTile h) {
		ownedHexTiles.add(h);
		
		if(h.hasFort())
			ownedForts.add(h.getFort());	
		
		if(h.hasSpecialIncome())
			ownedSpecialIncomes.add(h.getSpecialIncome());
		
		if(h.hasSettlement())
			ownedSettlements.add(h.getSettlement());
	}
	
	public void removeHexTile(HexTile h){
		ownedHexTiles.remove(h);
	}

	public boolean canTradeForRecruits(int numRecruits) {
		return playerRack.hasThings(numRecruits * 2);
	}

	public boolean canAffordRecruits(int numRecruits) {
		return canAfford(numRecruits*GameConstants.GOLD_PER_RECRUIT);
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
	
	public void addSpecialIncome(SpecialIncome si) {
		ownedSpecialIncomes.add(si);
	}
	
	public void addSettlement(Settlement s) {
		ownedSettlements.add(s);
	}

	public int getGold() {
		return gold;
	}

	public int getIncome() {
		int gold =0;
		
		//gold pieces for land hexes
		for(HexTile h: ownedHexTiles)
				if(h.terrain != Terrain.SEA)
				{
						gold += h.getIncome();
						System.out.println("INCOME FROM HEX TILE: " + h.getIncome());
				}
		
		//combat values for forts
		for(Fort f: ownedForts)
			gold+= f.getIncome();
		
		//special income tiles
		for(SpecialIncome si: ownedSpecialIncomes)
			gold+= si.getIncome();
		
		for(Settlement s: ownedSettlements)
			gold+= s.getIncome();
		
		//special characters
		for(SpecialCharacter sc: specialCharacters)
			gold += sc.getIncome();
		
		return gold;
	}

	public void decrementGold(int amount) {
		gold -= amount;
	}

	public boolean canAfford(int amount) {
		return gold >= amount;
	}

	public boolean rackTooFull() {
		return playerRack.tooFull();
	}

	public boolean hasCitadel() {
		for(Fort f: ownedForts)
			if (f.getLevel() == Level.CITADEL)
				return true;
		
		return false;
	}

	public boolean citadelWasConstructed() {
		return citadelConstructed;
	}

	public void setCitadelConstructed(boolean constructed) {
		citadelConstructed = constructed;
	}

	public int getNumCitadels() {
		int num =0;
		
		for(Fort f: ownedForts)
			if(f.getLevel() == Level.CITADEL)
				num++;
		
		return num;
	}
	
	public void removeFort(Fort f){
		ownedForts.remove(f);
	}
}
