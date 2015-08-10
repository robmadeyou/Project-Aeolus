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
	public enum NormalPrayer {
		THICK_SKIN(1, 446),
		BURST_OF_STRENGTH(1, 450),
		CLARITY_OF_THOUGHT(1, 448),
		SHARP_EYE(1, -1),
		MYSTIC_WILL(1, -1),
		ROCK_SKIN(2, 449),
		SUPERHUMAN_STRENGTH(2, 434),
		IMPROVED_REFLEXES(2, 436),
		RAPID_RESTORE(0.4, -1),
		RAPID_HEAL(0.6, -1),
		PROTECT_ITEM(0.6, 433),
		HAWK_EYE(1.5, -1),
		MYSTIC_LORE(2, -1),
		STEEL_SKIN(4, 439),
		ULTIMATE_STRENGTH(4, 441),
		INCREDIBLE_REFLEXES(4, 440),
		PROTECT_FROM_MAGIC(4, 438),
		PROTECT_FROM_MISSLES(4, 444),
		PROTECT_FROM_MELEE(4, 433),
		EAGLE_EYE(4, -1),
		MYSTIC_MIGHT(4, -1),
		RETRIBUTION(1, -1),
		REDEMPTION(2, -1),
		SMITE(6, -1),
		CHIVALRY(8, -1),
		PIETY(8, -1);
		
		private double drainRate;
		private int soundId;
		
		private NormalPrayer(double drainRate, int soundId) {
			this.drainRate = drainRate;
			this.soundId = soundId;
		}
		
		public double getDrainRate() {
			return drainRate;
		}
		
		public int getSoundId() {
			return soundId;
		}
	}
}
