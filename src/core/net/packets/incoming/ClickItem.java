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
		int junk = c.getInStream().readLEShortA();
		int itemSlot = c.getInStream().readUShortA();
		int itemId = c.getInStream().readLEUShort();
		if (itemId != c.playerItems[itemSlot] - 1) {
			return;
		}
		if (c.getFood().isFood(itemId))
			c.getFood().eat(itemId, itemSlot);
	}
}
