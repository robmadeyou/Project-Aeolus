package core.net.packets.incoming;

import core.Server;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.world.clipping.region.Region;
import core.net.packets.PacketType;

/**
 * An incoming packet used when a player enters a new map region.
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
		if (player.getRights().equals(Rights.DEVELOPER)) {
		player.sendMessage("Entered a new region: " + Region.getRegion(player.getX(), player.getY()).id());
		}
	}
}