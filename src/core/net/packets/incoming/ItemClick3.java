package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;

@SuppressWarnings("all")
public class ItemClick3 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId11 = c.getInStream().readSignedWordBigEndianA();
		int itemId1 = c.getInStream().readSignedWordA();
		int itemId = c.getInStream().readSignedWordA();
		switch (itemId) {
		}
	}
}