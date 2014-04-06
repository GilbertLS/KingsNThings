package gui;

import java.util.ArrayList;

import org.controlsfx.dialog.Dialog;

import Game.GameConstants.ControlledBy;
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
		
		VBox vbox = new VBox();
		vbox.getChildren().add(addThingBox);
		vbox.getChildren().add(removeThingBox);
		vbox.getChildren().add(setPhaseBox);
		vbox.getChildren().add(executeCommandButton);
		
		stackPane.getChildren().add(vbox);
	}
	
	private VBox CreateAddThingView(ToggleGroup toggleGroup) {
		VBox addThingBox = new VBox();
		
		addThingButton = new RadioButton("Add thing");
		addThingButton.setToggleGroup(toggleGroup);
		
		addThingBox.getChildren().add(addThingButton);
		
		ArrayList<ThingView> thingViews = new ArrayList<ThingView>();
		ArrayList<Thing> things = GameClient.game.gameModel.getAllPlayingCupCreatures();
		for(Thing t : things) {
			t.setFlipped(false);
			thingViews.add(new ThingView(t));
		}
		
		ThingViewList thingViewList = new ThingViewList(
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
}
