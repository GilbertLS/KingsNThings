package gui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class RackView extends ThingViewList {	
	RackView(ObservableList<ThingView> l) {
		super(l);
		
		view.getChildrenUnmodifiable().addListener(new ListChangeListener() {
	        @Override
	        public void onChanged(Change change) {
	        	if (view.getItems().size() <= 10)
	        		view.setPrefWidth(64.25 * 10);
	        	else
	        		view.setPrefWidth((view.getItems().size() * 64.5));
	        }
		});
	}
}
