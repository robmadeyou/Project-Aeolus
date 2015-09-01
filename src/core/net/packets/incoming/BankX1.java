package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;
/**
 * Bank X Items
 **/
public class BankX1 implements PacketType {

	public static final int PART1 = 135;
	public static final int	PART2 = 208;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		if (packetType == 135) {
			c.xRemoveSlot = c.getInStream().readLEShort();
			c.xInterfaceId = c.getInStream().readUShortA();
			c.xRemoveId = c.getInStream().readLEShort();
		}
		if (c.xInterfaceId == 3900) {
			c.getShops().buyItem(c.xRemoveId, c.xRemoveSlot, 20);//buy 20
			c.xRemoveSlot = 0;
			c.xInterfaceId = 0;
			c.xRemoveId = 0;
			return;
		}

		if(packetType == PART1) {
			synchronized(c) {
				c.getOutStream().createFrame(27);
			}			
		}
	
	}
}
