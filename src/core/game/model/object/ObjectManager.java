package core.game.model.object;

import java.util.ArrayList;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;

/**
 * @author Sanity
 */

public class ObjectManager {

	public ArrayList<Object> objects = new ArrayList<Object>();
	private ArrayList<Object> toRemove = new ArrayList<Object>();
	public void process() {
		for (Object o : objects) {
			if (o.tick > 0)
				o.tick--;
			else {
				updateObject(o);
				toRemove.add(o);
			}		
	
			objects.remove(o);	
		}
		toRemove.clear();
	}
	
	public void removeObject(int x, int y) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = (Player)PlayerHandler.players[j];
				c.getActionSender().object(-1, x, y, 0, 10);			
			}	
		}	
	}
	
	public void updateObject(Object o) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = (Player)PlayerHandler.players[j];
				c.getActionSender().object(o.newId, o.objectX, o.objectY, o.face, o.type);			
			}	
		}	
	}
	
	public void placeObject(Object o) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = (Player)PlayerHandler.players[j];
				if (c.distanceToPoint(o.objectX, o.objectY) <= 60)
					c.getActionSender().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
			}	
		}
	}
	
	public Object getObject(int x, int y, int height) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height)
				return o;
		}	
		return null;
	}
	
	public void loadObjects(Player p) {
		if (p == null)
			return;
		for (Object o : objects) {
			if (loadForPlayer(o,p))
				p.getActionSender().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
		}
		loadCustomSpawns(p);
	}
	
	public void loadCustomSpawns(Player p) {
			p.getActionSender().checkObjectSpawn(-1, 0, 0, 0, 10);
		if (p.heightLevel == 1)
			p.getActionSender().checkObjectSpawn(-1, 0, 0, 0, 10);
	}
	
	public boolean loadForPlayer(Object o, Player p) {
		if (o == null || p == null)
			return false;
		return p.distanceToPoint(o.objectX, o.objectY) <= 60 && p.heightLevel == o.height;
	}
	
	public void addObject(Object o) {
		if (getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			placeObject(o);
		}	
	}

}