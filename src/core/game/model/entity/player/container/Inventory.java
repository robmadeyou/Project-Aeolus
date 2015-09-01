package core.game.model.entity.player.container;

import java.util.stream.IntStream;

import core.game.GameConstants;
import core.game.model.entity.player.Player;
import core.game.model.item.Item;
import core.game.model.item.ItemDefinition;

public class Inventory {
	
	private Player player;
	
	public Inventory(Player player) {
		this.player = player;
	}
	
	/**
	 * Empties all of (a) player's items.
	 */
	public void resetItems(int WriteFrame) {
		synchronized (player) {
			if (player.getOutStream() != null && player != null) {
				player.getOutStream().createFrameVarSizeWord(53);
				player.getOutStream().writeShort(WriteFrame);
				player.getOutStream().writeShort(player.playerItems.length);
				IntStream.range(0, player.playerItems.length).forEach(item -> {
					if (player.playerItemsN[item] > 254) {
						player.getOutStream().writeByte(255);
						player.getOutStream().writeInt_v2(player.playerItemsN[item]);
					} else {
						player.getOutStream().writeByte(player.playerItemsN[item]);
					}
					player.getOutStream().writeLEShortA(player.playerItems[item]);
				});
				player.getOutStream().endFrameVarSizeWord();
				player.flushOutStream();
			}
		}
	}

	/**
	 * Replaces an item in a players inventory
	 */
	public void replaceItem(Player c, int i, int l) {
		IntStream.range(0, c.playerItems.length).forEach(item -> {
			if (playerHasItem(i, 1)) {
				deleteItem(i, c.getEquipment().getItemSlot(i), 1);
				addItem(l, 1);
			}
		});
	}
	
	/**
	 * Counts (a) player's items.
	 * 
	 * @param itemID
	 * @return count start
	 */
	public int getItemCount(int itemID) {
		int count = 0;
		for (int j = 0; j < player.playerItems.length; j++) {
			if (player.playerItems[j] == itemID + 1) {
				count += player.playerItemsN[j];
			}
		}
		return count;
	}
	
	/**
	 * Gets the total count of (a) player's items.
	 * 
	 * @param itemID
	 * @return
	 */
	public int getTotalCount(int itemID) {
		int count = 0;
		for (int j = 0; j < player.playerItems.length; j++) {
			if (Item.itemIsNote(itemID + 1)) {
				if (itemID + 2 == player.playerItems[j])
					count += player.playerItemsN[j];
			}
			if (!Item.itemIsNote(itemID + 1)) {
				if (itemID + 1 == player.playerItems[j]) {
					count += player.playerItemsN[j];
				}
			}
		}
		for (int j = 0; j < player.bankItems.length; j++) {
			if (player.bankItems[j] == itemID + 1) {
				count += player.bankItemsN[j];
			}
		}
		return count;
	}
	
	/**
	 * Checks if you have a free slot.
	 * 
	 * @return
	 */
	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}
	
	/**
	 * Adds an item to a player's inventory.
	 **/
	public boolean addItem(int item, int amount) {
		synchronized (player) {
			if (amount < 1) {
				amount = 1;
			}
			if (item <= 0) {
				return false;
			}
			if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && this.getStackable(item))
					|| ((freeSlots() > 0) && !this.getStackable(item))) {
				for (int i = 0; i < player.playerItems.length; i++) {
					if ((player.playerItems[i] == (item + 1))
							&& this.getStackable(item)
							&& (player.playerItems[i] > 0)) {
						player.playerItems[i] = (item + 1);
						if (((player.playerItemsN[i] + amount) < GameConstants.MAXITEM_AMOUNT)
								&& ((player.playerItemsN[i] + amount) > -1)) {
							player.playerItemsN[i] += amount;
						} else {
							player.playerItemsN[i] = GameConstants.MAXITEM_AMOUNT;
						}
						if (player.getOutStream() != null && player != null) {
							player.getOutStream().createFrameVarSizeWord(34);
							player.getOutStream().writeShort(3214);
							player.getOutStream().writeByte(i);
							player.getOutStream().writeShort(player.playerItems[i]);
							if (player.playerItemsN[i] > 254) {
								player.getOutStream().writeByte(255);
								player.getOutStream().writeInt(player.playerItemsN[i]);
							} else {
								player.getOutStream().writeByte(player.playerItemsN[i]);
							}
							player.getOutStream().endFrameVarSizeWord();
							player.flushOutStream();
						}
						i = 30;
						return true;
					}
				}
				for (int i = 0; i < player.playerItems.length; i++) {
					if (player.playerItems[i] <= 0) {
						player.playerItems[i] = item + 1;
						if ((amount < GameConstants.MAXITEM_AMOUNT) && (amount > -1)) {
							player.playerItemsN[i] = 1;
							if (amount > 1) {
								addItem(item, amount - 1);
								return true;
							}
						} else {
							player.playerItemsN[i] = GameConstants.MAXITEM_AMOUNT;
						}
						resetItems(3214);
						i = 30;
						return true;
					}
				}
				return false;
			} else {
				resetItems(3214);
				player.sendMessage("Not enough space in your inventory.");
				return false;
			}
		}
	}
	
	public boolean getStackable(int itemId) {
		return ItemDefinition.getDefinitions()[itemId].isStackable();
	}
	
	/**
	 * Checks to see if a player has a certain item in their inventory
	 * 
	 * @param itemID
	 * @param amt
	 * @param slot
	 * @return
	 */
	public boolean playerHasItem(int itemID, int amount, int slot) {
		itemID++;
		int found = 0;
		if (player.playerItems[slot] == (itemID)) {
			for (int i = 0; i < player.playerItems.length; i++) {
				if (player.playerItems[i] == itemID) {
					if (player.playerItemsN[i] >= amount) {
						return true;
					} else {
						found++;
					}
				}
			}
			if (found >= amount) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Checks to see if a player has a certain item in their inventory
	 * @param itemId
	 */
	public boolean playerHasItem(int itemId) {
		itemId++;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] == itemId)
				return true;
		}
		return false;
	}

	/**
	 * Checks to see if a player has a certain item in their inventory
	 * @param itemId
	 * @param amt
	 */
	public boolean playerHasItem(int itemId, int amount) {
		itemId++;
		int found = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] == itemId) {
				if (player.playerItemsN[i] >= amount) {
					return true;
				} else {
					found++;
				}
			}
		}
		if (found >= amount) {
			return true;
		}
		return false;
	}
	
	/**
	 * Moving Items in your bag.
	 * @param from
	 * @param to
	 * @param moveWindow
	 **/
	public void moveItems(int from, int to, int moveWindow) {
		if (moveWindow == 3724) {
			int tempI;
			int tempN;
			tempI = player.playerItems[from];
			tempN = player.playerItemsN[from];

			player.playerItems[from] = player.playerItems[to];
			player.playerItemsN[from] = player.playerItemsN[to];
			player.playerItems[to] = tempI;
			player.playerItemsN[to] = tempN;
		}

		if (moveWindow == 34453 && from >= 0 && to >= 0
				&& from < GameConstants.BANK_SIZE && to < GameConstants.BANK_SIZE
				&& to < GameConstants.BANK_SIZE) {
			int tempI;
			int tempN;
			tempI = player.bankItems[from];
			tempN = player.bankItemsN[from];

			player.bankItems[from] = player.bankItems[to];
			player.bankItemsN[from] = player.bankItemsN[to];
			player.bankItems[to] = tempI;
			player.bankItemsN[to] = tempN;
		}

		if (moveWindow == 34453) {
			player.getItems().resetBank();
		}
		if (moveWindow == 18579) {
			int tempI;
			int tempN;
			tempI = player.playerItems[from];
			tempN = player.playerItemsN[from];

			player.playerItems[from] = player.playerItems[to];
			player.playerItemsN[from] = player.playerItemsN[to];
			player.playerItems[to] = tempI;
			player.playerItemsN[to] = tempN;
			player.getInventory().resetItems(3214);
		}
		player.getItems().resetTempItems();
		if (moveWindow == 3724) {
			resetItems(3214);
		}
	}
	
	/**
	 * Checks to see if a player has a specific amount of a certain item
	 * 
	 * @param itemID
	 * @return
	 */
	public int itemAmount(int itemID) {
		int tempAmount = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] == itemID) {
				tempAmount += player.playerItemsN[i];
			}
		}
		return tempAmount;
	}
	
	/**
	 * Gets the item amount.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getItemAmount(int ItemID) {
		int itemCount = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if ((player.playerItems[i] - 1) == ItemID) {
				itemCount += player.playerItemsN[i];
			}
		}
		return itemCount;
	}
	
	/**
	 * Delete items.
	 * 
	 * @param id
	 * @param amount
	 */
	public void deleteItem(int id, int amount) {
		deleteItem(id, getItemInventorySlot(id), amount);
	}
	
	/**
	 * Deletes an item from a players inventory
	 * 
	 * @param id
	 * @param slot
	 * @param amount
	 */
	public void deleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (player.playerItems[slot] == (id + 1)) {
			if (player.playerItemsN[slot] > amount) {
				player.playerItemsN[slot] -= amount;
			} else {
				player.playerItemsN[slot] = 0;
				player.playerItems[slot] = 0;
			}
			resetItems(3214);
		}
	}
	
	/**
	 * Checks inventory and returns the item ids inventory slot
	 * @param itemId
	 */
	public int getItemInventorySlot(int itemId) {
		if (itemId < 0) {
			return -1;
		}
		for(int item = 0; item < player.playerItems.length; item++) {
			if (player.playerItems[item] == itemId + 1) {
				return item;
			}
		}
		return -1;
	}
}
