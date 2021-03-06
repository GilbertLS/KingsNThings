package Game;
import java.util.ArrayList;
import java.util.Vector;

import Game.GameConstants.ControlledBy;
import Game.GameConstants.Level;
import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;
import Game.Networking.GameClient;

/*
 * This class represents a single Hex Tile in a Kings N' Things Game
 * It is of a specific Terrain (options defined in GlobalConstants class), is controlled
 * by a specific faction (options defined in GlobalConstants class), and contains
 * Things, one or no Forts, and one or no Special Income Tiles
 * Hex Tiles also have income, so implement the IIncomable interface
 */
public class HexTile implements IIncomable{
	public Terrain terrain;				//Tile's terrain type
	private ControlledBy controlledBy;		//Faction currently controlling this Tile
	public ArrayList<Thing> player1Things;	//player 1's Things in this Hex Tile
	public ArrayList<Thing> player2Things;	//player 2's Things in this Hex Tile
	public ArrayList<Thing> player3Things;	//player 3's Things in this Hex Tile
	public ArrayList<Thing> player4Things;	//player 4's Things in this Hex Tile
	public ArrayList<Thing> defendingThings;	//Defending Things in this Hex Tile
	public ArrayList<Fort> forts;					//Fort for this Hex Tile (if applicable)
	public ArrayList<SpecialIncome> specialIncomes;	//Special Income for this Hex Tile (if applicable)
	public ArrayList<Settlement> settlements;
	public ArrayList<Treasure> treasures;
	public ArrayList<Magic> magics;
	public int x;
	public int y;
	public int moveValue;
	public boolean constructionAllowed = true;
	
	public boolean isLand()
	{
		return terrain != Terrain.SEA;
	}
	
	public HexTile(Terrain terrain)
	{
		this.terrain = terrain;
		this.controlledBy = ControlledBy.NEUTRAL;
		
		this.player1Things = new ArrayList<Thing>(GameConstants.MAX_NUM_THINGS_PER_HEX);
		this.player2Things = new ArrayList<Thing>(GameConstants.MAX_NUM_THINGS_PER_HEX);
		this.player3Things = new ArrayList<Thing>(GameConstants.MAX_NUM_THINGS_PER_HEX);
		this.player4Things = new ArrayList<Thing>(GameConstants.MAX_NUM_THINGS_PER_HEX);
		this.defendingThings = new ArrayList<Thing>(GameConstants.MAX_NUM_THINGS_PER_HEX);
		
		this.specialIncomes = new ArrayList<SpecialIncome>(GameConstants.MAX_NUM_SPECIAL_INCOME_PER_HEX);
		this.settlements = new ArrayList<Settlement>(GameConstants.MAX_NUM_SPECIAL_INCOME_PER_HEX);
		this.treasures = new ArrayList<Treasure>();
		this.magics = new ArrayList<Magic>();
		this.forts = new ArrayList<Fort>();
		
		if(terrain == Terrain.SWAMP
				|| terrain == Terrain.MOUNTAIN
				|| terrain == Terrain.FOREST
				|| terrain == Terrain.JUNGLE)
			moveValue = 2;
		else
			moveValue = 1;
	}
	
	public void setControlledBy(ControlledBy controlledBy)	{this.controlledBy = controlledBy;}
	public ControlledBy getControlledBy(){return controlledBy;}
	public boolean isControlledBy(ControlledBy controlledBy){return this.controlledBy == controlledBy;}
	
	public void AddThingToTile(Player player, Thing thing){
		AddThingToTile(player.GetPlayerNum(), thing);
	}
	
	public void AddThingToTile(int playerIndex, Thing thing){
		if(handlePlaceSpecialIncome(playerIndex, thing))
			return;
		
		if(handleTreasure(playerIndex, thing))
			return;
		
		if(handleMagic(playerIndex, thing))
			return;
		
		if(handleRandomEvent(playerIndex, thing))
			return;
		
		if ( playerIndex == 0 ){
			player1Things.add(thing);
			thing.setControlledBy(ControlledBy.PLAYER1);
		} else if ( playerIndex == 1 ){
			player2Things.add(thing);
			thing.setControlledBy(ControlledBy.PLAYER2);
		} else if ( playerIndex == 2 ){
			player3Things.add(thing);
			thing.setControlledBy(ControlledBy.PLAYER3);
		} else if ( playerIndex == 3 ){
			player4Things.add(thing);
			thing.setControlledBy(ControlledBy.PLAYER4);
		} else if ( playerIndex == 4 ){
			defendingThings.add(thing);
			thing.setControlledBy(ControlledBy.NEUTRAL);
		}
		
		Player player = GameClient.game.gameModel.playerFromIndex(playerIndex);
		if(thing.isSpecialCharacter())
			player.addSpecialCharacter((SpecialCharacter) thing);
		else if(thing.thingType == ThingType.SETTLEMENT)
			player.addSettlement((Settlement) thing);
		else if(thing.thingType == ThingType.SPECIAL_INCOME)
			player.addSpecialIncome((SpecialIncome)thing);
	}
	
	private boolean handleTreasure(int playerIndex, Thing thing) {
		if(thing.thingType == ThingType.TREASURE) 
		{
			if(playerIndex == 4)	//add treasure to be capture after battle
				treasures.add((Treasure)thing);
			else
			{	
				Player player = GameClient.game.gameModel.playerFromIndex(playerIndex);
						
				player.addGold(((Treasure)thing).getValue());
			}
			
			return true;
		}
		
		return false;
	}

	private boolean handlePlaceSpecialIncome(int playerIndex, Thing t) {
		if(!hasSettlement() && !hasSpecialIncome())
		{
			Player player = null;
			
			if(playerIndex != 4){
				player = GameClient.game.gameModel.playerFromIndex(playerIndex);
				t.setControlledBy(player.faction);
			}
			else
				t.setControlledBy(ControlledBy.NEUTRAL);
			
			if(t.thingType == ThingType.SPECIAL_INCOME) 
			{
				SpecialIncome si = (SpecialIncome)t;
				
				specialIncomes.add(si);
				
				if(player != null)
					player.addSpecialIncome(si);
				
				return true;
			}
			else if(t.thingType == ThingType.SETTLEMENT)
			{
				Settlement s = (Settlement)t;
				
				settlements.add(s);
				
				if(player != null)
					player.addSettlement(s);
				
				return true;
			}
		}

		return false;
	}
	
	private boolean handleMagic(int playerIndex, Thing thing) {
		if(thing.thingType == ThingType.MAGIC)
		{
			return true;
		}
		
		return false;
	}
	
	private boolean handleRandomEvent(int playerIndex, Thing thing) {
		if(thing.isRandomEvent())
		{
			return true;
		}
		
		return false;
	}

	public boolean HasThingsOnTile(Player player){
		if ( player.GetPlayerNum() == 0 ){
			return !player1Things.isEmpty();
		} else if ( player.GetPlayerNum() == 1 ){
			return !player2Things.isEmpty();
		} else if ( player.GetPlayerNum() == 2 ){
			return !player3Things.isEmpty();
		} else if ( player.GetPlayerNum() == 3 ){
			return !player4Things.isEmpty();
		} else {return !defendingThings.isEmpty();}
	}
	
	public boolean HasThingsOnTile(int playerIndex){
		
		if(playerIndex == 4)
			return !defendingThings.isEmpty();
		else
			return HasThingsOnTile(GameClient.game.gameModel.playerFromIndex(playerIndex));
	}
	
	public ArrayList<Thing> GetThings(Player player){
		ArrayList<Thing> returnList = GetThings(player.GetPlayerNum());
		
		return returnList;
	}
	
	public ArrayList<Thing> GetThings(int playerIndex){
		if ( playerIndex == 0 ){
			return player1Things;
		} else if ( playerIndex == 1 ){
			return player2Things;
		} else if ( playerIndex == 2 ){
			return player3Things;
		} else if ( playerIndex == 3 ){
			return player4Things;
		}else if ( playerIndex == 4)
			return defendingThings;
		
		return null;
	}
	
	public void Print(){
		for(int i = 0; i < GameClient.game.gameModel.PlayerCount(); i++) {
			System.out.print("Player " + i + " things: ");
			for(Thing thing : this.GetThings(i)){ System.out.print(thing.GetThingId() + " ");}
			System.out.println();
		}
		
		System.out.print("Neutral things: ");
		for(Thing thing : defendingThings){ System.out.print(thing.GetThingId() + " ");}
		System.out.println();
	}

	public Terrain getTerrain() {
		return terrain;
	}
	
	public int getIncome()
	{
		return 1;
	}
	
	public boolean isAdjacent(HexTile h2)
	{
		return((h2.x <= x+1 && h2.x >= x-1)
			&& (h2.y <= y+1 && h2.y >= y-1)
			&& !(h2.x == x+1 && h2.y == y-1)
			&& !(h2.x == x-1 && h2.y == y+1));
	}

	public void addFort(Fort f) {
		forts.add(f);
	}
	
	public int distance(HexTile h)
	{
		int dx = h.x - x;
		int dy = h.y - y;
		
		if((dx < 0 && dy >= 0)
			||(dx >= 0 && dy <0))
			return (Math.abs(dx) + Math.abs(dy));
		else
			return Math.max(Math.abs(dx), Math.abs(dy));
	}

	public boolean hasThingWithID(String id, int playerIndex) {
		ArrayList<Thing> thingsToCheck = GetThings(playerIndex);
		
		for(Thing t: thingsToCheck)
		{
			if(t.thingID == Integer.parseInt(id))
				return true;
		}
		
		return false;
	}

	public void removeThing(int id, int playerIndex) {
		ArrayList<Thing> thingsToCheck = GetThings(playerIndex);
		
		Thing thingToRemove = null;
		for(Thing t: thingsToCheck)
		{
			if(t.thingID == id)
				thingToRemove = t;
		}
		
		thingsToCheck.remove(thingToRemove);
	}

	private Thing getThingFromTileByID(Integer id, int playerIndex) {
		
		ArrayList<Thing> thingsToCheck = GetThings(playerIndex);

		for(Thing t: thingsToCheck)
		{
			if(t.thingID == id)
				return t;
		}
		
		return null;
	}
	
	public Thing getThingFromTileByID(Integer id) {
		//check for special income
		if(hasSpecialIncome()){
			SpecialIncome si = specialIncomeByID(id);
			if(si != null)
				return si;	
		}

		//check for settlement
		if(hasSettlement()){
			Settlement s = settlementByID(id);
			if(s != null)
				return s;
		}

		//check for fort
		if(hasFort()){
			Fort f = fortByID(id);
			if(f != null)
				return f;
		}
		
		//check for treasure
		if(hasTreasure()){
			Treasure t = treasureByID(id);
			if(t != null)
				return t;
		}
		
		//check all player things
		Thing t;
		for(int i=0; i<5; i++){
			if(HasThingsOnTile(i)){
				t = getThingFromTileByID(id, i);
				if(t != null)
					return t;
			}
		}
		
		//thing does not exist on tile
		return null;
	}
	
	private Treasure treasureByID(Integer id) {
		for(Treasure t: treasures)
		{
			if(t.thingID == id)
				return t;
		}
		
		return null;
	}

	private Fort fortByID(Integer id) {
		for(Fort f: forts)
		{
			if(f.thingID == id)
				return f;
		}
		
		return null;
	}

	private Settlement settlementByID(Integer id) {
		for(Settlement s: settlements)
		{
			if(s.thingID == id)
				return s;
		}
		
		return null;
	}

	private SpecialIncome specialIncomeByID(Integer id) {
		for(SpecialIncome si: specialIncomes)
		{
			if(si.thingID == id)
				return si;
		}
		
		return null;
	}

	public void handlePostBattle(boolean fortHit, boolean settlementHit, boolean specialIncomeHit) {
		if(hasFort())	
			if(fortHit)
				if(getFort().getLevel() == Level.TOWER)
					GameClient.game.gameModel.handleElimination(getFort(), this);
				else
					getFort().decrementLevel();

		if(hasSpecialIncome())
			if(specialIncomeHit)
				GameClient.game.gameModel.handleElimination(getSpecialIncome(), this);		
		
		if(hasSettlement())		
			if(settlementHit)
				GameClient.game.gameModel.handleElimination(getSettlement(), this);		
		
		resetCounters();
	}

	public void resetCounters() {
		if(hasFort())
			getFort().resetCounters();
			
		if(hasSettlement())
			getSettlement().resetCounters();
	}

	public void removeSettlement() {
		settlements = new ArrayList<Settlement>();
	}

	public void removeSpecialIncome() {
		specialIncomes = new ArrayList<SpecialIncome>();
	}

	public boolean noOtherPlayerOnTile(int playerIndex) {
		boolean ret = false;
		
		switch(playerIndex)
		{
		case 0:
			ret = (player2Things.isEmpty() && player3Things.isEmpty() && player4Things.isEmpty() && defendingThings.isEmpty());
			break;
		case 1:
			ret = (player1Things.isEmpty() && player3Things.isEmpty() && player4Things.isEmpty() && defendingThings.isEmpty());
			break;
		case 2:
			ret = (player1Things.isEmpty() && player2Things.isEmpty() && player4Things.isEmpty() && defendingThings.isEmpty());
			break;
		default:
			ret = (player1Things.isEmpty() && player2Things.isEmpty() && player3Things.isEmpty() && defendingThings.isEmpty());
			break;
		}
		
		return ret;
	}

	public boolean hasSpecialIncome() {
		return !specialIncomes.isEmpty();
	}

	public SpecialIncome getSpecialIncome() {
		if(!specialIncomes.isEmpty())
			return specialIncomes.get(0);
		else
			return null;
	}

	public boolean hasFort() {
		return !forts.isEmpty();
	}

	public Fort getFort() {
		if(hasFort())
			return forts.get(0);
		else
			return null;
	}
	
	public ArrayList<Fort> getAllForts() {
		return forts;
	}

	public boolean hasSettlement() {
		return !settlements.isEmpty();
	}
	
	public Settlement getSettlement()
	{
		if(!settlements.isEmpty())
			return settlements.get(0);
		else
			return null;
	}

	public boolean hasRoomForThings(ArrayList<Thing> things) {		
		int numCombatants = 0;
		
		if(hasFort() && getFort().isCitadel())
			return true;
		
		//list of things may contain non-combatants, parse them to combatants
		for(Thing t: things){
			if(t.IsCombatant() && !t.isSpecialIncome())
				numCombatants++;
		}
		
		switch(things.get(0).getControlledBy()){
			case PLAYER1:
				numCombatants += player1Things.size();
				break;
			case PLAYER2:
				numCombatants += player2Things.size();
				break;
			case PLAYER3:
				numCombatants += player3Things.size();
				break;
			case PLAYER4:
				numCombatants += player4Things.size();
				break;
		default:
			break;
		}
		
		if(numCombatants > GameConstants.MAX_NUM_THINGS_PER_HEX)
			return false;
		
		return true;
	}

	public void setCordinates(int x, int y) {
		this.x = x;
		this.y = y;
		
	}
	
	public void setConstructionAllowed(boolean allowed)
	{
		constructionAllowed = allowed;
	}

	public ArrayList<Thing> enforceValidDefense(ArrayList<Thing> things) {
		ArrayList<Thing> thingsToRemove = new ArrayList<Thing>();
		ArrayList<SpecialIncome> specialIncomes = new ArrayList<SpecialIncome>();
		ArrayList<Settlement> settlements = new ArrayList<Settlement>();
		
		//find all valid incomes
		for(Thing t: things)
		{
			if (t.thingType == ThingType.SPECIAL_INCOME)
			{
				if(((SpecialIncome)t).getTerrain() != terrain)
					thingsToRemove.add(t);
				else
					specialIncomes.add((SpecialIncome)t);
			}
			else if (t.thingType == ThingType.SETTLEMENT)
				settlements.add((Settlement)t);
				
		}
		
		//find highest special income
		int highestSpecialIncome = 0;
		SpecialIncome highestSI = null;
		for(SpecialIncome si: specialIncomes)
		{
			if (si.getIncome() > highestSpecialIncome)
			{
				highestSpecialIncome = si.getIncome();
				
				if(highestSI != null)
					thingsToRemove.add(highestSI);	
				
				highestSI = si;
			}
			else
				thingsToRemove.add(si);
		}
		
		//find highest settlement
		int highestSettlementIncome = 0;
		Settlement highestSettlement = null;
		for(Settlement s: settlements)
		{
			if (s.getIncome() > highestSettlementIncome)
			{
				highestSettlementIncome = s.getIncome();
				
				if(highestSettlement != null)
					thingsToRemove.add(highestSettlement);
			
				highestSettlement = s;
			}
			else
				thingsToRemove.add(s);
		}
			
		//if a highest settlement and highest special income were found
		//take the higher of the two (if equal, will take settlement)
		if(highestSI != null && highestSettlement != null)
			if(highestSI.getIncome() > highestSettlement.getIncome())
				thingsToRemove.add(highestSettlement);
			else
				thingsToRemove.add(highestSI);
		
		//remove invalid things from things to play
		things.removeAll(thingsToRemove);
		
		//return all invalid things to cup
		for(Thing t: thingsToRemove)
			GameClient.game.gameModel.handleElimination(t, null);
			
		return things;
	}

	public boolean noDefense() {
		return defendingThings.isEmpty();
	}

	public boolean hasTreasure() {
		return !treasures.isEmpty();
	}

	public void removePlayerThings(ArrayList<Thing> things, int playerIndex) {
		ArrayList<Thing> thingsToRemoveFrom = GetThings(playerIndex);
		ArrayList<Thing> thingsToRemove = new ArrayList<Thing>();
		
		for(Thing t: thingsToRemoveFrom){
			if(things.contains(t))
				thingsToRemove.add(t);
		}
		
		thingsToRemoveFrom.removeAll(thingsToRemove);
	}

	public void removeFort() {
		forts = new ArrayList<Fort>();
	}

	public ArrayList<Treasure> getTreasures() {
		return treasures;
	}

	public boolean isOnlyCombatantPlayerOnTile(int playerIndex) {
		for(int i=0; i<5; i++)
		{
			//false if does not have combatants
			if(i == playerIndex){
				if(!hasCombatants(i))
					return false;
			}else{	//or other players do
				if(hasCombatants(i))
					return false;
			}
		}

		return true;
	}

	public boolean hasCombatants(int playerIndex) {
		
		ControlledBy playerFaction;
		if(playerIndex == 4)
			playerFaction = ControlledBy.NEUTRAL;
		else
			playerFaction = GameClient.game.gameModel.playerFromIndex(playerIndex).faction;
		
		//true if player controls fort
		if(hasFort() && getFort().isControlledBy(playerFaction))
			return true;
		
		//true if player controls settlement
		if(hasSettlement() && getSettlement().isControlledBy(playerFaction))
			return true;
		
		//true if player has things in hex
		switch(playerIndex){
		case 0:
			return !player1Things.isEmpty();
		case 1:
			return !player2Things.isEmpty();
		case 2:
			return !player3Things.isEmpty();
		case 3:
			return !player4Things.isEmpty();
		default:
			return !defendingThings.isEmpty();
		}
	}

	public boolean hasMagic() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean bribeSettlement(ArrayList<Thing> things) {
		ArrayList<Settlement> settlementsToBribe = new ArrayList<Settlement>();
		boolean settlementBribed = false;
		
		for(Settlement s: settlements){
			if(things.contains(s)){
				settlementsToBribe.add(s);
				s.neutralize();
				settlementBribed = true;
				things.remove(s);	//remove from list so is not eliminated
			}
		}
	
		return settlementBribed;
	}

	public ArrayList<Magic> getMagics() {
		return magics;
	}

	public void clearTreasure() {
		treasures = new ArrayList<Treasure>();
	}

	public void clearMagic() {
		magics = new ArrayList<Magic>();
	}

	public ArrayList<Thing> getCombatants(int playerIndex) {
		ArrayList<Thing> combatants = new ArrayList<Thing>();
		ControlledBy faction;
		
		if(playerIndex == 4)
			faction = ControlledBy.NEUTRAL;
		else
			faction = GameClient.game.gameModel.playerFromIndex(playerIndex).faction;
		
		if(isControlledBy(faction)){ //player controls this tile, 
			if(hasFort())
				combatants.add(getFort());
		
			if(hasSettlement())
				combatants.add(getSettlement());
		}
		
		//add things
		switch(playerIndex){
		case 0:
			combatants.addAll(player1Things);
			break;
		case 1:
			combatants.addAll(player2Things);
			break;
		case 2:
			combatants.addAll(player3Things);
			break;
		case 3:
			combatants.addAll(player4Things);
			break;
		case 4:
			combatants.addAll(defendingThings);
			break;
		}
		
		return combatants;
	}

	public boolean isBribeDoubled() {
		return hasTreasure() || hasMagic() || hasSettlement() || hasSpecialIncome();
	}

	public ArrayList<Thing> getAllThings() {
		ArrayList<Thing> ret = new ArrayList<Thing>();
		
		ret.addAll(forts);
		ret.addAll(specialIncomes);
		ret.addAll(settlements);
		ret.addAll(treasures);
		ret.addAll(magics);
		ret.addAll(player1Things);
		ret.addAll(player2Things);
		ret.addAll(player3Things);
		ret.addAll(player4Things);
		ret.addAll(defendingThings);
		
		return ret;
	}

	public boolean[] getBuildingEliminations() {
		boolean[] ret = new boolean[3];
		int roll = 0;
		if(hasFort()){
			 Dice.rollDice(1);
			 roll = Dice.getRoll(0);
			 if(roll == 1 || roll == 6)
				 ret[0] = true;
		}
		if(hasSettlement()){
			 Dice.rollDice(1);
			 roll = Dice.getRoll(0);
			 if(roll == 1 || roll == 6)
				 ret[1] = true;
		}
		if(hasSpecialIncome()){
			 Dice.rollDice(1);
			 roll = Dice.getRoll(0);
			 if(roll == 1 || roll == 6)
				 ret[2] = true;
		}
		
		return ret;
	}
}
