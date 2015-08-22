package core.net.packets.incoming;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.entity.player.Rights;
import core.net.packets.PacketType;

/**
 * Clicking Actions, previously was known as ClickingStuff but was very misleading
 * @author 7Winds
 */
public class ClickingAction implements PacketType {
	
	public static final int CLOSE_WINDOW = 130;

	@Override
	public void processPacket(Player p, int packetType, int packetSize) {
		
		switch (packetType) {
		
		case CLOSE_WINDOW:
			if (p.isTrading) {
				if(!p.acceptedTrade) {
					p.getTrade().declineTrade();
				}
			}

			Player o = PlayerHandler.players[p.duelingWith];
			if(o != null) {
				if(p.duelStatus >= 1 && p.duelStatus <= 4) {
					p.getDuel().declineDuel();
					o.getDuel().declineDuel();
				}
			}
			
			if (p.isBanking) {
				p.isBanking = false;
			}
			
			if(p.duelStatus == 6) {
				p.getDuel().claimStakedItems();		
			}
			if (Configuration.SERVER_DEBUG && p.getRights().equals(Rights.DEVELOPER))
				p.sendMessage("ClickingAction - Close Window: " + "packetType " +  packetType + " " + "packetSize: " + packetSize);
			break;		
		}
	}		
}
