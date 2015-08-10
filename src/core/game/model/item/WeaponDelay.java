package core.game.model.item;

/**
 * A class which represents a single weapon delay
 * @author 7Winds
 */
public class WeaponDelay {
	
	private int id;	
	private int delay;
	
	public WeaponDelay(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}

	public int getId() {
		return id;
	}
	
	public int getDelay() {
		return delay;
	}
	
}
