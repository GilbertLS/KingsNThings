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
	
	//The different type of Settlements
	public static enum SettlementType {VILLAGE, CITY}
	
	//thing image paths
	public static final String PlaceHolderImageFront = "C_Fort_375.png";
	public static final String PlaceHolderImageBack = "C_Fort_375.png";
	public static final String TowerImageBack = "C_Fort_375.png";
	public static final String TowerImageFront = "C_Fort_376.png";
	public static final String WitchDoctorImageBack = "C_Fort_375.png";
	public static final String WitchDoctorImageFront = "C_Fort_375.png";
	public static final String OgreImageBack = "C_Fort_375.png";
	public static final String OgreImageFront = "C_Fort_375.png";
	public static final String GoblinsImageBack = "C_Fort_375.png";
	public static final String GoblinsImageFront = "C_Fort_375.png";
	public static final String WatusiImageBack = "C_Fort_375.png";
	public static final String WatusiImageFront = "C_Fort_375.png";
	public static final String SkeletonsImageBack = "C_Fort_375.png";
	public static final String SkeletonsImageFront = "C_Fort_375.png";
	public static final String DwarvesImageBack = "C_Fort_375.png";
	public static final String DwarvesImageFront = "C_Fort_375.png";
	public static final String GiantImageBack = "C_Fort_375.png";
	public static final String GiantImageFront = "C_Fort_375.png";
	public static final String BrownKnightImageBack = "C_Fort_375.png";
	public static final String BrownKnightImageFront = "C_Fort_375.png";
	public static final String ElephantImageBack = "C_Fort_375.png";
	public static final String ElephantImageFront = "C_Fort_375.png";
	public static final String GiantSpiderImageBack = "C_Fort_375.png";
	public static final String GiantSpiderImageFront = "C_Fort_375.png";
	public static final String OldDragonImageBack = "C_Fort_375.png";
	public static final String OldDragonImageFront = "C_Fort_375.png";
	public static final String BanditsImageBack = "C_Fort_375.png";
	public static final String BanditsImageFront = "C_Fort_375.png";
	public static final String CrawlingVinesImageBack = "C_Fort_375.png";
	public static final String CrawlingVinesImageFront = "C_Fort_375.png";
	public static final String WalkingTreeImageBack = "C_Fort_375.png";
	public static final String WalkingTreeImageFront = "C_Fort_375.png";
	public static final String DruidImageBack = "C_Fort_375.png";
	public static final String DruidImageFront = "C_Fort_375.png";
	public static final String NomadsImageBack = "C_Fort_375.png";
	public static final String NomadsImageFront = "C_Fort_375.png";
	public static final String CrocodilesImageBack = "C_Fort_375.png";
	public static final String CrocodilesImageFront = "C_Fort_375.png";
	public static final String DervishImageBack = "C_Fort_375.png";
	public static final String DervishImageFront = "C_Fort_375.png";
	public static final String GreenKnightImageBack = "C_Fort_375.png";
	public static final String GreenKnightImageFront = "C_Fort_375.png";
	public static final String SandwormImageBack = "C_Fort_375.png";
	public static final String SandwormImageFront = "C_Fort_375.png";
	public static final String PterodactylWarriorsImageBack = "C_Fort_375.png";
	public static final String PterodactylWarriorsImageFront = "C_Fort_375.png";
	public static final String GreatHunterImageBack = "C_Fort_375.png";
	public static final String GreatHunterImageFront = "C_Fort_375.png";
	public static final String PygmiesImageBack = "C_Fort_375.png";
	public static final String PygmiesImageFront = "C_Fort_375.png";
	public static final String GenieImageBack = "C_Fort_375.png";
	public static final String GenieImageFront = "C_Fort_375.png";
	public static final String FarmersImageBack = "C_Fort_375.png";
	public static final String FarmersImageFront = "C_Fort_375.png";
	public static final String CamelCorpsImageBack = "C_Fort_375.png";
	public static final String CamelCorpsImageFront = "C_Fort_375.png";
	public static final String CentaurImageBack = "C_Fort_375.png";
	public static final String CentaurImageFront = "C_Fort_375.png";
	public static final String BuffaloHerdImageBack = "C_Fort_375.png";
	public static final String BuffaloHerdImageFront = "C_Fort_375.png";
	public static final String GiantApeImageBack = "C_Fort_375.png";
	public static final String GiantApeImageFront = "C_Fort_375.png";
	public static final String BlackKnightImageBack = "C_Fort_375.png";
	public static final String BlackKnightImageFront = "C_Fort_375.png";
	public static final String DarkWizardImageBack = "C_Fort_375.png";
	public static final String DarkWizardImageFront = "C_Fort_375.png";
	public static final String TribesmanImageBack = "C_Fort_375.png";
	public static final String TribesmanImageFront = "C_Fort_375.png";
	public static final String VampireBatImageBack = "C_Fort_375.png";
	public static final String VampireBatImageFront = "C_Fort_375.png";
	public static final String TigersImageBack = "C_Fort_375.png";
	public static final String TigersImageFront = "C_Fort_375.png";
	public static final String VillainsImageBack = "C_Fort_375.png";
	public static final String VillainsImageFront = "C_Fort_375.png";
	public static final String GiantLizardImageBack = "C_Fort_375.png";
	public static final String GiantLizardImageFront = "C_Fort_375.png";
	
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
