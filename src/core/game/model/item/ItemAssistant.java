package core.game.model.item;

import core.Configuration;
import core.Server;
import core.game.GameConstants;
import core.game.model.entity.mob.MobHandler;
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
	
	public int getItemId(int itemId) {
		return ItemDefinition.getDefinitions()[itemId].getId();
	}
	
	/**
	 * Getting un-noted items.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getUnnotedItem(int itemId) {
		return ItemDefinition.getDefinitions()[itemId].getId() - 1;
	}
	
	/**
	 * Send the items kept on death.
	 */
	public void sendItemsKept() {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(53);
				c.getOutStream().writeShort(6963);
				c.getOutStream().writeShort(c.itemKeptId.length);
				for (int i = 0; i < c.itemKeptId.length; i++) {
					if (c.playerItemsN[i] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeInt_v2(1);
					} else {
						c.getOutStream().writeByte(1);
					}
					if (c.itemKeptId[i] > 0) {
						c.getOutStream().writeLEShortA(
								c.itemKeptId[i] + 1);
					} else {
						c.getOutStream().writeLEShortA(0);
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
		if (itemId == -1) {
			return true;
		}
		for (int index = 0; index < Configuration.UNTRADEABLE_ITEMS.length; index++) {
			if (ItemDefinition.getDefinitions()[itemId].getName() == Configuration.UNTRADEABLE_ITEMS[index])
				return false;
		}
		return true;
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
	 * Reseting your bank.
	 */
	public void resetBank() {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeShort(5382); // Bank
			c.getOutStream().writeShort(GameConstants.BANK_SIZE);
			for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
				if (c.bankItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeInt_v2(c.bankItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.bankItemsN[i]);
				}
				if (c.bankItemsN[i] < 1) {
					c.bankItems[i] = 0;
				}
				if (c.bankItems[i] > GameConstants.ITEM_LIMIT || c.bankItems[i] < 0) {
					c.bankItems[i] = GameConstants.ITEM_LIMIT;
				}
				c.getOutStream().writeLEShortA(c.bankItems[i]);
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
			c.getOutStream().writeShort(5064);
			c.getOutStream().writeShort(itemCount + 1);
			for (int i = 0; i < itemCount + 1; i++) {
				if (c.playerItemsN[i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeInt_v2(c.playerItemsN[i]);
				} else {
					c.getOutStream().writeByte(c.playerItemsN[i]);
				}
				if (c.playerItems[i] > GameConstants.ITEM_LIMIT
						|| c.playerItems[i] < 0) {
					c.playerItems[i] = GameConstants.ITEM_LIMIT;
				}
				c.getOutStream().writeLEShortA(c.playerItems[i]);
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
		if (c.isTrading) {
			c.sendMessage("You can't store items while trading!");
			return false;
		}
		if (c.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!Item.itemIsNote(c.playerItems[fromSlot] - 1)) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (c.getInventory().getStackable(c.playerItems[fromSlot] - 1)
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
				} else if (alreadyInBank) {
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
		} else if (Item.itemIsNote(c.playerItems[fromSlot] - 1)
				&& !Item.itemIsNote(c.playerItems[fromSlot] - 2)) {
			if (c.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (c.getInventory().getStackable(c.playerItems[fromSlot - 1])
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
					if (c.getInventory().getStackable(c.bankItems[fromSlot] - 1)) {
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
						&& Item.itemIsNote(c.bankItems[fromSlot])) {
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
					if (c.getInventory().getStackable(c.bankItems[fromSlot] - 1)) {
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
				c.getOutStream().writeShort(1688);
				c.getOutStream().writeByte(c.playerArrows);
				c.getOutStream().writeShort(
						c.playerEquipment[c.playerArrows] + 1);
				if (c.playerEquipmentN[c.playerArrows] - 1 > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeInt(
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
		int enemyX = MobHandler.npcs[c.oldNpcIndex].getX();
		int enemyY = MobHandler.npcs[c.oldNpcIndex].getY();
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
			c.getOutStream().writeLEShortA(itemID);
			c.getOutStream().writeShort(itemAmount);
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
			c.getOutStream().writeShort(itemID);
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
}