package core.net.packets;

import core.game.model.entity.player.Player;
	
public interface PacketType {
	public void processPacket(Player c, int packetType, int packetSize);
}

