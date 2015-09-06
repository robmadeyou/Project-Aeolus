package core.game.model.entity.mob;

import java.util.ArrayList;
import java.util.Arrays;

import core.Configuration;
import core.Server;
import core.game.GameConstants;
import core.game.model.entity.Hit;
import core.game.model.entity.mob.drop.Drop;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.item.GameItem;
import core.game.sound.effects.SoundEffects;
import core.game.util.Misc;
import core.game.world.clipping.region.Region;

public class MobHandler {

	public static int maxNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static Mob npcs[] = new Mob[maxNPCs];
	public static MobDefinition npcDefinitions[] = new MobDefinition[GameConstants.MAX_LISTED_NPCS];

	public MobHandler() {		
		Arrays.fill(npcs, null);		
		Arrays.fill(npcDefinitions, null);
	}

	public void sendDrop(Player player, Drop drop, int i) {
		/*
		 * This is to stop those items from dropping, If you load higher
		 * revision items, I suggest you modify this.
		 */
		if (drop.getItemId() >= GameConstants.ITEM_LIMIT) {
			return;
		}
		if (player.getEquipment().getItemName(drop.getItemId()) == null) {
			return;
		}
		GameItem item = new GameItem(player, drop.getItemId(), 1).stackable
				? new GameItem(player, drop.getItemId(),
						(drop.getMinAmount() * GameConstants.DROP_RATE)
								+ Misc.random(drop.getExtraAmount() * GameConstants.DROP_RATE))
				: new GameItem(player, drop.getItemId(), drop.getMinAmount() + Misc.random(drop.getExtraAmount()));

		Server.itemHandler.createGroundItem(player, item.id, npcs[i].absX, npcs[i].absY, item.amount, player.playerId);

	}

	public void dropItems(int i) {
		Player killer = PlayerHandler.players[npcs[i].killedBy];
		Drop[] drops = MobDrop.getDrops(npcs[i].npcType);
		if (drops == null)
			return;
		Player c = (PlayerHandler.players[npcs[i].killedBy]);
		if (c != null) {
			if (npcs[i].npcType == 912 || npcs[i].npcType == 913 || npcs[i].npcType == 914)
				c.magePoints += 1;
			Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (Drop drop : drops) {
				if (drop.getRate() == 100)
					sendDrop(killer, drop, i);
				else {
					if ((Misc.random(99) + 1) <= drop.getRate() * 1.0)
						possibleDrops[possibleDropsCount++] = drop;
				}
			}
			if (possibleDropsCount > 0)
				sendDrop(killer, possibleDrops[Misc.random(possibleDropsCount - 1)], i);

		}
	}

	public void multiAttackGfx(int i, int gfx) {
		if (npcs[i].projectileId < 0)
			return;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = (Player) PlayerHandler.players[j];
				if (c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
					int nX = MobHandler.npcs[i].getX() + offset(i);
					int nY = MobHandler.npcs[i].getY() + offset(i);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nY - pY) * -1;
					int offY = (nX - pX) * -1;
					c.getActionSender().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i),
							npcs[i].projectileId, 43, 31, -c.getId() - 1, 65);
				}
			}
		}
	}

	public boolean switchesAttackers(int i) {
		switch (npcs[i].npcType) {
		case 2551:
		case 2552:
		case 2553:
		case 2559:
		case 2560:
		case 2561:
		case 2563:
		case 2564:
		case 2565:
		case 2892:
		case 2894:
			return true;

		}
		return false;
	}

	public void multiAttackDamage(int i) {
		int max = getMaxHit(i);
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Player c = Server.playerHandler.players[j];
				if (c.isDead || c.heightLevel != npcs[i].heightLevel)
					continue;
				if (Server.playerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
					if (npcs[i].attackType == AttackType.MAGIC) {
						if (!c.prayerActive[16]) {
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().mageDef())) {
								int dam = Misc.random(max);
								c.dealHit(new Hit(dam));
							} else {
								c.dealHit(new Hit(0));
							}
						} else {
							c.dealHit(new Hit(0));
						}
					} else if (npcs[i].attackType == AttackType.RANGED) {
						if (!c.prayerActive[17]) {
							int dam = Misc.random(max);
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().calculateRangeDefence())) {
								c.dealHit(new Hit(dam));
							} else {
								c.dealHit(new Hit(0));
							}
						} else {
							c.dealHit(new Hit(0));
						}
					}
					if (npcs[i].endGfx > 0) {
						c.gfx0(npcs[i].endGfx);
					}
				}
				c.getActionSender().refreshSkill(3);
			}
		}
	}

	public int getClosePlayer(int mobIndex) {
		for (int playerIndex = 0; playerIndex < PlayerHandler.players.length; playerIndex++) {
			if (PlayerHandler.players[playerIndex] != null) {
				if (playerIndex == npcs[mobIndex].spawnedBy)
					return playerIndex;
				if (goodDistance(PlayerHandler.players[playerIndex].absX, PlayerHandler.players[playerIndex].absY, npcs[mobIndex].absX,
						npcs[mobIndex].absY, 2 + distanceRequired(mobIndex) + followDistance(mobIndex)) || isFightCaveNpc(mobIndex)) {
					if ((PlayerHandler.players[playerIndex].underAttackBy <= 0 && PlayerHandler.players[playerIndex].underAttackBy2 <= 0)
							|| PlayerHandler.players[playerIndex].inMulti())
						if (PlayerHandler.players[playerIndex].heightLevel == npcs[mobIndex].heightLevel)
							return playerIndex;
				}
			}
		}
		return 0;
	}

	public int getCloseRandomPlayer(int mobIndex) {
		ArrayList<Integer> players = new ArrayList<Integer>();
		for (int playerIndex = 0; playerIndex < PlayerHandler.players.length; playerIndex++) {
			if (PlayerHandler.players[playerIndex] != null) {
				if (goodDistance(PlayerHandler.players[playerIndex].absX, PlayerHandler.players[playerIndex].absY, npcs[mobIndex].absX,
						npcs[mobIndex].absY, 2 + distanceRequired(mobIndex) + followDistance(mobIndex)) || isFightCaveNpc(mobIndex)) {
					if ((PlayerHandler.players[playerIndex].underAttackBy <= 0 && PlayerHandler.players[playerIndex].underAttackBy2 <= 0)
							|| PlayerHandler.players[playerIndex].inMulti())
						if (PlayerHandler.players[playerIndex].heightLevel == npcs[mobIndex].heightLevel)
							players.add(playerIndex);
				}
			}
		}
		if (players.size() > 0)
			return players.get(Misc.random(players.size() - 1));
		else
			return 0;
	}

	public boolean isFightCaveNpc(int mobIndex) {
		switch (npcs[mobIndex].npcType) {
		case 2627:
		case 2630:
		case 2631:
		case 2741:
		case 2743:
		case 2745:
			return true;
		}
		return false;
	}

	/**
	 * Summon npc, etc
	 **/
	public void spawnNpc(Player p, int npcType, int x, int y, int heightLevel, WalkType walkType, int HP, int maxHit,
			int attack, int defence, boolean attackPlayer, boolean headIcon) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return;
		}
		Mob newNPC = new Mob(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkType = walkType;
		newNPC.HP = HP;
		newNPC.maxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = p.getId();
		if (headIcon)
			p.getActionSender().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (p != null) {
				newNPC.killerId = p.playerId;
			}
		}
		npcs[slot] = newNPC;
	}

	/**
	 * Emotes
	 **/
	public static int getAttackEmote(int mobIndex) {
		switch (MobHandler.npcs[mobIndex].npcType) {
		case 2550:
			if (npcs[mobIndex].attackType == AttackType.MELEE)
				return 7060;
			else
				return 7063;
		case 2892:
		case 2894:
			return 2868;
		case 2627:
			return 2621;
		case 2630:
			return 2625;
		case 2631:
			return 2633;
		case 2741:
			return 2637;
		case 2746:
			return 2637;
		case 2607:
			return 2611;
		case 2743:// 360
			return 2647;
		// bandos gwd
		case 2551:
		case 2552:
		case 2553:
			return 6154;
		// end of gwd
		// arma gwd
		case 2558:
			return 3505;
		case 2560:
			return 6953;
		case 2559:
			return 6952;
		case 2561:
			return 6954;
		// end of arma gwd
		// sara gwd
		case 2562:
			return 6964;
		case 2563:
			return 6376;
		case 2564:
			return 7018;
		case 2565:
			return 7009;
		// end of sara gwd
		case 13: // wizards
			return 711;

		case 103:
		case 655:
			return 123;

		case 1624:
			return 1557;

		case 1648:
			return 1590;

		case 2783: // dark beast
			return 2733;

		case 1615: // abby demon
			return 1537;

		case 1613: // nech
			return 1528;

		case 1610:
		case 1611: // garg
			return 1519;

		case 1616: // basilisk
			return 1546;

		case 90: // skele
			return 260;

		case 50:// drags
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
			return 80;

		case 124: // earth warrior
			return 390;

		case 803: // monk
			return 422;

		case 52: // baby drag
			return 25;

		case 58: // Shadow Spider
		case 59: // Giant Spider
		case 60: // Giant Spider
		case 61: // Spider
		case 62: // Jungle Spider
		case 63: // Deadly Red Spider
		case 64: // Ice Spider
		case 134:
			return 143;

		case 105: // Bear
		case 106: // Bear
			return 41;

		case 412:
		case 78:
			return 30;

		case 2033: // rat
			return 138;

		case 2031: // bloodworm
			return 2070;

		case 101: // goblin
			return 309;

		case 81: // cow
			return 0x03B;

		case 21: // hero
			return 451;

		case 41: // chicken
			return 55;

		case 9: // guard
		case 32: // guard
		case 20: // paladin
			return 451;

		case 1338: // dagannoth
		case 1340:
		case 1342:
			return 1341;

		case 19: // white knight
			return 406;

		case 110:
		case 111: // ice giant
		case 112:
		case 117:
			return 128;

		case 2452:
			return 1312;

		case 2889:
			return 2859;

		case 118:
		case 119:
			return 99;

		case 82:// Lesser Demon
		case 83:// Greater Demon
		case 84:// Black Demon
		case 1472:// jungle demon
			return 64;

		case 1267:
		case 1265:
			return 1312;

		case 125: // ice warrior
		case 178:
			return 451;

		case 1153: // Kalphite Worker
		case 1154: // Kalphite Soldier
		case 1155: // Kalphite guardian
		case 1156: // Kalphite worker
		case 1157: // Kalphite guardian
			return 1184;

		case 123:
		case 122:
			return 164;

		case 2028: // karil
			return 2075;

		case 2025: // ahrim
			return 729;

		case 2026: // dharok
			return 2067;

		case 2027: // guthan
			return 2080;

		case 2029: // torag
			return 0x814;

		case 2030: // verac
			return 2062;

		case 2881: // supreme
			return 2855;

		case 2882: // prime
			return 2854;

		case 2883: // rex
			return 2851;

		case 3200:
			return 3146;

		case 2745:
			if (npcs[mobIndex].attackType == AttackType.MAGIC)
				return 2656;
			else if (npcs[mobIndex].attackType == AttackType.RANGED)
				return 2652;
			else if (npcs[mobIndex].attackType == AttackType.MELEE)
				return 2655;

		default:
			return 0x326;
		}
	}

	public int getDeadEmote(int i) {
		switch (npcs[i].npcType) {
		// sara gwd
		case 2562:
			return 6965;
		case 2563:
			return 6377;
		case 2564:
			return 7016;
		case 2565:
			return 7011;
		// bandos gwd
		case 2551:
		case 2552:
		case 2553:
			return 6156;
		case 2550:
			return 7062;
		case 2892:
		case 2894:
			return 2865;
		case 1612: // banshee
			return 1524;
		case 2558:
			return 3503;
		case 2559:
		case 2560:
		case 2561:
			return 6956;
		case 2607:
			return 2607;
		case 2627:
			return 2620;
		case 2630:
			return 2627;
		case 2631:
			return 2630;
		case 2738:
			return 2627;
		case 2741:
			return 2638;
		case 2746:
			return 2638;
		case 2743:
			return 2646;
		case 2745:
			return 2654;

		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return -1;

		case 3200:
			return 3147;

		case 2035: // spider
			return 146;

		case 2033: // rat
			return 141;

		case 2031: // bloodvel
			return 2073;

		case 101: // goblin
			return 313;

		case 81: // cow
			return 0x03E;

		case 41: // chicken
			return 57;

		case 1338: // dagannoth
		case 1340:
		case 1342:
			return 1342;

		case 2881:
		case 2882:
		case 2883:
			return 2856;

		case 111: // ice giant
			return 131;

		case 125: // ice warrior
			return 843;

		case 751:// Zombies!!
			return 302;

		case 1626:
		case 1627:
		case 1628:
		case 1629:
		case 1630:
		case 1631:
		case 1632: // turoth!
			return 1597;

		case 1616: // basilisk
			return 1548;

		case 1653: // hand
			return 1590;

		case 82:// demons
		case 83:
		case 84:
			return 67;

		case 1605:// abby spec
			return 1508;

		case 51:// baby drags
		case 52:
		case 1589:
		case 3376:
			return 28;

		case 1610:
		case 1611:
			return 1518;

		case 1618:
		case 1619:
			return 1553;

		case 1620:
		case 1621:
			return 1563;

		case 2783:
			return 2732;

		case 1615:
			return 1538;

		case 1624:
			return 1558;

		case 1613:
			return 1530;

		case 1633:
		case 1634:
		case 1635:
		case 1636:
			return 1580;

		case 1648:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1654:
		case 1655:
		case 1656:
		case 1657:
			return 1590;

		case 100:
		case 102:
			return 313;

		case 105:
		case 106:
			return 44;

		case 412:
		case 78:
			return 36;

		case 122:
		case 123:
			return 167;

		case 58:
		case 59:
		case 60:
		case 61:
		case 62:
		case 63:
		case 64:
		case 134:
			return 146;

		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
			return 1190;

		case 103:
		case 104:
			return 123;

		case 118:
		case 119:
			return 102;

		case 50:// drags
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
			return 92;

		default:
			return 2304;
		}
	}

	/**
	 * Attack delays
	 **/
	public int getNpcDelay(int i) {
		switch (npcs[i].npcType) {
		case 2025:
		case 2028:
			return 7;

		case 2745:
			return 8;

		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2550:
			return 6;
		// saradomin gw boss
		case 2562:
			return 2;

		default:
			return 5;
		}
	}

	/**
	 * Hit delays
	 **/
	public int getHitDelay(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 3200:
		case 2892:
		case 2894:
			return 3;

		case 2743:
		case 2631:
		case 2558:
		case 2559:
		case 2560:
			return 3;

		case 2745:
			if (npcs[i].attackType == AttackType.RANGED || npcs[i].attackType == AttackType.MAGIC)
				return 5;
			else
				return 2;

		case 2025:
			return 4;
		case 2028:
			return 3;

		default:
			return 2;
		}
	}

	/**
	 * Npc respawn time
	 **/
	public int getRespawnTime(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 2883:
		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2562:
		case 2563:
		case 2564:
		case 2550:
		case 2551:
		case 2552:
		case 2553:
			return 100;
		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return 500;
		default:
			return 25;
		}
	}

	public static void newNPC(MobSpawn s) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return; // no free slot found

		Mob newNPC = new Mob(slot, s.getNpcId());
		newNPC.absX = s.getXPos();
		newNPC.absY = s.getYPos();
		newNPC.makeX = s.getXPos();
		newNPC.makeY = s.getYPos();
		newNPC.heightLevel = s.getHeight();
		newNPC.walkType = s.getWalkType();
		int hp = MobDefinition.getDefinitions()[s.getNpcId()].getHitpoints();
		newNPC.HP = hp;
		newNPC.maxHP = hp;
		npcs[slot] = newNPC;
	}

	/**
	 * Checks MobDefinitions to see if an npc is aggressive
	 * @param npcId
	 */
	public boolean isAggressive(int npcId) {
		return MobDefinition.DEFINITIONS[npcId].isAggressive();
	}
	
	public void newNPC(int npcType, int x, int y, int heightLevel, WalkType walkType, int HP, int maxHit, int attack,
			int defence, int hp) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return; // no free slot found

		Mob newNPC = new Mob(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkType = walkType;
		newNPC.HP = HP;
		newNPC.maxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.hp = hp;
		npcs[slot] = newNPC;
	}

	public void process() {
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] == null)
				continue;
			npcs[i].clearUpdateFlags();
		}

		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] != null) {
				if (npcs[i].actionTimer > 0) {
					npcs[i].actionTimer--;
				}
				if (npcs[i].freezeTimer > 0) {
					npcs[i].freezeTimer--;
				}
				if (npcs[i].hitDelayTimer > 0) {
					npcs[i].hitDelayTimer--;
				}
				if (npcs[i].hitDelayTimer == 1) {
					npcs[i].hitDelayTimer = 0;
					applyDamage(i);
				}
				if (npcs[i].attackTimer > 0) {
					npcs[i].attackTimer--;
				}
				if (npcs[i].spawnedBy > 0) { // delete summons npc
					if (PlayerHandler.players[npcs[i].spawnedBy] == null
							|| PlayerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel
							|| PlayerHandler.players[npcs[i].spawnedBy].respawnTimer > 0
							|| !PlayerHandler.players[npcs[i].spawnedBy].goodDistance(npcs[i].getX(), npcs[i].getY(),
									PlayerHandler.players[npcs[i].spawnedBy].getX(),
									PlayerHandler.players[npcs[i].spawnedBy].getY(), 20)) {

						if (PlayerHandler.players[npcs[i].spawnedBy] != null) {

						}
						npcs[i] = null;
					}
				}
				if (npcs[i] == null)
					continue;

				/**
				 * Attacking player
				 **/
				if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead
						&& !switchesAttackers(i)) {
					npcs[i].killerId = getCloseRandomPlayer(i);
				} else if (isAggressive(i) && !npcs[i].underAttack
						&& !npcs[i].isDead && switchesAttackers(i)) {
					npcs[i].killerId = getCloseRandomPlayer(i);
				}

				if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000)
					npcs[i].underAttackBy = 0;

				if ((npcs[i].killerId > 0 || npcs[i].underAttack)
						&& npcs[i].npcType != 7891 && !npcs[i].walkingHome
						&& retaliates(npcs[i].npcType)) {
					if (!npcs[i].isDead) {
						int p = npcs[i].killerId;
						if (PlayerHandler.players[p] != null) {
							Player c = PlayerHandler.players[p];
							followPlayer(i, c.playerId);
							if (npcs[i] == null)
								continue;
							if (npcs[i].attackTimer == 0) {
								attackPlayer(c, i);
							}
						} else {
							npcs[i].killerId = 0;
							npcs[i].underAttack = false;
							npcs[i].facePlayer(0);
						}
					}
				}

				/**
				 * Random walking and walking home
				 **/
				if (npcs[i] == null)
					continue;
				if ((!npcs[i].underAttack || npcs[i].walkingHome) && npcs[i].randomWalk && !npcs[i].isDead) {
					npcs[i].facePlayer(0);
					npcs[i].killerId = 0;
					if (npcs[i].spawnedBy == 0) {
						if ((npcs[i].absX > npcs[i].makeX + GameConstants.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absX < npcs[i].makeX - GameConstants.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absY > npcs[i].makeY + GameConstants.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absY < npcs[i].makeY - GameConstants.NPC_RANDOM_WALK_DISTANCE)) {
							npcs[i].walkingHome = true;
						}
					}

					if (npcs[i].walkingHome && npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
						npcs[i].walkingHome = false;
					} else if (npcs[i].walkingHome) {
						npcs[i].moveX = GetMove(npcs[i].absX, npcs[i].makeX);
						npcs[i].moveY = GetMove(npcs[i].absY, npcs[i].makeY);
						npcs[i].getNextNPCMovement(i);
						npcs[i].updateRequired = true;
					}
					if (npcs[i].walkType == WalkType.RANDOM) {
						if (Misc.random(3) == 1 && !npcs[i].walkingHome) {
							int MoveX = 0;
							int MoveY = 0;
							int Rnd = Misc.random(9);
							
							switch(Rnd) {
							
							case 1:
								MoveX = 1;
								MoveY = 1;
								break;
								
							case 2:
								MoveX = -1;
								break;
								
							case 3:
								MoveY = -1;
								break;
								
							case 4:
								MoveX = 1;
								break;
								
							case 5:
								MoveY = 1;
								break;
								
							case 6:
								MoveX = -1;
								MoveY = -1;
								break;
								
							case 7:
								MoveX = -1;
								MoveY = 1;
								break;
								
							case 8:
								MoveX = 1;
								MoveY = -1;
								break;
								
							default:
								MoveX = 0;
								MoveY = 0;
								break;							
							}
							
							switch (MoveX) {
							
							case -1:
								if (npcs[i].absX - MoveX > npcs[i].makeX - 1) {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
								break;
							
							case 1:
								if (npcs[i].absX + MoveX < npcs[i].makeX + 1) {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
								break;
							
							}
							
							switch (MoveY) {
							
							case -1:
								if (npcs[i].absY - MoveY > npcs[i].makeY - 1) {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
								break;
							
							case 1:
								if (npcs[i].absY + MoveY < npcs[i].makeY + 1) {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
								break;
							
							}							
							npcs[i].getNextNPCMovement(i);
							npcs[i].updateRequired = true;
						}					
				} else if (npcs[i].walkType != WalkType.RANDOM && !npcs[i].walkingHome) {
					
					switch (npcs[i].walkType) {

					case WEST:
						npcs[i].turnNpc(npcs[i].absX - 1, npcs[i].absY);
						break;

					case EAST:
						npcs[i].turnNpc(npcs[i].absX + 1, npcs[i].absY);
						break;

					case SOUTH:
						npcs[i].turnNpc(npcs[i].absX, npcs[i].absY - 1);
						break;
					case NORTH:
						npcs[i].turnNpc(npcs[i].absX, npcs[i].absY + 1);
						break;

					default:
						if (npcs[i].walkType != WalkType.RANDOM) {
							npcs[i].turnNpc(npcs[i].absX, npcs[i].absY);
						}
						break;
					}
				}
			}

				if (npcs[i].isDead == true) {
					if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false && npcs[i].needRespawn == false) {
						npcs[i].updateRequired = true;
						npcs[i].facePlayer(0);
						npcs[i].killedBy = getNpcKillerId(i);
						npcs[i].animNumber = getDeadEmote(i); // dead emote
						npcs[i].animUpdateRequired = true;
						npcs[i].freezeTimer = 0;
						npcs[i].applyDead = true;
						npcs[i].actionTimer = 4; // delete time
						resetPlayersInCombat(i);
					} else if (npcs[i].actionTimer == 0 && npcs[i].applyDead == true && npcs[i].needRespawn == false) {
						npcs[i].needRespawn = true;
						npcs[i].actionTimer = getRespawnTime(i); // respawn time
						dropItems(i); // Mob drops
						appendKillCount(i);
						npcs[i].absX = npcs[i].makeX;
						npcs[i].absY = npcs[i].makeY;
						npcs[i].HP = npcs[i].maxHP;
						npcs[i].animNumber = 0x328;
						npcs[i].updateRequired = true;
						npcs[i].animUpdateRequired = true;
						if (npcs[i].npcType >= 2440 && npcs[i].npcType <= 2446) {
							Server.objectManager.removeObject(npcs[i].absX, npcs[i].absY);
						}
						if (npcs[i].npcType == 2745) {
							// handleJadDeath(i);
						}
					} else if (npcs[i].actionTimer == 0 && npcs[i].needRespawn == true) {
						Player player = (Player) PlayerHandler.players[npcs[i].spawnedBy];
						if (player != null) {
							npcs[i] = null;
						} else {
							int old1 = npcs[i].npcType;
							int old2 = npcs[i].makeX;
							int old3 = npcs[i].makeY;
							int old4 = npcs[i].heightLevel;
							WalkType old5 = npcs[i].walkType;
							int old6 = npcs[i].maxHP;
							int old7 = npcs[i].maxHit;
							int old8 = npcs[i].attack;
							int old9 = npcs[i].defence;
							int old10 = npcs[i].hp;

							npcs[i] = null;
							newNPC(old1, old2, old3, old4, old5, old6, old7, old8, old9, old10);
						}
					}
				}
			}
		}
	}

	public boolean getsPulled(int i) {
		switch (npcs[i].npcType) {
		case 2550:
			if (npcs[i].firstAttacker > 0)
				return false;
			break;
		}
		return true;
	}

	public boolean multiAttacks(int i) {
		switch (npcs[i].npcType) {
		case 2558:
			return true;
		case 2562:
			if (npcs[i].attackType == AttackType.MAGIC)
				return true;
		case 2550:
			if (npcs[i].attackType == AttackType.RANGED)
				return true;
		default:
			return false;
		}

	}

	/**
	 * Npc killer id?
	 **/
	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int count = 0;
		int killerId = 0;
		for (int p = 1; p < GameConstants.MAX_PLAYERS; p++) {
			if (PlayerHandler.players[p] != null) {
				if (PlayerHandler.players[p].lastNpcAttacked == npcId) {
					if (PlayerHandler.players[p].totalDamageDealt > oldDamage) {
						oldDamage = PlayerHandler.players[p].totalDamageDealt;
						killerId = p;
					}
					PlayerHandler.players[p].totalDamageDealt = 0;
				}
			}
		}
		return killerId;
	}

	public void appendKillCount(int i) {
		Player c = (Player) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] kcMonsters = { 122, 49, 2558, 2559, 2560, 2561, 2550, 2551, 2552, 2553, 2562, 2563, 2564, 2565 };
			for (int j : kcMonsters) {
				if (npcs[i].npcType == j) {
					if (c.killCount < 20) {
						c.killCount++;
						c.sendMessage("Killcount: " + c.killCount);
					} else {
						c.sendMessage("You already have 20 kill count");
					}
					break;
				}
			}
		}
	}

	/**
	 * Resets players in combat
	 */

	public void resetPlayersInCombat(int i) {
		for (int count = 0; count < PlayerHandler.players.length; count++) {
			if (PlayerHandler.players[count] != null)
				if (PlayerHandler.players[count].underAttackBy2 == i)
					PlayerHandler.players[count].underAttackBy2 = 0;
		}
	}

	/**
	 * Npc Follow Player
	 **/
	public int GetMove(int Place1, int Place2) {
		if ((Place1 - Place2) == 0) {
			return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean followPlayer(int i) {
		switch (npcs[i].npcType) {
		case 2892:
		case 2894:
			return false;
		}
		return true;
	}

	public void followPlayer(int i, int playerId) {
		if (PlayerHandler.players[playerId] == null) {
			return;
		}
		if (PlayerHandler.players[playerId].respawnTimer > 0) {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			return;
		}

		if (!followPlayer(i)) {
			npcs[i].facePlayer(playerId);
			return;
		}

		int playerX = PlayerHandler.players[playerId].absX;
		int playerY = PlayerHandler.players[playerId].absY;
		npcs[i].randomWalk = false;
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), playerX, playerY, distanceRequired(i)))
			return;
		if ((npcs[i].spawnedBy > 0) || ((npcs[i].absX < npcs[i].makeX + GameConstants.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absX > npcs[i].makeX - GameConstants.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absY < npcs[i].makeY + GameConstants.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absY > npcs[i].makeY - GameConstants.NPC_FOLLOW_DISTANCE))) {
			if (npcs[i].heightLevel == PlayerHandler.players[playerId].heightLevel) {
				if (PlayerHandler.players[playerId] != null && npcs[i] != null) {
					if (playerY < npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerY > npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX < npcs[i].absX) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX > npcs[i].absX) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX == npcs[i].absX || playerY == npcs[i].absY) {
						int o = Misc.random(3);
						switch (o) {
						case 0:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY + 1);
							break;

						case 1:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY - 1);
							break;

						case 2:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX + 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;

						case 3:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX - 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;
						}
					}
					handleClipping(i);
					npcs[i].getNextNPCMovement(i);
					npcs[i].facePlayer(playerId);
					npcs[i].updateRequired = true;
				}
			}
		} else {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
		}
	}

	public void handleClipping(int i) {
		Mob npc = npcs[i];
			if(npc.moveX == 1 && npc.moveY == 1) {
					if((Region.getClipping(npc.absX + 1, npc.absY + 1, npc.heightLevel) & 0x12801e0) != 0)  {
							npc.moveX = 0; npc.moveY = 0;
							if((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) == 0)
								npc.moveY = 1;
							else 
								npc.moveX = 1; 				
							}
					} else if(npc.moveX == -1 && npc.moveY == -1) {
						if((Region.getClipping(npc.absX - 1, npc.absY - 1, npc.heightLevel) & 0x128010e) != 0)  {
							npc.moveX = 0; npc.moveY = 0;
							if((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) == 0)
								npc.moveY = -1;
							else
								npc.moveX = -1; 		
					}
					} else if(npc.moveX == 1 && npc.moveY == -1) {
							if((Region.getClipping(npc.absX + 1, npc.absY - 1, npc.heightLevel) & 0x1280183) != 0)  {
							npc.moveX = 0; npc.moveY = 0;
							if((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) == 0)
								npc.moveY = -1;
							else
								npc.moveX = 1; 
							}
					} else if(npc.moveX == -1 && npc.moveY == 1) {
						if((Region.getClipping(npc.absX - 1, npc.absY + 1, npc.heightLevel) & 0x128013) != 0)  {
							npc.moveX = 0; npc.moveY = 0;
							if((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) == 0)
								npc.moveY = 1;
							else
								npc.moveX = -1; 
										}
					} //Checking Diagonal movement. 
					
			if (npc.moveY == -1 ) {
				if ((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) != 0)
                    npc.moveY = 0;
			} else if( npc.moveY == 1) {
				if((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) != 0)
					npc.moveY = 0;
			} //Checking Y movement.
			if(npc.moveX == 1) {
				if((Region.getClipping(npc.absX + 1, npc.absY, npc.heightLevel) & 0x1280180) != 0) 
					npc.moveX = 0;
				} else if(npc.moveX == -1) {
				 if((Region.getClipping(npc.absX - 1, npc.absY, npc.heightLevel) & 0x1280108) != 0)
					npc.moveX = 0;
			} //Checking X movement.
	}

	/**
	 * load spell
	 **/
	public void loadSpell2(int i) {
		npcs[i].attackType = AttackType.FIRE_BREATH;
		int random = Misc.random(3);
		if (random == 0) {
			npcs[i].projectileId = 393; // red
			npcs[i].endGfx = 430;
		} else if (random == 1) {
			npcs[i].projectileId = 394; // green
			npcs[i].endGfx = 429;
		} else if (random == 2) {
			npcs[i].projectileId = 395; // white
			npcs[i].endGfx = 431;
		} else if (random == 3) {
			npcs[i].projectileId = 396; // blue
			npcs[i].endGfx = 428;
		}
	}

	public void loadSpell(int i) {
		switch (npcs[i].npcType) {
		case 2892:
			npcs[i].projectileId = 94;
			npcs[i].attackType = AttackType.MAGIC;
			npcs[i].endGfx = 95;
			break;
		case 2894:
			npcs[i].projectileId = 298;
			npcs[i].attackType = AttackType.RANGED;
			break;
		case 50:
			int random = Misc.random(4);
			if (random == 0) {
				npcs[i].projectileId = 393; // red
				npcs[i].endGfx = 430;
				npcs[i].attackType = AttackType.FIRE_BREATH;
			} else if (random == 1) {
				npcs[i].projectileId = 394; // green
				npcs[i].endGfx = 429;
				npcs[i].attackType = AttackType.FIRE_BREATH;
			} else if (random == 2) {
				npcs[i].projectileId = 395; // white
				npcs[i].endGfx = 431;
				npcs[i].attackType = AttackType.FIRE_BREATH;
			} else if (random == 3) {
				npcs[i].projectileId = 396; // blue
				npcs[i].endGfx = 428;
				npcs[i].attackType = AttackType.FIRE_BREATH;
			} else if (random == 4) {
				npcs[i].projectileId = -1; // melee
				npcs[i].endGfx = -1;
				npcs[i].attackType = AttackType.MELEE;
			}
			break;
		// arma npcs
		case 2561:
			npcs[i].attackType = AttackType.MELEE;
			break;
		case 2560:
			npcs[i].attackType = AttackType.RANGED;
			npcs[i].projectileId = 1190;
			break;
		case 2559:
			npcs[i].attackType = AttackType.MAGIC;
			npcs[i].projectileId = 1203;
			break;
		case 2558:
			random = Misc.random(1);
			
			switch (random) {
		case 0:
			npcs[i].attackType = AttackType.RANGED;
			if (npcs[i].attackType == AttackType.RANGED) {
				npcs[i].projectileId = 1197;
			}
			break;
			
		case 1:
			npcs[i].attackType = AttackType.MAGIC;
			 if (npcs[i].attackType == AttackType.MAGIC) {
				npcs[i].attackType = AttackType.MAGIC;
				npcs[i].projectileId = 1198;
			 }
			break;
			}
			break;
		// sara npcs
		case 2562: // sara
			random = Misc.random(1);
			if (random == 0) {
				npcs[i].attackType = AttackType.MAGIC;
				npcs[i].endGfx = 1224;
				npcs[i].projectileId = -1;
			} else if (random == 1)
				npcs[i].attackType = AttackType.MELEE;
			break;
		case 2563: // star
			npcs[i].attackType = AttackType.MELEE;
			break;
		case 2564: // growler
			npcs[i].attackType = AttackType.MAGIC;
			npcs[i].projectileId = 1203;
			break;
		case 2565: // bree
			npcs[i].attackType = AttackType.RANGED;
			npcs[i].projectileId = 9;
			break;
		// bandos npcs
		case 2550:
			random = Misc.random(2);
			if (random == 0 || random == 1)
				npcs[i].attackType = AttackType.MELEE;
			else {
				npcs[i].attackType = AttackType.RANGED;
				npcs[i].endGfx = 1211;
				npcs[i].projectileId = 288;
			}
			break;
		case 2551:
			npcs[i].attackType = AttackType.MELEE;
			break;
		case 2552:
			npcs[i].attackType = AttackType.MAGIC;
			npcs[i].projectileId = 1203;
			break;
		case 2553:
			npcs[i].attackType = AttackType.RANGED;
			npcs[i].projectileId = 1206;
			break;
		case 2025:
			npcs[i].attackType = AttackType.MAGIC;
			int r = Misc.random(3);
			if (r == 0) {
				npcs[i].gfx100(158);
				npcs[i].projectileId = 159;
				npcs[i].endGfx = 160;
			}
			if (r == 1) {
				npcs[i].gfx100(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			if (r == 2) {
				npcs[i].gfx100(164);
				npcs[i].projectileId = 165;
				npcs[i].endGfx = 166;
			}
			if (r == 3) {
				npcs[i].gfx100(155);
				npcs[i].projectileId = 156;
			}
			break;
		case 2881:// supreme
			npcs[i].attackType = AttackType.RANGED;
			npcs[i].projectileId = 298;
			break;

		case 2882:// prime
			npcs[i].attackType = AttackType.MAGIC;
			npcs[i].projectileId = 162;
			npcs[i].endGfx = 477;
			break;

		case 2028:
			npcs[i].attackType = AttackType.RANGED;
			npcs[i].projectileId = 27;
			break;

		case 3200:
			int r2 = Misc.random(1);
			if (r2 == 0) {
				npcs[i].attackType = AttackType.RANGED;
				npcs[i].gfx100(550);
				npcs[i].projectileId = 551;
				npcs[i].endGfx = 552;
			} else {
				npcs[i].attackType = AttackType.MAGIC;
				npcs[i].gfx100(553);
				npcs[i].projectileId = 554;
				npcs[i].endGfx = 555;
			}
			break;
		case 2745:
			int r3 = 0;
			if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].spawnedBy].absX,
					PlayerHandler.players[npcs[i].spawnedBy].absY, 1))
				r3 = Misc.random(2);
			else
				r3 = Misc.random(1);
			if (r3 == 0) {
				npcs[i].attackType = AttackType.MAGIC;
				npcs[i].endGfx = 157;
				npcs[i].projectileId = 448;
			} else if (r3 == 1) {
				npcs[i].attackType = AttackType.RANGED;
				npcs[i].endGfx = 451;
				npcs[i].projectileId = -1;
			} else if (r3 == 2) {
				npcs[i].attackType = AttackType.MELEE;
				npcs[i].projectileId = -1;
			}
			break;
		case 2743:
			npcs[i].attackType = AttackType.MELEE;
			npcs[i].projectileId = 445;
			npcs[i].endGfx = 446;
			break;

		case 2631:
			npcs[i].attackType = AttackType.RANGED;
			npcs[i].projectileId = 443;
			break;
		}
	}

	/**
	 * Distanced required to attack
	 **/
	public int distanceRequired(int i) {
		switch (npcs[i].npcType) {
		case 2025:
		case 2028:
			return 6;
		case 50:
		case 2562:
			return 2;
		case 2881:// dag kings
		case 2882:
		case 3200:// chaos ele
		case 2743:
		case 2631:
		case 2745:
			return 8;
		case 2883:// rex
			return 1;
		case 2552:
		case 2553:
		case 2556:
		case 2557:
		case 2558:
		case 2559:
		case 2560:
		case 2564:
		case 2565:
			return 9;
		// things around dags
		case 2892:
		case 2894:
			return 10;
		default:
			return 1;
		}
	}

	public int followDistance(int i) {
		switch (npcs[i].npcType) {
		case 2550:
		case 2551:
		case 2562:
		case 2563:
			return 8;
		case 2883:
			return 4;
		case 2881:
		case 2882:
			return 1;

		}
		return 0;

	}

	public int getProjectileSpeed(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 3200:
			return 85;

		case 2745:
			return 130;

		case 50:
			return 90;

		case 2025:
			return 85;

		case 2028:
			return 80;

		default:
			return 85;
		}
	}

	/**
	 * NPC Attacking Player
	 **/

	public void attackPlayer(Player p, int mobIndex) {
		if (npcs[mobIndex] != null) {
			if (npcs[mobIndex].isDead)
				return;
			if (!npcs[mobIndex].inMulti() && npcs[mobIndex].underAttackBy > 0 && npcs[mobIndex].underAttackBy != p.playerId) {
				npcs[mobIndex].killerId = 0;
				return;
			}
			if (!npcs[mobIndex].inMulti() && (p.underAttackBy > 0 || (p.underAttackBy2 > 0 && p.underAttackBy2 != mobIndex))) {
				npcs[mobIndex].killerId = 0;
				return;
			}
			if (npcs[mobIndex].heightLevel != p.heightLevel) {
				npcs[mobIndex].killerId = 0;
				return;
			}
			npcs[mobIndex].facePlayer(p.playerId);
			boolean special = false;// specialCase(c,i);
			if (goodDistance(npcs[mobIndex].getX(), npcs[mobIndex].getY(), p.getX(), p.getY(), distanceRequired(mobIndex)) || special) {
				if (p.respawnTimer <= 0) {
					npcs[mobIndex].facePlayer(p.playerId);
					npcs[mobIndex].attackTimer = getNpcDelay(mobIndex);
					npcs[mobIndex].hitDelayTimer = getHitDelay(mobIndex);
					npcs[mobIndex].attackType = AttackType.MELEE;
					if (special)
						loadSpell2(mobIndex);
					else
						loadSpell(mobIndex);
					if (npcs[mobIndex].attackType == AttackType.FIRE_BREATH)
						npcs[mobIndex].hitDelayTimer += 2;
					if (multiAttacks(mobIndex)) {
						multiAttackGfx(mobIndex, npcs[mobIndex].projectileId);
						startAnimation(getAttackEmote(mobIndex), mobIndex);
						if (Configuration.enableSound) {
							p.getActionSender().sendSound(SoundEffects.getNpcAttackSounds(npcs[mobIndex].npcType));
						}
						npcs[mobIndex].oldIndex = p.playerId;
						return;
					}
					if (npcs[mobIndex].projectileId > 0) {
						int nX = MobHandler.npcs[mobIndex].getX() + offset(mobIndex);
						int nY = MobHandler.npcs[mobIndex].getY() + offset(mobIndex);
						int pX = p.getX();
						int pY = p.getY();
						int offX = (nY - pY) * -1;
						int offY = (nX - pX) * -1;
						p.getActionSender().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(mobIndex),
								npcs[mobIndex].projectileId, 43, 31, -p.getId() - 1, 65);
					}
					p.underAttackBy2 = mobIndex;
					p.singleCombatDelay2 = System.currentTimeMillis();
					npcs[mobIndex].oldIndex = p.playerId;
					startAnimation(getAttackEmote(mobIndex), mobIndex);
					p.getActionSender().removeAllWindows();
				}
			}
		}
	}

	public int offset(int mobIndex) {
		switch (npcs[mobIndex].npcType) {
		case 50:
			return 2;
		case 2881:
		case 2882:
			return 1;
		case 2745:
		case 2743:
			return 1;
		}
		return 0;
	}

	public boolean specialCase(Player p, int mobIndex) {
		if (goodDistance(npcs[mobIndex].getX(), npcs[mobIndex].getY(), p.getX(), p.getY(), 8)
				&& !goodDistance(npcs[mobIndex].getX(), npcs[mobIndex].getY(), p.getX(), p.getY(), distanceRequired(mobIndex)))
			return true;
		return false;
	}

	public boolean retaliates(int npcType) {
		return npcType < 3777 || npcType > 3780 && !(npcType >= 2440 && npcType <= 2446);
	}

	public void applyDamage(int mobIndex) {
		if (npcs[mobIndex] != null) {
			if (Server.playerHandler.players[npcs[mobIndex].oldIndex] == null) {
				return;
			}
			if (npcs[mobIndex].isDead)
				return;
			Player c = Server.playerHandler.players[npcs[mobIndex].oldIndex];
			if (multiAttacks(mobIndex)) {
				multiAttackDamage(mobIndex);
				return;
			}
			if (c.playerIndex <= 0 && c.npcIndex <= 0)
				if (c.autoRet)
					c.npcIndex = mobIndex;
			if (c.attackTimer <= 3 || c.attackTimer == 0 && c.npcIndex == 0 && c.oldNpcIndex == 0) {
				c.startAnimation(c.getCombat().getBlockEmote());
			}
			if (c.respawnTimer <= 0) {
				int damage = 0;
				if (npcs[mobIndex].attackType == AttackType.MELEE) {
					damage = Misc.random(npcs[mobIndex].maxHit);
					if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc
							.random(Server.npcHandler.npcs[mobIndex].attack)) {
						damage = 0;
					}
					if (c.prayerActive[18]) { // protect from melee
						damage = 0;
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
				}

				if (npcs[mobIndex].attackType == AttackType.RANGED) {
					damage = Misc.random(npcs[mobIndex].maxHit);
					if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > Misc
							.random(Server.npcHandler.npcs[mobIndex].attack)) {
						damage = 0;
					}
					if (c.prayerActive[17]) { // protect from range
						damage = 0;
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
				}

				if (npcs[mobIndex].attackType == AttackType.MAGIC) {
					damage = Misc.random(npcs[mobIndex].maxHit);
					boolean magicFailed = false;
					if (10 + Misc.random(c.getCombat().mageDef()) > Misc.random(Server.npcHandler.npcs[mobIndex].attack)) {
						damage = 0;
						magicFailed = true;
					}
					if (c.prayerActive[16]) { // protect from magic
						damage = 0;
						magicFailed = true;
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
					if (npcs[mobIndex].endGfx > 0 && (!magicFailed || isFightCaveNpc(mobIndex))) {
						c.gfx100(npcs[mobIndex].endGfx);
					} else {
						c.gfx100(85);
					}
				}

				if (npcs[mobIndex].attackType == AttackType.FIRE_BREATH) { // fire breath
					int anti = c.getPA().antiFire();
					if (anti == 0) {
						damage = Misc.random(30) + 10;
						c.sendMessage("You are badly burnt by the dragon fire!");
					} else if (anti == 1)
						damage = Misc.random(20);
					else if (anti == 2)
						damage = Misc.random(5);
					if (c.playerLevel[3] - damage < 0)
						damage = c.playerLevel[3];
					c.gfx100(npcs[mobIndex].endGfx);
				}
				handleSpecialEffects(c, mobIndex, damage);
				c.logoutDelay = System.currentTimeMillis(); // logout delay
				// c.setHitDiff(damage);
				c.dealHit(new Hit(damage));
			}
		}
	}

	public void handleSpecialEffects(Player c, int i, int damage) {
		if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
			if (damage > 0) {
				if (c != null) {
					if (c.playerLevel[5] > 0) {
						c.playerLevel[5]--;
						c.getActionSender().refreshSkill(5);
						c.getPA().appendPoison(12);
					}
				}
			}
		}

	}

	public static void startAnimation(int animId, int i) {
		npcs[i].animNumber = animId;
		npcs[i].animUpdateRequired = true;
		npcs[i].updateRequired = true;
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the maximum hit for an npc
	 * @param npcId
	 */
	public int getMaxHit(int npcId) {
		return MobDefinition.DEFINITIONS[npcId].getMaxHit();
	}

	/**
	 * Gets an npc's hitpoints
	 * @param npcId
	 */
	public int getNpcListHP(int npcId) {
		return MobDefinition.DEFINITIONS[npcId].getHitpoints();
	}

	/**
	 * Gets an npc's name
	 * @param npcId
	 */
	public String getNpcListName(int npcId) {
		if (MobDefinition.DEFINITIONS != null) {
			return MobDefinition.DEFINITIONS[npcId].getName();
		}
		return "nameless";
	}

	public Mob[] getNPCs() {
		return npcs;
	}
}
