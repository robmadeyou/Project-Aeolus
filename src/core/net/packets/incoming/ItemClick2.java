package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;

public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		if (!c.getInventory().playerHasItem(itemId,1))
			return;
		switch (itemId) {
		}
	}
}