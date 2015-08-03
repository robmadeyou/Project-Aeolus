package core.net.packets;

import core.game.model.entity.player.Player;

public interface Packet {

	public void handlePacket(Player player, int packetType, int packetSize);

}