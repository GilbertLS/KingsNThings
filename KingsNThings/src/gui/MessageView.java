package gui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MessageView extends HBox {
	private Label title = new Label("INFO:  ");
	private Text message = new Text("");
	private String currMessage = "";
		
	public MessageView(){
		this.message.setWrappingWidth(270);
		this.message.setId("message-text");
		getChildren().add(title);
		getChildren().add(message);
		this.getStyleClass().add("message");
	}
	
	public String GetCurrentMessage(){
		return currMessage;
	}

	public void displayMessage(String message) {
		this.message.setText(message);
		currMessage = message;
		
	}

	public void clearMessage() {
		this.message.setText("");
		
	}
}
