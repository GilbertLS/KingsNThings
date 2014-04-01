package gui;

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.dialog.Dialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Game.Combatant;
import Game.Dice;
import Game.GameConstants;
import Game.HexTile;
import Game.SpecialCharacter;
import Game.Thing;
import Game.Treasure;
import Game.Networking.GameClient;

public class BribeView extends Dialog {
	BribeView self = this;
	VBox viewBox = new VBox();
	Label defendingCreaturesLabel = new Label("Defending Creatrues");
	ThingViewList defenseCreaturesList;
	Label treasureLabel = new Label("Tile has no Treasure");
	ThingViewList treasureList;
	
	VBox interactionBox = new VBox();
	HBox buttonBox = new HBox();
	Button bribe = new Button("Bribe Creatures");
	Button endBribe = new Button("End Bribing");
	
	HBox infoBox = new HBox();
	Label info = new Label("INFO: ");
	
	HexTile referenceHexTile;
	Tile t;
	boolean hasTreasure = false;
	BorderPane root = null;
	
	ObservableList<Node> children;
		
	public BribeView(Stage stage, Tile t){
		super(stage, "Bribe Defending Creatrues", true, false);
		
	    referenceHexTile = t.getTileRef();
	    this.t = t;
	    hasTreasure = t.getTileRef().hasTreasure();
	    
	    root = new BorderPane();
			
	    getStylesheets().add("gui/main.css");

	    //add defense creatures (with settlements)
	    children = viewBox.getChildren();
	    defenseCreaturesList = new ThingViewList(FXCollections.observableList(t.defendingThings));
	    if(referenceHexTile.hasSettlement())
	    	defenseCreaturesList.add(new ThingView(referenceHexTile.getSettlement()));
	    children.add(defendingCreaturesLabel);
	    children.add(defenseCreaturesList);
	    
	    //add treasures if applicable
	    if(hasTreasure){
	    	ArrayList<ThingView> treasureThingViews = new ArrayList<ThingView>();
	    	for(Treasure treasure: referenceHexTile.getTreasures())
	    		treasureThingViews.add(new ThingView(treasure));
	    		
	    	treasureList = new ThingViewList(FXCollections.observableList(treasureThingViews));
		    treasureLabel = new Label("Tile Treasure:");
		    children.add(treasureLabel);
		    children.add(treasureList);
	    } else{
	    	children.add(treasureLabel);
	    }
	    viewBox.setPadding(new Insets(20,5,20,5));
	    root.setTop(viewBox);
	    
	    //add info pane
	    if(t.getTileRef().isBribeDoubled())
	    {
	    	info.setText("INFO: Bribes are doubled!!!");
	    }
	    children = infoBox.getChildren();
	    children.add(info);
	    
	    //add buttons
	    children = buttonBox.getChildren();
	    children.add(bribe);
	    children.add(endBribe);
	    
	    children = interactionBox.getChildren();
	    children.add(infoBox);
	    children.add(buttonBox);
	    
	    interactionBox.setPadding(new Insets(20,5,20,5));
	    root.setBottom(interactionBox);
	    
	    //init buttons
	    initListeners();
	      
	    this.setResizable(false);
	    this.setContent(root);
	    this.setClosable(false);
	    this.show();
	}
	
	public void initListeners()
	{
		bribe.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				
				List<ThingView> selectedThingViews = defenseCreaturesList.getSelectionModel().getSelectedItems();
				ArrayList<Thing> selectedThings = new ArrayList<Thing>();
				int playerIndex = GameClient.game.gameView.currPlayerNum;
				
				for(int i=0; i<selectedThingViews.size(); i++){
						selectedThings.add(selectedThingViews.get(i).thingRef);
				}
				
				if(GameClient.game.validBribe(selectedThings, referenceHexTile)){					
					//need to handle special incomes as well
					GameClient.game.gameModel.handleBribe(referenceHexTile, selectedThings, playerIndex);
					GameClient.game.gameView.updateTiles(referenceHexTile, 4);
					
					int gold = GameClient.game.gameModel.GetCurrentPlayer().getGold();
					GameClient.game.gameView.updateGold(gold, playerIndex);
					
					GameClient.game.gameView.tilePreview.changeTile(t);
					
					GameClient.game.sendBribeCreaturesEvent(referenceHexTile, selectedThings, playerIndex);	
					
					self.hide();
					GameClient.game.gameView.endBribeCreatures();
				}
					
				else
					info.setText("INFO: Invalid Bribe, please try again");
			}
		});
		
		endBribe.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				self.hide();
				GameClient.game.gameView.endBribeCreatures();
			}
		});
	}
}
