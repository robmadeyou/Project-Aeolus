package core.game.model.shop;

/**
 * A simple class which resembles ingame currency
 * @author 7Winds
 */
public enum Currency {
	
	COINS(995),
	TOKKUL(6529),
	WARRIOR_GUILD_TOKENS(8851);
	
	private int itemId;
	
	private Currency(int itemId) {
		this.itemId = itemId;
	}
	
	public int getCurrencyItemId() {
		return itemId;
	}

}
