package gui;

import java.util.ArrayList;

import org.controlsfx.dialog.Dialog;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import Game.SpecialCharacter;
import Game.Networking.GameClient;

public class SpecialCharacterView extends Dialog{
	SpecialCharacterView self = this;
	ArrayList<SpecialCharacter> specialCharacters;
	int numSCs;
	int playerIndex;
	int rollAugmentation = 0;
	int[] rolls;
	ThingViewList specialCharacterList;
	HBox specialCharacterListBox;
	HBox buttonBox;
	HBox labelBox;
	VBox infoBox;
	Button selectCharacter = new Button("Select Character");
	Button attemptRoll = new Button("Attempt Roll");
	Button endRecruitment = new Button("End Recruitment");
	Button augmentRoll = new Button("Augment Roll");
	private Label rollLabel	= new Label("Roll:");
	private Label rollAugmentationLabel = new Label("Augmentation:");
	private Label requiredRollLabel = new Label("Required Roll:");
	private Label rollTotalLabel = new Label("Total Roll:");
	ThingView selection;
	boolean diceRolled = false;
	
	public SpecialCharacterView(Stage stage, int numSCs, int playerIndex){
		super(stage, "Special Character Recruitment", true, false);
		
		specialCharacterListBox = new HBox();
		BorderPane root = new BorderPane();
		
		root.setTop(specialCharacterListBox);
		
		infoBox = new VBox();
		buttonBox = new HBox();
		labelBox = new HBox();
		
		infoBox.getChildren().add(buttonBox);
		infoBox.getChildren().add(labelBox);
		root.setBottom(infoBox);
		
		buttonBox.getChildren().add(selectCharacter);
		buttonBox.getChildren().add(endRecruitment);
		
		rollLabel.setPadding(new Insets(5,20,5,20));
		rollLabel.setTextFill(Color.BLACK);
		labelBox.getChildren().add(rollLabel);
		
		rollAugmentationLabel.setPadding(new Insets(5,20,5,20));
		rollAugmentationLabel.setTextFill(Color.BLACK);
		labelBox.getChildren().add(rollAugmentationLabel);
		
		rollTotalLabel.setPadding(new Insets(5,20,5,20));
		rollTotalLabel.setTextFill(Color.BLACK);
		labelBox.getChildren().add(rollTotalLabel);
		
		requiredRollLabel.setPadding(new Insets(5,20,5,20));
		requiredRollLabel.setTextFill(Color.BLACK);
		labelBox.getChildren().add(requiredRollLabel);
		
		specialCharacters = GameClient.game.gameModel.getUnownedSpecialCharacters();
		this.numSCs = numSCs;
		this.playerIndex = playerIndex;
		rolls = new int[2];
		rolls[0] = 0;
		rolls[1] = 0;
		
        getStylesheets().add("gui/myStyle.css");
        
        setupThingViews();
        initListeners();
        
        this.setResizable(false);
        this.setContent(root);
        this.setClosable(false);
        this.show();
	}

	private void setupThingViews() {
		ArrayList<ThingView> specialCharacterViews = new ArrayList<ThingView>();
		
		for(SpecialCharacter sc: specialCharacters)
		{
			specialCharacterViews.add(new ThingView(sc));
		}
		
		specialCharacterList = new ThingViewList(FXCollections.observableList(specialCharacterViews));
		specialCharacterList.setPrefSize(500, 75);
		
		specialCharacterListBox.getChildren().add(specialCharacterList);
	}
	
	public void initListeners()
	{
		selectCharacter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
				ArrayList<Integer> selectedIDs = new ArrayList<Integer>(specialCharacterList.getSelectionModel().getSelectedIndices());
				
				if(selectedIDs.size() == 1)
				{
					selection = specialCharacterList.getItems().get(selectedIDs.get(0));
					
					specialCharacterListBox.getChildren().clear();
					
					specialCharacterList.getItems().removeAll();					
					ArrayList<ThingView> selectedList = new ArrayList<ThingView>();
					selectedList.add(selection);					
					specialCharacterList = new ThingViewList(FXCollections.observableList(selectedList));
					specialCharacterList.setPrefSize(500, 75);
					
					specialCharacterListBox.getChildren().add(specialCharacterList);
					
					buttonBox.getChildren().remove(selectCharacter);
					buttonBox.getChildren().add(0, attemptRoll);
					buttonBox.getChildren().add(0, augmentRoll);
					requiredRollLabel.setText("Required Roll:" + 2*((Combatant)selection.thingRef).GetCombatValue());
				}
			}
		});
		
		augmentRoll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
				if(!diceRolled)
				{
					//augment before roll
					if(GameClient.game.gameModel.canAfford(GameConstants.SPECIAL_CHARACTER_AUG_ROLL_BEFORE_COST, playerIndex))
						augmentRoll(GameConstants.SPECIAL_CHARACTER_AUG_ROLL_BEFORE_COST, playerIndex);
				}					
				else //augment after roll
				{
					if(GameClient.game.gameModel.canAfford(GameConstants.SPECIAL_CHARACTER_AUG_ROLL_AFTER_COST, playerIndex))
						augmentRoll(GameConstants.SPECIAL_CHARACTER_AUG_ROLL_AFTER_COST, playerIndex);
				}
			}

			private void augmentRoll(int amount, int playerIndex) {
				GameClient.game.augmentRoll(amount, playerIndex);
				GameClient.game.sendSpendGoldEvent(amount, playerIndex);
				rollAugmentation++;
				rollAugmentationLabel.setText("Augmentation:"+rollAugmentation);
				rollTotalLabel.setText("Total Roll:" + (rollAugmentation + rolls[0] + rolls[1]));
			}
		});
		

		
		attemptRoll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
				rolls = Dice.rollDice(2);
				buttonBox.getChildren().remove(attemptRoll);
				rollLabel.setText("Roll:"+(rolls[0]+rolls[1]));
				rollTotalLabel.setText("Total Roll:" + (rollAugmentation + rolls[0] + rolls[1]));
				diceRolled = true;
			}
		});
		
		endRecruitment.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
				int totalRoll =0;
				
				if(diceRolled)
				{
					totalRoll += Dice.getRoll(0) + Dice.getRoll(1);
				
					totalRoll += rollAugmentation;
					
					if(totalRoll >= ((Combatant) selection.thingRef).GetCombatValue()*2)
					{
						GameClient.game.sendRecruitSpecialCharacterEvent(selection.thingRef.thingID, playerIndex);
						GameClient.game.gameView.addToRack(selection.thingRef);
					}
				}
					
				self.hide();
				GameClient.game.gameView.endRecruitSpecialCharacter();
			}
		});
	}
}
