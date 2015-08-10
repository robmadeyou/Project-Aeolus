package core.game.sound;

import core.Config;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.container.Equipment;
import core.game.model.item.ItemDefinition;

/**
 * 
 * @author FUZENSETH
 *
 */
public class SoundManager {

	/**
	 * The sound types.
	 * 
	 * @author FUZENSETH
	 *
	 */
	public enum SoundType {
		MELEE_COMBAT, NPC_SOUND, REGION_MUSIC, CAST_SOUND, MAGIC_COMBAT, RANGED_COMBAT, SKILL_SOUND, PLAYER_BLOCK, NPC_BLOCK, SPECIAL_ATTACK;
	}

	/**
	 * Sends a sound
	 * 
	 * @param p
	 * @param id
	 * @param type
	 * @return
	 */
	public static final int sendSound(Player p, int id, SoundType type) {
		switch (type) {
		/**
		 * RANGE COMBAT SOUNDS so basically if i want to add new bow soudn
		 */
		case RANGED_COMBAT:
			String bow = p.getEquipment().getItemName(id).toLowerCase();
			if (bow.contains("shortbow") || bow.contains("longbow"))
				p.getPlayerAssistant().sendSound(370);
			else if (bow.contains("knife") || bow.contains("dart"))
				p.getPlayerAssistant().sendSound(364);
			else if (bow.contains("c'bow")) // <.. item name contains
				p.getPlayerAssistant().sendSound(364);// <-- sends the sound by
														// given id

			break;
		/**
		 * BLOCK SOUNDS WHEN PLAYER GETS A HIT
		 * 
		 */
		case PLAYER_BLOCK:
			// String shield = p.getItems()
			// .getItemName(p.playerEquipment[p.playerShield]).toLowerCase();
			String shield = p.getEquipment().getItemName(id).toLowerCase();
			if (shield.contains("kiteshield")
					|| shield.contains("toktz-ket-xil"))
				p.getPlayerAssistant().sendSound(414);
			else if (shield.contains("defender"))
				p.getPlayerAssistant().sendSound(409);
			break;
		/**
		 * THE CASTING SOUND WHEN YOU START CASTING A SPELL.
		 */
		case CAST_SOUND:
			switch (id) { // switches spell id

			case 0:// Air strike
				p.getPlayerAssistant().sendSound(220);
				break;
			case 1:// Water strike
				p.getPlayerAssistant().sendSound(211);
				break;
			case 2:// Earth strike
				p.getPlayerAssistant().sendSound(1002);
				break;
			case 3:// Fire strike
				p.getPlayerAssistant().sendSound(1017);
				break;
			case 4:// Air bolt
				p.getPlayerAssistant().sendSound(218);
				break;
			case 5:// Water bolt
				p.getPlayerAssistant().sendSound(211);
				break;
			case 6:// Earth bolt
				p.getPlayerAssistant().sendSound(1003);
				break;
			case 7:// Fire bolt
				p.getPlayerAssistant().sendSound(1015);
				break;
			case 8:// Air blast
				p.getPlayerAssistant().sendSound(216);
				break;
			case 9:// Water blast
				p.getPlayerAssistant().sendSound(207);
				break;
			case 10:// Earth blast
				p.getPlayerAssistant().sendSound(1007);
				break;
			case 11:// Fire blast
				p.getPlayerAssistant().sendSound(1020);
				break;
			case 12:// Air wave
				p.getPlayerAssistant().sendSound(222);
				break;
			case 13:// Water wave
				p.getPlayerAssistant().sendSound(213);
				break;
			case 14:// Earth wave
				p.getPlayerAssistant().sendSound(1009);
				break;
			case 15:// Fire wave
				p.getPlayerAssistant().sendSound(1014);
				break;
			case 32:
				p.getPlayerAssistant().sendSound(1114);
				break;
			case 33:
			case 34:
				p.getPlayerAssistant().sendSound(986);
				break;
			case 35:
				p.getPlayerAssistant().sendSound(1112);
				break;
			case 36:
				p.getPlayerAssistant().sendSound(1115);
				break;
			case 37:
				p.getPlayerAssistant().sendSound(1014);
				break;
			case 38:
				p.getPlayerAssistant().sendSound(986);
				break;
			case 39:
				p.getPlayerAssistant().sendSound(1126);
				break;
			case 40:
				p.getPlayerAssistant().sendSound(1116);
				break;
			case 41:
			case 42:
				p.getPlayerAssistant().sendSound(986);
				break;
			case 43:
				p.getPlayerAssistant().sendSound(1110);
				break;
			case 44:
			case 45:
			case 46:
			case 47:
				p.getPlayerAssistant().sendSound(1111);
				break;
			// spells

			}
			break;

		/**
		 * Magic sounds when it reaches the target.
		 */
		case MAGIC_COMBAT:
			switch (id) {

			// spells
			case 0:// Air strike
				p.getPlayerAssistant().sendSound(221);
				break;
			case 1:// Water strike
				p.getPlayerAssistant().sendSound(212);
				break;
			case 2:// Earth strike
				p.getPlayerAssistant().sendSound(1004);
				break;
			case 3:// Fire strike
				p.getPlayerAssistant().sendSound(1018);
				break;
			case 4:// Air bolt
				p.getPlayerAssistant().sendSound(219);
				break;
			case 5:// Water bolt
				p.getPlayerAssistant().sendSound(210);
				break;
			case 6:// Earth bolt
				p.getPlayerAssistant().sendSound(1006);
				break;
			case 7:// Fire bolt
				p.getPlayerAssistant().sendSound(1016);
				break;
			case 8:// Air blast
				p.getPlayerAssistant().sendSound(217);
				break;
			case 9:// Water blast
				p.getPlayerAssistant().sendSound(208);
				break;
			case 10:// Earth blast
				p.getPlayerAssistant().sendSound(1005);
				break;
			case 11:// Fire blast
				p.getPlayerAssistant().sendSound(1019);
				break;
			case 12:// Air wave
				p.getPlayerAssistant().sendSound(223);
				break;
			case 13:// Water wave
				p.getPlayerAssistant().sendSound(214);
				break;
			case 14:// Earth wave
				p.getPlayerAssistant().sendSound(1008);
				break;
			case 15:// Fire wave
				p.getPlayerAssistant().sendSound(1021);
				break;
			case 47:
				p.getPlayerAssistant().sendSound(1125);
				break;
			case 39:
				p.getPlayerAssistant().sendSound(1126);
				break;
			case 43:
				p.getPlayerAssistant().sendSound(1110);
				break;
			case 32:
			case 33:
			case 34:
			case 35:
			case 36:
			case 37:
			case 38:
			case 40:
			case 41:
			case 42:
			case 44:
			case 45:
			case 46:
				p.getPlayerAssistant().sendSound(1125);
				break;
			}
			break;

		/**
		 * Represent's players combat sounds. (when you hit with a weapon)
		 */
		case MELEE_COMBAT:
			final String weaponName = ItemDefinition.getDefinitions()[id].getName()
					.toLowerCase();
			if (weaponName.contains("scimitar"))
				p.getPlayerAssistant().sendSound(396);
			else if (weaponName.contains("mace"))
				p.getPlayerAssistant().sendSound(395);
			else if (weaponName.contains("dagger"))
				p.getPlayerAssistant().sendSound(793);
			else if (weaponName.contains("battleaxe"))
				p.getPlayerAssistant().sendSound(399);
			else if (weaponName.contains("2h sword"))
				p.getPlayerAssistant().sendSound(425);
			else if (weaponName.contains("flail"))
				p.getPlayerAssistant().sendSound(1059);
			switch (id) {// switches weapon id
			case 4151:
			case 14004:// tentacle
				p.getPlayerAssistant().sendSound(1080);
				break;
			case 4718:// dh axe etcetc
				p.getPlayerAssistant().sendSound(1057);
				break;
			case 4153:
				p.getPlayerAssistant().sendSound(1079);
				break;
			case 4727:
				p.getPlayerAssistant().sendSound(1061);
				break;
			}
			break;

		/**
		 * Plays region music -TODO not added
		 */
		case REGION_MUSIC:// regionids
			break;

		default:
			break;

		}
		return id;
	}

	/**
	 * Ripped this int from r-s 2 lazy to do
	 * 
	 * @param id
	 * @return
	 */
	public static int specialSounds(int id) {
		if (id == 4151) // whip
		{
			return 1081;
		}
		if (id == 5698 || id == 1215) // dds
		{
			return 385;
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

	/**
	 * Playes a sound effect.
	 * 
	 * @param player
	 * @param id
	 */
	public static final void play(Player player, int i) {
		if (!Config.enableSound)
			return;
		player.getPlayerAssistant().sendSound(i);
	}

}