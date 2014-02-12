package gui;

import java.util.ArrayList;

import Game.Creature;
import Game.GameConstants;
import Game.HexTile;
import Game.Thing;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;

public class TilePreview extends VBox {
	ThingViewList view1 = 		new ThingViewList(FXCollections.observableList(new ArrayList<ThingView>()));
	ThingViewList view2 = 		new ThingViewList(FXCollections.observableList(new ArrayList<ThingView>()));
	ThingViewList view3 = 		new ThingViewList(FXCollections.observableList(new ArrayList<ThingView>()));
	ThingViewList view4 = 		new ThingViewList(FXCollections.observableList(new ArrayList<ThingView>()));
	ThingViewList viewNeutral = new ThingViewList(FXCollections.observableList(new ArrayList<ThingView>()));
	Tile tileRef;
	
	TilePreview() {
		for(int i = 0; i < 10; i++) {
			view1.add(new ThingView(new Creature(GameConstants.Terrain.DESERT)));
			view2.add(new ThingView(new Creature(GameConstants.Terrain.DESERT)));
			view3.add(new ThingView(new Creature(GameConstants.Terrain.DESERT)));
			view4.add(new ThingView(new Creature(GameConstants.Terrain.DESERT)));
		}
		
		view1.getStyleClass().add("preview-list");
		view2.getStyleClass().add("preview-list");
		view3.getStyleClass().add("preview-list");
		view4.getStyleClass().add("preview-list");
		viewNeutral.getStyleClass().add("preview-list");
		
		this.getChildren().add(view1);
		this.getChildren().add(view2);
		this.getChildren().add(view3);
		this.getChildren().add(view4);
		
		this.setPrefSize(300,280);
	}
	
	public void show() {
		ArrayList<ThingViewList> list = new ArrayList<ThingViewList>();
		
		if (view1.getChildren().size() > 0)
			list.add(view1);
		if (view2.getChildren().size() > 0)
			list.add(view2);
		if (view3.getChildren().size() > 0)
			list.add(view3);
		if (view4.getChildren().size() > 0)
			list.add(view4);
		if (viewNeutral.getChildren().size() > 0)
			list.add(viewNeutral);

		this.getChildren().setAll(list);
	}
	
	public ThingViewList GetThingList(int playerNum){
		if( playerNum == 1 ){
			return view1;
		} else if ( playerNum == 2 ){
			return view2;
		} else if ( playerNum == 3){
			return view3;
		} else if ( playerNum == 4 ){
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
		viewNeutral = new ThingViewList(FXCollections.observableList(t.neutralThings));
		
		show();
	}
	
	public void changeTile(HexTile tile){
		ThingView thingView;
		
		ArrayList<ThingView> arr = new ArrayList<ThingView>();
		for (Thing thing : tile.player1Things){
			thingView = new ThingView(thing);
			
			arr.add(thingView);
		}
		view1 = new ThingViewList(FXCollections.observableList((arr)));
		
		arr = new ArrayList<ThingView>();
		for (Thing thing : tile.player2Things){
			thingView = new ThingView(thing);
			arr.add(thingView);
		}
		view2 = new ThingViewList(FXCollections.observableList((arr)));
		
		arr = new ArrayList<ThingView>();
		for (Thing thing : tile.player3Things){
			thingView = new ThingView(thing);
			arr.add(thingView);
		}
		view3 = new ThingViewList(FXCollections.observableList((arr)));
		
		arr = new ArrayList<ThingView>();
		for (Thing thing : tile.player4Things){
			thingView = new ThingView(thing);
			arr.add(thingView);
		}
		view4 = new ThingViewList(FXCollections.observableList((arr)));
		show();
	}
}