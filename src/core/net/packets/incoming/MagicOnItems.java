package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;


/**
 * Magic on items
 **/
@SuppressWarnings("all")
public class MagicOnItems implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int slot = c.getInStream().readSignedShort();
		int itemId = c.getInStream().readSignedShortA();
		int junk = c.getInStream().readSignedShort();
		int spellId = c.getInStream().readSignedShortA();
		
		c.usingMagic = true;
		c.getPA().magicOnItems(slot, itemId, spellId);
		c.usingMagic = false;

	}

}
