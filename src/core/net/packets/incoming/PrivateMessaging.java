package core.net.packets.incoming;

import core.Configuration;
import core.game.GameConstants;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.util.Misc;
import core.net.packets.PacketType;

/**
 * Private messaging, friends etc
 **/
@SuppressWarnings("all")
public class PrivateMessaging implements PacketType {

	public final int ADD_FRIEND = 188, SEND_PM = 126, REMOVE_FRIEND = 215,
			CHANGE_PM_STATUS = 95, REMOVE_IGNORE = 59, ADD_IGNORE = 133;

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		switch (packetType) {
		
		case ADD_FRIEND:
			c.friendUpdate = true;
			long friendToAdd = c.getInStream().readLong();
			boolean canAdd = true;

			for (int i1 = 0; i1 < c.friends.length; i1++) {
				if (c.friends[i1] != 0 && c.friends[i1] == friendToAdd) {
					canAdd = false;
					c.sendMessage(friendToAdd
							+ " is already on your friends list.");
				}
			}
			if (canAdd == true) {
				for (int i1 = 0; i1 < c.friends.length; i1++) {
					if (c.friends[i1] == 0) {
						c.friends[i1] = friendToAdd;
						for (int i2 = 1; i2 < GameConstants.MAX_PLAYERS; i2++) {
							if (PlayerHandler.players[i2] != null
									&& PlayerHandler.players[i2].isActive
									&& Misc.playerNameToInt64(PlayerHandler.players[i2].playerName) == friendToAdd) {
								Player o = (Player) PlayerHandler.players[i2];
								if (o != null) {
									if (PlayerHandler.players[i2].privateChat == 0
											|| (PlayerHandler.players[i2].privateChat == 1 && o
													.getActionSender()
													.isInPM(Misc
															.playerNameToInt64(c.playerName)))) {
										c.getActionSender().loadPM(friendToAdd, 1);
										break;
									}
								}
							}
						}
						break;
					}
				}
			}
			break;

		case SEND_PM:
			long sendMessageToFriendId = c.getInStream().readLong();
			byte pmchatText[] = new byte[100];
			int pmchatTextSize = (byte) (packetSize - 8);
			c.getInStream().readBytes(pmchatText, pmchatTextSize, 0);
			for (int i1 = 0; i1 < c.friends.length; i1++) {
				if (c.friends[i1] == sendMessageToFriendId) {
					boolean pmSent = false;

					for (int i2 = 1; i2 < GameConstants.MAX_PLAYERS; i2++) {
						if (PlayerHandler.players[i2] != null
								&& PlayerHandler.players[i2].isActive
								&& Misc.playerNameToInt64(PlayerHandler.players[i2].playerName) == sendMessageToFriendId) {
							Player o = (Player) PlayerHandler.players[i2];
							if (o != null) {
								if (PlayerHandler.players[i2].privateChat == 0
										|| (PlayerHandler.players[i2].privateChat == 1 && o
												.getActionSender()
												.isInPM(Misc
														.playerNameToInt64(c.playerName)))) {
									o.getActionSender()
											.sendPM(Misc
													.playerNameToInt64(c.playerName),
													c.getRights().getValues(), pmchatText,
													pmchatTextSize);
									pmSent = true;
								}
							}
							break;
						}
					}
					if (!pmSent) {
						c.sendMessage("That player is currently offline.");
						break;
					}
				}
			}
			break;

		case REMOVE_FRIEND:
			c.friendUpdate = true;
			long friendToRemove = c.getInStream().readLong();

			for (int i1 = 0; i1 < c.friends.length; i1++) {
				if (c.friends[i1] == friendToRemove) {
					for (int i2 = 1; i2 < GameConstants.MAX_PLAYERS; i2++) {
						Player o = (Player) PlayerHandler.players[i2];
						if (o != null) {
							if (c.friends[i1] == Misc
									.playerNameToInt64(PlayerHandler.players[i2].playerName)) {
								o.getActionSender().updatePM(c.playerId, 0);
								break;
							}
						}
					}
					c.friends[i1] = 0;
					break;
				}
			}
			break;

		case REMOVE_IGNORE:
			int i = c.getInStream().readInt();
			int i2 = c.getInStream().readInt();
			int i3 = c.getInStream().readInt();
			// for other status changing
			c.getActionSender().handleStatus(i, i2, i3);
			break;

		case CHANGE_PM_STATUS:
			int tradeAndCompete = c.getInStream().readUnsignedByte();
			c.privateChat = c.getInStream().readUnsignedByte();
			int publicChat = c.getInStream().readUnsignedByte();
			for (int i1 = 1; i1 < GameConstants.MAX_PLAYERS; i1++) {
				if (PlayerHandler.players[i1] != null
						&& PlayerHandler.players[i1].isActive == true) {
					Player o = (Player) PlayerHandler.players[i1];
					if (o != null) {
						o.getActionSender().updatePM(c.playerId, 1);
					}
				}
			}
			break;

		case ADD_IGNORE:
			// TODO: fix this so it works :)
			break;

		}

	}
}
