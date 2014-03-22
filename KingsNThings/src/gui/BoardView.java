package gui;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import Game.GameConstants.CurrentPhase;
import Game.HexTile;
import Game.Utility;
import Game.Networking.Event;
import Game.Networking.EventList;
import Game.Networking.GameClient;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class BoardView extends Region {
	private BoardView 	thisBoard = this;
	private TilePreview tilePreview;
	public 	Tile 		lastSelectedTile;
	private Semaphore	selectedTileLock = new Semaphore(0);
	private Semaphore 	tempSem;
	private boolean 	waitingForOtherSelectedTile = false;
	
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
	    	            	if (waitingForOtherSelectedTile) {
	    	            		waitingForOtherSelectedTile = false;
	    	            		Utility.GotInput(tempSem);
	    	            	} else {
	    	            		Utility.GotInput(selectedTileLock);
	    	            	}
	    	            		
	    	            	GameView gv = (GameView)getScene();
	    	            	if(gv.currentPhase == CurrentPhase.CONSTRUCTION)
	    	            	{
	    	            		int playerIndex = GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum();
	    	            		HexTile h = t.getTileRef();
	    	            		
	    	            		if(GameClient.game.gameModel.isValidConstruction(h, playerIndex))
	    	            		{
	    	            			GameClient.game.gameModel.updateConstruction(h, playerIndex);
	    	            			gv.updateHexTile(h);
	    	            			gv.updateGold(GameClient.game.gameModel.GetCurrentPlayer().getGold(), playerIndex);
	    	            			
		    	            		String[] args = new String[2];
		    	            		args[0] = Integer.toString(playerIndex);
		    	            		args[1] = h.x + "SPLIT" + h.y;
		    	            		
		    	            		boolean[] intendedPlayers = new boolean[GameClient.game.gameModel.PlayerCount()];
		    	            		
		    	            		for(int i=0; i<GameClient.game.gameModel.PlayerCount(); i++)
		    	            			if(i != playerIndex)
		    	            				intendedPlayers[i] = true;
		    	            			
		    	            		Event gameEvent = new Event()
		    	            			.EventId(EventList.HANDLE_CONSTRUCTION)
		    	            			.IntendedPlayers(intendedPlayers)
		    	            			.EventParameters(args);
		    	            		
		    	            		Game.Networking.EventHandler.SendEvent(gameEvent);
	    	            		}
	    	            	}
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
	
	private Tile getNextSelectedTile(Semaphore waitSem) {
		this.clearLastSelectedTile();
		
		Utility.PromptForInput(waitSem);
		
		return lastSelectedTile;
	}
	
	public Tile getNextSelectedTileFromEditState() {
		waitingForOtherSelectedTile = true;
		tempSem = new Semaphore(0);
		return getNextSelectedTile(tempSem);
	}
        
	public Tile getNextSelectedTile() {
		selectedTileLock = new Semaphore(0);
		return getNextSelectedTile(selectedTileLock);
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
