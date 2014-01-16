package Game;

import java.util.Vector;

/*
 * This class models a King's N' Things Game. Its methods are called at appropriate times
 * by the Game class, and then the Game View is called to update accordingly
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
	
	//-----------INITIAL SETUP METHODS--------------------
	public void randomizePlayingCup() {
		// TODO Auto-generated method stub
		
	}
	public void randomizeSpecialCharacters() {
		// TODO Auto-generated method stub
		
	}
	public void setPlayerCount() {
		// TODO Auto-generated method stub
		
	}
	public void setUpHexTiles() {
		// TODO Auto-generated method stub
		
	}
	//--------------/end INITIAL SETUP METHODS-----------
	
	
	
	//---------------PLAYER SETUP METHODS----------------
	public void shuffleRemainingTerrain() {
		// TODO Auto-generated method stub
		
	}
	//------------/end PLAYER SETUP METHODS
	
}
