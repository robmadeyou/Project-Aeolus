package core.net.packets.incoming;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.net.packets.PacketType;

/**
 * Trading
 */
public class Trade implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int tradeId = c.getInStream().readSignedWordBigEndian();
		c.getActionSender().resetFollow();
		
		if(c.arenas()) {
			c.sendMessage("You can't trade inside the arena!");
			return;
		}
		if(c.getRights().equal(Rights.ADMINISTRATOR) && !Configuration.ADMIN_CAN_TRADE) {
			c.sendMessage("Trading as an admin has been disabled.");
			return;
		}
		if (tradeId != c.playerId)
			c.getContentManager().getTrading().requestTrade(tradeId);
	}
}