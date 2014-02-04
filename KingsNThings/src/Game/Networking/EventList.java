package Game.Networking;

public class EventList {
	public static final int READY = 0;
	public static final int ROLL_DICE = 1;
	public static final int SET_PLAYER_ORDER = 2;
	public static final int SET_NUM_PLAYERS = 3;
	public static final int BEGIN_BATTLE = 4;
	public static final int GET_CONTESTED_ZONES = 5;
	public static final int UPDATE_PLAYER_ORDER = 6;
	public static final int GET_CREATURE_ROLLS = 7;
	public static final int SET_CURRENT_PLAYER = 8;
	public static final int CHOOSE_PLAYER = 9;
	public static final int INFLICT_HITS = 10;
	public static final int REMOVE_THINGS = 11;
	public static final int ADD_THING_TO_TILE = 12;
	public static final int BATTLE_OVER = 13;
	public static final int TEST_EVENT = 999;
	public static final int NULL_EVENT = -1;
}
