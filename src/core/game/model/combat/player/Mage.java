package core.game.model.combat.player;

/**
 * This class represents a CombatType of derived from using spells.
 * @author 7Winds
 */
public class Mage {

	public enum Spell {

		WIND_STRIKE(1152, 1, 711, 90, 91, 92, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0);
		
		private int magicId, levelReq, animationId, startGFX, projectileId, endGFX, maxHit,
		expGained, rune1, rune1Amt, rune2, rune2Amt, rune3, rune3Amt, rune4, rune4Amt;
		
		private Spell(int magicId, int levelReq, int animationId, int startGFX, int projectileId, int endGFX, int maxHit,
				int expGained, int rune1, int rune1Amt, int rune2, int rune2Amt, int rune3, int rune3Amt, int rune4, int rune4Amt) {
			this.magicId = magicId;
			this.levelReq = levelReq;
			this.animationId = animationId;
			this.startGFX = startGFX;
			this.projectileId = projectileId;
			this.endGFX = endGFX;
			this.maxHit = maxHit;
			this.expGained = expGained;
			this.rune1 = rune1;
			this.rune1Amt = rune1Amt;
			this.rune2 = rune2;
			this.rune2Amt = rune2Amt;
			this.rune3 = rune3;
			this.rune3Amt = rune3Amt;
			this.rune4 = rune4;
			this.rune4Amt = rune4Amt;
		}

		public int getMagicId() {
			return magicId;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public int getAnimationId() {
			return animationId;
		}

		public int getStartGFX() {
			return startGFX;
		}

		public int getProjectileId() {
			return projectileId;
		}

		public int getEndGFX() {
			return endGFX;
		}

		public int getMaxHit() {
			return maxHit;
		}

		public int getExpGained() {
			return expGained;
		}

		public int getRune1() {
			return rune1;
		}

		public int getRune1Amt() {
			return rune1Amt;
		}

		public int getRune2() {
			return rune2;
		}

		public int getRune2Amt() {
			return rune2Amt;
		}

		public int getRune3() {
			return rune3;
		}

		public int getRune3Amt() {
			return rune3Amt;
		}

		public int getRune4() {
			return rune4;
		}

		public int getRune4Amt() {
			return rune4Amt;
		}		
	}
}
