package core.game.model.item;

import core.Config;
import core.Server;
import core.game.GameConstants;
import core.game.model.entity.npc.NPCHandler;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.util.Misc;

/**
 * Indicates Several Usage Of Items
 * 
 * @author Sanity Revised by Shawn Notes by Shawn
 */
@SuppressWarnings("all")
public class ItemAssistant {

	private Player c;

	public ItemAssistant(Player client) {
		this.c = client;
	}
	
	public int getItemId(int itemName) {
		return ItemTableManager.forName(itemName).getId();
	}
	
	/**
	 * Getting un-noted items.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getUnnotedItem(int ItemID) {
		return ItemTableManager.forID(ItemID).getId() - 1;
	}
	
	/**
	 * Send the items kept on death.
	 */
	public void sendItemsKept() {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(53);
				c.getOutStream().writeWord(6963);
				c.getOutStream().writeWord(c.itemKeptId.length);
				for (int i = 0; i < c.itemKeptId.length; i++) {
					if (c.playerItemsN[i] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord_v2(1);
					} else {
						c.getOutStream().writeByte(1);
					}
					if (c.itemKeptId[i] > 0) {
						c.getOutStream().writeWordBigEndianA(
								c.itemKeptId[i] + 1);
					} else {
						c.getOutStream().writeWordBigEndianA(0);
					}
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
		}
	}

	/**
	 * Item kept on death
	 **/
	public void keepItem(int keepItem, boolean deleteItem) {
		int value = 0;
		int item = 0;
		int slotId = 0;
		boolean itemInInventory = false;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] - 1 > 0) {
				int inventoryItemValue = c.getShops().getItemShopValue(
						c.playerItems[i] - 1);
				if (inventoryItemValue > value && (!c.invSlot[i])) {
					value = inventoryItemValue;
					item = c.playerItems[i] - 1;
					slotId = i;
					itemInInventory = true;
				}
			}
		}
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			if (c.playerEquipment[i1] > 0) {
				int equipmentItemValue = c.getShops().getItemShopValue(
						c.playerEquipment[i1]);
				if (equipmentItemValue > value && (!c.equipSlot[i1])) {
					value = equipmentItemValue;
					item = c.playerEquipment[i1];
					slotId = i1;
					itemInInventory = false;
				}
			}
		}
		if (itemInInventory) {
			c.invSlot[slotId] = true;
			if (deleteItem) {
				c.getInventory().deleteItem(c.playerItems[slotId] - 1,
						c.getEquipment().getItemSlot(c.playerItems[slotId] - 1), 1);
			}
		} else {
			c.equipSlot[slotId] = true;
			if (deleteItem) {
				c.getEquipment().deleteEquipment(item, slotId);
			}
		}
		c.itemKeptId[keepItem] = item;
	}
	
	/**
	 * Deletes all of a player's items.
	 **/
	public void deleteAllItems() {
		for (int i1 = 0; i1 < c.playerEquipment.length; i1++) {
			c.getEquipment().deleteEquipment(c.playerEquipment[i1], i1);
		}
		for (int i = 0; i < c.playerItems.length; i++) {
			c.getInventory().deleteItem(c.playerItems[i] - 1, c.getEquipment().getItemSlot(c.playerItems[i] - 1),
					c.playerItemsN[i]);
		}
	}

	/**
	 * Reset items kept on death.
	 **/
	public void resetKeepItems() {
		for (int i = 0; i < c.itemKeptId.length; i++) {
			c.itemKeptId[i] = -1;
		}
		for (int i1 = 0; i1 < c.invSlot.length; i1++) {
			c.invSlot[i1] = false;
		}
		for (int i2 = 0; i2 < c.equipSlot.length; i2++) {
			c.equipSlot[i2] = false;
		}
	}

	/**
	 * Drops all items for a killer.
	 **/
	public void dropAllItems() {
		Player o = PlayerHandler.players[c.killerId];

		for (int i = 0; i < c.playerItems.length; i++) {
			if (o != null) {
				if (tradeable(c.playerItems[i] - 1)) {
					Server.itemHandler.createGroundItem(o,
							c.playerItems[i] - 1, c.getX(), c.getY(),
							c.playerItemsN[i], c.killerId);
				} else {
					if (specialCase(c.playerItems[i] - 1))
						Server.itemHandler.createGroundItem(o, 995, c.getX(),
								c.getY(),
								getUntradePrice(c.playerItems[i] - 1),
								c.killerId);
					Server.itemHandler.createGroundItem(c,
							c.playerItems[i] - 1, c.getX(), c.getY(),
							c.playerItemsN[i], c.playerId);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerItems[i] - 1,
						c.getX(), c.getY(), c.playerItemsN[i], c.playerId);
			}
		}
		for (int e = 0; e < c.playerEquipment.length; e++) {
			if (o != null) {
				if (tradeable(c.playerEquipment[e])) {
					Server.itemHandler.createGroundItem(o,
							c.playerEquipment[e], c.getX(), c.getY(),
							c.playerEquipmentN[e], c.killerId);
				} else {
					if (specialCase(c.playerEquipment[e]))
						Server.itemHandler.createGroundItem(o, 995, c.getX(),
								c.getY(),
								getUntradePrice(c.playerEquipment[e]),
								c.killerId);
					Server.itemHandler.createGroundItem(c,
							c.playerEquipment[e], c.getX(), c.getY(),
							c.playerEquipmentN[e], c.playerId);
				}
			} else {
				Server.itemHandler.createGroundItem(c, c.playerEquipment[e],
						c.getX(), c.getY(), c.playerEquipmentN[e], c.playerId);
			}
		}
		if (o != null) {
			Server.itemHandler.createGroundItem(o, 526, c.getX(), c.getY(), 1,
					c.killerId);
		}
	}

	/**
	 * Untradable items with a special currency. (Tokkel, etc)
	 * 
	 * @param item
	 * @return amount
	 */
	public int getUntradePrice(int item) {
		switch (item) {
		case 2518:
		case 2524:
		case 2526:
			return 100000;
		case 2520:
		case 2522:
			return 150000;
		}
		return 0;
	}

	/**
	 * Special items with currency.
	 */
	public boolean specialCase(int itemId) {
		switch (itemId) {
		case 2518:
		case 2520:
		case 2522:
		case 2524:
		case 2526:
			return true;
		}
		return false;
	}

	/**
	 * Voided items. (Not void knight items..)
	 * 
	 * @param itemId
	 */
	public void addToVoidList(int itemId) {
		switch (itemId) {
		case 2518:
			c.voidStatus[0]++;
			break;
		case 2520:
			c.voidStatus[1]++;
			break;
		case 2522:
			c.voidStatus[2]++;
			break;
		case 2524:
			c.voidStatus[3]++;
			break;
		case 2526:
			c.voidStatus[4]++;
			break;
		}
	}

	/**
	 * Handles tradable items.
	 */
	public boolean tradeable(int itemId) {
		for (int j = 0; j < Config.ITEM_TRADEABLE.length; j++) {
			if (itemId == Config.ITEM_TRADEABLE[j])
				return false;
		}
		return true;
	}

	/**
	 * Weapon requirements.
	 **/
	public void getRequirements(String itemName, int itemId) {
		c.attackLevelReq = c.defenceLevelReq = c.strengthLevelReq = c.rangeLevelReq = c.magicLevelReq = 0;
		if (itemName.contains("mystic") || itemName.contains("nchanted")) {
			if (itemName.contains("staff")) {
				c.magicLevelReq = 20;
				c.attackLevelReq = 40;
			} else {
				c.magicLevelReq = 20;
				c.defenceLevelReq = 20;
			}
		}
		if (itemName.contains("infinity")) {
			c.magicLevelReq = 50;
			c.defenceLevelReq = 25;
		}
		if (itemName.contains("splitbark")) {
			c.magicLevelReq = 40;
			c.defenceLevelReq = 40;
		}
		if (itemName.contains("Green")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 40;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Blue")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 50;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Red")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 60;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Black")) {
			if (itemName.contains("hide")) {
				c.rangeLevelReq = 70;
				if (itemName.contains("body"))
					c.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("bronze")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("iron")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("steel")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 5;
			}
			return;
		}
		if (itemName.contains("black")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("vamb") && !itemName.contains("chap")) {
				c.attackLevelReq = c.defenceLevelReq = 10;
			}
			return;
		}
		if (itemName.contains("mithril")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 20;
			}
			return;
		}
		if (itemName.contains("adamant")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				c.attackLevelReq = c.defenceLevelReq = 30;
			}
			return;
		}
		if (itemName.contains("rune")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("'bow")) {
				c.attackLevelReq = c.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("dragon")) {
			if (!itemName.contains("nti-") && !itemName.contains("fire")) {
				c.attackLevelReq = c.defenceLevelReq = 60;
				return;
			}
		}
		if (itemName.contains("crystal")) {
			if (itemName.contains("shield")) {
				c.defenceLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
			}
			return;
		}
		if (itemName.contains("ahrim")) {
			if (itemName.contains("staff")) {
				c.magicLevelReq = 70;
				c.attackLevelReq = 70;
			} else {
				c.magicLevelReq = 70;
				c.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("karil")) {
			if (itemName.contains("crossbow")) {
				c.rangeLevelReq = 70;
			} else {
				c.rangeLevelReq = 70;
				c.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("godsword")) {
			c.attackLevelReq = 75;
		}
		if (itemName.contains("3rd age") && !itemName.contains("amulet")) {
			c.defenceLevelReq = 60;
		}
		if (itemName.contains("Initiate")) {
			c.defenceLevelReq = 20;
		}
		if (itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {

			if (itemName.contains("hammers")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("axe")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("warspear")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else if (itemName.contains("flail")) {
				c.attackLevelReq = 70;
				c.strengthLevelReq = 70;
			} else {
				c.defenceLevelReq = 70;
			}
		}

		switch (itemId) {
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			c.attackLevelReq = 42;
			c.rangeLevelReq = 42;
			c.strengthLevelReq = 42;
			c.magicLevelReq = 42;
			c.defenceLevelReq = 42;
			return;
		case 10551:
		case 2503:
		case 2501:
		case 2499:
		case 1135:
			c.defenceLevelReq = 40;
			return;
		case 11235:
		case 6522:
			c.rangeLevelReq = 60;
			break;
		case 6524:
			c.defenceLevelReq = 60;
			break;
		case 11284:
			c.defenceLevelReq = 75;
			return;
		case 6889:
		case 6914:
			c.magicLevelReq = 60;
			break;
		case 861:
			c.rangeLevelReq = 50;
			break;
		case 10828:
			c.defenceLevelReq = 55;
			break;
		case 11724:
		case 11726:
		case 11728:
			c.defenceLevelReq = 65;
			break;
		case 3751:
		case 3749:
		case 3755:
			c.defenceLevelReq = 40;
			break;

		case 7462:
		case 7461:
			c.defenceLevelReq = 40;
			break;
		case 8846:
			c.defenceLevelReq = 5;
			break;
		case 8847:
			c.defenceLevelReq = 10;
			break;
		case 8848:
			c.defenceLevelReq = 20;
			break;
		case 8849:
			c.defenceLevelReq = 30;
			break;
		case 8850:
			c.defenceLevelReq = 40;
			break;

		case 7460:
			c.defenceLevelReq = 40;
			break;

		case 837:
			c.rangeLevelReq = 61;
			break;

		case 4151:
			c.attackLevelReq = 70;
			return;

		case 6724:
			c.rangeLevelReq = 60;
			return;
		case 4153:
			c.attackLevelReq = 50;
			c.strengthLevelReq = 50;
			return;
		}
	}

	/**
	 * Two handed weapon check.
	 **/
	public boolean is2handed(String itemName, int itemId) {
		if (itemName.contains("ahrim") || itemName.contains("karil")
				|| itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {
			return true;
		}
		if (itemName.contains("longbow") || itemName.contains("shortbow")
				|| itemName.contains("ark bow")) {
			return true;
		}
		if (itemName.contains("crystal")) {
			return true;
		}
		if (itemName.contains("godsword")
				|| itemName.contains("aradomin sword")
				|| itemName.contains("2h") || itemName.contains("spear")) {
			return true;
		}
		switch (itemId) {
		case 6724:
		case 11730:
		case 4153:
		case 6528:
		case 14484:
			return true;
		}
		return false;
	}

	/**
	 * Items in your bank.
	 */
	public void rearrangeBank() {
		int totalItems = 0;
		int highestSlot = 0;
		for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
			if (c.bankItems[i] != 0) {
				totalItems++;
				if (highestSlot <= i) {
					highestSlot = i;
				}
			}
		}

		for (int i = 0; i <= highestSlot; i++) {
			if (c.bankItems[i] == 0) {
				boolean stop = false;

				for (int k = i; k <= highestSlot; k++) {
					if (c.bankItems[k] != 0 && !stop) {
						int spots = k - i;
						for (int j = k; j <= highestSlot; j++) {
							c.bankItems[j - spots] = c.bankItems[j];
							c.bankItemsN[j - spots] = c.bankItemsN[j];
							stop = true;
							c.bankItems[j] = 0;
							c.bankItemsN[j] = 0;
						}
					}
				}
			}
		}

		int totalItemsAfter = 0;
		for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
			if (c.bankItems[i] != 0) {
				totalItemsAfter++;
			}
		}

		if (totalItems != totalItemsAfter)
			c.disconnected = true;
	}

	/**
	 * Items displayed on the armor interface.
	 * 
	 * @param id
	 * @param amount
	 */
	public void itemOnInterface(int id, int amount) {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(2274);
			c.getOutStream().writeWord(1);
			if (amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(amount);
			} else {
				c.getOutStream().writeByte(amount);
			}
			c.getOutStream().writeWordBigEndianA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	/**
	 * Reseting your bank.
	 */
	public void resetBank() {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(5382); // Bank
			c.getOutStream().writeWord(GameConstants.BANK_SIZE);
			for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
				if (c.bankItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.bankItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.bankItemsN[i]);
				}
				if (c.bankItemsN[i] < 1) {
					c.bankItems[i] = 0;
				}
				if (c.bankItems[i] > GameConstants.ITEM_LIMIT || c.bankItems[i] < 0) {
					c.bankItems[i] = GameConstants.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(c.bankItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	/**
	 * Resets temporary worn items. Used in minigames, etc
	 */
	public void resetTempItems() {
		synchronized (c) {
			int itemCount = 0;
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] > -1) {
					itemCount = i;
				}
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(5064);
			c.getOutStream().writeWord(itemCount + 1);
			for (int i = 0; i < itemCount + 1; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.playerItemsN[i]);
				}
				if (c.playerItems[i] > GameConstants.ITEM_LIMIT
						|| c.playerItems[i] < 0) {
					c.playerItems[i] = GameConstants.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	/**
	 * Banking your item.
	 * 
	 * @param itemID
	 * @param fromSlot
	 * @param amount
	 * @return
	 */
	public boolean bankItem(int itemID, int fromSlot, int amount) {
		if (c.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!Item.itemIsNote[c.playerItems[fromSlot] - 1]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
					if (c.bankItems[i] == c.playerItems[fromSlot]) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConstants.BANK_SIZE + 1;
					}
				}

				/*
				 * Checks if you already have the same item in your bank.
				 */
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = GameConstants.BANK_SIZE + 1;
						}
					}
					c.bankItems[toBankSlot] = c.playerItems[fromSlot];
					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((c.bankItemsN[toBankSlot] + amount) <= GameConstants.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					c.getInventory().deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				}

				else if (alreadyInBank) {
					if ((c.bankItemsN[toBankSlot] + amount) <= GameConstants.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
					} else {
						c.sendMessage("Bank full!");
						return false;
					}
					c.getInventory().deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
					if (c.bankItems[i] == c.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConstants.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = GameConstants.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItems[toBankSlot] = c.playerItems[firstPossibleSlot];
							c.bankItemsN[toBankSlot] += 1;
							c.getInventory().deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItemsN[toBankSlot] += 1;
							c.getInventory().deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else if (Item.itemIsNote[c.playerItems[fromSlot] - 1]
				&& !Item.itemIsNote[c.playerItems[fromSlot] - 2]) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[c.playerItems[fromSlot] - 1]
					|| c.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
					if (c.bankItems[i] == (c.playerItems[fromSlot] - 1)) {
						if (c.playerItemsN[fromSlot] < amount)
							amount = c.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConstants.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = GameConstants.BANK_SIZE + 1;
						}
					}
					c.bankItems[toBankSlot] = (c.playerItems[fromSlot] - 1);
					if (c.playerItemsN[fromSlot] < amount) {
						amount = c.playerItemsN[fromSlot];
					}
					if ((c.bankItemsN[toBankSlot] + amount) <= GameConstants.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					c.getInventory().deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((c.bankItemsN[toBankSlot] + amount) <= GameConstants.MAXITEM_AMOUNT
							&& (c.bankItemsN[toBankSlot] + amount) > -1) {
						c.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					c.getInventory().deleteItem((c.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = c.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
					if (c.bankItems[i] == (c.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = GameConstants.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
						if (c.bankItems[i] <= 0) {
							toBankSlot = i;
							i = GameConstants.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItems[toBankSlot] = (c.playerItems[firstPossibleSlot] - 1);
							c.bankItemsN[toBankSlot] += 1;
							c.getInventory().deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < c.playerItems.length; i++) {
							if ((c.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							c.bankItemsN[toBankSlot] += 1;
							c.getInventory().deleteItem((c.playerItems[firstPossibleSlot] - 1),
									firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					c.sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			c.sendMessage("Item not supported " + (c.playerItems[fromSlot] - 1));
			return false;
		}
	}

	/**
	 * Checks if you have free bank slots.
	 */
	public int freeBankSlots() {
		int freeS = 0;
		for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
			if (c.bankItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	/**
	 * Getting items from your bank.
	 * 
	 * @param itemID
	 * @param fromSlot
	 * @param amount
	 */
	public void fromBank(int itemID, int fromSlot, int amount) {
		if (amount > 0) {
			if (c.bankItems[fromSlot] > 0) {
				if (!c.takeAsNote) {
					if (Item.itemStackable[c.bankItems[fromSlot] - 1]) {
						if (c.bankItemsN[fromSlot] > amount) {
							if (c.getInventory().addItem((c.bankItems[fromSlot] - 1), amount)) {
								c.bankItemsN[fromSlot] -= amount;
								resetBank();
								c.getInventory().resetItems(5064);
							}
						} else {
							if (c.getInventory().addItem((c.bankItems[fromSlot] - 1),
									c.bankItemsN[fromSlot])) {
								c.bankItems[fromSlot] = 0;
								c.bankItemsN[fromSlot] = 0;
								resetBank();
								c.getInventory().resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (c.bankItemsN[fromSlot] > 0) {
								if (c.getInventory().addItem((c.bankItems[fromSlot] - 1), 1)) {
									c.bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						c.getInventory().resetItems(5064);
					}
				} else if (c.takeAsNote
						&& Item.itemIsNote[c.bankItems[fromSlot]]) {
					if (c.bankItemsN[fromSlot] > amount) {
						if (c.getInventory().addItem(c.bankItems[fromSlot], amount)) {
							c.bankItemsN[fromSlot] -= amount;
							resetBank();
							c.getInventory().resetItems(5064);
						}
					} else {
						if (c.getInventory().addItem(c.bankItems[fromSlot],
								c.bankItemsN[fromSlot])) {
							c.bankItems[fromSlot] = 0;
							c.bankItemsN[fromSlot] = 0;
							resetBank();
							c.getInventory().resetItems(5064);
						}
					}
				} else {
					c.sendMessage("This item can't be withdrawn as a note.");
					if (Item.itemStackable[c.bankItems[fromSlot] - 1]) {
						if (c.bankItemsN[fromSlot] > amount) {
							if (c.getInventory().addItem((c.bankItems[fromSlot] - 1), amount)) {
								c.bankItemsN[fromSlot] -= amount;
								resetBank();
								c.getInventory().resetItems(5064);
							}
						} else {
							if (c.getInventory().addItem((c.bankItems[fromSlot] - 1),
									c.bankItemsN[fromSlot])) {
								c.bankItems[fromSlot] = 0;
								c.bankItemsN[fromSlot] = 0;
								resetBank();
								c.getInventory().resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (c.bankItemsN[fromSlot] > 0) {
								if (c.getInventory().addItem((c.bankItems[fromSlot] - 1), 1)) {
									c.bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						c.getInventory().resetItems(5064);
					}
				}
			}
		}
	}

	/**
	 * Delete arrows.
	 **/
	public void deleteArrow() {
		synchronized (c) {
			if (c.playerEquipment[c.playerCape] == 10499 && Misc.random(5) != 1
					&& c.playerEquipment[c.playerArrows] != 4740)
				return;
			if (c.playerEquipmentN[c.playerArrows] == 1) {
				c.getEquipment().deleteEquipment(c.playerEquipment[c.playerArrows],
						c.playerArrows);
			}
			if (c.playerEquipmentN[c.playerArrows] != 0) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(c.playerArrows);
				c.getOutStream().writeWord(
						c.playerEquipment[c.playerArrows] + 1);
				if (c.playerEquipmentN[c.playerArrows] - 1 > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(
							c.playerEquipmentN[c.playerArrows] - 1);
				} else {
					c.getOutStream().writeByte(
							c.playerEquipmentN[c.playerArrows] - 1);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
				c.playerEquipmentN[c.playerArrows] -= 1;
			}
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}

	/**
	 * Dropping arrows
	 **/
	public void dropArrowNpc() {
		if (c.playerEquipment[c.playerCape] == 10499)
			return;
		int enemyX = NPCHandler.npcs[c.oldNpcIndex].getX();
		int enemyY = NPCHandler.npcs[c.oldNpcIndex].getY();
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.rangeItemUsed, enemyX, enemyY) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, 1, c.getId());
			} else if (Server.itemHandler.itemAmount(c.rangeItemUsed, enemyX,
					enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(c.rangeItemUsed,
						enemyX, enemyY);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, amount + 1, c.getId());
			}
		}
	}

	/**
	 * Ranging arrows.
	 */
	public void dropArrowPlayer() {
		int enemyX = PlayerHandler.players[c.oldPlayerIndex].getX();
		int enemyY = PlayerHandler.players[c.oldPlayerIndex].getY();
		if (c.playerEquipment[c.playerCape] == 10499)
			return;
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(c.rangeItemUsed, enemyX, enemyY) == 0) {
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, 1, c.getId());
			} else if (Server.itemHandler.itemAmount(c.rangeItemUsed, enemyX,
					enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(c.rangeItemUsed,
						enemyX, enemyY);
				Server.itemHandler.removeGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, false);
				Server.itemHandler.createGroundItem(c, c.rangeItemUsed, enemyX,
						enemyY, amount + 1, c.getId());
			}
		}
	}

	/**
	 * Finds the item.
	 * 
	 * @param id
	 * @param items
	 * @param amounts
	 * @return
	 */
	public int findItem(int id, int[] items, int[] amounts) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if (((items[i] - 1) == id) && (amounts[i] > 0)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Dropping items
	 **/
	public void createGroundItem(int itemID, int itemX, int itemY,
			int itemAmount) {
		synchronized (c) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(44);
			c.getOutStream().writeWordBigEndianA(itemID);
			c.getOutStream().writeWord(itemAmount);
			c.getOutStream().writeByte(0);
			c.flushOutStream();
		}
	}

	/**
	 * Pickup items from the ground.
	 **/
	public void removeGroundItem(int itemID, int itemX, int itemY, int Amount) {
		synchronized (c) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((itemY - 8 * c.mapRegionY));
			c.getOutStream().writeByteC((itemX - 8 * c.mapRegionX));
			c.getOutStream().createFrame(156);
			c.getOutStream().writeByteS(0);
			c.getOutStream().writeWord(itemID);
			c.flushOutStream();
		}
	}

	/**
	 * Checks if a player owns a cape.
	 * 
	 * @return
	 */
	public boolean ownsCape() {
		if (c.getInventory().playerHasItem(2412, 1)
				|| c.getInventory().playerHasItem(2413, 1)
				|| c.getInventory().playerHasItem(2414, 1))
			return true;
		for (int j = 0; j < GameConstants.BANK_SIZE; j++) {
			if (c.bankItems[j] == 2412 || c.bankItems[j] == 2413
					|| c.bankItems[j] == 2414)
				return true;
		}
		if (c.playerEquipment[c.playerCape] == 2413
				|| c.playerEquipment[c.playerCape] == 2414
				|| c.playerEquipment[c.playerCape] == 2415)
			return true;
		return false;
	}

	/**
	 * Checks if the player has all the shards.
	 * 
	 * @return
	 */
	public boolean hasAllShards() {
		return c.getInventory().playerHasItem(11712, 1) && c.getInventory().playerHasItem(11712, 1)
				&& c.getInventory().playerHasItem(11714, 1);
	}

	/**
	 * Makes the godsword blade.
	 */
	public void makeBlade() {
		c.getInventory().deleteItem(11710, 1);
		c.getInventory().deleteItem(11712, 1);
		c.getInventory().deleteItem(11714, 1);
		c.getInventory().addItem(11690, 1);
		c.sendMessage("You combine the shards to make a blade.");
	}

	/**
	 * Makes the godsword.
	 * 
	 * @param i
	 */
	public void makeGodsword(int i) {
		int godsword = i - 8;
		if (c.getInventory().playerHasItem(11690) && c.getInventory().playerHasItem(i)) {
			c.getInventory().deleteItem(11690, 1);
			c.getInventory().deleteItem(i, 1);
			c.getInventory().addItem(i - 8, 1);
			c.sendMessage("You combine the hilt and the blade to make a godsword.");
		}
	}

	/**
	 * Checks if the item is a godsword hilt.
	 * 
	 * @param i
	 * @return
	 */
	public boolean isHilt(int i) {
		return i >= 11702 && i <= 11708 && i % 2 == 0;
	}

}