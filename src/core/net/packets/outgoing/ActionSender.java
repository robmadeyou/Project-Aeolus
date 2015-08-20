package core.net.packets.outgoing;

import java.util.stream.IntStream;

import core.Configuration;
import core.game.GameConstants;
import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.entity.player.Rights;
import core.game.util.Misc;

/**
 * A class which holds methods for outgoing {@link Packet}s. There's still a lot
 * of methods that need to be moved out of this class, because they are not
 * outgoing packets.
 * 
 * @author 7Winds
 */
public class ActionSender {

	private Player c;

	public ActionSender(Player Player) {
		this.c = Player;
	}

	public int CraftInt, Dcolor, FletchInt;

	/**
	 * MulitCombat icon
	 * 
	 * @param icon
	 *            0 = off icon 1 = on
	 */
	public void multiWay(int icon) {
		// synchronized(c) {
		c.outStream.createFrame(61);
		c.outStream.writeByte(icon);
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/**
	 * Used for showing the IP address for the welcome screen.
	 */
	public static int ipToInt(String addr) {
		String[] addrArray = addr.split("\\.");
		int num = 0;
		for (int i = 0; i < addrArray.length; i++) {
			int power = 3 - i;
			num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
		}
		return num;
	}	

	/**
	 * Items displayed on the armor interface.
	 * 
	 * @param id
	 * @param amount
	 */
	public void itemOnInterface(int id, int amount) {
		synchronized (c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(2274);
			c.getOutStream().writeWord(1);
			if (amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(amount);
			} else {
				c.getOutStream().writeByte(amount);
			}
			c.getOutStream().writeWordBigEndianA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}
	
	/**
	 * Displays the welcome screen.
	 */
	public void showWelcomeScreen(int days, int unreadMessages, int member, int ip, int daysSince) {
		c.outStream.createFrame(176);
		c.outStream.writeByteC(days);
		c.outStream.writeWordA(unreadMessages);
		c.outStream.writeByte(member);
		c.outStream.writeDWord_v1(ip);
		c.outStream.writeWord(daysSince);
	}
	
	/**
	 * Sets a sidebar interface
	 * @param menuId
	 * @param form
	 */
	public void setSidebarInterface(int menuId, int form) {
		// synchronized (this) {
		if (c.getOutStream() != null) {
			c.outStream.createFrame(71);
			c.outStream.writeWord(form);
			c.outStream.writeByteA(menuId);
		}
	}
	
	public void sendClan(String name, String message, String clan, int rights) {
		c.outStream.createFrameVarSizeWord(217);
		c.outStream.writeString(name);
		c.outStream.writeString(message);
		c.outStream.writeString(clan);
		c.outStream.writeWord(c.getRights().getValues());
		c.outStream.endFrameVarSize();
	}
	
	/**
	 * Spins a players camera
	 */
  	public static void spinCamera(Player c, int i1, int i2, int i3, int i4, int i5) {
		c.outStream.createFrame(177);
		c.outStream.writeByte(i1);
		c.outStream.writeByte(i2);
		c.outStream.writeWord(i3);
		c.outStream.writeByte(i4);
		c.outStream.writeByte(i5); 
	}

	/**
	 * Updates the music tabs song colors
	 * 
	 * @param id
	 * @param color
	 */
	public void sendColor(int id, int color) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrame(122);
			c.outStream.writeWordBigEndianA(id);
			c.outStream.writeWordBigEndianA(color);
		}
	}

	/**
	 * Sends a player a sound effect
	 */
	public void sendSound(int soundId) {
		if (soundId > 0 & c != null && c.outStream != null) {
			c.outStream.createFrame(174);
			c.outStream.writeWord(soundId);
			c.outStream.writeByte(100);
			c.outStream.writeWord(5);
		}
	}

	/**
	 * Sends a player a sound effect
	 */
	public void sound(int soundId) {
		if (soundId > 0 && c.outStream != null) {
			c.outStream.createFrame(174);
			c.outStream.writeWord(soundId);
			c.outStream.writeByte(100);
			c.outStream.writeWord(5);
		}
	}

	/**
	 * Sends a player a sound effect
	 */
	public void sendSound2(int i1, int i2, int i3) {
		c.outStream.createFrame(174);
		c.outStream.writeWord(i1); // id
		c.outStream.writeByte(i2); // volume, just set it to 100 unless you play
									// around with your client after this
		c.outStream.writeWord(i3); // delay
		c.updateRequired = true;
		c.appearanceUpdateRequired = true;
		c.flushOutStream();
	}

	/**
	 * Sends a song
	 * 
	 * @param client
	 * @param sound
	 */
	public void sendMusic(Player client, int songId) {
		client.outStream.createFrame(74);
		client.outStream.writeWordBigEndian(songId);
	}

	public void clearClanChat() {
		c.clanId = -1;
		c.getActionSender().textOnInterface("Talking in: ", 18139);
		c.getActionSender().textOnInterface("Owner: ", 18140);
		for (int j = 18144; j < 18244; j++)
			c.getActionSender().textOnInterface("", j);
	}

	public void resetAutocast() {
		c.autocastId = 0;
		c.autocasting = false;
		c.getActionSender().setConfig(108, 0);
	}

	public void setSideBarInterfaces(Player p, boolean enable) {
		if (enable) {
			setSidebarInterface(1, 3917);
			setSidebarInterface(2, 638);
			setSidebarInterface(3, 3213);
			setSidebarInterface(4, 1644);
			setSidebarInterface(5, 5608);
			if (p.playerMagicBook == 0) {
				setSidebarInterface(6, 1151); // modern
			} else {
				setSidebarInterface(6, 12855); // ancient
			}
			setSidebarInterface(7, 18128); // clan chat
			setSidebarInterface(8, 5065);
			setSidebarInterface(9, 5715);
			setSidebarInterface(10, 2449);
			// setSidebarInterface(11, 4445); // wrench tab
			setSidebarInterface(11, 904); // wrench tab
			setSidebarInterface(12, 147); // run tab
			setSidebarInterface(13, 962); // music tab
			// setSidebarInterface(13, -1);
			setSidebarInterface(0, 2423);
		} else {
			setSidebarInterface(1, -1);
			setSidebarInterface(2, -1);
			setSidebarInterface(3, 6014);//
			setSidebarInterface(4, -1);
			setSidebarInterface(5, -1);
			if (p.playerMagicBook == 0) {
				setSidebarInterface(6, -1); // modern
			} else {
				setSidebarInterface(6, -1); // ancient
			}
			setSidebarInterface(7, 18128); // clan chat
			setSidebarInterface(8, 5065);
			setSidebarInterface(9, 5715);
			setSidebarInterface(10, -1);
			setSidebarInterface(11, -1); // wrench tab
			setSidebarInterface(12, -1); // run tab
			setSidebarInterface(13, -1);
			setSidebarInterface(14, -1);
			setSidebarInterface(0, -1);
		}
	}

	/**
	 * Sets text on an interface
	 */
	public void sendString(final String s, final int id) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(s);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}

	}

	/**
	 * Displays items on death screen
	 */
	public void ItemsOnDeath(Player c) {
		if (c.isTrading) {
			return;
		}
		c.getActionSender().textOnInterface("Item's Kept on Death", 17103);
		c.StartBestItemScan(c);
		c.EquipStatus = 0;
		for (int k = 0; k < 4; k++)
			c.getActionSender().sendFrame34a(10494, -1, k, 1);
		for (int k = 0; k < 39; k++)
			c.getActionSender().sendFrame34a(10600, -1, k, 1);
		if (c.WillKeepItem1 > 0)
			c.getActionSender().sendFrame34a(10494, c.WillKeepItem1, 0, c.WillKeepAmt1);
		if (c.WillKeepItem2 > 0)
			c.getActionSender().sendFrame34a(10494, c.WillKeepItem2, 1, c.WillKeepAmt2);
		if (c.WillKeepItem3 > 0)
			c.getActionSender().sendFrame34a(10494, c.WillKeepItem3, 2, c.WillKeepAmt3);
		if (c.WillKeepItem4 > 0 && c.prayerActive[10])
			c.getActionSender().sendFrame34a(10494, c.WillKeepItem4, 3, 1);
		for (int ITEM = 0; ITEM < 28; ITEM++) {
			if (c.playerItems[ITEM] - 1 > 0
					&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot)
					&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot)
					&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot)
					&& !(c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot)) {
				c.getActionSender().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM]);
				c.EquipStatus += 1;
			} else if (c.playerItems[ITEM] - 1 > 0
					&& (c.playerItems[ITEM] - 1 == c.WillKeepItem1 && ITEM == c.WillKeepItem1Slot)
					&& c.playerItemsN[ITEM] > c.WillKeepAmt1) {
				c.getActionSender().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
						c.playerItemsN[ITEM] - c.WillKeepAmt1);
				c.EquipStatus += 1;
			} else if (c.playerItems[ITEM] - 1 > 0
					&& (c.playerItems[ITEM] - 1 == c.WillKeepItem2 && ITEM == c.WillKeepItem2Slot)
					&& c.playerItemsN[ITEM] > c.WillKeepAmt2) {
				c.getActionSender().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
						c.playerItemsN[ITEM] - c.WillKeepAmt2);
				c.EquipStatus += 1;
			} else if (c.playerItems[ITEM] - 1 > 0
					&& (c.playerItems[ITEM] - 1 == c.WillKeepItem3 && ITEM == c.WillKeepItem3Slot)
					&& c.playerItemsN[ITEM] > c.WillKeepAmt3) {
				c.getActionSender().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus,
						c.playerItemsN[ITEM] - c.WillKeepAmt3);
				c.EquipStatus += 1;
			} else if (c.playerItems[ITEM] - 1 > 0
					&& (c.playerItems[ITEM] - 1 == c.WillKeepItem4 && ITEM == c.WillKeepItem4Slot)
					&& c.playerItemsN[ITEM] > 1) {
				c.getActionSender().sendFrame34a(10600, c.playerItems[ITEM] - 1, c.EquipStatus, c.playerItemsN[ITEM] - 1);
				c.EquipStatus += 1;
			}
		}
		for (int EQUIP = 0; EQUIP < 14; EQUIP++) {
			if (c.playerEquipment[EQUIP] > 0
					&& !(c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot)
					&& !(c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot)
					&& !(c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot)
					&& !(c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot)) {
				c.getActionSender().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP]);
				c.EquipStatus += 1;
			} else if (c.playerEquipment[EQUIP] > 0
					&& (c.playerEquipment[EQUIP] == c.WillKeepItem1 && EQUIP + 28 == c.WillKeepItem1Slot)
					&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt1 > 0) {
				c.getActionSender().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
						c.playerEquipmentN[EQUIP] - c.WillKeepAmt1);
				c.EquipStatus += 1;
			} else if (c.playerEquipment[EQUIP] > 0
					&& (c.playerEquipment[EQUIP] == c.WillKeepItem2 && EQUIP + 28 == c.WillKeepItem2Slot)
					&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt2 > 0) {
				c.getActionSender().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
						c.playerEquipmentN[EQUIP] - c.WillKeepAmt2);
				c.EquipStatus += 1;
			} else if (c.playerEquipment[EQUIP] > 0
					&& (c.playerEquipment[EQUIP] == c.WillKeepItem3 && EQUIP + 28 == c.WillKeepItem3Slot)
					&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - c.WillKeepAmt3 > 0) {
				c.getActionSender().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus,
						c.playerEquipmentN[EQUIP] - c.WillKeepAmt3);
				c.EquipStatus += 1;
			} else if (c.playerEquipment[EQUIP] > 0
					&& (c.playerEquipment[EQUIP] == c.WillKeepItem4 && EQUIP + 28 == c.WillKeepItem4Slot)
					&& c.playerEquipmentN[EQUIP] > 1 && c.playerEquipmentN[EQUIP] - 1 > 0) {
				c.getActionSender().sendFrame34a(10600, c.playerEquipment[EQUIP], c.EquipStatus, c.playerEquipmentN[EQUIP] - 1);
				c.EquipStatus += 1;
			}
		}
		c.ResetKeepItems();
		c.getActionSender().showInterface(17100);
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public int absX, absY;
	public int heightLevel;

	public static void showInterface(Player Player, int i) {
		Player.getOutStream().createFrame(97);
		Player.getOutStream().writeWord(i);
		Player.flushOutStream();
	}

	public static void sendQuest(Player Player, String s, int i) {
		Player.getOutStream().createFrameVarSizeWord(126);
		Player.getOutStream().writeString(s);
		Player.getOutStream().writeWordA(i);
		Player.getOutStream().endFrameVarSizeWord();
		Player.flushOutStream();
	}

	public void sendStillGraphics(int id, int heightS, int y, int x, int timeBCS) {
		c.getOutStream().createFrame(85);
		c.getOutStream().writeByteC(y - (c.mapRegionY * 8));
		c.getOutStream().writeByteC(x - (c.mapRegionX * 8));
		c.getOutStream().createFrame(4);
		c.getOutStream().writeByte(0);// Tiles away (X >> 4 + Y & 7)
										// //Tiles away from
		// absX and absY.
		c.getOutStream().writeWord(id); // Graphic ID.
		c.getOutStream().writeByte(heightS); // Height of the graphic when
												// cast.
		c.getOutStream().writeWord(timeBCS); // Time before the graphic
												// plays.
		c.flushOutStream();
	}

	public void createArrow(int type, int id) {
		if (c != null) {
			c.getOutStream().createFrame(254); // The packet ID
			c.getOutStream().writeByte(type); // 1=NPC, 10=Player
			c.getOutStream().writeWord(id); // NPC/Player ID
			c.getOutStream().write3Byte(0); // Junk
		}
	}

	public void createArrow(int x, int y, int height, int pos) {
		if (c != null) {
			c.getOutStream().createFrame(254); // The packet ID
			c.getOutStream().writeByte(pos); // Position on Square(2 = middle, 3
												// = west, 4 = east, 5 = south,
												// 6 = north)
			c.getOutStream().writeWord(x); // X-Coord of Object
			c.getOutStream().writeWord(y); // Y-Coord of Object
			c.getOutStream().writeByte(height); // Height off Ground
		}
	}

	public void sendQuest(String s, int i) {
		c.getOutStream().createFrameVarSizeWord(126);
		c.getOutStream().writeString(s);
		c.getOutStream().writeWordA(i);
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	/**
	 * Sends text on interface text - String id - ShortA
	 */
	public void textOnInterface(String text, int id) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(text);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}

	}

	/**
	 * Update single item
	 */
	public void sendFrame34a(int frame, int item, int slot, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	/**
	 * Enter name
	 */
	public void enterName(String s) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(187);
			c.getOutStream().writeString(s);
		}

	}

	public void setSkillLevel(int skillNum, int currentLevel, int XP) {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(134);
				c.getOutStream().writeByte(skillNum);
				c.getOutStream().writeDWord_v1(XP);
				c.getOutStream().writeByte(currentLevel);
				c.flushOutStream();
			}
		}
	}

	/**
	 * Force Open Tab
	 */
	public void forceOpenTab(int sideIcon) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(106);
			c.getOutStream().writeByteC(sideIcon);
			c.flushOutStream();
			requestUpdates();
		}
	}

	/**
	 * Resets Camera
	 */
	public void resetCamera() {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(107);
			c.flushOutStream();
		}
	}

	/**
	 * Handle Byte Configuration
	 */
	public void setConfig(int id, int state) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(36);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeByte(state);
			c.flushOutStream();
		}

	}

	/**
	 * Shows player head on interface
	 */
	public void showPlayerHeadOnInterface(int Frame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(185);
			c.getOutStream().writeWordBigEndianA(Frame);
		}

	}

	/**
	 * Show Interface
	 */
	public void showInterface(int interfaceid) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(97);
			c.getOutStream().writeWord(interfaceid);
			c.flushOutStream();

		}
	}

	/**
	 * Opens Interface with inventory
	 */
	public void sendFrame248(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();

		}
	}

	/**
	 * Item on Interface
	 */
	public void itemOnInterface(int MainFrame, int SubFrame, int SubFrame2) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(246);
			c.getOutStream().writeWordBigEndian(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.getOutStream().writeWord(SubFrame2);
			c.flushOutStream();

		}
	}

	/**
	 * Sets interface visibility
	 */
	public void sendFrame171(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(171);
			c.getOutStream().writeByte(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();

		}
	}

	public void sendFrame200(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(200);
			c.getOutStream().writeWord(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void moveComponent(int i, int o, int id) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(70);
			c.getOutStream().writeWord(i);
			c.getOutStream().writeWordBigEndian(o);
			c.getOutStream().writeWordBigEndian(id);
			c.flushOutStream();
		}

	}

	public void showNpcHeadOnInterface(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(75);
			c.getOutStream().writeWordBigEndianA(MainFrame);
			c.getOutStream().writeWordBigEndianA(SubFrame);
			c.flushOutStream();
		}

	}

	public void openBackDialogue(int Frame) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(164);
			c.getOutStream().writeWordBigEndian_dup(Frame);
			c.flushOutStream();
		}

	}

	public void setPrivateMessaging(int i) { // friends and ignore list status
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(221);
			c.getOutStream().writeByte(i);
			c.flushOutStream();
		}

	}

	/**
	 * Sets different chat options
	 * public, private, tradeblock
	 */
	public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(206);
			c.getOutStream().writeByte(publicChat);
			c.getOutStream().writeByte(privateChat);
			c.getOutStream().writeByte(tradeBlock);
			c.flushOutStream();
		}

	}

	public void handleIntConfig(int id, int state) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(87);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.getOutStream().writeDWord_v1(state);
			c.flushOutStream();
		}

	}

	/**
	 * Sends another player a private message
	 */
	public void sendPM(long name, int rights, byte[] chatmessage, int messagesize) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSize(196);
			c.getOutStream().writeQWord(name);
			c.getOutStream().writeDWord(c.lastChatId++);
			c.getOutStream().writeByte(rights);
			c.getOutStream().writeBytes(chatmessage, messagesize, 0);
			c.getOutStream().endFrameVarSize();
			c.flushOutStream();
			String chatmessagegot = Misc.textUnpack(chatmessage, messagesize);
			String target = Misc.longToPlayerName(name);
		}

	}

	public void createPlayerHints(int type, int id) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(type);
			c.getOutStream().writeWord(id);
			c.getOutStream().write3Byte(0);
			c.flushOutStream();
		}
	}

	public void createObjectHints(int x, int y, int height, int pos) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(pos);
			c.getOutStream().writeWord(x);
			c.getOutStream().writeWord(y);
			c.getOutStream().writeByte(height);
			c.flushOutStream();
		}
	}

	/**
	 * Loads a private message
	 */
	public void loadPM(long playerName, int world) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			if (world != 0) {
				world += 9;
			} else if (!Configuration.WORLD_LIST_FIX) {
				world += 1;
			}
			c.getOutStream().createFrame(50);
			c.getOutStream().writeQWord(playerName);
			c.getOutStream().writeByte(world);
			c.flushOutStream();
		}

	}

	/**
	 * Removes all windows open
	 */
	public void removeAllWindows() {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(219);
			c.flushOutStream();
		}
	}

	public void sendFrame34(int id, int slot, int column, int amount) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrameVarSizeWord(34); // init item to smith screen
			c.outStream.writeWord(column); // Column Across Smith Screen
			c.outStream.writeByte(4); // Total Rows?
			c.outStream.writeDWord(slot); // Row Down The Smith Screen
			c.outStream.writeWord(id + 1); // item
			c.outStream.writeByte(amount); // how many there are?
			c.outStream.endFrameVarSizeWord();
		}

	}

	public void walkableInterface(int id) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(208);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.flushOutStream();
		}

	}

	public int mapStatus = 0;

	/**
	 * Disables the minimap
	 */
	public void disableMinimap(int state) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			if (mapStatus != state) {
				mapStatus = state;
				c.getOutStream().createFrame(99);
				c.getOutStream().writeByte(state);
				c.flushOutStream();
			}

		}
	}

	/**
	 * Commonly used for crashing cheat clients
	 */
	public void sendCrashFrame() {
		synchronized (c) {
			if (c.getOutStream() != null && c != null) {
				c.getOutStream().createFrame(123);
				c.flushOutStream();
			}
		}
	}

	/**
	 * Reseting animations for everyone
	 **/
	public void frame1() {
		// synchronized(c) {
		IntStream.range(0, GameConstants.MAX_PLAYERS).forEach(player -> {
			if (PlayerHandler.players[player] != null) {
				Player person = (Player) PlayerHandler.players[player];
				if (person != null) {
					if (person.getOutStream() != null && !person.disconnected) {
						if (c.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							person.getActionSender().requestUpdates();
						}
					}
				}

			}
		});
	}

	/**
	 * Creating projectile
	 **/
	public void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(16);
			c.getOutStream().writeByte(64);
			c.flushOutStream();

		}
	}

	public void createProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(slope);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}
	}

	public void createProjectile3(int casterY, int casterX, int offsetY, int offsetX, int gfxMoving, int StartHeight,
			int endHeight, int speed, int AtkIndex) {		
		IntStream.range(0, GameConstants.MAX_PLAYERS).forEach(player -> {
			if (PlayerHandler.players[player] != null) {
				Player p = PlayerHandler.players[player];
				if (p.goodDistance(c.absX, c.absY, p.absX, p.absY, 60)) {
					if (p.heightLevel == c.heightLevel) {
						if (PlayerHandler.players[player] != null && !PlayerHandler.players[player].disconnected) {
							p.outStream.createFrame(85);
							p.outStream.writeByteC((casterY - (p.mapRegionY * 8)) - 2);
							p.outStream.writeByteC((casterX - (p.mapRegionX * 8)) - 3);
							p.outStream.createFrame(117);
							p.outStream.writeByte(50);
							p.outStream.writeByte(offsetY);
							p.outStream.writeByte(offsetX);
							p.outStream.writeWord(AtkIndex);
							p.outStream.writeWord(gfxMoving);
							p.outStream.writeByte(StartHeight);
							p.outStream.writeByte(endHeight);
							p.outStream.writeWord(51);
							p.outStream.writeWord(speed);
							p.outStream.writeByte(16);
							p.outStream.writeByte(64);
						}
					}
				}
			}
		});
	}

	/**
	 * Creates a projectile for everyone within 25 squares
	 */
	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
		// synchronized(c) {
		IntStream.range(0, GameConstants.MAX_PLAYERS).forEach(player -> {
			Player p = PlayerHandler.players[player];
			if (p != null) {
				Player person = (Player) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							if (p.heightLevel == c.heightLevel)
								person.getActionSender().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
										endHeight, lockon, time);
						}
					}
				}

			}
		});
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		IntStream.range(0, GameConstants.MAX_PLAYERS).forEach(player -> {
			Player p = PlayerHandler.players[player];
			if (p != null) {
				Player person = (Player) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getActionSender().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time, slope);
						}
					}
				}
			}
		});
	}

	/**
	 ** GFX
	 **/
	public void stillGfx(int id, int x, int y, int height, int time) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(y - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(x - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(4);
			c.getOutStream().writeByte(0);
			c.getOutStream().writeWord(id);
			c.getOutStream().writeByte(height);
			c.getOutStream().writeWord(time);
			c.flushOutStream();
		}

	}

	/**
	 * Creates a gfx for everyone
	 */
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {
		// synchronized(c) {
		IntStream.range(0, GameConstants.MAX_PLAYERS).forEach(player -> {
			Player p = PlayerHandler.players[player];
			if (p != null) {
				Player person = (Player) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getActionSender().stillGfx(id, x, y, height, time);
						}
					}
				}
			}
		});
	}

	/**
	 * Objects, add and remove
	 **/
	public void object(int objectId, int objectX, int objectY, int face, int objectType) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}

	}

	public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
		if (c.distanceToPoint(objectX, objectY) > 60)
			return;
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}

	}

	/**
	 * Show option, attack, trade, follow etc
	 **/
	public String optionType = "null";

	public void showOption(int i, int l, String s, int a) {
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			if (!optionType.equalsIgnoreCase(s)) {
				optionType = s;
				c.getOutStream().createFrameVarSize(104);
				c.getOutStream().writeByteC(i);
				c.getOutStream().writeByteA(l);
				c.getOutStream().writeString(s);
				c.getOutStream().endFrameVarSize();
				c.flushOutStream();
			}

		}
	}

	/**
	 * Open bank
	 **/
	public void openUpBank() {
		c.isBanking = true;
		// synchronized(c) {
		if (c.getOutStream() != null && c != null) {
			c.getInventory().resetItems(5064);
			c.getItems().rearrangeBank();
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(5292);
			c.getOutStream().writeWord(5063);
			c.flushOutStream();
		}

	}

	/**
	 * Private Messaging
	 **/
	public void logIntoPM() {
		setPrivateMessaging(2);
		IntStream.range(0, GameConstants.MAX_PLAYERS).forEach(player -> {
			Player p = PlayerHandler.players[player];
			if (p != null && p.isActive) {
				Player o = (Player) p;
				if (o != null) {
					o.getActionSender().updatePM(c.playerId, 1);
				}
			}
		});
		boolean pmLoaded = false;
		for (int i = 0; i < c.friends.length; i++) {
			if (c.friends[i] != 0) {
				for (int i2 = 1; i2 < GameConstants.MAX_PLAYERS; i2++) {
					Player p = PlayerHandler.players[i2];
					if (p != null && p.isActive && Misc.playerNameToInt64(p.playerName) == c.friends[i]) {
						Player o = (Player) p;
						if (o != null) {
							if (c.getRights().greaterOrEqual(Rights.ADMINISTRATOR) || p.privateChat == 0
									|| (p.privateChat == 1 && o.getActionSender().isInPM(Misc.playerNameToInt64(c.playerName)))) {
								loadPM(c.friends[i], 1);
								pmLoaded = true;
							}
							break;
						}
					}
				}
				if (!pmLoaded) {
					loadPM(c.friends[i], 0);
				}
				pmLoaded = false;
			}
			IntStream.range(1, GameConstants.MAX_PLAYERS).forEach(player -> {
				Player p = PlayerHandler.players[player];
				if (p != null && p.isActive) {
					Player o = (Player) p;
					if (o != null) {
						o.getActionSender().updatePM(c.playerId, 1);
					}
				}
			});
		}
	}

	/**
	 * Used for private chat updates
	 */
	public void updatePM(int pID, int world) {
		Player p = PlayerHandler.players[pID];
		if (p == null || p.playerName == null || p.playerName.equals("null")) {
			return;
		}
		Player o = (Player) p;
		if (o == null) {
			return;
		}
		long l = Misc.playerNameToInt64(PlayerHandler.players[pID].playerName);

		if (p.privateChat == 0) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i]) {
						loadPM(l, world);
						return;
					}
				}
			}
		} else if (p.privateChat == 1) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i]) {
						if (o.getActionSender().isInPM(Misc.playerNameToInt64(c.playerName))) {
							loadPM(l, world);
							return;
						} else {
							loadPM(l, 0);
							return;
						}
					}
				}
			}
		} else if (p.privateChat == 2) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i] && c.getRights().less(Rights.ADMINISTRATOR)) {
						loadPM(l, 0);
						return;
					}
				}
			}
		}
	}

	public boolean isInPM(long l) {
		for (int i = 0; i < c.friends.length; i++) {
			if (c.friends[i] != 0) {
				if (l == c.friends[i]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void handleStatus(int i, int i2, int i3) {
		if (i == 1)
			c.getInventory().addItem(i2, i3);
		else if (i == 2) {
			c.playerXP[i2] = c.getActionSender().getXPForLevel(i3) + 5;
			c.playerLevel[i2] = c.getActionSender().getLevelForXP(c.playerXP[i2]);
		}
	}

	/**
	 * Following another player
	 **/
	public void followPlayer() {
		if (PlayerHandler.players[c.followPlayerId] == null || PlayerHandler.players[c.followPlayerId].isDead) {
			resetFollow();
			return;
		}
		if (c.freezeTimer > 0) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0)
			return;

		int otherX = PlayerHandler.players[c.followPlayerId].getX();
		int otherY = PlayerHandler.players[c.followPlayerId].getY();

		boolean sameSpot = (c.absX == otherX && c.absY == otherY);

		boolean hallyDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);

		boolean rangeWeaponDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 4);
		boolean bowDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 6);
		boolean mageDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 7);

		boolean castingMagic = (c.usingMagic || c.mageFollow || c.autocasting || c.spellId > 0) && mageDistance;
		boolean playerRanging = (c.usingRangeWeapon) && rangeWeaponDistance;
		boolean playerBowOrCross = (c.usingBow) && bowDistance;

		if (!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followPlayerId = 0;
			resetFollow();
			return;
		}
		c.faceUpdate(c.followPlayerId + 32768);
		if (!sameSpot) {
			if (c.playerIndex > 0 && !c.usingSpecial && c.inWild()) {
				if (c.usingSpecial && (playerRanging || playerBowOrCross)) {
					c.stopMovement();
					return;
				}
				if (castingMagic || playerRanging || playerBowOrCross) {
					c.stopMovement();
					return;
				}
				if (c.getCombat().usingHally() && hallyDistance) {
					c.stopMovement();
					return;
				}
			}
		}
		if (otherX == c.absX && otherY == c.absY) {
			int r = Misc.random(3);
			switch (r) {
			case 0:
				walkTo(0, -1);
				break;
			case 1:
				walkTo(0, 1);
				break;
			case 2:
				walkTo(1, 0);
				break;
			case 3:
				walkTo(-1, 0);
				break;
			}
		} else if (c.isRunning2) {
			if (otherY > c.getY() && otherX == c.getX()) {
				c.playerWalk(otherX, otherY - 1);
			} else if (otherY < c.getY() && otherX == c.getX()) {
				c.playerWalk(otherX, otherY + 1);
			} else if (otherX > c.getX() && otherY == c.getY()) {
				c.playerWalk(otherX - 1, otherY);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				c.playerWalk(otherX + 1, otherY);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				c.playerWalk(otherX + 1, otherY + 1);
			} else if (otherX > c.getX() && otherY > c.getY()) {
				c.playerWalk(otherX - 1, otherY - 1);
			} else if (otherX < c.getX() && otherY > c.getY()) {
				c.playerWalk(otherX + 1, otherY - 1);
			} else if (otherX > c.getX() && otherY < c.getY()) {
				c.playerWalk(otherX + 1, otherY - 1);
			}
		} else {
			if (otherY > c.getY() && otherX == c.getX()) {
				c.playerWalk(otherX, otherY - 1);
			} else if (otherY < c.getY() && otherX == c.getX()) {
				c.playerWalk(otherX, otherY + 1);
			} else if (otherX > c.getX() && otherY == c.getY()) {
				c.playerWalk(otherX - 1, otherY);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				c.playerWalk(otherX + 1, otherY);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				c.playerWalk(otherX + 1, otherY + 1);
			} else if (otherX > c.getX() && otherY > c.getY()) {
				c.playerWalk(otherX - 1, otherY - 1);
			} else if (otherX < c.getX() && otherY > c.getY()) {
				c.playerWalk(otherX + 1, otherY - 1);
			} else if (otherX > c.getX() && otherY < c.getY()) {
				c.playerWalk(otherX - 1, otherY + 1);
			}
		}
		c.faceUpdate(c.followPlayerId + 32768);
	}

	public void followNpc() {
		if (MobHandler.npcs[c.followPlayerId] == null || MobHandler.npcs[c.followPlayerId].isDead) {
			c.followPlayerId = 0;
			return;
		}
		if (c.freezeTimer > 0) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0)
			return;

		int otherX = MobHandler.npcs[c.followMobId].getX();
		int otherY = MobHandler.npcs[c.followMobId].getY();
		boolean withinDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
		boolean hallyDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 2);
		boolean bowDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 8);
		boolean rangeWeaponDistance = c.goodDistance(otherX, otherY, c.getX(), c.getY(), 4);
		boolean sameSpot = c.absX == otherX && c.absY == otherY;
		if (!c.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followMobId = 0;
			return;
		}
		if (c.goodDistance(otherX, otherY, c.getX(), c.getY(), 1)) {
			if (otherX != c.getX() && otherY != c.getY()) {
				stopDiagonal(otherX, otherY);
				return;
			}
		}

		if ((c.usingBow || c.mageFollow || (c.npcIndex > 0 && c.autocastId > 0)) && bowDistance && !sameSpot) {
			return;
		}

		if (c.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (c.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}

		c.faceUpdate(c.followPlayerId);
		if (otherX == c.absX && otherY == c.absY) {
			int r = Misc.random(3);
			switch (r) {
			case 0:
				walkTo(0, -1);
				break;
			case 1:
				walkTo(0, 1);
				break;
			case 2:
				walkTo(1, 0);
				break;
			case 3:
				walkTo(-1, 0);
				break;
			}
		} else if (c.isRunning2 && !withinDistance) {
			if (otherY > c.getY() && otherX == c.getX()) {
				c.playerWalk(otherX, otherY - 1);
			} else if (otherY < c.getY() && otherX == c.getX()) {
				c.playerWalk(otherX, otherY + 1);
			} else if (otherX > c.getX() && otherY == c.getY()) {
				c.playerWalk(otherX - 1, otherY);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				c.playerWalk(otherX + 1, otherY);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				c.playerWalk(otherX + 1, otherY + 1);
			} else if (otherX > c.getX() && otherY > c.getY()) {
				c.playerWalk(otherX - 1, otherY - 1);
			} else if (otherX < c.getX() && otherY > c.getY()) {
				c.playerWalk(otherX + 1, otherY - 1);
			} else if (otherX > c.getX() && otherY < c.getY()) {
				c.playerWalk(otherX + 1, otherY - 1);
			}
		} else {
			if (otherY > c.getY() && otherX == c.getX()) {
				c.playerWalk(otherX, otherY - 1);
			} else if (otherY < c.getY() && otherX == c.getX()) {
				c.playerWalk(otherX, otherY + 1);
			} else if (otherX > c.getX() && otherY == c.getY()) {
				c.playerWalk(otherX - 1, otherY);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				c.playerWalk(otherX + 1, otherY);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				c.playerWalk(otherX + 1, otherY + 1);
			} else if (otherX > c.getX() && otherY > c.getY()) {
				c.playerWalk(otherX - 1, otherY - 1);
			} else if (otherX < c.getX() && otherY > c.getY()) {
				c.playerWalk(otherX + 1, otherY - 1);
			} else if (otherX > c.getX() && otherY < c.getY()) {
				c.playerWalk(otherX - 1, otherY + 1);
			}
		}
		c.faceUpdate(c.followPlayerId);
	}

	public int getRunningMove(int i, int j) {
		if (j - i > 2)
			return 2;
		else if (j - i < -2)
			return -2;
		else
			return j - i;
	}

	public void resetFollow() {
		c.followPlayerId = 0;
		c.followMobId = 0;
		c.mageFollow = false;
	}

	public void walkTo(int dirX, int dirY) {
		int walkToX = dirX + c.getX() - c.mapRegionX * 8;
		int walkToY = dirY + c.getY() - c.mapRegionY * 8;
		c.getNewWalkCmdX()[0] = walkToX;
		c.getNewWalkCmdY()[0] = walkToY;
		c.newWalkCmdSteps = 0;
	}

	public void walkTo2(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public void stopDiagonal(int otherX, int otherY) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 1;
		int xMove = otherX - c.getX();
		int yMove = 0;
		if (xMove == 0)
			yMove = otherY - c.getY();

		int k = c.getX() + xMove;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + yMove;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public void walkToCheck(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public int getMove(int place1, int place2) {
		if (System.currentTimeMillis() - c.lastSpear < 4000)
			return 0;
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	/**
	 * reseting animation
	 **/
	public void resetAnimation() {
		c.getCombat().getPlayerAnimIndex(c.getEquipment().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
		c.startAnimation(c.playerStandIndex);
		requestUpdates();
	}

	public void requestUpdates() {
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void levelUp(int skill) {
		int totalLevel = (getLevelForXP(c.playerXP[0]) + getLevelForXP(c.playerXP[1]) + getLevelForXP(c.playerXP[2])
				+ getLevelForXP(c.playerXP[3]) + getLevelForXP(c.playerXP[4]) + getLevelForXP(c.playerXP[5])
				+ getLevelForXP(c.playerXP[6]) + getLevelForXP(c.playerXP[7]) + getLevelForXP(c.playerXP[8])
				+ getLevelForXP(c.playerXP[9]) + getLevelForXP(c.playerXP[10]) + getLevelForXP(c.playerXP[11])
				+ getLevelForXP(c.playerXP[12]) + getLevelForXP(c.playerXP[13]) + getLevelForXP(c.playerXP[14])
				+ getLevelForXP(c.playerXP[15]) + getLevelForXP(c.playerXP[16]) + getLevelForXP(c.playerXP[17])
				+ getLevelForXP(c.playerXP[18]) + getLevelForXP(c.playerXP[19]) + getLevelForXP(c.playerXP[20]));
		textOnInterface("Total Lvl: " + totalLevel, 3984);
		switch (skill) {
		case 0:
			textOnInterface("Congratulations, you just advanced an attack level!", 6248);
			textOnInterface("Your attack level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6249);
			c.sendMessage("Congratulations, you just advanced an attack level.");
			openBackDialogue(6247);

			break;

		case 1:
			textOnInterface("Congratulations, you just advanced a defence level!", 6254);
			textOnInterface("Your defence level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6255);
			c.sendMessage("Congratulations, you just advanced a defence level.");
			openBackDialogue(6253);

			break;

		case 2:
			textOnInterface("Congratulations, you just advanced a strength level!", 6207);
			textOnInterface("Your strength level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6208);
			c.sendMessage("Congratulations, you just advanced a strength level.");
			openBackDialogue(6206);

			break;

		case 3:
			textOnInterface("Congratulations, you just advanced a hitpoints level!", 6217);
			textOnInterface("Your hitpoints level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6218);
			c.sendMessage("Congratulations, you just advanced a hitpoints level.");
			openBackDialogue(6216);
			break;

		case 4:
			textOnInterface("Congratulations, you just advanced a ranged level!", 5453);
			textOnInterface("Your ranged level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6114);
			c.sendMessage("Congratulations, you just advanced a ranging level.");
			openBackDialogue(4443);

			break;

		case 5:
			textOnInterface("Congratulations, you just advanced a prayer level!", 6243);
			textOnInterface("Your prayer level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6244);
			c.sendMessage("Congratulations, you just advanced a prayer level.");
			openBackDialogue(6242);

			break;

		case 6:
			textOnInterface("Congratulations, you just advanced a magic level!", 6212);
			textOnInterface("Your magic level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6213);
			c.sendMessage("Congratulations, you just advanced a magic level.");
			openBackDialogue(6211);

			break;

		case 7:
			textOnInterface("Congratulations, you just advanced a cooking level!", 6227);
			textOnInterface("Your cooking level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6228);
			c.sendMessage("Congratulations, you just advanced a cooking level.");
			openBackDialogue(6226);

			break;

		case 8:
			textOnInterface("Congratulations, you just advanced a woodcutting level!", 4273);
			textOnInterface("Your woodcutting level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4274);
			c.sendMessage("Congratulations, you just advanced a woodcutting level.");
			openBackDialogue(4272);

			break;

		case 9:
			textOnInterface("Congratulations, you just advanced a fletching level!", 6232);
			textOnInterface("Your fletching level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6233);
			c.sendMessage("Congratulations, you just advanced a fletching level.");
			openBackDialogue(6231);
			break;

		case 10:
			textOnInterface("Congratulations, you just advanced a fishing level!", 6259);
			textOnInterface("Your fishing level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6260);
			c.sendMessage("Congratulations, you just advanced a fishing level.");
			openBackDialogue(6258);
			break;

		case 11:
			textOnInterface("Congratulations, you just advanced a fire making level!", 4283);
			textOnInterface("Your firemaking level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4284);
			c.sendMessage("Congratulations, you just advanced a fire making level.");
			openBackDialogue(4282);
			break;

		case 12:
			textOnInterface("Congratulations, you just advanced a crafting level!", 6264);
			textOnInterface("Your crafting level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6265);
			c.sendMessage("Congratulations, you just advanced a crafting level.");
			openBackDialogue(6263);
			break;

		case 13:
			textOnInterface("Congratulations, you just advanced a smithing level!", 6222);
			textOnInterface("Your smithing level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6223);
			c.sendMessage("Congratulations, you just advanced a smithing level.");
			openBackDialogue(6221);
			break;

		case 14:
			textOnInterface("Congratulations, you just advanced a mining level!", 4417);
			textOnInterface("Your mining level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4438);
			c.sendMessage("Congratulations, you just advanced a mining level.");
			openBackDialogue(4416);
			break;

		case 15:
			textOnInterface("Congratulations, you just advanced a herblore level!", 6238);
			textOnInterface("Your herblore level is now " + getLevelForXP(c.playerXP[skill]) + ".", 6239);
			c.sendMessage("Congratulations, you just advanced a herblore level.");
			openBackDialogue(6237);
			break;

		case 16:
			textOnInterface("Congratulations, you just advanced a agility level!", 4278);
			textOnInterface("Your agility level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4279);
			c.sendMessage("Congratulations, you just advanced an agility level.");
			openBackDialogue(4277);
			break;

		case 17:
			textOnInterface("Congratulations, you just advanced a thieving level!", 4263);
			textOnInterface("Your theiving level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4264);
			c.sendMessage("Congratulations, you just advanced a thieving level.");
			openBackDialogue(4261);
			break;

		case 18:
			textOnInterface("Congratulations, you just advanced a slayer level!", 12123);
			textOnInterface("Your slayer level is now " + getLevelForXP(c.playerXP[skill]) + ".", 12124);
			c.sendMessage("Congratulations, you just advanced a slayer level.");
			openBackDialogue(12122);
			break;

		case 20:
			textOnInterface("Congratulations, you just advanced a runecrafting level!", 4268);
			textOnInterface("Your runecrafting level is now " + getLevelForXP(c.playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations, you just advanced a runecrafting level.");
			openBackDialogue(4267);
			break;
		}
		c.dialogueAction = 0;
		c.nextChat = 0;
	}

	public void refreshSkill(int skillId) {
		switch (skillId) {
		case 0:
			textOnInterface("" + c.playerLevel[0] + "", 4004);
			textOnInterface("" + getLevelForXP(c.playerXP[0]) + "", 4005);
			textOnInterface("" + c.playerXP[0] + "", 4044);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[0]) + 1) + "", 4045);
			break;

		case 1:
			textOnInterface("" + c.playerLevel[1] + "", 4008);
			textOnInterface("" + getLevelForXP(c.playerXP[1]) + "", 4009);
			textOnInterface("" + c.playerXP[1] + "", 4056);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[1]) + 1) + "", 4057);
			break;

		case 2:
			textOnInterface("" + c.playerLevel[2] + "", 4006);
			textOnInterface("" + getLevelForXP(c.playerXP[2]) + "", 4007);
			textOnInterface("" + c.playerXP[2] + "", 4050);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[2]) + 1) + "", 4051);
			break;

		case 3:
			textOnInterface("" + c.playerLevel[3] + "", 4016);
			textOnInterface("" + getLevelForXP(c.playerXP[3]) + "", 4017);
			textOnInterface("" + c.playerXP[3] + "", 4080);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[3]) + 1) + "", 4081);
			break;

		case 4:
			textOnInterface("" + c.playerLevel[4] + "", 4010);
			textOnInterface("" + getLevelForXP(c.playerXP[4]) + "", 4011);
			textOnInterface("" + c.playerXP[4] + "", 4062);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[4]) + 1) + "", 4063);
			break;

		case 5:
			textOnInterface("" + c.playerLevel[5] + "", 4012);
			textOnInterface("" + getLevelForXP(c.playerXP[5]) + "", 4013);
			textOnInterface("" + c.playerXP[5] + "", 4068);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[5]) + 1) + "", 4069);
			textOnInterface("" + c.playerLevel[5] + "/" + getLevelForXP(c.playerXP[5]) + "", 687);// Prayer
																								// frame
			break;

		case 6:
			textOnInterface("" + c.playerLevel[6] + "", 4014);
			textOnInterface("" + getLevelForXP(c.playerXP[6]) + "", 4015);
			textOnInterface("" + c.playerXP[6] + "", 4074);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[6]) + 1) + "", 4075);
			break;

		case 7:
			textOnInterface("" + c.playerLevel[7] + "", 4034);
			textOnInterface("" + getLevelForXP(c.playerXP[7]) + "", 4035);
			textOnInterface("" + c.playerXP[7] + "", 4134);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[7]) + 1) + "", 4135);
			break;

		case 8:
			textOnInterface("" + c.playerLevel[8] + "", 4038);
			textOnInterface("" + getLevelForXP(c.playerXP[8]) + "", 4039);
			textOnInterface("" + c.playerXP[8] + "", 4146);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[8]) + 1) + "", 4147);
			break;

		case 9:
			textOnInterface("" + c.playerLevel[9] + "", 4026);
			textOnInterface("" + getLevelForXP(c.playerXP[9]) + "", 4027);
			textOnInterface("" + c.playerXP[9] + "", 4110);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[9]) + 1) + "", 4111);
			break;

		case 10:
			textOnInterface("" + c.playerLevel[10] + "", 4032);
			textOnInterface("" + getLevelForXP(c.playerXP[10]) + "", 4033);
			textOnInterface("" + c.playerXP[10] + "", 4128);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[10]) + 1) + "", 4129);
			break;

		case 11:
			textOnInterface("" + c.playerLevel[11] + "", 4036);
			textOnInterface("" + getLevelForXP(c.playerXP[11]) + "", 4037);
			textOnInterface("" + c.playerXP[11] + "", 4140);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[11]) + 1) + "", 4141);
			break;

		case 12:
			textOnInterface("" + c.playerLevel[12] + "", 4024);
			textOnInterface("" + getLevelForXP(c.playerXP[12]) + "", 4025);
			textOnInterface("" + c.playerXP[12] + "", 4104);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[12]) + 1) + "", 4105);
			break;

		case 13:
			textOnInterface("" + c.playerLevel[13] + "", 4030);
			textOnInterface("" + getLevelForXP(c.playerXP[13]) + "", 4031);
			textOnInterface("" + c.playerXP[13] + "", 4122);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[13]) + 1) + "", 4123);
			break;

		case 14:
			textOnInterface("" + c.playerLevel[14] + "", 4028);
			textOnInterface("" + getLevelForXP(c.playerXP[14]) + "", 4029);
			textOnInterface("" + c.playerXP[14] + "", 4116);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[14]) + 1) + "", 4117);
			break;

		case 15:
			textOnInterface("" + c.playerLevel[15] + "", 4020);
			textOnInterface("" + getLevelForXP(c.playerXP[15]) + "", 4021);
			textOnInterface("" + c.playerXP[15] + "", 4092);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[15]) + 1) + "", 4093);
			break;

		case 16:
			textOnInterface("" + c.playerLevel[16] + "", 4018);
			textOnInterface("" + getLevelForXP(c.playerXP[16]) + "", 4019);
			textOnInterface("" + c.playerXP[16] + "", 4086);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[16]) + 1) + "", 4087);
			break;

		case 17:
			textOnInterface("" + c.playerLevel[17] + "", 4022);
			textOnInterface("" + getLevelForXP(c.playerXP[17]) + "", 4023);
			textOnInterface("" + c.playerXP[17] + "", 4098);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[17]) + 1) + "", 4099);
			break;

		case 18:
			textOnInterface("" + c.playerLevel[18] + "", 12166);
			textOnInterface("" + getLevelForXP(c.playerXP[18]) + "", 12167);
			textOnInterface("" + c.playerXP[18] + "", 12171);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[18]) + 1) + "", 12172);
			break;

		case 19:
			textOnInterface("" + c.playerLevel[19] + "", 13926);
			textOnInterface("" + getLevelForXP(c.playerXP[19]) + "", 13927);
			textOnInterface("" + c.playerXP[19] + "", 13921);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[19]) + 1) + "", 13922);
			break;

		case 20:
			textOnInterface("" + c.playerLevel[20] + "", 4152);
			textOnInterface("" + getLevelForXP(c.playerXP[20]) + "", 4153);
			textOnInterface("" + c.playerXP[20] + "", 4157);
			textOnInterface("" + getXPForLevel(getLevelForXP(c.playerXP[20]) + 1) + "", 4158);
			break;
		}
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public boolean addSkillXP(int amount, int skill) {
		if (amount + c.playerXP[skill] < 0 || c.playerXP[skill] > 500000000) {
			if (c.playerXP[skill] > 500000000) {
				c.playerXP[skill] = 500000000;
			}
			return false;
		}
		if (skill == 11) {
			amount *= 20;
		}
		if (skill == 12) {
			amount *= 20;
		}
		int oldLevel = getLevelForXP(c.playerXP[skill]);
		c.playerXP[skill] += amount;
		if(c.playerEquipment[3] == 6570) {
			c.playerXP[skill] += amount;
		}
		amount *= GameConstants.SERVER_EXP_BONUS;	
		if (oldLevel < getLevelForXP(c.playerXP[skill])) {
			if (c.playerLevel[skill] < c.getLevelForXP(c.playerXP[skill]) && skill != 3 && skill != 5)
				c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
			levelUp(skill);
			c.gfx100(199);
			requestUpdates();
		}
		setSkillLevel(skill, c.playerLevel[skill], c.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}
	
	/**
	 * Initializes a player upon login
	 * @param playerIsMember 0 free, 1 Member
	 * @param index playerId
	 */
	public void initializePlayer(int playerIsMember, int index) {
		c.outStream.createFrame(249);
		c.outStream.writeByteA(playerIsMember); // 1 for members, zero for free
		c.outStream.writeWordBigEndianA(index);
	}

	/**
	 * Show an arrow icon on the selected player.
	 * 
	 * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
	 * @Param j - The player/Npc that the arrow will be displayed above.
	 * @Param k - Keep this set as 0
	 * @Param l - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {
		// synchronized(c) {
		c.outStream.createFrame(254);
		c.outStream.writeByte(i);

		if (i == 1 || i == 10) {
			c.outStream.writeWord(j);
			c.outStream.writeWord(k);
			c.outStream.writeByte(l);
		} else {
			c.outStream.writeWord(k);
			c.outStream.writeWord(l);
			c.outStream.writeByte(j);
		}

	}

	public void removeObject(int x, int y) {
		object(-1, x, x, 10, 10);
	}

	private void objectToRemove(int X, int Y) {
		object(-1, X, Y, 10, 10);
	}

	private void objectToRemove2(int X, int Y) {
		object(-1, X, Y, -1, 0);
	}

	public void removeObjects() {
		objectToRemove(2638, 4688);
		objectToRemove2(2635, 4693);
		objectToRemove2(2634, 4693);
	}

	public boolean checkForFlags() {
		int[][] itemsToCheck = { { 995, 100000000 }, { 35, 5 }, { 667, 5 }, { 2402, 5 }, { 746, 5 }, { 4151, 150 },
				{ 565, 100000 }, { 560, 100000 }, { 555, 300000 }, { 11235, 10 } };
		for (int j = 0; j < itemsToCheck.length; j++) {
			if (itemsToCheck[j][1] < c.getInventory().getTotalCount(itemsToCheck[j][0]))
				return true;
		}
		return false;
	}

	public void addStarter() {
		c.getInventory().addItem(995, 50000);
	}

	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < c.playerEquipment.length; j++) {
			if (c.playerEquipment[j] > 0)
				count++;
		}
		return count;
	}

	public void useOperate(int itemId) {
		switch (itemId) {
		}
	}

	public void getSpeared(int otherX, int otherY) {
		int x = c.absX - otherX;
		int y = c.absY - otherY;
		if (x > 0)
			x = 1;
		else if (x < 0)
			x = -1;
		if (y > 0)
			y = 1;
		else if (y < 0)
			y = -1;
		moveCheck(x, y);
		c.lastSpear = System.currentTimeMillis();
	}

	public void moveCheck(int xMove, int yMove) {
		c.getMovement().movePlayer(c.absX + xMove, c.absY + yMove, c.heightLevel);
	}

	/**
	 * Handles the different types of combat styles
	 */
	public void handleWeaponStyle() {
		if (c.fightMode == 0) {
			c.getActionSender().setConfig(43, c.fightMode);
		} else if (c.fightMode == 1) {
			c.getActionSender().setConfig(43, 3);
		} else if (c.fightMode == 2) {
			c.getActionSender().setConfig(43, 1);
		} else if (c.fightMode == 3) {
			c.getActionSender().setConfig(43, 2);
		}
	}
}