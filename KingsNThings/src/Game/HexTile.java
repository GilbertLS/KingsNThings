package Game;
import java.util.Vector;

import Game.GameConstants.ControlledBy;
import Game.GameConstants.Terrain;
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
	public ControlledBy controlledBy;		//Faction currently controlling this Tile
	public Vector<Thing> player1Things;	//player 1's Things in this Hex Tile
	public Vector<Thing> player2Things;	//player 2's Things in this Hex Tile
	public Vector<Thing> player3Things;	//player 3's Things in this Hex Tile
	public Vector<Thing> player4Things;	//player 4's Things in this Hex Tile
	private Vector<Fort> fort;						//Fort for this Hex Tile (if applicable)
	private Vector<SpecialIncome> specialIncome;	//Special Income for this Hex Tile (if applicable)
	
	public HexTile(Terrain terrain)
	{
		this.terrain = terrain;
		this.controlledBy = ControlledBy.NEUTRAL;
		
		this.player1Things = new Vector<Thing>(GameConstants.NUM_THINGS_PER_HEX);
		this.player2Things = new Vector<Thing>(GameConstants.NUM_THINGS_PER_HEX);
		this.player3Things = new Vector<Thing>(GameConstants.NUM_THINGS_PER_HEX);
		this.player4Things = new Vector<Thing>(GameConstants.NUM_THINGS_PER_HEX);
		
		this.fort = new Vector<Fort>(GameConstants.MAX_NUM_FORTS_PER_HEX);
		
		this.specialIncome = new Vector<SpecialIncome>(GameConstants.MAX_NUM_SPECIAL_INCOME_PER_HEX);
	}
	
	public void AddThingToTile(Player player, Thing thing){
		if ( player.GetPlayerNum() == 1 ){
			player1Things.add(thing);
		} else if ( player.GetPlayerNum() == 2 ){
			player2Things.add(thing);
		} else if ( player.GetPlayerNum() == 3 ){
			player3Things.add(thing);
		} else if ( player.GetPlayerNum() == 4 ){
			player4Things.add(thing);
		}
	}
	
	public boolean HasThingsOnTile(Player player){
		if ( player.GetPlayerNum() == 1 ){
			return !player1Things.isEmpty();
		} else if ( player.GetPlayerNum() == 2 ){
			return !player2Things.isEmpty();
		} else if ( player.GetPlayerNum() == 3 ){
			return !player3Things.isEmpty();
		} else if ( player.GetPlayerNum() == 4 ){
			return !player4Things.isEmpty();
		}
		return false;
	}

	public Terrain getTerrain() {
		return terrain;
	}
	
	public int getIncome()
	{
		return 1;
	}

}
