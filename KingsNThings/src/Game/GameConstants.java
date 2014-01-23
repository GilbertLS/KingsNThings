package Game;

import java.util.Collection;
import java.util.Vector;

/*
 * This class holds all relevant constants for the Game (for example, enums, screen width/height)
 */
public final class GameConstants {
	//-----------ENUMS-------------------
	
	//The different types of possible terrain in the game
	//(Note that all except "Sea" are considered "Land")
	public static enum Terrain {SEA, JUNGLE, FROZEN_WASTE, FOREST, PLAINS, SWAMP, MOUNTAIN, DESERT}
	
	//The Different Factions which may control game pieces
	public static enum ControlledBy {NEUTRAL, PLAYER1, PLAYER2, PLAYER3, PLAYER4}
	
	//The different types of "Things" in the game
	public static enum ThingType {SPECIAL_INCOME, 
									MAGIC,
									TREASURE,
									EVENT,
									CREATURE,
									FORT, 
									SETTLEMENT, 
									SPECIAL_CHARACTER,
									TERRAIN_LORD}
	
	//The different levels a Fort can have (Tower < Keep < Castle < Citadel)
	public static enum Level {TOWER, KEEP, CASTLE, CITADEL}
	
	//The different special types of Combatants there can be
	public static enum SpecialType {NONE, FLYING, MAGIC, RANGED, CHARGE}
	
	//The different type of Settlements
	public static enum SettlementType {VILLAGE, CITY}
	
	//-------------/end ENUMS---------------
	
	//-------------INTEGERS-----------------
	//Piece count constants
	public static final int MAX_NUM_THINGS = 150;
	public static final int MAX_NUM_THINGS_IN_RACK = 10;
	public static final int MAX_NUM_SPECIAL_CHARACTERS = 30;
	public static final int MAX_NUM_TILES = 48;
	public static final int NUM_FOUR_PLAYER_TILE_RINGS = 3;
	public static final int NUM_TWO_OR_THREE_PLAYER_TILE_RINGS = 2;
	
	//Dice Constants
	public static final int DIE_1_SIDES = 6;
	public static final int DIE_2_SIDES = 6;
	public static final int DIE_3_SIDES = 6;
	public static final int DIE_4_SIDES = 6;
	
	//HexTile Constants
	public static final int NUM_THINGS_PER_HEX = 10;
	public static final int MAX_NUM_FORTS_PER_HEX = 2;
	public static final int MAX_NUM_SPECIAL_INCOME_PER_HEX = 1;
	
	//HexTile type counts
	public static final int NUM_SEA_TILES = 8;
	public static final int NUM_JUNGLE_TILES = 6;
	public static final int NUM_FROZEN_WASTE_TILES = 6;
	public static final int NUM_FOREST_TILES = 6;
	public static final int NUM_PLAINS_TILES = 6;
	public static final int NUM_SWAMP_TILES = 6;
	public static final int NUM_MOUNTAIN_TILES = 6;
	public static final int NUM_DESERT_TILES = 6;
	
	//event types
	public static final int PLAYERS_ROLL = 0;
	public static final int UPDATE_PLAYER_ORDER = 1;



	//--------------/end INTEGERS
}
