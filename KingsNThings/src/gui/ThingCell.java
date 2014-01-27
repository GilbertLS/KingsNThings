package gui;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class ThingCell extends ListCell<ThingView> {
	public ThingCell() {
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		setAlignment(Pos.CENTER);
		initListeners();
	}

	protected void initListeners()
	{
		this.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent event) {
				Dragboard db = startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				content.putString("Hello");
				db.setContent(content);
				event.consume();
			}
		});
		
		setOnDragOver(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent event) {
	            if (event.getGestureSource() != this &&
	                   event.getDragboard().hasString()) {
	                event.acceptTransferModes(TransferMode.MOVE);
	            }
	
	            event.consume();
			}
        });

        setOnDragEntered(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent event) {
	            if (event.getGestureSource() != this &&
	                    event.getDragboard().hasString()) {
	                setOpacity(0.3);
	            }
			}
        });

        this.setOnDragExited(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent event) {
	            if (event.getGestureSource() != this &&
	                    event.getDragboard().hasString()) {
	                setOpacity(1);
	            }
			}
        });
		
		
	}

	@Override
	protected void updateItem(ThingView t, boolean b) {
		super.updateItem(t, b);

		if (t != null) {
			getChildren().add(t);
			setPrefSize(64, 64);
			setStyle("-fx-margin: 100; -fx-padding: 1000; -fx-background-image: url('res/images/T_Back.png'); -fx-background-repeat: stretch; -fx-background-position: center center;");
		}
	}
}