package core.game.model.entity.player.combat;

import core.Config;
import core.Server;
import core.game.GameConstants;
import core.game.event.task.Task;
import core.game.event.task.TaskHandler;
import core.game.model.entity.Hit;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.entity.player.combat.impl.ranged.RangeData;
import core.game.sound.SoundManager;
import core.game.sound.SoundManager.SoundType;
import core.game.util.Misc;

public class PlayerVsPlayer {

	public static void applyPlayerHit(final Player c, final int i) {
		c.stopPlayerSkill = false;
			c.getCombat().applyPlayerMeleeDamage(i, 1,
					Misc.random(c.getCombat().calculateMeleeMaxHit()));
			if (c.doubleHit) {
				if (!c.oldSpec) {
					c.getCombat().applyPlayerMeleeDamage(i, 2,
							Misc.random(c.getCombat().calculateMeleeMaxHit()));
				} else {			
					
					TaskHandler.submit(new Task(1, false) {

						@Override
						public void execute() {
							c.getCombat().applyPlayerMeleeDamage(
									i,
									2,
									Misc.random(c.getCombat()
											.calculateMeleeMaxHit()));
							this.cancel();							
						}						
					});					
				}
			}
	}

	public static void applyPlayerMeleeDamage(Player c, int i, int damageMask,
			int damage) {
		c.previousDamage = damage;

		Player o = PlayerHandler.players[i];
		if (o == null) {
			return;
		}
		boolean veracsEffect = false;
		boolean guthansEffect = false;
		if (c.getEquipment().isWearingVeracs(c)) {
			if (Misc.random(4) == 1) {
				veracsEffect = true;
			}
		}
		if (c.getEquipment().isWearingGuthans(c)) {
			if (Misc.random(4) == 1) {
				guthansEffect = true;
			}
		}
		if (c.getEquipment().tentacleWhip() && o.poisonDamage == 0
				&& Misc.random(4) == 1) {
			o.getPA().appendPoison(4);
			c.sendMessage("Your poisonous Abyssal tentacle poisons "
					+ o.playerName + ".");

		}
			if (damageMask == 1) {
				damage = c.delayedDamage;
				c.delayedDamage = 0;
			} else {
				damage = c.delayedDamage2;
				c.delayedDamage2 = 0;
			}
		if (Misc.random(o.getCombat().calculateMeleeDefence()) > Misc.random(c
				.getCombat().calculateMeleeAttack()) && !veracsEffect) {
			damage = 0;
			c.bonusAttack = 0;
		} else if (c.playerEquipment[c.playerWeapon] == 5698
				&& o.poisonDamage <= 0 && Misc.random(3) == 1) {
			o.getPA().appendPoison(5);
			c.bonusAttack += damage / 3;
		} else {
			c.bonusAttack += damage / 3;
		}
		if (o.prayerActive[18]
				&& System.currentTimeMillis() - o.protMeleeDelay > 1500
				&& !veracsEffect) { // if prayer active reduce damage by 40%
			damage = (int) damage * 60 / 100;
		}
		if (c.maxNextHit) {
			damage = c.getCombat().calculateMeleeMaxHit();
		}
		if (damage > 0 && guthansEffect) {
			c.playerLevel[3] += damage;
			if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3]))
				c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
			c.getPA().refreshSkill(3);
			o.gfx0(398);
		}
		if (c.ssSpec && damageMask == 2) {
			damage = 5 + Misc.random(11);
			c.ssSpec = false;
		}
		if (PlayerHandler.players[i].playerLevel[3] - damage < 0) {
			damage = PlayerHandler.players[i].playerLevel[3];
		}
		if (o.vengOn && damage > 0)
			c.getCombat().appendVengeance(i, damage);
		if (damage > 0)
			c.getCombat().applyRecoil(damage, i);
		switch (c.specEffect) {
		case 1: // dragon scimmy special
			if (damage > 0) {
				if (o.prayerActive[16] || o.prayerActive[17]
						|| o.prayerActive[18]) {
					o.headIcon = -1;
					o.getPA().sendFrame36(c.PRAYER_GLOW[16], 0);
					o.getPA().sendFrame36(c.PRAYER_GLOW[17], 0);
					o.getPA().sendFrame36(c.PRAYER_GLOW[18], 0);
				}
				o.sendMessage("You have been injured!");
				o.stopPrayerDelay = System.currentTimeMillis();
				o.prayerActive[16] = false;
				o.prayerActive[17] = false;
				o.prayerActive[18] = false;
				o.getPA().requestUpdates();
			}
			break;
		case 2:
			if (damage > 0) {
				if (o.freezeTimer <= 0)
					o.freezeTimer = 30;
				o.gfx0(369);
				o.sendMessage("You have been frozen.");
				o.frozenBy = c.playerId;
				o.stopMovement();
				c.sendMessage("You freeze your enemy.");
			}
			break;
		case 3:
			if (damage > 0) {
				o.playerLevel[1] -= damage;
				o.sendMessage("You feel weak.");
				if (o.playerLevel[1] < 1)
					o.playerLevel[1] = 1;
				o.getPA().refreshSkill(1);
			}
			break;
		case 4:
			if (damage > 0) {
				if (c.playerLevel[3] + damage > c.getLevelForXP(c.playerXP[3]))
					if (c.playerLevel[3] > c.getLevelForXP(c.playerXP[3]))
						;
					else
						c.playerLevel[3] = c.getLevelForXP(c.playerXP[3]);
				else
					c.playerLevel[3] += damage;
				c.getPA().refreshSkill(3);
			}
			break;
		}
		c.specEffect = 0;
		if (c.fightMode == 3) { // check
			c.getPA()
					.addSkillXP((damage * GameConstants.MELEE_EXP_RATE / 3), 0);
			c.getPA()
					.addSkillXP((damage * GameConstants.MELEE_EXP_RATE / 3), 1);
			c.getPA()
					.addSkillXP((damage * GameConstants.MELEE_EXP_RATE / 3), 2);
			c.getPA()
					.addSkillXP((damage * GameConstants.MELEE_EXP_RATE / 3), 3);
			c.getPA().refreshSkill(0);
			c.getPA().refreshSkill(1);
			c.getPA().refreshSkill(2);
			c.getPA().refreshSkill(3);
		} else {
			c.getPA().addSkillXP((damage * GameConstants.MELEE_EXP_RATE),
					c.fightMode);
			c.getPA()
					.addSkillXP((damage * GameConstants.MELEE_EXP_RATE / 3), 3);
			c.getPA().refreshSkill(c.fightMode);
			c.getPA().refreshSkill(3);
		}
		PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
		PlayerHandler.players[i].underAttackBy = c.playerId;
		PlayerHandler.players[i].killerId = c.playerId;
		PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
		if (c.killedBy != PlayerHandler.players[i].playerId)
			c.totalPlayerDamageDealt = 0;
		c.killedBy = PlayerHandler.players[i].playerId;
		c.getCombat().applySmite(i, damage);
		switch (damageMask) {
		case 1:
			PlayerHandler.players[i].dealHit(new Hit(damage));
			PlayerHandler.players[i].damageTaken[c.playerId] += damage;
			c.totalPlayerDamageDealt += damage;
			PlayerHandler.players[i].updateRequired = true;
			o.getPA().refreshSkill(3);
			break;

		case 2:
			PlayerHandler.players[i].dealSecondaryHit(new Hit(damage));
			PlayerHandler.players[i].damageTaken[c.playerId] += damage;
			c.totalPlayerDamageDealt += damage;
			PlayerHandler.players[i].updateRequired = true;
			c.doubleHit = false;
			o.getPA().refreshSkill(3);
			break;
		}
	}

	public static void playerDelayedHit(final Player c, final int i) {
		if (PlayerHandler.players[i] != null) {
			if (PlayerHandler.players[i].isDead || c.isDead
					|| PlayerHandler.players[i].playerLevel[3] <= 0
					|| c.playerLevel[3] <= 0) {
				c.playerIndex = 0;
				return;
			}
			if (PlayerHandler.players[i].respawnTimer > 0) {
				c.faceUpdate(0);
				c.playerIndex = 0;
				return;
			}
			Player o = PlayerHandler.players[i];
			o.getPA().removeAllWindows();
			if (o.playerIndex <= 0 && o.npcIndex <= 0) {
				if (o.autoRet) {
					o.playerIndex = c.playerId;
				}
			}

			if (o.attackTimer <= 3 || o.attackTimer == 0 && o.playerIndex == 0
					&& !c.castingMagic) { // block animation
				o.startAnimation(o.getCombat().getBlockEmote());
				SoundManager.sendSound(o, 0, SoundType.PLAYER_BLOCK);
			}
			if ((Boolean) o.getAttributes().get("isTrading")) {
				o.getContentManager().getTrading().declineTrade();
			}
			if (c.projectileStage == 0 && !c.usingMagic) { // melee hit damage
				c.getCombat().applyPlayerHit(c, i);
			}
			c.getCombat().addCharge();
			if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
				int damage = Misc.random(c.getCombat().rangeMaxHit());
				int damage2 = -1;
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1) {
					damage2 = Misc.random(c.getCombat().rangeMaxHit());
				}
				c.rangeEndGFX = RangeData.getRangeEndGFX(c);

				if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc
						.random(10 + c.getCombat().calculateRangeAttack())
						&& !c.ignoreDefence) {
					damage = 0;
				}
				if (c.playerEquipment[3] == 14614) {
					damage *= 2.00;
				}
				if (c.playerEquipment[3] == 9185
						|| c.playerEquipment[3] == 11785) {
					if (Misc.random(10) == 1) {
						if (damage > 0) {
							c.boltDamage = damage;
							c.getCombat().crossbowSpecial(c, i);
							damage *= c.crossbowDamage;
						}
					}
				}
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1) {
					if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc
							.random(10 + c.getCombat().calculateRangeAttack()))
						damage2 = 0;
				}

				if (c.dbowSpec) {
					o.gfx100(c.lastArrowUsed == 11212 ? 1100 : 1103);
					if (damage < 8)
						damage = 8;
					if (damage2 < 8)
						damage2 = 8;
					c.dbowSpec = false;
				}

				if (o.prayerActive[17]
						&& System.currentTimeMillis() - o.protRangeDelay > 1500) { // if
																					// prayer
																					// active
																					// reduce
																					// damage
																					// by
																					// half
					damage = (int) damage * 60 / 100;
					if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1)
						damage2 = (int) damage2 * 60 / 100;
				}
				if (PlayerHandler.players[i].playerLevel[3] - damage < 0) {
					damage = PlayerHandler.players[i].playerLevel[3];
				}
				if (PlayerHandler.players[i].playerLevel[3] - damage - damage2 < 0) {
					damage2 = PlayerHandler.players[i].playerLevel[3] - damage;
				}
				if (damage < 0)
					damage = 0;
				if (damage2 < 0 && damage2 != -1)
					damage2 = 0;
				if (o.vengOn) {
					c.getCombat().appendVengeance(i, damage);
					c.getCombat().appendVengeance(i, damage2);
				}
				if (damage > 0)
					c.getCombat().applyRecoil(damage, i);
				if (damage2 > 0)
					c.getCombat().applyRecoil(damage2, i);
				if (c.fightMode == 3) {
					c.getPA().addSkillXP(
							(damage * GameConstants.RANGE_EXP_RATE / 3), 4);
					c.getPA().addSkillXP(
							(damage * GameConstants.RANGE_EXP_RATE / 3), 1);
					c.getPA().addSkillXP(
							(damage * GameConstants.RANGE_EXP_RATE / 3), 3);
					c.getPA().refreshSkill(1);
					c.getPA().refreshSkill(3);
					c.getPA().refreshSkill(4);
				} else {
					c.getPA().addSkillXP(
							(damage * GameConstants.RANGE_EXP_RATE), 4);
					c.getPA().addSkillXP(
							(damage * GameConstants.RANGE_EXP_RATE / 3), 3);
					c.getPA().refreshSkill(3);
					c.getPA().refreshSkill(4);
				}
				boolean dropArrows = true;

				for (int noArrowId : c.NO_ARROW_DROP) {
					if (c.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					c.getItems().dropArrowPlayer();
				}
				if (c.rangeEndGFX > 0
						&& !c.getCombat().usingBolts(c.lastArrowUsed)) {
					if (c.rangeEndGFXHeight) {
						o.gfx100(c.rangeEndGFX);
					} else {
						o.gfx0(c.rangeEndGFX);
					}
				}
				PlayerHandler.players[i].underAttackBy = c.playerId;
				PlayerHandler.players[i].logoutDelay = System
						.currentTimeMillis();
				PlayerHandler.players[i].singleCombatDelay = System
						.currentTimeMillis();
				PlayerHandler.players[i].killerId = c.playerId;
				PlayerHandler.players[i].dealHit(new Hit(damage));
				PlayerHandler.players[i].damageTaken[c.playerId] += damage;
				c.killedBy = PlayerHandler.players[i].playerId;
				PlayerHandler.players[i].dealHit(new Hit(damage));
				c.ignoreDefence = false;
				if (damage2 != -1) {
					PlayerHandler.players[i].dealSecondaryHit(new Hit(damage2));
					PlayerHandler.players[i].damageTaken[c.playerId] += damage2;
				}
				o.getPA().refreshSkill(3);
				PlayerHandler.players[i].updateRequired = true;
				c.getCombat().applySmite(i, damage);
				if (damage2 != -1)
					c.getCombat().applySmite(i, damage2);

			} else if (c.projectileStage > 0) { // magic hit damageno0b
				int damage = 0;
				if (c.spellSwap) {
					c.spellSwap = false;
					c.setSidebarInterface(6, 16640);
					c.playerMagicBook = 2;
					c.gfx0(-1);
				}
				if (c.fullVoidMage()
						&& c.playerEquipment[c.playerWeapon] == 8841) {
					damage = Misc.random(c.getCombat().magicMaxHit() + 13);
				} else {
					damage = Misc.random(c.getCombat().magicMaxHit());
				}
				if (c.getCombat().godSpells()) {
					if (System.currentTimeMillis() - c.godSpellDelay < GameConstants.GOD_SPELL_CHARGE) {
						damage += 10;
					}
				}
				if (c.magicFailed)
					damage = 0;

				if (o.prayerActive[16]
						&& System.currentTimeMillis() - o.protMageDelay > 1500) { // if
																					// prayer
																					// active
																					// reduce
																					// damage
																					// by
																					// half
					damage = (int) damage * 60 / 100;
				}
				if (PlayerHandler.players[i].playerLevel[3] - damage < 0) {
					damage = PlayerHandler.players[i].playerLevel[3];
				}
				if (o.vengOn)
					c.getCombat().appendVengeance(i, damage);
				if (damage > 0)
					c.getCombat().applyRecoil(damage, i);
				if (c.magicDef) {
					c.getPA().addSkillXP(
							(damage * GameConstants.MELEE_EXP_RATE / 3), 1);
					c.getPA().refreshSkill(1);
				}
				c.getPA().addSkillXP(
						(c.MAGIC_SPELLS[c.oldSpellId][7] + damage
								* GameConstants.MAGIC_EXP_RATE), 6);
				c.getPA().addSkillXP(
						(c.MAGIC_SPELLS[c.oldSpellId][7] + damage
								* GameConstants.MAGIC_EXP_RATE / 3), 3);
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);

				if (c.getCombat().getEndGfxHeight() == 100 && !c.magicFailed) { // end
																				// GFX
					PlayerHandler.players[i]
							.gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
				} else if (!c.magicFailed) {
					PlayerHandler.players[i]
							.gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
				} else if (c.magicFailed) {
					PlayerHandler.players[i].gfx100(85);
				}

				if (!c.magicFailed) {
					if (System.currentTimeMillis()
							- PlayerHandler.players[i].reduceStat > 35000) {
						PlayerHandler.players[i].reduceStat = System
								.currentTimeMillis();
						switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
						case 12987:
						case 13011:
						case 12999:
						case 13023:
							PlayerHandler.players[i].playerLevel[0] -= ((o
									.getPA()
									.getLevelForXP(
											PlayerHandler.players[i].playerXP[0]) * 10) / 100);
							break;
						}
					}

					switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 12445: // teleblock
						if (System.currentTimeMillis() - o.teleBlockDelay > o.teleBlockLength) {
							o.teleBlockDelay = System.currentTimeMillis();
							o.sendMessage("You have been teleblocked.");
							if (o.prayerActive[16]
									&& System.currentTimeMillis()
											- o.protMageDelay > 1500)
								o.teleBlockLength = 150000;
							else
								o.teleBlockLength = 300000;
						}
						break;

					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int heal = (int) (damage / 4);
						if (c.playerLevel[3] + heal > c.getPA().getLevelForXP(
								c.playerXP[3])) {
							c.playerLevel[3] = c.getPA().getLevelForXP(
									c.playerXP[3]);
						} else {
							c.playerLevel[3] += heal;
						}
						c.getPA().refreshSkill(3);
						break;

					case 1153:
						PlayerHandler.players[i].playerLevel[0] -= ((o.getPA()
								.getLevelForXP(
										PlayerHandler.players[i].playerXP[0]) * 5) / 100);
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System
								.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;

					case 1157:
						PlayerHandler.players[i].playerLevel[2] -= ((o.getPA()
								.getLevelForXP(
										PlayerHandler.players[i].playerXP[2]) * 5) / 100);
						o.sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System
								.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1161:
						PlayerHandler.players[i].playerLevel[1] -= ((o.getPA()
								.getLevelForXP(
										PlayerHandler.players[i].playerXP[1]) * 5) / 100);
						o.sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System
								.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1542:
						PlayerHandler.players[i].playerLevel[1] -= ((o.getPA()
								.getLevelForXP(
										PlayerHandler.players[i].playerXP[1]) * 10) / 100);
						o.sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System
								.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1543:
						PlayerHandler.players[i].playerLevel[2] -= ((o.getPA()
								.getLevelForXP(
										PlayerHandler.players[i].playerXP[2]) * 10) / 100);
						o.sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System
								.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1562:
						PlayerHandler.players[i].playerLevel[0] -= ((o.getPA()
								.getLevelForXP(
										PlayerHandler.players[i].playerXP[0]) * 10) / 100);
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System
								.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;
					}
				}

				PlayerHandler.players[i].logoutDelay = System
						.currentTimeMillis();
				PlayerHandler.players[i].underAttackBy = c.playerId;
				PlayerHandler.players[i].killerId = c.playerId;
				PlayerHandler.players[i].singleCombatDelay = System
						.currentTimeMillis();
				if (c.getCombat().magicMaxHit() != 0) {
					PlayerHandler.players[i].dealHit(new Hit(damage));
					PlayerHandler.players[i].damageTaken[c.playerId] += damage;
					c.totalPlayerDamageDealt += damage;
				}
				c.getCombat().applySmite(i, damage);
				c.killedBy = PlayerHandler.players[i].playerId;
				o.getPA().refreshSkill(3);
				PlayerHandler.players[i].updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
				if (o.inMulti() && c.getCombat().multis()) {
					c.barrageCount = 0;
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							if (j == o.playerId)
								continue;
							if (c.barrageCount >= 9)
								break;
							if (o.goodDistance(o.getX(), o.getY(),
									PlayerHandler.players[j].getX(),
									PlayerHandler.players[j].getY(), 1))
								c.getCombat().appendMultiBarrage(j,
										c.magicFailed);
						}
					}
				}
				SoundManager.sendSound(c, c.oldSpellId, SoundType.MAGIC_COMBAT);
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);
				c.oldSpellId = 0;
			}
		}
		c.getPA().requestUpdates();
		if (c.bowSpecShot <= 0) {
			c.oldPlayerIndex = 0;
			c.projectileStage = 0;
			c.lastWeaponUsed = 0;
			c.doubleHit = false;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot != 0) {
			c.bowSpecShot = 0;
		}
	}

	public static void attackPlayer(Player c, int i) {
		if (PlayerHandler.players[i] != null) {
			if (PlayerHandler.players[i].isDead) {
				c.getCombat().resetPlayerAttack();
				return;
			}

			if (c.playerEquipment[c.playerWeapon] == 9703) {
				c.sendMessage("You cannot attack players with training sword.");
				return;
			}
			if (c.respawnTimer > 0 || PlayerHandler.players[i].respawnTimer > 0) {
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (!c.getCombat().checkReqs()) {
				return;
			}
			boolean sameSpot = c.absX == PlayerHandler.players[i].getX()
					&& c.absY == PlayerHandler.players[i].getY();
			if (!c.goodDistance(PlayerHandler.players[i].getX(),
					PlayerHandler.players[i].getY(), c.getX(), c.getY(), 25)
					&& !sameSpot) {
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (PlayerHandler.players[i].respawnTimer > 0) {
				PlayerHandler.players[i].playerIndex = 0;
				c.getCombat().resetPlayerAttack();
				return;
			}

			if (PlayerHandler.players[i].heightLevel != c.heightLevel) {
				c.getCombat().resetPlayerAttack();
				return;
			}
			c.followId = i;
			c.followId2 = 0;
			if (c.attackTimer <= 0) {
				c.usingBow = false;
				c.specEffect = 0;
				c.usingRangeWeapon = false;
				c.rangeItemUsed = 0;
				c.usingBow = false;
				c.usingArrows = false;
				c.usingOtherRangeWeapons = false;
				c.usingCross = c.playerEquipment[c.playerWeapon] == 9185
						|| c.playerEquipment[c.playerWeapon] == 11785;
				c.projectileStage = 0;

				if (c.absX == PlayerHandler.players[i].absX
						&& c.absY == PlayerHandler.players[i].absY) {
					if (c.freezeTimer > 0) {
						c.getCombat().resetPlayerAttack();
						return;
					}
					c.followId = i;
					c.attackTimer = 0;
					return;
				}
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
				if (c.autocasting) {
					c.spellId = c.autocastId;
					c.usingMagic = true;
					SoundManager.sendSound(c, c.spellId, SoundType.CAST_SOUND);
				}
				if (c.spellId > 0) {
					c.usingMagic = true;
					SoundManager.sendSound(c, c.spellId, SoundType.CAST_SOUND);
				}
				c.attackTimer = c.getCombat().getAttackDelay(
						c.getEquipment()
								.getItemName(c.playerEquipment[c.playerWeapon])
								.toLowerCase());

				if (c.duelRule[9]) {
					boolean canUseWeapon = false;
					for (int funWeapon : Config.FUN_WEAPONS) {
						if (c.playerEquipment[c.playerWeapon] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						c.sendMessage("You can only use fun weapons in this duel!");
						c.getCombat().resetPlayerAttack();
						return;
					}
				}
				if (c.duelRule[2] && (c.usingBow || c.usingOtherRangeWeapons)) {
					c.sendMessage("Range has been disabled in this duel!");
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.duelRule[3]
						&& (!c.usingBow && !c.usingOtherRangeWeapons && !c.usingMagic)) {
					c.sendMessage("Melee has been disabled in this duel!");
					c.getCombat().resetPlayerAttack();
					return;
				}

				if (c.duelRule[4] && c.usingMagic) {
					c.sendMessage("Magic has been disabled in this duel!");
					c.getCombat().resetPlayerAttack();
					return;
				}

				if ((!c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[i].getX(),
						PlayerHandler.players[i].getY(), 4) && (c.usingOtherRangeWeapons
						&& !c.usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(),
								PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), 2) && (!c.usingOtherRangeWeapons
								&& c.getCombat().usingHally() && !c.usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(),
								PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), c.getCombat()
										.getRequiredDistance()) && (!c.usingOtherRangeWeapons
								&& !c.getCombat().usingHally() && !c.usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(),
								PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), 10) && (c.usingBow || c.usingMagic))) {
					c.attackTimer = 1;
					if (!c.usingBow && !c.usingMagic
							&& !c.usingOtherRangeWeapons && c.freezeTimer > 0)
						c.getCombat().resetPlayerAttack();
					return;
				}

				if (!c.usingCross
						&& !c.usingArrows
						&& c.usingBow
						&& (c.playerEquipment[c.playerWeapon] < 4212 || c.playerEquipment[c.playerWeapon] > 4223 && !c.usingMagic && c.playerEquipment[c.playerWeapon] != 14614)
						&& !c.usingMagic) {
					c.sendMessage("You have run out of arrows!");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.getCombat().correctBowAndArrows() < c.playerEquipment[c.playerArrows]
						&& Config.CORRECT_ARROWS
						&& c.usingBow
						&& !c.getCombat().usingCrystalBow()
						&& c.playerEquipment[c.playerWeapon] != 9185
						&& c.playerEquipment[c.playerWeapon] != 11785
						&& !c.usingMagic) {
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
					c.getCombat().resetPlayerAttack();
					return;
				}
				if (c.playerEquipment[c.playerWeapon] == 9185
						|| c.playerEquipment[c.playerWeapon] == 11785
						&& !c.getCombat().properBolts() && !c.usingMagic) {
					c.sendMessage("You must use bolts with a crossbow.");
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}

				if (c.usingBow || c.usingMagic || c.usingOtherRangeWeapons
						|| c.getCombat().usingHally()) {
					c.stopMovement();
				}

				if (!c.getCombat().checkMagicReqs(c.spellId)) {
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
					return;
				}

				c.faceUpdate(i + 32768);
				if (c.duelStatus != 5) {
					if (!c.attackedPlayers.contains(c.playerIndex)
							&& !PlayerHandler.players[c.playerIndex].attackedPlayers
									.contains(c.playerId)) {
						c.attackedPlayers.add(c.playerIndex);
						c.getAttributes().put("isSkulled", Boolean.TRUE);
						c.skullTimer = GameConstants.SKULL_TIMER;
						c.headIconPk = 0;
						c.getPA().requestUpdates();
					}
				}

				c.specAccuracy = 1.0;
				c.specDamage = 1.0;
				c.delayedDamage = c.delayedDamage2 = 0;
				if (c.usingSpecial && !c.usingMagic) {
					if (c.duelRule[10] && c.duelStatus == 5) {
						c.sendMessage("Special attacks have been disabled during this duel!");
						c.usingSpecial = false;
						c.getEquipment().updateSpecialBar();
						c.getCombat().resetPlayerAttack();
						return;
					}
					if (c.getCombat().checkSpecAmount(
							c.playerEquipment[c.playerWeapon])) {
						c.lastArrowUsed = c.playerEquipment[c.playerArrows];
						c.getCombat().activateSpecial(
								c.playerEquipment[c.playerWeapon], i);
						c.followId = c.playerIndex;
						return;
					} else {
						c.sendMessage("You don't have the required special energy to use this attack.");
						c.usingSpecial = false;
						c.getEquipment().updateSpecialBar();
						c.playerIndex = 0;
						return;
					}
				}
				if (c.playerLevel[3] > 0 && !c.isDead
						&& PlayerHandler.players[i].playerLevel[3] > 0) {
					if (!c.usingMagic) {
						c.startAnimation(c
								.getCombat()
								.getWepAnim(
										c.getEquipment()
												.getItemName(
														c.playerEquipment[c.playerWeapon])
												.toLowerCase()));
						c.mageFollow = false;
					} else {
						c.startAnimation(c.MAGIC_SPELLS[c.spellId][2]);
						c.mageFollow = true;
						c.followId = c.playerIndex;
					}
				}
				PlayerHandler.players[i].underAttackBy = c.playerId;
				PlayerHandler.players[i].logoutDelay = System
						.currentTimeMillis();
				PlayerHandler.players[i].singleCombatDelay = System
						.currentTimeMillis();
				PlayerHandler.players[i].killerId = c.playerId;
				c.lastArrowUsed = 0;
				c.rangeItemUsed = 0;
				if (!c.usingBow && !c.usingMagic && !c.usingOtherRangeWeapons) { // melee
																					// hit
																					// delay
					c.followId = PlayerHandler.players[c.playerIndex].playerId;
					c.getPA().followPlayer();
					SoundManager.sendSound(c,
							c.playerEquipment[c.playerWeapon],
							SoundType.MELEE_COMBAT);
					c.hitDelay = c.getCombat().getHitDelay(
							i,
							c.getEquipment()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.delayedDamage = Misc.random(c.getCombat()
							.calculateMeleeMaxHit());
					c.projectileStage = 0;
					c.oldPlayerIndex = i;
				}

				if (c.usingBow && !c.usingOtherRangeWeapons && !c.usingMagic
						|| c.usingCross) { // range hit delay
					if (c.playerEquipment[c.playerWeapon] >= 4212
							&& c.playerEquipment[c.playerWeapon] <= 4223) {
						c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
						c.crystalBowArrowCount++;
					} else {
						c.rangeItemUsed = c.playerEquipment[c.playerArrows];
						c.getItems().deleteArrow();
					}
					if (c.fightMode == 2)
						c.attackTimer--;
					if (c.usingCross)
						c.usingBow = true;
					c.usingBow = true;
					c.followId = PlayerHandler.players[c.playerIndex].playerId;
					c.getPA().followPlayer();
					c.lastWeaponUsed = c.playerEquipment[c.playerWeapon];
					c.lastArrowUsed = c.playerEquipment[c.playerArrows];
					c.gfx100(c.getCombat().getRangeStartGFX());
					c.hitDelay = c.getCombat().getHitDelay(
							i,
							c.getEquipment()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					c.getCombat().fireProjectilePlayer();
				}

				if (c.usingOtherRangeWeapons) { // knives, darts, etc hit delay
					c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					c.getEquipment().deleteEquipment();
					c.usingRangeWeapon = true;
					c.followId = PlayerHandler.players[c.playerIndex].playerId;
					c.getPA().followPlayer();
					c.gfx100(c.getCombat().getRangeStartGFX());
					if (c.fightMode == 2)
						c.attackTimer--;
					c.hitDelay = c.getCombat().getHitDelay(
							i,
							c.getEquipment()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					c.getCombat().fireProjectilePlayer();
				}

				if (c.usingMagic) { // magic hit delay
					int pX = c.getX();
					int pY = c.getY();
					int nX = PlayerHandler.players[i].getX();
					int nY = PlayerHandler.players[i].getY();
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					c.castingMagic = true;
					c.projectileStage = 2;
					if (c.MAGIC_SPELLS[c.spellId][3] > 0) {
						if (c.getCombat().getStartGfxHeight() == 100) {
							c.gfx100(c.MAGIC_SPELLS[c.spellId][3]);
						} else {
							c.gfx0(c.MAGIC_SPELLS[c.spellId][3]);
						}
					}
					if (c.MAGIC_SPELLS[c.spellId][4] > 0) {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY,
								50, 78, c.MAGIC_SPELLS[c.spellId][4],
								c.getCombat().getStartHeight(),
								c.getCombat().getEndHeight(), -i - 1,
								c.getCombat().getStartDelay());
					}
					if (c.autocastId > 0) {
						c.followId = c.playerIndex;
						c.followDistance = 5;
					}
					SoundManager.sendSound(c, c.spellId, SoundType.CAST_SOUND);
					c.hitDelay = c.getCombat().getHitDelay(
							i,
							c.getEquipment()
									.getItemName(
											c.playerEquipment[c.playerWeapon])
									.toLowerCase());
					c.oldPlayerIndex = i;
					c.oldSpellId = c.spellId;
					c.spellId = 0;
					Player o = PlayerHandler.players[i];
					if (c.MAGIC_SPELLS[c.oldSpellId][0] == 12891 && o.isMoving) {
						// c.sendMessage("Barrage projectile..");
						c.getPA().createPlayersProjectile(pX, pY, offX, offY,
								50, 85, 368, 25, 25, -i - 1,
								c.getCombat().getStartDelay());
					}
					if (Misc.random(o.getCombat().mageDef()) > Misc.random(c
							.getCombat().mageAtk())) {
						c.magicFailed = true;
					} else {
						c.magicFailed = false;
					}
					
					int freezeDelay = c.getCombat().getFreezeTime();// freeze
																	// time
					if (freezeDelay > 0
							&& PlayerHandler.players[i].freezeTimer <= -3
							&& !c.magicFailed) {
						PlayerHandler.players[i].freezeTimer = freezeDelay;
						o.resetWalkingQueue();
						o.sendMessage("You have been frozen.");
						o.frozenBy = c.playerId;
					}
					if (!c.autocasting && c.spellId <= 0)
						c.playerIndex = 0;
				}

				if (c.usingBow && Config.CRYSTAL_BOW_DEGRADES) {
					
					/**
					 * Degrading of the crystal bow.
					 * The new crystal bow becoms a full bow on first shot.
					 */
					if (c.playerEquipment[c.playerWeapon] == 4212) {
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
