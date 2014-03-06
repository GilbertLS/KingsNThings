package gui;

import java.util.ArrayList;
import java.util.List;

import Game.Creature;
import Game.GameConstants;
import Game.GameController;
import Game.HexTile;
import Game.Thing;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;

public class TilePreview extends VBox {
	ThingViewList view1 = 		new ThingViewList();
	ThingViewList view2 = 		new ThingViewList();
	ThingViewList view3 = 		new ThingViewList();
	ThingViewList view4 = 		new ThingViewList();
	ThingViewList viewNeutral = new ThingViewList();
	Tile tileRef;
	int playerNum;
	
	TilePreview(int num) {
		this.getStyleClass().add("tile-preview");
		view1.getStyleClass().add("preview-list");
		view2.getStyleClass().add("preview-list");
		view3.getStyleClass().add("preview-list");
		view4.getStyleClass().add("preview-list");
		//viewNeutral.getStyleClass().add("preview-list");
		
		this.getChildren().add(view1);
		this.getChildren().add(view2);
		this.getChildren().add(view3);
		this.getChildren().add(view4);
		//this.getChildren().add(viewNeutral);
		
		this.playerNum = num;
	}
	
	public void show() {
		this.getChildren().clear();
		
		if (view1 != null) this.getChildren().add(view1);
		if (view2 != null) this.getChildren().add(view2);
		if (view3 != null) this.getChildren().add(view3);
		if (view4 != null) this.getChildren().add(view4);
		//if (viewNeutral != null) this.getChildren().add(viewNeutral);
	}
	
	public ThingViewList GetThingList(int playerNum){
		System.out.println("REMOVING PLAYERS THINGS: " + playerNum);
		if( playerNum == 0 ){
			return view1;
		} else if ( playerNum == 1 ){
			return view2;
		} else if ( playerNum == 2 ){
			return view3;
		} else if ( playerNum == 3 ){
			return view4;
		}
		return null;
	}
		
	public void changeTile(Tile t) {
		
		tileRef = t;
		
		view1 = new ThingViewList(FXCollections.observableList(t.p1Things));
		view2 = new ThingViewList(FXCollections.observableList(t.p2Things));
		view3 = new ThingViewList(FXCollections.observableList(t.p3Things));
		view4 = new ThingViewList(FXCollections.observableList(t.p4Things));
		//viewNeutral = new ThingViewList(FXCollections.observableList(t.neutralThings));
		
		show();
	}
	
	public void setPlayerNum(int i) {
		this.playerNum = i;
	}
	
	public void changeTile(HexTile tile){
		//change so gets combatants? or, add functionality to load in settlements
		
		//Change this for neutral things in the future?
		for (int i = 0; i < GameController.numClients; i++) {
			ThingViewList view = getThingViewList(i);
			System.out.println("hello");
			if (view.getChildrenUnmodifiable().size() > 1) {				
				view = new ThingViewList(FXCollections.observableList((tileRef.getThings(i))));
			}
			else
				view = null;
		
		}
		
		if(tile.fort != null)
		{
			ThingViewList thingViewForFort;
			switch(tile.controlledBy)
			{
			case PLAYER1:
				thingViewForFort = view1;
				break;
			case PLAYER2:
				thingViewForFort = view2;
				break;
			case PLAYER3:
				thingViewForFort = view3;
				break;
			default:
				thingViewForFort = view4;
				break;
			}
			
			thingViewForFort.add(new ThingView(tile.fort));
		}
		
		show();
	}
	
	public ThingViewList getThingViewList(int i) {
		switch(i) {
			case 1: return view1;
			case 2: return view2;
			case 3: return view3;
			case 4: return view4;
			default: return viewNeutral;
		}
	}
}