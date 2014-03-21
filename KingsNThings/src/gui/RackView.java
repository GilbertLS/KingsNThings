package gui;

import javafx.collections.ObservableList;

public class RackView extends ThingViewList {	
	RackView(ObservableList<ThingView> l) {
		super(l);
		this.setPrefWidth(643);
	}
}
