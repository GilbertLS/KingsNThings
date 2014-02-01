package gui;

import java.util.ArrayList;
import java.util.Arrays;
import Game.HexTile;
import Game.GameConstants.Terrain;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameView extends Scene {
    public BorderPane root;
    public VBox rightPanel;
    public HBox bottomPanel;
    public BoardView board;
    public PlayerList playerList;
    public RackView rack;
	
    public GameView(BorderPane r) {
    	super(r, 1200, 800);
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
        
        board = new BoardView(tiles);
        root.setCenter(board);        
        
        playerList = new PlayerList(Arrays.asList(new PlayerPanel("Player 1", 0), new PlayerPanel("Player 2", 0), new PlayerPanel("Player 3", 0), new PlayerPanel("Player 4", 0)));
        rightPanel.getChildren().add(playerList);
        
        ArrayList<ThingView> arr = new ArrayList<ThingView>();
        for(int i = 0; i < 10; i++)
        	arr.add(new ThingView());
        rack = new RackView(FXCollections.observableList((arr)));
        bottomPanel.getChildren().add(rack);
        
        this.getStylesheets().add("gui/myStyle.css");
    }
}