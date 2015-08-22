package core.game.world;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.sun.istack.internal.logging.Logger;

import core.game.model.entity.player.Player;

/**
 * Class which represents the game world
 */
public class World {
	
	public static final Logger logger = Logger.getLogger(World.class);
	
	private static World singleton = null;
	private Map<Integer, Queue<Player>> playersByRegion = new HashMap<Integer, Queue<Player>>();;
	public final int areaSize = 26;

	public static World getSingleton() {
		if (singleton == null) {
			singleton = new World();
		}
		return singleton;
	}
	
	/**
	 * Setups up square regions, each region will be 24 squares big
	 */
	public void setupPlayerRegions() {
		int hash = 0;
		for (int i = 0; i < 9999; i += areaSize) {//x axis
			for (int j = 0; j < 12500; j += areaSize) {// y axis
				int g = ((i / areaSize)) + ((j / areaSize) * 100);
				Queue<Player> he = new LinkedList<Player>();
				playersByRegion.put(g, he);
				hash++;
			}
		}
		logger.info("Loaded: " + hash + " player regions.");
	}
	
	public Queue<Player> getClientRegion(int id) {
		return playersByRegion.get(id);
	}

}
