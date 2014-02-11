package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import Game.Creature;
import Game.GameClientController;
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
	private GameClientController controller;
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
	
    public GameView(BorderPane r) {
    	super(r, 1000, 600);
    	controller = null;
    	root = r; 
        
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
		
		//example for playign things
		if(currentPhase == CurrentPhase.PLAY_THINGS)
		{
			userInputDone = false;
			try {
				do{
					Thread.sleep(500);
				}while(!userInputDone);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(currentPhase == CurrentPhase.MOVEMENT)
		{
			do
			{
				
			}while(returnString.equals(""));
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

		
	public void setCurrentPlayer(int p) {
		this.currPlayerNum = p;
		this.tilePreview.setPlayerNum(p);
	}
	
	public Integer getCurrentPlayer() {
		return this.currPlayerNum;
	}
	
	public void setController(GameClientController c) {
		this.controller = c;
	}
	
}