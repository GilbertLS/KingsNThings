package gui;

import java.util.List;
import javafx.scene.layout.VBox;

public class PlayerList extends VBox {
	
	PlayerList(List<PlayerPanel> l)
	{	
		this.getStyleClass().add("player-list");
		this.getChildren().addAll(l);
	}
	
	public PlayerPanel getPlayerPanel(int playerNum)
	{
			return ((PlayerPanel)this.getChildren().get(playerNum));
	}
}