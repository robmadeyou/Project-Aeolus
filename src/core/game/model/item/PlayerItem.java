package core.game.model.item;

/**
 * Gets Items From The Item List
 * 
 * @author Patrick Lewis (Dragonking)
 */
public class PlayerItem {
	public int id;
	private String name;
	private String description;
	private String equipSlot;
	private int value;

	private int attackDelay;
	private int[] bonuses = null;
	private int[] animations = null;
	private int[] combatAnimations = null;
	private int[] levelRequirements = null;

	private boolean special = false;
	private boolean twoHanded = false;
	private boolean stackable = false;

	public boolean isStackable() {
		return stackable;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getValue() {
		return value;
	}

	public int[] getBonuses() {
		return bonuses;
	}

	public String getEquipSlot() {
		return equipSlot != null ? equipSlot : "weapon";
	}

	public boolean isSpecial() {
		return special;
	}

	public boolean isTwoHanded() {
		return twoHanded;
	}

	public int[] getAnimations() {
		return animations;
	}

	public int getStandAnim() {
		return animations != null && animations[0] != 0 ? animations[0] : 0x328;
	}

	public int getWalkAnim() {
		return animations != null && animations[1] != 0 ? animations[2] : 0x333;
	}

	public int getWalkTurnAnim() {
		return animations != null && animations[2] != 0 ? animations[1] : 0x337;
	}

	public int getWalkTurn180Anim() {
		return animations != null && animations[3] != 0 ? animations[3] : 0x334;
	}

	public int getWalkTurn90CWAnim() {
		return animations != null && animations[4] != 0 ? animations[4] : 0x335;
	}

	public int getWalkTurn90CCAnim() {
		return animations != null && animations[5] != 0 ? animations[5] : 0x336;
	}

	public int getRunAnim() {
		return animations != null && animations[6] != 0 ? animations[6] : 0x338;
	}

	public int[] getLevelRequirements() {
		return levelRequirements;
	}

	public int[] getCombatAnims() {
		return combatAnimations;
	}

	public PlayerItem(int _itemId) {
		id = _itemId;
	}

	public int getAttackDelay() {
		return attackDelay > 0 ? attackDelay : 5;
	}

	public int[] getCombatAnimations() {
		return combatAnimations;
	}

	public int getBonus(String type) {
		switch (type) {
		case "stab":
			return this.bonuses[0];
		case "slash":
			return this.bonuses[1];
		case "crush":
			return this.bonuses[2];
		case "magic":
			return this.bonuses[3];
		case "range":
			return this.bonuses[4];
		}
		return 0;
	}

	public int getCombatAnim(int type) {
		switch (type) {
		case 0:
			return this.combatAnimations != null
					&& this.combatAnimations[0] != 0 ? this.combatAnimations[0]
					: 451;
		case 1:
			return this.combatAnimations != null
					&& this.combatAnimations[1] != 0 ? this.combatAnimations[1]
					: 451;
		case 2:
			return this.combatAnimations != null
					&& this.combatAnimations[2] != 0 ? this.combatAnimations[2]
					: 451;
		case 3:
			return this.combatAnimations != null
					&& this.combatAnimations[3] != 0 ? this.combatAnimations[3]
					: 451;
		case 4:
			return this.combatAnimations != null
					&& this.combatAnimations[4] != 0 ? this.combatAnimations[4]
					: 424;

		}
		return 0;
	}

	public int getBonusById(int id) {
		return this.bonuses[id];
	}
}
