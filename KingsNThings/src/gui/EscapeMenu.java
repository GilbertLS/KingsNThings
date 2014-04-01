package gui;

import org.controlsfx.dialog.Dialog;

import com.sun.glass.events.MouseEvent;

import Game.Networking.GameClient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EscapeMenu extends Dialog {
	
	private int boxWidth = 200;
	private int menuWidth = 250;
	private int menuHeight = 200;
	
	private Button returnButton;
	private Button editMenuButton;
	private StackPane stackPane;
	
	private EditStateWindow editWindow;

	public EscapeMenu() {
		super(null, "Menu");
		stackPane = new StackPane();		
		this.setContent(stackPane);
		
		final EscapeMenu _this = this;
		
		VBox box = new VBox();
		returnButton = new Button("Return to game");
		
		returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _this.hide();
            }
        });
		
		
		EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.M) {
					_this.hide();
				}
			}
        };
        
        //scene.setOnKeyPressed(keyPressed);
        
        returnButton.setMinWidth(boxWidth);
        returnButton.setMaxWidth(boxWidth);
        box.getChildren().add(returnButton);

		editMenuButton = new Button("Edit the state of the game");
		editMenuButton.setMinWidth(boxWidth);
		editMenuButton.setMaxWidth(boxWidth);
		
		editWindow = new EditStateWindow();
		editMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editWindow.show();
            }
        });
		
		box.getChildren().add(editMenuButton);
		
		stackPane.getChildren().add(box);
	}

	public void hideMenu() {
		editWindow.hide();
		this.hide();
	}
}
