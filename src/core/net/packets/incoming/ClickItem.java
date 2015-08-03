package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;

/**
 * Clicking an item, bury bone, eat food etc
 **/
@SuppressWarnings("all")
public class ClickItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int junk = c.getInStream().readSignedWordBigEndianA();
		int itemSlot = c.getInStream().readUnsignedWordA();
		int itemId = c.getInStream().readUnsignedWordBigEndian();
		if (itemId != c.playerItems[itemSlot] - 1) {
			return;
		}
	}
}
