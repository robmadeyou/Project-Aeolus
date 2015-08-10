package core.game.model.entity.player.combat.impl.magic;

import core.game.GameConstants;
import core.game.model.entity.player.Player;

public class NonCombatSpells extends MagicRequirements {

	public static void attemptDate(Player c, int action) {
		switch (action) {
		case 4135:
			bonesToBase(c, 15, new int[] { EARTH, WATER, NATURE }, new int[] {
					2, 2, 1 }, new int[] { 526, 1963 });
			break;
		case 62005:
			bonesToBase(c, 60, new int[] { NATURE, WATER, EARTH }, new int[] {
					2, 4, 4 }, new int[] { 526, 6883 });
			break;
		}
	}

	private static void bonesToBase(Player c, int levelReq, int[] runes,
			int[] amount, int[] item) {
		if (!hasRequiredLevel(c, levelReq)) {
			c.sendMessage("You need to have a magic level of levelReq to cast this spell.");
			return;
		}
		if (!hasRunes(c, new int[] { runes[0], runes[1], runes[2] }, new int[] {
				amount[0], amount[1], amount[2] })) {
			return;
		}
		if ((!c.getInventory().playerHasItem(item[0], 1))) {
			c.sendMessage("You need some " + c.getEquipment().getItemName(item[0])
					+ " to cast this spell!");
			return;
		}
		c.getInventory().replaceItem(c, item[0], item[1]);
		c.gfx100(141);
		c.startAnimation(722);
		c.getActionSender().addSkillXP(100 * c.getInventory().getItemAmount(item[0]), 6);
		c.sendMessage("You use your magic power to convert bones into "
				+ c.getEquipment().getItemName(item[1]).toLowerCase().toLowerCase()
				+ "" + (item[1] != 1963 ? ("e") : ("")) + "s!");
		c.getCombat().resetPlayerAttack();
	}

	public static void superHeatItem(Player c, int itemID) {
		if (!hasRequiredLevel(c, 43)) {
			c.sendMessage("You need to have a magic level of 43 to cast this spell.");
			return;
		}
		if (!hasRunes(c, new int[] { FIRE, NATURE }, new int[] { 4, 2 })) {
			return;
		}
		int[][] data = { { 436, 1, 438, 1, 2349, 53 }, // TIN
				{ 438, 1, 436, 1, 2349, 53 }, // COPPER
				{ 440, 1, -1, -1, 2351, 53 }, // IRON ORE
				{ 442, 1, -1, -1, 2355, 53 }, // SILVER ORE
				{ 444, 1, -1, -1, 2357, 23 }, // GOLD BAR
				{ 447, 1, 453, 4, 2359, 30 }, // MITHRIL ORE
				{ 449, 1, 453, 6, 2361, 38 }, // ADDY ORE
				{ 451, 1, 453, 8, 2363, 50 }, // RUNE ORE
		};
		for (int i = 0; i < data.length; i++) {
			if (itemID == data[i][0]) {
				if (!c.getInventory().playerHasItem(data[i][2], data[i][3])) {
					c.sendMessage("You haven't got enough "
							+ c.getEquipment().getItemName(data[i][2])
									.toLowerCase() + " to cast this spell!");
					return;
				}
				c.getInventory().deleteItem(itemID,
						c.getEquipment().getItemSlot(itemID), 1);
				for (int lol = 0; lol < data[i][3]; lol++) {
					c.getInventory().deleteItem(data[i][2],
							c.getEquipment().getItemSlot(data[i][2]), 1);
				}
				c.getInventory().addItem(data[i][4], 1);
				c.getActionSender().addSkillXP(data[i][5], 6);
				c.startAnimation(725);
				c.gfx100(148);
				c.getActionSender().forceOpenTab(6);
				return;
			}
		}
		c.sendMessage("You can only cast superheat item on ores!");
	}

	public static void playerAlching(Player c, int spell, int itemId, int slot) {
		switch (spell) {
		case 1162: // low alch
			if (System.currentTimeMillis() - c.alchDelay > 1000) {
				if (c.getInventory().playerHasItem(itemId, slot, 1)) {
					if (!c.getCombat().checkMagicReqs(49)) {
						break;
					}
					if (itemId == 995) {
						c.sendMessage("You can't alch coins.");
						break;
					}
					int reward = c.getShops().getItemShopValue(itemId) / 3;
					int playerAmount = c.getInventory().getItemAmount(995);
					if (reward + playerAmount > 2147483647) {
						c.sendMessage("You have reached max cash you can't alch anymore.");
						break;
					}
					if (c.getInventory().playerHasItem(itemId, slot, 1)) {
						c.getInventory().deleteItem(itemId, slot, 1);
						c.getInventory().addItem(995,
								c.getShops().getItemShopValue(itemId) / 3);
						c.startAnimation(c.MAGIC_SPELLS[49][2]);
						c.gfx100(c.MAGIC_SPELLS[49][3]);
						c.alchDelay = System.currentTimeMillis();
						c.getActionSender().forceOpenTab(6);
						c.getActionSender().addSkillXP(
								c.MAGIC_SPELLS[49][7]
										* GameConstants.MAGIC_EXP_RATE, 6);
						c.getActionSender().refreshSkill(6);
					}
				}
			}
			break;

		case 1178: // high alch
			if (System.currentTimeMillis() - c.alchDelay > 2000) {
				if (c.getInventory().playerHasItem(itemId, slot, 1)) {
					if (!c.getCombat().checkMagicReqs(50)) {
						break;
					}
					if (itemId == 995) {
						c.sendMessage("You can't alch coins.");
						break;
					}
					int reward = (int) (c.getShops().getItemShopValue(itemId) * .75);
					int playerAmount = c.getInventory().getItemAmount(995);
					if (reward + playerAmount > 2147483647) {
						c.sendMessage("You can't alch anymore!");
						break;
					}
					if (c.getInventory().playerHasItem(itemId, slot, 1)) {
						c.getInventory().deleteItem(itemId, slot, 1);
						c.getInventory()
								.addItem(
										995,
										(int) (c.getShops().getItemShopValue(
												itemId) * .75));
						c.startAnimation(c.MAGIC_SPELLS[50][2]);
						c.gfx100(c.MAGIC_SPELLS[50][3]);
						c.alchDelay = System.currentTimeMillis();
						c.getActionSender().forceOpenTab(6);
						c.getActionSender().addSkillXP(
								c.MAGIC_SPELLS[50][7]
										* GameConstants.MAGIC_EXP_RATE, 6);
						c.getActionSender().refreshSkill(6);
					}
				}
			}
			break;
		}
	}
}
