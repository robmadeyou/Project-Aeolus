package core.net.packets.incoming;

import core.Configuration;
import core.game.event.task.Task;
import core.game.event.task.TaskHandler;
import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.player.Player;
import core.net.packets.PacketType;

/**
 * Click NPC
 */
public class ClickNPC implements PacketType {
	public static final int ATTACK_NPC = 72, MAGE_NPC = 131, FIRST_CLICK = 155,
			SECOND_CLICK = 17, THIRD_CLICK = 21;

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		c.npcIndex = 0;
		c.npcClickIndex = 0;
		c.playerIndex = 0;
		c.clickNpcType = 0;
		c.getActionSender().resetFollow();
		c.followMobId = c.npcClickIndex;
		c.followDistance = 1;
		c.faceUpdate(c.npcClickIndex);
		c.getActionSender().followNpc();
		c.faceUpdate(c.followPlayerId);

		switch (packetType) {

		/**
		 * Attack npc melee or range
		 **/
		case ATTACK_NPC:
			if (!c.mageAllowed) {
				c.mageAllowed = true;
				c.sendMessage("I can't reach that.");
				break;
			}
			c.npcIndex = c.getInStream().readUnsignedWordA();
			if (MobHandler.npcs[c.npcIndex] == null) {
				c.npcIndex = 0;
				break;
			}
			if (MobHandler.npcs[c.npcIndex].maxHP == 0) {
				c.npcIndex = 0;
				break;
			}
			if (MobHandler.npcs[c.npcIndex] == null) {
				break;
			}
			if (c.autocastId > 0)
				c.autocasting = true;
			if (!c.autocasting && c.spellId > 0) {
				c.spellId = 0;
			}
			c.faceUpdate(c.npcIndex);
			c.usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			c.faceUpdate(c.followPlayerId);
			boolean usingCross = c.playerEquipment[c.playerWeapon] == 9185;
			if (c.playerEquipment[c.playerWeapon] >= 4214
					&& c.playerEquipment[c.playerWeapon] <= 4223)
				usingBow = true;
			for (int bowId : c.BOWS) {
				if (c.playerEquipment[c.playerWeapon] == bowId) {
					usingBow = true;
					for (int arrowId : c.ARROWS) {
						if (c.playerEquipment[c.playerArrows] == arrowId) {
							usingArrows = true;
						}
					}
				}
			}
			for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
				if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
					usingOtherRangeWeapons = true;
				}
			}
			if ((usingBow || c.autocasting)
					&& c.goodDistance(c.getX(), c.getY(),
							MobHandler.npcs[c.npcIndex].getX(),
							MobHandler.npcs[c.npcIndex].getY(), 7)) {
				c.stopMovement();
			}

			if (usingOtherRangeWeapons
					&& c.goodDistance(c.getX(), c.getY(),
							MobHandler.npcs[c.npcIndex].getX(),
							MobHandler.npcs[c.npcIndex].getY(), 4)) {
				c.stopMovement();
			}
			if (!usingCross && !usingArrows && usingBow
					&& c.playerEquipment[c.playerWeapon] < 4212
					&& c.playerEquipment[c.playerWeapon] > 4223 && !usingCross) {
				c.sendMessage("You have run out of arrows!");
				break;
			}
			if (c.getCombat().correctBowAndArrows() < c.playerEquipment[c.playerArrows]
					&& Configuration.CORRECT_ARROWS
					&& usingBow
					&& !c.getCombat().usingCrystalBow()
					&& c.playerEquipment[c.playerWeapon] != 9185) {
				c.sendMessage("You can't use "
						+ c.getEquipment()
								.getItemName(c.playerEquipment[c.playerArrows])
								.toLowerCase()
						+ "s with a "
						+ c.getEquipment()
								.getItemName(c.playerEquipment[c.playerWeapon])
								.toLowerCase() + ".");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (c.playerEquipment[c.playerWeapon] == 9185
					&& !c.getCombat().properBolts()) {
				c.sendMessage("You must use bolts with a crossbow.");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}

			if (c.followPlayerId > 0) {
				c.getActionSender().resetFollow();
			}
			if (c.attackTimer <= 0) {
				c.getCombat().attackNpc(c.npcIndex);
				c.attackTimer++;
			}
			break;

		/**
		 * Attack npc with magic
		 **/
		case MAGE_NPC:
			if (!c.mageAllowed) {
				c.mageAllowed = true;
				c.sendMessage("I can't reach that.");
				break;
			}
			c.npcIndex = c.getInStream().readSignedWordBigEndianA();
			int castingSpellId = c.getInStream().readSignedWordA();
			c.usingMagic = false;

			if (MobHandler.npcs[c.npcIndex] == null) {
				break;
			}

			if (MobHandler.npcs[c.npcIndex].maxHP == 0
					|| MobHandler.npcs[c.npcIndex].npcType == 944) {
				c.sendMessage("You can't attack this npc.");
				break;
			}

			for (int i = 0; i < c.MAGIC_SPELLS.length; i++) {
				if (castingSpellId == c.MAGIC_SPELLS[i][0]) {
					c.spellId = i;
					c.usingMagic = true;
					break;
				}
			}
			if (castingSpellId == 1171) { // crumble undead
				for (int npc : Configuration.UNDEAD_NPCS) {
					if (MobHandler.npcs[c.npcIndex].npcType != npc) {
						c.sendMessage("You can only attack undead monsters with this spell.");
						c.usingMagic = false;
						c.stopMovement();
						break;
					}
				}
			}

			if (c.autocasting)
				c.autocasting = false;

			if (c.usingMagic) {
				if (c.goodDistance(c.getX(), c.getY(),
						MobHandler.npcs[c.npcIndex].getX(),
						MobHandler.npcs[c.npcIndex].getY(), 6)) {
					c.stopMovement();
				}
				if (c.attackTimer <= 0) {
					c.getCombat().attackNpc(c.npcIndex);
					c.attackTimer++;
				}
			}

			break;

		case FIRST_CLICK:
			if (c.isDead)
				return;
			c.npcClickIndex = c.inStream.readSignedWordBigEndian();
			c.getActionSender().followNpc();
			c.followMobId = c.npcClickIndex;
			c.faceNPC(c.npcClickIndex);
			c.faceUpdate(c.npcIndex);
			c.faceUpdate(c.npcClickIndex);
			c.faceUpdate(c.followMobId);
			if (c != null && MobHandler.npcs[c.npcClickIndex] != null
					|| c.clickNpcType > 0)
				c.npcType = MobHandler.npcs[c.npcClickIndex].npcType;

			if (c.goodDistance(MobHandler.npcs[c.npcClickIndex].getX(),
					MobHandler.npcs[c.npcClickIndex].getY(), c.getX(),
					c.getY(), 2)) {
				c.turnPlayerTo(MobHandler.npcs[c.npcClickIndex].getX(),
						MobHandler.npcs[c.npcClickIndex].getY());
				MobHandler.npcs[c.npcClickIndex].facePlayer(c.playerId);
				c.getActions().firstClickNpc(c.npcType);
			} else {
				c.faceUpdate(c.followMobId);
				c.clickNpcType = 1;
				c.faceUpdate(c.npcIndex);
				
				TaskHandler.submit(new Task(1, true) {

					@Override
					public void execute() {
						c.faceUpdate(c.npcIndex);
						c.faceUpdate(c.followMobId);
						if ((c.clickNpcType == 1)
								&& MobHandler.npcs[c.npcClickIndex] != null) {
							if (c.goodDistance(c.getX(), c.getY(),
									MobHandler.npcs[c.npcClickIndex].getX(),
									MobHandler.npcs[c.npcClickIndex].getY(), 1)) {
								c.faceUpdate(c.npcIndex);
								c.turnPlayerTo(
										MobHandler.npcs[c.npcClickIndex].getX(),
										MobHandler.npcs[c.npcClickIndex].getY());
								MobHandler.npcs[c.npcClickIndex]
										.facePlayer(c.playerId);
								c.getActions().firstClickNpc(c.npcType);
								this.cancel();
								c.faceUpdate(c.npcIndex);
							}
						}
						if (c.clickNpcType == 0 || c.clickNpcType > 1)
							this.cancel();
					}
					
					@Override
					public void onCancel() {
						c.clickNpcType = 0;
					}
					
				});
				
			}
			break;

		case SECOND_CLICK:
			c.npcClickIndex = c.inStream.readUnsignedWordBigEndianA();
			c.npcType = MobHandler.npcs[c.npcClickIndex].npcType;
			if (c.goodDistance(MobHandler.npcs[c.npcClickIndex].getX(),
					MobHandler.npcs[c.npcClickIndex].getY(), c.getX(),
					c.getY(), 2)) {
				c.faceUpdate(c.npcClickIndex);
				c.turnPlayerTo(MobHandler.npcs[c.npcClickIndex].getX(),
						MobHandler.npcs[c.npcClickIndex].getY());
				MobHandler.npcs[c.npcClickIndex].facePlayer(c.playerId);
				c.getActions().secondClickNpc(c.npcType);
			} else {
				c.clickNpcType = 2;
				
				TaskHandler.submit(new Task(1, true) {

					@Override
					public void execute() {
						if ((c.clickNpcType == 2)
								&& MobHandler.npcs[c.npcClickIndex] != null) {
							if (c.goodDistance(c.getX(), c.getY(),
									MobHandler.npcs[c.npcClickIndex].getX(),
									MobHandler.npcs[c.npcClickIndex].getY(), 1)) {
								c.turnPlayerTo(
										MobHandler.npcs[c.npcClickIndex].getX(),
										MobHandler.npcs[c.npcClickIndex].getY());
								MobHandler.npcs[c.npcClickIndex]
										.facePlayer(c.playerId);
								c.faceUpdate(c.npcIndex);
								c.getActions().secondClickNpc(c.npcType);
								this.cancel();
							}
						}
						if (c.clickNpcType < 2 || c.clickNpcType > 2)
							this.cancel();						
					}
					
					@Override
					public void onCancel() {
						c.clickNpcType = 0;
					}					
				});
			}
			break;

		case THIRD_CLICK:
			c.npcClickIndex = c.inStream.readSignedWord();
			c.npcType = MobHandler.npcs[c.npcClickIndex].npcType;
			if (c.goodDistance(MobHandler.npcs[c.npcClickIndex].getX(),
					MobHandler.npcs[c.npcClickIndex].getY(), c.getX(),
					c.getY(), 1)) {
				c.turnPlayerTo(MobHandler.npcs[c.npcClickIndex].getX(),
						MobHandler.npcs[c.npcClickIndex].getY());
				MobHandler.npcs[c.npcClickIndex].facePlayer(c.playerId);
				c.getActions().thirdClickNpc(c.npcType);
			} else {
				c.clickNpcType = 3;
				
				TaskHandler.submit(new Task(1, true) {

					@Override
					public void execute() {
						if ((c.clickNpcType == 3)
								&& MobHandler.npcs[c.npcClickIndex] != null) {
							if (c.goodDistance(c.getX(), c.getY(),
									MobHandler.npcs[c.npcClickIndex].getX(),
									MobHandler.npcs[c.npcClickIndex].getY(), 1)) {
								c.turnPlayerTo(
										MobHandler.npcs[c.npcClickIndex].getX(),
										MobHandler.npcs[c.npcClickIndex].getY());
								MobHandler.npcs[c.npcClickIndex]
										.facePlayer(c.playerId);
								c.getActions().thirdClickNpc(c.npcType);
								this.cancel();
							}
						}
						if (c.clickNpcType < 3)
							this.cancel();						
					}
					
					@Override
					public void onCancel() {
						c.clickNpcType = 0;
					}					
				});				
			}
			break;
		}
	}
}
