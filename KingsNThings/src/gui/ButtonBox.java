package gui;

import java.util.ArrayList;

import Game.Networking.GameClient;
import Game.GameConstants.CurrentPhase;
import Game.Utility;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ButtonBox extends HBox {
	Button showHideTiles = new Button("Show Tiles");
	Button recruitSpecial = new Button("Recruit Special Character");
	Button userInputComplete = new Button("Done Phase");
	Button stealGold = new Button("Steal Gold");
	Button stealRecruit = new Button("Steal Recruit");
	Button trade = new Button("Trade");
	ArrayList<Button> buttons;
	
	ButtonBox()
	{
		buttons = new ArrayList<Button>(5);
		buttons.add(showHideTiles);
		buttons.add(userInputComplete);
		
		this.getChildren().addAll(buttons);
		
		this.getStyleClass().add("button-box");
		
		//Setting an action for the Submit button
		userInputComplete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
		    public void handle(ActionEvent e) {
				GameView gv = ((GameView)getScene());
				
				if(gv.currentPhase != CurrentPhase.NULL)
				{
					Utility.GotInput(gv.inputLock);
					
					if(gv.currentPhase == CurrentPhase.MOVEMENT)
						Utility.GotInput(gv.moveLock);
					
					if(gv.currentPhase == CurrentPhase.PLAY_THINGS)
						Utility.GotInput(gv.playLock);
					
					//allow end of recruitment phase when no character recruited
					if(gv.currentPhase == CurrentPhase.RECRUIT_CHARACTER
							&& !gv.characterRecruited)
						Utility.GotInput(gv.recruitLock);
					
					gv.userInputDone = true;
				}
			}
		});
		
		showHideTiles.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
		    public void handle(ActionEvent e) {
				if(showHideTiles.getText().equals("Show Tiles"))
				{
					showHideTiles.setText("Hide Tiles");
					GameView gv = (GameView)getScene();
					gv.showHideAllTiles(true);
				}
				else
				{
					showHideTiles.setText("Show Tiles");
					GameView gv = (GameView)getScene();
					gv.showHideAllTiles(false);	
				}
			}
		});
		
		recruitSpecial.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
		    public void handle(ActionEvent e) {
				if(GameClient.game.gameView.currentPhase == CurrentPhase.RECRUIT_CHARACTER
						&& GameClient.game.gameView.characterRecruited == false)
				{
					int playerIndex = GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum();
					
					SpecialCharacterView SCView = new SpecialCharacterView(GameClient.game.gameView.primaryStage, playerIndex);
					GameView.specialCharacterView = SCView;
				}
			}
		});
		
		trade.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
		    public void handle(ActionEvent e) {
				CurrentPhase currentPhase = GameClient.game.gameView.currentPhase;
				
				if(currentPhase == CurrentPhase.INITIAL_TRADE_THINGS
						|| currentPhase == CurrentPhase.TRADE_THINGS){
					GameView gv = (GameView)getScene();
					
					//ensure divisible by 2 if normal trade
					if(currentPhase == CurrentPhase.TRADE_THINGS){
						if(gv.rack.getSelectionModel().getSelectedIndices().size() % 2 != 0)
							return;
					}
					
					//return IDs of things traded
					ObservableList<ThingView> items = gv.rack.getItems();
					for (Integer i : gv.rack.getSelectionModel().getSelectedIndices()) {
						gv.returnString += ((ThingView)items.get(i)).thingRef.thingID + " ";
					}
					
					Utility.GotInput(gv.inputLock);
				}
			}
		});
		
		stealGold.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
		    public void handle(ActionEvent e) {
				GameView gv = GameClient.game.gameView;
				
				gv.returnString = "gold";
				Utility.GotInput(gv.inputLock);
			}
		});
		
		stealRecruit.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
		    public void handle(ActionEvent e) {
				GameView gv = GameClient.game.gameView;
				
				gv.returnString = "recruit";
				Utility.GotInput(gv.inputLock);
			}
		});
	}
	
	public void updateButtons(final CurrentPhase currentPhase)
	{
		Platform.runLater(new Runnable(){
			public void run(){
				switch(currentPhase){
				case INITIAL_TRADE_THINGS:
				case TRADE_THINGS:
					getChildren().setAll(trade, userInputComplete);
					break;
				case RECRUIT_CHARACTER:
					getChildren().setAll(recruitSpecial, userInputComplete);
					break;
				case CHOOSE_THIEF_ACTION:
					getChildren().setAll(stealGold, stealRecruit);
					break;
				case SELECT_TARGET_PLAYER:
					getChildren().setAll();
					break;
				default:
					getChildren().setAll(buttons);
					break;
				}
			}
		});
	}
}
