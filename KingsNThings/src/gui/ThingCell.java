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

				if(gv.currentPhase == CurrentPhase.MOVEMENT || gv.currentPhase == CurrentPhase.PLAY_THINGS)
				{
					//prevent movement from tile preview during playthings phase
					if(!(!getListView().equals(gv.rack) && gv.currentPhase == CurrentPhase.PLAY_THINGS))
					{
						ArrayList<Integer> selectedIds = new ArrayList<Integer>(getListView().getSelectionModel().getSelectedIndices());
						
						//Check if thing is owned by player
						if (getListView().getItems().get(selectedIds.get(0)).thingRef.getControlledByPlayerNum() == GameClient.game.gameView.getCurrentPlayer()) {
								
							//check not pinned
							if( getListView().equals(gv.rack)
									|| GameClient.game.gameView.tilePreview.tileRef.getTileRef().isOnlyPlayerOnTile(gv.getCurrentPlayer()))
							{		
								Dragboard db = startDragAndDrop(TransferMode.MOVE);
								ClipboardContent content = new ClipboardContent();
								content.put(thingRackIds, selectedIds);
						
								content.put(originalTile, gv.tilePreview.tileRef.getTileRef().x + "SPLIT" +  gv.tilePreview.tileRef.getTileRef().y + "~");
							
								db.setContent(content);
							}
						}
					}
				}

				//db.setDragView(arg0);
				e.consume();;
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
			setStyle("-fx-background-image: url('res/images/" + t.thingRef.frontFileName + "'); -fx-background-size: 60 60;");
			
		}
	}
}