package Game;

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
	public static final String PlaceHolderImageFront = "Bowl.png";
	public static final String PlaceHolderImageBack = "Bowl.png";
	public static final String ThingImageBack = "T_Back.png";
	public static final String TowerImageBack = "C_Fort_375.png";
	public static final String TowerImageFront = "C_Fort_376.png";
	public static final String WitchDoctorImageFront = "T_Jungle_006.png";
	public static final String OgreImageFront = "T_Mountains_I.png";
	public static final String GoblinsImageFront = "T_Mountains_E.png";
	public static final String WatusiImageFront = "T_Jungle_012.png";
	public static final String SkeletonsImageFront = "T_Desert_331.png";
	public static final String DwarvesImageFront = "T_Mountains_N.png";
	public static final String GiantImageFront = "T_Mountains_J.png";
	public static final String BrownKnightImageFront = "T_Mountains_D.png";
	public static final String ElephantImageFront = "T_Jungle_001.png";
	public static final String GiantSpiderImageFront = "T_Desert_221.png";
	public static final String OldDragonImageFront = "T_Desert_211.png";
	public static final String BanditsImageFront = "T_Forest_086.png";
	public static final String CrawlingVinesImageFront = "T_Jungle_003.png";
	public static final String WalkingTreeImageFront = "T_Forest_098.png";
	public static final String DruidImageFront = "T_Forest_088.png";
	public static final String NomadsImageFront = "T_Desert_220.png";
	public static final String CrocodilesImageFront = "T_Jungle_005.png";
	public static final String DervishImageFront = "T_Desert_214.png";
	public static final String GreenKnightImageFront = "T_Forest_097.png";
	public static final String SandwormImageFront = "T_Desert_224.png";
	public static final String PterodactylWarriorsImageFront = "T_Jungle_002.png";
	public static final String GreatHunterImageFront = "T_Plains_021.png";
	public static final String PygmiesImageFront = "T_Jungle_008.png";
	public static final String GenieImageFront = "T_Desert_215.png";
	public static final String FarmersImageFront = "T_Plains_014.png";
	public static final String CamelCorpsImageFront = "T_Desert_219.png";
	public static final String CentaurImageFront = "T_Plains_031.png";
	public static final String BuffaloHerdImageFront = "T_Plains_016.png";
	public static final String GiantApeImageFront = "T_Jungle_009.png";
	public static final String BlackKnightImageFront = "T_Swamp_080.png";
	public static final String DarkWizardImageFront = "T_Swamp_081.png";
	public static final String TribesmanImageFront = "T_Plains_027.png";
	public static final String VampireBatImageFront = "T_Swamp_084.png";
	public static final String TigersImageFront = "T_Jungle_011.png";
	public static final String VillainsImageFront = "T_Plains_015.png";
	public static final String GiantLizardImageFront = "T_Swamp_065.png";
	
	//-------------/end ENUMS---------------
	
	//-------------INTEGERS-----------------
	//Piece count constants
	public static final int MAX_NUM_THINGS = 150;
	public static final int MAX_NUM_THINGS_IN_RACK = 10;
	public static final int MAX_NUM_SPECIAL_CHARACTERS = 30;
	public static final int MAX_NUM_TILES = 48;
	public static final int NUM_FOUR_PLAYER_TILE_RINGS = 3;
	public static final int NUM_TWO_OR_THREE_PLAYER_TILE_RINGS = 2;
	public static final int INITIAL_GOLD_AMOUNT = 10;
	
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

	public static final int GOLD_PER_RECRUIT = 5;

	//battle turns
	public static enum BattleTurn {
		MAGIC,
		RANGED,
		OTHER
	}


	//--------------/end INTEGERS
}
