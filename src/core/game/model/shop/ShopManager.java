package core.game.model.shop;

import core.game.model.item.ItemDefinition;

public class ShopManager {
	
	private static int maxShops = 10;

	public static final Shop[] SHOPS = new Shop[maxShops];
	
	public int getShopItemValue(int itemId) {
		return ItemDefinition.getDefinitions()[itemId].getValue();
	}
	
}

