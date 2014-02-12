package Game;
import java.util.ArrayList;
import java.util.Vector;

import Game.GameConstants.ControlledBy;
import Game.GameConstants.Terrain;

/*
 * This class represents a single Hex Tile in a Kings N' Things Game
 * It is of a specific Terrain (options defined in GlobalConstants class), is controlled
 * by a specific faction (options defined in GlobalConstants class), and contains
 * Things, one or no Forts, and one or no Special Income Tiles
 * Hex Tiles also have income, so implement the IIncomable interface
 */
public class HexTile implements IIncomable{
	public Terrain terrain;				//Tile's terrain type
	public ControlledBy controlledBy;		//Faction currently controlling this Tile
	public ArrayList<Thing> player1Things;	//player 1's Things in this Hex Tile
	public ArrayList<Thing> player2Things;	//player 2's Things in this Hex Tile
	public ArrayList<Thing> player3Things;	//player 3's Things in this Hex Tile
	public ArrayList<Thing> player4Things;	//player 4's Things in this Hex Tile
	public Fort fort;						//Fort for this Hex Tile (if applicable)
	public ArrayList<SpecialIncome> specialIncomes;	//Special Income for this Hex Tile (if applicable)
	public int x;
	public int y;
	
	public HexTile(Terrain terrain)
	{
		this.terrain = terrain;
		this.controlledBy = ControlledBy.NEUTRAL;
		
		this.player1Things = new ArrayList<Thing>(GameConstants.NUM_THINGS_PER_HEX);
		this.player2Things = new ArrayList<Thing>(GameConstants.NUM_THINGS_PER_HEX);
		this.player3Things = new ArrayList<Thing>(GameConstants.NUM_THINGS_PER_HEX);
		this.player4Things = new ArrayList<Thing>(GameConstants.NUM_THINGS_PER_HEX);
		
		this.fort = null;
		
		this.specialIncomes = new ArrayList<SpecialIncome>(GameConstants.MAX_NUM_SPECIAL_INCOME_PER_HEX);
	}
	
	public void AddThingToTile(Player player, Thing thing){
		if ( player.GetPlayerNum() == 0 ){
			player1Things.add(thing);
		} else if ( player.GetPlayerNum() == 1 ){
			player2Things.add(thing);
		} else if ( player.GetPlayerNum() == 2 ){
			player3Things.add(thing);
		} else if ( player.GetPlayerNum() == 3 ){
			player4Things.add(thing);
		}
	}
	
	public void AddThingToTile(int playerIndex, Thing thing){
		if ( playerIndex == 0 ){
			player1Things.add(thing);
		} else if ( playerIndex == 1 ){
			player2Things.add(thing);
		} else if ( playerIndex == 2 ){
			player3Things.add(thing);
		} else if ( playerIndex == 3 ){
			player4Things.add(thing);
		}
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
		}
		return false;
	}
	
	public ArrayList<Thing> GetThings(Player player){
		if ( player.GetPlayerNum() == 0 ){
			return player1Things;
		} else if ( player.GetPlayerNum() == 1 ){
			return player2Things;
		} else if ( player.GetPlayerNum() == 2 ){
			return player3Things;
		} else if ( player.GetPlayerNum() == 3 ){
			return player4Things;
		}
		return null;
	}
	
	public void Print(){
		System.out.print("Player 1 things: ");
		for(Thing thing : player1Things){ System.out.print(thing.GetThingId() + " ");}
		System.out.println();
		System.out.print("Player 2 things: ");
		for(Thing thing : player2Things){ System.out.print(thing.GetThingId() + " ");}
		System.out.println();
		System.out.print("Player 3 things: ");
		for(Thing thing : player3Things){ System.out.print(thing.GetThingId() + " ");}
		System.out.println();
		System.out.print("Player 4 things: ");
		for(Thing thing : player4Things){ System.out.print(thing.GetThingId() + " ");}
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

	public void addTower(Fort f) {
		fort = f;
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
		ArrayList<Thing> thingsToCheck = new ArrayList<Thing>();
		
		switch(playerIndex)
		{
		case 0:
			thingsToCheck = player1Things;
			break;
		case 1:
			thingsToCheck = player1Things;
			break;
		case 2:
			thingsToCheck = player3Things;
			break;
		case 3:
			thingsToCheck = player4Things;
			break;
		}
		
		for(Thing t: thingsToCheck)
		{
			if(t.thingID == Integer.parseInt(id))
				return true;
		}
		
		return false;
	}

	public void removeThingByID(String id, int playerIndex) {
		ArrayList<Thing> thingsToCheck = new ArrayList<Thing>();
		
		switch(playerIndex)
		{
		case 0:
			thingsToCheck = player1Things;
			break;
		case 1:
			thingsToCheck = player1Things;
			break;
		case 2:
			thingsToCheck = player3Things;
			break;
		case 3:
			thingsToCheck = player4Things;
			break;
		}
		
		Thing thingToRemove = null;
		for(Thing t: thingsToCheck)
		{
			if(t.thingID == Integer.parseInt(id))
				thingToRemove = t;
		}
		
		thingsToCheck.remove(thingToRemove);
	}
}
