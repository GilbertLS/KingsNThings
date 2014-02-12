package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Game.Thing;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class ThingViewList extends HBox {
	public ListView<ThingView> view;
	public ArrayList<ThingView> items;
	
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
		
		items = new ArrayList<ThingView>();
		
		this.getChildren().add(view);
		this.getStyleClass().add("thingview-list");
	 }
	 
	 public void add(ThingView t) {
		 items.add(t);
		 view.getItems().add(t);
	 }
	 
	 public void addAll(List<ThingView> l) {
		 items.addAll(l);
		 view.getItems().addAll(l);
	 }
	 
	 public void remove(ThingView t) {
		 items.remove(t);
		 view.getItems().remove(t);
	 }
	 
	 public void remove(int i) {
		 if (items.get(i) == view.getItems().get(i)){
			 items.remove(i);
			 view.getItems().remove(i);
		 }
	 }
	 
	 public void removeAll(List<ThingView> l) {
		 items.removeAll(l);
		 view.getItems().removeAll(l);
	 }
	 
	 public void setAll(List<ThingView> l) {
		 items.clear();
		 items.addAll(l);
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
