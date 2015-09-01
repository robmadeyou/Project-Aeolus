package core.net.packets.incoming;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.model.item.UseItem;
import core.net.packets.PacketType;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Player p, int packetType, int packetSize) {
		int usedWithSlot = p.getInStream().readUnsignedWord();
		int itemUsedSlot = p.getInStream().readUShortA();
		int useWith = p.playerItems[usedWithSlot] - 1;
		int itemUsed = p.playerItems[itemUsedSlot] - 1;
		UseItem.ItemonItem(p, itemUsed, useWith);
		
		if (p.getRights().equals(Rights.DEVELOPER) && Configuration.SERVER_DEBUG) {
			p.sendMessage("itemUsed: " + itemUsed + " useWith: " + useWith
					+ " itemUsedSlot: " + itemUsedSlot + " usedWithSlot: " + usedWithSlot);
		}
	}
}
