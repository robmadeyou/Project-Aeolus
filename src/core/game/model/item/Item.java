package core.game.model.item;

import core.game.GameConstants;

/**
 * @author Sanity Revised by Shawn Notes by Shawn
 */
public class Item {

	/**
	 * Full body items that cover your arms. Names go here.
	 */
	private static String[] fullbody = { "wings", "Spined body", "plate",
			"Monk's robe", "torso", "top", "shirt", "platebody",
			"Ahrims robetop", "Karils leathertop", "brassard", "Robe top",
			"robetop", "platebody (t)", "platebody (g)", "chestplate", "torso",
			"hauberk", "Dragon chainbody" };

	/**
	 * Full hat items that cover your head go here. Names go here.
	 */
	private static String[] fullhat = { "helmet", "mage hat", "med helm",
			"coif", "Dharok's helm", "hood", "Initiate helm", "Coif",
			"Helm of neitiznot", "Armadyl helmet", "Berserker helm",
			"Archer helm", "Farseer helm", "Warrior helm", "Void" };

	/**
	 * Full mask items that cover your face go here. Names go here.
	 */
	private static String[] fullmask = { "helm (t)", "full helm(g)", "full",
			"heraldic", "heraldic helm", "full helm", "mask", "Verac's helm",
			"Guthan's helm", "Karil's coif", "mask", "Torag's helm", "Void",
			"sallet" };

	/**
	 * Calls if an item is a full body item.
	 */
	public static boolean isFullBody(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (int i = 0; i < fullbody.length; i++) {
			if (weapon.endsWith(fullbody[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calls if an item is a full helm item.
	 */
	public static boolean isFullHelm(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (int i = 0; i < fullhat.length; i++) {
			if (weapon.endsWith(fullhat[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calls if an item is a full mask item.
	 */
	public static boolean isFullMask(int itemId) {
		String weapon = getItemName(itemId);
		if (weapon == null)
			return false;
		for (int i = 0; i < fullmask.length; i++) {
			if (weapon.endsWith(fullmask[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets an item name from the ItemDefinitions.json
	 * 
	 * @param ItemID
	 */
	public static String getItemName(int ItemID) {
		if (ItemID < 0)
			return "Unarmed";
		return ItemDefinition.getDefinitions()[ItemID].getName();
	}

	public static boolean isStackable(int itemId) {
		return ItemDefinition.getDefinitions()[itemId].isStackable();
	}

	public static boolean itemIsNote(int itemId) {
			return ItemDefinition.getDefinitions()[itemId].isNoted();
	}
}