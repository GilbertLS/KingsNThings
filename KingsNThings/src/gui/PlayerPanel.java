package gui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class PlayerPanel extends VBox {
	private int playerNum;
	private HBox playerInfo = new HBox();
	private Label goldText 	= new Label();
	private Label nameText	= new Label();
	private Label thingText = new Label();
	
	PlayerPanel(int playerNumber) {
		this.getStyleClass().add("player-panel");
		this.playerNum = playerNumber;

		
		this.playerInfo.getChildren().add(nameText);
		this.playerInfo.getChildren().add(goldText);
		this.playerInfo.getChildren().add(thingText);
		this.getChildren().add(playerInfo);
		this.setPrefSize(300, 50);
		setName("Player" + playerNumber);
		setGold(0);
		setThings(0);
	}
	
	public void setGold(int g) {
		goldText.setText("Gold: " + g);
	}
	
	private void setName(String n) {
		nameText.setText(n);
	}
	
	public void setThings(int t) {
		thingText.setText("Rack: " + t);
	}
	
	public int getPlayerNumber() {
		return playerNum;
	}

	public void addGold(int i) {
		String[] goldTextStrings = goldText.getText().split(" ");
		
		int prevGold = Integer.parseInt(goldTextStrings[1]);
		
		prevGold += i;
		
		goldText.setText("Gold: " + prevGold);
	}
}
