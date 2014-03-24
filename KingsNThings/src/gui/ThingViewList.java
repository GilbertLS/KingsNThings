package gui;

import java.util.ArrayList;
import java.util.List;
import Game.Thing;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Callback;

public class ThingViewList extends ListView<ThingView> {
	protected ThingViewList self = this;
	
	ThingViewList(ObservableList<ThingView> l) {
		super(l);
		setup(Orientation.HORIZONTAL);
	 }
	
	 ThingViewList(ObservableList<ThingView> l, Orientation orientation) {
		super(l);
		setup(orientation);
	 }
	 
	 ThingViewList() {
		 super();
		 setup(Orientation.HORIZONTAL);
	 }
	 
	 private void setup(Orientation orientation) {
		 this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			this.setOrientation(orientation);
			this.getStyleClass().add("thingview-list");
			if(orientation == Orientation.HORIZONTAL) {
				this.setPrefHeight(70);
			} else {
				this.setPrefWidth(70);
			}
			this.setCellFactory(new Callback<ListView<ThingView>, ListCell<ThingView>>() {
	            @Override
	            public ListCell<ThingView> call(ListView<ThingView> param) {
	                return new ThingCell();
	            }
	        });
	 }
	 
	 public void add(ThingView t) {
		 this.getItems().add(t);
	 }
	 
	 public void addAll(List<ThingView> l) {
		 this.getItems().addAll(l);
	 }
	 
	 public void remove(ThingView t) {
		 this.getItems().remove(t);
		 
		 if(getItems().isEmpty())
			 this.setVisible(false);
	 }
	 
	 public void remove(int i) {
		 this.getItems().remove(i);
		 
		 if(getItems().isEmpty())
			 this.setVisible(false);
	 }
	 
	 public void removeByThingId(int id) {
		 ThingView remove = null;
		 
		 for (ThingView tv : this.getItems()) {
			 if (tv.thingRef.thingID == id) {
				 System.out.println("REMOVED ITEM: " + id);
				 remove = tv;
				 break;
			 }
		 }
		 
		 this.getItems().remove(remove);
	 }
	 
	 public void removeAll(List<ThingView> l) {
		 this.getItems().removeAll(l);
	 }
	 
	 public void setAll(List<ThingView> l) {
		 this.getItems().setAll(l);
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
