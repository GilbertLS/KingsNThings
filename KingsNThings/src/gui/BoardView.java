package gui;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import Game.HexTile;
import Game.Utility;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class BoardView extends Region {
	private BoardView 	thisBoard = this;
	private TilePreview tilePreview;
	public 	Tile 		lastSelectedTile;
	private Semaphore	selectedTileLock = new Semaphore(0);
	
	BoardView(TilePreview tp)
	{
		tilePreview = tp;
		getStyleClass().add("board");
	}

	public void setTiles(HexTile[][] h) {
		Double width 	= 90.0;
		Double height 	= 81.0;
		
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
	    	            	
	    	            	//tilePreview
	    	            	tilePreview.changeTile((t));
	    	            	
	    	            	//tileSelection
	    	            	thisBoard.lastSelectedTile = t;
	    	            	Utility.GotInput(selectedTileLock);
	    	            }
	    	        });
	    	        
	    	        list.add(tile);
        		}
        	}
        	
        	this.getChildren().setAll(list);
        }
	}
	
	public Tile getLastSelectedTile() {
		return this.lastSelectedTile;
	}
        
	public Tile getNextSelectedTile() {
		this.clearLastSelectedTile();
		
		selectedTileLock = new Semaphore(0);
		Utility.PromptForInput(selectedTileLock);
		
		return lastSelectedTile;
	}
	
	public void clearLastSelectedTile() {
	   this.lastSelectedTile = null;
	}
	
	public Tile getTileByHex(HexTile h) {
		for (Node n : this.getChildren()) {
			if (n.getClass() == Tile.class) {
				Tile t = (Tile)n;
				if (t.getTileRef() == h) {
					return t;
				}
			}
		}
		return null;
	}
	
	public void showHideAllTiles(boolean show) {
		for (Node n : this.getChildren()) {
			if (n.getClass() == Tile.class) {
				Tile t = (Tile)n;
					if(show)
						t.showTile();
					else
						t.hideTile();
			}
		}		
	}
}
