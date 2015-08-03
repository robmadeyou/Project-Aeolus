package core.net.packets.incoming;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import core.game.model.entity.player.Player;

/**
 * Player manager
 * 
 * @author Daiki
 * @author Perfectworld
 * 
 */
@SuppressWarnings("all")
public class PlayerManager {

	private static PlayerManager singleton = null;
	private Map<Integer, Queue<Player>> playersByRegion = new HashMap<Integer, Queue<Player>>();;
	public final int areaSize = 26;

	public static PlayerManager getSingleton() {
		if (singleton == null) {
			singleton = new PlayerManager();
		}
		return singleton;
	}

	public void setupRegionPlayers() {
		int hash = 0;
		for (int i = 0; i < 9999; i += areaSize) {// each region will be 24
													// squares big lol this will
													// eb teh x axis
			for (int j = 0; j < 12500; j += areaSize) {// y axis
				int g = ((i / areaSize)) + ((j / areaSize) * 100);
				Queue<Player> he = new LinkedList<Player>();
				playersByRegion.put(g, he);
				hash++;
			}
		}
	}

	public Queue<Player> getPlayerRegion(int id) {
		return playersByRegion.get(id);
	}
}
