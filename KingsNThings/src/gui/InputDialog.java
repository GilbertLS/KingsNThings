package gui;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.ButtonBar.ButtonType;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

public class InputDialog extends Dialog {
	TextField inputField = new TextField();
	
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
	    
		this.setContent(content);
	}
	
	public String showInput() {
		return this.show() == Dialog.Actions.OK ? inputField.getText() : null;
	}
}
