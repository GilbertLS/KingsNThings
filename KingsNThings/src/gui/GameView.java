package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import Game.Creature;
import Game.GameConstants;
import Game.GameConstants.ControlledBy;
import Game.GameConstants.CurrentPhase;
import Game.HexTile;
import Game.GameConstants.Terrain;
import Game.Thing;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class GameView extends Scene {
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
	
    public GameView(BorderPane r) {
    	super(r, 1000, 600);
    	root = r; 
        
        rightPanel = new VBox();
        root.setRight(rightPanel);
        
        bottomPanel = new HBox();
        root.setBottom(bottomPanel);
        
        //BOARDVIEW TEST
        HexTile[] array1 = new HexTile[] {null, null, null, new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA)};
        HexTile[] array2 = new HexTile[] {null, null, new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA)};
        HexTile[] array3 = new HexTile[] {null, new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA)};
        HexTile[] array4 = new HexTile[] {new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA)};
        HexTile[] array5 = new HexTile[] {new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), null};
        HexTile[] array6 = new HexTile[] {new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), null, null};
        HexTile[] array7 = new HexTile[] {new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), null, null, null};
        HexTile[][] tiles = {array1, array2, array3, array4, array5, array6, array7};
        
        tilePreview = new TilePreview();
        
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
	
	public String waitForPhaseCompletion(CurrentPhase currentPhase){
		//pass and set phase, update controls accordingly, exit when "Done" is pressed
		this.currentPhase = currentPhase;
		
		String s = "";
		
		userInputDone = false;
		try {
			do{
				Thread.sleep(500);
			}while(!userInputDone);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//example for playign things
		if(currentPhase == CurrentPhase.PLAY_THINGS)
		{
			HexTile hypotheticalTile = new HexTile(Terrain.SEA);
			Thing hypotheticalThing = new Creature(Terrain.SEA);
			
			//e.g.
			s += hypotheticalTile.x + "SPLIT" + hypotheticalTile.y+ " " + hypotheticalThing.thingID + "/";
		}
		
		this.currentPhase = CurrentPhase.NULL;
		
		return s;
	}

	public void updatePlayerRack() {
		//re-draw the player rack for current player (because it has changed)
		//if necessary, I can pass the appropriate rack
	}

	public int getNumTradeRecruits() {
		return 0;
	}
	
	
}