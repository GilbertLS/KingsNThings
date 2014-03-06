package gui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class RackView extends ThingViewList {	
	RackView(ObservableList<ThingView> l) {
		super(l);
		
		this.getChildrenUnmodifiable().addListener(new ListChangeListener() {
	        @Override
	        public void onChanged(Change change) {
	        	if (self.getItems().size() <= 10)
	        		self.setPrefWidth(64.25 * 10);
	        	else
	        		self.setPrefWidth((self.getItems().size() * 64.5));
	        }
		});
	}
}
