package gui;

import javafx.scene.control.Label;

public class ThingView extends Label
{
	public ThingView()
	{
		getStyleClass().add("thing");
		setPrefSize(50, 50);
	}
}