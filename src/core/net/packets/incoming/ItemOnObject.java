package core.net.packets.incoming;

/**
 * @author Ryan / Lmctruck30
 */

import core.game.model.entity.player.Player;
import core.game.model.item.UseItem;
import core.net.packets.PacketType;

@SuppressWarnings("all")
public class ItemOnObject implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		/*
		 * a = ?
		 * b = ?
		 */
		
		int a = c.getInStream().readUnsignedWord();
		int objectId = c.getInStream().readLEShort();
		int objectY = c.getInStream().readLEShortA();
		int b = c.getInStream().readUnsignedWord();
		int objectX = c.getInStream().readLEShortA();
		int itemId = c.getInStream().readUnsignedWord();
		UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);
		
	}

}
