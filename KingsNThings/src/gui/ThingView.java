package gui;

import Game.Thing;
import javafx.scene.control.Label;

public class ThingView extends Label
{
	public Thing thingRef;
	
	public ThingView(Thing t)
	{
		thingRef = t;
		
		this.getStyleClass().add("thing");
		this.setPrefSize(50, 50);
		this.setBackground();
	}
	
	private void setBackground() {
		this.setStyle("-fx-background-image: url('res/images/" + this.thingRef.frontFileName + ");");
	}
}