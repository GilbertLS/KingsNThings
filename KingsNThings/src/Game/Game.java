package Game;

/*
 * This class is the controller for a Kings n' Things Game. It calls methods from the Game Model,
 * and then calls the GameView to update accordingly
 */
public class Game {
	private GameModel gameModel; 	//conceptual model of a Kings N' things Game
	private GameView gameView;		//View to display the game
	
	public Game(int playerCount)
	{
		this.gameModel = new GameModel(playerCount);
		this.gameView = new GameView();
	}

	public int playGame() {
		//all required initial Game setup
		initialSetup();
		
		//all required player setup
		playerSetup();
		
		//play the Game until it is won
		boolean gameOver = false;
		do
		{
			gameOver = playGameTurn();
		}while (!gameOver);
		
		//game finished successfully
		return 0;
	}

	private boolean playGameTurn() {
		
		//play each phase of the game individually
		
		collectGoldPhase();
		
		recruitCharactersPhase();
		
		recruitThingsPhase();
		
		playEventsPhase();
		
		movementPhase();
		
		if (combatPhase())			//game may be won here
			return true;
		
		if (checkVictoryConditionPhase())	//game may be won here
			return true;
		
		specialPowersPhase();
		
		changePlayerOrderPhase();
		
		return false;
	}
	
	//---------------GAME TURN PHASES----------------
	public void collectGoldPhase() {
		// TODO code to handle the gold collection phase
		
	}

	public void recruitCharactersPhase() {
		// TODO code to handle the recruit character phase
		
	}

	public void recruitThingsPhase() {
		// TODO code to handle the recruit things phase
		
	}

	public void playEventsPhase() {
		// TODO code to handle the play events phase
		
	}

	public void movementPhase() {
		// TODO code to handle the movement phase
		
	}

	//returns true if the game was won as a result of this battle
	public boolean combatPhase() {
		// TODO code to handle the combat phase
		
		return false;
		
	}

	//returns true if the game was found to be won
	public boolean checkVictoryConditionPhase() {
		// TODO code to handle the check victory condition phase
		
		return false;
		
	}

	public void specialPowersPhase() {
		// TODO code to handle the special powers phase
		
	}

	public void changePlayerOrderPhase() {
		// TODO code to handle the change player order phase
		
	}
	//-----------------/end GAME TURN PHASES---------------

	private void initialSetup() {
		//perform initial setup of the game via the model
		//call the view to update when necessary		
		gameModel.randomizePlayingCup();
		
		gameModel.randomizeSpecialCharacters();
		
		gameModel.setUpHexTiles();
		
		gameView.drawInitGame();
		
		determineInitialPlayerOrder();
	}
	
	//--------------------INITIAL SETUP PHASES------------
	private void determineInitialPlayerOrder() {
		// TODO Auto-generated method stub
		
	}
	//-----------------------/end INITIAL SETUP PHASES

	private void playerSetup() {
		//perform the intial player setup via the model,
		//call the view to update when necessary
		chooseStartingPositions();
		
		revealHexTiles();
		
		chooseInitialKingdoms();
		
		recieveResources();
		
		placeInitialTowers();
		
		getInitialThings();
		
		playThings();
		
		exchangeThings();
		
		gameModel.shuffleRemainingTerrain();
	}

	//--------------PLAYER SETUP PHASES
	private void exchangeThings() {
		// TODO Auto-generated method stub
		
	}

	private void playThings() {
		// TODO Auto-generated method stub
		
	}

	private void getInitialThings() {
		// TODO Auto-generated method stub
		
	}

	private void placeInitialTowers() {
		// TODO Auto-generated method stub
		
	}

	private void recieveResources() {
		// TODO Auto-generated method stub
		
	}

	private void chooseInitialKingdoms() {
		// TODO Auto-generated method stub
		
	}

	private void revealHexTiles() {
		// TODO Auto-generated method stub
		
	}

	private void chooseStartingPositions() {
		// TODO Auto-generated method stub
		
	}
	//----------------/end PLAYER SETUP PHASES
	
}
