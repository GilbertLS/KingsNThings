package gui;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

public class BoardScrollPane extends ScrollPane {
	BoardView board;
	StackPane centeredPane;

	BoardScrollPane(BoardView b) {
		board = b;
		this.setStyle("-fx-background: rgba(0,0,0,0);");
		
		centeredPane = new StackPane();
	    centeredPane.setStyle("-fx-border-color: red");
	    centeredPane.getChildren().add(board);
	    StackPane.setAlignment(board, Pos.CENTER);
	    
	    this.setContent(centeredPane);
	}
}
