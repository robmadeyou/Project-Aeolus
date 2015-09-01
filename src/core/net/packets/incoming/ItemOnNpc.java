package core.net.packets.incoming;

import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.player.Player;
import core.game.model.item.UseItem;
import core.net.packets.PacketType;


public class ItemOnNpc implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedShortA();
		int i = c.getInStream().readSignedShortA();
		int slot = c.getInStream().readLEShort();
		int npcId = MobHandler.npcs[i].npcType;
		
		UseItem.ItemonNpc(c, itemId, npcId, slot);
	}
}
