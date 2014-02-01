package gui;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

public class PlayerPanelRack extends HBox {
	List<Button> things = new ArrayList<Button>(10);
	
	public PlayerPanelRack(List<Button> l)
	{
		this.getChildren().addAll(l);
	}
}
