package core.net.packets.incoming;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.net.packets.PacketType;

public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Player p, int packetType, int packetSize) {
		int itemId = p.getInStream().readSignedWordA();
		if (!p.getInventory().playerHasItem(itemId,1))
			return;
		
		if (p.getRights().equal(Rights.DEVELOPER) && Configuration.SERVER_DEBUG) {
			p.sendMessage("ItemClick2: itemId - " + itemId);
		}

	}
}