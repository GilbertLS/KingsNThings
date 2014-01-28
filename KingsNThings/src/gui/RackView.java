package gui;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;

public class RackView extends ListView<ThingView> {
	
	RackView(ObservableList<ThingView> l) {
		super(l);
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		setOrientation(Orientation.HORIZONTAL);
		getStyleClass().add("rack");
		setPrefSize(800, 70);
		setCellFactory(new Callback<ListView<ThingView>, ListCell<ThingView>>() {
            @Override
            public ListCell<ThingView> call(ListView<ThingView> param) {
                return new ThingCell();
            }
        });
	}
}
