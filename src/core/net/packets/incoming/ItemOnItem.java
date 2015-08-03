package core.net.packets.incoming;

/**
 * @author Ryan / Lmctruck30
 */

import core.game.model.entity.player.Player;
import core.game.model.item.UseItem;
import core.net.packets.PacketType;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int usedWithSlot = c.getInStream().readUnsignedWord();
		int itemUsedSlot = c.getInStream().readUnsignedWordA();
		int useWith = c.playerItems[usedWithSlot] - 1;
		int itemUsed = c.playerItems[itemUsedSlot] - 1;
		UseItem.ItemonItem(c, itemUsed, useWith);
	}

}
