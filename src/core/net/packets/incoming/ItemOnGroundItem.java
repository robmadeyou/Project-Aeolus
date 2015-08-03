package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.util.Misc;
import core.net.packets.PacketType;

@SuppressWarnings("all")
public class ItemOnGroundItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int a1 = c.getInStream().readSignedWordBigEndian();
		int itemUsed = c.getInStream().readSignedWordA();
		int groundItem = c.getInStream().readUnsignedWord();
		int gItemY = c.getInStream().readSignedWordA();
		int itemUsedSlot = c.getInStream().readSignedWordBigEndianA();
		int gItemX = c.getInStream().readUnsignedWord();
		
		switch(itemUsed) {
		
		default:
			if(c.getRights().greaterOrEqual(Rights.ADMINISTRATOR))
				Misc.println("ItemUsed "+itemUsed+" on Ground Item "+groundItem);
			break;
		}
	}

}
