package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
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
		//board.setStyle("-fx-border-color: blue");
	    //centeredPane.setStyle("-fx-border-color: red");
	    centeredPane.getChildren().add(board);
	    StackPane.setAlignment(board, Pos.CENTER);
	    
	    this.setContent(centeredPane);
	    this.viewportBoundsProperty().addListener(
	    	      new ChangeListener<Bounds>() {
	    	      @Override public void changed(ObservableValue<? extends Bounds> observableValue, Bounds oldBounds, Bounds newBounds) {
	    	    	  centeredPane.setPrefSize(
	    	          Math.max(board.getBoundsInParent().getMaxX(), newBounds.getWidth()),
	    	          Math.max(board.getBoundsInParent().getMaxY(), newBounds.getHeight())
	    	        );
	    	      }
	    	    });
	}
}
