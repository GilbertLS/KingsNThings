package Game;

import java.util.Comparator;

import Game.Networking.GameRouter;

//helper comparator class to sort servers by player order
public class ServerComparator implements Comparator<GameRouter> {
    @Override
    public int compare(GameRouter r1, GameRouter r2) {
    	return Integer.compare(r1.myPlayerOrder, r2.myPlayerOrder);
    }
}
