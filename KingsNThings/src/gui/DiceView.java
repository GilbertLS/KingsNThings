package gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Semaphore;

import Game.Dice;
import Game.Utility;
import Game.Networking.GameClient;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class DiceView extends Region {
	private int roll = 1;
	private Boolean isEnabled = false;
	private DiceView thisDice = this;
	private Semaphore inputLock = new Semaphore(0);
	private boolean takingInput = false;
	
	public DiceView(){
		this.setStyle("-fx-background-image: url(/res/images/ " + getBackgroundFromRoll() + "); ");
		this.setPrefSize(50,50);
    	this.getStyleClass().add("dice");
    	this.initListeners();
	}
	
	private String getBackgroundFromRoll(){
		return "dice_" + roll + ".png";
	}
	
	public int RollDice(){	
		do {
			this.isEnabled = true;
			PromptForRoll();
		} while (roll < 1 && roll > 6);
		return this.roll;
	}
	
	
	private void UpdateDice(){
		if (roll >= 1 && roll <= 6) {
			setStyle("-fx-background-image: url(/res/images/ " + getBackgroundFromRoll() + "); ");
			isEnabled = false;
			takingInput = false;
			Utility.GotInput(inputLock);
		}
	};
	
	public void PromptForRoll(){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				setStyle( getStyle() + "-fx-border-color: yellow;");
				setStyle( getStyle() + "-fx-border-width: 3;");
				setStyle( getStyle() + "-fx-border-style: solid;");
			}
		});
		
		Utility.PromptForInput(inputLock);
	}
	
	private void initListeners(){
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	if (e.getButton() == MouseButton.PRIMARY && thisDice.isEnabled){
	        		roll = Dice.rollDice(1)[0];
	        		UpdateDice();
	        	} else if (e.getButton() == MouseButton.SECONDARY && thisDice.isEnabled) {
	        		System.out.println("Taking input: " + true);
	        		takingInput = true;
	        	} 
	        }
	   });
	}
	
	public void SetRoll(int r) {
		System.out.println(takingInput + " " + r);
		if (!takingInput) {
			return;
		}
		
		roll = r;
		
		UpdateDice();
	}
}
