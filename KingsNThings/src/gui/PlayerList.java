package gui;

import java.util.List;
import javafx.scene.layout.VBox;

public class PlayerList extends VBox {
	List<PlayerPanel> playerPanels;
	
	PlayerList(List<PlayerPanel> l)
	{
		this.getStyleClass().add("player-list");
		playerPanels = l;
		
		this.getChildren().addAll(l);
	}
}