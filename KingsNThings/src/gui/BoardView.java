package gui;

import java.util.ArrayList;
import java.util.List;

import Game.HexTile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class BoardView extends Region {
	TilePreview tilePreview;
	
	BoardView(TilePreview tp)
	{
		tilePreview = tp;
		
		getStyleClass().add("board");
	}

	public void setTiles(HexTile[][] h) {
		Double width 	= 100.0;
		Double height 	= 88.0;
		
		setMinSize(width*6, height*7+16);
		setMaxSize(width*6, height*7+16);
		
		ArrayList<Tile> list = new ArrayList<Tile>();
		
        for (int i = 0; i < 7; i++) {
        	for (int j = 0; j < 7; j++) {
        		if (h[i][j] != null) {
	    	        Tile tile = new Tile(width, height);
	    	        
	    	        Double x = j*width - j*30 + j*4;
	    	        Double y = i*height + j*height/2 - 3*height/2 + i + 2;
	    	        tile.relocate(x, y);
	    	        tile.setOnMouseClicked(new EventHandler<MouseEvent>(){
	    	        	 
	    	            @Override
	    	            public void handle(MouseEvent e) {
	    	            	Tile t = (Tile)e.getSource();
	    	            	tilePreview.changeTile((t));
	    	            	t.setStyle("-fx-border: 100px; -fx-border-color: 'red';");
	    	            }
	    	        });
	    	        	    	        
	    	       /* tile..addListener(new ChangeListener() {
	    		        @Override
	    		        public void changed(ObservableValue ov, Object t, Object t1) {
	    		        	if (ov..getItems().size() <= 10)
	    		        		view.setPrefWidth(64.25 * 10);
	    		        	else
	    		        		view.setPrefWidth((view.getItems().size() * 64.5));
	    		        }
	    			});*/
	    	        
	    	        list.add(tile);
        		}
        	}
        	
        	this.getChildren().setAll(list);
        }
    }
}
