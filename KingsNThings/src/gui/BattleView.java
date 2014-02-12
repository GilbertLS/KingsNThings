package gui;

import java.util.Arrays;

import Game.HexTile;
import Game.GameConstants.Terrain;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class BattleView extends Scene {
	public BorderPane root;
    public VBox rightPanel;
    public HBox bottomPanel;
    public BoardView board;
    public PlayerList playerList;
    public ButtonBox buttonBox;
    public RackView rack;
    public TilePreview tilePreview;
    public DiceListView diceListView;
	
	public BattleView(BorderPane root){
		super(root, 700, 500);
		
		 rightPanel = new VBox();
        root.setRight(rightPanel);
        
        bottomPanel = new HBox();
        root.setBottom(bottomPanel);
        
        tilePreview = new TilePreview();
        
        board = new BoardView(tilePreview);
        root.setCenter(board);        
        
        diceListView = new DiceListView();
        rightPanel.getChildren().add(diceListView);
        
        this.getStylesheets().add("gui/myStyle.css");
	}
}
