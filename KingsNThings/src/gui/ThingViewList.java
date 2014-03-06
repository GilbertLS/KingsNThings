package gui;

import java.util.ArrayList;
import java.util.List;
import Game.Thing;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ThingViewList extends HBox {
	protected ListView<ThingView> view;
	
	 ThingViewList(ObservableList<ThingView> l) {
		view = new ListView<ThingView>(l);
		view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		view.setOrientation(Orientation.HORIZONTAL);
		view.getStyleClass().add("thingview-list");
		view.setPrefHeight(70);
		view.setCellFactory(new Callback<ListView<ThingView>, ListCell<ThingView>>() {
            @Override
            public ListCell<ThingView> call(ListView<ThingView> param) {
                return new ThingCell();
            }
        });
				
		this.getChildren().add(view);
		this.getStyleClass().add("thingview-list");
	 }
	 
	 public void add(ThingView t) {
		 view.getItems().add(t);
	 }
	 
	 public void addAll(List<ThingView> l) {
		 view.getItems().addAll(l);
	 }
	 
	 public void remove(ThingView t) {
		 view.getItems().remove(t);
	 }
	 
	 public void remove(int i) {
		 view.getItems().remove(i);
	 }
	 
	 public void removeByThingId(int id) {
		 ThingView remove = null;
		 
		 for (ThingView tv : view.getItems()) {
			 if (tv.thingRef.thingID == id) {
				 System.out.println("REMOVED ITEM: " + id);
				 remove = tv;
				 break;
			 }
		 }
		 
		 view.getItems().remove(remove);
	 }
	 
	 public void removeAll(List<ThingView> l) {
		 view.getItems().removeAll(l);
	 }
	 
	 public void setAll(List<ThingView> l) {
		 view.getItems().setAll(l);
	 }
	 
	 public void setAllThings(List<Thing> t) {
		 List<ThingView> list = new ArrayList<ThingView>();
		 
		 for (int i = 0; i < t.size(); i++) {
			 ThingView tv = new ThingView(t.get(i));
			 
			 list.add(tv);
		 }
		 
		 this.setAll(list);
	 }
}
