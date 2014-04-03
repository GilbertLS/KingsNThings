package gui;

import Game.Networking.GameClient;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class PlayerBattleBox extends Region {
	private int playerNum;
	
	public PlayerBattleBox(int pNum) {
		playerNum = pNum;
		this.setStyle("-fx-background-image: url(/res/images/Player" + (playerNum + 1) + ".png); ");
		String iconPath = PlayerPanel.getPlayerIcon(playerNum + 1);
		if(iconPath != null) {
			Image playerIcon = new Image(iconPath, 25, 25, false, true);
			ImageView iconView = new ImageView(playerIcon);
			this.getChildren().add(iconView);
		}
	
		
		this.getStyleClass().add("battle-player");
    	this.setPrefSize(50, 50);
    	this.setMaxHeight(50);
    	this.setMaxWidth(50);
    	this.setMinHeight(50);
    	this.setMinWidth(50);
    	this.initListeners();
	}

	private void initListeners() {
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		@Override public void handle(MouseEvent e) {
    			GameView.battleView.setPlayerTarget(playerNum);
    		}
    	});
	}
}
