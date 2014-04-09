package gui;

import java.util.ArrayList;

import org.controlsfx.dialog.Dialog;

import Game.GameConstants.ControlledBy;
import Game.GameConstants.SetOption;
import Game.GameConstants.Terrain;
import Game.Phases;
import Game.Thing;
import Game.Phases.Phase;
import Game.Networking.GameClient;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
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

public class EditStateWindow extends Dialog {
	public Scene scene;
	private Button executeCommandButton;
	
	private EditStateWindow _this;
	public RadioButton removeThingButton;
	public RadioButton setPhaseButton;
	public RadioButton addThingButton;
	public RadioButton setHexButton;
	public RadioButton setHexTerrainButton;
	public RadioButton setStateButton;
	public ThingViewList thingViewList;
	
	public ChoiceBox<Phase> changePhaseDropDown;
	public ChoiceBox<ControlledBy> controlledByDropDown;
	public ChoiceBox<ControlledBy> setHexControlledByDropDown;
	public ChoiceBox<SetOption> setOptionDropDown;
	public ChoiceBox<Terrain> setHexTerrainDropDown;
	public ChoiceBox<FunctionalityStates> setStateDropDown;
	
	public enum FunctionalityStates {
		MINIMAL,
		AVERAGE,
		SUPERIOR,
		OUTSTANDING1,
		OUTSTANDING2
	}
	
	public EditStateWindow() {
		super(null, "Edit State Menu", false, false);
		StackPane stackPane = new StackPane();
		this.setContent(stackPane);
		
		
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
		VBox setTileBox = CreateSetTileView(toggleGroup);
		VBox setHexTypeBox = CreateHexTypeBox(toggleGroup);
		VBox setFunctionalityState = CreateFunctionalityBox(toggleGroup);
		
		VBox vbox = new VBox();
		vbox.getChildren().add(addThingBox);
		vbox.getChildren().add(removeThingBox);
		vbox.getChildren().add(setPhaseBox);
		vbox.getChildren().add(setTileBox);
		vbox.getChildren().add(setHexTypeBox);
		vbox.getChildren().add(setFunctionalityState);
		vbox.getChildren().add(executeCommandButton);
		
		stackPane.getChildren().add(vbox);
	}

	private VBox CreateAddThingView(ToggleGroup toggleGroup) {
		VBox addThingBox = new VBox();
		
		addThingButton = new RadioButton("Add thing");
		addThingButton.setToggleGroup(toggleGroup);
		
		addThingBox.getChildren().add(addThingButton);
		
		ArrayList<ThingView> thingViews = new ArrayList<ThingView>();
		ArrayList<Thing> things = GameClient.game.gameModel.getAllTestPlayingCupCreatures();
		for(Thing t : things) {
			t.setFlipped(false);
			thingViews.add(new ThingView(t));
		}
		
		thingViewList = new ThingViewList(
			FXCollections.observableArrayList(thingViews)
		);
		
		HBox hbox = new HBox();
		hbox.getChildren().add(thingViewList);
		addThingBox.getChildren().add(hbox);
		
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
	
	private VBox CreateSetTileView(ToggleGroup toggleGroup) {
		VBox setTileBox = new VBox();
		
		setHexButton = new RadioButton("Set hex");
		setHexButton.setToggleGroup(toggleGroup);
		
		Label controlledByLabel = new Label("Controlled By: ");
		setHexControlledByDropDown = new ChoiceBox<ControlledBy>(FXCollections.observableArrayList(
			ControlledBy.PLAYER1, ControlledBy.PLAYER2, ControlledBy.PLAYER3, ControlledBy.PLAYER4,
			ControlledBy.NEUTRAL
		));
		setHexControlledByDropDown.getSelectionModel().selectFirst();
		HBox controlledByBox = new HBox();
		controlledByBox.getChildren().add(controlledByLabel);
		controlledByBox.getChildren().add(setHexControlledByDropDown);
		HBox.setMargin(controlledByLabel, new Insets(0, 0, 0, 20));
		
		Label setOptionLabel = new Label("Set option: ");
		setOptionDropDown = new ChoiceBox<SetOption>(FXCollections.observableArrayList(
			SetOption.HEX, SetOption.TOWER, SetOption.KEEP, SetOption.CASTLE, SetOption.CITADEL
		));
		setOptionDropDown.getSelectionModel().selectFirst();
		HBox setOptionBox = new HBox();
		setOptionBox.getChildren().add(setOptionLabel);
		setOptionBox.getChildren().add(setOptionDropDown);
		HBox.setMargin(setOptionLabel, new Insets(0, 0, 0, 20));
		
		setTileBox.getChildren().add(setHexButton);
		setTileBox.getChildren().add(controlledByBox);
		setTileBox.getChildren().add(setOptionBox);
		
		return setTileBox;
	}
	
	private VBox CreateHexTypeBox(ToggleGroup toggleGroup) {
		VBox setTileBox = new VBox();
		
		setHexTerrainButton = new RadioButton("Set hex terrain");
		setHexTerrainButton.setToggleGroup(toggleGroup);
		
		Label setTerrainLabel = new Label("Set hex type: ");
		setHexTerrainDropDown = new ChoiceBox<Terrain>(FXCollections.observableArrayList(
			Terrain.DESERT, Terrain.FOREST, Terrain.FROZEN_WASTE, Terrain.JUNGLE,
			Terrain.MOUNTAIN, Terrain.PLAINS, Terrain.SEA, Terrain.SWAMP
		));
		setHexTerrainDropDown.getSelectionModel().selectFirst();
		HBox setOptionBox = new HBox();
		setOptionBox.getChildren().add(setTerrainLabel);
		setOptionBox.getChildren().add(setHexTerrainDropDown);
		HBox.setMargin(setTerrainLabel, new Insets(0, 0, 0, 20));
		
		setTileBox.getChildren().add(setHexTerrainButton);
		setTileBox.getChildren().add(setOptionBox);
		
		return setTileBox;
	}
	
	private VBox CreateFunctionalityBox(ToggleGroup toggleGroup) {
		VBox setStateBox = new VBox();
		
		setStateButton = new RadioButton("Set game state");
		setStateButton.setToggleGroup(toggleGroup);
		
		Label setStateLabel = new Label("Set staet: ");
		setStateDropDown = new ChoiceBox<FunctionalityStates>(FXCollections.observableArrayList(
			FunctionalityStates.MINIMAL, FunctionalityStates.AVERAGE, FunctionalityStates.SUPERIOR,
			FunctionalityStates.OUTSTANDING1, FunctionalityStates.OUTSTANDING2
		));
		setStateDropDown.getSelectionModel().selectFirst();
		HBox setStateHBox = new HBox();
		setStateHBox.getChildren().add(setStateLabel);
		setStateHBox.getChildren().add(setStateDropDown);
		HBox.setMargin(setStateLabel, new Insets(0, 0, 0, 20));
		
		setStateBox.getChildren().add(setStateButton);
		setStateBox.getChildren().add(setStateHBox);
		
		return setStateBox;
	}


}
