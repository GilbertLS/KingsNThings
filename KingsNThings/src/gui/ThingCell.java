package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
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
	ThingCell thisCell = this;

	public ThingCell() {
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
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

				ObservableList<ThingView> items = getListView().getItems();

				Dragboard db = startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				content.putString(String.valueOf(items.indexOf(getItem())));
				db.setContent(content);

				//db.setDragView(arg0);
				e.consume();;
			}
		});

		setOnDragOver(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {				if (e.getGestureSource() != thisCell &&						e.getDragboard().hasString()) {					e.acceptTransferModes(TransferMode.MOVE);				}
				e.consume();
			}		});
		setOnDragEntered(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {				if (e.getGestureSource() != thisCell &&						e.getDragboard().hasString()) {					setOpacity(0.3);				}
			}		});
		setOnDragExited(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {				if (e.getGestureSource() != thisCell &&						e.getDragboard().hasString()) {					setOpacity(1);				}
			}		});
		/*setOnDragDropped(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {
				if (getItem() == null) {
					return;
				}

				Dragboard db = e.getDragboard();
				boolean success = false;

				if (db.hasString()) {
					ObservableList<ThingView> items = getListView().getItems();
					int draggedIdx 					= Integer.parseInt(db.getString());
					int thisIdx 					= items.indexOf(getItem());
					ThingView draggedItem 			= items.get(draggedIdx);

					items.set(draggedIdx, getItem());
					items.set(thisIdx, draggedItem);

					List<ThingView> itemscopy = new ArrayList<ThingView>(getListView().getItems());
					getListView().getItems().setAll(items);

					success = true;
				}
				e.setDropCompleted(success);

				e.consume();
			}
		});*/

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