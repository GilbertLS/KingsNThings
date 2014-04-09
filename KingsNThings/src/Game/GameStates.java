package Game;

import java.util.ArrayList;

import gui.GameView;
import Game.GameConstants.ControlledBy;
import Game.GameConstants.SetOption;
import Game.GameConstants.Terrain;
import Game.Phases.Phase;
import Game.Networking.Event;
import Game.Networking.EventHandler;
import Game.Networking.EventList;
import Game.Networking.GameClient;

public class GameStates {
	public static void Minimal() {
		// hex tiles
		GameStates.SetTileType(Terrain.PLAINS, 0, 3);
		GameStates.SetTileType(Terrain.FOREST, -1, 2);
		GameStates.SetTileType(Terrain.SWAMP, -2, 1);
		GameStates.SetTileType(Terrain.SEA, -3, 0);
		GameStates.SetTileType(Terrain.SWAMP, 1, 3);
		GameStates.SetTileType(Terrain.FROZEN_WASTE, 0, 2);
		GameStates.SetTileType(Terrain.MOUNTAIN, -1, 1);
		GameStates.SetTileType(Terrain.SEA, -2, 0);
		GameStates.SetTileType(Terrain.SWAMP, -3, -1);
		GameStates.SetTileType(Terrain.PLAINS, 2, 3);
		GameStates.SetTileType(Terrain.DESERT, 1, 2);
		GameStates.SetTileType(Terrain.MOUNTAIN, 0, 1);
		GameStates.SetTileType(Terrain.FOREST, -1, 0);
		GameStates.SetTileType(Terrain.PLAINS, -2, -1);
		GameStates.SetTileType(Terrain.FOREST, -3, -2);
		GameStates.SetTileType(Terrain.SEA, 3, 3);
		GameStates.SetTileType(Terrain.SWAMP, 2, 2);
		GameStates.SetTileType(Terrain.SEA, 1, 1);
		GameStates.SetTileType(Terrain.SWAMP, 0, 0);
		GameStates.SetTileType(Terrain.DESERT, -1, -1);
		GameStates.SetTileType(Terrain.MOUNTAIN, -2, -2);
		GameStates.SetTileType(Terrain.MOUNTAIN, -3, -3);
		GameStates.SetTileType(Terrain.JUNGLE, 3, 2);
		GameStates.SetTileType(Terrain.MOUNTAIN, 2, 1);
		GameStates.SetTileType(Terrain.PLAINS, 1, 0);
		GameStates.SetTileType(Terrain.FROZEN_WASTE, 0, -1);
		GameStates.SetTileType(Terrain.JUNGLE, -1, -2);
		GameStates.SetTileType(Terrain.FROZEN_WASTE, -2, -3);
		GameStates.SetTileType(Terrain.FROZEN_WASTE, 3, 1);
		GameStates.SetTileType(Terrain.FOREST, 2, 0);
		GameStates.SetTileType(Terrain.DESERT, 1, -1);
		GameStates.SetTileType(Terrain.FOREST, 0, -2);
		GameStates.SetTileType(Terrain.DESERT, -1, -3);
		GameStates.SetTileType(Terrain.PLAINS, 3, 0);
		GameStates.SetTileType(Terrain.FROZEN_WASTE, 2, -1);
		GameStates.SetTileType(Terrain.SWAMP, 1, -2);
		GameStates.SetTileType(Terrain.DESERT, 0, -3);
	
		// hex ownership
		GameStates.AddMarkerToTile(SetOption.HEX, 4, 0, 3);
		GameStates.AddMarkerToTile(SetOption.HEX, 4, -1, 2);
		GameStates.AddMarkerToTile(SetOption.HEX, 4, -2, 1);
		GameStates.AddMarkerToTile(SetOption.HEX, 4, 1, 3);
		GameStates.AddMarkerToTile(SetOption.HEX, 4, 0, 2);
		GameStates.AddMarkerToTile(SetOption.HEX, 3, -3, -1);
		GameStates.AddMarkerToTile(SetOption.HEX, 4, 2, 3);
		GameStates.AddMarkerToTile(SetOption.HEX, 4, 1, 2);
		GameStates.AddMarkerToTile(SetOption.HEX, 3, -1, 0);
		GameStates.AddMarkerToTile(SetOption.HEX, 3, -2, -1);
		GameStates.AddMarkerToTile(SetOption.HEX, 3, -3, -2);
		GameStates.AddMarkerToTile(SetOption.HEX, 1, 2, 2);
		GameStates.AddMarkerToTile(SetOption.HEX, 1, 0, 0);
		GameStates.AddMarkerToTile(SetOption.HEX, 2, -1, -1);
		GameStates.AddMarkerToTile(SetOption.HEX, 3, -2, -2);
		GameStates.AddMarkerToTile(SetOption.HEX, 3, -3, -3);
		GameStates.AddMarkerToTile(SetOption.HEX, 1, 3, 2);
		GameStates.AddMarkerToTile(SetOption.HEX, 1, 2, 1);
		GameStates.AddMarkerToTile(SetOption.HEX, 1, 1, 0);
		GameStates.AddMarkerToTile(SetOption.HEX, 2, 0, -1);
		GameStates.AddMarkerToTile(SetOption.HEX, 2, -1, -2);
		GameStates.AddMarkerToTile(SetOption.HEX, 2, -2, -3);
		GameStates.AddMarkerToTile(SetOption.HEX, 1, 3, 1);
		GameStates.AddMarkerToTile(SetOption.HEX, 1, 2, 0);
		GameStates.AddMarkerToTile(SetOption.HEX, 2, 1, -1);
		GameStates.AddMarkerToTile(SetOption.HEX, 2, 0, -2);
		GameStates.AddMarkerToTile(SetOption.HEX, 2, -1, -3);
		GameStates.AddMarkerToTile(SetOption.HEX, 1, 3, 0);
		GameStates.AddMarkerToTile(SetOption.HEX, 2, 2, -1);
		GameStates.AddMarkerToTile(SetOption.HEX, 2, 1, -2);
		GameStates.AddMarkerToTile(SetOption.HEX, 2, 0, -3);
		
		// towers
		GameStates.AddMarkerToTile(SetOption.KEEP, 4, 0, 3);
		GameStates.AddMarkerToTile(SetOption.KEEP, 4, -1, 2);
		GameStates.AddMarkerToTile(SetOption.CASTLE, 4, 1, 3);
		GameStates.AddMarkerToTile(SetOption.TOWER, 4, 0, 2);
		GameStates.AddMarkerToTile(SetOption.TOWER, 3, -3, -1);
		GameStates.AddMarkerToTile(SetOption.KEEP, 3, -3, -2);
		GameStates.AddMarkerToTile(SetOption.KEEP, 1, 0, 0);
		GameStates.AddMarkerToTile(SetOption.KEEP, 1, 3, 2);
		GameStates.AddMarkerToTile(SetOption.CASTLE, 1, 2, 1);
		GameStates.AddMarkerToTile(SetOption.TOWER, 1, 1, 0);
		GameStates.AddMarkerToTile(SetOption.TOWER, 2, 0, -1);
		GameStates.AddMarkerToTile(SetOption.CASTLE, 2, -1, -2);
		GameStates.AddMarkerToTile(SetOption.TOWER, 1, 2, 0);
		GameStates.AddMarkerToTile(SetOption.KEEP, 2, 1, -1);
		GameStates.AddMarkerToTile(SetOption.KEEP, 2, 0, -2);
		GameStates.AddMarkerToTile(SetOption.CASTLE, 1, 3, 0);
		GameStates.AddMarkerToTile(SetOption.KEEP, 2, 2, -1);
		GameStates.AddMarkerToTile(SetOption.TOWER, 2, 1, -2);
		
		// p1 stack
		GameStates.AddThing("Crocodiles", 1, 1, 0);
		GameStates.AddThing("Mountain Men", 1, 1, 0);
		GameStates.AddThing("Giant Lizard", 1, 1, 0);
		GameStates.AddThing("Slime Beast", 1, 1, 0);
		GameStates.AddThing("Killer Raccoon", 1, 1, 0);
		GameStates.AddThing("Farmers", 1, 1, 0);
		GameStates.AddThing("Wild Cat", 1, 1, 0);
		
		
		// p2 stack
		GameStates.AddThing("Thing", 2, 0, -1);
		GameStates.AddThing("Giant Lizard", 2, 0, -1);
		GameStates.AddThing("Swamp Rat", 2, 0, -1);
		GameStates.AddThing("Unicorn", 2, 0, -1);
		GameStates.AddThing("Bears", 2, 0, -1);
		GameStates.AddThing("Giant Spider", 2, 0, -1);
		GameStates.AddThing("Camel Corps", 2, 0, -1);
		GameStates.AddThing("Sandworm", 2, 0, -1);
	}
	
	public static void Average() {
		
	}
	
	public static void Superior() {
		
	}
	
	public static void Outstanding1() {
		
	}
	
	public static void Outstanding2() {
		
	}
	
	public static void SetTileType(Terrain terrain, int x, int y) {
		String[] params = new String[4];
		params[0] = "" + x;
		params[1] = "" + y;
		params[2] = "" + terrain.name();
		
		Event e = new Event()
			.EventId(EventList.SET_HEX_TERRAIN)
			.EventParameters(params);
		
		EventHandler.SendEvent(e);
	}
	
	public static void AddMarkerToTile(SetOption type, int player, int x, int y) {
		String[] params = new String[4];
		params[0] = "" + x;
		params[1] = "" + y;
		params[2] = "" + (player - 1);
		params[3] = "" + type.name();
		
		Event e = new Event()
			.EventId(EventList.SET_HEX_TILE)
			.EventParameters(params);
		
		EventHandler.SendEvent(e);
	}
	
	public static void SetPhase(Phase phase) {
		Event e = new Event()
			.EventId(EventList.SET_PHASE)
			.EventParameter("" + phase.name());
		
		EventHandler.SendEvent(e);
	}
	
	public static void ClearThings() {
		
	}
	
	public static void AddThing(String name, int player, int x, int y ) {
		String[] params = new String[4];
		params[0] = "" + x;
		params[1] = "" + y;
		
		ArrayList<Thing> things = GameClient.game.gameModel.getAllTestPlayingCupCreatures();
		int thingId = -1;
		for(Thing t : things) {
			if(t.GetName().equals(name)) {
				thingId = t.thingID;
				break;
			}
		}
		
		params[2] = "" + thingId;
		params[3] = "" + (player - 1);
		
		Event e = new Event()
			.EventId(EventList.ADD_THING)
			.EventParameters(params);
		
		EventHandler.SendEvent(e);
	}
}
