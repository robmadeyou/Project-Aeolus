package core.game.model.entity.player;

import core.Configuration;
import core.game.GameConstants;
import core.game.content.Spellbook;
import core.game.model.entity.player.save.PlayerSave;
import core.game.util.Misc;

/**
 * A class which contains methods not related to PlayerIO, instead helps a
 * player do something
 * 
 * @author 7Winds
 */
public class PlayerAssistant {

	private Player p;

	public PlayerAssistant(Player p) {
		this.p = p;
	}

	/**
	 * Deals poisonous damage towards a Player
	 */
	public void appendPoison(int damage) {
		if (System.currentTimeMillis() - p.lastPoisonSip > p.poisonImmune) {
			p.sendMessage("You have been poisoned.");
			p.poisonDamage = damage;
		}
	}

	/**
	 * Drink AntiPosion Potions
	 * 
	 * @param itemId
	 *            The itemId
	 * @param itemSlot
	 *            The itemSlot
	 * @param newItemId
	 *            The new item After Drinking
	 * @param healType
	 *            The type of poison it heals
	 */
	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId, int healType) {
		p.attackTimer = p.getCombat().getAttackDelay(p.getItems().getItemId(p.playerEquipment[p.playerWeapon]));
		if (p.duelRule[5]) {
			p.sendMessage("Potions has been disabled in this duel!");
			return;
		}
		if (!p.isDead && System.currentTimeMillis() - p.foodDelay > 2000) {
			if (p.getInventory().playerHasItem(itemId, 1, itemSlot)) {
				p.sendMessage("You drink the " + p.getEquipment().getItemName(itemId).toLowerCase() + ".");
				p.foodDelay = System.currentTimeMillis();
				// Actions
				if (healType == 1) {
					// Cures The Poison
				} else if (healType == 2) {
					// Cures The Poison + protects from getting poison again
				}
				p.startAnimation(0x33D);
				p.getInventory().deleteItem(itemId, itemSlot, 1);
				p.getInventory().addItem(newItemId, 1);
				p.getActionSender().requestUpdates();
			}
		}
	}

	/**
	 * Changes a players spellbook
	 */
	public void setSpellbook(Spellbook book) {
		p.playerMagicBook = book.getIndex();
		p.getActionSender().setSidebarInterface(6, book.getInterfaceIndex());
		p.getActionSender().removeAllWindows();
		p.sendMessage("You read the lectern and " + book.toString().toLowerCase() + " magicks fills your mind.");
		p.autocasting = false;
	}

	public int antiFire() {
		int toReturn = 0;
		if (p.antiFirePot)
			toReturn++;
		if (p.playerEquipment[p.playerShield] == 1540 || p.prayerActive[12]
				|| p.playerEquipment[p.playerShield] == 11284)
			toReturn++;
		return toReturn;
	}

	/**
	 * Magic on items
	 **/
	public void magicOnItems(int slot, int itemId, int spellId) {

		switch (spellId) {
		case 1162: // low alch
			if (System.currentTimeMillis() - p.alchDelay > 1000) {
				if (!p.getCombat().checkMagicReqs(49)) {
					break;
				}
				if (itemId == 995) {
					p.sendMessage("You can't alch coins");
					break;
				}
				p.getInventory().deleteItem(itemId, slot, 1);
				p.getInventory().addItem(995, p.getShops().getItemShopValue(itemId) / 3);
				p.startAnimation(p.MAGIC_SPELLS[49][2]);
				p.gfx100(p.MAGIC_SPELLS[49][3]);
				p.alchDelay = System.currentTimeMillis();
				p.getActionSender().forceOpenTab(6);
				p.getActionSender().addSkillXP(p.MAGIC_SPELLS[49][7] * GameConstants.MAGIC_EXP_RATE, 6);
				p.getActionSender().refreshSkill(6);
			}
			break;

		case 1178: // high alch
			if (System.currentTimeMillis() - p.alchDelay > 2000) {
				if (!p.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (itemId == 995) {
					p.sendMessage("You can't alch coins");
					break;
				}
				p.getInventory().deleteItem(itemId, slot, 1);
				p.getInventory().addItem(995, (int) (p.getShops().getItemShopValue(itemId) * .75));
				p.startAnimation(p.MAGIC_SPELLS[50][2]);
				p.gfx100(p.MAGIC_SPELLS[50][3]);
				p.alchDelay = System.currentTimeMillis();
				p.getActionSender().forceOpenTab(6);
				p.getActionSender().addSkillXP(p.MAGIC_SPELLS[50][7] * GameConstants.MAGIC_EXP_RATE, 6);
				p.getActionSender().refreshSkill(6);
			}
			break;
		}
	}

	/**
	 * Finds the player that killed a player
	 */
	public int findKiller() {
		int killer = p.playerId;
		int damage = 0;
		for (int j = 0; j < GameConstants.MAX_PLAYERS; j++) {
			if (PlayerHandler.players[j] == null)
				continue;
			if (j == p.playerId)
				continue;
			if (p.goodDistance(p.absX, p.absY, PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, 40)
					|| p.goodDistance(p.absX, p.absY + 9400, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY, 40)
					|| p.goodDistance(p.absX, p.absY, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY + 9400, 40))
				if (p.damageTaken[j] > damage) {
					damage = p.damageTaken[j];
					killer = j;
				}
		}
		return killer;
	}

	public void giveLife() {
		p.isDead = false;
		p.faceUpdate(-1);
		p.freezeTimer = 0;
		if (p.duelStatus <= 4) { // if we are not in a duel we must be in wildy
									// so remove items
			p.getItems().resetKeepItems();
			if ((p.getRights().greaterOrEqual(Rights.ADMINISTRATOR) && Configuration.ADMIN_DROP_ITEMS)) {
				return;
			}
				if (!(Boolean) p.isSkulled) { // what
																		// items
																		// to
																		// keep
					p.getItems().keepItem(0, true);
					p.getItems().keepItem(1, true);
					p.getItems().keepItem(2, true);
				}
				if (p.prayerActive[10] && System.currentTimeMillis() - p.lastProtItem > 700) {
					p.getItems().keepItem(3, true);
				}
				p.getItems().dropAllItems(); // drop all items
				p.getItems().deleteAllItems(); // delete all items

				if (!p.isSkulled) { // add the
																		// kept
																		// items
																		// once
																		// we
																		// finish
																		// deleting
																		// and
																		// dropping
																		// them
					for (int i1 = 0; i1 < 3; i1++) {
						if (p.itemKeptId[i1] > 0) {
							p.getInventory().addItem(p.itemKeptId[i1], 1);
						}
					}
				}
				if (p.prayerActive[10]) { // if we have protect items
					if (p.itemKeptId[3] > 0) {
						p.getInventory().addItem(p.itemKeptId[3], 1);
					}
				}
			p.getItems().resetKeepItems();
		}
		p.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			p.playerLevel[i] = p.getActionSender().getLevelForXP(p.playerXP[i]);
			p.getActionSender().refreshSkill(i);
		}
		if (p.duelStatus <= 4) { // if we are not in a duel repawn to wildy
			p.getMovement().movePlayer(GameConstants.RESPAWN_X, GameConstants.RESPAWN_Y, 0);
			p.isSkulled = true;
			p.skullTimer = 0;
			p.attackedPlayers.clear();
		} else { // we are in a duel, respawn outside of arena
			Player o = (Player) PlayerHandler.players[p.duelingWith];
			if (o != null) {
				o.getActionSender().createPlayerHints(10, -1);
				if (o.duelStatus == 6) {
					o.getDuel().duelVictory();
				}
			}
			p.getMovement().movePlayer(
					GameConstants.DUELING_RESPAWN_X + (Misc.random(Configuration.RANDOM_DUELING_RESPAWN)),
					GameConstants.DUELING_RESPAWN_Y + (Misc.random(Configuration.RANDOM_DUELING_RESPAWN)), 0);
			o.getMovement().movePlayer(
					GameConstants.DUELING_RESPAWN_X + (Misc.random(Configuration.RANDOM_DUELING_RESPAWN)),
					GameConstants.DUELING_RESPAWN_Y + (Misc.random(Configuration.RANDOM_DUELING_RESPAWN)), 0);
			if (p.duelStatus != 6) { // if we have won but have died, don't
										// reset the duel status.
				p.getDuel().resetDuel();
			}
		}
		// PlayerSaving.getSingleton().requestSave(p.playerId);
		PlayerSave.saveGame(p);
		p.getCombat().resetPlayerAttack();
		p.getActionSender().resetAnimation();
		p.startAnimation(65535);
		p.getActionSender().frame1();
		resetTb();
		p.isSkulled = false;
		p.attackedPlayers.clear();
		p.headIconPk = -1;
		p.skullTimer = -1;
		p.damageTaken = new int[GameConstants.MAX_PLAYERS];
		p.getActionSender().requestUpdates();
	}

	/**
	 * Dieing
	 **/
	public void applyDead() {
		p.respawnTimer = 15;
		p.isDead = false;

		if (p.duelStatus != 6) {
			// p.killerId = p.getCombat().getKillerId(p.playerId);
			p.killerId = findKiller();
			Player o = (Player) PlayerHandler.players[p.killerId];
			if (o != null) {
				if (p.killerId != p.playerId)
					o.sendMessage("You have defeated " + p.playerName + "!");
				p.playerKilled = p.playerId;
				if (o.duelStatus == 5) {
					o.duelStatus++;
				}
			}
		}
		p.faceUpdate(0);
		p.npcIndex = 0;
		p.playerIndex = 0;
		p.stopMovement();
		if (p.duelStatus <= 4) {
			p.sendMessage("Oh dear you are dead!");
		} else if (p.duelStatus != 6) {
			p.sendMessage("You have lost the duel!");
		}
		resetDamageDone();
		p.specAmount = 10;
		p.getEquipment().addSpecialBar(p.playerEquipment[p.playerWeapon]);
		p.lastVeng = 0;
		p.vengOn = false;
		resetFollowers();
		p.attackTimer = 10;
	}

	/**
	 * Location change for digging, levers etc
	 **/

	public void changeLocation() {
		switch (p.newLocation) {
		case 1:
			p.getActionSender().disableMinimap(2);
			p.getMovement().movePlayer(3578, 9706, -1);
			break;
		case 2:
			p.getActionSender().disableMinimap(2);
			p.getMovement().movePlayer(3568, 9683, -1);
			break;
		case 3:
			p.getActionSender().disableMinimap(2);
			p.getMovement().movePlayer(3557, 9703, -1);
			break;
		case 4:
			p.getActionSender().disableMinimap(2);
			p.getMovement().movePlayer(3556, 9718, -1);
			break;
		case 5:
			p.getActionSender().disableMinimap(2);
			p.getMovement().movePlayer(3534, 9704, -1);
			break;
		case 6:
			p.getActionSender().disableMinimap(2);
			p.getMovement().movePlayer(3546, 9684, -1);
			break;
		}
		p.newLocation = 0;
	}

	/**
	 * Teleporting
	 **/
	public void spellTeleport(int x, int y, int height) {
		startTeleport(x, y, height, p.playerMagicBook == 1 ? "ancient" : "modern");
	}

	public void startTeleport(int x, int y, int height, String teleportType) {
		if (p.duelStatus == 5) {
			p.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (p.inWild() && p.wildLevel > Configuration.NO_TELEPORT_WILD_LEVEL) {
			p.sendMessage(
					"You can't teleport above level " + Configuration.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return;
		}
		if (System.currentTimeMillis() - p.teleBlockDelay < p.teleBlockLength) {
			p.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!p.isDead && p.teleTimer == 0 && p.respawnTimer == -6) {
			if (p.playerIndex > 0 || p.npcIndex > 0)
				p.getCombat().resetPlayerAttack();
			p.stopMovement();
			p.getActionSender().removeAllWindows();
			p.teleX = x;
			p.teleY = y;
			p.npcIndex = 0;
			p.playerIndex = 0;
			p.faceUpdate(0);
			p.teleHeight = height;
			if (teleportType.equalsIgnoreCase("modern")) {
				p.startAnimation(714);
				p.teleTimer = 11;
				p.teleGfx = 308;
				p.teleEndAnimation = 715;
			}
			if (teleportType.equalsIgnoreCase("ancient")) {
				p.startAnimation(1979);
				p.teleGfx = 0;
				p.teleTimer = 9;
				p.teleEndAnimation = 0;
				p.gfx0(392);
			}

		}
	}

	public void startTeleport2(int x, int y, int height) {
		if (p.duelStatus == 5) {
			p.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (System.currentTimeMillis() - p.teleBlockDelay < p.teleBlockLength) {
			p.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!p.isDead && p.teleTimer == 0) {
			p.stopMovement();
			p.getActionSender().removeAllWindows();
			p.teleX = x;
			p.teleY = y;
			p.npcIndex = 0;
			p.playerIndex = 0;
			p.faceUpdate(0);
			p.teleHeight = height;
			p.startAnimation(714);
			p.teleTimer = 11;
			p.teleGfx = 308;
			p.teleEndAnimation = 715;
		}
	}

	public void processTeleport() {
		p.teleportToX = p.teleX;
		p.teleportToY = p.teleY;
		p.heightLevel = p.teleHeight;
		if (p.teleEndAnimation > 0) {
			p.startAnimation(p.teleEndAnimation);
		}
	}
	
	/**
	 * A player casting vengeance
	 */
	public void castVeng() {
		if (p.playerLevel[6] < 94) {
			p.sendMessage("You need a magic level of 94 to cast this spell.");
			return;
		}
		if (p.playerLevel[1] < 40) {
			p.sendMessage("You need a defence level of 40 to cast this spell.");
			return;
		}
		if (!p.getInventory().playerHasItem(9075, 4)
				|| !p.getInventory().playerHasItem(557, 10)
				|| !p.getInventory().playerHasItem(560, 2)) {
			p.sendMessage("You don't have the required runes to cast this spell.");
			return;
		}
		if (System.currentTimeMillis() - p.lastVeng < 30000) {
			p.sendMessage("You can only cast vengeance every 30 seconds.");
			return;
		}
		if (p.vengOn) {
			p.sendMessage("You already have vengeance casted.");
			return;
		}
		if (p.duelRule[4]) {
			p.sendMessage("You can't cast this spell because magic has been disabled.");
			return;
		}
		p.startAnimation(4410);
		p.gfx100(726);
		p.getInventory().deleteItem(9075, 4);
		p.getInventory().deleteItem(557, 10);
		p.getInventory().deleteItem(560, 2);
		p.getActionSender().addSkillXP(10000, 6);
		p.getActionSender().refreshSkill(6);
		p.vengOn = true;
		p.lastVeng = System.currentTimeMillis();
	}

	/**
	 * Casts vengeance on self
	 */
	public void vengMe() {
		if (System.currentTimeMillis() - p.lastVeng > 30000) {
			if (p.getInventory().playerHasItem(557, 10) && p.getInventory().playerHasItem(9075, 4)
					&& p.getInventory().playerHasItem(560, 2)) {
				p.vengOn = true;
				p.lastVeng = System.currentTimeMillis();
				p.startAnimation(4410);
				p.gfx100(726);
				p.getInventory().deleteItem(557, p.getEquipment().getItemSlot(557), 10);
				p.getInventory().deleteItem(560, p.getEquipment().getItemSlot(560), 2);
				p.getInventory().deleteItem(9075, p.getEquipment().getItemSlot(9075), 4);
			} else {
				p.sendMessage("You do not have the required runes to cast this spell. (9075 for astrals)");
			}
		} else {
			p.sendMessage("You must wait 30 seconds before casting this again.");
		}
	}

	public void resetDamageDone() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].damageTaken[p.playerId] = 0;
			}
		}
	}

	/**
	 * Resets the player following
	 */
	public void resetFollowers() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].followPlayerId == p.playerId) {
					p.getActionSender().resetFollow();
				}
			}
		}
	}

	/**
	 * Resets the teleblock timer to 0.
	 */
	public void resetTb() {
		p.teleBlockLength = 0;
		p.teleBlockDelay = 0;
	}

}
