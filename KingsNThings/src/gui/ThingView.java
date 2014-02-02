package gui;

import Game.Thing;
import javafx.scene.control.Label;

public class ThingView extends Label
{
	Thing thingRef;
	
	public ThingView(Thing t)
	{
		thingRef = t;
		
		getStyleClass().add("thing");
		setPrefSize(50, 50);
	}
}