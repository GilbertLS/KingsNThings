package gui;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class ThingCell extends ListCell<ThingView> implements Draggable {
	ThingCell thisCell = this;

	public ThingCell() {
		setAlignment(Pos.CENTER);
		initListeners();
	}

	protected void initListeners()
	{	
			
		setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent e) {
				if (getItem() == null) {
					return;
				}

				ArrayList<Integer> selectedIds = new ArrayList<Integer>(getListView().getSelectionModel().getSelectedIndices());
				
				Dragboard db = startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				content.put(thingRackIds, selectedIds);
				db.setContent(content);

				//db.setDragView(arg0);
				e.consume();;
			}
		});

		/* IMPLEMENT RACK REORDERING? */
		/*setOnDragOver(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {				if (e.getGestureSource() != thisCell &&						e.getDragboard().hasString()) {					e.acceptTransferModes(TransferMode.MOVE);				}
				e.consume();
			}		});
		setOnDragEntered(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {				if (e.getGestureSource() != thisCell &&						e.getDragboard().hasString()) {					setOpacity(0.3);				}
			}		});
		setOnDragExited(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {				if (e.getGestureSource() != thisCell &&						e.getDragboard().hasString()) {					setOpacity(1);				}
			}		});

		setOnDragDropped(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {
				if (getItem() == null) {
					return;
				}

				Dragboard db = e.getDragboard();
				boolean success = false;

				if (db.hasString()) {
					//STUFF
				}
				e.setDropCompleted(success);

				e.consume();
			}
		});*/

	}

	@Override
	protected void updateItem(ThingView t, boolean b) {
		super.updateItem(t, b);

		if (t == null) {
			setStyle("");
		} else {
			setPrefSize(64, 64);
			setStyle("-fx-background-image: url('res/images/T_Back.png'); -fx-background-repeat: stretch; -fx-background-position: center center;");
		}
	}
}