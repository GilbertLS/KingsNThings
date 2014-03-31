package gui;

import javafx.scene.control.ScrollPane;

public class BoardScrollPane extends ScrollPane {
	BoardView board;

	BoardScrollPane(BoardView b) {
		board = b;
		this.setStyle("-fx-background: rgba(0,0,0,0);");
		this.setContent(board);
	}
}
