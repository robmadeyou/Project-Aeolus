package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;


/**
 * Remove Item
 **/
@SuppressWarnings("all")
public class RemoveItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readUnsignedWordA();
		int removeSlot = c.getInStream().readUnsignedWordA();
		int removeId = c.getInStream().readUnsignedWordA();
		int shop = 0;
		int value = 0;
		String name = "null";
	
		switch(interfaceId) {
			
			case 1688:
			c.getItems().removeItem(removeId, removeSlot);
			break;
			
			case 5064:
			c.getItems().bankItem(removeId, removeSlot, 1);
			break;
			
			case 5382:
			c.getItems().fromBank(removeId, removeSlot, 1);
			break;
			
			case 3900:
			c.getShops().buyFromShopPrice(removeId, removeSlot);
			break;
			
			case 3823:
			c.getShops().sellToShopPrice(removeId, removeSlot);
			break;
			
			case 3322:
			if(c.duelStatus <= 0) { 
                c.getContentManager().getTrading().tradeItem(removeId, removeSlot, 1);
           	} else {
				c.getContentManager().getDueling().stakeItem(removeId, removeSlot, 1);
			}
			break;
			
			case 3415:
			if(c.duelStatus <= 0) { 
				c.getContentManager().getTrading().fromTrade(removeId, removeSlot, 1);
           	} 
			break;
			
			case 6669:
			c.getContentManager().getDueling().fromDuel(removeId, removeSlot, 1);
			break;
			

		}
	}
			
}
