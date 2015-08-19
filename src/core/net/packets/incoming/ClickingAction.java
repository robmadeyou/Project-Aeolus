package core.net.packets.incoming;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.util.Misc;
import core.net.packets.PacketType;

/**
 * Clicking Actions, previously was known as ClickingStuff but was very misleading
 * @author 7Winds
 */
public class ClickingAction implements PacketType {
	
	public static final int CLOSE_WINDOW = 130;

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		
		switch (packetType) {
		
		case CLOSE_WINDOW:
			if ((Boolean) c.getAttributes().get("isTrading")) {
				if(!c.acceptedTrade) {
					Misc.println("trade reset");
					c.getTrade().declineTrade();
				}
			}

			Player o = PlayerHandler.players[c.duelingWith];
			if(o != null) {
				if(c.duelStatus >= 1 && c.duelStatus <= 4) {
					c.getDuel().declineDuel();
					o.getDuel().declineDuel();
				}
			}
			
			if ((Boolean) c.getAttributes().get("isBanking")) {
				c.getAttributes().put("isBanking", Boolean.FALSE);
			}
			
			if(c.duelStatus == 6) {
				c.getDuel().claimStakedItems();		
			}
			if (Configuration.SERVER_DEBUG)
				System.out.println("ClickingAction - Close Window: " + "packetType " +  packetType + " " + "packetSize: " + packetSize);
			break;		
		}
	}		
}
