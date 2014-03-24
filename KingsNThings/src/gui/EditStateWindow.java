package gui;

import Game.GameConstants.ControlledBy;
import Game.GameConstants.Terrain;
import Game.Phases;
import Game.Phases.Phase;
import Game.Networking.GameClient;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EditStateWindow extends Stage {
	public Scene scene;
	private Button executeCommandButton;
	
	private EditStateWindow _this;
	public RadioButton magicButton;
	public RadioButton rangedButton;
	public RadioButton flyingButton;
	public RadioButton chargeButton;
	public ChoiceBox<Terrain> hexTypeDropDown;
	public ChoiceBox<Integer> combatValueDropDown;
	public RadioButton removeThingButton;
	public RadioButton setPhaseButton;
	public RadioButton addThingButton;
	public ChoiceBox<Phase> changePhaseDropDown;
	public ChoiceBox<ControlledBy> controlledByDropDown;
	
	public EditStateWindow(GameView g) {
		StackPane stackPane = new StackPane();
		scene = new Scene(stackPane);
		
		this.setScene(scene);
		this.setWidth(450);
		this.setHeight(250);
		
		_this = this;
		
		executeCommandButton = new Button();
		executeCommandButton.setText("Execute command");
		executeCommandButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameClient.game.parseEdit(_this);
            }
        });
		
		ToggleGroup toggleGroup = new ToggleGroup();
		
		VBox addThingBox = CreateAddThingView(toggleGroup);
		VBox removeThingBox = CreateRemoveThingView(toggleGroup);
		VBox setPhaseBox = CreateSetPhaseView(toggleGroup);
		
		VBox vbox = new VBox();
		vbox.getChildren().add(addThingBox);
		vbox.getChildren().add(removeThingBox);
		vbox.getChildren().add(setPhaseBox);
		vbox.getChildren().add(executeCommandButton);
		
		stackPane.getChildren().add(vbox);
		
		this.setTitle("Edit State");
		this.initStyle(StageStyle.UTILITY);
		this.initModality(Modality.APPLICATION_MODAL);
		this.initOwner(g.getWindow());
	}
	
	private VBox CreateAddThingView(ToggleGroup toggleGroup) {
		VBox addThingBox = new VBox();
		
		addThingButton = new RadioButton("Add thing");
		addThingButton.setToggleGroup(toggleGroup);
		
		// Combat value box
		HBox combatValueHBox = new HBox();
		combatValueDropDown = new ChoiceBox<Integer>(FXCollections.observableArrayList(
			1, 2, 3, 4, 5, 6
		));
		combatValueDropDown.getSelectionModel().selectFirst();
		Label combatLabel = new Label("Combat Value: ");
		combatValueHBox.getChildren().add(combatLabel);
		combatValueHBox.getChildren().add(combatValueDropDown);
		HBox.setMargin(combatLabel, new Insets(0, 0, 0, 20));
		
		// Hex type box
		HBox hexTypeHBox = new HBox();
		hexTypeDropDown = new ChoiceBox<Terrain>(FXCollections.observableArrayList(
			Terrain.DESERT,   Terrain.FOREST, Terrain.FROZEN_WASTE, Terrain.JUNGLE, 
			Terrain.MOUNTAIN, Terrain.PLAINS, Terrain.SEA, 			Terrain.SWAMP
		));
		hexTypeDropDown.getSelectionModel().selectFirst();
		Label hexTypeLabel = new Label("Hex Type: ");
		hexTypeHBox.getChildren().add(hexTypeLabel);
		hexTypeHBox.getChildren().add(hexTypeDropDown);
		HBox.setMargin(hexTypeLabel, new Insets(0, 0, 0, 20));
		
		// Creature type box
		Label creatureType = new Label("Creature Type: ");
		ToggleGroup creatureButtons = new ToggleGroup();
		magicButton = new RadioButton("Magic");
		magicButton.setToggleGroup(creatureButtons);
		rangedButton = new RadioButton("Ranged");
		rangedButton.setToggleGroup(creatureButtons);
		flyingButton = new RadioButton("Flying");
		chargeButton = new RadioButton("Charge");
		HBox creatureTypeHBox = new HBox();
		creatureTypeHBox.getChildren().add(creatureType);
		creatureTypeHBox.getChildren().add(magicButton);
		creatureTypeHBox.getChildren().add(rangedButton);
		creatureTypeHBox.getChildren().add(chargeButton);
		creatureTypeHBox.getChildren().add(flyingButton);
		HBox.setMargin(creatureType, new Insets(0, 0, 0, 20));
		
		// Controlled by box
		Label controlledByLabel = new Label("Controlled By: ");
		controlledByDropDown = new ChoiceBox<ControlledBy>(FXCollections.observableArrayList(
			ControlledBy.PLAYER1, ControlledBy.PLAYER2, ControlledBy.PLAYER3, ControlledBy.PLAYER4,
			ControlledBy.NEUTRAL
		));
		controlledByDropDown.getSelectionModel().selectFirst();
		HBox controlledByBox = new HBox();
		controlledByBox.getChildren().add(controlledByLabel);
		controlledByBox.getChildren().add(controlledByDropDown);
		HBox.setMargin(controlledByLabel, new Insets(0, 0, 0, 20));
		
		addThingBox.getChildren().add(addThingButton);
		addThingBox.getChildren().add(combatValueHBox);
		addThingBox.getChildren().add(hexTypeHBox);
		addThingBox.getChildren().add(creatureTypeHBox);
		addThingBox.getChildren().add(controlledByBox);
		
		return addThingBox;
	}
	
	private VBox CreateRemoveThingView(ToggleGroup toggleGroup) {
		VBox removeThingBox = new VBox();
		
		removeThingButton = new RadioButton("Remove thing");
		removeThingButton.setToggleGroup(toggleGroup);
		
		removeThingBox.getChildren().add(removeThingButton);
		
		return removeThingBox;
	}
	
	private VBox CreateSetPhaseView(ToggleGroup toggleGroup) {
		VBox setPhaseBox = new VBox();

		setPhaseButton = new RadioButton("Set phase");
		setPhaseButton.setToggleGroup(toggleGroup);
		
		// Changing phase
		HBox phaseBox = new HBox();
		changePhaseDropDown = new ChoiceBox<Phase>(FXCollections.observableArrayList(
			Phase.BATTLE, Phase.CONSTRUCTION, Phase.MOVE_THINGS, Phase.PLAY_THINGS,
			Phase.RECRUIT_SPECIAL_CHARACTERS, Phase.RECRUIT_THINGS, Phase.SETUP
		));
		changePhaseDropDown.getSelectionModel().selectFirst();
		Label changePhaseLabel = new Label("Phase: ");
		phaseBox.getChildren().add(changePhaseLabel);
		phaseBox.getChildren().add(changePhaseDropDown);
		HBox.setMargin(changePhaseLabel, new Insets(0, 0, 0, 20));
		
		setPhaseBox.getChildren().add(setPhaseButton);
		setPhaseBox.getChildren().add(phaseBox);
		
		return setPhaseBox;
	}
}
