package core.game.world;

import core.Server;
import core.game.model.entity.player.Player;


/**
 * Handles still graphics
 * 
 * @author Graham
 * 
 */
public class StillGraphicsManager {

	/**
	 * Nothing to load, as of yet.
	 */
	public StillGraphicsManager() {
	}

	public void stillGraphics(Player curPlr, int absX, int absY,
			int heightLevel, int id, int pause) {
		for(Player p : Server.getPlayerManager().getPlayerRegion((curPlr).currentRegion)){
			if (p == null)
				continue;
			if (!p.isActive)
				continue;
			if (p.disconnected)
				continue;
			Player c = p;
			if (c == curPlr || c.withinDistance(absX, absY, heightLevel)) {
				c.getActionSender().sendStillGraphics(id, heightLevel, absY, absX, pause);
			}
		}
	}

}
