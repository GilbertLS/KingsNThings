package gui;

import java.util.ArrayList;
import java.util.List;
import Game.Player;
import Game.GameConstants.CurrentPhase;
import Game.Networking.GameClient;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class ThingCell extends ListCell<ThingView> implements Draggable {
	ThingCell thisCell = this;

	public ThingCell() {
		getStyleClass().add("thing");
		setAlignment(Pos.CENTER);
		initListeners();
	}

	protected void initListeners()
	{	
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (getItem() == null){
					return;
				}
				
				List<ThingView> list = getListView().getSelectionModel().getSelectedItems();
				
				Player currPlayer = GameClient.game.gameModel.GetCurrentPlayer();
				GameView.selectedThings.clear();
				for (ThingView thing : list){
					if (currPlayer != null && thing.thingRef.controlledBy != currPlayer.faction){
						continue;
					}
					
					int thingId = thing.thingRef.thingID;
					
					GameView.selectedThings.add(thingId);
				}
				
				System.out.println(GameView.selectedThings);
			}
		});
		
		setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent e) {
				if (getItem() == null) {
					return;
				}
				
				GameView gv = GameClient.game.gameView;

				//drag can be started from any of these phases
				if(gv.currentPhase == CurrentPhase.MOVEMENT 
						|| gv.currentPhase == CurrentPhase.PLAY_THINGS
						|| gv.currentPhase == CurrentPhase.RECRUIT_CHARACTER)
				{
					ArrayList<Integer> selectedIds = new ArrayList<Integer>(getListView().getSelectionModel().getSelectedIndices());
					
					//check valid drag
					if(GameClient.game.validDragStart(gv, getListView(), selectedIds))
					{															
						Dragboard db = startDragAndDrop(TransferMode.MOVE);
						ClipboardContent content = new ClipboardContent();
						content.put(thingRackIds, selectedIds);
						
						content.put(originalTile, gv.tilePreview.tileRef.getTileRef().x + "SPLIT" +  gv.tilePreview.tileRef.getTileRef().y + "~");
							
						db.setContent(content);
					}
				}
				//db.setDragView(arg0);
				e.consume();
			}
		});
	}
	
	@Override
	protected void updateItem(ThingView t, boolean b) {
		super.updateItem(t, b);

		if (t == null) {
			setStyle("");
		} else {
			setPrefSize(64, 64);
			
			int controlledBy 	= t.thingRef.getControlledByPlayerNum();
			int currPlayer 		= GameClient.game.gameModel.getCurrPlayerNumber();
			
			if(!t.thingRef.isFlipped() || (controlledBy == currPlayer && currPlayer > -1))
				setStyle("-fx-background-image: url('res/images/" + t.thingRef.frontFileName + "'); -fx-background-size: 60 60;");
			else
				setStyle("-fx-background-image: url('res/images/" + t.thingRef.backFileName + "'); -fx-background-size: 60 60;");
		}
	}
}