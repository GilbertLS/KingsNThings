package Game;

import Game.SpecialCharacter;

import java.util.ArrayList;

import Game.GameConstants.ControlledBy;
import Game.GameConstants.Level;
import Game.GameConstants.SettlementType;
import Game.GameConstants.Terrain;
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
	private LinkedList<HexTile> unusedTiles;				//all unused HexTiles
	private Player player1, player2, player3, player4;	//Players of the game
	private Player currPlayer;
	private ArrayList<Thing> playingCup;					//Container to hold unplayed Things
	private ArrayList<SpecialCharacter> unownedCharacters;	//Container to hold unplayed Special Characters
	private ArrayList<SpecialCharacter> ownedCharacters;	//Container to hold in-play Special Characters
	private Dice dice;									//Object to emulate up to 4 dice
	private int playerCount;
	public GameBoard gameBoard;
	private ArrayList<Thing> movedThings = new ArrayList<Thing>();
	
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
	
	public Player GetCurrentPlayer(){
		return currPlayer;
	}
	
	public int PlayerCount(){
		return playerCount;
	}
	
	//-----------INITIAL SETUP METHODS--------------------
	public GameModel()
	{
		gameBoard = new GameBoard();
		
		unusedTiles = new LinkedList<HexTile>();
		
		this.player1 = new Player(0);
		this.player2 = new Player(1);
		this.player3 = new Player(2);
		this.player4 = new Player(3);
		
		//initialize Special Characters (and store in unownedCharacters)
		createNewSpecialCharacters();
		
		ownedCharacters = new ArrayList<SpecialCharacter>(GameConstants.MAX_NUM_SPECIAL_CHARACTERS);
		playingCup = new ArrayList<Thing>(GameConstants.MAX_NUM_THINGS);
		
		dice = new Dice(4);
		
		boardController = new BoardController(gameBoard);
		
		initializePlayingCup();
	}
	private void createNewSpecialCharacters() {
		this.unownedCharacters = new ArrayList<SpecialCharacter>(GameConstants.MAX_NUM_SPECIAL_CHARACTERS);
		
		//REMOVED FOR ITERATION 1
		/*
		//placeholder until we get the time to determine proper values for all Special Characters
		for(int i=0; i< Math.floor(GameConstants.MAX_NUM_SPECIAL_CHARACTERS/2); i++)
		{
			unownedCharacters.add(new SpecialCharacter());		
		}
		
		for(int i=0; i<2; i++)
		{
			unownedCharacters.add(new TerrainLord(Terrain.DESERT));
			unownedCharacters.add(new TerrainLord(Terrain.FOREST));
			unownedCharacters.add(new TerrainLord(Terrain.FROZEN_WASTE));
			unownedCharacters.add(new TerrainLord(Terrain.JUNGLE));
			unownedCharacters.add(new TerrainLord(Terrain.MOUNTAIN));
			unownedCharacters.add(new TerrainLord(Terrain.PLAINS));
			unownedCharacters.add(new TerrainLord(Terrain.SWAMP));
		}
		*/
	}
	
	//create the 48 HexTiles to use for the Game
	public static String initializeHexTiles() {		
		String initializeHexTilesString = "";
		
		//HARD-CODING FOR FIRST ITERATION
		initializeHexTilesString += "DESERT" + "SPLIT" + 0 + "SPLIT" + 3;
		initializeHexTilesString += "/";
		initializeHexTilesString += "SWAMP" + "SPLIT" + 1 + "SPLIT" + 3;
		initializeHexTilesString += "/";
		initializeHexTilesString += "MOUNTAIN" + "SPLIT" + 2 + "SPLIT" + 3;
		initializeHexTilesString += "/";
		initializeHexTilesString += "JUNGLE" + "SPLIT" + 3 + "SPLIT" + 3;
		initializeHexTilesString += "/";
		
		initializeHexTilesString += "FROZEN_WASTE" + "SPLIT" + -1 + "SPLIT" + 2;
		initializeHexTilesString += "/";
		initializeHexTilesString += "JUNGLE" + "SPLIT" + 0 + "SPLIT" + 2;
		initializeHexTilesString += "/";
		initializeHexTilesString += "PLAINS" + "SPLIT" + 1 + "SPLIT" + 2;
		initializeHexTilesString += "/";
		initializeHexTilesString += "FROZEN_WASTE" + "SPLIT" + 2 + "SPLIT" + 2;
		initializeHexTilesString += "/";
		initializeHexTilesString += "SWAMP" + "SPLIT" + 3 + "SPLIT" + 2;
		initializeHexTilesString += "/";
		
		initializeHexTilesString += "FOREST" + "SPLIT" + -2 + "SPLIT" + 1;
		initializeHexTilesString += "/";
		initializeHexTilesString += "MOUNTAIN" + "SPLIT" + -1 + "SPLIT" + 1;
		initializeHexTilesString += "/";
		initializeHexTilesString += "SWAMP" + "SPLIT" + 0 + "SPLIT" + 1;
		initializeHexTilesString += "/";
		initializeHexTilesString += "FOREST" + "SPLIT" + 1 + "SPLIT" + 1;
		initializeHexTilesString += "/";
		initializeHexTilesString += "MOUNTAIN" + "SPLIT" + 2 + "SPLIT" + 1;
		initializeHexTilesString += "/";
		initializeHexTilesString += "DESERT" + "SPLIT" + 3 + "SPLIT" + 1;
		initializeHexTilesString += "/";
		
		initializeHexTilesString += "MOUNTAIN" + "SPLIT" + -3 + "SPLIT" + 0;
		initializeHexTilesString += "/";
		initializeHexTilesString += "PLAINS" + "SPLIT" + -2 + "SPLIT" + 0;
		initializeHexTilesString += "/";
		initializeHexTilesString += "FOREST" + "SPLIT" + -1 + "SPLIT" + 0;
		initializeHexTilesString += "/";
		initializeHexTilesString += "FROZEN_WASTE" + "SPLIT" + 0 + "SPLIT" + 0;
		initializeHexTilesString += "/";
		initializeHexTilesString += "JUNGLE" + "SPLIT" + 1 + "SPLIT" + 0;
		initializeHexTilesString += "/";
		initializeHexTilesString += "FROZEN_WASTE" + "SPLIT" + 2 + "SPLIT" + 0;
		initializeHexTilesString += "/";
		initializeHexTilesString += "FOREST" + "SPLIT" + 3 + "SPLIT" + 0;
		initializeHexTilesString += "/";
		
		initializeHexTilesString += "JUNGLE" + "SPLIT" + -3 + "SPLIT" + -1;
		initializeHexTilesString += "/";
		initializeHexTilesString += "DESERT" + "SPLIT" + -2 + "SPLIT" + -1;
		initializeHexTilesString += "/";
		initializeHexTilesString += "SEA" + "SPLIT" + -1 + "SPLIT" + -1;
		initializeHexTilesString += "/";
		initializeHexTilesString += "PLAINS" + "SPLIT" + 0 + "SPLIT" + -1;
		initializeHexTilesString += "/";
		initializeHexTilesString += "SWAMP" + "SPLIT" + 1 + "SPLIT" + -1;
		initializeHexTilesString += "/";
		initializeHexTilesString += "PLAINS" + "SPLIT" + 2 + "SPLIT" + -1;
		initializeHexTilesString += "/";
		
		initializeHexTilesString += "PLAINS" + "SPLIT" + -3 + "SPLIT" + -2;
		initializeHexTilesString += "/";
		initializeHexTilesString += "FOREST" + "SPLIT" + -2 + "SPLIT" + -2;
		initializeHexTilesString += "/";
		initializeHexTilesString += "SWAMP" + "SPLIT" + -1 + "SPLIT" + -2;
		initializeHexTilesString += "/";
		initializeHexTilesString += "DESERT" + "SPLIT" + 0 + "SPLIT" + -2;
		initializeHexTilesString += "/";
		initializeHexTilesString += "FOREST" + "SPLIT" + 1 + "SPLIT" + -2;
		initializeHexTilesString += "/";
		
		initializeHexTilesString += "DESERT" + "SPLIT" + -3 + "SPLIT" + -3;
		initializeHexTilesString += "/";
		initializeHexTilesString += "MOUNTAIN" + "SPLIT" + -2 + "SPLIT" + -3;
		initializeHexTilesString += "/";
		initializeHexTilesString += "JUNGLE" + "SPLIT" + -1 + "SPLIT" + -3;
		initializeHexTilesString += "/";
		initializeHexTilesString += "FROZEN_WASTE" + "SPLIT" + -0 + "SPLIT" + -3;
		initializeHexTilesString += "/";
		
		initializeHexTilesString += " ";
		
		//INITIALIZE UNUSED TILES
		initializeHexTilesString += "FROZEN_WASTE";
		initializeHexTilesString += "/";

		
		return initializeHexTilesString;
		
		//COMMENTED OUT FOR FIRST ITERATION
		/*//add all but 4 SEA HexTiles
		for(int i=0; i < GameConstants.NUM_SEA_TILES-4; i++)
		{
			unusedTiles.add(new HexTile(Terrain.SEA));
		}
		
		for(int i=0; i < GameConstants.NUM_JUNGLE_TILES; i++)
		{
			unusedTiles.add(new HexTile(Terrain.JUNGLE));
		}
		
		for(int i=0; i < GameConstants.NUM_FROZEN_WASTE_TILES; i++)
		{
			unusedTiles.add(new HexTile(Terrain.FROZEN_WASTE));
		}
		
		for(int i=0; i < GameConstants.NUM_FOREST_TILES; i++)
		{
			unusedTiles.add(new HexTile(Terrain.FOREST));
		}
		
		for(int i=0; i < GameConstants.NUM_PLAINS_TILES; i++)
		{
			unusedTiles.add(new HexTile(Terrain.PLAINS));
		}
		
		for(int i=0; i < GameConstants.NUM_SWAMP_TILES; i++)
		{
			unusedTiles.add(new HexTile(Terrain.SWAMP));
		}
		
		for(int i=0; i < GameConstants.NUM_MOUNTAIN_TILES; i++)
		{
			unusedTiles.add(new HexTile(Terrain.MOUNTAIN));
		}
		
		for(int i=0; i < GameConstants.NUM_DESERT_TILES; i++)
		{
			unusedTiles.add(new HexTile(Terrain.DESERT));
		}
		*/
		
		//COMMENTED OUT FOR FIRST ITERATION
		/*
		int x=0, y=0;	//current tile coordinates
		
		//add center tile
		gameBoard.addHexTile(unusedTiles.pop(), x, y);
		
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
			gameBoard.addHexTile(unusedTiles.pop(), x, y);
			
			//place top-right tiles
			for(int j=0; j<i; j++)
			{				
				//move in the negative y direction
				y--;
				
				gameBoard.addHexTile(unusedTiles.pop(), x, y);
			}
						
			//place right tiles
			for(int j=0; j<i; j++)
			{
				//move in the "downward" direction
				x--;
				y--;
				
				gameBoard.addHexTile(unusedTiles.pop(), x, y);
			}
			
			//place bottom-right tiles
			for(int j=0; j<i; j++)
			{				
				//move in the negative x direction
				x--;
				
				gameBoard.addHexTile(unusedTiles.pop(), x, y);
			}	
			
			//place bottom-left tiles
			for(int j=0; j<i; j++)
			{				
				//move in the positive y direction
				y++;
				
				gameBoard.addHexTile(unusedTiles.pop(), x, y);
			}
			
			//place left tiles
			for(int j=0; j<i; j++)
			{				
				//move in the "upward" direction
				x++;
				y++;
				
				gameBoard.addHexTile(unusedTiles.pop(), x, y);
			}
			
			//place top-left tiles
			for(int j=0; j<i; j++)
			{				
				//move in the positive x direction
				x++;
				
				gameBoard.addHexTile(unusedTiles.pop(), x, y);
			}
		}
		
		//add in remaining 4 SEA HexTles and shuffle again
		for(int i=0; i<4; i++)
		{
			unusedTiles.add(new HexTile(Terrain.SEA));
		}
		shuffleUnusedTiles();
		*/
	}

	public void randomizePlayingCup() {
		Collections.shuffle(playingCup);
	}

	public void randomizeSpecialCharacters() {
		Collections.shuffle(unownedCharacters);
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
	//--------------/end INITIAL SETUP METHODS-----------
	
	public void shuffleUnusedTiles() {
		Collections.shuffle(unusedTiles);
	}
	public int rollDice() {
		return dice.rollDice();
	}
	
	public int[] rollDice(int numDice){
		return dice.rollDice(numDice);
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

	public HexTile[][] setInitialHexTiles(String[] hexTileStrings) {
		for(int i=0; i< hexTileStrings.length; i++)
		{
			String hexTileString = hexTileStrings[i];
			String[] hexTileParamStrings = hexTileString.split("SPLIT");
			
			GameConstants.Terrain terrain = GameConstants.Terrain.valueOf(hexTileParamStrings[0]);
			int x = Integer.parseInt(hexTileParamStrings[1]);
			int y = Integer.parseInt(hexTileParamStrings[2]);
			
			gameBoard.addHexTile(new HexTile(terrain), x, y);
		}
		
		return gameBoard.getTiles();
		
	}

	public void setInitialUnusedHexTiles(String[] unusedHexTileStrings) {
		for(int i=0; i< unusedHexTileStrings.length; i++)
		{
			String hexTileString = unusedHexTileStrings[i];
			String[] hexTileParamStrings = hexTileString.split("SPLIT");
			
			GameConstants.Terrain terrain = GameConstants.Terrain.valueOf(hexTileParamStrings[0]);
			
			unusedTiles.add(new HexTile(terrain));
		}
		
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
			playingCup.add(new Creature(Terrain.SWAMP, "Ghost", 3, GameConstants.GhostImageFront)
			.Flying(true));
		}
	}

	public void initializePlayingCup() {
		initializeCreatures();
		initializeSpecialIncomes();
		initializeTreasure();
		initializeMagic();
	}
	
	public void initializeSpecialCharacters(String s)
	{
		String[] initValues = s.trim().split(" ");
		
		if(Integer.parseInt(initValues[0]) == 0)
			unownedCharacters.add(new SpecialCharacter("Arch Cleric", 5, GameConstants.ArchClericImageFront));
		else
			unownedCharacters.add(new SpecialCharacter("Arch Mage", 6, GameConstants.ArchMageImageFront));
		
		if(Integer.parseInt(initValues[1]) == 0)
			unownedCharacters.add(new SpecialCharacter("Assassin Primus", 4, GameConstants.AssassinPrimusImageFront));
		else
			unownedCharacters.add(new SpecialCharacter("Baron Munchausen", 4, GameConstants.BaronMunchausenImageFront));
		
		if(Integer.parseInt(initValues[2]) == 0)
			unownedCharacters.add(new SpecialCharacter("Deerhunter", 4, GameConstants.DeerhunterImageFront));
		else
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
		
		if(Integer.parseInt(initValues[7]) == 0)
			unownedCharacters.add((SpecialCharacter)new SpecialCharacter("Marksman", 5, GameConstants.MarksmanImageFront)
			.Ranged(true));
		else
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

	public void getThingsFromCup(int playerIndex, int numThings) {
		Player player = playerFromIndex(playerIndex);
				
		for(int i=0; i<numThings; i++)
		{
			Thing currentThing = playingCup.remove(playingCup.size()-1);
			
			player.addThingToRack(currentThing);
			currentThing.controlledBy = player.faction;
		}
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
		}
		
		return playerGoldUpdates;
	}

	public boolean isValidControlMarkerPlacement(HexTile selectedTile) {
		if(selectedTile.controlledBy == ControlledBy.NEUTRAL)
		{
			if(currPlayer.ownedHexTiles.isEmpty())
			{
				int x = selectedTile.x;
				int y = selectedTile.y;
				
				//need to modify for 2/3 players
				if((x == 3 && y == 1)
						||(x == 1 && y == 3)
						||(x == -3 && y == -1)
						||(x == -1 && y == -3))
					return true;
			}
			else
			{
				for(int i=0; i<playerCount; i++)
				{
					if(i != currPlayer.GetPlayerNum())
						for(HexTile h: playerFromIndex(i).ownedHexTiles)
							if(h.isAdjacent(selectedTile))
								return false;
				}
				
				for(HexTile h: currPlayer.ownedHexTiles)
				{
					if(selectedTile.isAdjacent(h))
						return true;
				}
			}
		}
		return false;
	}

	public HexTile updateTileFaction(int playerIndex, int x, int y) {
		HexTile h = gameBoard.getTile(x, y);
		ControlledBy oldFaction = h.controlledBy;	
		
		Player player = playerFromIndex(playerIndex);
		ControlledBy faction = player.faction;
		
		h.controlledBy = faction;
		
		player.addHexTile(h);
		
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
		
		return h;
	}

	public boolean isValidTowerPlacement(HexTile selectedTile) {
		return (selectedTile.controlledBy == currPlayer.faction && selectedTile.fort == null);
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
		f.controlledBy = player.faction;
		
		h.addTower(f);
		player.addTower(f);
		
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
		default:
			return player4;
		}
	}

	public void updatePlayerRack(ArrayList<Integer> thingIDs, int playerIndex) {
		Player player = playerFromIndex(playerIndex);

		for(Integer i: thingIDs)
			if(player.hasInRack(i))
				player.removeFromRack(i);
	}

	public void updatePlayedThings(ArrayList<HexTile> hexTiles, ArrayList<Integer> thingIDs, int playerIndex) {		
		Player player = playerFromIndex(playerIndex);
		
		int i=0;
		for(HexTile h: hexTiles)
		{
			int thingID = thingIDs.get(i++);
			
			Thing thingPlayed = player.getThingByID(thingID);
				
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
			Thing thingPlayed = tileFrom.getThingFromTileByID(id, playerIndex);
			
			tileFrom.removeThing(id, playerIndex); 
			tileTo.AddThingToTile(playerIndex, thingPlayed);
			
			//handle movement speed
			thingPlayed.numMoves += tileTo.moveValue;
			
			movedThings.add(thingPlayed);
			
			if(!player.ownedHexTiles.contains(tileTo) && tileTo.controlledBy == ControlledBy.NEUTRAL)
			{
				updateTileFaction(playerIndex, tileTo.x, tileTo.y);
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
		if(hexTile.hasFort())
			hexTile.getFort().upgrade();
		else
			addTower(hexTile, playerIndex);
		
		playerFromIndex(playerIndex).decrementGold(GameConstants.CONSTRUCTION_COST);
	}

	public boolean isValidConstruction(HexTile h, int playerIndex) {
		boolean valid = true;
		Player player = playerFromIndex(playerIndex);
		
		//invalid if not controlled
		if(h.controlledBy != player.faction)
			valid = false;
		
		//or if cant afford
		if(player.getGold() < 5)
			valid = false;
		
		//or has fort and...
		if(h.hasFort())
		{
			//is trying to upgrade to citadel but doesn't have the income
			if(h.getFort().getLevel() == Level.CASTLE)
			{
				if(player.getIncome() < 20)
					valid = false;
			}
			//cannot upgrade fort further
			else if(h.getFort().getLevel() == Level.CITADEL)
				valid = false;
		}
		
		return valid;
	}

	public ArrayList<SpecialCharacter> getUnownedSpecialCharacters() {
		return unownedCharacters;
	}

	public boolean canAfford(int amount,
			int playerIndex) {
		return playerFromIndex(playerIndex).canAfford(amount);
	}

	public void augmentRoll(int cost,
			int playerIndex) {
		playerFromIndex(playerIndex).decrementGold(cost);
	}

	public void recruitSpecialCharacter(int thingID, int playerIndex) {
		Thing t = null;
		
		for(SpecialCharacter sc: unownedCharacters)
		{
			if(sc.thingID == thingID)
				t = sc;
		}
		
		if(t != null)
			playerFromIndex(playerIndex).addThingToRack(t);
	}

	public void handleRackOverload(int playerIndex) {
		Player player = playerFromIndex(playerIndex);
		
		player.playerRack.handleRackOverload();
	}

	public boolean playerRackTooFull(int playerIndex) {	
		return playerFromIndex(playerIndex).rackTooFull();
	}
}
