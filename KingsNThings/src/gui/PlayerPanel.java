package gui;

import java.util.Arrays;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class PlayerPanel extends VBox {
	private PlayerPanelRack rack;
	private HBox playerInfo = new HBox();
	private Label goldText 	= new Label();
	private Label nameText	= new Label();
	
	PlayerPanel(String n, int g) {
		this.getStyleClass().add("player-panel");

		rack = new PlayerPanelRack(Arrays.asList(new Button(), new Button(), new Button(), new Button(), new Button(), new Button(), new Button(), new Button()));
		
		this.playerInfo.getChildren().add(nameText);
		this.playerInfo.getChildren().add(goldText);
		this.getChildren().add(playerInfo);
		this.getChildren().add(rack);
		this.setPrefSize(300, 50);
		setName(n);
		setGold(g);
	}
	
	public void setGold(int g) {
		goldText.setText("Gold: " + g);
	}
	
	private void setName(String n) {
		nameText.setText(n);
	}
}
