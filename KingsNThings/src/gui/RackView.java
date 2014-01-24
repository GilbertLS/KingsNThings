package gui;

import javafx.scene.layout.HBox;

public class RackView extends HBox {
	
	RackView() {
		this.getStyleClass().add("rack");
		this.setPrefSize(400, 50);
	}
}
