package gui;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;

public class Tile extends Region implements Draggable {
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
	            if (e.getGestureSource() != this && e.getDragboard().hasContent(thingRackIds)) {
	                e.acceptTransferModes(TransferMode.MOVE);
	            }
	
	            e.consume();
			}
        });

        setOnDragEntered(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {
	            if (e.getGestureSource() != this && e.getDragboard().hasContent(thingRackIds)) {
	                setOpacity(0.3);
	            }
			}
        });

        setOnDragExited(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {
	            if (e.getGestureSource() != this && e.getDragboard().hasContent(thingRackIds)) {
	                setOpacity(1);
	            }
			}
        });
		
		setOnDragDropped(new EventHandler<DragEvent>() {
			@Override public void handle(DragEvent e) {
				Dragboard 	db 		= e.getDragboard();
				boolean 	success = false;

				if (db.hasContent(thingRackIds)) {
					ThingCell 					source 		= (ThingCell)e.getGestureSource();
					ArrayList<Integer> 			listOfIds 	= (ArrayList<Integer>) e.getDragboard().getContent(thingRackIds);
					ObservableList<ThingView> 	items		= source.getListView().getItems();
					ArrayList<ThingView> 		things		= new ArrayList<ThingView>();

					for (Integer i : listOfIds) {
						things.add(items.get(i));
					}
					
					thisTile.getChildren().addAll(things);
					source.getListView().getItems().removeAll(things);

					success = true;
				}
				
				e.setDropCompleted(success);
				e.consume();
			}
		});
	}
    
    //add all required methods for custom drawing, styling etc.
}