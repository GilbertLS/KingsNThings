package gui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class RackView extends ListView<ThingView> {
	ObservableList<ThingView> items;
	 RackView thisList = this;
	
	RackView(ObservableList<ThingView> l) {
		super(l);
		
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		setOrientation(Orientation.HORIZONTAL);
		getStyleClass().add("rack");
		setPrefHeight(70);
		setCellFactory(new Callback<ListView<ThingView>, ListCell<ThingView>>() {
            @Override
            public ListCell<ThingView> call(ListView<ThingView> param) {
                return new ThingCell();
            }
        });
		
		this.getChildren().addListener(new ListChangeListener() {
	        @Override
	        public void onChanged(Change change) {
	        	thisList.setPrefWidth((thisList.getItems().size() * 64.5));
	        }
		});
	}
}
