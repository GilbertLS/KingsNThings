package gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

public class BoardScrollPane extends ScrollPane {
	BoardView board;
	StackPane centered;

	BoardScrollPane(BoardView b) {
		centered = new StackPane();
		board = b;
		this.setStyle("-fx-background: rgba(0,0,0,0);");
		this.setContent(centered);
		centered.getChildren().add(board);
	}
}
