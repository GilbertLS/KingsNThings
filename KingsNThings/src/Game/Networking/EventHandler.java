package Game.Networking;

import java.util.List;

public class EventHandler {
	public static void HandleEvent( String input ){
		Event e = Event.Destringify( input );
		HandleEvent( e );
	}
	
	public static void SendEvent( Event e ){
		String eventString = e.toString();
		
		EventSender.Send( eventString );
	}
	
	private static void HandleEvent( Event e ){
		if (e.eventId == EventList.READY){}
		else if (e.eventId == EventList.ROLL_DICE)
		{
			System.out.println("CREATING DIE ROLL RESPONSE EVENT");
			
			String[] args = new String[1];
			args[0] = Integer.toString((int)Math.ceil(Math.random()*6));
			EventHandler.SendEvent(new Event(EventList.ROLL_DICE, args));
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
				args[i++] = zone[0] + "," + zone[1];
			}
			
			EventHandler.SendEvent(
				new Event(EventList.GET_CONTESTED_ZONES, args)
			);
		}
		else if(e.eventId == EventList.TEST_EVENT)
		{
			GameClient.game.gameView.playerList.getChildren().get(0).setStyle("-fx-background-color: 'red'");
		}
	}
}