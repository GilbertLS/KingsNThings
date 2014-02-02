package gui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ButtonBox extends HBox {
	Button recruitThings = new Button("Recruit Things");
	Button recruitSpecial = new Button("Recruit Special Character");
	
	ButtonBox()
	{
		this.getChildren().add(recruitThings);
		this.getChildren().add(recruitSpecial);
		
		this.getStyleClass().add("button-box");
	}
}
