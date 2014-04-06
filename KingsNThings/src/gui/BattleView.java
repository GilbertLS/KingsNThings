package gui;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import Game.Building;
import Game.Combatant;
import Game.Fort;
import Game.GameConstants;
import Game.HexTile;
import Game.Thing;
import Game.Utility;
import Game.GameConstants.Level;
import Game.Networking.GameClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    //public Stage battleStage;
    private Semaphore inputLock = new Semaphore(0);
    VBox centerVBox = new VBox();
    
    public int tileX;
    public int tileY;
    private HexTile currTile;
    private ThingViewList[] thingViewLists;
    
    private boolean currentlyTargeting = false;
    private int targetedPlayer = -1;
    private Semaphore targetLock = new Semaphore(0);
	
	public BattleView(BorderPane root, int tileX, int tileY){
		super(root, 700, 500);
		
		this.tileX = tileX;
		this.tileY = tileY;
		this.currTile = GameClient.game.gameModel.boardController.GetTile(tileX, tileY);
		
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
        
        SetupPlayerThings();
    
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
		
        
        /*battleStage = new Stage();
		battleStage.setTitle("Combat Time - Player " + (GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum() + 1) );
		battleStage.setScene(this);
        battleStage.initStyle(StageStyle.UNDECORATED);
        battleStage.initModality(Modality.WINDOW_MODAL);
        battleStage.initOwner(GameClient.game.gameView.getWindow());*/
		
		//battleStage.setX(battleStage.getX() + 150);
		//battleStage.setY(battleStage.getY() + 150);

		//battleStage.show();
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.DIGIT1) {
					diceListView.SetRoll(0, 1);
				} else if (event.getCode() == KeyCode.DIGIT2) {
					diceListView.SetRoll(0, 2);
				} else if (event.getCode() == KeyCode.DIGIT3) {
					diceListView.SetRoll(0, 3);
				} else if (event.getCode() == KeyCode.DIGIT4) {
					diceListView.SetRoll(0, 4);
				} else if (event.getCode() == KeyCode.DIGIT5) {
					diceListView.SetRoll(0, 5);
				} else if (event.getCode() == KeyCode.DIGIT6) {
					diceListView.SetRoll(0, 6);
				}
			}
        });
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
		thingViewLists = new ThingViewList[numPlayers + 1];
		
		for(int i = 0; i < numPlayers; i++) {
			ArrayList<ThingView> thingViews = new ArrayList<ThingView>();
			for(Thing t : currTile.getCombatants(i)) {
				thingViews.add(new ThingView(t));
			}
			
			Orientation orientation = i % 2 == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL;
			
			ThingViewList thingViewList = new ThingViewList(
				FXCollections.observableArrayList(thingViews),
				orientation
			);
			
			thingViewLists[i] = thingViewList;
			
			Pane panel = null;
			
			if (i == 0) { panel = topPanel; } 
			else if (i == 1) { panel = leftPanel; }
			else if (i == 2) { panel = bottomPanel; } 
			else if (i == 3) { panel = rightPanel; }
			
			if (i % 2 == 0) {
				HBox playerAndThings = new HBox();
				playerAndThings.getChildren().add(new PlayerBattleBox(i));
				playerAndThings.getChildren().add(thingViewList);
				HBox hb = (HBox)panel;
				
				hb.getChildren().add(playerAndThings);
				hb.setAlignment(Pos.CENTER);
				
			} else {
				VBox playerAndThings = new VBox();
				playerAndThings.getChildren().add(new PlayerBattleBox(i));
				playerAndThings.getChildren().add(thingViewList);
				
				VBox vb = (VBox)panel;
				
				vb.getChildren().add(playerAndThings);
				vb.setAlignment(Pos.CENTER);
			}
		}
		
		ArrayList<ThingView> thingViews = new ArrayList<ThingView>();
		for(Thing t : currTile.getCombatants(4)) {
			thingViews.add(new ThingView(t));
		}
		
		ThingViewList thingViewList = new ThingViewList(
			FXCollections.observableArrayList(thingViews)
		);
		
		thingViewLists[numPlayers] = thingViewList;
		if (!thingViews.isEmpty()) {
			HBox neutralThings = new HBox();
			neutralThings.getChildren().add(new PlayerBattleBox(4));
			neutralThings.getChildren().add(thingViewList);
			
			centerVBox.getChildren().add(neutralThings);
		}
	}
	
	public void RemoveThings(int[] thingIds, int playerNum){
		for (int thingId : thingIds){
			RemoveThingFromBattle(thingId, playerNum);
		}
	}
	
	public int[] inflictHits(int numHitsTaken) {
		UpdateMessage("Select " + numHitsTaken + " things to take hits."
				+ "\nSelecting a fort will take however amount of hits are leftover.");
		
		int currPlayer = GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum();
		
		int numValid;
		do {
			submitPressed = InputState.WAITING_FOR_INPUT;
			
			Utility.PromptForInput(inputLock);
			
			numValid = 0;
			for(Thing t : GameView.selectedThings) {
				//ensure controlled by, and not a neutralized building
				if (GameConstants.GetPlayerNumber(t.getControlledBy()) == currPlayer 
						&& !(t.isBuilding() && ((Building)t).isNeutralized())) {
					numValid++;
				}
			}
			System.out.println("DEBUG- NUM VALID " + numValid);
		} while (numValid != numHitsTaken);
		
		submitPressed = InputState.NOT_WAITING_FOR_INPUT;
		
		int[] thingsToRemove = new int[numHitsTaken];
		int i = 0;
		for (Thing thing : GameView.selectedThings){
			System.out.println("--------------");
			thingsToRemove[i++] = thing.thingID;
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
	        	boolean removeThing = true;
	        	if(currTile.hasFort() && currTile.getFort().thingID == thingId) {
	        		Fort f = currTile.getFort();
	        		if (f != null) {
	        			removeThing = false;
	        			ThingView thingView = new ThingView(f);
	        			list.removeByThingId(thingId);
	        			list.add(thingView);
	        		}
	        	}
	        	if(removeThing) {
	        		list.removeByThingId(thingId);
	        	}
	        }
	    });
		
	}
	
	public int GetTargetPlayer() {
		this.UpdateMessage("Target player");
		currentlyTargeting = true;
		do {
			if (targetedPlayer != -1) {
				this.UpdateMessage("Cannot target player, target someone else");
			}
			
			targetLock = new Semaphore(0);
			Utility.PromptForInput(targetLock);
		} while (targetedPlayerNotValid());
		
		this.UpdateMessage("Targeted player " + targetedPlayer);
		int temp = targetedPlayer;
		targetedPlayer = -1;
		currentlyTargeting = false;
		
		return temp;
	}
	
	private boolean targetedPlayerNotValid() {
		int currPlayer = GameClient.game.gameModel.getCurrPlayerNumber();
		int numPlayers = GameClient.game.gameModel.PlayerCount();
		
		boolean targetedPlayerValid = targetedPlayer != currPlayer && 
				((targetedPlayer >= 0 && targetedPlayer < numPlayers) ||
				 (targetedPlayer == 4));
		
		if (currTile.getCombatants(targetedPlayer).isEmpty()) {
			targetedPlayerValid = false;
		}
		
		return !targetedPlayerValid;
	}

	public void setPlayerTarget(int playerNum) {
		if (currentlyTargeting) {
			targetedPlayer = playerNum;
			Utility.GotInput(targetLock);
		}
	}
}
