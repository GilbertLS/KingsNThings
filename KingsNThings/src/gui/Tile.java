package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;

public class Tile extends Region {
	Tile thisTile = this;
	
    public Tile(Double width, Double height)
    {
    	getStyleClass().add("tile");
    	setPrefSize(width, height);
    	
    	Polygon hex = new Polygon();
    	hex.getPoints().addAll(new Double[]{
    			0.0, 		height/2,
    			width/4, 	0.0,
    		    3*width/4, 	0.0,
    		    width, 		height/2,
    		    3*width/4, 	height,
    		    width/4, 	height});

        setShape(hex);
        initListeners();
    }

    protected void initListeners()
	{	
		setOnDragOver(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {
	            if (e.getGestureSource() != this &&
	                   e.getDragboard().hasString()) {
	                e.acceptTransferModes(TransferMode.MOVE);
	            }
	
	            e.consume();
			}
        });

        setOnDragEntered(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {
	            if (e.getGestureSource() != this &&
	                    e.getDragboard().hasString()) {
	                setOpacity(0.3);
	            }
			}
        });

        setOnDragExited(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {
	            if (e.getGestureSource() != this &&
	                    e.getDragboard().hasString()) {
	                setOpacity(1);
	            }
			}
        });
		
		setOnDragDropped(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {
				Dragboard db = e.getDragboard();
				boolean success = false;

				if (db.hasString()) {
					ThingCell t 					= (ThingCell)e.getGestureSource();
					int id							= Integer.parseInt(db.getString());
					ThingView item 					= t.getListView().getItems().get(id);

					thisTile.getChildren().add(item);
					
					t.getListView().getItems().remove(id);

					success = true;
				}
				e.setDropCompleted(success);

				e.consume();
			}
		});
	}
    
    //add all required methods for custom drawing, styling etc.
}