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
	public static final int SET_HEX_TILES = 14;
	public static final int SET_CREATURES = 15;
	public static final int GET_THINGS_FROM_CUP = 16;
	public static final int DISTRIBUTE_INITIAL_GOLD = 18;
	public static final int AWARD_INCOME = 19;
	public static final int TEST_EVENT = 999;
	public static final int NULL_EVENT = -1;
	public static final int PLACE_PIECE_ON_TILE = 100;
	public static final int HANDLE_PLACE_PIECE_ON_TILE = 101;
	public static final int DETERMINE_NUM_PAID_THINGS = 104;
	public static final int DISTRIBUTE_RECRUITS = 105;
	public static final int HANDLE_DISTRIBUTE_RECRUITS = 106;
	public static final int PLAY_THINGS = 107;
	public static final int CHECK_PLAYER_RACK_OVERLOAD = 108;
	public static final int HANDLE_PLAY_THINGS = 109;
	public static final int HANDLE_CHECK_PLAYER_RACK_OVERLOAD = 110;
	public static final int DETERMINE_NUM_TRADE_THINGS = 111;
	public static final int PAY_GOLD = 112;
	public static final int ENTER_NUMBER = 113;
	public static final int DETERMINE_TOTAL_NUM_RECRUITS = 114;
}
