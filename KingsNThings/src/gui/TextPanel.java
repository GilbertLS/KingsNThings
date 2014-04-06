package gui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TextPanel extends HBox {
	Label text = new Label();
	
	TextPanel(String s) {
		text.setText(s);
		this.getChildren().add(text);
		this.getStyleClass().add("text-panel");
		text.setStyle("-fx-text-fill: white;");
	}
}
