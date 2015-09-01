package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;

/**
 * Move Items
 **/
public class MoveItems implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int somejunk = c.getInStream().readUShortA(); //junk
		int itemFrom =  c.getInStream().readUShortA();// slot1
		int itemTo = (c.getInStream().readUShortA() -128);// slot2
		c.getInventory().moveItems(itemFrom, itemTo, somejunk);
	}
}
