package Game.Networking;

import java.util.List;

import javafx.application.Platform;
import Game.HexTile;

public class EventHandler {
	public static void HandleEvent( String input ){
		Event e = Event.Destringify( input );
		HandleEvent( e );
	}
	
	public static void SendEvent( Event e ){
		String eventString = e.toString();
		
		EventSender.Send( eventString );
	}
	
	public static void HandleEvent( Event e ){
		if (e.eventId == EventList.READY){}
		else if (e.eventId == EventList.ROLL_DICE)
		{
			System.out.println("CREATING DIE ROLL RESPONSE EVENT");
			
			String[] args = new String[1];
			args[0] = Integer.toString((int)Math.ceil(Math.random()*6));
			
			EventHandler.SendEvent(
					new Event()
						.EventId(EventList.ROLL_DICE)
						.EventParameters(args)
			);
		}
		else if(e.eventId == EventList.SET_PLAYER_ORDER)
		{
			System.out.println("HANDLING SET PLAYER ORDER EVENT");
			
			GameClient.game.setPlayerOrders(Integer.parseInt(e.eventParams[1]));
		}
		else if(e.eventId == EventList.SET_NUM_PLAYERS)
		{
			System.out.println("HANDLING SET NUM PLAYERS EVENT");
			
			GameClient.game.setPlayerCount(Integer.parseInt(e.eventParams[0]));
		}
		else if(e.eventId == EventList.UPDATE_PLAYER_ORDER)
		{
			System.out.println("HANDLING UPDATE PLAYER ORDER EVENT");
			
			GameClient.game.updatePlayerOrder();
		}
		else if(e.eventId == EventList.BEGIN_BATTLE)
		{
			
		}
		else if(e.eventId == EventList.GET_CONTESTED_ZONES)
		{
			List<int[]> contestedZones = GameClient.game.gameModel.boardController.GetContestedZones();
			
			String[] args = new String[contestedZones.size()];
			
			int i = 0;
			for(int[] zone : contestedZones){
				args[i++] = zone[0] + "SPLIT" + zone[1];
				System.out.println(args[i-1]);
			}
			
			EventHandler.SendEvent(
				new Event()
					.EventId( EventList.GET_CONTESTED_ZONES )
					.EventParameters(args)
			);
		}
		else if(e.eventId == EventList.TEST_EVENT)
		{
			GameClient.game.gameView.playerList.getChildren().get(0).setStyle("-fx-background-color: 'red'");
		}
		else if (e.eventId == EventList.GET_MAGIC_ROLLS){
			int tileX = Integer.parseInt(e.eventParams[0]);
			int tileY = Integer.parseInt(e.eventParams[1]);
			
			if (!GameClient.game.gameModel.boardController.HasThingsOnTile(
					GameClient.game.gameModel.GetCurrentPlayer(), 
					tileX, 
					tileY
				)) {
				
			}
		}
		else if (e.eventId == EventList.SET_CURRENT_PLAYER){
			int playerNum = Integer.parseInt(e.eventParams[0]);
			
			GameClient.game.gameModel.SetCurrentPlayer(playerNum);
		}
		else if (e.eventId == EventList.SET_HEX_TILES)
		{
			String boardHexTilesString = e.eventParams[0];
			String unusedHexTileString = e.eventParams[1];
			
			String[] boardHexTileStrings = boardHexTilesString.split("/");
			String[] unusedHexTileStrings = unusedHexTileString.split("/");
			
			final HexTile[][] h = GameClient.game.gameModel.setInitialHexTiles(boardHexTileStrings);
			GameClient.game.gameModel.setInitialUnusedHexTiles(unusedHexTileStrings);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	GameClient.game.gameView.board.setTiles(h);
		        }
		    });
		}
		else if (e.eventId == EventList.SET_CREATURES)
		{
			String creaturesString = e.eventParams[0];
			
			String[] creaturesStrings = creaturesString.split("/");
			
			GameClient.game.gameModel.setPlayingCup(creaturesStrings);
			
			//GameClient.game.gameModel.printCurrentBoardTiles();
		}
		else if (e.eventId == EventList.ASSIGN_INITIAL_THINGS)
		{
			final int currentPlayerIndex = GameClient.game.gameModel.GetCurrentPlayer().GetPlayerNum();
			
			GameClient.game.gameModel.assignInitialThings(currentPlayerIndex);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
					GameClient.game.gameView.rack.setAllThings(GameClient.game.gameModel.GetCurrentPlayer().getPlayerRack().getThings());
					GameClient.game.gameView.playerList.getPlayerPanel(currentPlayerIndex).setThings(10);
		        }
		    });
		}
		else if (e.eventId == EventList.HANDLE_ASSIGN_INITIAL_THINGS)
		{
			final int playerIndex = Integer.parseInt(e.eventParams[0]);
			GameClient.game.gameModel.assignInitialThings(playerIndex);
			System.out.println(playerIndex);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	GameClient.game.gameView.playerList.getPlayerPanel(playerIndex).setThings(10);
		        }
			});
		}
		else if (e.eventId == EventList.DISTRIBUTE_INITIAL_GOLD)
		{
			final int numClients = Integer.parseInt(e.eventParams[0]);
			
			GameClient.game.gameModel.distributeInitialGold();
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	for(int i=0; i<numClients; i++)
		        	{
		        		GameClient.game.gameView.playerList.getPlayerPanel(i).setGold(10);
		        	}
		        }
			});
		}
		else if (e.eventId == EventList.AWARD_INCOME)
		{
			final int numClients = Integer.parseInt(e.eventParams[0]);
			
			final int[] goldUpdates = GameClient.game.gameModel.distributeIncome();
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	for(int i=0; i<numClients; i++)
		        	{
		        		GameClient.game.gameView.playerList.getPlayerPanel(i).addGold(goldUpdates[i]);
		        	}
		        }
			});
		}
	}
}
