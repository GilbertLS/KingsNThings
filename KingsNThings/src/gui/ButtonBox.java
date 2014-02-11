package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ButtonBox extends HBox {
	Button showHideTiles = new Button("Show Tiles");
	Button recruitSpecial = new Button("Recruit Special Character");
	Button userInputComplete = new Button("Done Phase");
	
	ButtonBox()
	{
		this.getChildren().addAll(showHideTiles, recruitSpecial);
		this.getChildren().add(userInputComplete);
		
		this.getStyleClass().add("button-box");
		
		//Setting an action for the Submit button
		userInputComplete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
		    public void handle(ActionEvent e) {
				GameView gv = ((GameView)getScene());
				gv.userInputDone = true;
			}
		});
		
		//Setting an action for the Submit button
		showHideTiles.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
		    public void handle(ActionEvent e) {
				if(showHideTiles.getText().equals("Show Tiles"))
				{
					showHideTiles.setText("Hide Tiles");
					GameView gv = (GameView)getScene();
					gv.showHideAllTiles(true);
				}
				else
				{
					showHideTiles.setText("Show Tiles");
					GameView gv = (GameView)getScene();
					gv.showHideAllTiles(false);	
				}
			}
		});
	}
}
