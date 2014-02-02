package gui;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;

public class Tile extends Region implements Draggable {
	Tile thisTile = this;
	ArrayList<ThingView> p1Things = 		new ArrayList<ThingView>();
	ArrayList<ThingView> p2Things = 		new ArrayList<ThingView>();
	ArrayList<ThingView> p3Things = 		new ArrayList<ThingView>();
	ArrayList<ThingView> p4Things = 		new ArrayList<ThingView>();
	ArrayList<ThingView> neutralThings = 	new ArrayList<ThingView>();
	ThingView fort;
	ThingView economy;
	int controllingPlayer = 0;
	
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
    
    private ArrayList<ThingView> getView(int i) {
    	if (i == 1)
    		return p1Things;
    	else if (i == 2)
    		return p2Things;
    	else if (i == 3)
    		return p3Things;
    	else if (i == 4)
    		return p4Things;
    	else
    		return neutralThings;
    }
    
    public void add(ThingView t, int player) {
    	getView(player).add(t);
    }
    
    public void addAll(List<ThingView> t, int player) {
    	getView(player).addAll(t);
    }
    
    public void remove(ThingView t, int player) {
    	getView(player).remove(t);
    }
    
    public void removeAll(List<ThingView> t, int player) {
    	getView(player).removeAll(t);
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
					
					//MODIFY THIS SO IT IS PLAYING PERSONS NUMBER
					thisTile.addAll(things, 1);
					source.getListView().getItems().removeAll(things);

					success = true;
				}
				
				e.setDropCompleted(success);
				e.consume();
			}
		});
		
		focusedProperty().addListener(new ChangeListener() {
	        @Override
	        public void changed(ObservableValue ov, Object t, Object t1) {
	        	thisTile.setStyle("-fx-border: 0px;");
	        }
		});
	}
    
    
}