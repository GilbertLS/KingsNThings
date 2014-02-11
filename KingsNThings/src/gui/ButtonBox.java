package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ButtonBox extends HBox {
	Button recruitThings = new Button("Recruit Things");
	Button recruitSpecial = new Button("Recruit Special Character");
	Button userInputComplete = new Button("Done Phase");
	
	ButtonBox()
	{
		this.getChildren().addAll(recruitThings, recruitSpecial);
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
	}
}
