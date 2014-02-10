package gui;

import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class BattleView extends HBox {
	DiceListView diceList;
	ListView<ThingViewList> thingListViews;
	
	public BattleView(
			DiceListView diceList
	){
		this.diceList = diceList;
	}

	
	
}
