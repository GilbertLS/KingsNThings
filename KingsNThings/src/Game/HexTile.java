package Game;
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
	private Terrain terrain;				//Tile's terrain type
	private ControlledBy controlledBy;		//Faction currently controlling this Tile
	private Vector<Thing> player1Things;	//player 1's Things in this Hex Tile
	private Vector<Thing> player2Things;	//player 2's Things in this Hex Tile
	private Vector<Thing> player3Things;	//player 3's Things in this Hex Tile
	private Vector<Thing> player4Things;	//player 4's Things in this Hex Tile
	private Fort fort;						//Fort for this Hex Tile (if applicable)
	private SpecialIncome specialIncome;	//Special Income for this Hex Tile (if applicable)

}
