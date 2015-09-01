package core.net.packets.incoming;

import core.Configuration;
import core.Server;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.net.packets.PacketType;

/**
 * Drop Item
 **/
public class DropItem implements PacketType {

	@Override
	public void processPacket(Player p, int packetType, int packetSize) {
		int itemId = p.getInStream().readUShortA();
		p.getInStream().readUnsignedByte();
		p.getInStream().readUnsignedByte();
		int slot = p.getInStream().readUShortA();
		if (p.arenas()) {
			p.sendMessage("You can't drop items inside the arena!");
			return;
		}

		boolean droppable = true;
		for (int i : Configuration.UNDROPPABLE_ITEMS) {
			if (i == itemId) {
				droppable = false;
				break;
			}
		}
		if (p.playerItemsN[slot] != 0 && itemId != -1
				&& p.playerItems[slot] == itemId + 1) {
			if (droppable) {
				if (p.underAttackBy > 0) {
					if (p.getShops().getItemShopValue(itemId) > 1000) {
						p.sendMessage("You may not drop items worth more than 1000 while in combat.");
						return;
					}
				}
				Server.itemHandler.createGroundItem(p, itemId, p.getX(),
						p.getY(), p.playerItemsN[slot], p.getId());
				p.getInventory().deleteItem(itemId, slot, p.playerItemsN[slot]);
				if (Configuration.enableSound) {
					p.sendMessage("test");
					p.getActionSender().sendSound(p.getSound().DROPITEM);
				}
			} else {
				p.sendMessage("This items cannot be dropped.");
			}
		}

		if (p.getRights().equals(Rights.DEVELOPER)
				&& Configuration.SERVER_DEBUG) {
			p.sendMessage("ItemDropped: " + itemId);
		}
	}
}
