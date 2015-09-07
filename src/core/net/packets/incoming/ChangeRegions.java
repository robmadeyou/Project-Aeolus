package core.net.packets.incoming;

import core.Configuration;
import core.Server;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.sound.MusicManager;
import core.game.world.clipping.region.Region;
import core.net.packets.PacketConstants;
import core.net.packets.PacketType;

/**
 * An incoming packet used when a player enters a new map region or
 * when the map region has been successfully loaded.
 * @author 7Winds
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
		switch (packetType) {
		
		case PacketConstants.ENTER_REGION:
			if (player.getRights().equals(Rights.DEVELOPER)) {
				player.sendMessage("Entered region: " + Region.getRegion(player.getX(), player.getY()).id());
			}
			break;	
			
		case PacketConstants.LOADED_REGION:
			if (Configuration.enableMusic) {
			player.getActionSender().sendMusic(player, MusicManager.songIdForRegion(player.getRegion()));
			player.sendMessage("Now playing: " + MusicManager.songNameForRegion(player.getRegion()));
			}
			if (player.getRights().equals(Rights.DEVELOPER)) {
			player.sendMessage("Region loaded");
			}
			break;
		}
	}
}