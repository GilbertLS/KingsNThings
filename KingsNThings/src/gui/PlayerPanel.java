package gui;

import Game.GameConstants.CurrentPhase;
import Game.Utility;
import Game.Networking.GameClient;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
		
		String iconPath = PlayerPanel.getPlayerIcon(playerNumber);
		if(iconPath != null) {
			Image playerIcon = new Image(iconPath, 25, 25, false, true);
			ImageView iconView = new ImageView(playerIcon);
			this.playerInfo.getChildren().add(iconView);
		}
		
		this.playerInfo.getChildren().add(nameText);
		this.playerInfo.getChildren().add(goldText);
		this.playerInfo.getChildren().add(thingText);
		this.getChildren().add(playerInfo);
		this.setPrefWidth(300);
		setName("Player" + playerNumber);
		setGold(0);
		setThings(0);
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				GameView gv = GameClient.game.gameView;
				
				if(gv.currentPhase == CurrentPhase.SELECT_TARGET_PLAYER
						&& gv.currPlayerNum != playerNum){
					gv.returnString = Integer.toString(gv.currPlayerNum);
					Utility.GotInput(gv.inputLock);
				}
			}
		});
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
	
	public void addThings(int i) {
		String[] thingsTextStrings = thingText.getText().split(" ");
		
		int prevNumThings = Integer.parseInt(thingsTextStrings[1]);
		
		prevNumThings += i;
		
		thingText.setText("Rack: " + prevNumThings);
	}

	public void payGold(int gold) {
		String[] goldTextStrings = goldText.getText().split(" ");
		
		int prevGold = Integer.parseInt(goldTextStrings[1]);
		
		prevGold -= gold;
		
		goldText.setText("Gold: " + prevGold);
		
	}

	public void removeThings(int i) {
		String[] thingsTextStrings = thingText.getText().split(" ");
		
		int prevNumThings = Integer.parseInt(thingsTextStrings[1]);
		
		prevNumThings -= i;
		
		thingText.setText("Rack: " + prevNumThings);
	}
	
	public static String getPlayerIcon(int i) {
		switch(i) {
			case 1: return "res/images/CM_411.png";
			case 2: return "res/images/CM_412.png";
			case 3: return "res/images/CM_413.png";
			case 4: return "res/images/CM_414.png";
		}
		return null;
	}
}
