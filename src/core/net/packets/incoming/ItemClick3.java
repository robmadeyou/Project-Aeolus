package core.net.packets.incoming;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.net.packets.PacketType;

@SuppressWarnings("all")
public class ItemClick3 implements PacketType {

	@Override
	public void processPacket(Player p, int packetType, int packetSize) {
		int itemId11 = p.getInStream().readSignedWordBigEndianA();
		int itemId1 = p.getInStream().readSignedWordA();
		int itemId = p.getInStream().readSignedWordA();

		if (p.getRights().equal(Rights.DEVELOPER) && Configuration.SERVER_DEBUG) {
			p.sendMessage("ItemClick3: itemId - " + itemId + " itemId1: " + itemId1 + " itemId11: " + itemId11);
		}
		
	}
}