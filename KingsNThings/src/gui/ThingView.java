package gui;

import javafx.scene.control.Label;

public class ThingView extends Label
{
	public ThingView()
	{
		this.setText("hello");
		this.getStyleClass().add("thing");
		this.setPrefSize(50, 50);
	}
}