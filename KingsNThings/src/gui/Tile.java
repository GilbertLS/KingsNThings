package gui;

import java.util.ArrayList;
import java.util.List;

import Game.GameConstants;
import Game.GameConstants.ControlledBy;
import Game.GameConstants.CurrentPhase;
import Game.GameConstants.ThingType;
import Game.Utility;
import Game.Networking.GameClient;
import Game.HexTile;
import Game.Thing;
import javafx.application.Platform;
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
	private int i;
	private int j;
	public ArrayList<ThingView> p1Things = 			new ArrayList<ThingView>();
	public ArrayList<ThingView> p2Things = 			new ArrayList<ThingView>();
	public ArrayList<ThingView> p3Things = 			new ArrayList<ThingView>();
	public ArrayList<ThingView> p4Things = 			new ArrayList<ThingView>();
	public ArrayList<ThingView> defendingThings = 	new ArrayList<ThingView>();
	public ThingView fort = null;
	public ThingView specialIncome = null;
	private int controllingPlayer = 0;
	private CurrentPhase currentPhase;
	
    public Tile(Double width, Double height, HexTile h, int ii, int jj)
    {
    	i 		= ii;
    	j 		= jj;
    	tileRef = h;
    	
    	this.setStyle("-fx-background-image: url(/res/images/ " + getBackgroundFromType() + "); ");
    	this.getStyleClass().add("tile");
    	this.setSize(width, height);
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
    		return defendingThings;
    }
    
    public int getI() {
    	return i;
    }
    
    public int getJ() {
    	return j;
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
    	ThingView removeThing = null;
    	for(ThingView thing : getView(t.getControlledByPlayerNum())) {
    		if (thing.thingRef.thingID == t.thingID) {
    			removeThing = thing;
    		}
    	}
    	
    	if (removeThing != null) {
    		getView(t.getControlledByPlayerNum()).remove(removeThing);
    	}
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
    	for(int i = 0; i <= 4; i++){
    		updateThings(i);
    	}
    }
    
    private String getBackgroundFromType()
    {
    	if (tileRef != null) {
   			 switch (tileRef.getTerrain()) {
			 	case SEA: return GameConstants.SeaTileFront;
				case JUNGLE: return GameConstants.JungleTileFront;
				case FROZEN_WASTE: return GameConstants.FrozenWasteTileFront;
				case FOREST: return GameConstants.ForestTileFront;
				case PLAINS: return GameConstants.PlainsTileFront;
				case SWAMP: return GameConstants.SwampTileFront;
				case MOUNTAIN: return GameConstants.MountainTileFront;
				case DESERT: return GameConstants.DesertTileFront;
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
	    		fort = new ThingView(tileRef.getFort());
    	}
    	else
    	{
    		fort = null;
    	}
    	
    	String specialIncomePath = getSpecialIncomeString();
    	if (specialIncomePath != null) {
	    	Image img = new Image("res/images/" + specialIncomePath);
	    	ImageView imgView = new ImageView(img);
	    	imgView.setFitHeight(25);
	    	imgView.setFitWidth(25);
	    	imgView.setX(this.getWidth()/4 + getWidth()/4);
	    	list.add(imgView);
	    	
	    	if(specialIncome == null)
	    	{
	    		if(tileRef.hasSpecialIncome())
	    			specialIncome = new ThingView(tileRef.getSpecialIncome());
	    		else if(tileRef.hasSettlement())
	    			specialIncome = new ThingView(tileRef.getSettlement());
	    	}
    	}
    	else
    	{
    		specialIncome = null;
    	}
    	
    	this.setStyle("-fx-background-image: url(/res/images/ " + getBackgroundFromType() + "); ");
    	
    	this.getChildren().setAll(list);
    	
    }
    
    public void changeHex(HexTile h)
    {
    	tileRef = h;
    	
    	update();
    }
    
    private String getMarkerString() {
    	if (tileRef != null) {
    		switch (tileRef.getControlledBy()) {
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
    	if (tileRef != null && tileRef.hasFort()) {
    		switch (tileRef.getFort().getLevel()) {
    			case TOWER: return "C_Fort_375.png";
    			case KEEP: return "C_Fort_377.png";
    			case CASTLE: return "C_Fort_379.png";
    			case CITADEL: return "C_Fort_381.png";
    			default: return null;
    		}
    	}
    	
    	return null;
    }
    
    private String getSpecialIncomeString() {
    	if (tileRef != null)
    	{
    		if (tileRef.hasSpecialIncome())
    			return tileRef.getSpecialIncome().getFrontImage();
    	    else if(tileRef.hasSettlement())
    	    	return tileRef.getSettlement().getFrontImage();
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
						ArrayList<ThingView> 		thingViews	= new ArrayList<ThingView>();
						ArrayList<Thing> 			things 		= new ArrayList<Thing>();
					
						final GameView gv = (GameView)getScene();
						if(gv.currentPhase != CurrentPhase.NULL && gv.returnString.equals(""))
						{											
							for (Integer i : listOfIds) {
								thingViews.add(items.get(i));
							}
							
							for(ThingView tv: thingViews)
								things.add(tv.thingRef);
							
							String originalTileString = (String)e.getDragboard().getContent(originalTile);
							
							
							if(gv.currentPhase == CurrentPhase.PLAY_THINGS)
							{
								if(GameClient.game.isValidPlacement(tileRef, things, gv.getCurrentPlayer()))
								{									
									for(ThingView t: thingViews)
										gv.returnString += tileRef.x + "SPLIT"+ tileRef.y+"~"+t.thingRef.thingID+"/";
									
									//remove special incomes, magic, and treasure (will be updated in model, not added to tile preview)
									ArrayList<ThingView> thingsToRemove = new ArrayList<ThingView>();
									for(ThingView tv: thingViews)
									{
										if(ThingType.isSpecialIncome(tv.thingRef.getThingType())
											|| tv.thingRef.getThingType() == ThingType.TREASURE
											|| tv.thingRef.getThingType() == ThingType.MAGIC)
											thingsToRemove.add(tv);
									}									
									
									for(ThingView tv: thingsToRemove)
									{
										thingViews.remove(tv);
									}
											
									thisTile.addAll(thingViews, gv.getCurrentPlayer());
									source.getListView().getItems().removeAll(thingViews);
									source.getListView().getItems().removeAll(thingsToRemove);
									
									Utility.GotInput(gv.playLock);
									
									success = true;
								}
							}
							else if(gv.currentPhase == CurrentPhase.MOVEMENT  && !source.getListView().equals(gv.rack))
							{
								String[] paramsStrings = originalTileString.split("~");
								String[] originalHexParams = paramsStrings[0].split("SPLIT");
								int x = Integer.parseInt(originalHexParams[0].trim());
								int y = Integer.parseInt(originalHexParams[1].trim());
								
								HexTile originalTile = GameClient.game.gameModel.gameBoard.getTile(x, y);
								
								if(GameClient.game.isValidMove(originalTile, tileRef, things))
								{
									if(tileRef.isControlledBy(ControlledBy.NEUTRAL)
											&& tileRef.noDefense()) 						//exploration
									{
										//things can not move further
										for(Thing t: things)
											t.setMovementFinished();
										
										//handle creatures
										if (GameClient.game.rollForCreatures(gv.getCurrentPlayer(), tileRef.x, tileRef.y)){
											gv.updateTiles(tileRef, 4);
											
											//if not run later, drag drop breaks
											Platform.runLater(new Runnable(){
												public void run(){
													//allow bribing if creatures were created
													gv.bribeCreatures(thisTile);
												}
											});
										}
									}
									
									thisTile.addAll(thingViews, gv.getCurrentPlayer());
									source.getListView().getItems().removeAll(thingViews);
									
									success = true;
									
									//gv.moveMade = true;
									
									//string to update 
									
									gv.returnString += originalTileString + tileRef.x + "SPLIT"+ tileRef.y+"~";
										
									for(ThingView t: thingViews)
										gv.returnString += t.thingRef.thingID + "/";
									
									Utility.GotInput(gv.moveLock);
									
								}
							}
							else if (gv.currentPhase == CurrentPhase.RECRUIT_CHARACTER)
							{
								if(GameClient.game.isValidPlacement(tileRef, things, gv.getCurrentPlayer()))
								{	
									GameClient.game.sendPlayThingEvent(tileRef.x + "SPLIT"+ tileRef.y+"~"+things.get(0).thingID+"/");
								
									thisTile.addAll(thingViews, gv.getCurrentPlayer());
									source.getListView().getItems().removeAll(thingViews);
									
									Utility.GotInput(gv.recruitLock);
									
									success = true;
								}
							}
						}
						
						if(success) {
							TilePreview 	tp 			= GameClient.game.gameView.tilePreview;
							tp.changeTile(thisTile);
							
							int 			currPlayer 	= GameClient.game.gameModel.getCurrPlayerNumber();
							ThingViewList 	list 		= tp.GetThingList(currPlayer);
							int 			size		= list.getItems().size();
							
							list.getSelectionModel().selectRange(size - listOfIds.size(), size);
						}
					}
					
					e.setDropCompleted(success);
					e.consume();
				}
		});
	}

	public void showTile() {  
		String s = "";
				
		switch(tileRef.getTerrain())
		{
		 	case SEA: s = GameConstants.SeaTileFront; break;
			case JUNGLE: s = GameConstants.JungleTileFront; break;
			case FROZEN_WASTE: s = GameConstants.FrozenWasteTileFront; break;
			case FOREST: s = GameConstants.ForestTileFront; break;
			case PLAINS: s = GameConstants.PlainsTileFront; break;
			case SWAMP: s = GameConstants.SwampTileFront; break;
			case MOUNTAIN: s = GameConstants.MountainTileFront; break;
			case DESERT: s = GameConstants.DesertTileFront; break;
		}
		
		this.setStyle("-fx-background-image: url(/res/images/ " + s + "); ");
	}
	
	public List<ThingView> getThings(int i) {
		switch(i) {
			case 1: return p1Things;
			case 2: return p2Things;
			case 3: return p3Things;
			case 4: return p4Things;
			default: return defendingThings;
		}
	}
	
	public void hideTile() {
		if(tileRef.isControlledBy(ControlledBy.NEUTRAL))
			this.setStyle("-fx-background-image: url(/res/images/ " + "Tuile_Back.png" + "); ");
	}
	
	public void setSize(double width, double height) {
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
		   
}