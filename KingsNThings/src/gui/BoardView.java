package gui;

import Game.HexTile;
import javafx.scene.layout.Region;

public class BoardView extends Region {
	BoardView(HexTile[][] h)
	{
		getStyleClass().add("board");
		addTiles(h);
	}

	private void addTiles(HexTile[][] h) {
		Double width 	= 100.0;//120.0;
		Double height 	= 86.0;//106.0;
		
		setMinSize(width*6, height*7+16);
		setMaxSize(width*6, height*7+16);
		
        for (int i = 0; i < 7; i++) {
        	for (int j = 0; j < 7; j++) {
        		if (h[i][j] != null) {
	    	        Tile tile = new Tile(width, height);
	    	        
	    	        Double x = j*width - j*30 + j*4;
	    	        Double y = i*height + j*height/2 - 3*height/2 + i + 2;
	    	        tile.relocate(x, y);
	    	        
	    	        getChildren().add(tile);
        		}
        	}
        }
    }
}
