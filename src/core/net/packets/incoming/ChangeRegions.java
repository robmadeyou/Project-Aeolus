package core.net.packets.incoming;

import core.Server;
import core.game.model.entity.player.Player;
import core.net.packets.PacketType;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		Server.objectHandler.updateObjects(player);
		Server.itemHandler.reloadItems(player);
		//Server.objectManager.loadObjects(c);
		player.updateSigns();
		player.saveFile = true;
		if(player.skullTimer > 0) {
			player.isSkulled = true;	
			player.headIconPk = 0;
			player.getActionSender().requestUpdates();
		}
	}
}