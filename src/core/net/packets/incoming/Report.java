package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.game.util.log.impl.ReportLogger;
import core.net.packets.PacketType;

/**
 * Report packet used to report a player
 * @author 7Winds
 */
public class Report implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		try {
			ReportLogger.handleReport(player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
