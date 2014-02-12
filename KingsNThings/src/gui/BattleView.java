package gui;

import java.util.ArrayList;
import java.util.Arrays;

import Game.Creature;
import Game.GameConstants;
import Game.HexTile;
import Game.GameConstants.Terrain;
import Game.Thing;
import Game.Networking.GameClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class BattleView extends Scene {
	public BorderPane root;
    public VBox rightPanel;
    public HBox bottomPanel;
    public VBox leftPanel;
    public BoardView board;
    public PlayerList playerList;
    public ButtonBox buttonBox;
    public RackView rack;
    public TilePreview tilePreview;
    public DiceListView diceListView;
    private Button submit = new Button("Submit");
    private Button yes = new Button("Yes");
    private Button no = new Button("No");
    protected InputState submitPressed = InputState.NOT_WAITING_FOR_INPUT;
    protected InputState yesPressed = InputState.NOT_WAITING_FOR_INPUT;
    protected InputState noPressed = InputState.NOT_WAITING_FOR_INPUT;
    public MessageView messageView;
    public Stage battleStage;
    
    private int tileX;
    private int tileY;
	
	public BattleView(BorderPane root, int tileX, int tileY){
		super(root, 700, 500);
		
		this.tileX = tileX;
		this.tileY = tileY;
		
		rightPanel = new VBox();
        root.setRight(rightPanel);
        
        bottomPanel = new HBox();
        root.setBottom(bottomPanel);
        
        leftPanel = new VBox();
        root.setLeft(leftPanel);
        
        
        submit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
		    public void handle(ActionEvent e) {
				if (submitPressed == InputState.WAITING_FOR_INPUT){
					submitPressed = InputState.GOT_INPUT;
				}
			}
		});
        
        yes.setOnAction(new EventHandler<ActionEvent>() {

			@Override
		    public void handle(ActionEvent e) {
				if (yesPressed == InputState.WAITING_FOR_INPUT){
					yesPressed = InputState.GOT_INPUT;
				}
			}
		});
        
        no.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
				if (noPressed == InputState.WAITING_FOR_INPUT){
					noPressed = InputState.GOT_INPUT;
				}
			}
		});
        //tilePreview = new TilePreview();
        
        /*board = new BoardView(tilePreview);
        root.setCenter(board);        */
        messageView = new MessageView();
        diceListView = new DiceListView();
        rightPanel.getChildren().add(diceListView);
        rightPanel.getChildren().add(submit);
        HBox yesNo = new HBox();
        yesNo.getChildren().add(yes);
        yesNo.getChildren().add(no);
        rightPanel.getChildren().add(yesNo);
        rightPanel.getChildren().add(messageView);
        
        this.getStylesheets().add("gui/myStyle.css");
        
        SetupPlayerThings();
        
        battleStage = new Stage();
		battleStage.setTitle("Combat Time!");
		battleStage.setScene(this);
         
		//battleStage.setX(battleStage.getX() + 150);
		//battleStage.setY(battleStage.getY() + 150);

		battleStage.show();
	}
	
	public void RollDice(int diceNum, int roll){
		diceListView.RollDice(diceNum, roll);
	}
	
	public void SetupPlayerThings(){
		HexTile tile = GameClient.game.gameModel.gameBoard.getTile(tileX, tileY);
		
		tilePreview = new TilePreview(
				GameClient.game.gameView.getCurrentPlayer()
		);
		
		tilePreview.changeTile(tile);
		
		leftPanel.getChildren().add(tilePreview);
		
	}
	
	public int[] inflictHits(int numHitsTaken) {
		UpdateMessage("Select " + numHitsTaken + " things to discard.");
		
		do {
			submitPressed = InputState.WAITING_FOR_INPUT;
			
			while(submitPressed != InputState.GOT_INPUT){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
		} while (GameView.selectedThings.size() != numHitsTaken);
		
		submitPressed = InputState.NOT_WAITING_FOR_INPUT;
		
		int[] thingsToRemove = new int[numHitsTaken];
		int i = 0;
		for (Integer thingId : GameView.selectedThings){
			System.out.println("--------------");
			System.out.println(thingId);
			thingsToRemove[i++] = thingId;
			RemoveThingFromBattle(thingId);
		}
		ClearMessage();
		
		return thingsToRemove;
	}
	
	public void UpdateMessage(final String message){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				messageView.displayMessage(message);
			}
		});
	}
	
	public boolean GetSurrender(){
		UpdateMessage("Would you like to surrender?");
		
		yesPressed = InputState.WAITING_FOR_INPUT;
		noPressed = InputState.WAITING_FOR_INPUT;
		
		while(yesPressed != InputState.GOT_INPUT && noPressed != InputState.GOT_INPUT){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
		
		boolean surrender;
		
		if (yesPressed == InputState.GOT_INPUT){
			surrender = true;
		} else {
			surrender = false;
		}
		
		yesPressed = InputState.NOT_WAITING_FOR_INPUT;
		noPressed = InputState.NOT_WAITING_FOR_INPUT;
		
		ClearMessage();
		
		return surrender;
	}
	
	public void ClearMessage(){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				messageView.clearMessage();
			}
		});
	}
	
	public void RemoveThingFromBattle(final int thingId){
		final int currPlayer = GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum();
		final ThingViewList list = tilePreview.GetThingList(currPlayer);
		
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	list.removeByThingId(thingId);
	        }
	    });
		
	}
}
