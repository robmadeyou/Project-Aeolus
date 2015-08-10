package core.game.model.entity.player.combat.impl.melee;

import core.game.event.task.Task;
import core.game.event.task.TaskHandler;
import core.game.model.entity.Hit;
import core.game.model.entity.Hit.HitType;
import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.util.Misc;

public class MeleeExtras {

	public static void applySmite(Player c, int index, int damage) {
		if (!c.prayerActive[23])
			return;
		if (damage <= 0)
			return;
		if (PlayerHandler.players[index] != null) {
			Player c2 = PlayerHandler.players[index];
			c2.playerLevel[5] -= (int) (damage / 4);
			if (c2.playerLevel[5] <= 0) {
				c2.playerLevel[5] = 0;
				c2.getCombat().resetPrayers();
			}
			c2.getActionSender().refreshSkill(5);
		}
	}

	public static void handleDragonFireShield(final Player c) {
		if (PlayerHandler.players[c.playerIndex].playerLevel[3] <= 0) {
			return;
		}
		if (c.playerIndex > 0 && PlayerHandler.players[c.playerIndex] != null) {
			if (c.dfsCount < 40) {
				c.sendMessage("My shield hasn't finished charging.");
				return;
			}
			final int pX = c.getX();
			final int pY = c.getY();
			final int oX = PlayerHandler.players[c.playerIndex].getX();
			final int oY = PlayerHandler.players[c.playerIndex].getY();
			final int offX = (pY - oY) * -1;
			final int offY = (pX - oX) * -1;
			final int damage = Misc.random(25) + 5;
			c.dfsCount = 0;
			c.startAnimation(6696);
			c.gfx0(1165);
			c.attackTimer += 3;
			
			TaskHandler.submit(new Task(3, false) {

				@Override
				public void execute() {
					c.getActionSender().createPlayersProjectile(pX, pY, offX, offY, 50,
							10, 1166, 25, 27, c.playerIndex - 1, 0);
					this.cancel();
				}
				
				
			});
			
			
			TaskHandler.submit(new Task(3, false) {

				@Override
				public void execute() {
					PlayerHandler.players[c.playerIndex].gfx100(1167);
					PlayerHandler.players[c.playerIndex].playerLevel[3] -= damage;
					if (!c.getHitUpdateRequired()) {
						c.dealHit(new Hit(damage, HitType.NORMAL));
					} else if (!c.getHitUpdateRequired2()) {
						c.dealSecondaryHit(new Hit(damage, HitType.NORMAL));
						c.setHitUpdateRequired2(true);
					}
					PlayerHandler.players[c.playerIndex].updateRequired = true;					
				}
				
				
				
			});
			
		}
	}

	public static void handleDragonFireShieldNPC(final Player c) {
		if (MobHandler.npcs[c.npcIndex].HP <= 0) {
			return;
		}
		if (c.npcIndex > 0 && MobHandler.npcs[c.npcIndex] != null) {
			if (c.dfsCount < 40) {
				c.sendMessage("My shield hasn't finished charging.");
				return;
			}
			if (MobHandler.npcs[c.npcIndex].HP <= 1) {
				return;
			}
			final int pX = c.getX();
			final int pY = c.getY();
			final int nX = MobHandler.npcs[c.npcIndex].getX();
			final int nY = MobHandler.npcs[c.npcIndex].getY();
			final int offX = (pY - nY) * -1;
			final int offY = (pX - nX) * -1;
			final int damage = Misc.random(25) + 5;
			c.dfsCount = 0;
			c.startAnimation(6696);
			c.gfx0(1165);
			c.attackTimer += 3;
			
			TaskHandler.submit(new Task(3, false) {

				@Override
				public void execute() {
					c.getActionSender().createPlayersProjectile(pX, pY, offX, offY, 50,
							10, 1166, 25, 27, c.npcIndex + 1, 0);
					this.cancel();					
				}		
				
			});
			
			TaskHandler.submit(new Task(3, false) {

				@Override
				public void execute() {
					MobHandler.npcs[c.npcIndex].gfx100(1167);
					MobHandler.npcs[c.npcIndex].handleHitMask(damage);
					MobHandler.npcs[c.npcIndex].HP -= damage;
					this.cancel();
				}				
			});			
		}
	}

	public static void addCharge(Player c) {
		if (c.playerEquipment[c.playerShield] != 11283) {
			return;
		}
		c.dfsCount++;
		if (c.dfsCount >= 40) {
			c.dfsCount = 40;
		}
		if (c.dfsCount == 39) {
			c.sendMessage("Your dragon fireshield has finished charging!");
		}
	}

	public static void appendVengeanceNPC(Player c, int otherPlayer, int damage) {
		if (damage <= 0)
			return;
		if (c.npcIndex > 0 && MobHandler.npcs[c.npcIndex] != null) {
			c.forcedChat = "Taste vengeance!";
			c.forcedChatUpdateRequired = true;
			c.updateRequired = true;
			c.vengOn = false;
			if ((MobHandler.npcs[c.npcIndex].HP - damage) > 0) {
				damage = (int) (damage * 0.75);
				if (damage > MobHandler.npcs[c.npcIndex].HP) {
					damage = MobHandler.npcs[c.npcIndex].HP;
				}
				MobHandler.npcs[c.npcIndex].HP -= damage;
				MobHandler.npcs[c.npcIndex].handleHitMask(damage);
			}
		}
		c.updateRequired = true;
	}

	public static void appendVengeance(Player c, int otherPlayer, int damage) {
		if (damage <= 0)
			return;
		Player o = PlayerHandler.players[otherPlayer];
		o.forcedChat = "Taste vengeance!";
		o.forcedChatUpdateRequired = true;
		o.updateRequired = true;
		o.vengOn = false;
		if ((o.playerLevel[3] - damage) > 0) {
			damage = (int) (damage * 0.75);
			if (damage > c.playerLevel[3]) {
				damage = c.playerLevel[3];
			}
			if (!c.getHitUpdateRequired()) {
				c.dealHit(new Hit(damage, HitType.NORMAL));
			} else if (!c.getHitUpdateRequired2()) {
				c.dealSecondaryHit(new Hit(damage, HitType.NORMAL));
			}
			c.playerLevel[3] -= damage;
			c.getActionSender().refreshSkill(3);
		}
		c.updateRequired = true;
	}

	public static void applyRecoilNPC(Player c, int damage, int i) {
		if (damage > 0 && c.playerEquipment[c.playerRing] == 2550) {
			int recDamage = damage / 10 + 1;
			MobHandler.npcs[c.npcIndex].HP -= recDamage;
			MobHandler.npcs[c.npcIndex].handleHitMask(recDamage);
			removeRecoil(c);
			c.recoilHits += damage;
		}
	}

	public static void applyRecoil(Player c, int damage, int i) {
		if (damage > 0
				&& PlayerHandler.players[i].playerEquipment[c.playerRing] == 2550) {
			int recDamage = damage / 10 + 1;
			if (!c.getHitUpdateRequired()) {
				c.dealHit(new Hit(recDamage, HitType.DIESEASE));
			} else if (!c.getHitUpdateRequired2()) {
				c.dealSecondaryHit(new Hit(recDamage, HitType.DIESEASE));
			}
		c.dealHit(new Hit(recDamage, HitType.DIESEASE));
			c.updateRequired = true;
			removeRecoil(c);
			c.recoilHits += damage;
		}
	}

	public static void removeRecoil(Player c) {
		if (c.recoilHits >= 400) {
			c.getEquipment().removeItem(2550, c.playerRing);
			c.getInventory().deleteItem(2550, c.getEquipment().getItemSlot(2550), 1);
			c.sendMessage("Your ring of recoil shaters!");
			c.recoilHits = 0;
		} else {
			c.recoilHits++;
		}
	}

	public static void graniteMaulSpecial(Player c) {
		if (c.playerIndex > 0) {
			Player o = PlayerHandler.players[c.playerIndex];
			if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), c
					.getCombat().getRequiredDistance())) {
				if (c.getCombat().checkReqs()) {
					if (c.getCombat().checkSpecAmount(4153)) {
						boolean hit = Misc.random(c.getCombat()
								.calculateMeleeAttack()) > Misc.random(o
								.getCombat().calculateMeleeDefence());
						int damage = 0;
						if (hit)
							damage = Misc.random(c.getCombat()
									.calculateMeleeMaxHit());
						if (o.prayerActive[18]
								&& System.currentTimeMillis()
										- o.protMeleeDelay > 1500)
							damage *= .6;
						if (o.playerLevel[3] - damage <= 0) {
							damage = o.playerLevel[3];
						}
						if (o.playerLevel[3] > 0) {
							c.startAnimation(1667);
							c.gfx100(340);
							c.getActionSender().sendSound(1082);
							o.dealHit(new Hit(damage));
						}
					}
				}
			}
		} else if (c.npcIndex > 0) {
			int x = MobHandler.npcs[c.npcIndex].absX;
			int y = MobHandler.npcs[c.npcIndex].absY;
			if (c.goodDistance(c.getX(), c.getY(), x, y, 2)) {
				if (c.getCombat().checkReqs()) {
					if (c.getCombat().checkSpecAmount(4153)) {
						int damage = Misc.random(c.getCombat()
								.calculateMeleeMaxHit());
						if (MobHandler.npcs[c.npcIndex].HP - damage < 0) {
							damage = MobHandler.npcs[c.npcIndex].HP;
						}
						if (MobHandler.npcs[c.npcIndex].HP > 0) {
							MobHandler.npcs[c.npcIndex].HP -= damage;
							MobHandler.npcs[c.npcIndex].handleHitMask(damage);
							c.startAnimation(1667);
							c.gfx100(337);
						}
					}
				}
			}
		}
	}
}