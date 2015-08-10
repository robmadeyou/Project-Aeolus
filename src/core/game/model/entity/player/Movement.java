package core.game.model.entity.player;

/**
 * Class which contains methods for player movements
 * @author 7Winds
 */
public class Movement {
	
	private Player p;
	
	public Movement(Player p) {
		this.p = p;
	}
	
	/**
	 * Player turns towards a certain position
	 * @param pointX
	 * @param pointY
	 */
	public void turnTo(int pointX, int pointY) {
		p.focusPointX = 2 * pointX + 1;
		p.focusPointY = 2 * pointY + 1;
		p.updateRequired = true;
	}

	/**
	 * Moves a player to a set of coordinates
	 * @param x
	 * @param y
	 * @param h
	 */
	public void movePlayer(int x, int y, int h) {
		p.resetWalkingQueue();
		p.teleportToX = x;
		p.teleportToY = y;
		p.heightLevel = h;
		p.getActionSender().requestUpdates();
	}
	
}
