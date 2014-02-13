package gui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MessageView extends VBox {
	private Label title = new Label("INFO:");
	private Label message = new Label("");
	private String currMessage = "";
	
	public String GetCurrentMessage(){
		return currMessage;
	}
	
	public MessageView(){
		getChildren().add(title);
		getChildren().add(message);
		this.getStyleClass().add("message");
	}

	public void displayMessage(String message) {
		this.message.setText("\t"+message);
		currMessage = message;
		
	}

	public void clearMessage() {
		this.message.setText("");
		
	}
}
