package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

import Game.Combatant;
import Game.Creature;
import Game.GameConstants;
import Game.HexTile;
import Game.GameConstants.ControlledBy;
import Game.GameConstants.Terrain;
import Game.Thing;
import Game.Utility;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


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
    private Semaphore inputLock = new Semaphore(0);
    
    public int tileX;
    public int tileY;
	
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
					Utility.GotInput(inputLock);
				}
			}
		});
        
        yes.setOnAction(new EventHandler<ActionEvent>() {

			@Override
		    public void handle(ActionEvent e) {
				if (yesPressed == InputState.WAITING_FOR_INPUT){
					yesPressed = InputState.GOT_INPUT;
					Utility.GotInput(inputLock);
				}
			}
		});
        
        no.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
				if (noPressed == InputState.WAITING_FOR_INPUT){
					Utility.GotInput(inputLock);
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
		battleStage.setTitle("Combat Time - Player " + (GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum() + 1) );
		battleStage.setScene(this);
        battleStage.initStyle(StageStyle.UNDECORATED);
        battleStage.initModality(Modality.WINDOW_MODAL);
        battleStage.initOwner(GameClient.game.gameView.getWindow());
		
		//battleStage.setX(battleStage.getX() + 150);
		//battleStage.setY(battleStage.getY() + 150);

		battleStage.show();
	}
	
	public void RollDice(Thing thing, int diceNum, int roll, int numPreviousRolls){
		if (!thing.IsCombatant()){
			return;
		}
		
		Combatant combatant = (Combatant)thing;
		
		String s = "";
		
		if (numPreviousRolls > 0){
			s = "Number of hits you will apply this round: " + numPreviousRolls + "\n";
		}
		
		if (combatant.IsMagic()){
			s += "Rolling for magic";
		} else if (combatant.IsRange()){
			s += "Rolling for range";
		} else {
			s += "Rolling for regular";
		}
		
		if (combatant.IsCharge()){
			s += " charge";
		}
		
		s += " creature: " + thing.GetName();
		
		UpdateMessage(s);
	
		diceListView.RollDice(diceNum, roll);
		
		ClearMessage();
	}
	
	public void SetupPlayerThings(){
		HexTile tile = GameClient.game.gameModel.gameBoard.getTile(tileX, tileY);
		
		tilePreview = new TilePreview(
				GameClient.game.gameView.getCurrentPlayer()
		);
		
		tilePreview.changeTile(tile);
		
		leftPanel.getChildren().add(tilePreview);
		
	}
	
	public void RemoveThings(int[] thingIds, int playerNum){
		for (int thingId : thingIds){
			RemoveThingFromBattle(thingId, playerNum);
		}
	}
	
	public int[] inflictHits(int numHitsTaken) {
		UpdateMessage("Select " + numHitsTaken + " things to discard.");
		
		do {
			submitPressed = InputState.WAITING_FOR_INPUT;
			
			Utility.PromptForInput(inputLock);
		} while (GameView.selectedThings.size() != numHitsTaken);
		
		submitPressed = InputState.NOT_WAITING_FOR_INPUT;
		
		int[] thingsToRemove = new int[numHitsTaken];
		int i = 0;
		for (Integer thingId : GameView.selectedThings){
			System.out.println("--------------");
			thingsToRemove[i++] = thingId;
			RemoveThingFromBattle(thingId, GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum());
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
			Utility.PromptForInput(inputLock);
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
	
	public void RemoveThingFromBattle(final int thingId, int playerNum){
		final ThingViewList list = tilePreview.GetThingList(playerNum);
		
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	list.removeByThingId(thingId);
	        }
	    });
		
	}
}
