package gui;

import java.util.concurrent.Semaphore;

import Game.Utility;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class DiceView extends Region {
	private int roll = 1;
	private Boolean isEnabled = false;
	private DiceView thisDice = this;
	private Semaphore inputLock = new Semaphore(0);
	
	public DiceView(){
		this.setStyle("-fx-background-image: url(/res/images/ " + getBackgroundFromRoll() + "); ");
		this.setPrefSize(50,50);
    	this.getStyleClass().add("dice");
    	this.initListeners();
	}
	
	private String getBackgroundFromRoll(){
		return "dice_" + roll + ".png";
	}
	
	public void RollDice(int roll){	
		this.roll = roll;
		this.isEnabled = true;
		
		PromptForRoll();
		System.out.println("GOT OUT");
	}
	
	
	private void UpdateDice(){
		setStyle("-fx-background-image: url(/res/images/ " + getBackgroundFromRoll() + "); ");
		isEnabled = false;
		Utility.GotInput(inputLock);
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
	        	if (thisDice.isEnabled){
	        		UpdateDice();
	        	}
	        }
	   });
	}
}
