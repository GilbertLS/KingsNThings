package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class InputView extends HBox {
	private Label title = new Label("INPUT:");
	private TextField input = new TextField();
	private Button submit = new Button("Submit");
	
	public InputView(){
		getChildren().addAll(title, input);
		getChildren().add(submit);
		this.getStyleClass().add("input");
		
		//Setting an action for the Submit button
		submit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
		    public void handle(ActionEvent e) {
				if(input.getText() != null)
				{
					GameView gv = ((GameView)getScene());
					gv.inputTextUpdated = true;
				}
			}
		});
	}

	public String getInput() {
		return input.getText();	
	}
}