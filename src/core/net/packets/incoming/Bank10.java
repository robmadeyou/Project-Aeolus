package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;
/**
 * Bank 10 Items
 **/
public class Bank10 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
	int interfaceId = c.getInStream().readUnsignedWordBigEndian();
	int removeId = c.getInStream().readUnsignedWordA();
	int removeSlot = c.getInStream().readUnsignedWordA();
					
		switch(interfaceId){
			case 1688:
				c.getActionSender().useOperate(removeId);
			break;
			case 3900:
			c.getShops().buyItem(removeId, removeSlot, 5);
			break;
			
			case 3823:
			c.getShops().sellItem(removeId, removeSlot, 5);
			break;	

			case 5064:
			c.getItems().bankItem(removeId, removeSlot, 10);
			break;
			
			case 5382:
			c.getItems().fromBank(removeId, removeSlot, 10);
			break;
			
			case 3322:
			if(c.duelStatus <= 0) { 
                c.getTrade().tradeItem(removeId, removeSlot, 10);
           	} else {
				c.getDuel().stakeItem(removeId, removeSlot, 10);
			}	
			break;
			
			case 3415:
			if(c.duelStatus <= 0) { 
				c.getTrade().fromTrade(removeId, removeSlot, 10);
           	} 
			break;
			
			case 6669:
			c.getDuel().fromDuel(removeId, removeSlot, 10);
			break;
			
		
		}	
	}

}
