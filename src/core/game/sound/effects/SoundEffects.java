package core.game.sound.effects;

import core.game.model.entity.npc.NPCDefinitions;
import core.game.model.entity.player.Player;
import core.game.model.item.ItemDefinition;
import core.game.util.Misc;

/**
 * A class that represents ingame sound effects
 */
public class SoundEffects {

	Player player;

	public SoundEffects(Player player) {
		this.player = player;
	}

	/**
	 * Singular sound variables.
	 */

	public final int LEVELUP = 67;
	public final int DUELWON = 77;
	public final int DUELLOST = 76;
	public final int FOODEAT = 317;
	public final int DROPITEM = 376;
	public final int COOKITEM = 357;
	public final int SHOOT_ARROW = 370;
	public final int TELEPORTM = 202;
	public final int BONE_BURY = 380;
	public final int DRINK_POTION = 334;

	public static int getNpcAttackSounds(int NPCID) {
		String npc = GetNpcName(NPCID).toLowerCase();
		if (npc.contains("bat")) {
			return 1;
		}
		if (npc.contains("cow")) {
			return 4;
		}
		if (npc.contains("imp")) {
			return 11;
		}
		if (npc.contains("rat")) {
			return 17;
		}
		if (npc.contains("duck")) {
			return 26;
		}
		if (npc.contains("wolf") || npc.contains("bear")) {
			return 28;
		}
		if (npc.contains("dragon")) {
			return 47;
		}
		if (npc.contains("ghost")) {
			return 57;
		}
		if (npc.contains("goblin")) {
			return 88;
		}
		if (npc.contains("skeleton") || npc.contains("demon")
				|| npc.contains("ogre") || npc.contains("giant")
				|| npc.contains("tz-") || npc.contains("jad")) {
			return 48;
		}
		if (npc.contains("zombie")) {
			return 1155;
		}
		if (npc.contains("man") || npc.contains("woman")
				|| npc.contains("monk")) {
			return 417;
		}
		return Misc.random(6) > 3 ? 398 : 394;

	}

	public static int getNpcBlockSound(int NPCID) {
		String npc = GetNpcName(NPCID).toLowerCase();
		if (npc.contains("bat")) {
			return 7;
		}
		if (npc.contains("cow")) {
			return 5;
		}
		if (npc.contains("imp")) {
			return 11;
		}
		if (npc.contains("rat")) {
			return 16;
		}
		if (npc.contains("duck")) {
			return 24;
		}
		if (npc.contains("wolf") || npc.contains("bear")) {
			return 34;
		}
		if (npc.contains("dragon")) {
			return 45;
		}
		if (npc.contains("ghost")) {
			return 53;
		}
		if (npc.contains("goblin")) {
			return 87;
		}
		if (npc.contains("skeleton") || npc.contains("demon")
				|| npc.contains("ogre") || npc.contains("giant")
				|| npc.contains("tz-") || npc.contains("jad")) {
			return 1154;
		}
		if (npc.contains("zombie")) {
			return 1151;
		}
		if (npc.contains("man") && !npc.contains("woman")) {
			return 816;
		}
		if (npc.contains("monk")) {
			return 816;
		}

		if (!npc.contains("man") && npc.contains("woman")) {
			return 818;
		}
		return 791;

	}

	public static int getNpcDeathSounds(int NPCID) {
		String npc = GetNpcName(NPCID).toLowerCase();
		if (npc.contains("bat")) {
			return 7;
		}
		if (npc.contains("cow")) {
			return 3;
		}
		if (npc.contains("imp")) {
			return 9;
		}
		if (npc.contains("rat")) {
			return 15;
		}
		if (npc.contains("duck")) {
			return 25;
		}
		if (npc.contains("wolf") || npc.contains("bear")) {
			return 35;
		}
		if (npc.contains("dragon")) {
			return 44;
		}
		if (npc.contains("ghost")) {
			return 60;
		}
		if (npc.contains("goblin")) {
			return 125;
		}
		if (npc.contains("skeleton") || npc.contains("demon")
				|| npc.contains("ogre") || npc.contains("giant")
				|| npc.contains("tz-") || npc.contains("jad")) {
			return 70;
		}
		if (npc.contains("zombie")) {
			return 1140;
		}
		return 70;

	}

	public static String GetNpcName(int NpcID) {
		return NPCDefinitions.getDefinitions()[NpcID].getName();
	}

	public static String getItemName(int itemId) {
		return ItemDefinition.getDefinitions()[itemId].getName();
	}

	public static int getPlayerBlockSounds(Player player) {

		int blockSound = 511;

		if (player.playerEquipment[player.playerChest] == 2499
				|| player.playerEquipment[player.playerChest] == 2501
				|| player.playerEquipment[player.playerChest] == 2503
				|| player.playerEquipment[player.playerChest] == 4746
				|| player.playerEquipment[player.playerChest] == 4757
				|| player.playerEquipment[player.playerChest] == 10330) {// Dragonhide
																// sound
			blockSound = 24;
		} else if (player.playerEquipment[player.playerChest] == 10551 || // Torso
				player.playerEquipment[player.playerChest] == 10438) {// 3rd age
			blockSound = 32;// Weird sound
		} else if (player.playerEquipment[player.playerChest] == 10338 || // 3rd age
				player.playerEquipment[player.playerChest] == 7399 || // Enchanted
				player.playerEquipment[player.playerChest] == 6107 || // Ghostly
				player.playerEquipment[player.playerChest] == 4091 || // Mystic
				player.playerEquipment[player.playerChest] == 4101 || // Mystic
				player.playerEquipment[player.playerChest] == 4111 || // Mystic
				player.playerEquipment[player.playerChest] == 1035 || // Zamorak
				player.playerEquipment[player.playerChest] == 12971) {// Combat
			blockSound = 14;// Robe sound
		} else if (player.playerEquipment[player.playerShield] == 4224) {// Crystal Shield
			blockSound = 30;// Crystal sound
		} else if (player.playerEquipment[player.playerChest] == 1101
				|| // Chains
				player.playerEquipment[player.playerChest] == 1103
				|| player.playerEquipment[player.playerChest] == 1105
				|| player.playerEquipment[player.playerChest] == 1107
				|| player.playerEquipment[player.playerChest] == 1109
				|| player.playerEquipment[player.playerChest] == 1111
				|| player.playerEquipment[player.playerChest] == 1113
				|| player.playerEquipment[player.playerChest] == 1115
				|| // Plates
				player.playerEquipment[player.playerChest] == 1117
				|| player.playerEquipment[player.playerChest] == 1119
				|| player.playerEquipment[player.playerChest] == 1121
				|| player.playerEquipment[player.playerChest] == 1123
				|| player.playerEquipment[player.playerChest] == 1125
				|| player.playerEquipment[player.playerChest] == 1127
				|| player.playerEquipment[player.playerChest] == 4720
				|| // Barrows armour
				player.playerEquipment[player.playerChest] == 4728
				|| player.playerEquipment[player.playerChest] == 4749
				|| player.playerEquipment[player.playerChest] == 4712
				|| player.playerEquipment[player.playerChest] == 11720
				|| // Godwars armour
				player.playerEquipment[player.playerChest] == 11724
				|| player.playerEquipment[player.playerChest] == 3140
				|| // Dragon
				player.playerEquipment[player.playerChest] == 2615
				|| // Fancy
				player.playerEquipment[player.playerChest] == 2653
				|| player.playerEquipment[player.playerChest] == 2661
				|| player.playerEquipment[player.playerChest] == 2669
				|| player.playerEquipment[player.playerChest] == 2623
				|| player.playerEquipment[player.playerChest] == 3841
				|| player.playerEquipment[player.playerChest] == 1127) {// Metal armour
																// sound
			blockSound = 15;
		} else {
			blockSound = 511;
		}
		return blockSound;
	}

	public static int GetWeaponSound(Player player) {

		String wep = getItemName(player.playerEquipment[player.playerWeapon])
				.toLowerCase();

		if (player.playerEquipment[player.playerWeapon] == 4718) {// Dharok's Greataxe
			return 1320;
		}
		if (player.playerEquipment[player.playerWeapon] == 4734) {// Karil's Crossbow
			return 1081;
		}
		if (player.playerEquipment[player.playerWeapon] == 4747) {// Torag's Hammers
			return 1330;
		}
		if (player.playerEquipment[player.playerWeapon] == 4710) {// Ahrim's Staff
			return 2555;
		}
		if (player.playerEquipment[player.playerWeapon] == 4755) {// Verac's Flail
			return 1323;
		}
		if (player.playerEquipment[player.playerWeapon] == 4726) {// Guthan's Warspear
			return 2562;
		}

		if (player.playerEquipment[player.playerWeapon] == 772
				|| player.playerEquipment[player.playerWeapon] == 1379
				|| player.playerEquipment[player.playerWeapon] == 1381
				|| player.playerEquipment[player.playerWeapon] == 1383
				|| player.playerEquipment[player.playerWeapon] == 1385
				|| player.playerEquipment[player.playerWeapon] == 1387
				|| player.playerEquipment[player.playerWeapon] == 1389
				|| player.playerEquipment[player.playerWeapon] == 1391
				|| player.playerEquipment[player.playerWeapon] == 1393
				|| player.playerEquipment[player.playerWeapon] == 1395
				|| player.playerEquipment[player.playerWeapon] == 1397
				|| player.playerEquipment[player.playerWeapon] == 1399
				|| player.playerEquipment[player.playerWeapon] == 1401
				|| player.playerEquipment[player.playerWeapon] == 1403
				|| player.playerEquipment[player.playerWeapon] == 1405
				|| player.playerEquipment[player.playerWeapon] == 1407
				|| player.playerEquipment[player.playerWeapon] == 1409
				|| player.playerEquipment[player.playerWeapon] == 9100) { // Staff wack
			return 394;
		}
		if (player.playerEquipment[player.playerWeapon] == 839
				|| player.playerEquipment[player.playerWeapon] == 841
				|| player.playerEquipment[player.playerWeapon] == 843
				|| player.playerEquipment[player.playerWeapon] == 845
				|| player.playerEquipment[player.playerWeapon] == 847
				|| player.playerEquipment[player.playerWeapon] == 849
				|| player.playerEquipment[player.playerWeapon] == 851
				|| player.playerEquipment[player.playerWeapon] == 853
				|| player.playerEquipment[player.playerWeapon] == 855
				|| player.playerEquipment[player.playerWeapon] == 857
				|| player.playerEquipment[player.playerWeapon] == 859
				|| player.playerEquipment[player.playerWeapon] == 861
				|| player.playerEquipment[player.playerWeapon] == 4734
				|| player.playerEquipment[player.playerWeapon] == 2023 // RuneC'Bow
				|| player.playerEquipment[player.playerWeapon] == 4212
				|| player.playerEquipment[player.playerWeapon] == 4214
				|| player.playerEquipment[player.playerWeapon] == 4934
				|| player.playerEquipment[player.playerWeapon] == 9104
				|| player.playerEquipment[player.playerWeapon] == 9107) { // Bows/Crossbows
			return 370;
		}
		if (player.playerEquipment[player.playerWeapon] == 1363
				|| player.playerEquipment[player.playerWeapon] == 1365
				|| player.playerEquipment[player.playerWeapon] == 1367
				|| player.playerEquipment[player.playerWeapon] == 1369
				|| player.playerEquipment[player.playerWeapon] == 1371
				|| player.playerEquipment[player.playerWeapon] == 1373
				|| player.playerEquipment[player.playerWeapon] == 1375
				|| player.playerEquipment[player.playerWeapon] == 1377
				|| player.playerEquipment[player.playerWeapon] == 1349
				|| player.playerEquipment[player.playerWeapon] == 1351
				|| player.playerEquipment[player.playerWeapon] == 1353
				|| player.playerEquipment[player.playerWeapon] == 1355
				|| player.playerEquipment[player.playerWeapon] == 1357
				|| player.playerEquipment[player.playerWeapon] == 1359
				|| player.playerEquipment[player.playerWeapon] == 1361
				|| player.playerEquipment[player.playerWeapon] == 9109) { // BattleAxes/Axes
			return 399;
		}
		if (player.playerEquipment[player.playerWeapon] == 4718
				|| player.playerEquipment[player.playerWeapon] == 7808) { // Dharok
																// GreatAxe
			return 400;
		}
		if (player.playerEquipment[player.playerWeapon] == 6609
				|| player.playerEquipment[player.playerWeapon] == 1307
				|| player.playerEquipment[player.playerWeapon] == 1309
				|| player.playerEquipment[player.playerWeapon] == 1311
				|| player.playerEquipment[player.playerWeapon] == 1313
				|| player.playerEquipment[player.playerWeapon] == 1315
				|| player.playerEquipment[player.playerWeapon] == 1317
				|| player.playerEquipment[player.playerWeapon] == 1319) { // 2h
			return 425;
		}
		if (player.playerEquipment[player.playerWeapon] == 1321
				|| player.playerEquipment[player.playerWeapon] == 1323
				|| player.playerEquipment[player.playerWeapon] == 1325
				|| player.playerEquipment[player.playerWeapon] == 1327
				|| player.playerEquipment[player.playerWeapon] == 1329
				|| player.playerEquipment[player.playerWeapon] == 1331
				|| player.playerEquipment[player.playerWeapon] == 1333
				|| player.playerEquipment[player.playerWeapon] == 4587) { // Scimitars
			return 396;
		}
		if (wep.contains("halberd")) {
			return 420;
		}
		if (wep.contains("long")) {
			return 396;
		}
		if (wep.contains("knife")) {
			return 368;
		}
		if (wep.contains("javelin")) {
			return 364;
		}

		if (player.playerEquipment[player.playerWeapon] == 9110) {
			return 401;
		}
		if (player.playerEquipment[player.playerWeapon] == 4755) {
			return 1059;
		}
		if (player.playerEquipment[player.playerWeapon] == 4153) {
			return 1079;
		}
		if (player.playerEquipment[player.playerWeapon] == 9103) {
			return 385;
		}
		if (player.playerEquipment[player.playerWeapon] == -1) { // fists
			return 417;
		}
		if (player.playerEquipment[player.playerWeapon] == 2745
				|| player.playerEquipment[player.playerWeapon] == 2746
				|| player.playerEquipment[player.playerWeapon] == 2747
				|| player.playerEquipment[player.playerWeapon] == 2748) { // Godswords
			return 390;
		}
		if (player.playerEquipment[player.playerWeapon] == 4151) {
			return 1080;
		} else {
			return 398; // Daggers(this is enything that isn't added)
		}
	}

	public static int specialSounds(int id) {
		if (id == 4151) // whip
		{
			return 1081;
		}
		if (id == 5698) // dds
		{
			return 793;
		}
		if (id == 1434)// Mace
		{
			return 387;
		}
		if (id == 3204) // halberd
		{
			return 420;
		}
		if (id == 4153) // gmaul
		{
			return 1082;
		}
		if (id == 7158) // d2h
		{
			return 426;
		}
		if (id == 4587) // dscim
		{
			return 1305;
		}
		if (id == 1215) // Dragon dag
		{
			return 793;
		}
		if (id == 1305) // D Long
		{
			return 390;
		}
		if (id == 861) // MSB
		{
			return 386;
		}
		if (id == 11235) // DBow
		{
			return 386;
		}
		if (id == 6739) // D Axe
		{
		}
		if (id == 1377) // DBAxe
		{
			return 389;
		}
		return -1;
	}
}
