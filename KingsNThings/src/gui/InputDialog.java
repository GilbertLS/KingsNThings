package gui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialog;

public class InputDialog extends Dialog {
	TextField inputField = new TextField("0");
	
	InputDialog(Stage s, String title, String message) {
		super(s, title, true, false);
		setup(message);
	}
		
	private void setup(String message) {
		final GridPane content = new GridPane();
		content.setHgap(10);
	    content.setVgap(10);
	    
	    content.add(new Label(message), 0, 0);
	    content.add(inputField, 0, 1);
	    GridPane.setHgrow(inputField, Priority.ALWAYS);
		
	    this.setResizable(false);
	    this.setIconifiable(false);
	    this.getActions().addAll(Dialog.Actions.OK);
	    this.setContent(content);
	    this.setClosable(false);
	}
	
	public String showInput() {
	     Platform.runLater(new Runnable() {
	         public void run() {
	        	 inputField.requestFocus();
	         }
	     });
		return this.show() == Dialog.Actions.OK ? inputField.getText() : null;
	}
}
