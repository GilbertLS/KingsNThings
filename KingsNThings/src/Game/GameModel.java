package Game;

import Game.SpecialCharacter;

import java.util.ArrayList;

import Game.GameConstants.ControlledBy;
import Game.GameConstants.Level;
import Game.GameConstants.SettlementType;
import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;
import Game.Networking.GameClient;

import java.util.Collections;
import java.util.LinkedList;
/*
 * This class models a King's N' Things Game. Its methods are called at appropriate times
 * by the Game class
 */
public class GameModel {
	//--------GAME OBJECTS-----------
	//private GameBoard gameBoard;						//board holding the Hex Tiles in play
	private ArrayList<HexTile> unusedTiles;				//all unused HexTiles
	private Player player1, player2, player3, player4;	//Players of the game
	private Player currPlayer;
	private ArrayList<Thing> playingCup;					//Container to hold unplayed Things
	private ArrayList<Thing> copyPlayingCup;
	private ArrayList<SpecialCharacter> unownedCharacters;	//Container to hold unplayed Special Characters
	private int playerCount;
	public GameBoard gameBoard;
	private ArrayList<Thing> movedThings = new ArrayList<Thing>();
	private boolean specialElimination = false;
	
	public BoardController boardController;

	
	public Player GetPlayer(int playerNum){
		return playerFromIndex(playerNum);
	}
	
	public void SetCurrentPlayer(int playerNum){
		if ( playerNum == 0 ) { currPlayer = player1; }
		if ( playerNum == 1 ) { currPlayer = player2; }
		if ( playerNum == 2 ) { currPlayer = player3; }
		if ( playerNum == 3 ) { currPlayer = player4; }
	}
	
	public int getCurrPlayerNumber() {
		if(currPlayer == null)
			return -1;
		
		return this.currPlayer.GetPlayerNum();
	}
	
	public Player GetCurrentPlayer(){
		return currPlayer;
	}
	
	public int PlayerCount(){
		return playerCount;
	}
	
	public GameModel()
	{
		gameBoard = new GameBoard();
		
		unusedTiles = new ArrayList<HexTile>();
		
		this.player1 = new Player(0);
		this.player2 = new Player(1);
		this.player3 = new Player(2);
		this.player4 = new Player(3);
		
		unownedCharacters = new ArrayList<SpecialCharacter>(GameConstants.MAX_NUM_SPECIAL_CHARACTERS);
		playingCup = new ArrayList<Thing>(GameConstants.MAX_NUM_THINGS);

		boardController = new BoardController(gameBoard);
		
		initializePlayingCup();
	}
	
	//create the 48 HexTiles to use for the Game
	public static String initializeHexTiles() {		
		String initializeHexTilesString = "";
		
		ArrayList<HexTile> newTiles = new ArrayList<HexTile>(GameConstants.MAX_NUM_TILES);
		
		//add all but 4 SEA HexTiles
		for(int i=0; i < GameConstants.NUM_SEA_TILES-4; i++)
		{
			newTiles.add(new HexTile(Terrain.SEA));
		}
		
		for(int i=0; i < GameConstants.NUM_JUNGLE_TILES; i++)
		{
			newTiles.add(new HexTile(Terrain.JUNGLE));
		}
		
		for(int i=0; i < GameConstants.NUM_FROZEN_WASTE_TILES; i++)
		{
			newTiles.add(new HexTile(Terrain.FROZEN_WASTE));
		}
		
		for(int i=0; i < GameConstants.NUM_FOREST_TILES; i++)
		{
			newTiles.add(new HexTile(Terrain.FOREST));
		}
		
		for(int i=0; i < GameConstants.NUM_PLAINS_TILES; i++)
		{
			newTiles.add(new HexTile(Terrain.PLAINS));
		}
		
		for(int i=0; i < GameConstants.NUM_SWAMP_TILES; i++)
		{
			newTiles.add(new HexTile(Terrain.SWAMP));
		}
		
		for(int i=0; i < GameConstants.NUM_MOUNTAIN_TILES; i++)
		{
			newTiles.add(new HexTile(Terrain.MOUNTAIN));
		}
		
		for(int i=0; i < GameConstants.NUM_DESERT_TILES; i++)
		{
			newTiles.add(new HexTile(Terrain.DESERT));
		}
		
		//shuffle tiles
		Collections.shuffle(newTiles);
		
		for(HexTile h: newTiles){
			initializeHexTilesString += h.terrain;
			initializeHexTilesString += "/";
		}
		
		return initializeHexTilesString;
	}
	
	public void setPlayerOrder(int firstPlayerIndex) {
		/*
		 * 0: 0 1 2 3 (+0)
		 * 1: 3 0 1 2 (+1)
		 * 2: 2 3 0 1 (+2)
		 * 3: 1 2 3 0 (+3)
		 */
		int playerShift = playerCount - firstPlayerIndex;
		
		player1.setPlayerOrder((0 + playerShift)%playerCount);
		player2.setPlayerOrder((1 + playerShift)%playerCount);
		player3.setPlayerOrder((2 + playerShift)%playerCount);
		player4.setPlayerOrder((3 + playerShift)%playerCount);
		
		System.out.println("Player 1 Player order: " + player1.getPlayerOrder());
		System.out.println("Player 2 Player order: " + player2.getPlayerOrder());
		System.out.println("Player 3 Player order: " + player3.getPlayerOrder());
		System.out.println("Player 4 Player order: " + player4.getPlayerOrder());
		
	}

	
	public void setPlayerCount(int playerCount)
	{
		this.playerCount = playerCount;
	}
	public void updatePlayerOrder() {
		player1.updatePlayerOrder(playerCount);	
		player2.updatePlayerOrder(playerCount);	
		player3.updatePlayerOrder(playerCount);	
		player4.updatePlayerOrder(playerCount);	
	}
	
	public HexTile[][] setInitialHexTiles(ArrayList<HexTile> hexTiles)
	{
		int x=0, y=0;	//current tile coordinates
		
		//add center tile
		gameBoard.addHexTile(hexTiles.remove(0), x, y);
		
		for(int i=1; 
				i<=
					(playerCount == 4?
							GameConstants.NUM_FOUR_PLAYER_TILE_RINGS:
							GameConstants.NUM_TWO_OR_THREE_PLAYER_TILE_RINGS);
				i++)
		{
			
			//move to next ring of tiles ("upwards")
			x++;
			y++;
			
			//place first tile in ring
			gameBoard.addHexTile(hexTiles.remove(0), x, y);
			
			//place top-right tiles
			for(int j=0; j<i; j++)
			{				
				//move in the negative y direction
				y--;
				
				gameBoard.addHexTile(hexTiles.remove(0), x, y);
			}
						
			//place right tiles
			for(int j=0; j<i; j++)
			{
				//move in the "downward" direction
				x--;
				y--;
				
				gameBoard.addHexTile(hexTiles.remove(0), x, y);
			}
			
			//place bottom-right tiles
			for(int j=0; j<i; j++)
			{				
				//move in the negative x direction
				x--;
				
				gameBoard.addHexTile(hexTiles.remove(0), x, y);
			}	
			
			//place bottom-left tiles
			for(int j=0; j<i; j++)
			{				
				//move in the positive y direction
				y++;
				
				gameBoard.addHexTile(hexTiles.remove(0), x, y);
			}
			
			//place left tiles
			for(int j=0; j<i; j++)
			{				
				//move in the "upward" direction
				x++;
				y++;
				
				gameBoard.addHexTile(hexTiles.remove(0), x, y);
			}
			
			//place top-left tiles
			for(int j=0; j<i; j++)
			{				
				//move in the positive x direction
				x++;
		
				gameBoard.addHexTile(hexTiles.remove(0), x, y);
			}
		}
		
		unusedTiles.addAll(hexTiles);
		for(int i=0; i<4; i++)
		{
			unusedTiles.add(new HexTile(Terrain.SEA));
		}
		
		return gameBoard.getTiles();
	}
	
	public void printCurrentBoardTiles()
	{
		gameBoard.printCurrentTiles();
	}

	private void initializeCreatures() {
		
		//DESERT
		playingCup.add(new Creature(Terrain.DESERT, "Baby Dragon", 3, GameConstants.BabyDragonImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.DESERT, "Camel Corps", 3, GameConstants.CamelCorpsImageFront));
		playingCup.add(new Creature(Terrain.DESERT, "Desert Bat", 1, GameConstants.DesertBatImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.DESERT, "Dust Devil", 4, GameConstants.DustDevilImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.DESERT, "Genie", 4, GameConstants.GenieImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.DESERT, "Giant Spider", 1, GameConstants.GiantSpiderImageFront));
		playingCup.add(new Creature(Terrain.DESERT, "Giant Wasp", 2, GameConstants.GiantWaspImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.DESERT, "Giant Wasp", 4, GameConstants.GiantWasp1ImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.DESERT, "Griffon", 2, GameConstants.GriffonImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.DESERT, "Old Dragon", 4, GameConstants.OldDragonImageFront)
		.Flying(true)
		.Magic(true));
		playingCup.add(new Creature(Terrain.DESERT, "Sandworm", 3, GameConstants.SandwormImageFront));
		playingCup.add(new Creature(Terrain.DESERT, "Sphinx", 4, GameConstants.SphinxImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.DESERT, "Yellow Knight", 3, GameConstants.YellowKnightImageFront)
		.Charge(true));
		
		for(int i=0; i<2; i++) { //some desert creatures appear twice
			playingCup.add(new Creature(Terrain.DESERT, "Dervish", 2, GameConstants.DervishImageFront)
			.Magic(true));
			playingCup.add(new Creature(Terrain.DESERT, "Nomads", 1, GameConstants.NomadsImageFront));
			playingCup.add(new Creature(Terrain.DESERT, "Skeletons", 1, GameConstants.SkeletonsImageFront));
			playingCup.add(new Creature(Terrain.DESERT, "Vultures", 1, GameConstants.VulturesImageFront)
			.Flying(true));
		}
		
		
		//FOREST
		playingCup.add(new Creature(Terrain.FOREST, "Bandits", 2, GameConstants.BanditsImageFront));
		playingCup.add(new Creature(Terrain.FOREST, "Bears", 2, GameConstants.BearsImageFront));
		playingCup.add(new Creature(Terrain.FOREST, "Big Foot", 5, GameConstants.BigFootImageFront));
		playingCup.add(new Creature(Terrain.FOREST, "Druid", 3, GameConstants.DruidImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.FOREST, "Dryad", 1, GameConstants.DryadImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.FOREST, "Elf Mage", 2, GameConstants.ElfMageImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.FOREST, "Elves", 3, GameConstants.Elves1ImageFront)
		.Ranged(true));
		playingCup.add(new Creature(Terrain.FOREST, "Flying Squirrel", 1, GameConstants.FlyingSquirrelImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.FOREST, "Flying Squirrel", 2, GameConstants.FlyingSquirrel1ImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.FOREST, "Forester", 2, GameConstants.ForesterImageFront)
		.Ranged(true));
		playingCup.add(new Creature(Terrain.FOREST, "Great Owl", 2, GameConstants.GreatOwlImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.FOREST, "Green Knight", 4, GameConstants.GreenKnightImageFront)
		.Charge(true));
		playingCup.add(new Creature(Terrain.FOREST, "Killer Raccoon", 2, GameConstants.KillerRaccoonImageFront));
		playingCup.add(new Creature(Terrain.FOREST, "Unicorn", 4, GameConstants.UnicornImageFront));
		playingCup.add(new Creature(Terrain.FOREST, "Walking Tree", 5, GameConstants.WalkingTreeImageFront));
		playingCup.add(new Creature(Terrain.FOREST, "Wild Cat", 2, GameConstants.WildCatImageFront));
		playingCup.add(new Creature(Terrain.FOREST, "Wyvern", 3, GameConstants.WyvernImageFront)
		.Flying(true));
		
		for(int i=0; i<2; i++) { //some forest creatures appear twice
			playingCup.add(new Creature(Terrain.FOREST, "Elves", 2, GameConstants.ElvesImageFront)
			.Ranged(true));
			playingCup.add(new Creature(Terrain.FOREST, "Pixies", 1, GameConstants.PixiesImageFront)
			.Flying(true));
		}
		
		
		//FROZEN WASTE
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "Dragon Rider", 3, GameConstants.DragonRiderImageFront)
		.Flying(true)
		.Ranged(true));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "Elk Herd", 2, GameConstants.ElkHerdImageFront));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "Ice Bats", 1, GameConstants.IceBatsImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "Ice Giant", 5, GameConstants.IceGiantImageFront)
		.Ranged(true));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "Iceworm", 4, GameConstants.IcewormImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "Killer Penguins", 3, GameConstants.KillerPenguinsImageFront));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "Killer Puffins", 2, GameConstants.KillerPuffinsImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "Mammoth", 5, GameConstants.MammothImageFront)
		.Charge(true));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "North Wind", 2, GameConstants.NorthWindImageFront)
		.Flying(true)
		.Magic(true));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "Walrus", 4, GameConstants.WalrusImageFront));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "White Bear", 4, GameConstants.WhiteBearImageFront));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "White Dragon", 5, GameConstants.WhiteDragonImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.FROZEN_WASTE, "Wolves", 3, GameConstants.WolvesImageFront));
		
		for(int i=0; i<2; i++) { //eskimos appears 4 times
			playingCup.add(new Creature(Terrain.FROZEN_WASTE, "Eskimos", 2, GameConstants.EskimosImageFront));
		}
		
		
		//JUNGLE
		playingCup.add(new Creature(Terrain.JUNGLE, "Bird Of Paradise", 1, GameConstants.BirdOfParadiseImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.JUNGLE, "Crawling Vines", 6, GameConstants.CrawlingVinesImageFront));
		playingCup.add(new Creature(Terrain.JUNGLE, "Crocodiles", 2, GameConstants.CrocodilesJungleImageFront));
		playingCup.add(new Creature(Terrain.JUNGLE, "Dinosaur", 4, GameConstants.DinosaurImageFront));
		playingCup.add(new Creature(Terrain.JUNGLE, "Elephant", 4, GameConstants.ElephantImageFront)
		.Charge(true));
		playingCup.add(new Creature(Terrain.JUNGLE, "Giant Snake", 3, GameConstants.GiantSnakeJungleImageFront));
		playingCup.add(new Creature(Terrain.JUNGLE, "Head Hunter", 2, GameConstants.HeadHunterImageFront)
		.Ranged(true));
		playingCup.add(new Creature(Terrain.JUNGLE, "Pterodactyl Warriors", 2, GameConstants.PterodactylWarriorsImageFront)
		.Ranged(true)
		.Flying(true));
		playingCup.add(new Creature(Terrain.JUNGLE, "Pygmies", 2, GameConstants.PygmiesImageFront));
		playingCup.add(new Creature(Terrain.JUNGLE, "Watusi", 2, GameConstants.WatusiImageFront));
		playingCup.add(new Creature(Terrain.JUNGLE, "Witch Doctor", 2, GameConstants.WitchDoctorImageFront)
		.Magic(true));

		for(int i=0; i<2; i++) { //some creatures appear twice
			playingCup.add(new Creature(Terrain.JUNGLE, "Giant Ape", 5, GameConstants.GiantApeImageFront));
			playingCup.add(new Creature(Terrain.JUNGLE, "Tigers", 3, GameConstants.TigersImageFront));
		}
		
		
		//MOUNTAIN
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Brown Dragon", 3, GameConstants.BrownDragonImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Brown Knight", 4, GameConstants.BrownKnightImageFront)
		.Charge(true));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Cyclops", 5, GameConstants.CyclopsImageFront));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Dwarves", 3, GameConstants.DwarvesImageFront)
		.Charge(true));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Dwarves", 2, GameConstants.Dwarves1ImageFront)
		.Ranged(true));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Dwarves", 3, GameConstants.Dwarves2ImageFront)
		.Ranged(true));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Giant Roc", 3, GameConstants.GiantRocImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Giant", 4, GameConstants.GiantImageFront)
		.Ranged(true));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Giant Condor", 3, GameConstants.GiantCondorImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Great Eagle", 2, GameConstants.GreatEagleImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Great Hawk", 1, GameConstants.GreatHawkMountainImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Little Roc", 2, GameConstants.LittleRocImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Mountain Lion", 2, GameConstants.MountainLionImageFront));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Ogre", 2, GameConstants.OgreImageFront));
		playingCup.add(new Creature(Terrain.MOUNTAIN, "Troll", 4, GameConstants.TrollImageFront));

		for(int i=0; i<2; i++) { //there are 2 mountain men
			playingCup.add(new Creature(Terrain.MOUNTAIN, "Mountain Men", 1, GameConstants.MountainMenImageFront));
		}
		
		for(int i=0; i<4; i++) { //there are 4 Goblins
			playingCup.add(new Creature(Terrain.MOUNTAIN, "Goblins", 1, GameConstants.GoblinsImageFront));
		}
		
		
		//PLAINS
		playingCup.add(new Creature(Terrain.PLAINS, "Buffalo Herd", 3, GameConstants.BuffaloHerdImageFront));
		playingCup.add(new Creature(Terrain.PLAINS, "Buffalo Herd", 4, GameConstants.BuffaloHerd1ImageFront));
		playingCup.add(new Creature(Terrain.PLAINS, "Centaur", 2, GameConstants.CentaurImageFront));
		playingCup.add(new Creature(Terrain.PLAINS, "Dragonfly", 2, GameConstants.DragonflyImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Eagles", 2, GameConstants.EaglesImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Flying Buffalo", 2, GameConstants.FlyingBuffaloImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Giant Beetle", 2, GameConstants.GiantBeetleImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Great Hawk", 2, GameConstants.GreatHawkPlainsImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Greathunter", 4, GameConstants.GreathunterImageFront)
		.Ranged(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Gypsies", 1, GameConstants.GypsiesImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Gypsies", 2, GameConstants.Gypsies1ImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Hunter", 1, GameConstants.HunterImageFront)
		.Ranged(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Lion Pride", 3, GameConstants.LionPrideImageFront));
		playingCup.add(new Creature(Terrain.PLAINS, "Pegasus", 2, GameConstants.PegasusImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Pterodactyl", 3, GameConstants.PterodactylImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Villains", 2, GameConstants.VillainsImageFront));
		playingCup.add(new Creature(Terrain.PLAINS, "White Knight", 3, GameConstants.WhiteKnightImageFront)
		.Charge(true));
		playingCup.add(new Creature(Terrain.PLAINS, "Wolf Pack", 3, GameConstants.WolfPackImageFront));
		playingCup.add(new Creature(Terrain.PLAINS, "Tribesmen", 1, GameConstants.Tribesmen1ImageFront)
		.Ranged(true));
	
		for(int i=0; i<2; i++) { //there are 2 Tribesmen
			playingCup.add(new Creature(Terrain.PLAINS, "Tribesmen", 2, GameConstants.TribesmenImageFront));
		}
		
		for(int i=0; i<4; i++) { //there are 4 Farmers
			playingCup.add(new Creature(Terrain.PLAINS, "Farmers", 1, GameConstants.FarmersImageFront));
		}
		
		
		//SWAMP
		playingCup.add(new Creature(Terrain.SWAMP, "Basilisk", 3, GameConstants.BasiliskImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.SWAMP, "Black Knight", 3, GameConstants.BlackKnightImageFront)
		.Charge(true));
		playingCup.add(new Creature(Terrain.SWAMP, "Crocodiles", 2, GameConstants.CrocodilesSwampImageFront));
		playingCup.add(new Creature(Terrain.SWAMP, "Dark Wizard", 1, GameConstants.DarkWizardImageFront)
		.Flying(true)
		.Magic(true));
		playingCup.add(new Creature(Terrain.SWAMP, "Giant Mosquito", 2, GameConstants.GiantMosquitoImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.SWAMP, "Giant Snake", 3, GameConstants.GiantSnakeSwampImageFront));
		playingCup.add(new Creature(Terrain.SWAMP, "Huge Leech", 2, GameConstants.HugeLeechImageFront));
		playingCup.add(new Creature(Terrain.SWAMP, "Pirates", 2, GameConstants.PiratesImageFront));
		playingCup.add(new Creature(Terrain.SWAMP, "Poison Frog", 1, GameConstants.PoisonFrogImageFront));
		playingCup.add(new Creature(Terrain.SWAMP, "Spirit", 2, GameConstants.SpiritImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.SWAMP, "Sprite", 1, GameConstants.SpriteImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.SWAMP, "Slime Beast", 3, GameConstants.SlimeBeastImageFront));
		playingCup.add(new Creature(Terrain.SWAMP, "Swamp Gas", 1, GameConstants.SwampGasImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.SWAMP, "Swamp Rat", 1, GameConstants.SwampRatImageFront));
		playingCup.add(new Creature(Terrain.SWAMP, "Thing", 2, GameConstants.ThingImageFront));
		playingCup.add(new Creature(Terrain.SWAMP, "Vampire Bat", 4, GameConstants.VampireBatImageFront)
		.Flying(true));
		playingCup.add(new Creature(Terrain.SWAMP, "Watersnake", 1, GameConstants.WatersnakeImageFront));
		playingCup.add(new Creature(Terrain.SWAMP, "Will-O-Wisp", 2, GameConstants.WillOWispImageFront)
		.Magic(true));
		playingCup.add(new Creature(Terrain.SWAMP, "Winged Pirhana", 3, GameConstants.WingedPirhanaImageFront)
		.Flying(true));

		for(int i=0; i<2; i++) { //there are 2 giant lizards
			playingCup.add(new Creature(Terrain.SWAMP, "Giant Lizard", 2, GameConstants.GiantLizardImageFront));
		}
		
		for(int i=0; i<4; i++) { //there are 4 ghosts
			playingCup.add(new Creature(Terrain.SWAMP, "Ghost", 1, GameConstants.GhostImageFront)
			.Flying(true));
		}
	}

	public void initializePlayingCup() {
		initializeCreatures();
		initializeSpecialIncomes();
		initializeTreasure();
		initializeMagic();
		initializeRandomEvents();
		copyPlayingCup();
	}
	
	private void copyPlayingCup() {
		copyPlayingCup = new ArrayList<Thing>(); 
		for(Thing t : playingCup) {
			if (t.thingType == ThingType.CREATURE) {
				Creature c = (Creature)t;
				
				if (c.name.equals("Giant Spider")) {
					copyPlayingCup.add(c.copy());
				}
				
				copyPlayingCup.add(c.copy());
			} else if (t.thingType == ThingType.SETTLEMENT) {
				Settlement s = (Settlement)t;
				
				copyPlayingCup.add(s.copy());
			} else if (t.thingType == ThingType.RANDOM_EVENT) {
				RandomEvent r = (RandomEvent)t;
				
				copyPlayingCup.add(r.copy());
			} else if (t.thingType == ThingType.TREASURE) {
				Treasure tr = (Treasure)t;
				
				copyPlayingCup.add(tr.copy());
			} else if (t.thingType == ThingType.SPECIAL_INCOME) {
				SpecialIncome sp = (SpecialIncome)t;
				
				copyPlayingCup.add(sp.copy());
			}
		}
	}
	
	private void copySpecialCharacters() {
		for(Thing t : unownedCharacters) {
			if (t.thingType == ThingType.TERRAIN_LORD) {
				TerrainLord l = (TerrainLord)t;
				
				copyPlayingCup.add(l.copy());
			} else if (t.thingType == ThingType.SPECIAL_CHARACTER) {
				SpecialCharacter sc = (SpecialCharacter)t;
				
				copyPlayingCup.add(sc.copy());
			}
		}
	}
	
	private void initializeRandomEvents() {
		playingCup.add(new RandomEvent("Defection", GameConstants.DefectionImageFront));
	}

	public void initializeSpecialCharacters(String s)
	{
		String[] initValues = s.trim().split(" ");
		
		if(Integer.parseInt(initValues[0]) == 0)
			unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Arch Cleric", 5, GameConstants.ArchClericImageFront)
			.Magic(true));
		else
			unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Arch Mage", 6, GameConstants.ArchMageImageFront)
			.Magic(true));
		
		if(Integer.parseInt(initValues[1]) == 0)
			unownedCharacters.add(new SpecialCharacter("Assassin Primus", 4, GameConstants.AssassinPrimusImageFront));
		else
			unownedCharacters.add(new SpecialCharacter("Baron Munchausen", 4, GameConstants.BaronMunchausenImageFront));
		
		/*if(Integer.parseInt(initValues[2]) == 0)
			unownedCharacters.add(new SpecialCharacter("Deerhunter", 4, GameConstants.DeerhunterImageFront));
		else*/
			unownedCharacters.add(new TerrainLord(Terrain.DESERT,"Desert Master", 4, GameConstants.DesertMasterImageFront));
		
		if(Integer.parseInt(initValues[3]) == 0)
			unownedCharacters.add(new SpecialCharacter("Dwarf King", 5, GameConstants.DwarfKingImageFront));
		else
			unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Elf Lord", 6, GameConstants.ElfLordMasterImageFront)
			.Ranged(true));
		
		if(Integer.parseInt(initValues[4]) == 0)
			unownedCharacters.add(new TerrainLord(Terrain.FOREST, "Forest King", 4, GameConstants.ForestKingImageFront));
		else
			unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Ghaog II", 6, GameConstants.GhaogIIImageFront)
			.Flying(true));
		
		if(Integer.parseInt(initValues[5]) == 0)
			unownedCharacters.add(new SpecialCharacter("Grand Duke", 4, GameConstants.GrandDukeImageFront));
		else
			unownedCharacters.add(new TerrainLord(Terrain.FROZEN_WASTE, "Ice Lord", 4, GameConstants.IceLordImageFront));
		
		if(Integer.parseInt(initValues[6]) == 0)
			unownedCharacters.add(new TerrainLord(Terrain.JUNGLE, "Jungle Lord", 4, GameConstants.JungleLordImageFront));
		else
			unownedCharacters.add((SpecialCharacter)new SpecialCharacter( "Lord Of Eagles", 5, GameConstants.LordOfEaglesImageFront)
			.Flying(true));
		
		/*if(Integer.parseInt(initValues[7]) == 0)
			unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Marksman", 5, GameConstants.MarksmanImageFront)
			.Ranged(true));
		else*/
			unownedCharacters.add(new SpecialCharacter("Master Thief", 4, GameConstants.MasterThiefImageFront));
		
		if(Integer.parseInt(initValues[8]) == 0)
			unownedCharacters.add(new TerrainLord(Terrain.MOUNTAIN, "Mountain King", 4, GameConstants.MountainKingImageFront));
		else
			unownedCharacters.add(new TerrainLord(Terrain.PLAINS, "Plains Lord", 4, GameConstants.PlainsLordImageFront));
		
		if(Integer.parseInt(initValues[9]) == 0)
			unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Sir Lance-A-Lot", 5, GameConstants.SirLanceALotImageFront)
			.Charge(true));
		else
			unownedCharacters.add(new TerrainLord(Terrain.SWAMP, "Swamp King", 4, GameConstants.SwampKingImageFront));
		
		if(Integer.parseInt(initValues[10]) == 0)
			unownedCharacters.add(new SpecialCharacter("Swordmaster", 4, GameConstants.SwordmasterImageFront));
		else
			unownedCharacters.add(new SpecialCharacter("Warlord", 5, GameConstants.WarlordImageFront));
			
		copySpecialCharacters();
	}

	public String randomizeSpecialCharactersString() {
		String s = "";
		int n;
		
		for(int i=0; i<11; i++)
		{
			n = (int)Math.floor(Math.random()*2);
			s += n + " ";
		}
		
		return s;
	}

	private void initializeMagic() {
		playingCup.add(new Magic("Balloon", GameConstants.BalloonImageFront));
		playingCup.add(new Magic("Bow Magic", GameConstants.BowMagicImageFront));
		playingCup.add(new Magic("Dispell Magic", GameConstants.DispellMagicImageFront));
		playingCup.add(new Magic("Dust Of Defense", GameConstants.DustOfDefenseImageFront));
		playingCup.add(new Magic("Elixir", GameConstants.ElixirImageFront));
		playingCup.add(new Magic("Fan", GameConstants.FanImageFront));
		playingCup.add(new Magic("Firewall", GameConstants.FirewallImageFront));
		playingCup.add(new Magic("Golem", GameConstants.GolemImageFront));
		playingCup.add(new Magic("Lucky Charm", GameConstants.LuckyCharmImageFront));
		playingCup.add(new Magic("Sword", GameConstants.SwordImageFront));
		playingCup.add(new Magic("Talisman", GameConstants.TalismanImageFront));
	}

	private void initializeTreasure() {
		for(int i=0; i<GameConstants.NUM_DIAMONDS; i++)
			playingCup.add(new Treasure("Diamond", 5, GameConstants.DiamondImageFront));
		
		for(int i=0; i<GameConstants.NUM_EMERALDS; i++)
			playingCup.add(new Treasure("Emerald", 10, GameConstants.EmeraldImageFront));
		
		for(int i=0; i<GameConstants.NUM_PEARLS; i++)
			playingCup.add(new Treasure("Pearl", 5, GameConstants.PearlImageFront));
		
		for(int i=0; i<GameConstants.NUM_RUBYS; i++)
			playingCup.add(new Treasure("Ruby", 10, GameConstants.RubyImageFront));
		
		for(int i=0; i<GameConstants.NUM_SAPPHIRES; i++)
			playingCup.add(new Treasure("Sapphire", 5, GameConstants.SapphireImageFront));
		
		for(int i=0; i<GameConstants.NUM_TREASURE_CHESTS; i++)
			playingCup.add(new Treasure("Treasure Chest", 20, GameConstants.TreasureChestImageFront));
	}

	private void initializeSpecialIncomes() {
		for(int i=0; i<GameConstants.NUM_CITIES; i++)
		{
			playingCup.add(new Settlement("City", 2, GameConstants.CityImageFront, GameConstants.CityImageBack, SettlementType.CITY));
		}
		
		for(int i=0; i<GameConstants.NUM_VILLAGES; i++)
		{
			playingCup.add(new Settlement("Village", 1, GameConstants.VillageImageFront, GameConstants.VillageImageBack, SettlementType.VILLAGE));
		}

		
		playingCup.add(new SpecialIncome(Terrain.MOUNTAIN, "Copper Mine", GameConstants.CopperMineImageFront, 1));
		playingCup.add(new SpecialIncome(Terrain.MOUNTAIN, "Silver Mine", GameConstants.SilverMineImageFront, 2));
		playingCup.add(new SpecialIncome(Terrain.MOUNTAIN, "Gold Mine", GameConstants.GoldMineImageFront, 3));
		
		playingCup.add(new SpecialIncome(Terrain.DESERT, "Diamond Field", GameConstants.DiamondFieldImageFront, 1));
		
		playingCup.add(new SpecialIncome(Terrain.JUNGLE, "Elephants' Graveyard", GameConstants.ElephantsGraveyardImageFront, 3));
		
		playingCup.add(new SpecialIncome(Terrain.PLAINS, "Farmlands", GameConstants.FarmlandsImageFront, 1));

		playingCup.add(new SpecialIncome(Terrain.FROZEN_WASTE, "Oil Field", GameConstants.OilFieldImageFront, 3));
		
		playingCup.add(new SpecialIncome(Terrain.SWAMP, "Peat Bog", GameConstants.PeatBogImageFront, 1));
		
		playingCup.add(new SpecialIncome(Terrain.FOREST, "Timberland", GameConstants.TimberlandImageFront, 1));

	}

	public ArrayList<Thing> getThingsFromCup(int numThings) {
		ArrayList<Thing> things = new ArrayList<Thing>();
		
		if(! specialElimination)
		{
			for(int i=0; i<numThings; i++)
			{
				if(!playingCup.isEmpty())
				{
					Thing currentThing = playingCup.remove(playingCup.size()-1);
					currentThing.setControlledBy(ControlledBy.NEUTRAL);
					things.add(currentThing);
				}
				else
				{
					specialElimination = true;
					return things;
				}
			}
		}
		
		return things;
	}
	
	public ArrayList<Thing> getAllTestPlayingCupCreatures() {
		ArrayList<Thing> things = new ArrayList<Thing>();
		
		things.addAll(copyPlayingCup);
		
		return things;
	}
	
	public Thing removeThingFromTestCup(int thingId) {
		Thing ret = null;
		for(Thing t : copyPlayingCup) {
			if (t.thingID == thingId) {
				ret = t;
			}
		}
		
		if(ret != null) { copyPlayingCup.remove(ret); }
		
		return ret;
	}

	public void distributeInitialGold() {
		player1.addGold(GameConstants.INITIAL_GOLD_AMOUNT);
		player2.addGold(GameConstants.INITIAL_GOLD_AMOUNT);
		player3.addGold(GameConstants.INITIAL_GOLD_AMOUNT);
		player4.addGold(GameConstants.INITIAL_GOLD_AMOUNT);
	}
	
	public int[] distributeIncome()
	{
		Player player;
		int[] playerGoldUpdates = new int[playerCount];
		
		for(int i=0; i<playerCount; i++)
		{
			player = playerFromIndex(i);
		
			playerGoldUpdates[i] = player.getIncome();
			
			player.addGold(playerGoldUpdates[i]);
		}
		
		return playerGoldUpdates;
	}

	public boolean isValidControlMarkerPlacement(HexTile selectedTile) {
		if(selectedTile.isControlledBy(ControlledBy.NEUTRAL))
		{
			if(!selectedTile.isLand() && !currPlayer.hasNoHexes())
				return false;
			
			if(currPlayer.hasNoHexes())
			{
				int x = selectedTile.x;
				int y = selectedTile.y;
				
				if(playerCount == 4)
				{
					if((x == 3 && y == 1)
							||(x == 1 && y == 3)
							||(x == -3 && y == -1)
							||(x == -1 && y == -3))
						return true;
				}
				else
				{
					if((x == 2 && y == 1)
							||(x == 1 && y == 2)
							||(x == -2 && y == -1)
							||(x == -1 && y == -2))
						return true;
				}
			}
			else
			{
				for(int i=0; i<playerCount; i++)
				{
					if(i != currPlayer.GetPlayerNum())
						for(HexTile h: playerFromIndex(i).getOwnedHexTiles())
							if(h.isAdjacent(selectedTile))
								return false;
				}
				
				for(HexTile h: currPlayer.getOwnedHexTiles())
				{
					if(selectedTile.isAdjacent(h))
						return true;
				}
			}
		}
		return false;
	}

	public boolean isValidTowerPlacement(HexTile selectedTile) {
		return (selectedTile.isControlledBy(currPlayer.faction)
				&& !selectedTile.hasFort()
				&& selectedTile.isLand());
	}

	public HexTile addTower(int x, int y, int playerIndex) {
		Player player = playerFromIndex(playerIndex);
		HexTile h = gameBoard.getTile(x, y);
		
		addTower(h,playerIndex);
		
		return h;
	}
	
	public HexTile addTower(HexTile h, int playerIndex) {
		Player player = playerFromIndex(playerIndex);
	
		Fort f = new Fort();
		f.setControlledBy(player.faction);
		
		h.addFort(f);
		player.addFort(f);
		
		return h;
	}
	
	public Player playerFromIndex(int index)
	{
		switch(index)
		{
		case 0:
			return player1;
		case 1:
			return player2;
		case 2:
			return player3;
		case 3:
			return player4;
		}
		
		return null;
	}

	public void removeFromPlayerRack(ArrayList<Integer> thingIDs, int playerIndex) {
		for(Integer i: thingIDs)
			removeFromPlayerRack(i, playerIndex);
	}

	public void updatePlayedThings(ArrayList<HexTile> hexTiles, ArrayList<Integer> thingIDs, int playerIndex) {		
		Player player = playerFromIndex(playerIndex);
		
		int i=0;
		for(HexTile h: hexTiles)
		{
			int thingID = thingIDs.get(i++);
			
			Thing thingPlayed = player.getThingInRackByID(thingID);
				
			gameBoard.getTile(h.x, h.y).AddThingToTile(playerIndex, thingPlayed);
		}
	}

	public void updateMovedThings(ArrayList<HexTile> hexTiles,
			ArrayList<Integer> thingIDs,
			int playerIndex) {
		Player player = playerFromIndex(playerIndex);
		
		HexTile tileTo = hexTiles.get(1);
		HexTile tileFrom = hexTiles.get(0);
		
		for(Integer id: thingIDs)
		{			
			Thing thingPlayed = tileFrom.getThingFromTileByID(id);
			
			tileFrom.removeThing(id, playerIndex); 
			tileTo.AddThingToTile(playerIndex, thingPlayed);
			
			//handle movement speed
			thingPlayed.numMoves += tileTo.moveValue;
			
			movedThings.add(thingPlayed);
			
			if(tileTo.isControlledBy(ControlledBy.NEUTRAL)
					&& tileTo.noOtherPlayerOnTile(playerIndex))
			{
				updateTileFaction(tileTo);
			}
		}
	}

	public void clearThingMoves() {
		for(Thing t: movedThings)
			t.clearMoves();
		
		movedThings = new ArrayList<Thing>();
	}

	public String getCupIDsString() {
		String ret = "";
		
		for(Thing t: playingCup)
		{
			ret += t.thingID + " ";
		}
		
		return ret;
	}

	public void setPlayingCupOrder(ArrayList<Integer> thingIDs) {
		ArrayList<Thing> newCup = new ArrayList<Thing>();
		
		for(Integer i: thingIDs)
		{
			for(Thing t: playingCup)
			{
				if(t.thingID == i)
					newCup.add(t);
			}
		}
		
		playingCup = newCup;
	}

	public void updateConstruction(HexTile hexTile, int playerIndex) {
		hexTile.setConstructionAllowed(false);
		
		if(hexTile.hasFort()){
			if(!(hexTile.getFort().getLevel() == Level.CASTLE
				&& playerFromIndex(playerIndex).hasCitadel()))
					hexTile.getFort().upgrade();
		}
		else
			addTower(hexTile, playerIndex);
		
		if(hexTile.getFort().getLevel() == Level.CITADEL)
			playerFromIndex(playerIndex).setCitadelConstructed(true);
		
		playerFromIndex(playerIndex).payGold(GameConstants.CONSTRUCTION_COST);
	}

	public boolean isValidConstruction(HexTile h, int playerIndex) {
		Player player = playerFromIndex(playerIndex);
		
		//invalid if not controlled
		if(!player.ownsTile(h))
			return false;
		
		//invalid if already constructed
		else if(!h.constructionAllowed)
			return false;
		
		//or if cant afford
		else if(player.getGold() < 5)
			return false;
		
		//or has fort and...
		else if(h.hasFort())
		{
			//is trying to upgrade to citadel but doesn't have the income or..
			if(h.getFort().getLevel() == Level.CASTLE)
			{
				if(player.getIncome() < (playerCount == 4?
						GameConstants.CITADEL_INCOME_4_PLAYERS 
						: GameConstants.CITADEL_INCOME_2_OR_3_PLAYERS ))
						
					return false;
			}
			//cannot upgrade fort further
			else if(h.getFort().getLevel() == Level.CITADEL)
				return false;
		}
		
		return true;
	}

	public ArrayList<SpecialCharacter> getUnownedSpecialCharacters() {
		return unownedCharacters;
	}

	public boolean canAfford(int amount,
			int playerIndex) {
		return playerFromIndex(playerIndex).canAfford(amount);
	}

	public void payGold(int cost,
			int playerIndex) {
		playerFromIndex(playerIndex).payGold(cost);
	}

	public HexTile recruitSpecialCharacter(int thingID, int playerIndex) {
		HexTile ret = null;
		Thing t = null;
		
		for(SpecialCharacter sc: unownedCharacters){
			if(sc.thingID == thingID){
				t = sc;
			}
		}
		if(t!=null)
			unownedCharacters.remove(t);
		
		if(t == null){
			//check for in-play Special Characters
			for(HexTile[] hexArray: gameBoard.getTiles())
				for(HexTile h: hexArray)
					if(h != null){
								
						//iterate over players
						for(int i=0; i<5; i++){
		
							//find any special characters
							for(Thing thing: h.GetThings(i))
								if(thing.isSpecialCharacter())
									if(thing.thingID == thingID){
										t = thing;
										playerFromIndex(i).removeSpecialCharacter((SpecialCharacter) thing);
										ret = h;
									}
						}
						
					}
			
			if(t != null){
				ret.removeThing(thingID, t.getControlledByPlayerNum());	
			}
		}
		
		if(t != null)
			playerFromIndex(playerIndex).addThingToRack(t);
		
		return ret;
	}

	public void handleRackOverload(int playerIndex) {
		Player player = playerFromIndex(playerIndex);
		
		player.playerRack.handleRackOverload();
	}

	public boolean playerRackTooFull(int playerIndex) {	
		return playerFromIndex(playerIndex).rackTooFull();
	}

	public void tradeThings(int playerIndex, boolean isInitialTrade,
			ArrayList<Integer> tradedThingIDs) {
		Player player = playerFromIndex(playerIndex);
		
		ArrayList<Thing> things = new ArrayList<Thing>();
		
		//find things and remove from rack
		for(Integer i: tradedThingIDs)
		{
			things.add(player.getPlayerRack().getThing(i));
			player.removeFromRackByID(i);
		}
		
		//return them to the cup
		for(Thing t: things){
			returnToCup(t);
		}
		
		int numThings =0;
		if(isInitialTrade)
			numThings = things.size();
		else
			numThings = things.size()/2;
		
		//get new things from the cup
		ArrayList<Thing> newThings = getThingsFromCup(numThings);
		
		for(Thing t: newThings)
			GameClient.game.gameModel.playerFromIndex(playerIndex).addThingToRack(t);
	}

	private void returnToCup(Thing t) {
		if(!t.isControlledBy(ControlledBy.NEUTRAL))
		{
			Player player = playerFromFaction(t.getControlledBy());
			if(t.thingType == ThingType.SETTLEMENT)
				player.removeSettlement((Settlement)t);
			else if(t.thingType == ThingType.SPECIAL_INCOME)
				player.removeSpecialIncome((SpecialIncome)t);
		}	
		
		t.setControlledBy(ControlledBy.NEUTRAL);
		
		//enforce special elimination
		if(!specialElimination)
			playingCup.add(0, t);
		else
			t = null;
	}
	
	private void returnSpecialCharacter(SpecialCharacter sc, boolean flip){
		Player player = playerFromFaction(sc.getControlledBy());
		
		//remove from player's ownership
		player.removeSpecialCharacter(sc);		
		sc.setControlledBy(ControlledBy.NEUTRAL);
		
		//add back to unused characters
		if(flip){	//swap special character
			switch(sc.name){
			case "Arch Cleric":
				unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Arch Mage", 6, GameConstants.ArchMageImageFront)
				.Magic(true));
				break;
			case "Arch Mage":
				unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Arch Cleric", 5, GameConstants.ArchClericImageFront)
				.Magic(true));
				break;
			case "Assassin Primus":
				unownedCharacters.add(new SpecialCharacter("Baron Munchausen", 4, GameConstants.BaronMunchausenImageFront));
				break;
			case "Baron Munchausen":
				unownedCharacters.add(new SpecialCharacter("Assassin Primus", 4, GameConstants.AssassinPrimusImageFront));
				break;
			case "Deerhunter":
				unownedCharacters.add(new TerrainLord(Terrain.DESERT,"Desert Master", 4, GameConstants.DesertMasterImageFront));
				break;
			case "Desert Master":
				unownedCharacters.add(new SpecialCharacter("Deerhunter", 4, GameConstants.DeerhunterImageFront));
				break;
			case "Dwarf King":
				unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Elf Lord", 6, GameConstants.ElfLordMasterImageFront)
				.Ranged(true));
				break;
			case "Elf Lord":
				unownedCharacters.add(new SpecialCharacter("Dwarf King", 5, GameConstants.DwarfKingImageFront));
				break;
			case "Forest King":
				unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Ghaog II", 6, GameConstants.GhaogIIImageFront)
				.Flying(true));
				break;
			case "Ghaog II":
				unownedCharacters.add(new TerrainLord(Terrain.FOREST, "Forest King", 4, GameConstants.ForestKingImageFront));
				break;
			case "Grand Duke":
				unownedCharacters.add(new TerrainLord(Terrain.FROZEN_WASTE, "Ice Lord", 4, GameConstants.IceLordImageFront));
				break;
			case "Ice Lord":
				unownedCharacters.add(new SpecialCharacter("Grand Duke", 4, GameConstants.GrandDukeImageFront));
				break;
			case "Jungle Lord":
				unownedCharacters.add((SpecialCharacter)new SpecialCharacter( "Lord Of Eagles", 5, GameConstants.LordOfEaglesImageFront)
				.Flying(true));
				break;
			case "Lord Of Eagles":
				unownedCharacters.add(new TerrainLord(Terrain.JUNGLE, "Jungle Lord", 4, GameConstants.JungleLordImageFront));
				break;
			case "Marksman":
				unownedCharacters.add(new SpecialCharacter("Master Thief", 4, GameConstants.MasterThiefImageFront));
				break;
			case "Master Thief":
				unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Marksman", 5, GameConstants.MarksmanImageFront)
				.Ranged(true));
				break;
			case "Mountain King":
				unownedCharacters.add(new TerrainLord(Terrain.PLAINS, "Plains Lord", 4, GameConstants.PlainsLordImageFront));
				break;
			case "Plains Lord":
				unownedCharacters.add(new TerrainLord(Terrain.MOUNTAIN, "Mountain King", 4, GameConstants.MountainKingImageFront));
				break;
			case "Sir Lance-A-Lot":
				unownedCharacters.add(new TerrainLord(Terrain.SWAMP, "Swamp King", 4, GameConstants.SwampKingImageFront));
				break;
			case "Swamp King":
				unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Sir Lance-A-Lot", 5, GameConstants.SirLanceALotImageFront)
				.Charge(true));
				break;
			case "Swordmaster":
				unownedCharacters.add(new SpecialCharacter("Warlord", 5, GameConstants.WarlordImageFront));
				break;
			case "Warlord":
				unownedCharacters.add(new SpecialCharacter("Swordmaster", 4, GameConstants.SwordmasterImageFront));
				break;
			}
		}
		else{	//otherwise, just return
			unownedCharacters.add(sc);
		}	
	}

	public void randomizePlayingCup() {
		Collections.shuffle(playingCup);
		
	}

	public void randomizeUnusedTiles() {
		Collections.shuffle(unusedTiles);
	}

	public String getUnusedTileString() {
		String ret = "";
		
		for(HexTile h: unusedTiles)
		{
			ret += h.terrain + " ";
		}
		
		return ret;
	}

	public void setUnusedTiles(ArrayList<Terrain> tileTerrains) {
		ArrayList<HexTile> newTiles = new ArrayList<HexTile>();
		
		for(Terrain t: tileTerrains)
		{
			newTiles.add(new HexTile(t));
		}
		
		unusedTiles = newTiles;
	}

	public String checkWin() {
		boolean winnerFound = false;
		int winningIndex = -1;
		
		for(int i=0; i<GameClient.game.gameModel.playerCount; i++)
		{
			Player player = playerByOrder(i);
				
			//player has more than 1 citadel (player wins)
			//or player constructed and held a citadel since last round (player wins)
			if(player.getNumCitadels() > 1)	{
				winningIndex = player.GetPlayerNum();
				winnerFound = true;
				break;
			}
			else if(player.citadelWasConstructed() && player.getRoundsSinceCitadel() >= 1){
				if(!winnerFound){
					winningIndex = player.GetPlayerNum();
					winnerFound = true;
				}
				else{
					winningIndex = -1;
					winnerFound = false;
					break;
				}
			}				
		}
		
		String ret = "";
		if(winnerFound){
			ret = "" + true + "SPLIT";
			ret += winningIndex;
		} else {
			ret = ""+false + "SPLIT";
		}
		
		return ret;
	}

	private Player playerByOrder(int i) {
		if(player1.getPlayerOrder() == i)
			return player1;
		else if(player2.getPlayerOrder() == i)
			return player2;
		else if(player3.getPlayerOrder() == i)
			return player3;
		else if(player4.getPlayerOrder() == i)
			return player4;
		else
			return null;
	}

	public void changeFortFaction(ControlledBy controlledBy, Fort f) {
		//remove fort from previous player
		Player player = playerFromFaction(f.getControlledBy());	
		player.removeFort(f);
		
		//add fort to new player
		Player newPlayer = playerFromFaction(controlledBy);
		newPlayer.addFort(f);
	}

	private Player playerFromFaction(ControlledBy controlledBy) {
		switch(controlledBy)
		{
		case PLAYER1:
			return player1;
		case PLAYER2:
			return player2;
		case PLAYER3:
			return player3;
		case PLAYER4:
			return player4;
		default:
			return null;
		}
	}

	public void clearConstructions() {
		for(HexTile[] h1: gameBoard.getTiles())
			for(HexTile h2: h1)
				if(h2 != null)
					h2.setConstructionAllowed(true);
	}

	public void removePlayerThings(HexTile h, ArrayList<Thing> things, int playerIndex) {
		h.removePlayerThings(things, playerIndex);
	}

	public void handleBribe(HexTile h, ArrayList<Thing> selectedThings, int playerIndex) {
		payForBribe(h, selectedThings, playerIndex);
		bribeSettlement(h, selectedThings);
		removePlayerThings(h, selectedThings, 4);	
		
		//eliminate bribed things
		for(Thing t: selectedThings)
		{
			handleElimination(t, h);
		}
		
		if(h.defendingThings.isEmpty())		//take over hex if all things bribed (including settlement)
			if((h.hasSettlement() && h.getSettlement().neutralized)
				|| !h.hasSettlement()){
					updateTileFaction(h);
					h.resetCounters();
			}
	}


	public void handleElimination(Thing t, HexTile hex) {
		if(hex != null){	//deal with thing coming from a hex tile
			
			//remove forts, settlements, special income from hex and player's lists
			if(t.thingType == ThingType.FORT){
				hex.removeFort();
				playerFromFaction(t.getControlledBy()).removeFort((Fort)t);
			}
			else if(t.thingType == ThingType.SETTLEMENT){
				hex.removeSettlement();
				
				if(!t.isControlledBy(ControlledBy.NEUTRAL))
					playerFromFaction(t.getControlledBy()).removeSettlement((Settlement)t);
			}
			else if(t.thingType == ThingType.SPECIAL_INCOME){
				hex.removeSpecialIncome();
				
				if(!t.isControlledBy(ControlledBy.NEUTRAL))
					playerFromFaction(t.getControlledBy()).removeSpecialIncome((SpecialIncome)t);
			}
			
			//remove thing from the tile's list of things
			if(!t.isControlledBy(ControlledBy.NEUTRAL))
				hex.removeThing(t.thingID, playerFromFaction(t.getControlledBy()).GetPlayerNum());
			else {
				hex.removeThing(t.thingID, 4);
			}
		}	
		
		if(t.isSpecialCharacter())
			GameClient.game.gameModel.returnSpecialCharacter((SpecialCharacter)t, true);
		else if(t.thingType != ThingType.FORT)
			GameClient.game.gameModel.returnToCup(t);
	}

	private void bribeSettlement(HexTile h, ArrayList<Thing> selectedThings) {
		h.bribeSettlement(selectedThings);
	}

	private void payForBribe(HexTile h, ArrayList<Thing> selectedThings, int playerIndex) {
		int cost = 0;
		
		cost = calculateBribe(selectedThings, h);
		
		playerFromIndex(playerIndex).payGold(cost);
	}

	public int calculateBribe(ArrayList<Thing> selectedThings, HexTile h) {
		int cost = 0;
		
		for(Thing t: selectedThings)
			cost += ((Combatant)t).GetCombatValue();
		
		if(h.isBribeDoubled())
			cost *= 2;
		
		return cost;
	}

	public ArrayList<HexTile> eliminateSeaHexThings() {
		HexTile[][] hexTiles = gameBoard.getTiles();
		ArrayList<HexTile> seaTiles = new ArrayList<HexTile>();
		
		for(HexTile[] tileArray: hexTiles)
			for(HexTile h: tileArray)
				if(h != null && !h.isLand()){				//for all existent sea hexes...
					for(int i=0; i<5; i++){				//...for each player...
						if(h.HasThingsOnTile(i)){		//...if indicated player has things...
							if(!seaTiles.contains(h))
								seaTiles.add(h);	
								
							ArrayList<Thing> thingsToRemove = new ArrayList<Thing>();
							for(Thing t: h.GetThings(i))
							{
								returnToCup(t);					//...return them to the cup...
								thingsToRemove.add(t);
							}
								
							//...and remove from the hex
							h.removePlayerThings(thingsToRemove, i);
						}
					}
				}
		
		//return all modified hexes to update view
		return seaTiles;
	}

	public ArrayList<HexTile> handleTileSwap(ArrayList<HexTile> hexTiles) {
		ArrayList<HexTile> ret = new ArrayList<HexTile>();
		
		for(HexTile h: hexTiles){
			unusedTiles.add(0, h);
			HexTile newTile = unusedTiles.remove(unusedTiles.size()-1);
			
			swapTile(h, newTile);
			
			ret.add(newTile);
		}
		
		return ret;
	}
	
	public void swapTile(HexTile oldTile, HexTile newTile)
	{
		//remove tile from board
		unusedTiles.add(0, oldTile);

		//eliminate all things on tile
		for(Thing t: oldTile.getAllThings())
			handleElimination(t, oldTile);
		
		//add new hex to board
		gameBoard.addHexTile(newTile, oldTile.x, oldTile.y);
		
		//if tile was owned, remove from player's list
		//and claim new tile instead
		if(oldTile.getControlledBy() != ControlledBy.NEUTRAL){
			Player player = playerFromFaction(oldTile.getControlledBy());
			player.removeHexTile(oldTile);
			claimNewTile(player, newTile.x, newTile.y);
		}		
	}

	public ArrayList<HexTile> getInitTilesToSwap() {
		ArrayList<HexTile> ret = new ArrayList<HexTile>();
		
		for(int i=0; i<4; i++){
			int x = 0;
			int y = 0;
				
			if(playerCount == 4){
				switch(i){
				case 0:
					x = 3;
					y = 1;
					break;
				case 1:
					x = 1;
					y = 3;
					break;
				case 2:
					x = -3;
					y = -1;
					break;
				case 3:
					x = -1;
					y = -3;
					break;
				}
			}
			else{
				switch(i){
				case 0:
					x = 2;
					y = 1;
					break;
				case 1:
					x = 1;
					y = 2;
					break;
				case 2:
					x = -2;
					y = -1;
					break;
				case 3:
					x = -1;
					y = -2;
					break;
				}
			}
				
			HexTile h = gameBoard.getTile(x, y);
			if(h.isControlledBy(ControlledBy.NEUTRAL))
				continue;
			
			if(!h.isLand())
				ret.add(h);
				
			ArrayList<HexTile> adjacentSeaHexes = new ArrayList<HexTile>();
			for(HexTile[] hexArray: gameBoard.getTiles())
				for(HexTile h2: hexArray)
					if(h2 != null){
						if( !ret.contains(h2)){
							if(h.isAdjacent(h2) && !h2.isLand())
								adjacentSeaHexes.add(h2);	
						}
					}	
				
			if(adjacentSeaHexes.size() >= 2)
				ret.addAll(adjacentSeaHexes);
		}
		
		return ret;
	}

	public void handlePostBattle(HexTile h, boolean fortHit,  boolean settlementHit , boolean specialIncomeHit) {
		switch(h.getControlledBy()){	//only update after a battle if other player remain
		case PLAYER1:
			if(!h.noOtherPlayerOnTile(0))
				updateTileFaction(h);
			break;
		case PLAYER2:
			if(!h.noOtherPlayerOnTile(1))
				updateTileFaction(h);
			break;
		case PLAYER3:
			if(!h.noOtherPlayerOnTile(2))
				updateTileFaction(h);
			break;
		case PLAYER4:
			if(!h.noOtherPlayerOnTile(3))
				updateTileFaction(h);
			break;
		default:
			updateTileFaction(h);
		}
		
		//deal with forts etc.
		h.handlePostBattle(fortHit, settlementHit, specialIncomeHit);
	}

	private void updateTileFaction(HexTile h) {
		ControlledBy oldFaction = h.getControlledBy();	
		ControlledBy faction = ControlledBy.NEUTRAL;
		
		//update faction to player with things on tile
		if(h.HasThingsOnTile(0))
			faction = player1.faction;
		else if(h.HasThingsOnTile(1))
			faction = player2.faction;
		else if(h.HasThingsOnTile(2))
			faction = player3.faction;
		else if(h.HasThingsOnTile(3))
			faction = player4.faction;
		else
			faction = ControlledBy.NEUTRAL;
		
		h.setControlledBy(faction);
		
		//if tile was owned by a player, remove from their controlled hexes
		if(oldFaction != ControlledBy.NEUTRAL)
		{
			if(oldFaction == ControlledBy.PLAYER1)
				player1.removeHexTile(h);
			else if(oldFaction == ControlledBy.PLAYER2)
				player2.removeHexTile(h);
			else if(oldFaction == ControlledBy.PLAYER3)
				player3.removeHexTile(h);
			else if(oldFaction == ControlledBy.PLAYER4)
				player4.removeHexTile(h);
		}
		
		//if a player now owns this hex, add to owned hexes
		if(faction != ControlledBy.NEUTRAL){
			playerFromFaction(faction).addHexTile(h);
		}
		
		if(oldFaction == ControlledBy.NEUTRAL			//take over explored hex (claim treasure, magic)
				&& faction != ControlledBy.NEUTRAL)
		{
			Player player = playerFromFaction(faction);
			
			for(Treasure t: h.getTreasures())
				player.addThingToRack(t);
			
			h.clearTreasure();
			
			for(Magic m: h.getMagics())
				player.addThingToRack(m);
			
			h.clearMagic();
		}
	}

	public HexTile claimNewTile(Player player, int x, int y) {
		HexTile h = gameBoard.getTile(x, y);
		
		player.addNewHexTile(h);
		h.setControlledBy(player.faction);
		
		return h;
	}

	public void incrementCitadelRounds() {
		player1.incrementCitadels();
		player2.incrementCitadels();
		player3.incrementCitadels();
		player4.incrementCitadels();
	}

	public ArrayList<String> checkForSpecialPowers(int playerIndex) {
		switch(playerIndex){
		case 0:
			return player1.performSpecialPowers();
		case 1:
			return player2.performSpecialPowers();
		case 2:
			return player3.performSpecialPowers();
		case 3:
			return player4.performSpecialPowers();
		}
		
		return new ArrayList<String>();
	}
	
	public HexTile handleElimination(int thingID){
		//it is not known what tile the things resides on
		//need to check all tiles until thing is found
		
		for(HexTile[] hexArray: gameBoard.getTiles())
			for(HexTile h: hexArray)
				if(h != null){
					Thing t = h.getThingFromTileByID(thingID);
					
					if(t != null){
						handleElimination(t,h);
						return h;
					}
				}
		
		return null;
	}

	public void stealGold(int thiefPlayerIndex, int victimPlayerIndex) {
		Player thief = playerFromIndex(thiefPlayerIndex);
		Player victim = playerFromIndex(victimPlayerIndex);
		
		thief.addGold(victim.getGold());
		victim.payGold(victim.getGold());
	}

	public void stealRecruit(int thiefPlayerIndex, int victimPlayerIndex) {
		Player thief = playerFromIndex(thiefPlayerIndex);
		Player victim = playerFromIndex(victimPlayerIndex);
		
		int victimRackSize = victim.getPlayerRack().size();
		
		//choose random thing
		int thingIndex = (int)Math.floor(Math.random()*victimRackSize);
		
		Thing thing = victim.removeFromRackByIndex(thingIndex);
		thief.addThingToRack(thing);
	}

	public Thing removeFromPlayerRack(int thingID, int playerIndex) {
		Player player = playerFromIndex(playerIndex);

		if(player.hasInRack(thingID))
			return player.removeFromRackByID(thingID);
		
		return null;
	}

	public String performRandomEvent(int playerIndex) {
		switch(playerIndex){
		case 0:
			return player1.performRandomEvent();
		case 1:
			return player2.performRandomEvent();
		case 2:
			return player3.performRandomEvent();
		case 3:
			return player4.performRandomEvent();
		}
		
		return "";
	}

	public ArrayList<HexTile> revealPinningEnemyCombatant(int playerIndex) {
		ArrayList<HexTile> tilesToUpdate = new ArrayList<HexTile>();
		
		//reveal a combatant for each player who has things
		//in a tile that player with playerindex does
		for(HexTile[] hexArray: gameBoard.getTiles())
			for(HexTile h: hexArray)
				if(h != null)
					if(h.HasThingsOnTile(playerIndex))
						for(int i=0; i<PlayerCount(); i++)
							if(i != playerIndex && h.HasThingsOnTile(i)){
								revealCombatant(h, i);
								tilesToUpdate.add(h);
							}
		
		return tilesToUpdate;
	}

	private void revealCombatant(HexTile h, int i) {
		//dont need to worry about fort's etc, as they won't be 
		//ever hidden
		ArrayList<Thing> enemyThings = h.GetThings(i);
		ArrayList<Thing> enemyBluffs = boardController.GetBluffs(h.x, h.y, i);
		
		for(Thing t: enemyThings)
			if(!enemyBluffs.contains(t)){
				t.setFlipped(false);
				return;
			}
	}

	public ArrayList<SpecialCharacter> getInPlaySpecialCharacters() {
		ArrayList<SpecialCharacter> ret = new ArrayList<SpecialCharacter>();
		
		//iterate over hextiles
		for(HexTile[] hexArray: gameBoard.getTiles())
			for(HexTile h: hexArray)
				if(h != null){
					
					//iterate over players
					for(int i=0; i<5; i++){
						
						//find any special characters
						for(Thing t: h.GetThings(i))
							if(t.isSpecialCharacter())
								ret.add((SpecialCharacter) t);
					}
				}
		
		return ret;
	}
}
