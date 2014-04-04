package Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;
import Game.Networking.GameClient;
import Game.GameConstants.Level;

public class BoardController {
	private GameBoard gameBoard;
	
	public BoardController( 
			GameBoard gameBoard
	) {
		this.gameBoard = gameBoard;
	}
	
	public List<int[]> GetContestedZones(){
		List<int[]> conflictedTiles = new ArrayList<int[]>();
		
		for(int x = -3; x < 4; x++){
			for(int y = -3; y < 4; y++){
				int numPlayers = 0;
	
				HexTile h = gameBoard.getTile(x, y);
				if (h == null)
					continue;
				
				for(int i=0; i< 5; i++){
					if (h.hasCombatants(i)) 
						numPlayers++;
				}
				
				if(numPlayers > 1){
					conflictedTiles.add(new int[]{ x, y });
				}
			}
		}
		
		return conflictedTiles;
	}
	
	public void AddThingToTile(Thing thing, Player player, int tileX, int tileY){
		gameBoard.getTile(tileX, tileY).AddThingToTile(player, thing);
	}
	
	public boolean HasThingsOnTile(Player player, int tileX, int tileY){
		return gameBoard.getTile(tileX, tileY).HasThingsOnTile(player);
	}
	
	public HexTile GetTile(int tileX, int tileY){
		return gameBoard.getTile(tileX, tileY);
	}
	
	public List<Integer> PlayersOnTile(int tileX, int tileY){
		List<Integer> playersOnTile = new ArrayList<Integer>();
		if (!gameBoard.getTile(tileX, tileY).player1Things.isEmpty()) { playersOnTile.add(0); }
		if (!gameBoard.getTile(tileX, tileY).player2Things.isEmpty()) { playersOnTile.add(1); }
		if (!gameBoard.getTile(tileX, tileY).player3Things.isEmpty()) { playersOnTile.add(2); }
		if (!gameBoard.getTile(tileX, tileY).player4Things.isEmpty()) { playersOnTile.add(3); }
		
		return playersOnTile;
	}
	
	public void RemoveThings(int[] thingsToRemove, Player player, int tileX, int tileY){
		
		HexTile h = gameBoard.getTile(tileX, tileY);
		ArrayList<Thing> things = h.getCombatants(player.GetPlayerNum());
		
		/* removing this for visibility reasons
		if (thingsToRemove.length > things.size()){
			things.removeAllElements();
			return;
		}*/
		
		List<Thing> removeThings = new ArrayList<Thing>(things.size());
		
		for (int i = 0; i < thingsToRemove.length; i++){
			for (Thing thing : things){
				if (thing.GetThingId() == thingsToRemove[i]){
					removeThings.add(thing);
				}
			}
		}
		
		for (Thing thing : removeThings){
			if(thing.thingType == ThingType.SETTLEMENT
					|| thing.thingType == ThingType.FORT)	//buildings take hits
			{
				((Building)thing).takeHit();
			}
			else{
				//everything else is eliminated
				GameClient.game.gameModel.handleElimination(thing, h);
			}
		}
	}
	
	public ArrayList<Thing> GetBluffs(int tileX, int tileY, int player) {
		HexTile tile = this.GetTile(tileX, tileY);
		
		ArrayList<Terrain> controlledTerrains = getControlledTerrains(player);
		ArrayList<Terrain> terrainsControlledByTerrainLords = getControlledTerrainsByTerrainLords(player, tile);
		
		ArrayList<Thing> things = tile.getCombatants(player);
		ArrayList<Thing> bluffs = new ArrayList<Thing>();
		
		for(Thing t : things) {
			if(t.thingType == ThingType.CREATURE) {
				Creature c = (Creature)t;
				if (!controlledTerrains.contains(c.GetTerrain()) &&
					!terrainsControlledByTerrainLords.contains(c.GetTerrain()) &&
					!c.isBuilding()) 
				{
					bluffs.add(c);
				}
			}
		}
		
		return bluffs;
	}

	public ArrayList<Terrain> getControlledTerrains(int player) {
		ArrayList<Terrain> controlledTerrains = new ArrayList<Terrain>();
		
		for(int x = -3; x < 4; x++) {
			for(int y = -3; y < 4; y++) {
				HexTile tile = gameBoard.getTile(x, y);
				if (tile == null) { continue; }
				
				if (tile.isControlledBy(GameConstants.controlledByFromIndex(player))) {
					if (!controlledTerrains.contains(tile.getTerrain())) {
						controlledTerrains.add(tile.getTerrain());
					}
				} 
				
			}
		}
		
		return controlledTerrains;
	}
	
	public ArrayList<Terrain> getControlledTerrainsByTerrainLords(int player, HexTile tile) {
		ArrayList<Terrain> controlledTerrains = new ArrayList<Terrain>();
		
		List<Thing> things = tile.getCombatants(player);
		
		for(Thing t : things) {
			if (t.thingType == ThingType.TERRAIN_LORD) {
				TerrainLord tlord = (TerrainLord)t;
				
				if(!controlledTerrains.contains(tlord.getTerrain())) {
					controlledTerrains.add(tlord.getTerrain());
				}
				
			}
		}
		
		return controlledTerrains;
	}
}
