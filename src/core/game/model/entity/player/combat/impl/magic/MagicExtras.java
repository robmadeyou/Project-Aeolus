package core.game.model.entity.player.combat.impl.magic;

import core.Configuration;
import core.game.GameConstants;
import core.game.model.entity.Hit;
import core.game.model.entity.mob.Mob;
import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.util.Misc;

public class MagicExtras {

	public static void multiSpellEffectNPC(Player c, int npcId, int damage) {
		switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 12891:
		case 12881:
			if (MobHandler.npcs[npcId].freezeTimer < -4) {
				MobHandler.npcs[npcId].freezeTimer = c.getCombat()
						.getFreezeTime();
			}
			break;
		}
	}

	public static boolean checkMultiBarrageReqsNPC(int i) {
		if (MobHandler.npcs[i] == null) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean checkMultiBarrageReqs(Player c, int i) {
		if (PlayerHandler.players[i] == null) {
			return false;
		}
		if (i == c.playerId)
			return false;
		if (!PlayerHandler.players[i].inWild()) {
			return false;
		}
		if (Configuration.COMBAT_LEVEL_DIFFERENCE) {
			int combatDif1 = c.getCombat().getCombatDifference(c.combatLevel,
					PlayerHandler.players[i].combatLevel);
			if (combatDif1 > c.wildLevel
					|| combatDif1 > PlayerHandler.players[i].wildLevel) {
				c.sendMessage("Your combat level difference is too great to attack that player here.");
				return false;
			}
		}

		if (Configuration.SINGLE_AND_MULTI_ZONES) {
			if (!PlayerHandler.players[i].inMulti()) { // single combat zones
				if (PlayerHandler.players[i].underAttackBy != c.playerId
						&& PlayerHandler.players[i].underAttackBy != 0) {
					return false;
				}
				if (PlayerHandler.players[i].playerId != c.underAttackBy
						&& c.underAttackBy != 0) {
					c.sendMessage("You are already in combat.");
					return false;
				}
			}
		}
		return true;
	}

	public static void appendMultiBarrageNPC(Player c, int npcId,
			boolean splashed) {
		if (MobHandler.npcs[npcId] != null) {
			Mob n = (Mob) MobHandler.npcs[npcId];
			if (n.isDead)
				return;
			if (checkMultiBarrageReqsNPC(npcId)) {
				c.barrageCount++;
				c.multiAttacking = true;
				MobHandler.npcs[npcId].underAttackBy = c.playerId;
				MobHandler.npcs[npcId].underAttack = true;
				if (Misc.random(c.getCombat().mageAtk()) > Misc.random(c
						.getCombat().mageDef()) && !c.magicFailed) {
					if (c.getCombat().getEndGfxHeight() == 100) { // end GFX
						n.gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
					} else {
						n.gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
					}
					int damage = Misc.random(c.getCombat().magicMaxHit());
					if (MobHandler.npcs[npcId].HP - damage < 0) {
						damage = MobHandler.npcs[npcId].HP;
					}
					MobHandler.npcs[npcId].handleHitMask(damage);
					MobHandler.npcs[npcId].dealHit(new Hit(damage));
					c.totalPlayerDamageDealt += damage;
					c.getCombat().multiSpellEffectNPC(npcId, damage);
					c.totalDamageDealt += damage;
					c.getCombat().multiSpellEffectNPC(npcId, damage);
				} else {
					n.gfx100(85);
				}
			}
		}
	}

	public static void multiSpellEffect(Player c, int playerId, int damage) {
		switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 13011:
		case 13023:
			if (System.currentTimeMillis()
					- PlayerHandler.players[playerId].reduceStat > 35000) {
				PlayerHandler.players[playerId].reduceStat = System
						.currentTimeMillis();
				PlayerHandler.players[playerId].playerLevel[0] -= ((PlayerHandler.players[playerId]
						.getLevelForXP(PlayerHandler.players[playerId].playerXP[0]) * 10) / 100);
			}
			break;
		case 12919: // blood spells
		case 12929:
			int heal = (int) (damage / 4);
			if (c.playerLevel[3] + heal >= c.getActionSender().getLevelForXP(
					c.playerXP[3])) {
				c.playerLevel[3] = c.getActionSender().getLevelForXP(c.playerXP[3]);
			} else {
				c.playerLevel[3] += heal;
			}
			c.getActionSender().refreshSkill(3);
			break;
		case 12891:
		case 12881:
			if (PlayerHandler.players[playerId].freezeTimer < -4) {
				PlayerHandler.players[playerId].freezeTimer = c.getCombat()
						.getFreezeTime();
				PlayerHandler.players[playerId].stopMovement();
			}
			break;
		}
	}

	public static void appendMultiBarrage(Player c, int playerId,
			boolean splashed) {
		if (PlayerHandler.players[playerId] != null) {
			Player c2 = PlayerHandler.players[playerId];
			if (c2.isDead || c2.respawnTimer > 0)
				return;
			if (c.getCombat().checkMultiBarrageReqs(playerId)) {
				c.barrageCount++;
				if (Misc.random(c.getCombat().mageAtk()) > Misc.random(c2
						.getCombat().mageDef()) && !c.magicFailed) {
					if (c.getCombat().getEndGfxHeight() == 100) { // end GFX
						c2.gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
					} else {
						c2.gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
					}
					int damage = Misc.random(c.getCombat().magicMaxHit());
					if (c2.prayerActive[12]) {
						damage *= (int) (.60);
					}
					if (c2.playerLevel[3] - damage < 0) {
						damage = c2.playerLevel[3];
					}
					c.getActionSender().addSkillXP(
							(c.MAGIC_SPELLS[c.oldSpellId][7] + damage
									* GameConstants.MAGIC_EXP_RATE), 6);
					c.getActionSender().addSkillXP(
							(c.MAGIC_SPELLS[c.oldSpellId][7] + damage
									* GameConstants.MAGIC_EXP_RATE / 3), 3);
					PlayerHandler.players[playerId].dealHit(new Hit(damage));
					PlayerHandler.players[playerId].damageTaken[c.playerId] += damage;
					c2.getActionSender().refreshSkill(3);
					c.totalPlayerDamageDealt += damage;
					c.getCombat().multiSpellEffect(playerId, damage);
				} else {
					c2.gfx100(85);
				}
			}
		}
	}
}