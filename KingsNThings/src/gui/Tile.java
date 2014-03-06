package gui;

import java.util.ArrayList;
import java.util.List;

import Game.GameConstants;
import Game.GameConstants.ControlledBy;
import Game.GameConstants.CurrentPhase;
import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;
import Game.GameController;
import Game.Utility;
import Game.Networking.GameClient;
import Game.HexTile;
import Game.Thing;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;

public class Tile extends Region implements Draggable {
	private Tile thisTile = this;
	private HexTile tileRef;
	public ArrayList<ThingView> p1Things = 			new ArrayList<ThingView>();
	public ArrayList<ThingView> p2Things = 			new ArrayList<ThingView>();
	public ArrayList<ThingView> p3Things = 			new ArrayList<ThingView>();
	public ArrayList<ThingView> p4Things = 			new ArrayList<ThingView>();
	public ArrayList<ThingView> neutralThings = 	new ArrayList<ThingView>();
	public ThingView fort = null;
	public ThingView economy = null;
	private int controllingPlayer = 0;
	private CurrentPhase currentPhase;
	
    public Tile(Double width, Double height, HexTile h)
    {
    	tileRef = h;
    	
    	this.setStyle("-fx-background-image: url(/res/images/ " + getBackgroundFromType() + "); ");
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
    	this.initListeners();
    }
    
    public ArrayList<ThingView> getView(int i) {
    	if (i == 0)
    		return p1Things;
    	else if (i == 1)
    		return p2Things;
    	else if (i == 2)
    		return p3Things;
    	else if (i == 3)
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
    
    public void addThing(Thing t) {
    	ThingView tv = new ThingView(t);
    	getView(t.getControlledByPlayerNum()).add(tv);
    }
    
    public void removeThing(Thing t) {
    	ThingView tv = new ThingView(t);
    	getView(t.getControlledByPlayerNum()).remove(tv);
    }
    
    public void updateThings(int playerNum) {
    	ArrayList<ThingView> listOfTvs = getView(playerNum);
    	ArrayList<Thing> listOfThings = this.tileRef.GetThings(playerNum);
    	
    	listOfTvs.clear();
    	
    	for(Thing t : listOfThings) {
    		ThingView tv = new ThingView(t);
    		if(t.getThingType() != ThingType.FORT)
    		listOfTvs.add(tv);
    	}
    }
    
    public void updateThings(){
    	for(int i = 0; i < 4; i++){
    		updateThings(i);
    	}
    }
    
    private String getBackgroundFromType()
    {
    	if (tileRef != null) {
    		if(tileRef.controlledBy == ControlledBy.NEUTRAL)
    		{
    			return "Tuile_Back.png";
    		}
    		else
    		switch (tileRef.getTerrain()) {
    			case SEA: return "Tuile-Mer.png";
    			case JUNGLE: return "Tuile-Jungle.png";
    			case FROZEN_WASTE: return "Tuile-Entendue-Glac�e.png";
    			case FOREST: return "Tuile-For�t.png";
    			case PLAINS: return "Tuile-Plaines.png";
    			case SWAMP: return "Tuile-Marais.png";
    			case MOUNTAIN: return "Tuile-Montagne.png";
    			case DESERT: return "Tuile-Desert.png";
    		}
    	}
    	
    	return "Tuile_Back.png";
    }
    
    public HexTile getTileRef() {
    	return this.tileRef;
    }
    
    public void update() {
    	this.getChildren().clear();
    	
    	ArrayList<Node> list = new ArrayList<Node>();
    	
    	String markerPath = getMarkerString();
    	
    	if (markerPath != null) {
	    	Image img = new Image("res/images/" + markerPath);
	    	ImageView imgView = new ImageView(img);
	    	imgView.setFitHeight(25);
	    	imgView.setFitWidth(25);
	    	imgView.setX(this.getWidth()/4);
	    	list.add(imgView);
    	}
    	
    	
    	String fortPath = getFortString();
    	
    	if (fortPath != null) {
	    	Image img = new Image("res/images/" + fortPath);
	    	ImageView imgView = new ImageView(img);
	    	imgView.setFitHeight(40);
	    	imgView.setFitWidth(40);
	    	imgView.setX(this.getWidth()/2 - imgView.getFitWidth()/2);
	    	imgView.setY(this.getHeight()/2 - imgView.getFitHeight()/2);
	    	list.add(imgView);
	    	
	    	if(fort == null)
	    		fort = new ThingView(tileRef.fort);
    	}
    	else
    	{
    		fort = null;
    	}
    	
    	if(tileRef.controlledBy != ControlledBy.NEUTRAL)
    		this.setStyle("-fx-background-image: url(/res/images/ " + getBackgroundFromType() + "); ");
    	
    	this.getChildren().setAll(list);
    	
    }
    
    private String getMarkerString() {
    	if (tileRef != null) {
    		switch (tileRef.controlledBy) {
    			case PLAYER1: return "CM_411.png";
    			case PLAYER2: return "CM_412.png";
    			case PLAYER3: return "CM_413.png";
    			case PLAYER4: return "CM_414.png";
    			default: return null;
    		}
    	}
    	
    	return null;
    }
    
    private String getFortString() {
    	if (tileRef != null && tileRef.fort != null) {
    		switch (tileRef.fort.getLevel()) {
    			case TOWER: return "C_Fort_375.png";
    			case KEEP: return "C_Fort_377.png";
    			case CASTLE: return "C_Fort_379.png";
    			case CITADEL: return "C_Fort_381.png";
    			default: return null;
    		}
    	}
    	
    	return null;
    }

    protected void initListeners()
	{	
    	this.setOnMouseClicked(new EventHandler<MouseEvent>() {
    		@Override public void handle(MouseEvent e) {
    			
    		}
    	});
    	
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
						ArrayList<ThingView> 		thingViews		= new ArrayList<ThingView>();
						
						GameView gv = (GameView)getScene();
						
						for (Integer i : listOfIds) {
							thingViews.add(items.get(i));
						}
						
						String originalTileString = (String)e.getDragboard().getContent(originalTile);
						
						if(gv.currentPhase != CurrentPhase.NULL)
						{							
							if(gv.currentPhase == CurrentPhase.PLAY_THINGS)
							{
								if(items.get(0).thingRef.controlledBy == tileRef.controlledBy)
								{									
									gv.playerList.getPlayerPanel(gv.getCurrentPlayer()).removeThings(thingViews.size());
									
									for(ThingView t: thingViews)
										gv.returnString += tileRef.x + "SPLIT"+ tileRef.y+"~"+t.thingRef.thingID+"/";
									
									thisTile.addAll(thingViews, gv.getCurrentPlayer());
									source.getListView().getItems().removeAll(thingViews);
									
									success = true;
								}
							}
							else if(gv.currentPhase == CurrentPhase.MOVEMENT)
							{
								String[] paramsStrings = originalTileString.split("~");
								String[] originalHexParams = paramsStrings[0].split("SPLIT");
								int x = Integer.parseInt(originalHexParams[0].trim());
								int y = Integer.parseInt(originalHexParams[1].trim());
								
								HexTile originalTile = GameClient.game.gameModel.gameBoard.getTile(x, y);
								
								ArrayList<Thing> things = new ArrayList<Thing>();
								
								for(ThingView tv: thingViews)
									things.add(tv.thingRef);
								
								if(GameClient.game.isValidMove(originalTile, tileRef, things))
								{
									if(tileRef.controlledBy == ControlledBy.NEUTRAL) //exploration
									{
										//check roll in model
										boolean spawnCreatures = GameClient.game.rollForCreatures(gv.getCurrentPlayer(), tileRef.x, tileRef.y);
										
										if(spawnCreatures)
										{
											//handle creatures
										}
										else
										{
											//update faction
											update();
										}
									}
									
									thisTile.addAll(thingViews, gv.getCurrentPlayer());
									source.getListView().getItems().removeAll(thingViews);
									
									success = true;
									
									//Utility.GotInput(moveLock);
									gv.moveMade = true;
									
									//string to update 
									for(ThingView t: thingViews)
										gv.returnString += originalTileString + tileRef.x + "SPLIT"+ tileRef.y+"~"+t.thingRef.thingID+"/";
									
								}
							}
						}
					}
					
					e.setDropCompleted(success);
					e.consume();
				}
		});
	}

	public void showTile() {
		String s ="";
		
		switch (tileRef.getTerrain()) {
			case SEA: s = "Tuile-Mer.png"; break;
			case JUNGLE: s ="Tuile-Jungle.png"; break;
			case FROZEN_WASTE: s = "Tuile-Entendue-Glac�e.png"; break;
			case FOREST: s = "Tuile-For�t.png"; break;
			case PLAINS: s ="Tuile-Plaines.png"; break;
			case SWAMP: s ="Tuile-Marais.png"; break;
			case MOUNTAIN: s ="Tuile-Montagne.png"; break;
			case DESERT: s ="Tuile-Desert.png"; break;
		}
    	
    	this.setStyle("-fx-background-image: url(/res/images/ " + s + "); ");
	}
	
	public List<ThingView> getThings(int i) {
		switch(i) {
			case 1: return p1Things;
			case 2: return p2Things;
			case 3: return p3Things;
			case 4: return p4Things;
			default: return neutralThings;
		}
	}
	
	public void hideTile() {
		if(tileRef.controlledBy == ControlledBy.NEUTRAL)
			this.setStyle("-fx-background-image: url(/res/images/ " + "Tuile_Back.png" + "); ");
	}
		   
}