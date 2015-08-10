package core.game.model.entity.player.combat;

import core.Configuration;
import core.Server;
import core.game.GameConstants;
import core.game.event.task.Task;
import core.game.event.task.TaskHandler;
import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.entity.player.combat.impl.ranged.RangeData;
import core.game.sound.SoundManager;
import core.game.sound.SoundManager.SoundType;
import core.game.util.Misc;

public class PlayerVsMob {

	/** Integer holding the undead npc ids. */
	public static final int[] UNDEAD_NPC = { 1604, 1605, 1606, 1607, 4381,
			4382, 4383, 1612, 4226, 1648, 1649, 1650, 1651, 1652, 1653, 1654,
			1655, 1656, 1657, 1541, 1549, 491, 103, 104, 6094, 6095, 6096,
			6097, 6098, 5572, 5374, 5373, 5372, 5371, 5370, 5369, 5352, 5351,
			5350, 5349, 5348, 5347, 5346, 5345, 5344, 5343, 5342, 4388, 4387,
			2931, 2716, 1708, 1707, 1053, 1052, 3611, 3610, 3609, 3608, 3607,
			3606, 2019, 2018, 2017, 2016, 2015, 1968, 1967, 1966, 1965, 1964,
			1963, 1962, 1961, 1959, 1958, 430, 429, 428, 427, 426, 425, 3617,
			459, 90, 91, 92, 93, 6093, 6092, 6091, 5422, 5412, 5411, 5385,
			5386, 5387, 5388, 5389, 5390, 5391, 5392, 5381, 5368, 5367, 5366,
			5365, 5332, 5333, 5334, 5335, 5336, 5337, 5338, 5339, 5340, 5341,
			4386, 4385, 4384, 3581, 3291, 3151, 2715, 2037, 2036, 1471, 6104,
			6107, 6105, 77, 2057, 2056, 2050, 1698, 1692, 1691, 5211, 502, 503,
			504, 505, 5353, 5354, 5355, 5356, 5357, 5358, 6088, 6089, 6090,
			5393, 5394, 5395, 5396, 5397, 5398, 5399, 5400, 5401, 5402, 5403,
			5404, 5405, 5406, 5407, 5408, 5409, 5410, 5375, 5376, 5378, 5379,
			5380, 5331, 5330, 5329, 5328, 5327, 5326, 5325, 5324, 5323, 5322,
			5321, 5320, 5319, 5318, 5317, 5316, 5315, 5314, 5313, 5312, 5311,
			5310, 5309, 5308, 5307, 5306, 5305, 5304, 5303, 5302, 5301, 5300,
			5299, 5298, 5297, 5296, 5295, 5294, 5293, 4394, 4393, 4392, 3622,
			2878, 2869, 2866, 2863, 2714, 2058, 419, 420, 421, 422, 423, 424,
			73, 74, 75, 76, 2044, 2045, 2046, 2047, 2048, 2049, 2051, 2052,
			2053, 2054, 2055 };

	public static void addNPCHit(final int i, final Player c) {
		if (c.projectileStage == 0 && !c.usingMagic) {
			c.stopPlayerSkill = false;
				c.getCombat().applyNpcMeleeDamage(i, 1,
						Misc.random(c.getCombat().calculateMeleeMaxHit()));
				if (c.doubleHit) {
					if (!c.oldSpec) {
						c.getCombat().applyNpcMeleeDamage(
								i,
								2,
								Misc.random(c.getCombat()
										.calculateMeleeMaxHit()));

					} else {						
						TaskHandler.submit(new Task(1, false) {

							@Override
							public void execute() {
								c.getCombat()
								.applyNpcMeleeDamage(
										i,
										2,
										Misc.random(c
												.getCombat()
												.calculateMeleeMaxHit()));
						this.cancel();								
							}						
						});
					}
			}
		}
	}

	public static boolean kalphite1(int i) {
		switch (MobHandler.npcs[i].npcType) {
		case 1158:
			return true;
		}
		return false;
	}

	public static boolean kalphite2(int i) {
		switch (MobHandler.npcs[i].npcType) {
		case 1160:
			return true;
		}
		return false;
	}

	public static void applyNpcMeleeDamage(Player c, int i, int damageMask,
			int damage) {
			damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
		c.previousDamage = damage;

		boolean fullVeracsEffect = c.getEquipment().isWearingVeracs(c)
				&& Misc.random(3) == 1;
		if (MobHandler.npcs[i].HP - damage < 0) {
			damage = MobHandler.npcs[i].HP;
		}
		if (MobHandler.npcs[i].npcType == 5666) {
			damage = damage / 4;
			if (damage < 0) {
				damage = 0;
			}
		}
		if (!c.usingSpecial)
			SoundManager.sendSound(c, c.playerEquipment[c.playerWeapon],
					SoundType.MELEE_COMBAT);

		if (!fullVeracsEffect) {
			if (Misc.random(MobHandler.npcs[i].defence) > 10 + Misc.random(c
					.getCombat().calculateMeleeAttack())) {
				damage = 0;
			} else if (MobHandler.npcs[i].npcType == 2882
					|| MobHandler.npcs[i].npcType == 2883) {
				damage = 0;
			}
		}

		boolean guthansEffect = false;
		if (c.getEquipment().isWearingGuthans(c)) {
			if (Misc.random(3) == 1) {
				guthansEffect = true;
			}
		}
		if (c.playerEquipment[c.playerWeapon] == 4718
				&& c.playerEquipment[c.playerHat] == 4716
				&& c.playerEquipment[c.playerChest] == 4720
				&& c.playerEquipment[c.playerLegs] == 4722) {
			damage = Misc.random(c.getCombat().calculateMeleeMaxHit());
		}
		for (int undeadNPC : UNDEAD_NPC)
			if (MobHandler.npcs[i].npcType == undeadNPC) {
				if (c.playerEquipment[c.playerAmulet] == 10588)
					damage = (int) (damage * (double) 1.15);
				else if (c.playerEquipment[c.playerAmulet] == 4081)
					damage = (int) (damage * (double) 1.05);
			}
		if (MobHandler.npcs[i].HP - damage < 0) {
			damage = MobHandler.npcs[i].HP;
		}
		if (MobHandler.npcs[i].npcType == c.slayerTask
				&& c.playerEquipment[c.playerHat] == 13263
				&& !(c.playerEquipment[c.playerAmulet] == 10588)
				&& !(c.playerEquipment[c.playerAmulet] == 4081)) {
			damage = (int) (damage * (double) 1.15);
		}
		if (c.fightMode == 3) {
			c.getActionSender().addSkillXP((damage *GameConstants.MELEE_EXP_RATE / 3), 0);
			c.getActionSender().addSkillXP((damage * GameConstants.MELEE_EXP_RATE / 3), 1);
			c.getActionSender().addSkillXP((damage * GameConstants.MELEE_EXP_RATE / 3), 2);
			c.getActionSender().addSkillXP((damage * GameConstants.MELEE_EXP_RATE / 3), 3);
			c.getActionSender().refreshSkill(0);
			c.getActionSender().refreshSkill(1);
			c.getActionSender().refreshSkill(2);
			c.getActionSender().refreshSkill(3);
		} else {
			c.getActionSender().addSkillXP((damage * GameConstants.MELEE_EXP_RATE), c.fightMode);
			c.getActionSender().addSkillXP((damage * GameConstants.MELEE_EXP_RATE / 3), 3);
			c.getActionSender().refreshSkill(c.fightMode);
			c.getActionSender().refreshSkill(3);
		}
		if (damage > 0) {
			// Pest Control shit was here
		}
		if (damage > 0 && guthansEffect) {
			c.playerLevel[3] += damage;
			if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3]))
				c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
			c.getActionSender().refreshSkill(3);
			MobHandler.npcs[i].gfx0(398);
		}

		MobHandler.npcs[i].underAttack = true;
		c.killingNpcIndex = c.npcIndex;
		c.lastNpcAttacked = i;

		switch (c.specEffect) {
		case 4:
			if (damage > 0) {
				if (c.playerLevel[3] + damage > c.getLevelForXP(c.playerXP[3]))
					if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3]))
						;
					else
						c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
				else
					c.playerLevel[3] += damage;
				c.getActionSender().refreshSkill(3);
			}
			c.specEffect = 0;
			break;
		}

		switch (damageMask) {
		case 1:
			MobHandler.npcs[i].hitDiff = damage;
			MobHandler.npcs[i].HP -= damage;
			c.totalDamageDealt += damage;
			MobHandler.npcs[i].hitUpdateRequired = true;
			MobHandler.npcs[i].updateRequired = true;
			break;

		case 2:
			MobHandler.npcs[i].hitDiff2 = damage;
			MobHandler.npcs[i].HP -= damage;
			c.totalDamageDealt += damage;
			MobHandler.npcs[i].hitUpdateRequired2 = true;
			MobHandler.npcs[i].updateRequired = true;
			c.doubleHit = false;
			break;

		}
	}

	public static void delayedHit(final Player c, final int i) {
		if (MobHandler.npcs[i] != null) {
			if (MobHandler.npcs[i].isDead) {
				c.npcIndex = 0;
				return;
			}

			MobHandler.npcs[i].facePlayer(c.playerId);

			if (MobHandler.npcs[i].underAttackBy > 0
					&& Server.npcHandler.getsPulled(i)) {
				MobHandler.npcs[i].killerId = c.playerId;
			} else if (MobHandler.npcs[i].underAttackBy < 0
					&& !Server.npcHandler.getsPulled(i)) {
				MobHandler.npcs[i].killerId = c.playerId;
			}

			c.lastNpcAttacked = i;

			c.getCombat().addNPCHit(i, c);
			c.getCombat().addCharge();

			if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
				int damage = Misc.random(c.getCombat().rangeMaxHit());
				int damage2 = -1;

				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1)
					damage2 = Misc.random(c.getCombat().rangeMaxHit());
				if (c.playerEquipment[3] == 9185) {
					if (Misc.random(10) == 1) {
						if (damage > 0) {
							c.boltDamage = damage;
							c.getCombat().crossbowSpecial(c, i);
							damage *= c.crossbowDamage;
						}
					}
				}
				if (Misc.random(MobHandler.npcs[i].defence) > Misc
						.random(10 + c.getCombat().calculateRangeAttack())
						&& !c.ignoreDefence) {
					damage = 0;
				} else if (MobHandler.npcs[i].npcType == 2881
						|| MobHandler.npcs[i].npcType == 2883
						&& !c.ignoreDefence) {
					damage = 0;
				}
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1) {
					if (Misc.random(MobHandler.npcs[i].defence) > Misc
							.random(10 + c.getCombat().calculateRangeAttack()))
						damage2 = 0;
				}
				if (c.dbowSpec) {
					MobHandler.npcs[i].gfx100(c.lastArrowUsed == 11212 ? 1100
							: 1103);
					if (damage < 8)
						damage = 8;
					if (damage2 < 8)
						damage2 = 8;
					c.dbowSpec = false;
				}
				if (c.playerEquipment[3] == 14614) {
					damage *= 2.00;
				}
				if (MobHandler.npcs[i].HP - damage < 0) {
					damage = MobHandler.npcs[i].HP;
				}
				if (damage2 > 0) {
					if (damage == MobHandler.npcs[i].HP
							&& MobHandler.npcs[i].HP - damage2 > 0) {
						damage2 = 0;
					}
				}
				if (c.fightMode == 3) {
					c.getActionSender().addSkillXP((damage * GameConstants.RANGE_EXP_RATE / 3),
							4);
					c.getActionSender().addSkillXP((damage * GameConstants.RANGE_EXP_RATE / 3),
							1);
					c.getActionSender().addSkillXP((damage * GameConstants.RANGE_EXP_RATE / 3),
							3);
					c.getActionSender().refreshSkill(1);
					c.getActionSender().refreshSkill(3);
					c.getActionSender().refreshSkill(4);
				} else {
					c.getActionSender().addSkillXP((damage * GameConstants.RANGE_EXP_RATE), 4);
					c.getActionSender().addSkillXP((damage * GameConstants.RANGE_EXP_RATE / 3),
							3);
					c.getActionSender().refreshSkill(3);
					c.getActionSender().refreshSkill(4);
				}
				if (damage > 0) {

				}
				boolean dropArrows = true;

				for (int noArrowId : c.NO_ARROW_DROP) {
					if (c.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					c.getItems().dropArrowNpc();
					if (c.playerEquipment[3] == 11235) {
						c.getItems().dropArrowNpc();
					}
				}
				if (Server.npcHandler.getNPCs()[i].attackTimer < 9)
					MobHandler.startAnimation(c.getCombat().npcDefenceAnim(i),
							i);
				c.rangeEndGFX = RangeData.getRangeEndGFX(c);

				if ((c.playerEquipment[3] == 10034 || c.playerEquipment[3] == 10033)) {
					for (int j = 0; j < MobHandler.npcs.length; j++) {
						if (MobHandler.npcs[j] != null
								&& MobHandler.npcs[j].maxHP > 0) {
							int nX = MobHandler.npcs[j].getX();
							int nY = MobHandler.npcs[j].getY();
							int pX = MobHandler.npcs[i].getX();
							int pY = MobHandler.npcs[i].getY();
							if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1)
									&& (nY - pY == -1 || nY - pY == 0 || nY
											- pY == 1)) {
								if (MobHandler.npcs[i].inMulti()) {
									Player p = PlayerHandler.players[c.playerId];
									c.getCombat().appendMutliChinchompa(j);
									Server.npcHandler.attackPlayer(p, j);
								}
							}
						}
					}
				}
				if (!c.multiAttacking) {
					MobHandler.npcs[i].underAttack = true;
					MobHandler.npcs[i].hitDiff = damage;
					MobHandler.npcs[i].HP -= damage;
					if (damage2 > -1) {
						MobHandler.npcs[i].hitDiff2 = damage2;
						MobHandler.npcs[i].HP -= damage2;
						c.totalDamageDealt += damage2;
					}
				}
				c.ignoreDefence = false;
				c.multiAttacking = false;

				if (c.rangeEndGFX > 0) {
					if (c.rangeEndGFXHeight) {
						MobHandler.npcs[i].gfx100(c.rangeEndGFX);
					} else {
						MobHandler.npcs[i].gfx0(c.rangeEndGFX);
					}
				}
				if (c.killingNpcIndex != c.oldNpcIndex) {
					c.totalDamageDealt = 0;
				}
				c.killingNpcIndex = c.oldNpcIndex;
				c.totalDamageDealt += damage;
				MobHandler.npcs[i].hitUpdateRequired = true;
				if (damage2 > -1)
					MobHandler.npcs[i].hitUpdateRequired2 = true;
				MobHandler.npcs[i].updateRequired = true;

			} else if (c.projectileStage > 0) { // magic hit damage
				if (MobHandler.npcs[i].HP <= 0) {
					return;
				}
				if (c.spellSwap) {
					c.spellSwap = false;
					c.setSidebarInterface(6, 16640);
					c.playerMagicBook = 2;
					c.gfx0(-1);
				}
				int damage = 0;
				c.usingMagic = true;
				if (c.fullVoidMage()
						&& c.playerEquipment[c.playerWeapon] == 8841) {
					damage = Misc.random(c.getCombat().magicMaxHit() + 10);
				} else {
					damage = Misc.random(c.getCombat().magicMaxHit());
				}
				if (c.getCombat().godSpells()) {
					if (System.currentTimeMillis() - c.godSpellDelay < GameConstants.GOD_SPELL_CHARGE) {
						damage += Misc.random(10);
					}
				}
				boolean magicFailed = false;
				if (Misc.random(MobHandler.npcs[i].defence) > 10 + Misc
						.random(c.getCombat().mageAtk())) {
					damage = 0;
					magicFailed = true;
				} else if (MobHandler.npcs[i].npcType == 2881
						|| MobHandler.npcs[i].npcType == 2882) {
					damage = 0;
					magicFailed = true;
				}
				for (int j = 0; j < MobHandler.npcs.length; j++) {
					if (MobHandler.npcs[j] != null
							&& MobHandler.npcs[j].maxHP > 0) {
						int nX = MobHandler.npcs[j].getX();
						int nY = MobHandler.npcs[j].getY();
						int pX = MobHandler.npcs[i].getX();
						int pY = MobHandler.npcs[i].getY();
						if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1)
								&& (nY - pY == -1 || nY - pY == 0 || nY - pY == 1)) {
							if (c.getCombat().multis()
									&& MobHandler.npcs[i].inMulti()) {
								Player p = PlayerHandler.players[c.playerId];
								c.getCombat().appendMultiBarrageNPC(j,
										c.magicFailed);
								Server.npcHandler.attackPlayer(p, j);
							}
						}
					}
				}
				if (MobHandler.npcs[i].HP - damage < 0) {
					damage = MobHandler.npcs[i].HP;
				}
				if (c.magicDef) {
					c.getActionSender().addSkillXP((damage * GameConstants.MELEE_EXP_RATE / 2),
							1);
					c.getActionSender().refreshSkill(1);
				}
				c.getActionSender().addSkillXP(
						(c.MAGIC_SPELLS[c.oldSpellId][7] + damage
								* GameConstants.MAGIC_EXP_RATE), 6);
				c.getActionSender().addSkillXP(
						(c.MAGIC_SPELLS[c.oldSpellId][7] + damage
								* GameConstants.MAGIC_EXP_RATE / 3), 3);
				c.getActionSender().refreshSkill(3);
				c.getActionSender().refreshSkill(6);
				if (damage > 0) {
					// Pest Control Shit again.
				}
				if (c.getCombat().getEndGfxHeight() == 100 && !magicFailed) { // end
																				// GFX
					MobHandler.npcs[i].gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
					if (Server.npcHandler.getNPCs()[i].attackTimer < 9)
						MobHandler.startAnimation(
								c.getCombat().npcDefenceAnim(i), i);
				} else if (!magicFailed) {
					MobHandler.npcs[i].gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
				}

				if (magicFailed) {
					if (Server.npcHandler.getNPCs()[i].attackTimer < 9) {
						MobHandler.startAnimation(
								c.getCombat().npcDefenceAnim(i), i);
					}
					MobHandler.npcs[i].gfx100(85);
				}
				if (!magicFailed) {
					int freezeDelay = c.getCombat().getFreezeTime();// freeze
					if (freezeDelay > 0 && MobHandler.npcs[i].freezeTimer == 0) {
						MobHandler.npcs[i].freezeTimer = freezeDelay;
					}
					switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int heal = Misc.random(damage / 2);
						if (c.playerLevel[3] + heal >= c.getActionSender().getLevelForXP(
								c.playerXP[3])) {
							c.playerLevel[3] = c.getActionSender().getLevelForXP(
									c.playerXP[3]);
						} else {
							c.playerLevel[3] += heal;
						}
						c.getActionSender().refreshSkill(3);
						break;
					}

				}

				MobHandler.npcs[i].underAttack = true;
				if (c.getCombat().magicMaxHit() != 0) {
					if (!c.multiAttacking) {
						MobHandler.npcs[i].hitDiff = damage;
						MobHandler.npcs[i].HP -= damage;
						MobHandler.npcs[i].hitUpdateRequired = true;
						c.totalDamageDealt += damage;
					}
				}

				c.multiAttacking = false;
				c.killingNpcIndex = c.oldNpcIndex;
				MobHandler.npcs[i].updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
				SoundManager.sendSound(c, c.oldSpellId, SoundType.MAGIC_COMBAT);
				c.oldSpellId = 0;
			}
		}
		if (c.bowSpecShot <= 0) {
			c.oldNpcIndex = 0;
			c.projectileStage = 0;
			c.doubleHit = false;
			c.lastWeaponUsed = 0;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot >= 2) {
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot == 1) {
			c.getCombat().fireProjectileNpc();
			c.hitDelay = 2;
			c.bowSpecShot = 0;
		}

	}

	public static void attackNpc(Player c, int i) {
		if (MobHandler.npcs[i] != null && c != null) {
			if (MobHandler.npcs[i].isDead || MobHandler.npcs[i].maxHP <= 0) {
				c.usingMagic = false;
				c.faceUpdate(0);
				c.npcIndex = 0;
				return;
			}
			if (c.respawnTimer > 0) {
				c.npcIndex = 0;
				return;
			}
			if (MobHandler.npcs[i].underAttackBy > 0
					&& MobHandler.npcs[i].underAttackBy != c.playerId
					&& !MobHandler.npcs[i].inMulti()) {
				c.npcIndex = 0;
				c.sendMessage("This monster is already in combat.");
				return;
			}
			if ((c.underAttackBy > 0 || c.underAttackBy2 > 0)
					&& c.underAttackBy2 != i && !c.inMulti()) {
				c.getCombat().resetPlayerAttack();
				c.sendMessage("I am already under attack.");
				return;
			}
			if (!c.getCombat().goodSlayer(i)) {
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (MobHandler.npcs[i].spawnedBy != c.playerId
					&& MobHandler.npcs[i].spawnedBy > 0) {
				c.getCombat().resetPlayerAttack();
				c.sendMessage("This monster was not spawned for you.");
				return;
			}
			if (c.getX() == MobHandler.npcs[i].getX()
					&& c.getY() == MobHandler.npcs[i].getY()) {
				c.getActionSender().walkTo(0, 1);
			}
			c.followId2 = i;
			c.followId = 0;
			if (c.attackTimer <= 0) {
				c.usingBow = false;
				c.usingArrows = false;
				c.usingOtherRangeWeapons = false;
				c.usingCross = c.playerEquipment[c.playerWeapon] == 9185;
				c.bonusAttack = 0;
				c.rangeItemUsed = 0;
				c.projectileStage = 0;
				if (c.autocasting) {
					c.spellId = c.autocastId;
					c.sendMessage("Spell ID: " + c.spellId + "");
					c.usingMagic = true;
				}
				if (c.spellId > 0) {
					c.usingMagic = true;
				}
				c.attackTimer = c.getCombat().getAttackDelay(
						c.getEquipment()
								.getItemName(c.playerEquipment[c.playerWeapon])
								.toLowerCase());
				c.specAccuracy = 1.0;
				c.specDamage = 1.0;
				if (!c.usingMagic) {
					for (int bowId : c.BOWS) {
						if (c.playerEquipment[c.playerWeapon] == bowId) {
							c.usingBow = true;
							for (int arrowId : c.ARROWS) {
								if (c.playerEquipment[c.playerArrows] == arrowId) {
									c.usingArrows = true;
								}
							}
						}
					}

					for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
						if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
							c.usingOtherRangeWeapons = true;
						}
					}
				}
				if ((!c.goodDistance(c.getX(), c.getY(),
						MobHandler.npcs[i].getX(), MobHandler.npcs[i].getY(), 2) && (c
						.getCombat().usingHally()
						&& !c.usingOtherRangeWeapons
						&& !c.usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(),
								MobHandler.npcs[i].getX(),
								MobHandler.npcs[i].getY(), 4) && (c.usingOtherRangeWeapons
								&& !c.usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(),
								MobHandler.npcs[i].getX(),
								MobHandler.npcs[i].getY(), 1) && (!c.usingOtherRangeWeapons
								&& !c.getCombat().usingHally() && !c.usingBow && !c.usingMagic))
						|| ((!c.goodDistance(c.getX(), c.getY(),
								MobHandler.npcs[i].getX(),
								MobHandler.npcs[i].getY(), 8) && (c.usingBow || c.usingMagic)))) {
					c.attackTimer = 2;
					return;
				}
				SoundManager.sendSound(c, c.spellId, SoundType.CAST_SOUND);
				if (!c.usingCross
						&& !c.usingArrows
						&& c.usingBow
						&& (c.playerEquipment[c.playerWeapon] < 4212 || c.playerEquipment[c.playerWeapon] > 4223 && !c.usingMagic && c.playerEquipment[c.playerWeapon] != 14614)) {
					c.sendMessage("You have run out of arrows!");
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}
				if (c.getCombat().correctBowAndArrows() < c.playerEquipment[c.playerArrows]
						&& Configuration.CORRECT_ARROWS
						&& c.usingBow
						&& !c.getCombat().usingCrystalBow()
						&& c.playerEquipment[c.playerWeapon] != 9185) {
					c.sendMessage("You can't use "
							+ c.getEquipment()
									.getItemName(
											c.playerEquipment[c.playerArrows])
									.toLowerCase()
							+ "s with a "
							+ c.getEquipment()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase() + ".");
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}

				if (c.playerEquipment[c.playerWeapon] == 9185
						&& !c.getCombat().properBolts()) {
					c.sendMessage("You must use bolts with a crossbow.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.usingBow
						|| c.castingMagic
						|| c.usingOtherRangeWeapons
						|| (c.getCombat().usingHally() && c.goodDistance(
								c.getX(), c.getY(), MobHandler.npcs[i].getX(),
								MobHandler.npcs[i].getY(), 2))) {
					c.stopMovement();
				}
				if (!c.getCombat().checkMagicReqs(c.spellId)) {
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}
				c.faceUpdate(i);
				MobHandler.npcs[i].underAttackBy = c.playerId;
				MobHandler.npcs[i].lastDamageTaken = System.currentTimeMillis();
				if (c.usingSpecial && !c.usingMagic) {
					if (c.getCombat().checkSpecAmount(
							c.playerEquipment[c.playerWeapon])) {
						c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
						c.lastArrowUsed = c.playerEquipment[c.playerArrows];
						c.getCombat().activateSpecial(
								c.playerEquipment[c.playerWeapon], i);

						return;
					} else {
						c.sendMessage("You don't have the required special energy to use this attack.");
						c.usingSpecial = false;
						c.getEquipment().updateSpecialBar();
						c.npcIndex = 0;
						return;
					}
				}
				c.specMaxHitIncrease = 0;
				if (c.playerLevel[3] > 0 && !c.isDead
						&& MobHandler.npcs[i].maxHP > 0) {
					if (!c.usingMagic) {
						c.startAnimation(c
								.getCombat()
								.getWepAnim(
										c.getEquipment()
												.getItemName(
														c.playerEquipment[c.playerWeapon])
												.toLowerCase()));

						if (Server.npcHandler.getNPCs()[i].attackTimer < 9) {
							MobHandler.startAnimation(c.getCombat()
									.npcDefenceAnim(i), i);
						}
					} else {
						c.startAnimation(c.MAGIC_SPELLS[c.spellId][2]);
					}
				}
				c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
				c.lastArrowUsed = c.playerEquipment[c.playerArrows];

				if (!c.usingBow && !c.usingMagic && !c.usingOtherRangeWeapons) { // melee
																					// hit
																					// delay
					c.followId2 = MobHandler.npcs[i].npcId;
					c.getActionSender().followNpc();
					System.out.println("3");
					c.hitDelay = c.getCombat().getHitDelay(
							i,
							c.getEquipment()
							.getItemName(c.playerEquipment[c.playerWeapon])
							.toLowerCase());
					c.projectileStage = 0;
					c.oldNpcIndex = i;
				}

				if (c.usingBow && !c.usingOtherRangeWeapons && !c.usingMagic
						|| c.usingCross) { // range hit delay
					if (c.usingCross)
						c.usingBow = true;
					if (c.fightMode == 2)
						c.attackTimer--;
					c.followId2 = MobHandler.npcs[i].npcId;
					c.getActionSender().followNpc();
					c.lastArrowUsed = c.playerEquipment[c.playerArrows];
					c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
					c.gfx100(c.getCombat().getRangeStartGFX());
					System.out.println("4");
					c.hitDelay = c.getCombat().getHitDelay(
							i,
							c.getEquipment()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.playerEquipment[c.playerWeapon] >= 4212
							&& c.playerEquipment[c.playerWeapon] <= 4223) {
						c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
						c.crystalBowArrowCount++;
						c.lastArrowUsed = 0;
					} else {
						c.rangeItemUsed = c.playerEquipment[c.playerArrows];
						c.getItems().deleteArrow();
						if (c.playerEquipment[3] == 11235) {
							c.getItems().deleteArrow();
						}
					}
					c.getCombat().fireProjectileNpc();
				}

				if (c.usingOtherRangeWeapons && !c.usingMagic && !c.usingBow) {
					c.followId2 = MobHandler.npcs[i].npcId;
					c.getActionSender().followNpc();
					c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					c.getEquipment().deleteEquipment();
					c.gfx100(c.getCombat().getRangeStartGFX());
					c.lastArrowUsed = 0;
					System.out.println("1");
					c.hitDelay = c.getCombat().getHitDelay(
							i,
							c.getEquipment()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.fightMode == 2)
						c.attackTimer--;
					c.getCombat().fireProjectileNpc();
				}

				if (c.usingMagic) { // magic hit delay
					int pX = c.getX();
					int pY = c.getY();
					int nX = MobHandler.npcs[i].getX();
					int nY = MobHandler.npcs[i].getY();
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					c.castingMagic = true;
					c.projectileStage = 2;
					c.stopMovement();
					if (c.MAGIC_SPELLS[c.spellId][3] > 0) {
						if (c.getCombat().getStartGfxHeight() == 100) {
							c.gfx100(c.MAGIC_SPELLS[c.spellId][3]);
						} else {
							c.gfx0(c.MAGIC_SPELLS[c.spellId][3]);
						}
					}
					if (c.MAGIC_SPELLS[c.spellId][4] > 0) {
						c.getActionSender().createPlayersProjectile(pX, pY, offX, offY,
								50, 78, c.MAGIC_SPELLS[c.spellId][4],
								c.getCombat().getStartHeight(),
								c.getCombat().getEndHeight(), i + 1, 50);
					}
					System.out.println("2");
					c.hitDelay = c.getCombat().getHitDelay(
							i,
							c.getEquipment()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.oldNpcIndex = i;
					c.oldSpellId = c.spellId;

					c.spellId = 0;
					if (!c.autocasting)
						c.npcIndex = 0;
				}

				if (c.usingBow && Configuration.CRYSTAL_BOW_DEGRADES) { // crystal bow
																	// degrading
					if (c.playerEquipment[c.playerWeapon] == 4212) { // new
																		// crystal
																		// bow
																		// becomes
																		// full
																		// bow
																		// on
																		// the
																		// first
																		// shot
						c.getEquipment().wearItem(4214, 1, 3);
					}

					if (c.crystalBowArrowCount >= 250) {
						switch (c.playerEquipment[c.playerWeapon]) {

						case 4223: // 1/10 bow
							c.getEquipment().wearItem(-1, 1, 3);
							c.sendMessage("Your crystal bow has fully degraded.");
							if (!c.getInventory().addItem(4207, 1)) {
								Server.itemHandler.createGroundItem(c, 4207,
										c.getX(), c.getY(), 1, c.getId());
							}
							c.crystalBowArrowCount = 0;
							break;

						default:
							c.getEquipment().wearItem(
									++c.playerEquipment[c.playerWeapon], 1, 3);
							c.sendMessage("Your crystal bow degrades.");
							c.crystalBowArrowCount = 0;
							break;
						}
					}
				}
			}
		}
	}
}
