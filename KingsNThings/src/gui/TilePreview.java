package gui;

import java.util.ArrayList;

import Game.HexTile;
import Game.Thing;
import Game.Networking.GameClient;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TilePreview extends VBox {
	ThingViewList 	view1 			= new ThingViewList();
	ThingViewList 	view2 			= new ThingViewList();
	ThingViewList 	view3 			= new ThingViewList();
	ThingViewList 	view4 			= new ThingViewList();
	ThingViewList 	viewDefenders 	= new ThingViewList();
	Tile 			tileRef;
	int 			playerNum;
	
	TilePreview(int num) {	
		this.playerNum = num;
	}
	
	private void show(Tile t) {
		this.getChildren().clear();
		
		view1.setPrefWidth(307);
		view2.setPrefWidth(307);
		view3.setPrefWidth(307);
		view4.setPrefWidth(307);
		viewDefenders.setPrefWidth(307);
		
		if (view1 != null && !view1.isEmpty()) this.getChildren().add(createPanel(1, view1));
		if (view2 != null && !view2.isEmpty()) this.getChildren().add(createPanel(2, view2));
		if (view3 != null && !view3.isEmpty()) this.getChildren().add(createPanel(3, view3));
		if (view4 != null && !view4.isEmpty()) this.getChildren().add(createPanel(4, view4));
		if (viewDefenders != null && !viewDefenders.isEmpty()) this.getChildren().add(viewDefenders);
	}
	
	private HBox createPanel(int playerNum, ThingViewList t) {
		HBox panel = new HBox();
		panel.getChildren().add(getPlayerIcon(playerNum));
		panel.getChildren().add(t);
		
		return panel;
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
	
	private void addForts(HexTile h)
	{
 		if(tileRef.fort != null)
 		{
 			ThingViewList thingViewForFort;
 			switch(h.getControlledBy())
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
	
	private static VBox getPlayerIcon(int i) {
		ImageView icon = null;
		VBox box = new VBox();
		
		if(i==1)
			icon = new ImageView(new Image("res/images/CM_411.png", 25, 25, false, true));
		else if(i==2)
			icon = new ImageView(new Image("res/images/CM_412.png", 25, 25, false, true));
		else if(i==3)
			icon = new ImageView(new Image("res/images/CM_413.png", 25, 25, false, true));
		else if(i==4)
			icon = new ImageView(new Image("res/images/CM_414.png", 25, 25, false, true));
		
		box.getChildren().add(icon);
		box.setStyle("-fx-padding: 20 0 0 0;");
		
		return box;
	}
}