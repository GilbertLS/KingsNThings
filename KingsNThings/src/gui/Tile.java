package gui;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;

public class Tile extends Region {
    public Tile(Double width, Double height)
    {
    	this.getStyleClass().add("tile");
    	this.setPrefSize(width, height);
    	
    	Polygon hex = new Polygon();
    	hex.getPoints().addAll(new Double[]{
    			0.0, 		height/2,
    			width/4, 	0.0,
    		    3*width/4, 	0.0,
    		    width, 		height/2,
    		    3*width/4, 	height,
    		    width/4, 	height});

        this.setShape(hex);
    }

    //add all required methods for custom drawing, styling etc.
}