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
		int tradeId = c.getInStream().readLEShort();
		c.getActionSender().resetFollow();
		
		if (c.isTrading) {
			return;
		}
		
		if(c.arenas()) {
			c.sendMessage("You can't trade inside the arena!");
			return;
		}
		
		if(c.getRights().equal(Rights.ADMINISTRATOR) && !Configuration.ADMIN_CAN_TRADE) {
			c.sendMessage("Trading as an admin has been disabled.");
			return;
		}
		
        if(tradeId < 1)
            return;
        
		if (tradeId != c.playerId)
			c.getTrade().requestTrade(tradeId);
	}
}