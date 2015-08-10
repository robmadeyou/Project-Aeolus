package core.game.model.entity.player.combat.impl.prayer;

/**
 * Class which holds information about each prayer
 * @author 7Winds
 */
public class PrayerData {
	
	/**
	 * The type of prayer used
	 */
	public enum PrayerType {
		NORMAL,
		ANCIENT,
		CURSED,
		QUICK;	
	}

	/**
	 * Contains all regular prayers
	 */
	public enum NormalPrayerType {
		THICK_SKIN,
		BURST_OF_STRENGTH,
		CLARITY_OF_THOUGHT,
		SHARP_EYE,
		MYSTIC_WILL,
		ROCK_SKIN,
		SUPERHUMAN_STRENGTH,
		IMPROVED_REFLEXES,
		RAPID_RESTORE,
		RAPID_HEAL,
		PROTECT_ITEM,
		HAWK_EYE,
		MYSTIC_LORE,
		STEEL_SKIN,
		ULTIMATE_STRENGTH,
		INCREDIBLE_REFLEXES,
		PROTECT_FROM_MAGIC,
		PROTECT_FROM_MISSLES,
		PROTECT_FROM_MELEE,
		EAGLE_EYE,
		MYSTIC_MIGHT,
		REDEMPTION,
		SMITE,
		CHIVALRY,
		PIETY;
	}
}
