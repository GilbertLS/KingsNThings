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
import Game.Utility;
import Game.Networking.GameClient;
import Game.Thing;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameView extends Scene {
	public static BattleView battleView = null;
    public static List<Integer> selectedThings = new ArrayList<Integer>();
	protected static SpecialCharacterView specialCharacterView = null;
	
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
    public 	ScrollPane 		scroll;
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
	//public boolean moveMade = false;
	
    public GameView(HBox r, Stage ps) {
    	super(r, 972, 614);
    	root = r; 
    	primaryStage = ps;
        
        rightPanel = new VBox();
        leftPanel = new VBox();
        root.getChildren().add(leftPanel);
        root.getChildren().add(rightPanel);
                
        tilePreview = new TilePreview(0);
        
        scroll = new ScrollPane();
        board = new BoardView(tilePreview);
        leftPanel.getChildren().add(scroll);
        scroll.setContent(board);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        
        playerList = new PlayerList(Arrays.asList(new PlayerPanel(1), new PlayerPanel(2), new PlayerPanel(3), new PlayerPanel(4)));
        rightPanel.getChildren().add(playerList);
        
        messageView = new MessageView();
        rightPanel.getChildren().add(messageView);
                
        buttonBox = new ButtonBox();
        rightPanel.getChildren().add(buttonBox);
        
        rightPanel.getChildren().add(tilePreview);        
        
        ArrayList<ThingView> arr = new ArrayList<ThingView>();
        for(int i = 0; i < 10; i++)
        	arr.add(new ThingView(new Creature(GameConstants.Terrain.DESERT)));
        rack = new RackView(FXCollections.observableList((arr)));
        leftPanel.getChildren().add(rack);
        
        
        Button zoomin = new Button("zoom in");
        zoomin.setOnMouseClicked(new EventHandler<MouseEvent>(){
       	 
            @Override
            public void handle(MouseEvent e) {
            	self.board.zoomIn();
            }
        });
        rightPanel.getChildren().add(zoomin);
        Button zoomout = new Button("zoom out");
        zoomout.setOnMouseClicked(new EventHandler<MouseEvent>(){
       	 
            @Override
            public void handle(MouseEvent e) {
            	self.board.zoomOut();
            }
        });
        rightPanel.getChildren().add(zoomout);
        
        this.getStylesheets().add("gui/main.css");
    }
    
    //method to give back a tile
    public Tile chooseTile()
    {  	
    	return this.board.getNextSelectedTile();
    }
    
    public void updateHexTile(HexTile h)
    {
    	this.board.getTileByHex(h).update();
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
	
	public String performPhase(CurrentPhase currentPhase){
		//pass and set phase, update controls accordingly, exit when "Done" is pressed
		this.currentPhase = currentPhase;
		inputLock = new Semaphore(0);
		
		//update button controls for specific phase
		buttonBox.updateButtons(currentPhase);
		
		//if recruiting character, do not allow end of phase until
		//character played
		if(currentPhase == CurrentPhase.RECRUIT_CHARACTER)
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
		
		GameClient.game.handleRackOverload(getCurrentPlayer());
		
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
	
	public void StartBattle(final int tileX, final int tileY){	
		Platform.runLater(new Runnable() {
			public void run(){
				BorderPane p = new BorderPane();

				BattleView battleView = new BattleView(p, tileX, tileY);
				
				GameView.battleView = battleView;
				String style = "-fx-background-image: url(/res/images/ " + getBackgroundFromType(tileX, tileY) + ");";
				
				p.setStyle(style);

				
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
		 if(tile.controlledBy == ControlledBy.NEUTRAL) {
			return "Tuile_Back.png";
		 } else {
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
		 }
	    	
	    return "Tuile_Back.png";
	}
	
	public void EndBattle(){
		if (GameView.BattleOccuring()){
			for(int i = 0; i < 3; i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
			
			HexTile hexTile = GameClient.game.gameModel.boardController.GetTile(
				GameView.battleView.tileX, 
				GameView.battleView.tileY
			);
			
			final Tile tileView = GameClient.game.gameView.board.getTileByHex(hexTile);
			
			Platform.runLater(new Runnable(){
				public void run(){
					tileView.updateThings();
					tileView.update();
					GameView.battleView.battleStage.close();
					GameView.battleView = null;
				}
			});
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
	
	public void endRecruitSpecialCharacter()
	{
		if(currentPhase == CurrentPhase.RECRUIT_CHARACTER)
		{
			Platform.runLater(new Runnable(){
				public void run(){
					specialCharacterView = null;
						
					if(currentPhase != CurrentPhase.NULL)
					{
						Utility.GotInput(inputLock);
							
						userInputDone = true;
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

	public void handleWin(final ArrayList<Integer> winingPlayers) {
		Platform.runLater(new Runnable(){
			public void run(){
				//output the winning players here
			}
		});
	}
	
}