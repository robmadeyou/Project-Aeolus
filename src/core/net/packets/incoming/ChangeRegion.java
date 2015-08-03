package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;

public class ChangeRegion implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.getPA().removeObjects();
		//Server.objectManager.loadObjects(c);
	}

}
