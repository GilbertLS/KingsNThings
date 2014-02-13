package gui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

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
