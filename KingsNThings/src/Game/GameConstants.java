package Game;

import Game.GameConstants.ThingType;

/*
 * This class holds all relevant constants for the Game (for example, enums, screen width/height)
 */
public final class GameConstants {
	public static enum CurrentPhase {
		MOVEMENT,
		BATTLE,
		NULL, 
		PLAY_THINGS, 
		CONSTRUCTION, 
		RECRUIT_CHARACTER, 
		INITIAL_TRADE_THINGS,
		TRADE_THINGS, 
		AWARD_INCOME, 
		SWAP_INITIAL_HEXES, SELECT_TARGET_PLAYER, CHOOSE_THEIF_ACTION
	}

	//The different types of possible terrain in the game
	//(Note that all except "Sea" are considered "Land")
	public static enum Terrain {SEA, JUNGLE, FROZEN_WASTE, FOREST, PLAINS, SWAMP, MOUNTAIN, DESERT}
	
	//The Different Factions which may control game pieces
	public static enum ControlledBy {NEUTRAL, PLAYER1, PLAYER2, PLAYER3, PLAYER4}
	
	public static int GetPlayerNumber(ControlledBy c){
		if ( c == ControlledBy.PLAYER1 ){ return 0; }
		else if ( c == ControlledBy.PLAYER2 ){ return 1; }
		else if ( c == ControlledBy.PLAYER3 ){ return 2; }
		else if ( c == ControlledBy.PLAYER4 ){ return 3; }
		else if ( c == ControlledBy.NEUTRAL){ return 4; }
		return -1; 
	}
	
	//The different types of "Things" in the game
	public static enum ThingType {SPECIAL_INCOME, 
									MAGIC,
									TREASURE,
									EVENT,
									CREATURE,
									FORT, 
									SETTLEMENT, 
									SPECIAL_CHARACTER,
									TERRAIN_LORD;

									public static boolean isSpecialIncome(ThingType tt) {
										return tt == SPECIAL_CHARACTER
												|| tt == SETTLEMENT;
									}}
	
	//The different levels a Fort can have (Tower < Keep < Castle < Citadel)
	public static enum Level {TOWER, KEEP, CASTLE, CITADEL}
	
	//The different type of Settlements
	public static enum SettlementType {VILLAGE, CITY}
	
	//thing image paths
	public static final String PlaceHolderImageFront = "Bowl.png";
	public static final String PlaceHolderImageBack = "Bowl.png";
	public static final String ThingImageBack = "T_Back.png";
	
	//DESERT
	public static final String BabyDragonImageFront = "BabyDragon.jpg";
	public static final String CamelCorpsImageFront = "CamelCorps.jpg";
	public static final String GenieImageFront = "Genie.jpg";
	public static final String DustDevilImageFront = "DustDevil.jpg";
	public static final String DesertBatImageFront = "DesertBat.jpg";
	public static final String GiantSpiderImageFront = "GiantSpider.jpg";
	public static final String GiantWaspImageFront = "GiantWasp.jpg";
	public static final String GiantWasp1ImageFront = "GiantWasp1.jpg";
	public static final String GriffonImageFront = "Griffon.jpg";
	public static final String OldDragonImageFront = "OldDragon.jpg";
	public static final String SandwormImageFront = "Sandworm.jpg";
	public static final String SphinxImageFront = "Sphinx.jpg";
	public static final String YellowKnightImageFront = "YellowKnight.jpg";
	public static final String DervishImageFront = "Dervish.jpg";
	public static final String NomadsImageFront = "Nomads.jpg";
	public static final String SkeletonsImageFront = "Skeletons.jpg";
	public static final String VulturesImageFront = "Vultures.jpg";
	
	//FOREST
	public static final String BanditsImageFront = "Bandits.jpg";
	public static final String ElvesImageFront = "Elves.jpg";
	public static final String Elves1ImageFront = "Elves1.jpg";
	public static final String PixiesImageFront = "Pixies.jpg";
	public static final String WyvernImageFront = "Wyvern.jpg";
	public static final String WildCatImageFront = "WildCat.jpg";
	public static final String WalkingTreeImageFront = "WalkingTree.jpg";
	public static final String UnicornImageFront = "Unicorn.jpg";
	public static final String KillerRaccoonImageFront = "KillerRacoon.jpg";
	public static final String GreenKnightImageFront = "GreenKnight.jpg";
	public static final String GreatOwlImageFront = "GreatOwl.jpg";
	public static final String ForesterImageFront = "Forester.jpg";
	public static final String FlyingSquirrelImageFront = "FlyingSquirrel.jpg";
	public static final String FlyingSquirrel1ImageFront = "FlyingSquirrel1.jpg";
	public static final String ElfMageImageFront = "ElfMage.jpg";
	public static final String BearsImageFront = "Bears.jpg";
	public static final String BigFootImageFront = "BigFoot.jpg";
	public static final String DruidImageFront = "Druid.jpg";
	public static final String DryadImageFront = "Dryad.jpg";
	
	//FROZEN WASTE
	public static final String DragonRiderImageFront = "DragonRider.jpg";
	public static final String ElkHerdImageFront = "ElkHerd.jpg";
	public static final String IceBatsImageFront = "IceBats.jpg";
	public static final String IceGiantImageFront = "IceGiant.jpg";
	public static final String IcewormImageFront = "Iceworm.jpg";
	public static final String KillerPenguinsImageFront = "KillerPenguins.jpg";
	public static final String KillerPuffinsImageFront = "KillerPuffins.jpg";
	public static final String MammothImageFront = "Mammoth.jpg";
	public static final String NorthWindImageFront = "NorthWind.jpg";
	public static final String WalrusImageFront = "Walrus.jpg";
	public static final String WhiteBearImageFront = "WhiteBear.jpg";
	public static final String WhiteDragonImageFront = "WhiteDragon.jpg";
	public static final String WolvesImageFront = "Wolves.jpg";
	public static final String EskimosImageFront = "Eskimos.jpg";
	
	//JUNGLE
	public static final String BirdOfParadiseImageFront = "BirdOfParadise.jpg";
	public static final String CrawlingVinesImageFront = "CrawlingVines.jpg";
	public static final String CrocodilesJungleImageFront = "CrocodilesJungle.jpg";
	public static final String DinosaurImageFront = "Dinosaur.jpg";
	public static final String ElephantImageFront = "Elephant.jpg";
	public static final String GiantSnakeJungleImageFront = "GiantSnakeJungle.jpg";
	public static final String HeadHunterImageFront = "HeadHunter.jpg";
	public static final String PterodactylWarriorsImageFront = "PterodactylWarriors.jpg";
	public static final String PygmiesImageFront = "Pygmies.jpg";
	public static final String WatusiImageFront = "Watusi.jpg";
	public static final String GiantApeImageFront = "GiantApe.jpg";
	public static final String WitchDoctorImageFront = "WitchDoctor.jpg";
	public static final String TigersImageFront = "Tigers.jpg";
	
	//MOUNTAIN
	public static final String BrownDragonImageFront = "BrownDragon.jpg";
	public static final String BrownKnightImageFront = "BrownKnight.jpg";
	public static final String CyclopsImageFront = "Cyclops.jpg";
	public static final String DwarvesImageFront = "Dwarves.jpg";
	public static final String Dwarves1ImageFront = "Dwarves1.jpg";
	public static final String Dwarves2ImageFront = "Dwarves2.jpg";
	public static final String GiantRocImageFront = "GaintRoc.jpg";
	public static final String GiantImageFront = "Giant.jpg";
	public static final String GiantCondorImageFront = "GiantCondor.jpg";
	public static final String GreatEagleImageFront = "GreatEagle.jpg";
	public static final String GreatHawkMountainImageFront = "GreatHawkMountain.jpg";
	public static final String LittleRocImageFront = "LittleRoc.jpg";
	public static final String MountainLionImageFront = "MountainLion.jpg";
	public static final String OgreImageFront = "Ogre.jpg";
	public static final String TrollImageFront = "Troll.jpg";
	public static final String MountainMenImageFront = "MountainMen.jpg";
	public static final String GoblinsImageFront = "Goblins.jpg";
	
	//PLAINS
	public static final String BuffaloHerdImageFront = "BuffaloHerd.jpg";
	public static final String BuffaloHerd1ImageFront = "BuffaloHerd1.jpg";
	public static final String CentaurImageFront = "Centaur.jpg";
	public static final String DragonflyImageFront = "Dragonfly.jpg";
	public static final String EaglesImageFront = "Eagles.jpg";
	public static final String FlyingBuffaloImageFront = "FlyingBuffalo.jpg";
	public static final String GiantBeetleImageFront = "GiantBeetle.jpg";
	public static final String GreatHawkPlainsImageFront = "GreatHawkPlains.jpg";
	public static final String GreathunterImageFront = "Greathunter.jpg";
	public static final String GypsiesImageFront = "Gypsies.jpg";
	public static final String Gypsies1ImageFront = "Gypsies1.jpg";
	public static final String HunterImageFront = "Hunter.jpg";
	public static final String LionPrideImageFront = "LionPride.jpg";
	public static final String PegasusImageFront = "Pegasus.jpg";
	public static final String PterodactylImageFront = "Pterodactyl.jpg";
	public static final String VillainsImageFront = "Villains.jpg";
	public static final String WhiteKnightImageFront = "WhiteKnight.jpg";
	public static final String WolfPackImageFront = "WolfPack.jpg";
	public static final String Tribesmen1ImageFront = "Tribesmen1.jpg";
	public static final String TribesmenImageFront = "Tribesmen.jpg";
	public static final String FarmersImageFront = "Farmers.jpg";

	//SWAMP
	public static final String BasiliskImageFront = "Basilisk.jpg";
	public static final String BlackKnightImageFront = "BlackKnight.jpg";
	public static final String CrocodilesSwampImageFront = "CrocodilesSwamp.jpg";
	public static final String DarkWizardImageFront = "DarkWizard.jpg";
	public static final String GiantMosquitoImageFront = "GiantMosquito.jpg";
	public static final String GiantSnakeSwampImageFront = "GiantSnakeSwamp.jpg";
	public static final String HugeLeechImageFront = "HugeLeech.jpg";
	public static final String PiratesImageFront = "Pirates.jpg";
	public static final String PoisonFrogImageFront = "PoisonFrog.jpg";
	public static final String SpiritImageFront = "Spirit.jpg";
	public static final String SpriteImageFront = "Sprite.jpg";
	public static final String SlimeBeastImageFront = "SlimeBeast.jpg";
	public static final String SwampGasImageFront = "SwampGas.jpg";
	public static final String ThingImageFront = "Thing.jpg";
	public static final String SwampRatImageFront = "SwampRat.jpg";
	public static final String VampireBatImageFront = "VampireBat.jpg";
	public static final String WatersnakeImageFront = "Watersnake.jpg";
	public static final String WillOWispImageFront = "Will-O-Wisp.jpg";
	public static final String WingedPirhanaImageFront = "WingedPirhana.jpg";
	public static final String GiantLizardImageFront = "GiantLizard.jpg";
	public static final String GhostImageFront = "Ghost.jpg";
	
	//FORTS
	public static final String TowerImageFront = "TowerFront.jpg";
	public static final String TowerImageBack = "TowerBack.jpg";
	public static final String KeepImageFront = "KeepFront.jpg";
	public static final String KeepImageBack = "KeepBack.jpeg";
	public static final String CastleImageFront = "CastleFront.jpg";
	public static final String CastleImageBack = "CastleBack.jpg";
	public static final String CitadelImageFront = "CitadelFront.jpg";
	public static final String CitadelImageBack = "CitadelBack.jpg";
	
	//SPECIAL INCOMES
	public static final String CityImageFront = "CityFront.jpg";
	public static final String CityImageBack = "CityBack.jpg";
	public static final String VillageImageFront = "VillageFront.jpg";
	public static final String VillageImageBack = "VillageBack.jpg";
	public static final String CopperMineImageFront = "CopperMine.jpg";
	public static final String SilverMineImageFront = "SilverMine.jpg";
	public static final String GoldMineImageFront = "GoldMine.jpg";
	public static final String DiamondFieldImageFront = "DiamondField.jpg";
	public static final String ElephantsGraveyardImageFront = "ElephantsGraveyard.jpg";
	public static final String FarmlandsImageFront = "Farmlands.jpg";
	public static final String OilFieldImageFront = "OilField.jpg";
	public static final String PeatBogImageFront = "PeatBog.jpg";
	public static final String TimberlandImageFront = "Timberland.jpg";
	
	//HEXTILES
	public static final String SeaTileFront = "Sea.png";
	public static final String JungleTileFront = "Jungle.png";
	public static final String FrozenWasteTileFront = "FrozenWaste.png";
	public static final String ForestTileFront = "Forest.png";
	public static final String PlainsTileFront = "Plains.png";
	public static final String SwampTileFront = "Swamp.png";
	public static final String MountainTileFront = "Mountain.png";
	public static final String DesertTileFront = "Desert.png";
	
	//TREASURE
	public static final String DiamondImageFront = "Diamond.jpg";
	public static final String EmeraldImageFront = "Emerald.jpg";
	public static final String PearlImageFront = "Pearl.jpg";
	public static final String RubyImageFront = "Ruby.jpg";
	public static final String SapphireImageFront = "Sapphire.jpg";
	public static final String TreasureChestImageFront = "TreasureChest.jpg";
	
	//MAGIC
	public static final String BalloonImageFront = "Balloon.jpg";
	public static final String BowMagicImageFront = "BowMagic.jpg";
	public static final String DispellMagicImageFront = "DispellMagic.jpg";
	public static final String DustOfDefenseImageFront = "DustOfDefence.jpg";
	public static final String ElixirImageFront = "Elixir.jpg";
	public static final String FanImageFront = "Fan.jpg";
	public static final String FirewallImageFront = "Firewall.jpg";
	public static final String GolemImageFront = "Golem.jpg";
	public static final String LuckyCharmImageFront = "LuckyCharm.jpg";
	public static final String SwordImageFront = "Sword.jpg";
	public static final String TalismanImageFront = "Talisman.jpg";
	
	//SPECIAL CHARACTERS
	public static final String ArchClericImageFront = "ArchCleric.jpg";
	public static final String ArchMageImageFront = "ArchMage.jpg";
	public static final String AssassinPrimusImageFront = "AssassinPrimus.jpg";
	public static final String BaronMunchausenImageFront = "BaronMunchausen.jpg";
	public static final String DeerhunterImageFront = "Deerhunter.jpg";
	public static final String DesertMasterImageFront = "DesertMaster.jpg";
	public static final String DwarfKingImageFront = "DwarfKing.jpg";
	public static final String ElfLordMasterImageFront = "ElfLord.jpg";
	public static final String ForestKingImageFront = "ForestKing.jpg";
	public static final String GhaogIIImageFront = "GhaogII.jpg";
	public static final String GrandDukeImageFront = "GrandDuke.jpg";
	public static final String IceLordImageFront = "IceLord.jpg";
	public static final String JungleLordImageFront = "JungleLord.jpg";
	public static final String LordOfEaglesImageFront = "LordOfEagles.jpg";
	public static final String MarksmanImageFront = "Marksman.jpg";
	public static final String MasterThiefImageFront = "MasterThief.jpg";
	public static final String MountainKingImageFront = "MountainKing.jpg";
	public static final String PlainsLordImageFront = "PlainsLord.jpg";
	public static final String SirLanceALotImageFront = "SirLance-A-Lot.jpg";
	public static final String SwampKingImageFront = "SwampKing.jpg";
	public static final String SwordmasterImageFront = "Swordmaster.jpg";
	public static final String WarlordImageFront = "Warlord.jpg";
	
	
	//Piece count constants
	public static final int MAX_NUM_THINGS = 300;
	public static final int MAX_NUM_THINGS_IN_RACK = 10;
	public static final int MAX_NUM_SPECIAL_CHARACTERS = 30;
	public static final int MAX_NUM_TILES = 100;
	public static final int NUM_FOUR_PLAYER_TILE_RINGS = 3;
	public static final int NUM_TWO_OR_THREE_PLAYER_TILE_RINGS = 2;
	public static final int INITIAL_GOLD_AMOUNT = 10;
	
	//Dice Constants
	public static final int DIE_1_SIDES = 6;
	public static final int DIE_2_SIDES = 6;
	public static final int DIE_3_SIDES = 6;
	public static final int DIE_4_SIDES = 6;
	
	//HexTile Constants
	public static final int MAX_NUM_THINGS_PER_HEX = 10;
	public static final int MAX_NUM_FORTS_PER_HEX = 1;
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
	
	public static final int NUM_CITIES = 10;
	public static final int NUM_VILLAGES = 10;

	public static final int NUM_DIAMONDS = 2;
	public static final int NUM_EMERALDS = 2;
	public static final int NUM_PEARLS = 2;
	public static final int NUM_RUBYS = 2;
	public static final int NUM_SAPPHIRES = 2;
	public static final int NUM_TREASURE_CHESTS = 2;
	
	public static final int CONSTRUCTION_COST = 5;
	public static final int DEFAULT_THING_VIEW_WIDTH = 50;
	
	public static final int SPECIAL_CHARACTER_AUG_ROLL_BEFORE_COST = 5;
	public static final int SPECIAL_CHARACTER_AUG_ROLL_AFTER_COST = 10;

	public static final int CITADEL_INCOME_4_PLAYERS = 20;
	public static final int CITADEL_INCOME_2_OR_3_PLAYERS = 15;
	
	public static final int MAX_MOVES_PER_TURN = 4;
	




	//battle turns
	public static enum BattleTurn {
		MAGIC,
		RANGED,
		OTHER
	}

	public static ControlledBy controlledByFromIndex(Integer currentPlayer) {
		switch(currentPlayer)
		{
		case 0:
			return ControlledBy.PLAYER1;
		case 1:
			return ControlledBy.PLAYER2;
		case 2:
			return ControlledBy.PLAYER3;
		case 3:
			return ControlledBy.PLAYER4;
		default:
			return ControlledBy.NEUTRAL;
		}
	}
}
