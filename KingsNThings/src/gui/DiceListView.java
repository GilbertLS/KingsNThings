package gui;

import Game.Networking.GameClient;
import javafx.scene.layout.HBox;

public class DiceListView extends HBox {
	DiceView[] diceViews;
	
	public DiceListView(int numDice) {
		diceViews = new DiceView[numDice];
		for (int i = 0; i < numDice; i++) {
			diceViews[i] = new DiceView();
		}
		this.getChildren().addAll(diceViews);
	}
	
	public DiceListView(){
		this(4);
	}
	
	public int RollDice(int diceNum) {
		int roll = diceViews[diceNum].RollDice();
		return roll;
	}
	
	public void SetRoll(int diceNum, int roll) {
		diceViews[diceNum].SetRoll(roll);
	}
}
