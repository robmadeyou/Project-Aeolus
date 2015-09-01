package core.net.packets.incoming;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.net.packets.PacketType;


/**
 * Remove Item
 **/
@SuppressWarnings("all")
public class RemoveItem implements PacketType {

	@Override
	public void processPacket(Player p, int packetType, int packetSize) {
		int interfaceId = p.getInStream().readUShortA();
		int removeSlot = p.getInStream().readUShortA();
		int removeId = p.getInStream().readUShortA();
		int shop = 0;
		int value = 0;
		String name = "null";
	
		switch(interfaceId) {
			
			case 1688:
			p.getEquipment().removeItem(removeId, removeSlot);
			break;
			
			case 5064:
			p.getItems().bankItem(removeId, removeSlot, 1);
			break;
			
			case 5382:
			p.getItems().fromBank(removeId, removeSlot, 1);
			break;
			
			case 3900:
			p.getShops().buyFromShopPrice(removeId, removeSlot);
			break;
			
			case 3823:
			p.getShops().sellToShopPrice(removeId, removeSlot);
			break;
			
			case 3322:
			if(p.duelStatus <= 0) { 
                p.getTrade().tradeItem(removeId, removeSlot, 1);
           	} else {
				p.getDuel().stakeItem(removeId, removeSlot, 1);
			}
			break;
			
			case 3415:
			if(p.duelStatus <= 0) { 
				p.getTrade().fromTrade(removeId, removeSlot, 1);
           	} 
			break;
			
			case 6669:
			p.getDuel().fromDuel(removeId, removeSlot, 1);
			break;

			default:
				if (Configuration.SERVER_DEBUG && p.getRights().equals(Rights.DEVELOPER)) {
					p.sendMessage("RemoveItem : InterfaceId - " + interfaceId);
				}
				break;
		}
	}
			
}
