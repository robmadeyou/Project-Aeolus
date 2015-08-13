package core.game.model.shop;

/**
 * A class which resembles a single item in a shop
 * @author 7Winds
 */
public class ShopItem {

	private int id;
	private int amount;
	
	public ShopItem(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}
	
}