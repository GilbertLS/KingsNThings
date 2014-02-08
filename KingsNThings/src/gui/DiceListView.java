package gui;

import javafx.scene.layout.HBox;

public class DiceListView extends HBox {
	DiceView[] diceViews = new DiceView[4];
	
	public DiceListView(){
		for (int i = 0; i < 4; i++){
			diceViews[i] = new DiceView();
		}
		this.getChildren().addAll(diceViews);
	}
	
	public Void RollDice(int diceNum, int roll){
		diceViews[diceNum].RollDice(roll);
		return null;
	}
}
