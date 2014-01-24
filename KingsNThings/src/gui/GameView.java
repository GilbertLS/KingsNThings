package gui;

import java.util.Arrays;

import javafx.application.Application;
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
        
        PlayerPanel p1Panel = new PlayerPanel("Player 1", 0);
        PlayerPanel p2Panel = new PlayerPanel("Player 2", 0);
        PlayerPanel p3Panel = new PlayerPanel("Player 3", 0);
        PlayerPanel p4Panel = new PlayerPanel("Player 4", 0);
        
        PlayerList playerList = new PlayerList(Arrays.asList(p1Panel, p2Panel, p3Panel, p4Panel));
        rightPanel.getChildren().add(playerList);
        
        RackView rack = new RackView();
        bottomPanel.getChildren().add(rack);
                
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("gui/myStyle.css");
        
        primaryStage.setTitle(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}