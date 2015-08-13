package core.game.model.shop;

import core.game.GameConstants;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.item.Item;
import core.game.model.item.ItemDefinition;


public class ShopAssistant {

	private static Player c;

	public ShopAssistant(Player p) {
		ShopAssistant.c = p;
	}

	/**
	 * Opens a shop for a player
	 */
	public void openShop(int ShopID) {
		c.getEquipment().resetItems(3823);
		resetShop(ShopID);
		c.isShopping = true;
		c.myShopId = ShopID;
		c.getActionSender().sendFrame248(3824, 3822);
		c.getActionSender().sendString(ShopHandler.ShopName[ShopID], 3901);
	}

	public boolean shopSellsItem(int itemID) {
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
				return true;
			}
		}
		return false;
	}

	public static void updatePlayerShop() {
		for (int i = 1; i < GameConstants.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].isShopping == true && PlayerHandler.players[i].myShopId == c.myShopId && i != c.playerId) {
					PlayerHandler.players[i].updateShop = true;
				}
			}
		}
	}

	public void updateshop(int i) {
		resetShop(i);
	}

	public static void resetShop(int ShopID) {
		synchronized (c) {
			int TotalItems = 0;
			for (int i = 0; i < ShopHandler.MaxShopItems; i++) {
				if (ShopHandler.ShopItems[ShopID][i] > 0) {
					TotalItems++;
				}
			}
			if (TotalItems > ShopHandler.MaxShopItems) {
				TotalItems = ShopHandler.MaxShopItems;
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(3900);
			c.getOutStream().writeWord(TotalItems);
			int TotalCount = 0;
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[ShopID][i] > 0 || i <= ShopHandler.ShopItemsStandard[ShopID]) {
					if (ShopHandler.ShopItemsN[ShopID][i] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord_v2(ShopHandler.ShopItemsN[ShopID][i]);
					} else {
						c.getOutStream().writeByte(ShopHandler.ShopItemsN[ShopID][i]);
					}
					if (ShopHandler.ShopItems[ShopID][i] > GameConstants.ITEM_LIMIT || ShopHandler.ShopItems[ShopID][i] < 0) {
						ShopHandler.ShopItems[ShopID][i] = GameConstants.ITEM_LIMIT;
					}
					c.getOutStream().writeWordBigEndianA(ShopHandler.ShopItems[ShopID][i]);
					TotalCount++;
				}
				if (TotalCount > TotalItems) {
					break;
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public static double getItemShopValue(int itemId, int Type, int fromSlot) {
		return ItemDefinition.getDefinitions()[itemId].getValue();
	}

	public int getItemShopValue(int itemId) {
		return ItemDefinition.getDefinitions()[itemId].getValue();
	}

	/**
	 * Shows what shop accepts what currency
	 */
	public static Currency getShopType(final int shopID) {
		switch (shopID) {
		
			default:				
				return Currency.COINS;
		}
	}

	/**
	 * buy item from shop (Shop Price)
	 **/

	public void buyFromShopPrice(int removeId, int removeSlot) {
		final Currency ShopType = getShopType(c.myShopId);
		switch (ShopType) {

		}
	}

	/**
	 * Sell item to shop (Shop Price)
	 **/
	public void sellToShopPrice(int removeId, int removeSlot) {

		final Currency shopType = getShopType(c.myShopId);

		switch (shopType) {


		}
	}

	public boolean sellItem(int itemID, int fromSlot, int amount) {
		if ((Boolean) c.getAttributes().get("isTrading")) {
			c.sendMessage("You cant sell items to the shop while your in trade!");
			return false;
		}

		final Currency shopType = getShopType(c.myShopId);
		switch (shopType) {

		}
		return true;
	}

	public static boolean addShopItem(int itemID, int amount) {
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}
		if (Item.itemIsNote[itemID] == true) {
			itemID = c.getItems().getUnnotedItem(itemID);
		}
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if ((ShopHandler.ShopItems[c.myShopId][i] - 1) == itemID) {
				ShopHandler.ShopItemsN[c.myShopId][i] += amount;
				Added = true;
			}
		}
		if (Added == false) {
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[c.myShopId][i] == 0) {
					ShopHandler.ShopItems[c.myShopId][i] = (itemID + 1);
					ShopHandler.ShopItemsN[c.myShopId][i] = amount;
					ShopHandler.ShopItemsDelay[c.myShopId][i] = 0;
					break;
				}
			}
		}
		return true;
	}

	public boolean buyItem(int itemID, int fromSlot, int amount) {
		if (!shopSellsItem(itemID)) {
			return false;
		}

		if (amount > 0) {
			if (amount > ShopHandler.ShopItemsN[c.myShopId][fromSlot]) {
				amount = ShopHandler.ShopItemsN[c.myShopId][fromSlot];
			}

			final Currency shopType = getShopType(c.myShopId);
			switch (shopType) {

			}
			c.getEquipment().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return false;
	}
}
