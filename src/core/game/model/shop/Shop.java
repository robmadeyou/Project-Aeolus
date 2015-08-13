package core.game.model.shop;

/**
 * A class which represents a single shop
 * @author 7Winds
 */
public class Shop {

	private int id;
	private String name;
	private ShopItem[] item;
	private boolean restock;
	private boolean canSellItems;
	private Currency currency;
	
	public Shop(int id, String name, ShopItem[] item) {
		this.id = id;
		this.name = name;
		this.item = item;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ShopItem[] getItem() {
		return item;
	}

	public boolean isRestock() {
		return restock;
	}

	public boolean isCanSellItems() {
		return canSellItems;
	}

	public Currency getCurrency() {
		return currency;
	}	
	
}
