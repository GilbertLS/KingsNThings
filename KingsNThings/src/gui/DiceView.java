package gui;

import java.util.Random;

import Game.Networking.GameClient;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class DiceView extends Region {
	private int roll = 1;
	private boolean isPrompted = false;
	private boolean isEnabled = false;
	
	public DiceView(){
		this.setStyle("-fx-background-image: url(/res/images/ " + getBackgroundFromRoll() + "); ");
		this.setPrefSize(50,50);
    	this.getStyleClass().add("dice");
    	this.initListeners();
    	
    	//remove this
    	isEnabled = true;
	}
	
	private String getBackgroundFromRoll(){
		return "dice-" + roll + ".png";
	}
	
	public void RollDice(int roll){	
		this.roll = roll;
		
		this.isEnabled = true;
		this.isPrompted = true;
		
		PromptForRoll();
	}
	
	
	private void UpdateDice(){
		setStyle("-fx-background-image: url(/res/images/ " + getBackgroundFromRoll() + "); ");
		//thisDice.isEnabled = false;
		isPrompted = false;
		roll = new Random().nextInt(6) + 1;
	};
	
	public void PromptForRoll(){
		if (!this.isPrompted){
			setStyle( getStyle() + "-fx-border-color: yellow;");
			setStyle( getStyle() + "-fx-border-width: 3;");
			setStyle( getStyle() + "-fx-border-style: solid;");
		}
	}
	
	private void initListeners(){
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	if (isEnabled){
	        		UpdateDice();
	        	}
	        }
	   });
	}
}
