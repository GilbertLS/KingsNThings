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
	private static ArrayList<Integer> alreadySentThings = new ArrayList<Integer>();
	
	public static void Minimal() {
		alreadySentThings = new ArrayList<Integer>();
		
		GameStates.ClearThings();
		
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
		alreadySentThings = new ArrayList<Integer>();
		
		GameStates.ClearThings();
		
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
		GameStates.AddMarkerToTile(SetOption.CASTLE, 4, 1, 3);
		GameStates.AddMarkerToTile(SetOption.TOWER, 4, 0, 2);
		GameStates.AddMarkerToTile(SetOption.KEEP, 3, -3, -2);
		GameStates.AddMarkerToTile(SetOption.KEEP, 1, 0, 0);
		GameStates.AddMarkerToTile(SetOption.CASTLE, 1, 2, 1);
		GameStates.AddMarkerToTile(SetOption.TOWER, 1, 1, 0);
		GameStates.AddMarkerToTile(SetOption.TOWER, 2, 0, -1);
		GameStates.AddMarkerToTile(SetOption.CASTLE, 2, -1, -2);
		GameStates.AddMarkerToTile(SetOption.TOWER, 1, 2, 0);
		GameStates.AddMarkerToTile(SetOption.KEEP, 2, 1, -1);
		GameStates.AddMarkerToTile(SetOption.KEEP, 2, 0, -2);
		GameStates.AddMarkerToTile(SetOption.CASTLE, 1, 3, 0);
		GameStates.AddMarkerToTile(SetOption.KEEP, 2, 2, -1);
	
		//settlements
		GameStates.AddThing("Village", 4, -1, 2);
		GameStates.AddThing("City", 3, -3, -1);
		GameStates.AddThing("Village", 1, 3, 2);
		GameStates.AddThing("Village", 2, 1, -2);
		
		//p1 stack
		GameStates.AddThing("Crocodiles", 1, 1, 0);
		GameStates.AddThing("Mountain Men", 1, 1, 0);
		GameStates.AddThing("Nomads", 1, 1, 0);
		GameStates.AddThing("Giant Spider", 1, 1, 0);
		GameStates.AddThing("Killer Raccoon", 1, 1, 0);
		GameStates.AddThing("Farmers", 1, 1, 0);
		GameStates.AddThing("Ice Giant", 1, 1, 0);
		GameStates.AddThing("White Dragon", 1, 1, 0);
		GameStates.AddThing("Mammoth", 1, 1, 0);
		GameStates.AddThing("Head Hunter", 1, 1, 0);
		
		//p2 stack
		GameStates.AddThing("Thing", 2, 0, -1);
		GameStates.AddThing("Giant Lizard", 2, 0, -1);
		GameStates.AddThing("Swamp Rat", 2, 0, -1);
		GameStates.AddThing("Unicorn", 2, 0, -1);
		GameStates.AddThing("Bears", 2, 0, -1);
		GameStates.AddThing("Camel Corps", 2, 0, -1);
		GameStates.AddThing("Sandworm", 2, 0, -1);
		GameStates.AddThing("Black Knight", 2, 0, -1);
		GameStates.AddThing("Dervish", 2, 0, -1);
		GameStates.AddThing("Forester", 2, 0, -1);
		
		// p1 rack
		GameStates.AddThingToRack("Diamond Field", 1);
		GameStates.AddThingToRack("Peat Bog", 1);
		
		// p2 rack
		GameStates.AddThingToRack("Copper Mine", 2);
		GameStates.AddThingToRack("Gold Mine", 2);
		GameStates.AddThingToRack("Emerald", 2);
	}
	
	public static void Superior() {
		alreadySentThings = new ArrayList<Integer>();
		
		GameStates.ClearThings();
		
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
		GameStates.AddMarkerToTile(SetOption.CASTLE, 4, 1, 3);
		GameStates.AddMarkerToTile(SetOption.TOWER, 4, 0, 2);
		GameStates.AddMarkerToTile(SetOption.KEEP, 3, -3, -2);
		GameStates.AddMarkerToTile(SetOption.KEEP, 1, 0, 0);
		GameStates.AddMarkerToTile(SetOption.CASTLE, 1, 2, 1);
		GameStates.AddMarkerToTile(SetOption.TOWER, 1, 1, 0);
		GameStates.AddMarkerToTile(SetOption.TOWER, 2, 0, -1);
		GameStates.AddMarkerToTile(SetOption.CASTLE, 2, -1, -2);
		GameStates.AddMarkerToTile(SetOption.TOWER, 1, 2, 0);
		GameStates.AddMarkerToTile(SetOption.KEEP, 2, 1, -1);
		GameStates.AddMarkerToTile(SetOption.KEEP, 2, 0, -2);
		GameStates.AddMarkerToTile(SetOption.CASTLE, 1, 3, 0);
		GameStates.AddMarkerToTile(SetOption.KEEP, 2, 2, -1);
		
		//settlements
		GameStates.AddThing("Village", 4, -1, 2);
		GameStates.AddThing("City", 3, -3, -1);
		GameStates.AddThing("Village", 1, 3, 2);
		GameStates.AddThing("Village", 4, 1, -2);
		
		//p1 stack
		GameStates.AddThing("Flying Squirrel", 1, 1, 0);
		GameStates.AddThing("Pixies", 1, 1, 0);
		GameStates.AddThing("Giant Spider", 1, 1, 0);
		GameStates.AddThing("Killer Raccoon", 1, 1, 0);
		GameStates.AddThing("Farmers", 1, 1, 0);
		GameStates.AddThing("Ice Giant", 1, 1, 0);
		GameStates.AddThing("White Dragon", 1, 1, 0);
		GameStates.AddThing("Head Hunter", 1, 1, 0);
		GameStates.AddThing("Ghost", 1, 1, 0);
		GameStates.AddThing("Dark Wizard", 1, 1, 0);
		
		//p2 stack
		GameStates.AddThing("Thing", 2, 0, -1);
		GameStates.AddThing("Unicorn", 2, 0, -1);
		GameStates.AddThing("Bears", 2, 0, -1);
		GameStates.AddThing("Camel Corps", 2, 0, -1);
		GameStates.AddThing("Sandworm", 2, 0, -1);
		GameStates.AddThing("Black Knight", 2, 0, -1);
		GameStates.AddThing("Dervish", 2, 0, -1);
		GameStates.AddThing("Forester", 2, 0, -1);
		GameStates.AddThing("Pterodactyl Warriors", 2, 0, -1);
		GameStates.AddThing("Bird Of Paradise", 2, 0, -1);
		
		// p3 stack
		GameStates.AddThing("Nomads", 3, 1, -1);
		GameStates.AddThing("Dervish", 3, 1, -1);
		GameStates.AddThing("Giant Spider", 3, 1, -1);
		
		// p4 stack
		GameStates.AddThing("Walking Tree", 4, -1, 0);
		GameStates.AddThing("Wild Cat", 4, -1, 0);
		GameStates.AddThing("Elves", 4, -1, 0);
		GameStates.AddThing("Great Owl", 4, -1, 0);
		
		// p1 rack
		GameStates.AddThingToRack("Defection", 1);
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
		Event e = new Event()
			.EventId(EventList.CLEAR_THINGS);
		
		EventHandler.SendEvent(e);
	}
	
	public static void AddThingToRack(String name, int player) {
		String[] params = new String[4];
		
		ArrayList<Thing> things = GameClient.game.gameModel.getAllTestPlayingCupCreatures();
		
		int thingId = -1;
		for(Thing t : things) {
			if(t.GetName().equals(name)) {
				boolean cont = false;
				for(Integer i : alreadySentThings) {
					if (i == t.thingID) {
						cont = true;
					}
				}
				
				if(cont) { continue; }
				
				thingId = t.thingID;
				break;
			}
		}
		
		params[0] = "" + thingId;
		params[1] = "" + (player - 1);
		
		alreadySentThings.add(thingId);
		
		Event e = new Event().EventId(EventList.ADD_THING_TO_RACK).EventParameters(params);
		EventHandler.SendEvent(e);
	}
	
	public static void AddThing(String name, int player, int x, int y ) {
		String[] params = new String[4];
		params[0] = "" + x;
		params[1] = "" + y;
		
		ArrayList<Thing> things = GameClient.game.gameModel.getAllTestPlayingCupCreatures();
		int thingId = -1;
		for(Thing t : things) {
			if(t.GetName().equals(name)) {
				boolean cont = false;
				for(Integer i : alreadySentThings) {
					if (i == t.thingID) {
						cont = true;
					}
				}
				
				if(cont) { continue; }
				
				thingId = t.thingID;
				break;
			}
		}
		
		params[2] = "" + thingId;
		params[3] = "" + (player - 1);
		
		alreadySentThings.add(thingId);
		
		Event e = new Event()
			.EventId(EventList.ADD_THING)
			.EventParameters(params);
		
		EventHandler.SendEvent(e);
	}
}
