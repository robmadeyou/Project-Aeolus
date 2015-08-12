package core.game.model.entity.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import core.Configuration;
import core.Server;
import core.game.GameConstants;
import core.game.content.CombatAssistant;
import core.game.content.ContentManager;
import core.game.content.DialogueHandler;
import core.game.content.PlayerKilling;
import core.game.content.consumables.Food;
import core.game.event.tick.Scheduler;
import core.game.event.tick.Tick;
import core.game.model.entity.Entity;
import core.game.model.entity.mob.Mob;
import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.player.container.Equipment;
import core.game.model.entity.player.container.Inventory;
import core.game.model.entity.player.save.PlayerSave;
import core.game.model.item.Item;
import core.game.model.item.ItemAssistant;
import core.game.model.shop.ShopAssistant;
import core.game.sound.effects.SoundEffects;
import core.game.util.Censor;
import core.game.util.Misc;
import core.game.util.Stream;
import core.game.util.log.impl.ChatLogger;
import core.game.util.log.impl.TradeLogger;
import core.game.world.clipping.PathFinder;
import core.net.Packet;
import core.net.Packet.Type;
import core.net.packets.PacketHandler;
import core.net.packets.outgoing.ActionSender;
import core.net.security.ISAACCipher;

public class Player extends Entity {

	public ArrayList<String> killedPlayers = new ArrayList<String>();
	public ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();

	public byte buffer[] = null;
	public Stream inStream = null, outStream = null;
	private Channel session;
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private ActionSender actionSender = new ActionSender(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private ActionsPerformed actionHandler = new ActionsPerformed(this);
	private PlayerKilling playerKilling = new PlayerKilling(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Queue<Packet> queuedPackets = new LinkedList<Packet>();
	private ContentManager contentManager = new ContentManager(this);
	private Equipment equipment = new Equipment(this);
	private TradeLogger tradeLog = new TradeLogger(this);
	private ChatLogger chatLog = new ChatLogger(this);
	private Censor censor = new Censor(this);
	private Inventory inventory = new Inventory(this);
	private SoundEffects soundEffects = new SoundEffects(this);
	private Movement movement = new Movement(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private Food food = new Food(this);

	public int lowMemoryVersion = 0;
	public int timeOutCounter = 0;
	public int returnCode = 2;
	private Future<?> currentTask;
	public int currentRegion = 0;

	/**
	 * Time in milliseconds used for censoring
	 */
	public long muteEnd;

	/**
	 * Reporting a player
	 */
	public String lastReported = "";
	public long lastReport = 0;

	private Rights rights;

	public boolean isFullBody = false;
	public boolean isFullHelm = false;
	public boolean isFullMask = false;

	public long dfsDelay;

	public boolean initialized = false, disconnected = false, ruleAgreeButton = false, RebuildNPCList = false,
			isActive = false, isKicked = false, friendUpdate = false, newPlayer = false, hasMultiSign = false,
			saveCharacter = false, mouseButton = false, splitChat = false, chatEffects = true, acceptAid = false,
			nextDialogue = false, autocasting = false, usedSpecial = false, mageFollow = false, dbowSpec = false,
			properLogout = false, secDbow = false, maxNextHit = false, ssSpec = false, vengOn = false,
			addStarter = false, accountFlagged = false, msbSpec = false,

	dtOption = false, dtOption2 = false, doricOption = false, doricOption2 = false, caOption2 = false,
			caOption2a = false, caOption4a = false, caOption4b = false, caOption4c = false, caPlayerTalk1 = false,
			disableAttEvt = false, AttackEventRunning = false, npcindex, spawned = false, autoRet = false;

	public int saveDelay, playerKilled, stotalPlayerDamageDealt, killedBy, lastChatId = 1, privateChat, friendSlot = 0,
			dialogueId, randomCoffin, newLocation, specEffect, specBarId, attackLevelReq, defenceLevelReq,
			strengthLevelReq, rangeLevelReq, magicLevelReq, followId, skullTimer, nextChat = 0, talkingNpc = -1,
			dialogueAction = 0, autocastId, followDistance, followId2, barrageCount = 0, delayedDamage = 0,
			delayedDamage2 = 0, lastArrowUsed = -1, clanId = -1, xInterfaceId = 0, xRemoveId = 0, xRemoveSlot = 0,
			frozenBy = 0, poisonDamage = 0, teleAction = 0, bonusAttack = 0, lastNpcAttacked = 0, killCount = 0,
			actionTimer, height = 0, magePoints = 0;

	public boolean usingOtherRangeWeapons, usingCross, usingArrows, magicDef, ignoreDefence, spellSwap,
			rangeEndGFXHeight, stopPlayerSkill, oldSpec, multiAttacking, prayerDisabled;

	public int rangeEndGFX, boltDamage, previousDamage, slayerTask, taskAmount, teleotherType, dfsCount;

	public double crossbowDamage;

	public int[] voidStatus = new int[5];
	public int[] itemKeptId = new int[4];
	public boolean[] invSlot = new boolean[28], equipSlot = new boolean[14];
	public long friends[] = new long[200];
	public long ignores[] = new long[200];
	public double specAmount = 0;
	public double specAccuracy = 1;
	public int recoilHits = 0;
	public double specDamage = 1;
	public double prayerPoint = 1.0;
	public int teleGrabItem, teleGrabX, teleGrabY, duelCount, underAttackBy, underAttackBy2, wildLevel, teleTimer,
			respawnTimer, saveTimer = 0, teleBlockLength, poisonDelay;
	public long lastPlayerMove, lastPoison, lastPoisonSip, poisonImmune, lastSpear, lastProtItem, lastVeng, lastYell,
			teleGrabDelay, protMageDelay, protMeleeDelay, protRangeDelay, lastAction, lastThieve, lastLockPick,
			alchDelay, specDelay = System.currentTimeMillis(), duelDelay, teleBlockDelay, godSpellDelay,
			singleCombatDelay, singleCombatDelay2, reduceStat, restoreStatsDelay, logoutDelay, buryDelay, foodDelay,
			potDelay;
	public boolean canChangeAppearance = false;
	public boolean mageAllowed;
	public byte poisonMask = 0;
	public boolean[] curseActive = { false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false };
	public int focusPointX = -1, focusPointY = -1;
	public long lastChat, lastRandom, lastCaught = 0, lastAttacked, homeTeleTime, lastDagChange = -1, reportDelay,
			lastPlant, objectTimer, npcTimer, lastEss, lastClanMessage;
	public int DirectionCount = 0;
	public boolean hitUpdateRequired2;
	public boolean isDead = false;
	public boolean randomEvent = false;
	public boolean FirstClickRunning = false;
	public boolean protectItem = false;

	public boolean isPrayerDisabled() {
		return prayerDisabled;
	}

	public void setPrayerDisabled(boolean prayerDisabled) {
		this.prayerDisabled = prayerDisabled;
	}

	public void playerWalk(int x, int y) {
		PathFinder.getPathFinder().findRoute(this, x, y, true, 1, 1);
	}

	/**
	 * Music
	 */
	public boolean isLoopingMusic = true;
	public int auto = 1;
	/*
	 * Demon Slayer
	 */
	public int demonSlayer = 0;
	public int Incantation = 0;
	public boolean DSOption = false;
	public boolean DSOption2 = false;
	public boolean DSOption3 = false;
	public boolean DSOption4 = false;
	public boolean DSOption5 = false;
	public boolean DSOption6 = false;
	public boolean DSOption7 = false;
	public boolean DSOption8 = false;
	public boolean incantationOption = false;
	public boolean boneSearch = false;
	public boolean captainRovin = false;
	public boolean prysin = false;
	public boolean trailborn = false;

	public boolean openDuel = false;

	/**
	 * Clan Chat Variables
	 */
	public String clanName, properName;
	public String lastClanChat = "";

	public void faceNPC(int index) {
		faceNPC = index;
		faceNPCupdate = true;
		updateRequired = true;
	}

	protected boolean faceNPCupdate = false;
	public int faceNPC = -1;

	public void appendFaceNPCUpdate(Stream str) {
		str.writeWordBigEndian(faceNPC);
	}

	public final int[] BOWS = { 9185, 839, 845, 847, 851, 855, 859, 841, 843, 849, 853, 857, 861, 4212, 4214, 4215,
			11235, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 6724, 4734, 4934, 4935, 4936, 4937 };
	public final int[] ARROWS = { 882, 884, 886, 888, 890, 892, 4740, 11212, 9140, 9141, 4142, 9143, 9144, 9240, 9241,
			9242, 9243, 9244, 9245 };
	public final int[] NO_ARROW_DROP = { 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 4734, 4934,
			4935, 4936, 4937 };
	public final int[] OTHER_RANGE_WEAPONS = { 863, 864, 865, 866, 867, 868, 869, 806, 807, 808, 809, 810, 811, 825,
			826, 827, 828, 829, 830, 800, 801, 802, 803, 804, 805, 6522 };

	public final int[][] MAGIC_SPELLS = {
			// example {magicId, level req, animation, startGFX, projectile Id,
			// endGFX, maxhit, exp gained, rune 1, rune 1 amount, rune 2, rune 2
			// amount, rune 3, rune 3 amount, rune 4, rune 4 amount}

			// Modern Spells
			{ 1152, 1, 711, 90, 91, 92, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0 }, // wind
																			// strike
			{ 1154, 5, 711, 93, 94, 95, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0 }, // water
																				// strike
			{ 1156, 9, 711, 96, 97, 98, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0 }, // earth
																				// strike
			{ 1158, 13, 711, 99, 100, 101, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0 }, // fire
																					// strike
			{ 1160, 17, 711, 117, 118, 119, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0 }, // wind
																					// bolt
			{ 1163, 23, 711, 120, 121, 122, 10, 16, 556, 2, 555, 2, 562, 1, 0, 0 }, // water
																					// bolt
			{ 1166, 29, 711, 123, 124, 125, 11, 20, 556, 2, 557, 3, 562, 1, 0, 0 }, // earth
																					// bolt
			{ 1169, 35, 711, 126, 127, 128, 12, 22, 556, 3, 554, 4, 562, 1, 0, 0 }, // fire
																					// bolt
			{ 1172, 41, 711, 132, 133, 134, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0 }, // wind
																					// blast
			{ 1175, 47, 711, 135, 136, 137, 14, 28, 556, 3, 555, 3, 560, 1, 0, 0 }, // water
																					// blast
			{ 1177, 53, 711, 138, 139, 140, 15, 31, 556, 3, 557, 4, 560, 1, 0, 0 }, // earth
																					// blast
			{ 1181, 59, 711, 129, 130, 131, 16, 35, 556, 4, 554, 5, 560, 1, 0, 0 }, // fire
																					// blast
			{ 1183, 62, 711, 158, 159, 160, 17, 36, 556, 5, 565, 1, 0, 0, 0, 0 }, // wind
																					// wave
			{ 1185, 65, 711, 161, 162, 163, 18, 37, 556, 5, 555, 7, 565, 1, 0, 0 }, // water
																					// wave
			{ 1188, 70, 711, 164, 165, 166, 19, 40, 556, 5, 557, 7, 565, 1, 0, 0 }, // earth
																					// wave
			{ 1189, 75, 711, 155, 156, 157, 20, 42, 556, 5, 554, 7, 565, 1, 0, 0 }, // fire
																					// wave
			{ 1153, 3, 716, 102, 103, 104, 0, 13, 555, 3, 557, 2, 559, 1, 0, 0 }, // confuse
			{ 1157, 11, 716, 105, 106, 107, 0, 20, 555, 3, 557, 2, 559, 1, 0, 0 }, // weaken
			{ 1161, 19, 716, 108, 109, 110, 0, 29, 555, 2, 557, 3, 559, 1, 0, 0 }, // curse
			{ 1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0 }, // vulnerability
			{ 1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0 }, // enfeeble
			{ 1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0, 0 }, // stun
			{ 1572, 20, 711, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0 }, // bind
			{ 1582, 50, 711, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0 }, // snare
			{ 1592, 79, 711, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0 }, // entangle
			{ 1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0, 0 }, // crumble
																					// undead
			{ 1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0 }, // iban
																				// blast
			{ 12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0, 0 }, // magic
																					// dart
			{ 1190, 60, 811, 0, 0, 76, 20, 60, 554, 2, 565, 2, 556, 4, 0, 0 }, // sara
																				// strike
			{ 1191, 60, 811, 0, 0, 77, 20, 60, 554, 1, 565, 2, 556, 4, 0, 0 }, // cause
																				// of
																				// guthix
			{ 1192, 60, 811, 0, 0, 78, 20, 60, 554, 4, 565, 2, 556, 1, 0, 0 }, // flames
																				// of
																				// zammy
			{ 12445, 85, 1819, 0, 344, 345, 0, 65, 563, 1, 562, 1, 560, 1, 0, 0 }, // teleblock

			// Ancient Spells
			{ 12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1, 556, 1 }, // smoke
																						// rush
			{ 12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1, 556, 1 }, // shadow
																						// rush
			{ 12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0 }, // blood
																					// rush
			{ 12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0, 0 }, // ice
																					// rush
			{ 12963, 62, 1979, 0, 0, 389, 19, 36, 560, 2, 562, 4, 556, 2, 554, 2 }, // smoke
																					// burst
			{ 13011, 64, 1979, 0, 0, 382, 20, 37, 560, 2, 562, 4, 556, 2, 566, 2 }, // shadow
																					// burst
			{ 12919, 68, 1979, 0, 0, 376, 21, 39, 560, 2, 562, 4, 565, 2, 0, 0 }, // blood
																					// burst
			{ 12881, 70, 1979, 0, 0, 363, 22, 40, 560, 2, 562, 4, 555, 4, 0, 0 }, // ice
																					// burst
			{ 12951, 74, 1978, 0, 386, 387, 23, 42, 560, 2, 554, 2, 565, 2, 556, 2 }, // smoke
																						// blitz
			{ 12999, 76, 1978, 0, 380, 381, 24, 43, 560, 2, 565, 2, 556, 2, 566, 2 }, // shadow
																						// blitz
			{ 12911, 80, 1978, 0, 374, 375, 25, 45, 560, 2, 565, 4, 0, 0, 0, 0 }, // blood
																					// blitz
			{ 12871, 82, 1978, 366, 0, 367, 26, 46, 560, 2, 565, 2, 555, 3, 0, 0 }, // ice
																					// blitz
			{ 12975, 86, 1979, 0, 0, 391, 27, 48, 560, 4, 565, 2, 556, 4, 554, 4 }, // smoke
																					// barrage
			{ 13023, 88, 1979, 0, 0, 383, 28, 49, 560, 4, 565, 2, 556, 4, 566, 3 }, // shadow
																					// barrage
			{ 12929, 92, 1979, 0, 0, 377, 29, 51, 560, 4, 565, 4, 566, 1, 0, 0 }, // blood
																					// barrage
			{ 12891, 94, 1979, 0, 0, 369, 30, 52, 560, 4, 565, 2, 555, 6, 0, 0 }, // ice
																					// barrage

			{ -1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0 }, // charge
			{ -1, 21, 712, 112, 0, 0, 0, 10, 554, 3, 561, 1, 0, 0, 0, 0 }, // low
																			// alch
			{ -1, 55, 713, 113, 0, 0, 0, 20, 554, 5, 561, 1, 0, 0, 0, 0 }, // high
																			// alch
			{ -1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0 } // telegrab

	};

	public Player(Channel s, int _playerId) {
		this(_playerId);
		this.session = s;
		synchronized (this) {
			outStream = new Stream(new byte[GameConstants.BUFFER_SIZE]);
			outStream.currentOffset = 0;

			inStream = new Stream(new byte[GameConstants.BUFFER_SIZE]);
			inStream.currentOffset = 0;
			buffer = new byte[GameConstants.BUFFER_SIZE];
		}

	}

	public boolean isAutoButton(int button) {
		for (int j = 0; j < autocastIds.length; j += 2) {
			if (autocastIds[j] == button)
				return true;
		}
		return false;
	}

	public int[] autocastIds = { 51133, 32, 51185, 33, 51091, 34, 24018, 35, 51159, 36, 51211, 37, 51111, 38, 51069, 39,
			51146, 40, 51198, 41, 51102, 42, 51058, 43, 51172, 44, 51224, 45, 51122, 46, 51080, 47, 7038, 0, 7039, 1,
			7040, 2, 7041, 3, 7042, 4, 7043, 5, 7044, 6, 7045, 7, 7046, 8, 7047, 9, 7048, 10, 7049, 11, 7050, 12, 7051,
			13, 7052, 14, 7053, 15, 47019, 27, 47020, 25, 47021, 12, 47022, 13, 47023, 14, 47024, 15 };

	// public String spellName = "Select Spell";
	public void assignAutocast(int button) {
		for (int j = 0; j < autocastIds.length; j++) {
			if (autocastIds[j] == button) {
				Player c = PlayerHandler.players[this.playerId];
				autocasting = true;
				autocastId = autocastIds[j + 1];
				c.getActionSender().sendFrame36(108, 1);
				c.getActionSender().setSidebarInterface(0, 328);
				// spellName = getSpellName(autocastId);
				// spellName = spellName;
				// c.getPA().textOnInterface(spellName, 354);
				c = null;
				break;
			}
		}
	}

	public String getSpellName(int id) {
		switch (id) {
		case 0:
			return "Air Strike";
		case 1:
			return "Water Strike";
		case 2:
			return "Earth Strike";
		case 3:
			return "Fire Strike";
		case 4:
			return "Air Bolt";
		case 5:
			return "Water Bolt";
		case 6:
			return "Earth Bolt";
		case 7:
			return "Fire Bolt";
		case 8:
			return "Air Blast";
		case 9:
			return "Water Blast";
		case 10:
			return "Earth Blast";
		case 11:
			return "Fire Blast";
		case 12:
			return "Air Wave";
		case 13:
			return "Water Wave";
		case 14:
			return "Earth Wave";
		case 15:
			return "Fire Wave";
		case 32:
			return "Shadow Rush";
		case 33:
			return "Smoke Rush";
		case 34:
			return "Blood Rush";
		case 35:
			return "Ice Rush";
		case 36:
			return "Shadow Burst";
		case 37:
			return "Smoke Burst";
		case 38:
			return "Blood Burst";
		case 39:
			return "Ice Burst";
		case 40:
			return "Shadow Blitz";
		case 41:
			return "Smoke Blitz";
		case 42:
			return "Blood Blitz";
		case 43:
			return "Ice Blitz";
		case 44:
			return "Shadow Barrage";
		case 45:
			return "Smoke Barrage";
		case 46:
			return "Blood Barrage";
		case 47:
			return "Ice Barrage";
		default:
			return "Select Spell";
		}
	}

	public int reduceSpellId;
	public final int[] REDUCE_SPELL_TIME = { 250000, 250000, 250000, 500000, 500000, 500000 }; // how
																								// long
																								// does
																								// the
																								// other
																								// player
																								// stay
																								// immune
																								// to
																								// the
																								// spell
	public long[] reduceSpellDelay = new long[6];
	public final int[] REDUCE_SPELLS = { 1153, 1157, 1161, 1542, 1543, 1562 };
	public boolean[] canUseReducingSpell = { true, true, true, true, true, true };

	public int prayerId = -1;
	public int headIcon = -1;
	public int bountyIcon = 0;
	public long stopPrayerDelay, prayerDelay;
	public boolean usingPrayer;
	public final int[] PRAYER_DRAIN_RATE = { 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
			500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500 };
	public final int[] PRAYER_LEVEL_REQUIRED = { 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 37, 40, 43,
			44, 45, 46, 49, 52, 60, 70 };
	public final int[] PRAYER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25 };
	public final String[] PRAYER_NAME = { "Thick Skin", "Burst of Strength", "Clarity of Thought", "Sharp Eye",
			"Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes", "Rapid Restore", "Rapid Heal",
			"Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength", "Incredible Reflexes",
			"Protect from Magic", "Protect from Missiles", "Protect from Melee", "Eagle Eye", "Mystic Might",
			"Retribution", "Redemption", "Smite", "Chivalry", "Piety" };
	public final int[] PRAYER_GLOW = { 83, 84, 85, 700, 701, 86, 87, 88, 89, 90, 91, 702, 703, 92, 93, 94, 95, 96, 97,
			704, 705, 98, 99, 100, 706, 707 };
	public final int[] PRAYER_HEAD_ICONS = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 0,
			-1, -1, 3, 5, 4, -1, -1 };

	public boolean[] prayerActive = { false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };

	public int duelTimer, duelTeleX, duelTeleY, duelSlot, duelSpaceReq, duelOption, duelingWith, duelStatus;
	public int headIconPk = -1, headIconHints;
	public boolean duelRequested;
	public boolean[] duelRule = new boolean[22];
	public final int[] DUEL_RULE_ID = { 1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096, 8192, 16384, 32768, 65536, 131072,
			262144, 524288, 2097152, 8388608, 16777216, 67108864, 134217728 };

	public boolean doubleHit, usingSpecial, npcDroppingItems, usingRangeWeapon, usingBow, usingMagic, castingMagic;
	public int specMaxHitIncrease, freezeDelay, freezeTimer = -6, killerId, playerIndex, oldPlayerIndex, lastWeaponUsed,
			projectileStage, crystalBowArrowCount, playerMagicBook, teleGfx, teleEndAnimation, teleHeight, teleX, teleY,
			rangeItemUsed, killingNpcIndex, totalDamageDealt, oldNpcIndex, fightMode, attackTimer, npcIndex,
			npcClickIndex, npcType, castingSpellId, oldSpellId, spellId, hitDelay;
	public boolean magicFailed, oldMagicFailed;
	public int bowSpecShot, clickNpcType, clickObjectType, objectId, objectX, objectY, objectXOffset, objectYOffset,
			objectDistance;
	public int pItemX, pItemY, pItemId;
	public boolean isMoving, walkingToItem;
	public boolean isShopping, updateShop;
	public int myShopId, totalPlayerDamageDealt;
	public int tradeStatus, tradeWith;
	public boolean tradeAccepted, goodTrade, tradeRequested, tradeResetNeeded, tradeConfirmed, tradeConfirmed2,
			canOffer, acceptTrade, acceptedTrade;
	public int attackAnim, animationRequest = -1, animationWaitCycles;
	public int[] playerBonus = new int[12];
	public boolean isRunning2 = true;
	public boolean takeAsNote;
	public int combatLevel;
	public boolean saveFile = false;
	public int playerAppearance[] = new int[13];
	public int apset;
	public int actionID;
	public int wearItemTimer, wearId, wearSlot, interfaceId;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;
	public boolean antiFirePot = false;

	public void updateSigns() {
		if (inWild()) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			getActionSender().walkableInterface(197);
			if (Configuration.SINGLE_AND_MULTI_ZONES) {
				if (inMulti()) {
					getActionSender().textOnInterface("@yel@Level: " + wildLevel, 199);
				} else {
					getActionSender().textOnInterface("@yel@Level: " + wildLevel, 199);
				}
			} else {
				getActionSender().multiWay(-1);
				getActionSender().textOnInterface("@yel@Level: " + wildLevel, 199);
			}
			getActionSender().showOption(3, 0, "Attack", 1);
		} else if (inDuelArena()) {
			getActionSender().walkableInterface(201);
			if (duelStatus == 5) {
				getActionSender().showOption(3, 0, "Attack", 1);
			} else {
				getActionSender().showOption(3, 0, "Challenge", 1);
			}
		}
		if (!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getActionSender().multiWay(1);
		}

		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getActionSender().multiWay(-1);
		}
	}

	/**
	 * SouthWest, NorthEast, SouthWest, NorthEast
	 */

	public boolean inArea(int x, int y, int x1, int y1) {
		if (absX > x && absX < x1 && absY < y && absY > y1) {
			return true;
		}
		return false;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}

	public boolean arenas() {
		if (absX > 3331 && absX < 3391 && absY > 3242 && absY < 3260) {
			return true;
		}
		return false;
	}

	public boolean inDuelArena() {
		if ((absX > 3322 && absX < 3394 && absY > 3195 && absY < 3291)
				|| (absX > 3311 && absX < 3323 && absY > 3223 && absY < 3248)) {
			return true;
		}
		return false;
	}

	public boolean inMulti() {
		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)) {
			return true;
		}
		return false;
	}

	public boolean samePlayer() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == playerId)
				continue;
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName)) {
					disconnected = true;
					return true;
				}
			}
		}
		return false;
	}

	public void putInCombat(int attacker) {
		underAttackBy = attacker;
		logoutDelay = System.currentTimeMillis();
		singleCombatDelay = System.currentTimeMillis();
	}

	public int[] damageTaken = new int[GameConstants.MAX_PLAYERS];

	public boolean inHelpCc = false;
	private boolean isBusyHP = false;
	public boolean isBusyFollow = false;
	public boolean teleporting = false;

	public boolean checkBusyHP() {
		return isBusyHP;
	}

	public boolean checkBusyFollow() {
		return isBusyFollow;
	}

	public boolean isBusy() {
		if (this.isShopping || (Boolean) this.getAttributes().get("isBanking")) {
			return true;
		}
		return false;
	}

	public void setBusyFollow(boolean isBusyFollow) {
		this.isBusyFollow = isBusyFollow;
	}

	public void setBusyHP(boolean isBusyHP) {
		this.isBusyHP = isBusyHP;
	}

	public boolean isBusyHP() {
		return isBusyHP;
	}

	public boolean isBusyFollow() {
		return isBusyFollow;
	}

	public boolean canWalk = true;

	public boolean canWalk() {
		return canWalk;
	}

	public void setCanWalk(boolean canWalk) {
		this.canWalk = canWalk;
	}

	public ActionSender getPlayerAssistant() {
		return actionSender;
	}

	public void queueMessage(Packet arg1) {
		synchronized (queuedPackets) {
			queuedPackets.add(arg1);
		}
	}

	public int getMapRegionX() {
		return mapRegionX;
	}

	public int getMapRegionY() {
		return mapRegionY;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	/**
	 * Used for clipping
	 */
	public int getLocalX() {
		return getX() - 8 * getMapRegionX();
	}

	/**
	 * Used for clipping
	 */
	public int getLocalY() {
		return getY() - 8 * getMapRegionY();
	}

	public int getId() {
		return playerId;
	}

	public ContentManager getContentManager() {
		return contentManager;
	}

	public void setContentManager(ContentManager contentManager) {
		this.contentManager = contentManager;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public boolean processQueuedPackets() {
		synchronized (queuedPackets) {
			Packet p = null;
			while ((p = queuedPackets.poll()) != null) {
				inStream.currentOffset = 0;
				packetType = p.getOpcode();
				packetSize = p.getLength();
				inStream.buffer = p.getPayload().array();
				if (packetType > 0) {
					PacketHandler.processPacket(this, packetType, packetSize);
				}
			}
		}
		return true;
	}

	public synchronized Stream getOutStream() {
		return outStream;
	}

	private final Scheduler scheduler = new Scheduler();

	public Scheduler getScheduler() {
		return scheduler;
	}

	public static void schedule(Player player, Tick tick) {
		player.getScheduler().schedule(tick);
	}

	public void setCurrentTask(Future<?> task) {
		currentTask = task;
	}

	public Future<?> getCurrentTask() {
		return currentTask;
	}

	public synchronized Stream getInStream() {
		return inStream;
	}

	public synchronized int getPacketType() {
		return packetType;
	}

	public synchronized int getPacketSize() {
		return packetSize;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public ActionSender getActionSender() {
		return actionSender;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public ShopAssistant getShops() {
		return shopAssistant;
	}

	public CombatAssistant getCombat() {
		return combatAssistant;
	}

	public ActionsPerformed getActions() {
		return actionHandler;
	}

	public PlayerKilling getKill() {
		return playerKilling;
	}

	public Channel getSession() {
		return session;
	}

	public String connectedFrom = "";
	public String globalMessage = "";

	public int playerId = -1;
	public String playerName = null;
	public String playerName2 = null;
	public String playerPass = null;
	public PlayerHandler handler = null;
	public int playerItems[] = new int[28];
	public int playerItemsN[] = new int[28];
	public int bankItems[] = new int[GameConstants.BANK_SIZE];
	public int bankItemsN[] = new int[GameConstants.BANK_SIZE];
	public boolean bankNotes = false;

	public int playerStandIndex = 0x328;
	public int playerTurnIndex = 0x337;
	public int playerWalkIndex = 0x333;
	public int playerTurn180Index = 0x334;
	public int playerTurn90CWIndex = 0x335;
	public int playerTurn90CCWIndex = 0x336;
	public int playerRunIndex = 0x338;

	public int playerHat = 0;
	public int playerCape = 1;
	public int playerAmulet = 2;
	public int playerWeapon = 3;
	public int playerChest = 4;
	public int playerShield = 5;
	public int playerLegs = 7;
	public int playerHands = 9;
	public int playerFeet = 10;
	public int playerRing = 12;
	public int playerArrows = 13;

	public int playerAttack = 0;
	public int playerDefence = 1;
	public int playerStrength = 2;
	public int playerHitpoints = 3;
	public int playerRanged = 4;
	public int playerPrayer = 5;
	public int playerMagic = 6;
	public int playerCooking = 7;
	public int playerWoodcutting = 8;
	public int playerFletching = 9;
	public int playerFishing = 10;
	public int playerFiremaking = 11;
	public int playerCrafting = 12;
	public int playerSmithing = 13;
	public int playerMining = 14;
	public int playerHerblore = 15;
	public int playerAgility = 16;
	public int playerThieving = 17;
	public int playerSlayer = 18;
	public int playerFarming = 19;
	public int playerRunecrafting = 20;

	public int[] playerEquipment = new int[14];
	public int[] playerEquipmentN = new int[14];
	public int[] playerLevel = new int[25];
	public int[] playerXP = new int[25];

	public void updateshop(int i) {
		Player p = PlayerHandler.players[playerId];
		p.getShops().resetShop(i);
	}

	public void println_debug(String str) {
		System.out.println("[player-" + playerId + "]: " + str);
	}

	public void println(String str) {
		System.out.println("[player-" + playerId + "]: " + str);
	}

	public Player(int _playerId) {
		playerId = _playerId;
		this.setRights(Rights.PLAYER);

		for (int i = 0; i < playerItems.length; i++) {
			playerItems[i] = 0;
		}
		for (int i = 0; i < playerItemsN.length; i++) {
			playerItemsN[i] = 0;
		}

		for (int i = 0; i < playerLevel.length; i++) {
			if (i == 3) {
				playerLevel[i] = 10;
			} else {
				playerLevel[i] = 1;
			}
		}

		for (int i = 0; i < playerXP.length; i++) {
			if (i == 3) {
				playerXP[i] = 1300;
			} else {
				playerXP[i] = 0;
			}
		}
		for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
			bankItems[i] = 0;
		}

		for (int i = 0; i < GameConstants.BANK_SIZE; i++) {
			bankItemsN[i] = 0;
		}

		playerAppearance[0] = 0; // gender
		playerAppearance[1] = 7; // head
		playerAppearance[2] = 25;// Torso
		playerAppearance[3] = 29; // arms
		playerAppearance[4] = 35; // hands
		playerAppearance[5] = 39; // legs
		playerAppearance[6] = 44; // feet
		playerAppearance[7] = 14; // beard
		playerAppearance[8] = 7; // hair colour
		playerAppearance[9] = 8; // torso colour
		playerAppearance[10] = 9; // legs colour
		playerAppearance[11] = 5; // feet colour
		playerAppearance[12] = 0; // skin colour

		apset = 0;
		actionID = 0;

		playerEquipment[playerHat] = -1;
		playerEquipment[playerCape] = -1;
		playerEquipment[playerAmulet] = -1;
		playerEquipment[playerChest] = -1;
		playerEquipment[playerShield] = -1;
		playerEquipment[playerLegs] = -1;
		playerEquipment[playerHands] = -1;
		playerEquipment[playerFeet] = -1;
		playerEquipment[playerRing] = -1;
		playerEquipment[playerArrows] = -1;
		playerEquipment[playerWeapon] = -1;

		heightLevel = 0;

		teleportToX = GameConstants.START_LOCATION_X;
		teleportToY = GameConstants.START_LOCATION_Y;

		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;

		initAttributes();

		resetWalkingQueue();
	}

	void destruct1() {
		PlayerSave.saveGame(this);
		playerListSize = 0;
		for (int i = 0; i < maxPlayerListSize; i++)
			playerList[i] = null;
		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
	}

	public int[] keepItems = new int[4];
	public int[] keepItemsN = new int[4];
	public int WillKeepAmt1, WillKeepAmt2, WillKeepAmt3, WillKeepAmt4, WillKeepItem1, WillKeepItem2, WillKeepItem3,
			WillKeepItem4, WillKeepItem1Slot, WillKeepItem2Slot, WillKeepItem3Slot, WillKeepItem4Slot, EquipStatus;

	public void ResetKeepItems() {
		WillKeepAmt1 = -1;
		WillKeepItem1 = -1;
		WillKeepAmt2 = -1;
		WillKeepItem2 = -1;
		WillKeepAmt3 = -1;
		WillKeepItem3 = -1;
		WillKeepAmt4 = -1;
		WillKeepItem4 = -1;
	}

	public static int seconds, minutes, hours, days;

	public void StartBestItemScan(Player c) {
		if ((Boolean) this.getAttributes().get("isSkulled") && !c.prayerActive[10]) {
			ItemKeptInfo(c, 0);
			return;
		}
		FindItemKeptInfo(c);
		ResetKeepItems();
		BestItem1(c);
	}

	public void FindItemKeptInfo(Player c) {
		if ((Boolean) this.getAttributes().get("isSkulled") && c.prayerActive[10])
			ItemKeptInfo(this, 1);
		else if (!(Boolean) this.getAttributes().get("isSkulled") && !c.prayerActive[10])
			ItemKeptInfo(this, 3);
		else if (!(Boolean) this.getAttributes().get("isSkulled") && c.prayerActive[10])
			ItemKeptInfo(this, 4);
	}

	public void ItemKeptInfo(Player c, int Lose) {
		for (int i = 17109; i < 17131; i++) {
			c.getActionSender().textOnInterface("", i);
		}
		c.getActionSender().textOnInterface("Items you will keep on death:", 17104);
		c.getActionSender().textOnInterface("Items you will lose on death:", 17105);
		c.getActionSender().textOnInterface("Player Information", 17106);
		c.getActionSender().textOnInterface("Max items kept on death:", 17107);
		c.getActionSender().textOnInterface("~ " + Lose + " ~", 17108);
		c.getActionSender().textOnInterface("The normal amount of", 17111);
		c.getActionSender().textOnInterface("items kept is three.", 17112);
		switch (Lose) {
		case 0:
		default:
			c.getActionSender().textOnInterface("Items you will keep on death:", 17104);
			c.getActionSender().textOnInterface("Items you will lose on death:", 17105);
			c.getActionSender().textOnInterface("You're marked with a", 17111);
			c.getActionSender().textOnInterface("@red@skull. @lre@This reduces the", 17112);
			c.getActionSender().textOnInterface("items you keep from", 17113);
			c.getActionSender().textOnInterface("three to zero!", 17114);
			break;
		case 1:
			c.getActionSender().textOnInterface("Items you will keep on death:", 17104);
			c.getActionSender().textOnInterface("Items you will lose on death:", 17105);
			c.getActionSender().textOnInterface("You're marked with a", 17111);
			c.getActionSender().textOnInterface("@red@skull. @lre@This reduces the", 17112);
			c.getActionSender().textOnInterface("items you keep from", 17113);
			c.getActionSender().textOnInterface("three to zero!", 17114);
			c.getActionSender().textOnInterface("However, you also have", 17115);
			c.getActionSender().textOnInterface("the @red@Protect @lre@Items prayer", 17116);
			c.getActionSender().textOnInterface("active, which saves you", 17117);
			c.getActionSender().textOnInterface("one extra item!", 17118);
			break;
		case 3:
			c.getActionSender().textOnInterface("Items you will keep on death(if not skulled):", 17104);
			c.getActionSender().textOnInterface("Items you will lose on death(if not skulled):", 17105);
			c.getActionSender().textOnInterface("You have no factors", 17111);
			c.getActionSender().textOnInterface("affecting the items you", 17112);
			c.getActionSender().textOnInterface("keep.", 17113);
			break;
		case 4:
			c.getActionSender().textOnInterface("Items you will keep on death(if not skulled):", 17104);
			c.getActionSender().textOnInterface("Items you will lose on death(if not skulled):", 17105);
			c.getActionSender().textOnInterface("You have the @red@Protect", 17111);
			c.getActionSender().textOnInterface("@red@Item @lre@prayer active,", 17112);
			c.getActionSender().textOnInterface("which saves you one", 17113);
			c.getActionSender().textOnInterface("extra item!", 17114);
			break;
		}
	}

	public void BestItem1(Player c) {
		int BestValue = 0;
		int NextValue = 0;
		int ItemsContained = 0;
		WillKeepItem1 = 0;
		WillKeepItem1Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				ItemsContained += 1;
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue) {
					BestValue = NextValue;
					WillKeepItem1 = playerItems[ITEM] - 1;
					WillKeepItem1Slot = ITEM;
					if (playerItemsN[ITEM] > 2 && !c.prayerActive[10]) {
						WillKeepAmt1 = 3;
					} else if (playerItemsN[ITEM] > 3 && c.prayerActive[10]) {
						WillKeepAmt1 = 4;
					} else {
						WillKeepAmt1 = playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				ItemsContained += 1;
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue) {
					BestValue = NextValue;
					WillKeepItem1 = playerEquipment[EQUIP];
					WillKeepItem1Slot = EQUIP + 28;
					if (playerEquipmentN[EQUIP] > 2 && !c.prayerActive[10]) {
						WillKeepAmt1 = 3;
					} else if (playerEquipmentN[EQUIP] > 3 && c.prayerActive[10]) {
						WillKeepAmt1 = 4;
					} else {
						WillKeepAmt1 = playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!(Boolean) this.getAttributes().get("isSkulled") && ItemsContained > 1
				&& (WillKeepAmt1 < 3 || (c.prayerActive[10] && WillKeepAmt1 < 4))) {
			BestItem2(c, ItemsContained);
		}
	}

	public void BestItem2(Player c, int ItemsContained) {
		int BestValue = 0;
		int NextValue = 0;
		WillKeepItem2 = 0;
		WillKeepItem2Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1)) {
					BestValue = NextValue;
					WillKeepItem2 = playerItems[ITEM] - 1;
					WillKeepItem2Slot = ITEM;
					if (playerItemsN[ITEM] > 2 - WillKeepAmt1 && !c.prayerActive[10]) {
						WillKeepAmt2 = 3 - WillKeepAmt1;
					} else if (playerItemsN[ITEM] > 3 - WillKeepAmt1 && c.prayerActive[10]) {
						WillKeepAmt2 = 4 - WillKeepAmt1;
					} else {
						WillKeepAmt2 = playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1)) {
					BestValue = NextValue;
					WillKeepItem2 = playerEquipment[EQUIP];
					WillKeepItem2Slot = EQUIP + 28;
					if (playerEquipmentN[EQUIP] > 2 - WillKeepAmt1 && !c.prayerActive[10]) {
						WillKeepAmt2 = 3 - WillKeepAmt1;
					} else if (playerEquipmentN[EQUIP] > 3 - WillKeepAmt1 && c.prayerActive[10]) {
						WillKeepAmt2 = 4 - WillKeepAmt1;
					} else {
						WillKeepAmt2 = playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!(Boolean) this.getAttributes().get("isSkulled") && ItemsContained > 2
				&& (WillKeepAmt1 + WillKeepAmt2 < 3 || (c.prayerActive[10] && WillKeepAmt1 + WillKeepAmt2 < 4))) {
			BestItem3(c, ItemsContained);
		}
	}

	public void BestItem3(Player c, int ItemsContained) {
		int BestValue = 0;
		int NextValue = 0;
		WillKeepItem3 = 0;
		WillKeepItem3Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1)
						&& !(ITEM == WillKeepItem2Slot && playerItems[ITEM] - 1 == WillKeepItem2)) {
					BestValue = NextValue;
					WillKeepItem3 = playerItems[ITEM] - 1;
					WillKeepItem3Slot = ITEM;
					if (playerItemsN[ITEM] > 2 - (WillKeepAmt1 + WillKeepAmt2) && !c.prayerActive[10]) {
						WillKeepAmt3 = 3 - (WillKeepAmt1 + WillKeepAmt2);
					} else if (playerItemsN[ITEM] > 3 - (WillKeepAmt1 + WillKeepAmt2) && c.prayerActive[10]) {
						WillKeepAmt3 = 4 - (WillKeepAmt1 + WillKeepAmt2);
					} else {
						WillKeepAmt3 = playerItemsN[ITEM];
					}
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1)
						&& !(EQUIP + 28 == WillKeepItem2Slot && playerEquipment[EQUIP] == WillKeepItem2)) {
					BestValue = NextValue;
					WillKeepItem3 = playerEquipment[EQUIP];
					WillKeepItem3Slot = EQUIP + 28;
					if (playerEquipmentN[EQUIP] > 2 - (WillKeepAmt1 + WillKeepAmt2) && !c.prayerActive[10]) {
						WillKeepAmt3 = 3 - (WillKeepAmt1 + WillKeepAmt2);
					} else if (playerEquipmentN[EQUIP] > 3 - WillKeepAmt1 && c.prayerActive[10]) {
						WillKeepAmt3 = 4 - (WillKeepAmt1 + WillKeepAmt2);
					} else {
						WillKeepAmt3 = playerEquipmentN[EQUIP];
					}
				}
			}
		}
		if (!(Boolean) this.getAttributes().get("isSkulled") && ItemsContained > 3 && c.prayerActive[10]
				&& ((WillKeepAmt1 + WillKeepAmt2 + WillKeepAmt3) < 4)) {
			BestItem4(c);
		}
	}

	public void BestItem4(Player c) {
		int BestValue = 0;
		int NextValue = 0;
		WillKeepItem4 = 0;
		WillKeepItem4Slot = 0;
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (playerItems[ITEM] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(playerItems[ITEM] - 1));
				if (NextValue > BestValue && !(ITEM == WillKeepItem1Slot && playerItems[ITEM] - 1 == WillKeepItem1)
						&& !(ITEM == WillKeepItem2Slot && playerItems[ITEM] - 1 == WillKeepItem2)
						&& !(ITEM == WillKeepItem3Slot && playerItems[ITEM] - 1 == WillKeepItem3)) {
					BestValue = NextValue;
					WillKeepItem4 = playerItems[ITEM] - 1;
					WillKeepItem4Slot = ITEM;
				}
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (playerEquipment[EQUIP] > 0) {
				NextValue = (int) Math.floor(c.getShops().getItemShopValue(playerEquipment[EQUIP]));
				if (NextValue > BestValue
						&& !(EQUIP + 28 == WillKeepItem1Slot && playerEquipment[EQUIP] == WillKeepItem1)
						&& !(EQUIP + 28 == WillKeepItem2Slot && playerEquipment[EQUIP] == WillKeepItem2)
						&& !(EQUIP + 28 == WillKeepItem3Slot && playerEquipment[EQUIP] == WillKeepItem3)) {
					BestValue = NextValue;
					WillKeepItem4 = playerEquipment[EQUIP];
					WillKeepItem4Slot = EQUIP + 28;
				}
			}
		}
	}

	public static final int maxPlayerListSize = GameConstants.MAX_PLAYERS;
	public Player playerList[] = new Player[maxPlayerListSize];
	public int playerListSize = 0;

	public byte playerInListBitmap[] = new byte[(GameConstants.MAX_PLAYERS + 7) >> 3];

	public static final int maxNPCListSize = MobHandler.maxNPCs;
	public Mob npcList[] = new Mob[maxNPCListSize];
	public int npcListSize = 0;

	public byte npcInListBitmap[] = new byte[(MobHandler.maxNPCs + 7) >> 3];

	public boolean withinDistance(Player otherPlr) {
		if (heightLevel != otherPlr.heightLevel)
			return false;
		int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(Mob npc) {
		if (heightLevel != npc.heightLevel)
			return false;
		if (npc.needRespawn == true)
			return false;
		int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(int absX, int getY, int getHeightLevel) {
		if (this.getHeightLevel() != getHeightLevel)
			return false;
		int deltaX = this.getX() - absX, deltaY = this.getY() - getY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public int getHeightLevel;

	public int getHeightLevel() {
		return getHeightLevel;
	}

	public int distanceToPoint(int pointX, int pointY) {
		return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
	}

	public int mapRegionX, mapRegionY;
	public int currentX, currentY;

	public int heightLevel;
	public int playerSE = 0x328;
	public int playerSEW = 0x333;
	public int playerSER = 0x334;

	public final int walkingQueueSize = 50;
	public int walkingQueueX[] = new int[walkingQueueSize], walkingQueueY[] = new int[walkingQueueSize];
	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	public boolean isRunning = true;
	public int teleportToX = -1, teleportToY = -1;

	public void resetWalkingQueue() {
		wQueueReadPtr = wQueueWritePtr = 0;

		for (int i = 0; i < walkingQueueSize; i++) {
			walkingQueueX[i] = currentX;
			walkingQueueY[i] = currentY;
		}
	}

	public void addToWalkingQueue(int x, int y) {
		// if (VirtualWorld.I(heightLevel, absX, absY, x, y, 0)) {
		int next = (wQueueWritePtr + 1) % walkingQueueSize;
		if (next == wQueueWritePtr)
			return;
		walkingQueueX[wQueueWritePtr] = x;
		walkingQueueY[wQueueWritePtr] = y;
		wQueueWritePtr = next;
		// }
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	public int getNextWalkingDirection() {
		if (wQueueReadPtr == wQueueWritePtr)
			return -1;
		int dir;
		do {
			dir = Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
			if (dir == -1) {
				wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
			} else if ((dir & 1) != 0) {
				println_debug("Invalid waypoint in walking queue!");
				resetWalkingQueue();
				return -1;
			}
		} while ((dir == -1) && (wQueueReadPtr != wQueueWritePtr));
		if (dir == -1)
			return -1;
		dir >>= 1;
		currentX += Misc.directionDeltaX[dir];
		currentY += Misc.directionDeltaY[dir];
		absX += Misc.directionDeltaX[dir];
		absY += Misc.directionDeltaY[dir];
		return dir;
	}

	public boolean didTeleport = false;
	public boolean mapRegionDidChange = false;
	public int dir1 = -1, dir2 = -1;
	public boolean createItems = false;
	public int poimiX = 0, poimiY = 0;

	public synchronized void getNextPlayerMovement() {
		mapRegionDidChange = false;
		didTeleport = false;
		dir1 = dir2 = -1;

		if (teleportToX != -1 && teleportToY != -1) {
			mapRegionDidChange = true;
			if (mapRegionX != -1 && mapRegionY != -1) {
				int relX = teleportToX - mapRegionX * 8, relY = teleportToY - mapRegionY * 8;
				if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8 && relY < 11 * 8)
					mapRegionDidChange = false;
			}
			if (mapRegionDidChange) {
				mapRegionX = (teleportToX >> 3) - 6;
				mapRegionY = (teleportToY >> 3) - 6;
			}
			currentX = teleportToX - 8 * mapRegionX;
			currentY = teleportToY - 8 * mapRegionY;
			absX = teleportToX;
			absY = teleportToY;
			resetWalkingQueue();

			teleportToX = teleportToY = -1;
			didTeleport = true;
		} else {
			dir1 = getNextWalkingDirection();
			if (dir1 == -1)
				return;
			if (isRunning) {
				dir2 = getNextWalkingDirection();
			}
			int deltaX = 0, deltaY = 0;
			if (currentX < 2 * 8) {
				deltaX = 4 * 8;
				mapRegionX -= 4;
				mapRegionDidChange = true;
			} else if (currentX >= 11 * 8) {
				deltaX = -4 * 8;
				mapRegionX += 4;
				mapRegionDidChange = true;
			}
			if (currentY < 2 * 8) {
				deltaY = 4 * 8;
				mapRegionY -= 4;
				mapRegionDidChange = true;
			} else if (currentY >= 11 * 8) {
				deltaY = -4 * 8;
				mapRegionY += 4;
				mapRegionDidChange = true;
			}
			if (mapRegionDidChange) {
				currentX += deltaX;
				currentY += deltaY;
				for (int i = 0; i < walkingQueueSize; i++) {
					walkingQueueX[i] += deltaX;
					walkingQueueY[i] += deltaY;
				}
			}
		}
	}

	public void updateThisPlayerMovement(Stream str) {
		// synchronized(this) {
		if (mapRegionDidChange) {
			str.createFrame(73);
			str.writeWordA(mapRegionX + 6);
			str.writeWord(mapRegionY + 6);
		}

		if (didTeleport) {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);
			str.writeBits(2, 3);
			str.writeBits(2, heightLevel);
			str.writeBits(1, 1);
			str.writeBits(1, (updateRequired) ? 1 : 0);
			str.writeBits(7, currentY);
			str.writeBits(7, currentX);
			return;
		}

		if (dir1 == -1) {
			// don't have to update the character position, because we're just
			// standing
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			isMoving = false;
			if (updateRequired) {
				// tell Player there's an update block appended at the end
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
			if (DirectionCount < 50) {
				DirectionCount++;
			}
		} else {
			DirectionCount = 0;
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);

			if (dir2 == -1) {
				isMoving = true;
				str.writeBits(2, 1);
				str.writeBits(3, Misc.xlateDirectionToPlayer[dir1]);
				if (updateRequired)
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			} else {
				isMoving = true;
				str.writeBits(2, 2);
				str.writeBits(3, Misc.xlateDirectionToPlayer[dir1]);
				str.writeBits(3, Misc.xlateDirectionToPlayer[dir2]);
				if (updateRequired)
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			}
		}

	}

	public void updatePlayerMovement(Stream str) {
		// synchronized(this) {
		if (dir1 == -1) {
			if (updateRequired || isChatTextUpdateRequired()) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else
				str.writeBits(1, 0);
		} else if (dir2 == -1) {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToPlayer[dir1]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 2);
			str.writeBits(3, Misc.xlateDirectionToPlayer[dir1]);
			str.writeBits(3, Misc.xlateDirectionToPlayer[dir2]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		}

	}

	public byte cachedPropertiesBitmap[] = new byte[(GameConstants.MAX_PLAYERS + 7) >> 3];

	public void addNewNPC(Mob npc, Stream str, Stream updateBlock) {
		// synchronized(this) {
		int id = npc.npcId;
		npcInListBitmap[id >> 3] |= 1 << (id & 7);
		npcList[npcListSize++] = npc;

		str.writeBits(14, id);

		int z = npc.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = npc.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);

		str.writeBits(1, 0);
		str.writeBits(12, npc.npcType);

		boolean savedUpdateRequired = npc.updateRequired;
		npc.updateRequired = true;
		npc.appendNPCUpdateBlock(updateBlock);
		npc.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
	}

	public void addNewPlayer(Player plr, Stream str, Stream updateBlock) {
		// synchronized(this) {
		if (playerListSize >= 255) {
			return;
		}
		int id = plr.playerId;
		playerInListBitmap[id >> 3] |= 1 << (id & 7);
		playerList[playerListSize++] = plr;
		str.writeBits(11, id);
		str.writeBits(1, 1);
		boolean savedFlag = plr.isAppearanceUpdateRequired();
		boolean savedUpdateRequired = plr.updateRequired;
		plr.setAppearanceUpdateRequired(true);
		plr.updateRequired = true;
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setAppearanceUpdateRequired(savedFlag);
		plr.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
		int z = plr.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = plr.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
	}

	protected static Stream playerProps;

	static {
		playerProps = new Stream(new byte[100]);
	}

	protected void appendPlayerAppearance(Stream str) {
		playerProps.currentOffset = 0;

		playerProps.writeByte(playerAppearance[0]);
		playerProps.writeByte(headIcon);
		playerProps.writeByte(headIconPk);
		playerProps.writeByte(headIconHints);
		if (playerEquipment[playerHat] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[playerHat]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[playerCape] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[playerCape]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[playerAmulet] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[playerAmulet]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[playerWeapon] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[playerWeapon]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[playerChest] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[playerChest]);
		} else {
			playerProps.writeWord(0x100 + playerAppearance[2]);
		}

		if (playerEquipment[playerShield] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[playerShield]);
		} else {
			playerProps.writeByte(0);
		}

		if (!Item.isFullBody(playerEquipment[playerChest])) {
			playerProps.writeWord(0x100 + playerAppearance[3]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[playerLegs] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[playerLegs]);
		} else {
			playerProps.writeWord(0x100 + playerAppearance[5]);
		}

		if (!Item.isFullHelm(playerEquipment[playerHat]) && !Item.isFullMask(playerEquipment[playerHat])) {
			playerProps.writeWord(0x100 + playerAppearance[1]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[playerHands] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[playerHands]);
		} else {
			playerProps.writeWord(0x100 + playerAppearance[4]);
		}

		if (playerEquipment[playerFeet] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[playerFeet]);
		} else {
			playerProps.writeWord(0x100 + playerAppearance[6]);
		}

		if (playerAppearance[0] != 1 && !Item.isFullMask(playerEquipment[playerHat])) {
			playerProps.writeWord(0x100 + playerAppearance[7]);
		} else {
			playerProps.writeByte(0);
		}
		playerProps.writeByte(playerAppearance[8]);
		playerProps.writeByte(playerAppearance[9]);
		playerProps.writeByte(playerAppearance[10]);
		playerProps.writeByte(playerAppearance[11]);
		playerProps.writeByte(playerAppearance[12]);
		playerProps.writeWord(playerStandIndex);
		playerProps.writeWord(playerTurnIndex);
		playerProps.writeWord(playerWalkIndex);
		playerProps.writeWord(playerTurn180Index);
		playerProps.writeWord(playerTurn90CWIndex);
		playerProps.writeWord(playerTurn90CCWIndex);
		playerProps.writeWord(playerRunIndex);

		playerProps.writeQWord(Misc.playerNameToInt64(playerName));
		combatLevel = calculateCombatLevel();
		playerProps.writeByte(combatLevel);
		playerProps.writeWord(0);
		str.writeByteC(playerProps.currentOffset);
		str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);
	}

	public int calculateCombatLevel() {
		int j = getLevelForXP(playerXP[playerAttack]);
		int k = getLevelForXP(playerXP[playerDefence]);
		int l = getLevelForXP(playerXP[playerStrength]);
		int i1 = getLevelForXP(playerXP[playerHitpoints]);
		int j1 = getLevelForXP(playerXP[playerPrayer]);
		int k1 = getLevelForXP(playerXP[playerRanged]);
		int l1 = getLevelForXP(playerXP[playerMagic]);
		int combatLevel = (int) (((k + i1) + Math.floor(j1 / 2)) * 0.25D) + 1;
		double d = (j + l) * 0.32500000000000001D;
		double d1 = Math.floor(k1 * 1.5D) * 0.32500000000000001D;
		double d2 = Math.floor(l1 * 1.5D) * 0.32500000000000001D;
		if (d >= d1 && d >= d2) {
			combatLevel += d;
		} else if (d1 >= d && d1 >= d2) {
			combatLevel += d1;
		} else if (d2 >= d && d2 >= d1) {
			combatLevel += d2;
		}
		return combatLevel;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp)
				return lvl;
		}
		return 99;
	}

	private byte chatText[] = new byte[4096];
	private byte chatTextSize = 0;
	private int chatTextColor = 0;
	private int chatTextEffects = 0;

	protected void appendPlayerChatText(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(((getChatTextColor() & 0xFF) << 8) + (getChatTextEffects() & 0xFF));
		str.writeByte(getRights().getProtocolValue());
		str.writeByteC(getChatTextSize());
		str.writeBytes_reverse(getChatText(), getChatTextSize(), 0);

	}

	public void appendForcedChat(Stream str) {
		str.writeString(forcedChat);
	}

	public void appendMask100Update(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(gfx);
		str.writeDWord(gfxVar2);

	}

	public boolean wearing2h() {
		Player c = this;
		String s = c.getEquipment().getItemName(c.playerEquipment[c.playerWeapon]);
		if (s.contains("2h"))
			return true;
		else if (s.contains("godsword"))
			return true;
		return false;
	}

	/**
	 * Animations
	 **/
	public void startAnimation(int animId) {
		if (wearing2h() && animId == 829)
			return;
		animationRequest = animId;
		animationWaitCycles = 0;
		updateRequired = true;
	}

	public void startAnimation(int animId, int time) {
		animationRequest = animId;
		animationWaitCycles = time;
		updateRequired = true;
	}

	public void appendAnimationRequest(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian((animationRequest == -1) ? 65535 : animationRequest);
		str.writeByteC(animationWaitCycles);

	}

	/**
	 * Face Update
	 **/
	protected boolean faceUpdateRequired = false;
	public int face = -1;
	public int FocusPointX = -1, FocusPointY = -1;

	public void faceUpdate(int index) {
		face = index;
		faceUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceUpdate(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(face);

	}

	public void turnPlayerTo(int pointX, int pointY) {
		FocusPointX = 2 * pointX + 1;
		FocusPointY = 2 * pointY + 1;
		updateRequired = true;
	}

	private void appendSetFocusDestination(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndianA(FocusPointX);
		str.writeWordBigEndian(FocusPointY);

	}

	/**
	 * Hit Update
	 */

	protected void appendHitUpdate(Stream str) {
		str.writeByte(hit.getDamage());
		str.writeByteA(hit.getType().getId());
		if (playerLevel[3] <= 0) {
			playerLevel[3] = 0;
			isDead = true;
		}
		str.writeByteC(playerLevel[3]); // Their current hp, for HP bar
		str.writeByte(getLevelForXP(playerXP[3]));
	}

	protected void appendHitUpdate2(Stream str) {
		str.writeByte(secondaryHit.getDamage());
		str.writeByteS(secondaryHit.getType().getId());
		if (playerLevel[3] <= 0) {
			playerLevel[3] = 0;
			isDead = true;
		}
		str.writeByte(playerLevel[3]); // Their current hp, for HP bar
		str.writeByteC(getLevelForXP(playerXP[3])); // Their max hp, for HP
	}

	public void appendPlayerUpdateBlock(Stream str) {
		// synchronized(this) {
		if (!updateRequired && !isChatTextUpdateRequired())
			return;
		int updateMask = 0;
		if (graphicsUpdateRequired) {
			updateMask |= 0x100;
		}
		if (animationRequest != -1) {
			updateMask |= 8;
		}
		if (forcedChatUpdateRequired) {
			updateMask |= 4;
		}
		if (isChatTextUpdateRequired()) {
			updateMask |= 0x80;
		}
		if (isAppearanceUpdateRequired()) {
			updateMask |= 0x10;
		}
		if (faceUpdateRequired) {
			updateMask |= 1;
		}
		if (FocusPointX != -1) {
			updateMask |= 2;
		}
		if (isHitUpdateRequired()) {
			updateMask |= 0x20;
		}

		if (hitUpdateRequired2) {
			updateMask |= 0x200;
		}

		if (updateMask >= 0x100) {
			updateMask |= 0x40;
			str.writeByte(updateMask & 0xFF);
			str.writeByte(updateMask >> 8);
		} else {
			str.writeByte(updateMask);
		}

		// now writing the various update blocks itself - note that their order
		// crucial
		if (graphicsUpdateRequired) {
			appendMask100Update(str);
		}
		if (animationRequest != -1) {
			appendAnimationRequest(str);
		}
		if (forcedChatUpdateRequired) {
			appendForcedChat(str);
		}
		if (isChatTextUpdateRequired()) {
			appendPlayerChatText(str);
		}
		if (faceUpdateRequired) {
			appendFaceUpdate(str);
		}
		if (isAppearanceUpdateRequired()) {
			appendPlayerAppearance(str);
		}
		if (FocusPointX != -1) {
			appendSetFocusDestination(str);
		}
		if (isHitUpdateRequired()) {
			appendHitUpdate(str);
		}
		if (hitUpdateRequired2) {
			appendHitUpdate2(str);
		}

	}

	public void clearUpdateFlags() {
		updateRequired = false;
		setChatTextUpdateRequired(false);
		setAppearanceUpdateRequired(false);
		setHitUpdateRequired(false);
		hitUpdateRequired2 = false;
		forcedChatUpdateRequired = false;
		graphicsUpdateRequired = false;
		animationRequest = -1;
		FocusPointX = -1;
		FocusPointY = -1;
		poisonMask = -1;
		faceUpdateRequired = false;
		face = 65535;
	}

	public void stopMovement() {
		if (teleportToX <= 0 && teleportToY <= 0) {
			teleportToX = absX;
			teleportToY = absY;
		}
		newWalkCmdSteps = 0;
		getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
		getNextPlayerMovement();
	}

	private int newWalkCmdX[] = new int[walkingQueueSize];
	private int newWalkCmdY[] = new int[walkingQueueSize];
	public int newWalkCmdSteps = 0;
	private boolean newWalkCmdIsRunning = false;
	protected int travelBackX[] = new int[walkingQueueSize];
	protected int travelBackY[] = new int[walkingQueueSize];
	protected int numTravelBackSteps = 0;

	public void preProcessing() {
		newWalkCmdSteps = 0;
	}

	public void flushOutStream() {
		if (!session.isConnected() || disconnected || outStream.currentOffset == 0)
			return;

		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		Packet packet = new Packet(-1, Type.FIXED, ChannelBuffers.wrappedBuffer(temp));
		session.write(packet);
		outStream.currentOffset = 0;

	}

	public void destruct() {
		if (session == null)
			return;
		// PlayerSaving.getSingleton().requestSave(playerId);
		Misc.println("[DEREGISTERED]: " + playerName + "");
		disconnected = true;
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		buffer = null;
		this.destruct();
	}

	public void sendMessage(String s) {
		// synchronized (this) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}
	}

	/**
	 * A single players attributes
	 */
	@Override
	public void initAttributes() {
		this.getAttributes().put("isBusy", Boolean.FALSE);
		this.getAttributes().put("isBanking", Boolean.FALSE);
		this.getAttributes().put("isTrading", Boolean.FALSE);
		this.getAttributes().put("isDueling", Boolean.FALSE);
		this.getAttributes().put("isSkulled", Boolean.FALSE);
		this.getAttributes().put("canWalk", Boolean.TRUE);
	}

	public void showWelcomeScreen() {
		getActionSender().showWelcomeScreen(0, 0, 1, 0, 0);
		getActionSender().showInterface(15244);
		getActionSender().textOnInterface("Welcome to " + Configuration.SERVER_NAME + ".", 15257);
		getActionSender().textOnInterface("Your ip: " + this.connectedFrom, 15258);
		getActionSender().textOnInterface("\\n \\nRecovery Question Last Set: \\n @gre@ 10-Aug-2015", 15259);
		getActionSender().textOnInterface(
				"Jagex Staff will NEVER email you. We use the \\n" + "message centre on this website instead. \\n"
						+ "\\n" + "You have @gre@ 3 unread messages @yel@ in your \\n" + "message centre.",
				15260);
		getActionSender().textOnInterface("", 15261);
		getActionSender().textOnInterface("You are a member. Choose to subscribe \\n"
				+ "and you'll get loads of extra benefits and \\n" + "features.", 15262);
		getActionSender().textOnInterface("CLICK HERE TO PLAY", 15263);
		getActionSender().textOnInterface("1", 15264);
		// getActionSender().textOnInterface("2", 15265); //Hover
		// getActionSender().textOnInterface("3", 15266); //Hover
		getActionSender().textOnInterface("4", 15267);
		getActionSender().textOnInterface("5", 15268);
		getActionSender().textOnInterface("6", 15269);
		getActionSender().textOnInterface(
				"\\n You do not have a Bank PIN. Please visit a bank \\n" + "if you would like one.", 15270);
	}

	/**
	 * A players initial login, the step after the channels have accepted a
	 * valid password
	 */
	public void initialize() {
		// synchronized (this) {

		showWelcomeScreen();

		getActionSender().initializePlayer(1, this.playerId);

		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == playerId)
				continue;
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName))
					disconnected = true;
			}
		}
		for (int i = 0; i < 25; i++) {
			getActionSender().setSkillLevel(i, playerLevel[i], playerXP[i]);
			getActionSender().refreshSkill(i);
		}
		for (int p = 0; p < PRAYER.length; p++) { // reset prayer glows
			prayerActive[p] = false;
			getActionSender().sendFrame36(PRAYER_GLOW[p], 0);
		}
		loadRegion();
		getActionSender().handleWeaponStyle();
		accountFlagged = getActionSender().checkForFlags();
		// getPA().sendFrame36(43, fightMode-1);
		getActionSender().sendFrame36(108, 0);// resets autocast button
		getActionSender().sendFrame36(172, 1);
		getActionSender().sendFrame36(166, 3); // brightness level 3
		getActionSender().resetCamera(); // reset screen
		getActionSender().setChatOptions(0, 0, 0); // reset private messaging
													// options
		getActionSender().setSideBarInterfaces(this, true);
		sendMessage("Welcome to " + Configuration.SERVER_NAME);
		getActionSender().showOption(5, 0, "Follow", 4);
		getActionSender().showOption(4, 0, "Trade With", 3);
		getInventory().resetItems(3214);
		getEquipment().sendWeapon(playerEquipment[playerWeapon],
				getEquipment().getItemName(playerEquipment[playerWeapon]));
		getEquipment().resetBonus();
		getEquipment().getBonus();
		getEquipment().writeBonus();
		getEquipment().setEquipment(playerEquipment[playerHat], 1, playerHat);
		getEquipment().setEquipment(playerEquipment[playerCape], 1, playerCape);
		getEquipment().setEquipment(playerEquipment[playerAmulet], 1, playerAmulet);
		getEquipment().setEquipment(playerEquipment[playerArrows], playerEquipmentN[playerArrows], playerArrows);
		getEquipment().setEquipment(playerEquipment[playerChest], 1, playerChest);
		getEquipment().setEquipment(playerEquipment[playerShield], 1, playerShield);
		getEquipment().setEquipment(playerEquipment[playerLegs], 1, playerLegs);
		getEquipment().setEquipment(playerEquipment[playerHands], 1, playerHands);
		getEquipment().setEquipment(playerEquipment[playerFeet], 1, playerFeet);
		getEquipment().setEquipment(playerEquipment[playerRing], 1, playerRing);
		getEquipment().setEquipment(playerEquipment[playerWeapon], playerEquipmentN[playerWeapon], playerWeapon);
		getCombat().getPlayerAnimIndex(getEquipment().getItemName(playerEquipment[playerWeapon]).toLowerCase());
		getActionSender().logIntoPM();
		getEquipment().addSpecialBar(playerEquipment[playerWeapon]);
		saveTimer = GameConstants.SAVE_TIMER;
		saveCharacter = true;
		Misc.println("[REGISTERED]: " + playerName + "");
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();
		getActionSender().resetFollow();

		if (addStarter)
			getActionSender().addStarter();
		if (autoRet)
			getActionSender().sendFrame36(172, 1);
		else
			getActionSender().sendFrame36(172, 0);
	}

	private void loadRegion() {
		Server.itemHandler.reloadItems(this);
		Server.objectManager.loadObjects(this);
	}

	public void update() {
		synchronized (this) {
			handler.updatePlayer(this, outStream);
			handler.updateNPC(this, outStream);
			flushOutStream();
		}
	}

	public void logout() {
		synchronized (this) {
			if (System.currentTimeMillis() - logoutDelay > 10000) {
				outStream.createFrame(109);
				properLogout = true;
			} else {
				sendMessage("You must wait 10 seconds from being out of combat to logout.");
			}
		}
	}

	public int packetSize = 0, packetType = -1;

	/**
	 * The main tick for a player
	 */
	@Override
	public void process() {
		this.getActionSender().textOnInterface((int) (specAmount * 10) + "", 155);

		if (System.currentTimeMillis() - specDelay > GameConstants.INCREASE_SPECIAL_AMOUNT) {
			specDelay = System.currentTimeMillis();
			if (specAmount < 10) {
				specAmount += .5;
				if (specAmount > 10)
					specAmount = 10;
				getEquipment().addSpecialBar(playerEquipment[playerWeapon]);
			}
		}

		if (followId > 0) {
			getActionSender().followPlayer();
		} else if (followId2 > 0) {
			getActionSender().followNpc();
		}
		getCombat().handlePrayerDrain();
		if (System.currentTimeMillis() - singleCombatDelay > 3300) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 3300) {
			underAttackBy2 = 0;
		}

		if (System.currentTimeMillis() - restoreStatsDelay > 60000) {
			restoreStatsDelay = System.currentTimeMillis();
			for (int level = 0; level < playerLevel.length; level++) {
				if (playerLevel[level] < getLevelForXP(playerXP[level])) {
					if (level != 5) { // prayer doesn't restore
						playerLevel[level] += 1;
						getActionSender().setSkillLevel(level, playerLevel[level], playerXP[level]);
						getActionSender().refreshSkill(level);
					}
				} else if (playerLevel[level] > getLevelForXP(playerXP[level])) {
					playerLevel[level] -= 1;
					getActionSender().setSkillLevel(level, playerLevel[level], playerXP[level]);
					getActionSender().refreshSkill(level);
				}
			}
		}
		if (skullTimer > 0) {
			skullTimer--;
			if (skullTimer == 1) {
				this.getAttributes().put("isSkulled", Boolean.FALSE);
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getActionSender().requestUpdates();
			}
		}

		if (isDead && respawnTimer == -6) {
			getPA().applyDead();
		}

		if (respawnTimer == 7) {
			respawnTimer = -6;
			getPA().giveLife();
		} else if (respawnTimer == 12) {
			respawnTimer--;
			startAnimation(0x900);
			poisonDamage = -1;
		}

		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (PlayerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(absX, absY, PlayerHandler.players[frozenBy].absX,
						PlayerHandler.players[frozenBy].absY, 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}

		if (hitDelay > 0) {
			hitDelay--;
		}

		if (teleTimer > 0) {
			teleTimer--;
			if (!isDead) {
				if (teleTimer == 1 && newLocation > 0) {
					teleTimer = 0;
					getPA().changeLocation();
				}
				if (teleTimer == 5) {
					teleTimer--;
					getPA().processTeleport();
				}
				if (teleTimer == 9 && teleGfx > 0) {
					teleTimer--;
					gfx100(teleGfx);
				}
			} else {
				teleTimer = 0;
			}
		}

		if (hitDelay == 1) {
			if (oldNpcIndex > 0) {
				getCombat().delayedHit(this, oldNpcIndex);
			}
			if (oldPlayerIndex > 0) {
				getCombat().playerDelayedHit(this, oldPlayerIndex);
			}
		}

		if (attackTimer > 0) {
			attackTimer--;
		}

		if (attackTimer == 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		} else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
			if (npcIndex > 0) {
				attackTimer = 0;
				getCombat().attackNpc(npcIndex);
			} else if (playerIndex > 0) {
				attackTimer = 0;
				getCombat().attackPlayer(playerIndex);
			}
		}

		if ((Boolean) this.getAttributes().get("isTrading") && tradeResetNeeded) {
			Player o = PlayerHandler.players[tradeWith];
			if (o != null) {
				if (o.tradeResetNeeded) {
					getContentManager().getTrading().resetTrade();
					o.getContentManager().getTrading().resetTrade();
				}
			}
		}
	}

	public synchronized void postProcessing() {
		if (newWalkCmdSteps > 0) {
			int firstX = getNewWalkCmdX()[0], firstY = getNewWalkCmdY()[0];

			int lastDir = 0;
			boolean found = false;
			numTravelBackSteps = 0;
			int ptr = wQueueReadPtr;
			int dir = Misc.direction(currentX, currentY, firstX, firstY);
			if (dir != -1 && (dir & 1) != 0) {
				do {
					lastDir = dir;
					if (--ptr < 0)
						ptr = walkingQueueSize - 1;

					travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
					travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
					dir = Misc.direction(walkingQueueX[ptr], walkingQueueY[ptr], firstX, firstY);
					if (lastDir != dir) {
						found = true;
						break;
					}

				} while (ptr != wQueueWritePtr);
			} else
				found = true;

			if (!found)
				println_debug("Fatal: couldn't find connection vertex! Dropping packet.");
			else {
				wQueueWritePtr = wQueueReadPtr;

				addToWalkingQueue(currentX, currentY);

				if (dir != -1 && (dir & 1) != 0) {

					for (int i = 0; i < numTravelBackSteps - 1; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
					int wayPointX2 = travelBackX[numTravelBackSteps - 1],
							wayPointY2 = travelBackY[numTravelBackSteps - 1];
					int wayPointX1, wayPointY1;
					if (numTravelBackSteps == 1) {
						wayPointX1 = currentX;
						wayPointY1 = currentY;
					} else {
						wayPointX1 = travelBackX[numTravelBackSteps - 2];
						wayPointY1 = travelBackY[numTravelBackSteps - 2];
					}

					dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2, wayPointY2);
					if (dir == -1 || (dir & 1) != 0) {
						println_debug("Fatal: The walking queue is corrupt! wp1=(" + wayPointX1 + ", " + wayPointY1
								+ "), " + "wp2=(" + wayPointX2 + ", " + wayPointY2 + ")");
					} else {
						dir >>= 1;
						found = false;
						int x = wayPointX1, y = wayPointY1;
						while (x != wayPointX2 || y != wayPointY2) {
							x += Misc.directionDeltaX[dir];
							y += Misc.directionDeltaY[dir];
							if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
								found = true;
								break;
							}
						}
						if (!found) {
							println_debug("Fatal: Internal error: unable to determine connection vertex!" + "  wp1=("
									+ wayPointX1 + ", " + wayPointY1 + "), wp2=(" + wayPointX2 + ", " + wayPointY2
									+ "), " + "first=(" + firstX + ", " + firstY + ")");
						} else
							addToWalkingQueue(wayPointX1, wayPointY1);
					}
				} else {
					for (int i = 0; i < numTravelBackSteps; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
				}

				for (int i = 0; i < newWalkCmdSteps; i++) {
					addToWalkingQueue(getNewWalkCmdX()[i], getNewWalkCmdY()[i]);
				}

			}

			isRunning = isNewWalkCmdIsRunning() || isRunning2;
		}
	}

	public Rights getRights() {
		return rights;
	}

	public void setRights(Rights rights) {
		this.rights = rights;
	}

	public boolean inPcBoat() {
		return absX >= 2660 && absX <= 2663 && absY >= 2638 && absY <= 2643;
	}

	public boolean inPcGame() {
		return absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619;
	}

	public void setHitUpdateRequired(boolean hitUpdateRequired) {
		this.hitUpdateRequired = hitUpdateRequired;
	}

	public void setHitUpdateRequired2(boolean hitUpdateRequired2) {
		this.hitUpdateRequired2 = hitUpdateRequired2;
	}

	public boolean isHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired2() {
		return hitUpdateRequired2;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setChatTextEffects(int chatTextEffects) {
		this.chatTextEffects = chatTextEffects;
	}

	public int getChatTextEffects() {
		return chatTextEffects;
	}

	public void setChatTextSize(byte chatTextSize) {
		this.chatTextSize = chatTextSize;
	}

	public byte getChatTextSize() {
		return chatTextSize;
	}

	public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
		this.chatTextUpdateRequired = chatTextUpdateRequired;
	}

	public boolean isChatTextUpdateRequired() {
		return chatTextUpdateRequired;
	}

	public void setChatText(byte chatText[]) {
		this.chatText = chatText;
	}

	public ChatLogger getChatLog() {
		return chatLog;
	}

	public Censor getCensor() {
		return censor;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatTextColor(int chatTextColor) {
		this.chatTextColor = chatTextColor;
	}

	public int getChatTextColor() {
		return chatTextColor;
	}

	public void setNewWalkCmdX(int newWalkCmdX[]) {
		this.newWalkCmdX = newWalkCmdX;
	}

	public int[] getNewWalkCmdX() {
		return newWalkCmdX;
	}

	public void setNewWalkCmdY(int newWalkCmdY[]) {
		this.newWalkCmdY = newWalkCmdY;
	}

	public int[] getNewWalkCmdY() {
		return newWalkCmdY;
	}

	public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning) {
		this.newWalkCmdIsRunning = newWalkCmdIsRunning;
	}

	public boolean isNewWalkCmdIsRunning() {
		return newWalkCmdIsRunning;
	}

	private ISAACCipher inStreamDecryption = null, outStreamDecryption = null;

	public void setInStreamDecryption(ISAACCipher inStreamDecryption) {
		this.inStreamDecryption = inStreamDecryption;
	}

	public void setOutStreamDecryption(ISAACCipher outStreamDecryption) {
		this.outStreamDecryption = outStreamDecryption;
	}

	public SoundEffects getSound() {
		return soundEffects;
	}

	public Food getFood() {
		return food;
	}

	public Movement getMovement() {
		return movement;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public TradeLogger getTradeLog() {
		return tradeLog;
	}
}