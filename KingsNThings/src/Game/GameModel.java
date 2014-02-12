package Game;

import gui.ThingView;
import gui.Tile;

import java.util.ArrayList;

import Game.GameConstants.ControlledBy;
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
		
		//initialize playing cup
		createNewThings();
		
		//initialize Special Characters (and store in unownedCharacters)
		createNewSpecialCharacters();
		
		ownedCharacters = new ArrayList<SpecialCharacter>(GameConstants.MAX_NUM_SPECIAL_CHARACTERS);
		
		dice = new Dice(4);
		
		boardController = new BoardController(gameBoard);
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
	private void createNewThings() {
		this.playingCup = new ArrayList<Thing>(GameConstants.MAX_NUM_THINGS);
		
		//REMOVED FOR ITERATION 1
		/*
		//placeholder until we get the time to determine proper values for all Things
		for(int i=0; i<10; i++)
		{
			playingCup.add(new Creature(Terrain.DESERT));
			playingCup.add(new Creature(Terrain.FOREST));
			playingCup.add(new Creature(Terrain.FROZEN_WASTE));
			playingCup.add(new Creature(Terrain.JUNGLE));
			playingCup.add(new Creature(Terrain.PLAINS));
			playingCup.add(new Creature(Terrain.SWAMP));
			playingCup.add(new Creature(Terrain.MOUNTAIN));
			playingCup.add(new Event());
			playingCup.add(new Settlement());
			playingCup.add(new Magic());
			playingCup.add(new Treasure());
		}
		
		for(int i=0; i<3; i++)
		{
			playingCup.add(new SpecialIncome(Terrain.DESERT));
			playingCup.add(new SpecialIncome(Terrain.FOREST));
			playingCup.add(new SpecialIncome(Terrain.FROZEN_WASTE));
			playingCup.add(new SpecialIncome(Terrain.JUNGLE));
			playingCup.add(new SpecialIncome(Terrain.PLAINS));
			playingCup.add(new SpecialIncome(Terrain.SWAMP));
			playingCup.add(new SpecialIncome(Terrain.MOUNTAIN));
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

	public static String initializeCreatures() {
		String initializeThingsString = "";
		
		//done twice so that we won't run out of creatures in demo 1
		for(int i=0; i<2; i++) {
			//THINGS RECRUITED
			initializeThingsString += "Goblins" + "SPLIT"
					+ "MOUNTAIN" + "SPLIT" 
					+ 1 + "SPLIT" 
					+ GameConstants.GoblinsImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Mountain Men" + "SPLIT"
					+ "MOUNTAIN" + "SPLIT" 
					+ 1 + "SPLIT" 
					+ GameConstants.MountainMenImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Cyclops" + "SPLIT"
					+ "MOUNTAIN" + "SPLIT" 
					+ 5 + "SPLIT" 
					+ GameConstants.CyclopsImageFront +"SPLIT";
			initializeThingsString += "/";
			
			//PLAYER 4 THINGS (reverse order)
			//Stack 3
			initializeThingsString += "Buffalo Herd" + "SPLIT"
					+ "PLAINS" + "SPLIT" 
					+ 3 + "SPLIT" 
					+ GameConstants.BuffaloHerdImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Giant Ape" + "SPLIT"
					+ "JUNGLE" + "SPLIT" 
					+ 5 + "SPLIT" 
					+ GameConstants.GiantApeImageFront +"SPLIT";
			initializeThingsString += "/";
			
			//Stack 2
			initializeThingsString += "Black Knight" + "SPLIT"
					+ "SWAMP" + "SPLIT" 
					+ 3 + "SPLIT" 
					+ GameConstants.BlackKnightImageFront +"SPLIT"
					+ "CHARGE";
			initializeThingsString += "/";
			initializeThingsString += "Dark Wizard" + "SPLIT"
					+ "SWAMP" + "SPLIT" 
					+ 1 + "SPLIT" 
					+ GameConstants.DarkWizardImageFront +"SPLIT"
					+ "MAGIC" + "SPLIT"
					+ "FLYING";
			initializeThingsString += "/";
			initializeThingsString += "Tribesman" + "SPLIT"
					+ "PLAINS" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.TribesmanImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Vampire Bat" + "SPLIT"
					+ "SWAMP" + "SPLIT" 
					+ 4 + "SPLIT" 
					+ GameConstants.VampireBatImageFront +"SPLIT"
					+ "FLYING";
			initializeThingsString += "/";
			
			//Stack 1
			initializeThingsString += "Tigers" + "SPLIT"
					+ "JUNGLE" + "SPLIT" 
					+ 3 + "SPLIT" 
					+ GameConstants.TigersImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Villains" + "SPLIT"
					+ "PLAINS" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.VillainsImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Giant Lizard" + "SPLIT"
					+ "SWAMP" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.GiantLizardImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Tribesman" + "SPLIT"
					+ "PLAINS" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.TribesmanImageFront +"SPLIT";
			initializeThingsString += "/";
			
			//PLAYER 3 THINGS (reverse order)
			//Stack 3
			initializeThingsString += "Witch Doctor" + "SPLIT"
					+ "JUNGLE" + "SPLIT" 
					+ 2 + "SPLIT"  
					+ GameConstants.WitchDoctorImageFront +"SPLIT"
					+ "MAGIC";
			initializeThingsString += "/";
			initializeThingsString += "Nomads" + "SPLIT"
					+ "DESERT" + "SPLIT" 
					+ 1 + "SPLIT" 
					+ GameConstants.NomadsImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Great Hunter" + "SPLIT"
					+ "PLAINS" + "SPLIT" 
					+ 4 + "SPLIT" 
					+ GameConstants.GreatHunterImageFront +"SPLIT"
					+ "RANGE";
			initializeThingsString += "/";
			
			//Stack 2
			initializeThingsString += "Pygmies" + "SPLIT"
					+ "JUNGLE" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.PygmiesImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Skeletons" + "SPLIT"
					+ "DESERT" + "SPLIT" 
					+ 1 + "SPLIT" 
					+ GameConstants.SkeletonsImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Genie" + "SPLIT"
					+ "DESERT" + "SPLIT" 
					+ 4 + "SPLIT" 
					+ GameConstants.GenieImageFront +"SPLIT"
					+ "MAGIC";
			initializeThingsString += "/";
			
			//Stack 1
			initializeThingsString += "Farmers" + "SPLIT"
					+ "PLAINS" + "SPLIT" 
					+ 1 + "SPLIT"  
					+ GameConstants.FarmersImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Farmers" + "SPLIT"
					+ "PLAINS" + "SPLIT" 
					+ 1 + "SPLIT" 
					+ GameConstants.FarmersImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Camel Corps" + "SPLIT"
					+ "DESERT" + "SPLIT" 
					+ 3 + "SPLIT"  
					+ GameConstants.CamelCorpsImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Centaur" + "SPLIT"
					+ "PLAINS" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.CentaurImageFront +"SPLIT";
			initializeThingsString += "/";
			
			//PLAYER 2 THINGS (reverse order)
			//Stack 1
			initializeThingsString += "Bandits" + "SPLIT"
					+ "FOREST" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.BanditsImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Crawling Vines" + "SPLIT"
					+ "JUNGLE" + "SPLIT" 
					+ 6 + "SPLIT" 
					+ GameConstants.CrawlingVinesImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Walking Tree" + "SPLIT"
					+ "FOREST" + "SPLIT" 
					+ 5 + "SPLIT"  
					+ GameConstants.WalkingTreeImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Druid" + "SPLIT"
					+ "FOREST" + "SPLIT" 
					+ 3 + "SPLIT" 
					+ GameConstants.DruidImageFront +"SPLIT"
					+ "MAGIC";
			initializeThingsString += "/";
			initializeThingsString += "Nomads" + "SPLIT"
					+ "DESERT" + "SPLIT" 
					+ 1 + "SPLIT" 
					+ GameConstants.NomadsImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Crocodiles" + "SPLIT"
					+ "JUNGLE" + "SPLIT" 
					+ 2 + "SPLIT"  
					+ GameConstants.CrocodilesImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Dervish" + "SPLIT"
					+ "DESERT" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.DervishImageFront +"SPLIT"
					+ "MAGIC";
			initializeThingsString += "/";
			initializeThingsString += "Green Knight" + "SPLIT"
					+ "FOREST" + "SPLIT" 
					+ 4 + "SPLIT"  
					+ GameConstants.GreenKnightImageFront +"SPLIT"
					+ "CHARGE";
			initializeThingsString += "/";
			initializeThingsString += "Sandworm" + "SPLIT"
					+ "DESERT" + "SPLIT" 
					+ 3 + "SPLIT" 
					+ GameConstants.SandwormImageFront +"SPLIT"
					+ "RANGE";
			initializeThingsString += "/";
			initializeThingsString += "Pterodactyl Warriors" + "SPLIT"
					+ "JUNGLE" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.PterodactylWarriorsImageFront +"SPLIT"
					+ "RANGE" + "SPLIT"
					+ "FLYING";
			initializeThingsString += "/";
			
			
			//PLAYER 1 THINGS (reverse order)
			//Stack 2
			initializeThingsString += "Ogre" + "SPLIT"
					+ "MOUNTAIN" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.OgreImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Goblins" + "SPLIT"
					+ "MOUNTAIN" + "SPLIT" 
					+ 1 + "SPLIT"  
					+ GameConstants.GoblinsImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Watusi" + "SPLIT"
					+ "JUNGLE" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.WatusiImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Skeletons" + "SPLIT"
					+ "DESERT" + "SPLIT" 
					+ 1 + "SPLIT" 
					+ GameConstants.SkeletonsImageFront + "SPLIT";
			initializeThingsString += "/";
			
			
			//Stack 1
			initializeThingsString += "Dwarves" + "SPLIT"
					+ "MOUNTAIN" + "SPLIT" 
					+ 2 + "SPLIT" 
					+ GameConstants.DwarvesImageFront +"SPLIT"
					+ "RANGE";
			initializeThingsString += "/";
			initializeThingsString += "Giant" + "SPLIT"
					+ "MOUNTAIN" + "SPLIT" 
					+ 4 + "SPLIT"  
					+ GameConstants.GiantImageFront +"SPLIT"
					+ "RANGE";
			initializeThingsString += "/";
			initializeThingsString += "Brown Knight" + "SPLIT"
					+ "MOUNTAIN" + "SPLIT" 
					+ 4 + "SPLIT" 
					+ GameConstants.BrownKnightImageFront +"SPLIT"
					+ "CHARGE";
			initializeThingsString += "/";
			initializeThingsString += "Elephant" + "SPLIT"
					+ "JUNGLE" + "SPLIT" 
					+ 4 + "SPLIT" 
					+ GameConstants.ElephantImageFront +"SPLIT"
					+ "CHARGE";
			initializeThingsString += "/";
			initializeThingsString += "Giant Spider" + "SPLIT"
					+ "DESERT" + "SPLIT" 
					+ 1 + "SPLIT" 
					+ GameConstants.GiantSpiderImageFront +"SPLIT";
			initializeThingsString += "/";
			initializeThingsString += "Old Dragon" + "SPLIT"
					+ "DESERT" + "SPLIT" 
					+ 4 + "SPLIT" 
					+ GameConstants.OldDragonImageFront +"SPLIT"
					+ "FLYING" + "SPLIT"
					+ "MAGIC";
			initializeThingsString += "/";
		}
		
		
		return initializeThingsString;
	}

	public void setPlayingCup(String[] creatureStrings) {
		for(int i=0; i< creatureStrings.length; i++)
		{
			String creatureString = creatureStrings[i];
			String[] creatureParamsString = creatureString.split("SPLIT");
			
			GameConstants.Terrain terrain = GameConstants.Terrain.valueOf(creatureParamsString[1]);
			String name = creatureParamsString[0];
			int attackValue = Integer.parseInt(creatureParamsString[2]);
			String frontFileName = creatureParamsString[3];
			boolean isCharge = false;
			boolean isFlying = false;
			boolean isMagic = false;
			boolean isRange = false;
			
			if(creatureParamsString.length > 4)
			{
				for(int j=4; j< creatureParamsString.length; j++)
				{
					String special = creatureParamsString[j];
					
					if (special.equals("CHARGE"))
						isCharge = true;
						
					if (special.equals("FLYING"))
						isFlying = true;
						
					if (special.equals("MAGIC"))
						isMagic = true;
						
					if (special.equals("RANGE"))
						isRange = true;
				}
			}

			
			playingCup.add(new Creature(terrain, name, attackValue, frontFileName)
								.Charge(isCharge)
								.Flying(isFlying)
								.Magic(isMagic)
								.Ranged(isRange)
								);
			}
		

		/*
		System.out.println("PLAYING CUP SIZE:" + playingCup.size());
		for(int i=0; i<playingCup.size(); i++)
		{
			System.out.println(playingCup.get((i)).toString());
		}
		*/
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
		
			playerGoldUpdates[i] = getIncomeForPlayer(player);
		}
		
		return playerGoldUpdates;
	}

	private int getIncomeForPlayer(Player player) {
		int gold =0;
		
		//gold pieces for land hexes
		for(HexTile h: player.ownedHexTiles)
				if(h.terrain != Terrain.SEA)
				{
						gold += h.getIncome();
						System.out.println("INCOME FROM HEX TILE: " + h.getIncome());
				}
		
		//combat values for forts
		for(Fort f: player.forts)
			gold+= f.getIncome();
		
		//special income tiles
		for(SpecialIncome si: player.specialIncomes)
			gold+= si.getIncome();
		
		//special characters
		for(SpecialCharacter sc: player.specialCharacters)
			gold += sc.getIncome();
		
		if(gold > 0)
		{
			System.out.println("GOLD HAS BEEN AWARDED TO PLAYER - " + player.GetPlayerNum() + "in the amount of " + gold);
			System.out.println("# Hexes: " + player.ownedHexTiles.size());
			System.out.println("# Forts: " + player.forts.size());
			System.out.println("# Special Incomes: " + player.specialIncomes.size());
			System.out.println("# Special Characters: " + player.specialCharacters.size());
		}
		
		player.addGold(gold);
		return gold;
	}

	public boolean isValidControlMarkerPlacement(HexTile selectedTile) {
		if(selectedTile.controlledBy == ControlledBy.NEUTRAL)
		{
			if(currPlayer.ownedHexTiles.isEmpty())
			{
				int x = selectedTile.x;
				int y = selectedTile.y;
				
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
	
		Fort f = new Fort();
		f.controlledBy = player.faction;
		
		HexTile h = gameBoard.getTile(x, y);
		
		h.addTower(f);
		player.addTower(f);
		
		return h;
	}

	public boolean playerRackTooFull() {
		return currPlayer.rackTooFull();
	}

	public int removeExcessFromRack() {
		return currPlayer.removeExcessFromRack();
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

	public boolean isValidMove(String attemptedMoveString) {
		// TODO Auto-generated method stub
		
		//check validity of passed movement string
		return true;
	}

	public void updateMovedThings(ArrayList<HexTile> tilesFrom,
			ArrayList<HexTile> tilesTo, ArrayList<Integer> thingIDs,
			int playerIndex) {
		Player player = playerFromIndex(playerIndex);
		
		int i=0;
		for(Integer id: thingIDs)
		{
			HexTile fromTile = tilesFrom.get(i);
			HexTile toTile = tilesTo.get(i);
			
			Thing thingPlayed = fromTile.getThingFromTileByID(id, playerIndex);
			
			fromTile.removeThing(id, playerIndex); 
			toTile.AddThingToTile(playerIndex, thingPlayed);
			
			if(!player.ownedHexTiles.contains(toTile))
			{
				updateTileFaction(playerIndex, toTile.x, toTile.y);
			}
		}
	}
}
