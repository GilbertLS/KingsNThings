package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import Game.Creature;
import Game.GameClientController;
import Game.GameConstants;
import Game.Player;
import Game.GameConstants.ControlledBy;
import Game.GameConstants.CurrentPhase;
import Game.HexTile;
import Game.GameConstants.Terrain;
import Game.Networking.GameClient;
import Game.Thing;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameView extends Scene {
	public static BattleView battleView = null;
    public static List<Integer> selectedThings = new ArrayList<Integer>();
	
    public BorderPane root;
    public VBox rightPanel;
    public HBox bottomPanel;
    public BoardView board;
    public PlayerList playerList;
    public ButtonBox buttonBox;
    public RackView rack;
    public TilePreview tilePreview;
    public DiceListView diceListView;
    public MessageView messageView;
    public InputView inputView;
    public CurrentPhase currentPhase = CurrentPhase.NULL;
    public boolean userInputDone = false;
	protected boolean inputTextUpdated = false;
	String returnString = "";
	private int currPlayerNum;
	public Stage primaryStage;
	
    public GameView(BorderPane r, Stage ps) {
    	super(r, 1000, 600);
    	root = r; 
    	primaryStage = ps;
        
        rightPanel = new VBox();
        root.setRight(rightPanel);
        
        bottomPanel = new HBox();
        root.setBottom(bottomPanel);
        
        tilePreview = new TilePreview(0);
        
        board = new BoardView(tilePreview);
        root.setCenter(board);        
        
        playerList = new PlayerList(Arrays.asList(new PlayerPanel(1), new PlayerPanel(2), new PlayerPanel(3), new PlayerPanel(4)));
        rightPanel.getChildren().add(playerList);
        
        messageView = new MessageView();
        rightPanel.getChildren().add(messageView);
        
        inputView = new InputView();
        rightPanel.getChildren().add(inputView);
        
        buttonBox = new ButtonBox();
        rightPanel.getChildren().add(buttonBox);
        
        rightPanel.getChildren().add(tilePreview);
        
        diceListView = new DiceListView();
        rightPanel.getChildren().add(diceListView);
        
        
        ArrayList<ThingView> arr = new ArrayList<ThingView>();
        for(int i = 0; i < 10; i++)
        	arr.add(new ThingView(new Creature(GameConstants.Terrain.DESERT)));
        rack = new RackView(FXCollections.observableList((arr)));
        bottomPanel.getChildren().add(rack);
        
        this.getStylesheets().add("gui/myStyle.css");
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
		inputTextUpdated = false;
		try {
			do{
				Thread.sleep(500);
			}while(!inputTextUpdated);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Integer.parseInt(inputView.getInput());
	}
	
	public String performPhase(CurrentPhase currentPhase){
		//pass and set phase, update controls accordingly, exit when "Done" is pressed
		this.currentPhase = currentPhase;
		
		returnString = "";
		
		userInputDone = false;
		try {
			do{
				Thread.sleep(500);
			}while(!userInputDone);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.currentPhase = CurrentPhase.NULL;
		
		return returnString;
	}

	public void updatePlayerRack() {
		//re-draw the player rack for current player (because it has changed)
		//if necessary, I can pass the appropriate rack
	}

	public int getNumTradeRecruits() {
		return 0;
	}

	public void showHideAllTiles(boolean show) {
		board.showHideAllTiles(show);
	}
	public void updateTiles(ArrayList<HexTile> hexTiles, int playerIndex) {
		for(HexTile h: hexTiles)
		{
			this.board.getTileByHex(h).updateThings(playerIndex);
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
			 	case SEA: return "Tuile-Mer.png";
				case JUNGLE: return "Tuile-Jungle.png";
				case FROZEN_WASTE: return "Tuile-Entendue-Glacée.png";
				case FOREST: return "Tuile-Forêt.png";
				case PLAINS: return "Tuile-Plaines.png";
				case SWAMP: return "Tuile-Marais.png";
				case MOUNTAIN: return "Tuile-Montagne.png";
				case DESERT: return "Tuile-Desert.png";
			 }
		 }
	    	
	    return "Tuile_Back.png";
	}
	
	public void EndBattle(){
		if (GameView.BattleOccuring()){
			
			HexTile hexTile = GameClient.game.gameModel.boardController.GetTile(
				GameView.battleView.tileX, 
				GameView.battleView.tileY
			);
			
			Tile tileView = GameClient.game.gameView.board.getTileByHex(hexTile);
			
			tileView.updateThings();
			tileView.update();
			
			Platform.runLater(new Runnable(){
				public void run(){
					GameView.battleView.battleStage.close();
					GameView.battleView = null;
				}
			});
		}
	}
	
}