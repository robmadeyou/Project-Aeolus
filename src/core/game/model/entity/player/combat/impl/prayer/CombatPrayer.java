package core.game.model.entity.player.combat.impl.prayer;

import core.Configuration;
import core.game.model.entity.player.Player;

/**
 * Handles prayers drain and switching
 * 
 * @author 2012
 * @author Organic
 * @author 7Winds (Modified)
 */

public class CombatPrayer {

	/**
	 * Prayers in a array. This would be nicer to use an enum.
	 * Arrays can get ugly to work with.
	 */
	public static double[] prayerData = { 1, // Thick Skin.
			1, // Burst of Strength.
			1, // Clarity of Thought.
			1, // Sharp Eye.
			1, // Mystic Will.
			2, // Rock Skin.
			2, // SuperHuman Strength.
			2, // Improved Reflexes.
			0.4, // Rapid restore.
			0.6, // Rapid Heal.
			0.6, // Protect Items.
			1.5, // Hawk eye.
			2, // Mystic Lore.
			4, // Steel Skin.
			4, // Ultimate Strength.
			4, // Incredible Reflexes.
			4, // Protect from Magic.
			4, // Protect from Missiles.
			4, // Protect from Melee.
			4, // Eagle Eye.
			4, // Mystic Might.
			1, // Retribution.
			2, // Redemption.
			6, // Smite.
			8, // Chivalry.
			8, // Piety.
	};

	/**
	 * Drains prayer points
	 */
	public static void handlePrayerDrain(Player c) {
		c.usingPrayer = false;
		double toRemove = 0.0;
		for (int j = 0; j < prayerData.length; j++) {
			if (c.prayerActive[j]) {
				toRemove += prayerData[j] / 20;
				c.usingPrayer = true;
			}
		}
		if (toRemove > 0) {
			toRemove /= (1 + (0.035 * c.playerBonus[11]));
		}
		c.prayerPoint -= toRemove;
		if (c.prayerPoint <= 0) {
			c.prayerPoint = 1.0 + c.prayerPoint;
			reducePrayerLevel(c);
		}
	}
	
	/**
	 * Reduces a players prayer level (Smite)
	 */
	public static void reducePrayerLevel(Player c) {
		if (c.playerLevel[5] - 1 > 0) {
			c.playerLevel[5] -= 1;
		} else {
			c.sendMessage("You have run out of prayer points!");
			c.playerLevel[5] = 0;
			resetPrayers(c);
			c.prayerId = -1;
		}
		c.getActionSender().refreshSkill(5);
	}

	/**
	 * Resets all player prayers
	 */
	public static void resetPrayers(Player c) {
		for (int i = 0; i < c.prayerActive.length; i++) {
			c.prayerActive[i] = false;
			c.getActionSender().setConfig(c.PRAYER_GLOW[i], 0);
		}
		c.headIcon = -1;
		c.getActionSender().requestUpdates();
	}
	
	/**
	 * Prayer constants held within the array.
	 */
	private static final int THICK_SKIN = 0;	
	private static final int BURST_OF_STRENGTH = 1;	
	private static final int CLARITY_OF_THOUGHT = 2;
	private static final int SHARP_EYE = 3;	
	private static final int MYSTIC_WILL = 4;	
	private static final int ROCK_SKIN = 5;	
	private static final int SUPERHUMAN_STRENGTH = 6;	
	private static final int IMPROVED_REFLEXES = 7;	
	private static final int RAPID_RESTORE = 8;	
	private static final int RAPID_HEAL = 9;	
	private static final int PROTECT_ITEM = 10;	
	private static final int HAWK_EYE = 11;	
	private static final int MYSTIC_LORE = 12;	
	private static final int STEEL_SKIN = 13;	
	private static final int ULTIMATE_STRENGTH = 14;
	private static final int INCREDIBLE_REFLEXES = 15;
	private static final int PROTECT_FROM_MAGIC = 16;
	private static final int PROTECT_FROM_MISSLES = 17;
	private static final int PROTECT_FROM_MELEE = 18;
	private static final int EAGLE_EYE = 19;
	private static final int MYSTIC_MIGHT = 20;
	private static final int RETRIBUTION = 21;
	private static final int REDEMPTION = 22;
	private static final int SMITE = 23;
	private static final int CHIVALRY = 24;
	private static final int PIETY = 25;

	/**
	 * Handles the activation of a players prayer.
	 */
	public static void activatePrayer(Player c, int i) {
		if (c.isDead || c.playerLevel[3] <= 0) {
			return;
		}
		if (c.duelRule[7]) {
			for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows
				c.prayerActive[p] = false;
				c.getActionSender().setConfig(c.PRAYER_GLOW[p], 0);
			}
			c.sendMessage("Prayer has been disabled in this duel!");
			return;
		}
		int[] defPray = { 0, 5, 13, 24, 25 };
		int[] strPray = { 1, 6, 14, 24, 25 };
		int[] atkPray = { 2, 7, 15, 24, 25 };
		int[] rangePray = { 3, 11, 19 };
		int[] magePray = { 4, 12, 20 };

		if (c.playerLevel[5] > 0 || !Configuration.PRAYER_POINTS_REQUIRED) {
			if (c.getActionSender().getLevelForXP(c.playerXP[5]) >= c.PRAYER_LEVEL_REQUIRED[i]
					|| !Configuration.PRAYER_LEVEL_REQUIRED) {
				boolean headIcon = false;
				switch (i) {

				case THICK_SKIN:
					if (i == THICK_SKIN) {
						c.getActionSender().sendSound(446);
					}
				case ROCK_SKIN:
					if (i == ROCK_SKIN) {
						c.getActionSender().sendSound(449);
					}
				case STEEL_SKIN:
					if (i == STEEL_SKIN) {
						c.getActionSender().sendSound(439);
					}
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < defPray.length; j++) {
							if (defPray[j] != i) {
								c.prayerActive[defPray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;
				case BURST_OF_STRENGTH:
					if (i == BURST_OF_STRENGTH) {
						c.getActionSender().sendSound(450);
					}
				case SUPERHUMAN_STRENGTH:
					if (i == SUPERHUMAN_STRENGTH) {
						c.getActionSender().sendSound(434);
					}
				case ULTIMATE_STRENGTH:
					c.getActionSender().sendSound(441);
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;

				case CLARITY_OF_THOUGHT:
					if (i == CLARITY_OF_THOUGHT) {
						c.getActionSender().sendSound(448);
					}
				case IMPROVED_REFLEXES:
					if (i == IMPROVED_REFLEXES) {
						c.getActionSender().sendSound(436);
					}
				case INCREDIBLE_REFLEXES:
					if (i == INCREDIBLE_REFLEXES) {
						c.getActionSender().sendSound(440);
					}
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
					
				case RAPID_RESTORE:
				case RAPID_HEAL:
					
					break;

				case SHARP_EYE:
				case HAWK_EYE:
				case EAGLE_EYE:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							c.getActionSender().sendSound(448);
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case MYSTIC_WILL:
				case MYSTIC_LORE:
				case MYSTIC_MIGHT:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case PROTECT_ITEM:
					c.lastProtItem = System.currentTimeMillis();
					c.protectItem = !c.protectItem;
					c.getActionSender().sendSound(433);
					break;

				case PROTECT_FROM_MAGIC:
					if (i == PROTECT_FROM_MAGIC) {
						c.getActionSender().sendSound(438);
					}
				case PROTECT_FROM_MISSLES:
					if (i == PROTECT_FROM_MISSLES) {
						c.getActionSender().sendSound(444);
					}
				case PROTECT_FROM_MELEE:
					if (System.currentTimeMillis() - c.stopPrayerDelay < 5000) {
						c.sendMessage("You have been injured and can't use this prayer!");
						c.getActionSender().setConfig(c.PRAYER_GLOW[16], 0);
						c.getActionSender().setConfig(c.PRAYER_GLOW[17], 0);
						c.getActionSender().setConfig(c.PRAYER_GLOW[18], 0);
						c.getActionSender().sendSound(433);
						return;
					}
					if (i == PROTECT_FROM_MAGIC)
						c.protMageDelay = System.currentTimeMillis();
					else if (i == PROTECT_FROM_MISSLES)
						c.protRangeDelay = System.currentTimeMillis();
					else if (i == PROTECT_FROM_MELEE)
						c.protMeleeDelay = System.currentTimeMillis();
				case RETRIBUTION:
				case REDEMPTION:
				case SMITE:
					headIcon = true;
					for (int p = 16; p < 24; p++) {
						if (i != p && p != 19 && p != 20) {
							c.prayerActive[p] = false;
							c.getActionSender().setConfig(c.PRAYER_GLOW[p], 0);
						}
					}
					break;
				case CHIVALRY:
				case PIETY:
					if (c.prayerActive[i] == false) {
						c.getActionSender().sendSound(448);
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
						for (int j = 0; j < defPray.length; j++) {
							if (defPray[j] != i) {
								c.prayerActive[defPray[j]] = false;
								c.getActionSender().setConfig(
										c.PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;
				}

				if (!headIcon) {
					if (c.prayerActive[i] == false) {
						c.prayerActive[i] = true;
						c.getActionSender().setConfig(c.PRAYER_GLOW[i], 1);
					} else {
						c.prayerActive[i] = false;
						c.getActionSender().setConfig(c.PRAYER_GLOW[i], 0);
					}
				} else {
					if (c.prayerActive[i] == false) {
						c.prayerActive[i] = true;
						c.getActionSender().setConfig(c.PRAYER_GLOW[i], 1);
						c.headIcon = c.PRAYER_HEAD_ICONS[i];
						c.getActionSender().requestUpdates();
					} else {
						c.prayerActive[i] = false;
						c.getActionSender().setConfig(c.PRAYER_GLOW[i], 0);
						c.headIcon = -1;
						c.getActionSender().requestUpdates();
					}
				}
			} else {
				c.getActionSender().setConfig(c.PRAYER_GLOW[i], 0);
				c.getActionSender().sendSound(447);
				c.getActionSender().textOnInterface(
						"You need a @blu@Prayer level of "
								+ c.PRAYER_LEVEL_REQUIRED[i] + " to use "
								+ c.PRAYER_NAME[i] + ".", 357);
				c.getActionSender().textOnInterface("Click here to continue", 358);
				c.getActionSender().openBackDialogue(356);
			}
		} else {
			c.getActionSender().setConfig(c.PRAYER_GLOW[i], 0);
			c.sendMessage("You have run out of prayer points!");
		}

	}
}
