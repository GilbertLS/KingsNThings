package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

import Game.Creature;
import Game.GameConstants;
import Game.GameConstants.ControlledBy;
import Game.GameConstants.CurrentPhase;
import Game.HexTile;
import Game.Player;
import Game.Utility;
import Game.Networking.GameClient;
import Game.Thing;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameView extends Scene {
	public static BattleView battleView = null;
    public static List<Thing> selectedThings = new ArrayList<Thing>();
	protected static SpecialCharacterView specialCharacterView = null;
	protected static BribeView bribeView = null;
	
	private GameView 		self = this;
	public 	HBox 			root;
    public 	VBox 			rightPanel;
    public 	VBox 			leftPanel;
    public 	BoardView 		board;
    public 	PlayerList 		playerList;
    public 	ButtonBox 		buttonBox;
    public 	RackView 		rack;
    public 	TilePreview 	tilePreview;
    public 	MessageView 	messageView;
    public 	BoardScrollPane scroll;
    public 	CurrentPhase 	currentPhase = CurrentPhase.NULL;
    public 	boolean 		userInputDone = false;
	String 					returnString = "";
	public 	int 			currPlayerNum;
	public 	Stage 			primaryStage;
	public 	Semaphore 		inputLock = new Semaphore(0);
	public 	Semaphore 		submitLock = new Semaphore(0);
	public 	Semaphore 		moveLock = new Semaphore(0);
	public 	Semaphore		playLock = new Semaphore(0);
	public 	Semaphore 		recruitLock = new Semaphore(0);
	public 	boolean 		characterRecruited = false;
	public 	EscapeMenu 		escapeMenu;
	//public boolean moveMade = false;
	
    public GameView(HBox r, Stage ps) {
    	super(r, 972, 614);
    	root = r; 
    	primaryStage = ps;
        
        rightPanel = new VBox();
        rightPanel.getStyleClass().add("right-panel");
        leftPanel = new VBox();
        root.getChildren().add(leftPanel);
        root.getChildren().add(rightPanel);
                
        tilePreview = new TilePreview(0);
        
        board = new BoardView(tilePreview, this);
        scroll = new BoardScrollPane(board);
        leftPanel.getChildren().add(scroll);
        
        VBox.setVgrow(scroll, Priority.ALWAYS);
        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        
        rightPanel.getChildren().add(new TextPanel("Player List"));
        playerList = new PlayerList(Arrays.asList(new PlayerPanel(1), new PlayerPanel(2), new PlayerPanel(3), new PlayerPanel(4)));
        rightPanel.getChildren().add(playerList);
        
        rightPanel.getChildren().add(new TextPanel("Game Message"));
        messageView = new MessageView();
        rightPanel.getChildren().add(messageView);
             
        rightPanel.getChildren().add(new TextPanel("Game Controls"));
        buttonBox = new ButtonBox();
        rightPanel.getChildren().add(buttonBox);
        
        rightPanel.getChildren().add(new TextPanel("Tile Preview"));
        rightPanel.getChildren().add(tilePreview);        
        
        ArrayList<ThingView> arr = new ArrayList<ThingView>();
        for(int i = 0; i < 10; i++)
        	arr.add(new ThingView(new Creature(GameConstants.Terrain.DESERT)));
        rack = new RackView(FXCollections.observableList((arr)));
        leftPanel.getChildren().add(rack);      
        
        this.getStylesheets().add("gui/main.css");
		BorderPane b = new BorderPane();
        
        EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.M) {
					if (escapeMenu == null) {
						escapeMenu = new EscapeMenu(self);
					}
					
					if(escapeMenu.isVisible())
						escapeMenu.hide();
					else
						escapeMenu.show();
				}
			}
        };
        
        this.setOnKeyPressed(keyPressed);
    }
    
    //method to give back a tile
    public Tile chooseTile()
    {  	
    	return this.board.getNextSelectedTile();
    }
    
    public Tile chooseTileFromEditState() {
    	return this.board.getNextSelectedTileFromEditState();
    }
    
    public void updateHexTile(HexTile h)
    {
    	Tile t = board.getTileByHex(h);
    	t.update();
    	t.updateThings();
    }
    
    public void displayMessage(String message)
    {
    	messageView.displayMessage(message);
    }
    
    public void clearMessage()
    {
    	messageView.clearMessage();
    }

	public int getNumPaidRecruits() {
		int num = -1;
		
		while(num < 0) {		
			String response = new InputDialog(this.primaryStage, "Recruiting", "How many paid recruits would you like?").showInput();
		
			try { num = Integer.parseInt(response); }
			catch(Exception e){}
		}
		
		return num;
	}
	
	public String performUserFeedback(String feedback){
		return performPhaseWithUserFeedback(CurrentPhase.USER_FEEDBACK, feedback);
	}
	
	public String performPhaseWithUserFeedback(CurrentPhase currentPhase, final String feedback){
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	displayMessage(feedback);
	        }
		});
		
		String ret = performPhase(currentPhase);
		
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	clearMessage();
	        }
		});
	        	
		return ret;
	}
	
	public String performPhase(CurrentPhase currentPhase){
		//pass and set phase, update controls accordingly, exit when "Done" is pressed
		this.currentPhase = currentPhase;
		inputLock = new Semaphore(0);
		
		//update button controls for specific phase
		buttonBox.updateButtons(currentPhase);
		
		//if recruiting character, do not allow end of phase until
		//character played
		if(currentPhase == CurrentPhase.RECRUIT_CHARACTER
				|| currentPhase == CurrentPhase.CHOOSE_DEFECTION_ACTION)
		{
			Utility.PromptForInput(recruitLock);
			characterRecruited = false;
		}
		
		Utility.PromptForInput(inputLock);
		
		String s = returnString;
		userInputDone = false;
		returnString = "";
		
		this.currentPhase = CurrentPhase.NULL;
		
		//reset button controlls
		buttonBox.updateButtons(CurrentPhase.NULL);
		
		return s;
	}
	
	public String moveIteration()
	{
		//perform a single iteration of movement
		//movement is done when userInputDone is set to true
		//after the beginning of the movement phase

		currentPhase = CurrentPhase.MOVEMENT;
		
		Utility.PromptForInput(moveLock);
		
		String s = userInputDone + " " + returnString;
		
		userInputDone = false;
		returnString = "";
		
		currentPhase = CurrentPhase.NULL;
		
		return s;	
	}
	
	public String playIteration()
	{
		//perform a single iteration of playing things

		currentPhase = CurrentPhase.PLAY_THINGS;
		
		Utility.PromptForInput(playLock);
		
		String s = userInputDone + " " + returnString;
		
		userInputDone = false;
		returnString = "";
		
		currentPhase = CurrentPhase.NULL;
		
		
		
		return s;	
	}

	public void updatePlayerRack(ArrayList<Thing> things) {
		rack.setAllThings(things);
	}

	public int getNumTradeRecruits() {
		return 0;
	}

	public void showHideAllTiles(final boolean show) {
		Platform.runLater(new Runnable(){
			public void run(){
				board.showHideAllTiles(show);
			}
		});
	}
	public void updateTiles(ArrayList<HexTile> hexTiles, int playerIndex) {
		for(HexTile h: hexTiles)
		{
			this.board.getTileByHex(h).updateThings(playerIndex);
			this.board.getTileByHex(h).update();
		}
	}
	
	public void updateTiles(ArrayList<HexTile> hexTiles) {
		for(HexTile h: hexTiles)
		{
			for(int i=0; i<GameClient.game.gameModel.PlayerCount(); i++)
				this.board.getTileByHex(h).updateThings(i);
			
			this.board.getTileByHex(h).update();
		}
	}
	
	
		
	public void setCurrentPlayer(int p) {
		this.currPlayerNum = p;
		this.tilePreview.setPlayerNum(p);
		this.primaryStage.setTitle(primaryStage.getTitle() + " - Player " + (p + 1) );
	}
	
	public Integer getCurrentPlayer() {
		return this.currPlayerNum;
	}
	
	public static boolean BattleOccuring(){
		if(GameView.battleView != null){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean recruitingSpecial(){
		if(GameView.specialCharacterView != null){
			return true;
		} else {
			return false;
		}
	}
	
	public void HideBattle() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				self.primaryStage.setScene(self);
			}
		});
	}
	
	public void ShowBattle() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				self.primaryStage.setScene(battleView);
			}
		});
	}
	
	public void StartBattle(final int tileX, final int tileY){	
		Platform.runLater(new Runnable() {
			public void run(){
				BorderPane p = new BorderPane();

				BattleView battleView = new BattleView(p, tileX, tileY);
				
				GameView.battleView = battleView;
				String style = "-fx-background-image: url(/res/images/ " + getBackgroundFromType(tileX, tileY) + ");";
				
				p.setStyle(style);
				
				self.primaryStage.setScene(battleView);

				
				/*Stage battleStage = new Stage();
				battleStage.setTitle("Combat Time!");
				battleStage.setScene(battleView);
                 
				//battleStage.setX(battleStage.getX() + 150);
				//battleStage.setY(battleStage.getY() + 150);
  
				battleStage.show();*/
			}
		});
	}
	
	 private String getBackgroundFromType(int tileX, int tileY) {
		 HexTile tile = GameClient.game.gameModel.gameBoard.getTile(tileX, tileY);
			 switch (tile.getTerrain()) {
			 	case SEA: return GameConstants.SeaTileFront;
				case JUNGLE: return GameConstants.JungleTileFront;
				case FROZEN_WASTE: return GameConstants.FrozenWasteTileFront;
				case FOREST: return GameConstants.ForestTileFront;
				case PLAINS: return GameConstants.PlainsTileFront;
				case SWAMP: return GameConstants.SwampTileFront;
				case MOUNTAIN: return GameConstants.MountainTileFront;
				case DESERT: return GameConstants.DesertTileFront;
			 }
	    	
	    return "Tuile_Back.png";
	}
	
	public void PreEndBattle() {
		if (GameView.BattleOccuring()) {
			HexTile hexTile = GameClient.game.gameModel.boardController.GetTile(
				GameView.battleView.tileX, 
				GameView.battleView.tileY
			);
				
			final Tile tileView = GameClient.game.gameView.board.getTileByHex(hexTile);
			Platform.runLater(new Runnable(){
				public void run(){
					tileView.updateThings();
					tileView.update();
				}
			});
		}
	}
	 
	public void EndBattle(){
		if (GameView.BattleOccuring()){
			self.primaryStage.setScene(self);
			GameView.battleView = null;
		}
	}

	public void updateGold(final int gold, final int playerIndex) {
		Platform.runLater(new Runnable(){
			public void run(){
				playerList.getPlayerPanel(playerIndex).setGold(gold);
			}
		});
	}

	public void updateTiles(HexTile hexTileCopy, int playerIndex) {
		ArrayList<HexTile> hexTileList = new ArrayList<HexTile>();
		
		hexTileList.add(hexTileCopy);
		
		updateTiles(hexTileList, playerIndex);
	}
	
	public void endRecruitSpecialCharacter(final boolean diceRolled)
	{
		if(currentPhase == CurrentPhase.RECRUIT_CHARACTER
				|| currentPhase == CurrentPhase.CHOOSE_DEFECTION_ACTION)
		{
			Platform.runLater(new Runnable(){
				public void run(){
					specialCharacterView = null;
						
					if(currentPhase != CurrentPhase.NULL)
					{
						Utility.GotInput(inputLock);
							
						userInputDone = true;
						
						if(diceRolled && !characterRecruited)
							Utility.GotInput(recruitLock);
						
						
					}
				}
			});
		}
	}

	public void updateRackCount(final int numThingsInRack, final int playerIndex) {
		Platform.runLater(new Runnable(){
			public void run(){
				playerList.getPlayerPanel(playerIndex).setThings(numThingsInRack);
			}
		});
	}

	public void addToRack(Thing thingRef) {
		rack.add(new ThingView(thingRef));
	}

	public void handleWin(final int winningIndex) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				displayMessage("PLAYER " + (winningIndex + 1) +" WINS!!!");
			}
		});
		
	}
	
	public void bribeCreatures(Tile t)
	{		
		BribeView BView = new BribeView(GameClient.game.gameView.primaryStage, t);
		GameView.bribeView = BView;
	}
	
	public void endBribeCreatures()
	{
		bribeView = null;
	}
	
	public void changeHex(HexTile h)
	{
		int x = h.x;
		int y = h.y; 
		
		Tile t = board.getTileByCoords(x, y);
		t.changeHex(h);
	}

	public void changeHexes(ArrayList<HexTile> hexes) {
		for(HexTile h: hexes)
			changeHex(h);
	}	
}