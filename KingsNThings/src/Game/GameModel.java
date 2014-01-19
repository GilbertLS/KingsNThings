package Game;

import java.util.Collections;
import java.util.Vector;

import Game.GameConstants.Terrain;

/*
 * This class models a King's N' Things Game. Its methods are called at appropriate times
 * by the Game class
 */
public class GameModel {
	//--------GAME OBJECTS-----------
	private GameBoard gameBoard;						//board holding the Hex Tiles in play
	private Vector<HexTile> unusedTiles;				//all unused HexTiles
	private Player player1, player2, player3, player4;	//Players in the game
	private Vector<Thing> playingCup;					//Container to hold unplayed Things
	private Vector<SpecialCharacter> unownedCharacters;	//Container to hold unplayed Special Characters
	private Vector<SpecialCharacter> ownedCharacters;	//Container to hold in-play Special Characters
	private Dice dice;									//Object to emulate up to 4 dice
	private int playerCount;
	
	//-----------INITIAL SETUP METHODS--------------------
	public GameModel(int playerCount)
	{
		this.gameBoard = new GameBoard();
		
		//initialize unusedTiles
		createHexTiles();
		
		//initialize Players
		for(int i=0; i< playerCount; i++)
		{
			switch(i){
				case 0:
					player1 = new Player(i);
				case 1:
					player2 = new Player(i);
				case 2:
					player3 = new Player(i);
				case 3:
					player4 = new Player(i);
				default:
					break;
				
			}
		}
		
		//initialize playing cup
		createNewThings();
		
		//initialize Special Characters (and store in unownedCharacters)
		createNewSpecialCharacters();
		
		ownedCharacters = new Vector<SpecialCharacter>(GameConstants.MAX_NUM_SPECIAL_CHARACTERS);
		
		dice = new Dice();
		
		this.playerCount = playerCount;
		
	}
	private void createNewSpecialCharacters() {
		this.unownedCharacters = new Vector<SpecialCharacter>(GameConstants.MAX_NUM_SPECIAL_CHARACTERS);
		
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
	}
	private void createNewThings() {
		this.playingCup = new Vector<Thing>(GameConstants.MAX_NUM_THINGS);
		
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
	}
	
	//create the 48 HexTiles to use for the Game
	private void createHexTiles() {
		unusedTiles = new Vector<HexTile>(GameConstants.MAX_NUM_TILES);
		
		for(int i=0; i < GameConstants.NUM_SEA_TILES; i++)
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
	}

	public void randomizePlayingCup() {
		Collections.shuffle(playingCup);
	}

	public void randomizeSpecialCharacters() {
		Collections.shuffle(unownedCharacters);
	}
	
	public void setUpHexTiles() {
		
		
	}
	//--------------/end INITIAL SETUP METHODS-----------
	
	
	
	//---------------PLAYER SETUP METHODS----------------
	public void shuffleRemainingTerrain() {
		// TODO Auto-generated method stub
		
	}
	//------------/end PLAYER SETUP METHODS
	
}
