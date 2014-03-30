package gui;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import Game.Combatant;
import Game.GameConstants;
import Game.HexTile;
import Game.Thing;
import Game.Utility;
import Game.Networking.GameClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class BattleView extends Scene {
	public BorderPane root;
    public VBox rightPanel;
    public HBox bottomPanel;
    public VBox leftPanel;
    public HBox centerPanel;
    public HBox topPanel;
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
    private ThingViewList[] thingViewLists;
	
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
        
        centerPanel = new HBox();
        root.setCenter(centerPanel);
        
        topPanel = new HBox();
        root.setTop(topPanel);
        
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

        VBox centerVBox = new VBox();
        messageView = new MessageView();
        diceListView = new DiceListView();
        centerVBox.getChildren().add(diceListView);
        centerVBox.getChildren().add(submit);
        HBox yesNo = new HBox();
        yesNo.getChildren().add(yes);
        yesNo.getChildren().add(no);
        centerVBox.getChildren().add(yesNo);
        centerVBox.getChildren().add(messageView);
        
        centerVBox.setAlignment(Pos.CENTER);
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.getChildren().add(centerVBox);
        
        this.getStylesheets().add("gui/main.css");
        
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
	
	public int RollDice(Thing thing, int diceNum, int numPreviousRolls){
		if (!thing.IsCombatant()){
			return 0;
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
	
		int roll = diceListView.RollDice(diceNum);
		
		ClearMessage();
		
		return roll;
	}
	
	public void SetupPlayerThings(){
		int numPlayers = GameClient.game.gameModel.PlayerCount();
		thingViewLists = new ThingViewList[numPlayers];
		
		HexTile tile = GameClient.game.gameModel.gameBoard.getTile(tileX, tileY);
		for(int i = 0; i < numPlayers; i++) {
			ArrayList<ThingView> thingViews = new ArrayList<ThingView>();
			for(Thing t : tile.GetThings(i)) {
				thingViews.add(new ThingView(t));
			}
			
			Orientation orientation = i % 2 == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL;
			
			ThingViewList thingViewList = new ThingViewList(
				FXCollections.observableArrayList(thingViews),
				orientation
			);
			
			thingViewLists[i] = thingViewList;
			
			if (i == 0) {
				topPanel.getChildren().add(thingViewList);
				topPanel.setAlignment(Pos.CENTER);
			} else if (i == 1) {
				leftPanel.getChildren().add(thingViewList);
				leftPanel.setAlignment(Pos.CENTER);
			} else if (i == 2) {
				bottomPanel.getChildren().add(thingViewList);
				bottomPanel.setAlignment(Pos.CENTER);
			} else if (i == 3) {
				rightPanel.getChildren().add(thingViewList);
				rightPanel.setAlignment(Pos.CENTER);
			}
		}
	}
	
	public void RemoveThings(int[] thingIds, int playerNum){
		for (int thingId : thingIds){
			RemoveThingFromBattle(thingId, playerNum);
		}
	}
	
	public int[] inflictHits(int numHitsTaken) {
		UpdateMessage("Select " + numHitsTaken + " things to discard.");
		
		int currPlayer = GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum();
		
		int numValid;
		do {
			submitPressed = InputState.WAITING_FOR_INPUT;
			
			Utility.PromptForInput(inputLock);
			
			numValid = 0;
			for(Thing t : GameView.selectedThings) {
				if (GameConstants.GetPlayerNumber(t.getControlledBy()) == currPlayer ) {
					numValid++;
				}
			}
		} while (numValid != numHitsTaken);
		
		submitPressed = InputState.NOT_WAITING_FOR_INPUT;
		
		int[] thingsToRemove = new int[numHitsTaken];
		int i = 0;
		for (Thing thing : GameView.selectedThings){
			System.out.println("--------------");
			thingsToRemove[i++] = thing.thingID;
			//RemoveThingFromBattle(thing.thingID, GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum());
			
			//also update model
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
		final ThingViewList list = thingViewLists[playerNum];
		
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	list.removeByThingId(thingId);
	        }
	    });
		
	}
}
