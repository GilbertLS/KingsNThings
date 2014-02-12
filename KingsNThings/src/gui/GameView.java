package gui;

import java.util.ArrayList;
import java.util.Arrays;

import Game.Creature;
import Game.GameConstants;
import Game.GameConstants.ControlledBy;
import Game.HexTile;
import Game.GameConstants.Terrain;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameView extends Scene {
    public BorderPane root;
    public VBox rightPanel;
    public HBox bottomPanel;
    public BoardView board;
    public PlayerList playerList;
    public ButtonBox buttonBox;
    public RackView rack;
    public TilePreview tilePreview;
    public DiceListView diceListView;
	
    public GameView(BorderPane r) {
    	super(r, 1000, 600);
    	root = r; 
        
        rightPanel = new VBox();
        root.setRight(rightPanel);
        
        bottomPanel = new HBox();
        root.setBottom(bottomPanel);
        
        //BOARDVIEW TEST
        HexTile[] array1 = new HexTile[] {null, null, null, new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA)};
        HexTile[] array2 = new HexTile[] {null, null, new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA)};
        HexTile[] array3 = new HexTile[] {null, new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA)};
        HexTile[] array4 = new HexTile[] {new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA)};
        HexTile[] array5 = new HexTile[] {new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), null};
        HexTile[] array6 = new HexTile[] {new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), null, null};
        HexTile[] array7 = new HexTile[] {new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), new HexTile(Terrain.SEA), null, null, null};
        HexTile[][] tiles = {array1, array2, array3, array4, array5, array6, array7};
        
        tilePreview = new TilePreview();
        
        board = new BoardView(tilePreview);
        root.setCenter(board);        
        
        playerList = new PlayerList(Arrays.asList(new PlayerPanel(1), new PlayerPanel(2), new PlayerPanel(3), new PlayerPanel(4)));
        rightPanel.getChildren().add(playerList);
        
        buttonBox = new ButtonBox();
        rightPanel.getChildren().add(buttonBox);
        
        rightPanel.getChildren().add(tilePreview);
        
        diceListView = new DiceListView();
        rightPanel.getChildren().add(diceListView);
        
        ArrayList<ThingView> arr = new ArrayList<ThingView>();
        for(int i = 0; i < 10; i++)
        	arr.add(new ThingView(new Creature(GameConstants.Terrain.DESERT)));
        rack = new RackView(FXCollections.observableList((arr)));
        bottomPanel.getChildren().add(rack);
        
        this.getStylesheets().add("gui/myStyle.css");
    }
    
    //method to give back a hextile
    public HexTile chooseHexTile()
    {
    	Tile t = this.board.getNextSelectedTile();
    	HexTile h = t.getTileRef();
    	
    	/*HexTile h = new HexTile(Terrain.DESERT);
    	
    	do
    	{
    		h.x = (int)(Math.floor(Math.random()*7)-3);
    		h.y = (int)(Math.floor(Math.random()*7)-3);
    	}while(((h.x == -3 && (h.y == 3 || h.y == 2 || h.y == 1))
    			||(h.x == -2 && (h.y == 3 || h.y == 2))
    			||(h.x == -1 && (h.y == 3))
    			||(h.x == 3 && (h.y == -3 || h.y == -2 || h.y == -1))
    			||(h.x == 2 && (h.y == -3 || h.y == -2))
    			||(h.x == 1 && (h.y == -3))));
    	
    	int faction = (int)Math.floor(Math.random()*5);
    	
    	switch(faction)
    	{
    	case 0:
    		h.controlledBy = ControlledBy.PLAYER1;
    		break;
    	case 1:
    		h.controlledBy = ControlledBy.PLAYER2;
    		break;
    	case 2:
    		h.controlledBy = ControlledBy.PLAYER3;
    		break;
    	case 3:
    		h.controlledBy = ControlledBy.PLAYER4;
    		break;
    	default:
    		h.controlledBy = ControlledBy.NEUTRAL;
    		break;
    	}*/
    	
    	return h;
    }
    
    public void updateHexTile(HexTile h)
    {
    	//control marker
    	
    	//fort
    }
    
    public void displayMessage(String message)
    {
    	System.out.println("GAME VIEW MESSAGE: " + message);
    }

	public int getNumPaidRecruits() {
		return 2;
	}

	public void updatePlayerRack() {
		//re-draw the player rack for current player (because it has changed)
		//if necessary, I can pass the appropriate rack
	}

	public int getNumTradeRecruits() {
		return 0;
	}
	
	public void StartBattle(int tileX, int tileY){	
		Platform.runLater(new Runnable() {
			public void run(){
				BorderPane p = new BorderPane();
				Scene battleView = new BattleView(p);
				
				Stage battleStage = new Stage();
				battleStage.setTitle("Combat Time!");
				battleStage.setScene(battleView);
                 
				//battleStage.setX(battleStage.getX() + 150);
				//battleStage.setY(battleStage.getY() + 150);
  
				battleStage.show();
			}
		});
	}
}