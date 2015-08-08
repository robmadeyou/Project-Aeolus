package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.game.model.item.GameItem;
import core.game.model.item.Item;
import core.net.packets.PacketType;

/**
 * Bank All Items
 **/
public class BankAll implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
	int removeSlot = c.getInStream().readUnsignedWordA();
	int interfaceId = c.getInStream().readUnsignedWord();
	int removeId = c.getInStream().readUnsignedWordA();
	
		switch(interfaceId){			
			case 3900:
			c.getShops().buyItem(removeId, removeSlot, 10);
			break;
			
			case 3823:
			c.getShops().sellItem(removeId, removeSlot, 10);
			break;
			
			case 5064:
			if (c.getInventory().getStackable(removeId)) {
				c.getItems().bankItem(c.playerItems[removeSlot] , removeSlot, c.playerItemsN[removeSlot]);
			} else {
				c.getItems().bankItem(c.playerItems[removeSlot] , removeSlot, c.getInventory().itemAmount(c.playerItems[removeSlot]));
			}
			break;
			
			case 5382:
			c.getItems().fromBank(c.bankItems[removeSlot] , removeSlot, c.bankItemsN[removeSlot]);
			break;	
			
			case 3322:
			if(c.duelStatus <= 0) { 
				if(c.getInventory().getStackable(removeId)){
					c.getContentManager().getTrading().tradeItem(removeId, removeSlot, c.playerItemsN[removeSlot]);
		    	} else {
					c.getContentManager().getTrading().tradeItem(removeId, removeSlot, 28);  
				}
			} else {
				if(c.getInventory().getStackable(removeId) || Item.itemIsNote[removeId]) {
					c.getContentManager().getDueling().stakeItem(removeId, removeSlot, c.playerItemsN[removeSlot]);
				} else {
					c.getContentManager().getDueling().stakeItem(removeId, removeSlot, 28);
				}
			}
			break;
			
			case 3415: 
			if(c.duelStatus <= 0) { 
				if(c.getInventory().getStackable(removeId)) {
					for (GameItem item : c.getContentManager().getTrading().offeredItems) {
						if(item.id == removeId) {
							c.getContentManager().getTrading().fromTrade(removeId, removeSlot, c.getContentManager().getTrading().offeredItems.get(removeSlot).amount);
						}
					}
				} else {
					for (GameItem item : c.getContentManager().getTrading().offeredItems) {
						if(item.id == removeId) {
							c.getContentManager().getTrading().fromTrade(removeId, removeSlot, 28);
						}
					}
				}
            } 
			break;
			
			case 7295:
			if (c.getInventory().getStackable(removeId)) {
			c.getItems().bankItem(c.playerItems[removeSlot] , removeSlot, c.playerItemsN[removeSlot]);
			c.getInventory().resetItems(7423);
			} else {
			c.getItems().bankItem(c.playerItems[removeSlot] , removeSlot, c.getInventory().itemAmount(c.playerItems[removeSlot]));
			c.getInventory().resetItems(7423);
			}
			break;
			
			case 6669:
			if(c.getInventory().getStackable(removeId) || Item.itemIsNote[removeId]) {
				for (GameItem item : c.getContentManager().getDueling().stakedItems) {
					if(item.id == removeId) {
						c.getContentManager().getDueling().fromDuel(removeId, removeSlot, c.getContentManager().getDueling().stakedItems.get(removeSlot).amount);
					}
				}
						
			} else {
				c.getContentManager().getDueling().fromDuel(removeId, removeSlot, 28);
			}
			break;

		}
	}

}
