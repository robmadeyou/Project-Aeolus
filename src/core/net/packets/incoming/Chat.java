package core.net.packets.incoming;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Punishments;
import core.game.util.Censor;
import core.game.util.Misc;
import core.game.util.log.impl.ReportLogger;
import core.net.packets.PacketType;

/**
 * Receives the client's chat messages, and gets handled here.
 * Censoring if enabled and kicking players who spam.
 **/
public class Chat implements PacketType {

	// Anti-Spam Check By: A Duck Tale
	
	/**
	 * Amount of spams allowed
	 */
	int spamsAllowed = 10;
	
	/**
	 * Time to wait in milliseconds
	 */
	int timeAllowed = 5 * 1000;

	/**
	 * Initializing variables
	 */
	int spamCount = 0;
	long timeWaited = 0;
	long startTime = 0;
	long endTime = 0;
	byte[] message = null;
	byte[] storeMessage = null;
	
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.setChatTextEffects(c.getInStream().readUnsignedByteS());
		c.setChatTextColor(c.getInStream().readUnsignedByteS());
        c.setChatTextSize((byte)(c.packetSize - 2));
        c.inStream.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0);
		message = c.getChatText();
		
		String chatText = Misc.textUnpack(message, c.getChatTextSize());
		
		if(Configuration.enableCensor)
		Censor.performCensor(c, chatText);
		
		if (storeMessage == null) {
			storeMessage = message;
		} else if (storeMessage != null) {
			if (storeMessage == message) {
				spamCount++;
				if (startTime == 0) {
					startTime = System.currentTimeMillis();
				}
				if (spamCount >= spamsAllowed) {
					endTime = System.currentTimeMillis();
					timeWaited = endTime - startTime;
					if (timeWaited < timeAllowed) {
						System.out.println("Kicking " + c.playerName
								+ " For spamming.");
						startTime = 0;
						spamCount = 0;
						timeWaited = 0;
						endTime = 0;
						storeMessage = null;
						message = null;
						c.logout();
					} else if (timeWaited >= timeAllowed) {
						startTime = 0;
						spamCount = 0;
						timeWaited = 0;
						endTime = 0;
						storeMessage = null;
						message = null;
					}
				}
			} else if (storeMessage != message) {
				spamCount = 0;
				startTime = 0;
				storeMessage = message;
			}
		}
		if (System.currentTimeMillis() < c.muteEnd) {
			c.sendMessage("You are muted for breaking a rule.");
			return;
		} else {
			c.muteEnd = 0;
		}
		ReportLogger.addText(c.playerName, c.getChatText(), packetSize - 2);
		if (!Punishments.isMuted(c)) {
			c.setChatTextUpdateRequired(true);
		} else {
			c.sendMessage("You are muted for breaking a rule.");
			return;
		}
	}
}

