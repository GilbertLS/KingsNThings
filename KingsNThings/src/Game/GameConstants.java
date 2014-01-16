package Game;

/*
 * This class holds all relevant constants for the Game (for example, enums, screen width/height)
 */
public final class GameConstants {
	//-----------ENUMS-------------------
	
	//The different types of possible terrain in the game
	//(Note that all except "Unrevealed" and "Sea" are considered "Land")
	public static enum Terrain {Unrevealed, Sea, Jungle, Frozen_Waste, Forest, Plains, Swamp, Mountain, Desert}
	
	//The Different Factions which may control game pieces
	public static enum ControlledBy {Neutral, Player1, Player2, Player3, Player4}
	
	//The different types of "Things" in the game
	public static enum ThingType {Special_Income, Magic, Treasure, Event, Creature}
	
	//The different levels a Fort can have (Tower < Keep < Castle < Citadel)
	public static enum Level {Tower, Keep, Castle, Citadel}
}
