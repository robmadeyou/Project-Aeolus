package core.game.model.entity.player.container;

import core.Config;
import core.game.GameConstants;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.entity.player.save.PlayerSave;
import core.game.model.item.Item;
import core.game.model.item.ItemTableManager;
import core.game.model.item.PlayerItem;

public class Equipment {

	private Player c;

	public Equipment(Player c) {
		this.c = c;
	}

	/**
	 * slot shield
	 */
	public static final String[] SHIELDS = { "spirit shield", "defender",
			"farseer" };
	/**
	 * slot weapon
	 */
	public static final String[] WEAPONS = { "rapier", "maul", "staff",
			"longsword" };
	
	/**
	 * Checks if player is wearing an Anti-Fire Shield
	 */
	
	public boolean isWearingAntiFire(Player c) {
		if (c.playerEquipment != null && (c.playerEquipment[c.playerShield] == 1540 || c.playerEquipment[c.playerShield] == 11283)) {
			return true;
		}		
		return false;		
	}
	
	/**
	 * Checks to see if player is wearing full Dharoks
	 */
	public boolean isWearingDharoks(Player c) {
		if ((c.playerEquipment != null) 
				&& (c.playerEquipment[c.playerWeapon] == 4718 || c.playerEquipment[c.playerWeapon] == 4886 || c.playerEquipment[c.playerWeapon] == 4887 || c.playerEquipment[c.playerWeapon] == 4888 || c.playerEquipment[c.playerWeapon] == 4889)
				&& (c.playerEquipment[c.playerHat] == 4716 || c.playerEquipment[c.playerHat] == 4880 || c.playerEquipment[c.playerHat] == 4881 || c.playerEquipment[c.playerHat] == 4882 || c.playerEquipment[c.playerHat] == 4883)
				&& (c.playerEquipment[c.playerChest] == 4720 || c.playerEquipment[c.playerChest] == 4892 || c.playerEquipment[c.playerChest] == 4893 || c.playerEquipment[c.playerChest] == 4894 || c.playerEquipment[c.playerChest] == 4895)
				&& (c.playerEquipment[c.playerLegs] == 4722 || c.playerEquipment[c.playerLegs] == 4898 || c.playerEquipment[c.playerLegs] == 4899 || c.playerEquipment[c.playerLegs] == 4900 || c.playerEquipment[c.playerLegs] == 4901)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if player is wearing full Veracs
	 */
	public boolean isWearingVeracs(Player c) {
		if ((c.playerEquipment != null) 
				&& (c.playerEquipment[c.playerWeapon] == 4755 || c.playerEquipment[c.playerWeapon] == 4982 || c.playerEquipment[c.playerWeapon] == 4983 || c.playerEquipment[c.playerWeapon] == 4984 || c.playerEquipment[c.playerWeapon] == 4985)
				&& (c.playerEquipment[c.playerHat] == 4753 || c.playerEquipment[c.playerHat] == 4976 || c.playerEquipment[c.playerHat] == 4977 || c.playerEquipment[c.playerHat] == 4978 || c.playerEquipment[c.playerHat] == 4979)
				&& (c.playerEquipment[c.playerChest] == 4757 || c.playerEquipment[c.playerChest] == 4988 || c.playerEquipment[c.playerChest] == 4989 || c.playerEquipment[c.playerChest] == 4990 || c.playerEquipment[c.playerChest] == 4991)
				&& (c.playerEquipment[c.playerLegs] == 4759 || c.playerEquipment[c.playerLegs] == 4994 || c.playerEquipment[c.playerLegs] == 4995 || c.playerEquipment[c.playerLegs] == 4996 || c.playerEquipment[c.playerLegs] == 4997)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if player is wearing full Guthans
	 */
	public boolean isWearingGuthans(Player c) {
		if ((c.playerEquipment != null) 
				&& (c.playerEquipment[c.playerWeapon] == 4726 || c.playerEquipment[c.playerWeapon] == 4910 || c.playerEquipment[c.playerWeapon] == 4911 || c.playerEquipment[c.playerWeapon] == 4912 || c.playerEquipment[c.playerWeapon] == 4913)
				&& (c.playerEquipment[c.playerHat] == 4724 || c.playerEquipment[c.playerHat] == 4904 || c.playerEquipment[c.playerHat] == 4905 || c.playerEquipment[c.playerHat] == 4906 || c.playerEquipment[c.playerHat] == 4907)
				&& (c.playerEquipment[c.playerChest] == 4728 || c.playerEquipment[c.playerChest] == 4916 || c.playerEquipment[c.playerChest] == 4917 || c.playerEquipment[c.playerChest] == 4918 || c.playerEquipment[c.playerChest] == 4919)
				&& (c.playerEquipment[c.playerLegs] == 4730 || c.playerEquipment[c.playerLegs] == 4922 || c.playerEquipment[c.playerLegs] == 4923 || c.playerEquipment[c.playerLegs] == 4924 || c.playerEquipment[c.playerLegs] == 4925)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if player is wearing full Melee void
	 */
	public static boolean fullVoidMelee(Player c) {
		return c.playerEquipment != null && c.playerEquipment[c.playerHat] == 11665 && c.playerEquipment[c.playerChest] == 8839 && c.playerEquipment[c.playerLegs] == 8840 && c.playerEquipment[c.playerHands] == 8842;
	}
	
	/**
	 * Checks to see if player is wearing full Range void
	 */
	public static boolean fullVoidRange(Player c) {
		return c.playerEquipment != null && c.playerEquipment[c.playerHat] == 11664 && c.playerEquipment[c.playerChest] == 8839 && c.playerEquipment[c.playerLegs] == 8840 && c.playerEquipment[c.playerHands] == 8842;
	}
	
	/**
	 * Checks to see if player is wearing full Mage void
	 */
	public static boolean fullVoidMage(Player c) {
		return c.playerEquipment != null && c.playerEquipment[c.playerHat] == 11663 && c.playerEquipment[c.playerChest] == 8839 && c.playerEquipment[c.playerLegs] == 8840 && c.playerEquipment[c.playerHands] == 8842;
	}
	
	/**
	 * Gets the bonus' of an item.
	 */
	public void writeBonus() {
		int offset = 0;
		String send = "";
		for (int i = 0; i < c.playerBonus.length; i++) {
			if (c.playerBonus[i] >= 0) {
				send = BONUS_NAMES[i] + ": +" + c.playerBonus[i];
			} else {
				send = BONUS_NAMES[i] + ": -"
						+ java.lang.Math.abs(c.playerBonus[i]);
			}

			if (i == 10) {
				offset = 1;
			}
			c.getPA().sendFrame126(send, (1675 + i + offset));
		}

	}

	/**
	 * Gets the item name from the item json
	 */
	public static String getItemName(int ItemID) {
		return ItemTableManager.forID(ItemID).getName();
	}

	/**
	 * Gets the item type.
	 */
	public String itemType(int item) {
		if (Item.playerCapes(item)) {
			return "cape";
		}
		if (Item.playerBoots(item)) {
			return "boots";
		}
		if (Item.playerGloves(item)) {
			return "gloves";
		}
		if (Item.playerShield(item)) {
			return "shield";
		}
		if (Item.playerAmulet(item)) {
			return "amulet";
		}
		if (Item.playerArrows(item)) {
			return "arrows";
		}
		if (Item.playerRings(item)) {
			return "ring";
		}
		if (Item.playerHats(item)) {
			return "hat";
		}
		if (Item.playerLegs(item)) {
			return "legs";
		}
		if (Item.playerBody(item)) {
			return "body";
		}
		return "weapon";
	}

	/**
	 * Item bonuses.
	 **/
	public final String[] BONUS_NAMES = { "Stab", "Slash", "Crush", "Magic",
			"Range", "Stab", "Slash", "Crush", "Magic", "Range", "Strength",
			"Prayer" };

	/**
	 * Resets item bonuses.
	 */
	public void resetBonus() {
		for (int i = 0; i < c.playerBonus.length; i++) {
			c.playerBonus[i] = 0;
		}
	}

	/**
	 * Weapon requirements.
	 **/
	public void getRequirements(String itemName, int itemId) {
		c.attackLevelReq = c.defenceLevelReq = c.strengthLevelReq = c.rangeLevelReq = c.magicLevelReq = 0;
		int[] reqLvl = ItemTableManager.forID(itemId).getLevelRequirements();
		c.attackLevelReq = reqLvl[0];// attack
		c.defenceLevelReq = reqLvl[1];// defence
		c.strengthLevelReq = reqLvl[2];// strength
		c.rangeLevelReq = reqLvl[3];// range
		c.magicLevelReq = reqLvl[4];// magic
	}

	/**
	 * Finds the item.
	 * 
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
	 * Updates the slot when wielding an item.
	 * 
	 * @Param slot
	 */
	public void updateSlot(int slot) {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(slot);
				c.getOutStream().writeWord(c.playerEquipment[slot] + 1);
				if (c.playerEquipmentN[slot] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerEquipmentN[slot]);
				} else {
					c.getOutStream().writeByte(c.playerEquipmentN[slot]);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
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
		case 11838:
		case 4153:
		case 6528:
		case 10887:
		case 11777:
			return true;
		}
		return false;
	}

	/**
	 * Wielding items.
	 **/
	@SuppressWarnings("static-access")
	public boolean wearItem(int wearID, int slot) {
		int targetSlot = 0;
		boolean canWearItem = true;
		PlayerItem item = ItemTableManager.forID(wearID);
		if (c.playerItems[slot] == (wearID + 1)) {
			getRequirements(getItemName(wearID).toLowerCase(), wearID);
			String wearSlot = item.getEquipSlot();
			targetSlot = Item.targetSlots[wearID];
			if (wearSlot.equalsIgnoreCase("cape")) {
				targetSlot = 1;
			} else if (wearSlot.equalsIgnoreCase("hat")) {
				targetSlot = 0;
			} else if (wearSlot.equalsIgnoreCase("amulet")) {
				targetSlot = 2;
			} else if (wearSlot.equalsIgnoreCase("arrows")) {
				targetSlot = 13;
			} else if (wearSlot.equalsIgnoreCase("body")) {
				targetSlot = 4;
			} else if (wearSlot.equalsIgnoreCase("shield")) {
				targetSlot = 5;
			} else if (wearSlot.equalsIgnoreCase("legs")) {
				targetSlot = 7;
			} else if (wearSlot.equalsIgnoreCase("gloves")) {
				targetSlot = 9;
			} else if (wearSlot.equalsIgnoreCase("boots")) {
				targetSlot = 10;
			} else if (wearSlot.equalsIgnoreCase("ring")) {
				targetSlot = 12;
			} else {
				targetSlot = 3;

			}
			
			switch (wearID) {			
			
			/* Gloves */
			case 10336:
			case 9922:
			case 8842:
				targetSlot = 9;
				break;
			/* Arrows */
			case 9144:
				targetSlot = 13;
				break;
			/* Capes */
			case 9813:
			case 9747:
			case 9748:
			case 9750:
			case 9790:
			case 9751:
			case 9753:
			case 9754:
			case 9756:
			case 9757:
			case 9759:
			case 9760:
			case 9762:
			case 9763:
			case 9765:
			case 9766:
			case 9768:
			case 9769:
			case 9771:
			case 9772:
			case 9774:
			case 9775:
			case 10446:
			case 10448:
			case 10450:
			case 9777:
			case 9778:
			case 9780:
			case 9781:
			case 9783:
			case 9784:
			case 9786:
			case 9787:
			case 9792:
			case 9793:
			case 9795:
			case 9796:
			case 9798:
			case 9799:
			case 9801:
			case 9802:
			case 9804:
			case 9805:
			case 9807:
			case 9808:
			case 9810:
			case 9811:
			case 10499:
				targetSlot = 1;
				break;

			/* Arrows */
			case 9244:
				targetSlot = 13;
				break;

			/* Boots */
			case 15037:
			case 14605:
			case 11019:
			case 9921:
			case 11728:
			case 10839:
				targetSlot = 10;
				break;

			/* Legs */
			case 11726:
			case 11722:
			case 9678:
			case 9923:
			case 9676:
			case 10394:
			case 8840:
			case 15035:
			case 10332:
			case 15036:
			case 14603:
			case 14938:
			case 14077:
			case 10346:
			case 10372:
			case 10838:
			case 11022:
			case 10388:
			case 10380:
			case 10340:
			case 15425:
			case 13360:
			case 13352:
			case 13346:
				targetSlot = 7;
				break;

			/* Amulets */
			case 6861:
			case 6859:
			case 6863:
			case 9470:
			case 6857:
			case 10344:
			case 11128:
				targetSlot = 2;
				break;

			/* Shields */
			case 8850:
			case 8849:
			case 8848:
			case 8847:
			case 8846:
			case 8845:
			case 8844:
			case 11283:
			case 10352:
				targetSlot = 5;
				break;

			/* Bodies */
			case 10551:
			case 10348:
			case 9674:
			case 10837:
			case 14936:
			case 15034:
			case 10386:
			case 10370:
			case 11720:
			case 10330:
			case 15423:
			case 14076:
			case 11020:
			case 14595:
			case 8839:
			case 10338:
			case 13348:
			case 13354:
			case 13358:
			case 9924:
			case 11724:
				targetSlot = 4;
				break;

			/* Helms */
			case 13263:
			case 9920:
			case 10507:
			case 10836:
			case 10828:
			case 9672:
			case 10334:
			case 10350:
			case 10390:
			case 11718:
			case 10374:
			case 11021:
			case 15422:
			case 15033:
			case 9925:
			case 13362:
			case 11663:
			case 11664:
			case 11665:
			case 13355:
			case 13350:
			case 10342:
			case 1037:
			case 11335:
			case 10548:
			case 9749:
			case 9752:
			case 9755:
			case 9758:
			case 9761:
			case 9764:
			case 9767:
			case 9770:
			case 9773:
			case 9776:
			case 9779:
			case 9782:
			case 9785:
			case 9788:
			case 9791:
			case 9794:
			case 9797:
			case 9800:
			case 9803:
			case 9806:
			case 9809:
			case 9812:
				targetSlot = 0;
				break;

			/* Boots */
			case 11732:
				targetSlot = 10;
				break;
			}

			if (c.duelRule[11] && targetSlot == 0) {
				c.sendMessage("Wearing hats has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[12] && targetSlot == 1) {
				c.sendMessage("Wearing capes has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[13] && targetSlot == 2) {
				c.sendMessage("Wearing amulets has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[14] && targetSlot == 3) {
				c.sendMessage("Wielding weapons has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[15] && targetSlot == 4) {
				c.sendMessage("Wearing bodies has been disabled in this duel!");
				return false;
			}
			if ((c.duelRule[16] && targetSlot == 5)
					|| (c.duelRule[16] && is2handed(getItemName(wearID)
							.toLowerCase(), wearID))) {
				c.sendMessage("Wearing shield has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[17] && targetSlot == 7) {
				c.sendMessage("Wearing legs has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[18] && targetSlot == 9) {
				c.sendMessage("Wearing gloves has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[19] && targetSlot == 10) {
				c.sendMessage("Wearing boots has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[20] && targetSlot == 12) {
				c.sendMessage("Wearing rings has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[21] && targetSlot == 13) {
				c.sendMessage("Wearing arrows has been disabled in this duel!");
				return false;
			}

			if (Config.itemRequirements) {
				if (targetSlot == 10 || targetSlot == 7 || targetSlot == 5
						|| targetSlot == 4 || targetSlot == 0
						|| targetSlot == 9 || targetSlot == 10) {
					if (c.defenceLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[1]) < c.defenceLevelReq) {
							c.sendMessage("You need a defence level of "
									+ c.defenceLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
					if (c.rangeLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[4]) < c.rangeLevelReq) {
							c.sendMessage("You need a range level of "
									+ c.rangeLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
					if (c.magicLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[6]) < c.magicLevelReq) {
							c.sendMessage("You need a magic level of "
									+ c.magicLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
				}
				if (targetSlot == 3) {
					if (c.attackLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[0]) < c.attackLevelReq) {
							c.sendMessage("You need an attack level of "
									+ c.attackLevelReq
									+ " to wield this weapon.");
							canWearItem = false;
						}
					}
					if (c.rangeLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[4]) < c.rangeLevelReq) {
							c.sendMessage("You need a range level of "
									+ c.rangeLevelReq
									+ " to wield this weapon.");
							canWearItem = false;
						}
					}
					if (c.magicLevelReq > 0) {
						if (c.getPA().getLevelForXP(c.playerXP[6]) < c.magicLevelReq) {
							c.sendMessage("You need a magic level of "
									+ c.magicLevelReq
									+ " to wield this weapon.");
							canWearItem = false;
						}
					}
				}
			}

			if (!canWearItem) {
				return false;
			}

			int wearAmount = c.playerItemsN[slot];
			if (wearAmount < 1) {
				return false;
			}

			if (targetSlot == c.playerWeapon) {
				c.autocasting = false;
				c.autocastId = 0;
				c.getPA().sendFrame36(108, 0);
			}

			if (slot >= 0 && wearID >= 0) {
				int toEquip = c.playerItems[slot];
				int toEquipN = c.playerItemsN[slot];
				int toRemove = c.playerEquipment[targetSlot];
				int toRemoveN = c.playerEquipmentN[targetSlot];
				if (toEquip == toRemove + 1 && Item.itemStackable[toRemove]) {
					deleteItem(toRemove, getItemSlot(toRemove), toEquipN);
					c.playerEquipmentN[targetSlot] += toEquipN;
				} else if (targetSlot != 5 && targetSlot != 3) {
					c.playerItems[slot] = toRemove + 1;
					c.playerItemsN[slot] = toRemoveN;
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 5) {
					boolean wearing2h = is2handed(
							getItemName(c.playerEquipment[c.playerWeapon])
									.toLowerCase(),
							c.playerEquipment[c.playerWeapon]);
					@SuppressWarnings("unused")
					boolean wearingShield = c.playerEquipment[c.playerShield] > 0;
					if (wearing2h) {
						toRemove = c.playerEquipment[c.playerWeapon];
						toRemoveN = c.playerEquipmentN[c.playerWeapon];
						c.playerEquipment[c.playerWeapon] = -1;
						c.playerEquipmentN[c.playerWeapon] = 0;
						c.getEquipment().updateSlot(c.playerWeapon);
					}
					c.playerItems[slot] = toRemove + 1;
					c.playerItemsN[slot] = toRemoveN;
					c.playerEquipment[targetSlot] = toEquip - 1;
					c.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 3) {
					boolean is2h = is2handed(getItemName(wearID).toLowerCase(),
							wearID);
					boolean wearingShield = c.playerEquipment[c.playerShield] > 0;
					boolean wearingWeapon = c.playerEquipment[c.playerWeapon] > 0;
					if (is2h) {
						if (wearingShield && wearingWeapon) {
							if (c.getEquipment().freeSlots() > 0) {
								c.playerItems[slot] = toRemove + 1;
								c.playerItemsN[slot] = toRemoveN;
								c.playerEquipment[targetSlot] = toEquip - 1;
								c.playerEquipmentN[targetSlot] = toEquipN;
								removeItem(c.playerEquipment[c.playerShield],
										c.playerShield);
							} else {
								c.sendMessage("You do not have enough inventory space to do this.");
								return false;
							}
						} else if (wearingShield && !wearingWeapon) {
							c.playerItems[slot] = c.playerEquipment[c.playerShield] + 1;
							c.playerItemsN[slot] = c.playerEquipmentN[c.playerShield];
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
							c.playerEquipment[c.playerShield] = -1;
							c.playerEquipmentN[c.playerShield] = 0;
							c.getEquipment().updateSlot(c.playerShield);
						} else {
							c.playerItems[slot] = toRemove + 1;
							c.playerItemsN[slot] = toRemoveN;
							c.playerEquipment[targetSlot] = toEquip - 1;
							c.playerEquipmentN[targetSlot] = toEquipN;
						}
					} else {
						c.playerItems[slot] = toRemove + 1;
						c.playerItemsN[slot] = toRemoveN;
						c.playerEquipment[targetSlot] = toEquip - 1;
						c.playerEquipmentN[targetSlot] = toEquipN;
					}
				}
				c.isFullHelm = Item.isFullHelm(c.playerEquipment[c.playerHat]);
				c.isFullMask = Item.isFullMask(c.playerEquipment[c.playerHat]);
				c.isFullBody = Item
						.isFullBody(c.playerEquipment[c.playerChest]);
				resetItems(3214);
			}
			if (targetSlot == 3) {
				c.usingSpecial = false;
				addSpecialBar(wearID);
			}
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(targetSlot);
				c.getOutStream().writeWord(wearID + 1);

				if (c.playerEquipmentN[targetSlot] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(c.playerEquipmentN[targetSlot]);
				} else {
					c.getOutStream().writeByte(c.playerEquipmentN[targetSlot]);
				}

				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
			sendWeapon(c.playerEquipment[c.playerWeapon],
					getItemName(c.playerEquipment[c.playerWeapon]));
			c.getEquipment().resetBonus();
			c.getEquipment().getBonus();
			c.getEquipment().writeBonus();
			c.getCombat().getPlayerAnimIndex(c.getEquipment().getItemName(c.playerEquipment[c.playerWeapon])
							.toLowerCase());
			c.getPA().requestUpdates();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Indicates the action to wear an item.
	 * 
	 * @param wearID
	 * @param wearAmount
	 * @param targetSlot
	 */
	public void wearItem(int wearID, int wearAmount, int targetSlot) {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(targetSlot);
				c.getOutStream().writeWord(wearID + 1);

				if (wearAmount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(wearAmount);
				} else {
					c.getOutStream().writeByte(wearAmount);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
				c.playerEquipment[targetSlot] = wearID;
				c.playerEquipmentN[targetSlot] = wearAmount;
				this.sendWeapon(
						c.playerEquipment[c.playerWeapon],
						c.getEquipment().getItemName(
								c.playerEquipment[c.playerWeapon]));
				this.resetBonus();
				this.getBonus();
				this.writeBonus();
				c.getCombat().getPlayerAnimIndex(c.getEquipment()
								.getItemName(c.playerEquipment[c.playerWeapon])
								.toLowerCase());
				c.updateRequired = true;
				c.setAppearanceUpdateRequired(true);
			}
		}
	}

	public void deleteItem(int id, int amount) {
		deleteItem(id, getItemSlot(id), amount);
	}

	public void deleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (c.playerItems[slot] == (id + 1)) {
			if (c.playerItemsN[slot] > amount) {
				c.playerItemsN[slot] -= amount;
			} else {
				c.playerItemsN[slot] = 0;
				c.playerItems[slot] = 0;
			}
			PlayerSave.saveGame(c);
			resetItems(3214);
		}
	}

	/**
	 * Gets the item slot.
	 * 
	 * @Param ItemID
	 * @return
	 */
	public int getItemSlot(int ItemID) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if ((c.playerItems[i] - 1) == ItemID) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks if you have a free slot.
	 * 
	 * @return
	 */
	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	/**
	 * Gets the item bonus from the item json.
	 */
	public void getBonus() {
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > -1) {

				for (int k = 0; k < c.playerBonus.length; k++) {
					c.playerBonus[k] += ItemTableManager.forID(
							c.playerEquipment[i]).getBonusById(k);
				}

			}
		}
	}

	/**
	 * Weapon type.
	 **/
	public void sendWeapon(int Weapon, String WeaponName) {
		String WeaponName2 = WeaponName.replaceAll("Bronze", "");
		WeaponName2 = WeaponName2.replaceAll("Iron", "");
		WeaponName2 = WeaponName2.replaceAll("Steel", "");
		WeaponName2 = WeaponName2.replaceAll("Black", "");
		WeaponName2 = WeaponName2.replaceAll("Mithril", "");
		WeaponName2 = WeaponName2.replaceAll("Adamant", "");
		WeaponName2 = WeaponName2.replaceAll("Rune", "");
		WeaponName2 = WeaponName2.replaceAll("Granite", "");
		WeaponName2 = WeaponName2.replaceAll("Dragon", "");
		WeaponName2 = WeaponName2.replaceAll("Drag", "");
		WeaponName2 = WeaponName2.replaceAll("Crystal", "");
		WeaponName2 = WeaponName2.trim();
		/**
		 * Attack styles.
		 */
		if (WeaponName.equals("Unarmed")) {
			c.setSidebarInterface(0, 5855); // punch, kick, block
			c.getPA().sendFrame126(WeaponName, 5857);
		} else if (WeaponName.endsWith("whip")
				|| WeaponName.contains("tentacle")) {
			c.setSidebarInterface(0, 12290); // flick, lash, deflect
			c.getPA().sendFrame246(12291, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 12293);
		} else if (WeaponName.endsWith("bow") || WeaponName.endsWith("10")
				|| WeaponName.endsWith("full")
				|| WeaponName.startsWith("seercull")) {
			c.setSidebarInterface(0, 1764); // accurate, rapid, longrange
			c.getPA().sendFrame246(1765, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 1767);
		} else if (WeaponName.startsWith("Staff")
				|| WeaponName.endsWith("seas") || WeaponName.endsWith("staff")
				|| WeaponName.endsWith("wand")) {
			c.setSidebarInterface(0, 328); // spike, impale, smash, block
			c.getPA().sendFrame246(329, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 331);
		} else if (WeaponName2.startsWith("dart")
				|| WeaponName2.startsWith("knife")
				|| WeaponName2.startsWith("javelin")
				|| WeaponName.equalsIgnoreCase("toktz-xil-ul")) {
			c.setSidebarInterface(0, 4446); // accurate, rapid, longrange
			c.getPA().sendFrame246(4447, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 4449);
		} else if (WeaponName2.startsWith("dagger")
				|| WeaponName2.contains("anchor")
				|| WeaponName2.contains("sword")) {
			c.setSidebarInterface(0, 2276); // stab, lunge, slash, block
			c.getPA().sendFrame246(2277, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 2279);
		} else if (WeaponName2.startsWith("pickaxe")) {
			c.setSidebarInterface(0, 5570); // spike, impale, smash, block
			c.getPA().sendFrame246(5571, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 5573);
		} else if (WeaponName2.startsWith("axe")
				|| WeaponName2.startsWith("battleaxe")) {
			c.setSidebarInterface(0, 1698); // chop, hack, smash, block
			c.getPA().sendFrame246(1699, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 1701);
		} else if (WeaponName2.startsWith("halberd")) {
			c.setSidebarInterface(0, 8460); // jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.startsWith("Scythe")) {
			c.setSidebarInterface(0, 8460); // jab, swipe, fend
			c.getPA().sendFrame246(8461, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.startsWith("spear")) {
			c.setSidebarInterface(0, 4679); // lunge, swipe, pound, block
			c.getPA().sendFrame246(4680, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 4682);
		} else if (WeaponName2.toLowerCase().contains("mace")) {
			c.setSidebarInterface(0, 3796);
			c.getPA().sendFrame246(3797, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 3799);

		} else if (c.playerEquipment[c.playerWeapon] == 4153) {
			c.setSidebarInterface(0, 425); // war hammer equip.
			c.getPA().sendFrame246(426, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 428);
		} else {
			c.setSidebarInterface(0, 2423); // chop, slash, lunge, block
			c.getPA().sendFrame246(2424, 200, Weapon);
			c.getPA().sendFrame126(WeaponName, 2426);
		}

	}

	/**
	 * Removes a wielded item.
	 **/
	@SuppressWarnings("static-access")
	public void removeItem(int wearID, int slot) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			if (c.playerEquipment[slot] > -1) {
				if (addItem(c.playerEquipment[slot], c.playerEquipmentN[slot])) {
					c.playerEquipment[slot] = -1;
					c.playerEquipmentN[slot] = 0;
					sendWeapon(c.playerEquipment[c.playerWeapon],
							getItemName(c.playerEquipment[c.playerWeapon]));
					resetBonus();
					getBonus();
					writeBonus();
					c.getCombat().getPlayerAnimIndex(c.getEquipment().getItemName(
									c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.getOutStream().createFrame(34);
					c.getOutStream().writeWord(6);
					c.getOutStream().writeWord(1688);
					c.getOutStream().writeByte(slot);
					c.getOutStream().writeWord(0);
					c.getOutStream().writeByte(0);
					c.flushOutStream();
					c.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
					c.isFullHelm = Item
							.isFullHelm(c.playerEquipment[c.playerHat]);
					c.isFullMask = Item
							.isFullMask(c.playerEquipment[c.playerHat]);
					c.isFullBody = Item
							.isFullBody(c.playerEquipment[c.playerChest]);
				}
			}
		}
	}

	public boolean playerHasItem(int itemID, int amt, int slot) {
		itemID++;
		int found = 0;
		if (c.playerItems[slot] == (itemID)) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] == itemID) {
					if (c.playerItemsN[i] >= amt) {
						return true;
					} else {
						found++;
					}
				}
			}
			if (found >= amt) {
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean playerHasItem(int itemID) {
		itemID++;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID)
				return true;
		}
		return false;
	}

	public boolean playerHasItem(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] == itemID) {
				if (c.playerItemsN[i] >= amt) {
					return true;
				} else {
					found++;
				}
			}
		}
		if (found >= amt) {
			return true;
		}
		return false;
	}

	public boolean addItem(int item, int amount) {
		// synchronized(c) {
		if (amount < 1) {
			amount = 1;
		}
		if (item <= 0) {
			return false;
		}
		if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item])
				|| ((freeSlots() > 0) && !Item.itemStackable[item])) {
			for (int i = 0; i < c.playerItems.length; i++) {
				if ((c.playerItems[i] == (item + 1))
						&& Item.itemStackable[item] && (c.playerItems[i] > 0)) {
					c.playerItems[i] = (item + 1);
					if (((c.playerItemsN[i] + amount) < GameConstants.MAXITEM_AMOUNT)
							&& ((c.playerItemsN[i] + amount) > -1)) {
						c.playerItemsN[i] += amount;
					} else {
						c.playerItemsN[i] = GameConstants.MAXITEM_AMOUNT;
					}
					if (c.getOutStream() != null && c != null) {
						c.getOutStream().createFrameVarSizeWord(34);
						c.getOutStream().writeWord(3214);
						c.getOutStream().writeByte(i);
						c.getOutStream().writeWord(c.playerItems[i]);
						if (c.playerItemsN[i] > 254) {
							c.getOutStream().writeByte(255);
							c.getOutStream().writeDWord(c.playerItemsN[i]);
						} else {
							c.getOutStream().writeByte(c.playerItemsN[i]);
						}
						c.getOutStream().endFrameVarSizeWord();
						c.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < c.playerItems.length; i++) {
				if (c.playerItems[i] <= 0) {
					c.playerItems[i] = item + 1;
					if ((amount < GameConstants.MAXITEM_AMOUNT) && (amount > -1)) {
						c.playerItemsN[i] = 1;
						if (amount > 1) {
							c.getInventory().addItem(item, amount - 1);
							return true;
						}
					} else {
						c.playerItemsN[i] = GameConstants.MAXITEM_AMOUNT;
					}
					resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			resetItems(3214);
			c.sendMessage("Not enough space in your inventory.");
			return false;
		}
		// }
	}

	/**
	 * Empties all of (a) player's items.
	 */
	public void resetItems(int WriteFrame) {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrameVarSizeWord(53);
				c.getOutStream().writeWord(WriteFrame);
				c.getOutStream().writeWord(c.playerItems.length);
				for (int i = 0; i < c.playerItems.length; i++) {
					if (c.playerItemsN[i] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord_v2(c.playerItemsN[i]);
					} else {
						c.getOutStream().writeByte(c.playerItemsN[i]);
					}
					c.getOutStream().writeWordBigEndianA(c.playerItems[i]);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
		}
	}

	/**
	 * Adds special attack bar to special attack weapons. Removes special attack
	 * bar to weapons that do not have special attacks.
	 **/
	public void addSpecialBar(int weapon) {
		switch (weapon) {

		case 4151: // whip
			c.getPA().sendFrame171(0, 12323);
			specialAmount(weapon, c.specAmount, 12335);
			break;

		case 859: // magic bows
		case 861:
		case 11235:
			c.getPA().sendFrame171(0, 7549);
			specialAmount(weapon, c.specAmount, 7561);
			break;

		case 4587: // dscimmy
			c.getPA().sendFrame171(0, 7599);
			specialAmount(weapon, c.specAmount, 7611);
			break;

		case 3204: // d hally
			c.getPA().sendFrame171(0, 8493);
			specialAmount(weapon, c.specAmount, 8505);
			break;

		case 1377: // d battleaxe
			c.getPA().sendFrame171(0, 7499);
			specialAmount(weapon, c.specAmount, 7511);
			break;

		case 4153: // gmaul
			c.getPA().sendFrame171(0, 7474);
			specialAmount(weapon, c.specAmount, 7486);
			break;

		case 1249: // dspear
			c.getPA().sendFrame171(0, 7674);
			specialAmount(weapon, c.specAmount, 7686);
			break;

		case 1215:// dragon dagger
		case 1231:
		case 5680:
		case 5698:
		case 1305: // dragon long
		case 11694:
		case 11698:
		case 11700:
		case 11838:
		case 11696:
		case 10887:
			c.getPA().sendFrame171(0, 7574);
			specialAmount(weapon, c.specAmount, 7586);
			break;

		case 1434: // dragon mace
			c.getPA().sendFrame171(0, 7624);
			specialAmount(weapon, c.specAmount, 7636);
			break;

		default:
			c.getPA().sendFrame171(1, 7624); // mace interface
			c.getPA().sendFrame171(1, 7474); // hammer, gmaul
			c.getPA().sendFrame171(1, 7499); // axe
			c.getPA().sendFrame171(1, 7549); // bow interface
			c.getPA().sendFrame171(1, 7574); // sword interface
			c.getPA().sendFrame171(1, 7599); // scimmy sword interface, for most
												// swords
			c.getPA().sendFrame171(1, 8493);
			c.getPA().sendFrame171(1, 12323); // whip interface
			break;
		}
	}

	/**
	 * Special attack bar filling amount.
	 **/
	public void specialAmount(int weapon, double specAmount, int barId) {
		c.specBarId = barId;
		c.getPA().sendFrame70(specAmount >= 10 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 9 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 8 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 7 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 6 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 5 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 4 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 3 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 2 ? 500 : 0, 0, (--barId));
		c.getPA().sendFrame70(specAmount >= 1 ? 500 : 0, 0, (--barId));
		updateSpecialBar();
		sendWeapon(weapon, getItemName(weapon));
	}

	/**
	 * Special attack text.
	 **/
	public void updateSpecialBar() {
		String percent = Double.toString(c.specAmount);
		if (percent.contains(".")) {
			percent = percent.replace(".", "");
		}
		if (percent.startsWith("0") && !percent.equals("00")) {
			percent = percent.replace("0", "");
		}
		if (percent.startsWith("0") && percent.equals("00")) {
			percent = percent.replace("00", "0");
		}
		c.getPA()
				.sendFrame126(
						c.usingSpecial ? "@yel@Special Attack (" + percent
								+ "%)" : "@bla@Special Attack (" + percent
								+ "%)", c.specBarId);
	}
	
	/**
	 * Updates the equipment tab.
	 **/
	public void setEquipment(int wearID, int amount, int targetSlot) {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(34);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(targetSlot);
			c.getOutStream().writeWord(wearID + 1);
			if (amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord(amount);
			} else {
				c.getOutStream().writeByte(amount);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			c.playerEquipment[targetSlot] = wearID;
			c.playerEquipmentN[targetSlot] = amount;
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}
	
	/**
	 * Delete item equipment.
	 **/
	public void deleteEquipment(int i, int j) {
		synchronized (c) {
			if (PlayerHandler.players[c.playerId] == null) {
				return;
			}
			if (i < 0) {
				return;
			}

			c.playerEquipment[j] = -1;
			c.playerEquipmentN[j] = c.playerEquipmentN[j] - 1;
			c.getOutStream().createFrame(34);
			c.getOutStream().writeWord(6);
			c.getOutStream().writeWord(1688);
			c.getOutStream().writeByte(j);
			c.getOutStream().writeWord(0);
			c.getOutStream().writeByte(0);
			c.getEquipment().getBonus();
			if (j == c.playerWeapon) {
				sendWeapon(-1, "Unarmed");
			}
			resetBonus();
			c.getEquipment().getBonus();
			c.getEquipment().writeBonus();
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}
	
	public void deleteEquipment() {
		synchronized (c) {
			if (c.playerEquipmentN[c.playerWeapon] == 1) {
				c.getEquipment().deleteEquipment(c.playerEquipment[c.playerWeapon],
						c.playerWeapon);
			}
			if (c.playerEquipmentN[c.playerWeapon] != 0) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(1688);
				c.getOutStream().writeByte(c.playerWeapon);
				c.getOutStream().writeWord(
						c.playerEquipment[c.playerWeapon] + 1);
				if (c.playerEquipmentN[c.playerWeapon] - 1 > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(
							c.playerEquipmentN[c.playerWeapon] - 1);
				} else {
					c.getOutStream().writeByte(
							c.playerEquipmentN[c.playerWeapon] - 1);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
				c.playerEquipmentN[c.playerWeapon] -= 1;
			}
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
		}
	}
	
	/**
	 * Removes all items from player's equipment.
	 */
	public void removeAllItems() {
		for (int i = 0; i < c.playerItems.length; i++) {
			c.playerItems[i] = 0;
		}
		for (int i = 0; i < c.playerItemsN.length; i++) {
			c.playerItemsN[i] = 0;
		}
		resetItems(3214);
	}
}
