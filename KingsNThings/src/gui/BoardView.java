package gui;

import java.util.ArrayList;

import Game.HexTile;
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
		Double height 	= 90.0;
		
		setMinSize(width*6, height*7+16);
		setMaxSize(width*6, height*7+16);
		
		ArrayList<Tile> list = new ArrayList<Tile>();
		
        for (int i = 6; i >= 0; i--) {
        	for (int j = 6; j >= 0; j--) {
        		if (h[i][j] != null) {
	    	        Tile tile = new Tile(width, height, h[i][j]);
	    	        
	    	        Double x = width*3/4*(7+i) - j*width*3/4 - width*3/4*7/2;
	    	        Double y = height/2*(7-i) - j*height/2 + height/2*7/1.4;
	    	        tile.relocate(x, y);
	    	        tile.setOnMouseClicked(new EventHandler<MouseEvent>(){
	    	        	 
	    	            @Override
	    	            public void handle(MouseEvent e) {
	    	            	Tile t = (Tile)e.getSource();
	    	            	tilePreview.changeTile((t));
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
