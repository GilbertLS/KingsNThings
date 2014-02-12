package gui;

import java.util.ArrayList;

import Game.Creature;
import Game.GameConstants;
import javafx.collections.FXCollections;
import javafx.scene.layout.VBox;

public class TilePreview extends VBox {
	ThingViewList view1 = 		new ThingViewList(FXCollections.observableList(new ArrayList<ThingView>()));
	ThingViewList view2 = 		new ThingViewList(FXCollections.observableList(new ArrayList<ThingView>()));
	ThingViewList view3 = 		new ThingViewList(FXCollections.observableList(new ArrayList<ThingView>()));
	ThingViewList view4 = 		new ThingViewList(FXCollections.observableList(new ArrayList<ThingView>()));
	ThingViewList viewNeutral = new ThingViewList(FXCollections.observableList(new ArrayList<ThingView>()));
	Tile tileRef;
	int playerNum;
	
	TilePreview(int num) {		
		view1.getStyleClass().add("preview-list");
		view2.getStyleClass().add("preview-list");
		view3.getStyleClass().add("preview-list");
		view4.getStyleClass().add("preview-list");
		viewNeutral.getStyleClass().add("preview-list");
		
		this.getChildren().add(view1);
		this.getChildren().add(view2);
		this.getChildren().add(view3);
		this.getChildren().add(view4);
		this.getChildren().add(viewNeutral);
		
		this.playerNum = num;
		this.setSelectable();
		
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
		
	public void changeTile(Tile t) {
		tileRef = t;
		
		view1 = new ThingViewList(FXCollections.observableList(t.p1Things));
		view2 = new ThingViewList(FXCollections.observableList(t.p2Things));
		view3 = new ThingViewList(FXCollections.observableList(t.p3Things));
		view4 = new ThingViewList(FXCollections.observableList(t.p4Things));
		viewNeutral = new ThingViewList(FXCollections.observableList(t.neutralThings));
		
		setSelectable();
		show();
	}
	
	private void setSelectable() {
		if (this.playerNum != 0)
			view1.setOnMouseClicked(null);
		else if (this.playerNum != 1)
			view2.setOnMouseClicked(null);
		else if (this.playerNum != 2)
			view3.setOnMouseClicked(null);
		else if (this.playerNum != 3)
			view4.setOnMouseClicked(null);
		viewNeutral.setOnMouseClicked(null);
	}
	
	public void setPlayerNum(int i) {
		this.playerNum = i;
		this.setSelectable();
	}
}