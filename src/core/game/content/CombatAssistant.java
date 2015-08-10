package core.game.content;

import java.util.Timer;
import java.util.TimerTask;

import core.game.model.entity.Hit;
import core.game.model.entity.Hit.HitType;
import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.combat.PlayerVsMob;
import core.game.model.entity.player.combat.PlayerVsPlayer;
import core.game.model.entity.player.combat.impl.magic.MagicData;
import core.game.model.entity.player.combat.impl.magic.MagicExtras;
import core.game.model.entity.player.combat.impl.magic.MagicMaxHit;
import core.game.model.entity.player.combat.impl.magic.MagicRequirements;
import core.game.model.entity.player.combat.impl.melee.MeleeData;
import core.game.model.entity.player.combat.impl.melee.MeleeExtras;
import core.game.model.entity.player.combat.impl.melee.MeleeMaxHit;
import core.game.model.entity.player.combat.impl.melee.MeleeRequirements;
import core.game.model.entity.player.combat.impl.melee.MeleeSpecial;
import core.game.model.entity.player.combat.impl.prayer.CombatPrayer;
import core.game.model.entity.player.combat.impl.ranged.RangeData;
import core.game.model.entity.player.combat.impl.ranged.RangeExtras;
import core.game.model.entity.player.combat.impl.ranged.RangeMaxHit;
import core.game.util.Misc;

public class CombatAssistant {

	private Player c;

	public CombatAssistant(Player player) {
		this.c = player;
	}

	public int[][] slayerReqs = { { 1648, 5 }, { 1612, 15 }, { 1643, 45 },
			{ 1618, 50 }, { 1624, 65 }, { 1610, 75 }, { 1613, 80 },
			{ 1615, 85 }, { 2783, 90 } };

	public void appendPoison() {
		if (System.currentTimeMillis() - c.lastPoisonSip > c.poisonImmune) {
			c.sendMessage("You have been poisoned.");
			c.poisonDamage = 7 + Misc.random(3);
			c.lastPoison = 18;
			final Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					if (c.lastPoison == 0) {
						c.dealHit(new Hit(c.poisonDamage, HitType.POISON));
						c.poisonDamage--;
					}
					if (c.isDead) {
						c.poisonDamage = 0;
						timer.cancel();
					}
					c.lastPoison = c.lastPoison == 0 ? 18 : c.lastPoison - 1;
					if (c.poisonDamage == 0 || !c.isActive) {
						timer.cancel();
					}
				}
			}, 0, 1000);
		}
	}

	public boolean goodSlayer(int i) {
		for (int j = 0; j < slayerReqs.length; j++) {
			if (slayerReqs[j][0] == MobHandler.npcs[i].npcType) {
				if (slayerReqs[j][1] > c.playerLevel[c.playerSlayer]) {
					c.sendMessage("You need a slayer level of "
							+ slayerReqs[j][1] + " to harm this NPC.");
					return false;
				}
			}
		}
		return true;
	}

	public boolean kalphite1(int i) {
		switch (MobHandler.npcs[i].npcType) {
		case 1158:
			return true;
		}
		return false;
	}

	public boolean kalphite2(int i) {
		switch (MobHandler.npcs[i].npcType) {
		case 1160:
			return true;
		}
		return false;
	}

	public void resetPlayerAttack() {
		MeleeData.resetPlayerAttack(c);
	}

	public int getCombatDifference(int combat1, int combat2) {
		return MeleeRequirements.getCombatDifference(combat1, combat2);
	}

	public int getKillerId(int playerId) {
		return MeleeRequirements.getKillerId(c, playerId);
	}

	public boolean checkReqs() {
		return MeleeRequirements.checkReqs(c);
	}

	public boolean checkMultiBarrageReqs(int i) {
		return MagicExtras.checkMultiBarrageReqs(c, i);
	}

	public int getRequiredDistance() {
		return MeleeRequirements.getRequiredDistance(c);
	}

	public void multiSpellEffectNPC(int npcId, int damage) {
		MagicExtras.multiSpellEffectNPC(c, npcId, damage);
	}

	public boolean checkMultiBarrageReqsNPC(int i) {
		return MagicExtras.checkMultiBarrageReqsNPC(i);
	}

	public void appendMultiBarrageNPC(int npcId, boolean splashed) {
		MagicExtras.appendMultiBarrageNPC(c, npcId, splashed);
	}

	public void attackNpc(int i) {
		PlayerVsMob.attackNpc(c, i);
		if (KQnpc(i) && !c.getEquipment().isWearingVeracs(c) || c.usingMagic) {
			resetPlayerAttack();
			c.sendMessage("Your attacks are ineffective against the Kalphite Queen!");
			return;
		}
	}

	public boolean KQnpc(int i) {
		if (MobHandler.npcs[i] != null)
			switch (MobHandler.npcs[i].npcType) {
			case 1158:
				return true;
			}
		return false;
	}

	public void delayedHit(final Player c, final int i) {
		PlayerVsMob.delayedHit(c, i);

	}

	public void applyNpcMeleeDamage(int i, int damageMask, int damage) {
		PlayerVsMob.applyNpcMeleeDamage(c, i, damageMask, damage);
	}

	public void attackPlayer(int i) {
		PlayerVsPlayer.attackPlayer(c, i);
	}

	public void playerDelayedHit(final Player c, final int i) {
		PlayerVsPlayer.playerDelayedHit(c, i);
	}

	public void applyPlayerMeleeDamage(int i, int damageMask, int damage) {
		PlayerVsPlayer.applyPlayerMeleeDamage(c, i, damageMask, damage);
	}

	public void addNPCHit(int i, Player c) {
		PlayerVsMob.addNPCHit(i, c);
	}

	public void applyPlayerHit(Player c, final int i) {
		PlayerVsPlayer.applyPlayerHit(c, i);
	}

	public void fireProjectileNpc() {
		RangeData.fireProjectileNpc(c);
	}

	public void fireProjectilePlayer() {
		RangeData.fireProjectilePlayer(c);
	}

	public boolean usingCrystalBow() {
		return c.playerEquipment[c.playerWeapon] >= 4212
				&& c.playerEquipment[c.playerWeapon] <= 4223;
	}

	public boolean multis() {
		return MagicData.multiSpells(c);
	}

	public void appendMultiBarrage(int playerId, boolean splashed) {
		MagicExtras.appendMultiBarrage(c, playerId, splashed);
	}

	public void multiSpellEffect(int playerId, int damage) {
		MagicExtras.multiSpellEffect(c, playerId, damage);
	}

	public void applySmite(int index, int damage) {
		MeleeExtras.applySmite(c, index, damage);
	}

	public boolean usingDbow() {
		return c.playerEquipment[c.playerWeapon] == 11235;
	}

	public boolean usingHally() {
		return MeleeData.usingHally(c);
	}

	public void getPlayerAnimIndex(String weaponName) {
		MeleeData.getPlayerAnimIndex(c, weaponName);
	}

	public int getWepAnim(String weaponName) {
		return MeleeData.getWepAnim(c, weaponName);
	}

	public int getBlockEmote() {
		return MeleeData.getBlockEmote(c);
	}

	public int getAttackDelay(int id) {
		return MeleeData.getAttackDelay(c, id);
	}

	public int getHitDelay(int i, String weaponName) {
		return MeleeData.getHitDelay(c, i, weaponName);
	}

	public int npcDefenceAnim(int i) {
		return MeleeData.npcDefenceAnim(i);
	}

	public int calculateMeleeAttack() {
		return MeleeMaxHit.calculateMeleeAttack(c);
	}

	public int bestMeleeAtk() {
		return MeleeMaxHit.bestMeleeAtk(c);
	}

	public int calculateMeleeMaxHit() {
		return (int) MeleeMaxHit.calculateBaseDamage(c, c.usingSpecial);
	}

	public int calculateMeleeDefence() {
		return MeleeMaxHit.calculateMeleeDefence(c);
	}

	public int bestMeleeDef() {
		return MeleeMaxHit.bestMeleeDef(c);
	}

	public void addCharge() {
		MeleeExtras.addCharge(c);
	}

	public void handleDfs(final Player c) {
		MeleeExtras.handleDragonFireShield(c);
	}

	public void handleDfsNPC(final Player c) {
		MeleeExtras.handleDragonFireShieldNPC(c);
	}

	public void appendVengeanceNPC(int otherPlayer, int damage) {
		MeleeExtras.appendVengeanceNPC(c, otherPlayer, damage);
	}

	public void appendVengeance(int otherPlayer, int damage) {
		MeleeExtras.appendVengeance(c, otherPlayer, damage);
	}

	public void applyRecoilNPC(int damage, int i) {
		MeleeExtras.applyRecoilNPC(c, damage, i);
	}

	public void applyRecoil(int damage, int i) {
		MeleeExtras.applyRecoil(c, damage, i);
	}

	public void removeRecoil(Player c) {
		MeleeExtras.removeRecoil(c);
	}

	public void handleGmaulPlayer() {
		MeleeExtras.graniteMaulSpecial(c);
	}

	public void activateSpecial(int weapon, int i) {
		MeleeSpecial.activateSpecial(c, weapon, i);
	}

	public boolean checkSpecAmount(int weapon) {
		return MeleeSpecial.checkSpecAmount(c, weapon);
	}

	public int calculateRangeAttack() {
		return RangeMaxHit.calculateRangeAttack(c);
	}

	public int calculateRangeDefence() {
		return RangeMaxHit.calculateRangeDefence(c);
	}

	public int rangeMaxHit() {
		return RangeMaxHit.maxHit(c);
	}

	public int getRangeStr(int i) {
		return RangeData.getRangeStr(i);
	}

	public int getRangeStartGFX() {
		return RangeData.getRangeStartGFX(c);
	}

	public int getRangeProjectileGFX() {
		return RangeData.getRangeProjectileGFX(c);
	}

	public int correctBowAndArrows() {
		return RangeData.correctBowAndArrows(c);
	}

	public int getProjectileShowDelay() {
		return RangeData.getProjectileShowDelay(c);
	}

	public int getProjectileSpeed() {
		return RangeData.getProjectileSpeed(c);
	}

	public void crossbowSpecial(Player c, int i) {
		RangeExtras.crossbowSpecial(c, i);
	}

	public void appendMutliChinchompa(int npcId) {
		RangeExtras.appendMutliChinchompa(c, npcId);
	}

	public boolean properBolts() {
		return usingBolts(c.playerEquipment[c.playerArrows]);
	}

	public boolean usingBolts(int i) {
		return (i >= 9140 && i <= 9145) || (i >= 9236 && i <= 9245);
	}

	public int mageAtk() {
		return MagicMaxHit.mageAttack(c);
	}

	public int mageDef() {
		return MagicMaxHit.mageDefefence(c);
	}

	public int magicMaxHit() {
		return MagicMaxHit.magiMaxHit(c);
	}

	public boolean wearingStaff(int runeId) {
		return MagicRequirements.wearingStaff(c, runeId);
	}

	public boolean checkMagicReqs(int spell) {
		return MagicRequirements.checkMagicReqs(c, spell);
	}

	public int getMagicGraphic(Player c, int i) {
		return MagicData.getMagicGraphic(c, i);
	}

	public int getFreezeTime() {
		return MagicData.getFreezeTime(c);
	}

	public int getStartHeight() {
		return MagicData.getStartHeight(c);
	}

	public int getEndHeight() {
		return MagicData.getEndHeight(c);
	}

	public int getStartDelay() {
		return MagicData.getStartDelay(c);
	}

	public int getStaffNeeded() {
		return MagicData.getStaffNeeded(c);
	}

	public boolean godSpells() {
		return MagicData.godSpells(c);
	}

	public int getEndGfxHeight() {
		return MagicData.getEndGfxHeight(c);
	}

	public int getStartGfxHeight() {
		return MagicData.getStartGfxHeight(c);
	}

	public void handlePrayerDrain() {
		CombatPrayer.handlePrayerDrain(c);
	}

	public void reducePrayerLevel() {
		CombatPrayer.reducePrayerLevel(c);
	}

	public void resetPrayers() {
		CombatPrayer.resetPrayers(c);
	}

	public void activatePrayer(int i) {
		if (c.isPrayerDisabled()) {
			c.sendMessage("Prayers are currently disabled.");
			return;
		}
		CombatPrayer.activatePrayer(c, i);
	}
}