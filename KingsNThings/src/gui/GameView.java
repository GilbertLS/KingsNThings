package gui;

import java.util.Arrays;

import Game.HexTile;
import Game.GameConstants.Terrain;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameView extends Application {
    
    @Override
    public void start(Stage primaryStage) {   	
        BorderPane root = new BorderPane();
        
        VBox rightPanel = new VBox();
        root.setRight(rightPanel);
        
        HBox bottomPanel = new HBox();
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
        
        BoardView board = new BoardView(tiles);
        root.setCenter(board);        
        
        PlayerList playerList = new PlayerList(Arrays.asList(new PlayerPanel("Player 1", 0), new PlayerPanel("Player 2", 0), new PlayerPanel("Player 3", 0), new PlayerPanel("Player 4", 0)));
        rightPanel.getChildren().add(playerList);
        
        RackView rack = new RackView(FXCollections.observableList((Arrays.asList(new ThingView(), new ThingView(), new ThingView(), new ThingView(), new ThingView(), new ThingView(), new ThingView(), new ThingView(), new ThingView(), new ThingView()))));
        bottomPanel.getChildren().add(rack);
                
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add("gui/myStyle.css");
        
        primaryStage.setTitle("Kings N Things");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}