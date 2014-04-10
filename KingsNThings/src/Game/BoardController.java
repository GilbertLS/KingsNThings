package Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javafx.application.Platform;
import Game.GameConstants.ControlledBy;
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
	
	public boolean HasCombatantsOnTile(Player player, int tileX, int tileY) {
		return gameBoard.getTile(tileX, tileY).hasCombatants(player.GetPlayerNum());
	}
	
	public HexTile GetTile(int tileX, int tileY){
		return gameBoard.getTile(tileX, tileY);
	}
	
	public List<Integer> PlayersOnTile(int tileX, int tileY){
		List<Integer> playersOnTile = new ArrayList<Integer>();
		HexTile tile = gameBoard.getTile(tileX, tileY);
		
		for(int i = 0; i < GameClient.game.gameModel.PlayerCount(); i++) {
			ArrayList<Thing> combatants = tile.getCombatants(i);
			if (Utility.NumberCombatants(combatants) > 0) { playersOnTile.add(i); }
		}
		
		ArrayList<Thing> combatants = tile.getCombatants(4);
		if (Utility.NumberCombatants(combatants) > 0) { playersOnTile.add(4); }
		
		return playersOnTile;
	}
	
	public void RemoveThings(int[] thingsToRemove, int player, int tileX, int tileY){
		
		HexTile h = gameBoard.getTile(tileX, tileY);
		ArrayList<Thing> things = h.getCombatants(player);
		
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

	public ArrayList<HexTile> GetAdjacentTiles(int tileX, int tileY) {
		HexTile currTile = GetTile(tileX, tileY);
		
		ArrayList<HexTile> adjacentTiles = new ArrayList<HexTile>();
		for(int x = -3; x < 4; x++){
			for(int y = -3; y < 4; y++){
				HexTile h = gameBoard.getTile(x, y);
				if (h == null) {
					continue;
				}
				
				if (currTile.isAdjacent(h) && h != currTile) {
					adjacentTiles.add(h);
				}
			}
		}
		
		return adjacentTiles;
	}
	
	public ArrayList<HexTile> GetAdjacentControlledTiles(ControlledBy controlledBy, int tileX, int tileY) {
		ArrayList<HexTile> adjacentTiles = GetAdjacentTiles(tileX, tileY);
		ArrayList<HexTile> controlledAdjacentTiles = new ArrayList<HexTile>();
		
		for(HexTile t : adjacentTiles) {
			if (t.getControlledBy() == controlledBy) {
				controlledAdjacentTiles.add(t);
			}
		}
		
		return controlledAdjacentTiles;
	}
	
	public void ClearThings() {
		for(int x = -3; x < 4; x++){
			for(int y = -3; y < 4; y++){
				final HexTile h = gameBoard.getTile(x, y);
				if (h == null) {
					continue;
				}

				ArrayList<Thing> things = h.getAllThings();
				for(Thing t : things) {
					GameClient.game.gameModel.handleElimination(t, h);
				}
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						GameClient.game.gameView.board.getTileByHex(h).update();
						GameClient.game.gameView.board.getTileByHex(h).updateThings();
					}
				});
				
			}
		}
	}
}
