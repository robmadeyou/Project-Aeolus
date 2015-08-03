package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;

/**
 * Slient Packet
 **/
public class SilentPacket implements PacketType {
	
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
			
	}	
}
