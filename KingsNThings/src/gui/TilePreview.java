package gui;

import java.util.ArrayList;

import Game.HexTile;
import Game.Thing;
import Game.Networking.GameClient;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;

public class TilePreview extends VBox {
	ThingViewList view1 = 		new ThingViewList();
	ThingViewList view2 = 		new ThingViewList();
	ThingViewList view3 = 		new ThingViewList();
	ThingViewList view4 = 		new ThingViewList();
	ThingViewList viewDefenders = new ThingViewList();
	Tile tileRef;
	int playerNum;
	
	TilePreview(int num) {
		this.getStyleClass().add("tile-preview");
		view1.getStyleClass().add("preview-list");
		view2.getStyleClass().add("preview-list");
		view3.getStyleClass().add("preview-list");
		view4.getStyleClass().add("preview-list");
		viewDefenders.getStyleClass().add("preview-list");
		
		this.playerNum = num;
	}
	
	public void show(Tile t) {
		this.getChildren().clear();
		
		if (view1 != null) this.getChildren().add(view1);
		if (view2 != null) this.getChildren().add(view2);
		if (view3 != null) this.getChildren().add(view3);
		if (view4 != null) this.getChildren().add(view4);
		if (viewDefenders != null) this.getChildren().add(viewDefenders);
		
		setInvisibleIfEmpty(t);
	}
	
	private void setInvisibleIfEmpty(Tile t) {
		//make more awesome
		
		view1.setVisible(!t.p1Things.isEmpty());
		view2.setVisible(!t.p2Things.isEmpty());
		view3.setVisible(!t.p3Things.isEmpty());
		view4.setVisible(!t.p4Things.isEmpty());
		viewDefenders.setVisible(!t.defendingThings.isEmpty());
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
		viewDefenders = new ThingViewList(FXCollections.observableList(t.defendingThings));		
		
		show(tileRef);
	}
	
	public void changeBattleTile(Tile t) {
		//this method is intended to load all things for battle
		//in addition to base things
		
		changeTile(t);
		addForts(t.getTileRef());
		show(t);
	}
	
	public void addForts(HexTile h)
	{
 		if(tileRef.fort != null)
 		{
 			ThingViewList thingViewForFort;
 			switch(h.controlledBy)
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

 			thingViewForFort.add(new ThingView(h.getFort()));
 		}
	}
	
	public void setPlayerNum(int i) {
		this.playerNum = i;
	}
	
	public void changeTile(HexTile tile){
		changeTile(GameClient.game.gameView.board.getTileByHex(tile));
	}
	
	public ThingViewList getThingViewList(int i) {
		switch(i) {
			case 1: return view1;
			case 2: return view2;
			case 3: return view3;
			case 4: return view4;
			default: return viewDefenders;
		}
	}

	public Tile getTile() {
		return tileRef;
	}
}