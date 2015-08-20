package core.net.packets.incoming;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.model.item.GameItem;
import core.game.model.item.Item;
import core.game.util.InterfaceConstants;
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

		if (c.getRights().equal(Rights.DEVELOPER) && Configuration.SERVER_DEBUG) {
			c.sendMessage("BankAll: - InterfaceId: " + interfaceId
					+ " removeId: " + removeId + " slot: " + removeSlot);
		}

		switch (interfaceId) {
		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 10);
			break;

		case 3823:
			c.getShops().sellItem(removeId, removeSlot, 10);
			break;

		case InterfaceConstants.STORE_BANK:
			if (c.getInventory().getStackable(removeId)) {
				c.getItems().bankItem(c.playerItems[removeSlot], removeSlot,
						c.playerItemsN[removeSlot]);
			} else {
				c.getItems().bankItem(c.playerItems[removeSlot], removeSlot,
						c.getInventory().itemAmount(c.playerItems[removeSlot]));
			}
			break;

		case InterfaceConstants.WITHDRAW_BANK:
			c.getItems().fromBank(c.bankItems[removeSlot], removeSlot,
					c.bankItemsN[removeSlot]);
			break;

		case 3322:
			if (c.duelStatus <= 0) {
				if (c.getInventory().getStackable(removeId)) {
					c.getTrade().tradeItem(removeId, removeSlot,
							c.playerItemsN[removeSlot]);
				} else {
					c.getTrade().tradeItem(removeId, removeSlot, 28);
				}
			} else {
				if (c.getInventory().getStackable(removeId)
						|| Item.itemIsNote(removeId)) {
					c.getDuel().stakeItem(removeId, removeSlot,
							c.playerItemsN[removeSlot]);
				} else {
					c.getDuel().stakeItem(removeId, removeSlot, 28);
				}
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				if (c.getInventory().getStackable(removeId)) {
					for (GameItem item : c.getTrade().offeredItems) {
						if (item.id == removeId) {
							c.getTrade()
									.fromTrade(
											removeId,
											removeSlot,
											c.getTrade().offeredItems
													.get(removeSlot).amount);
						}
					}
				} else {
					for (GameItem item : c.getTrade().offeredItems) {
						if (item.id == removeId) {
							c.getTrade().fromTrade(removeId, removeSlot, 28);
						}
					}
				}
			}
			break;

		case 7295:
			if (c.getInventory().getStackable(removeId)) {
				c.getItems().bankItem(c.playerItems[removeSlot], removeSlot,
						c.playerItemsN[removeSlot]);
				c.getInventory().resetItems(7423);
			} else {
				c.getItems().bankItem(c.playerItems[removeSlot], removeSlot,
						c.getInventory().itemAmount(c.playerItems[removeSlot]));
				c.getInventory().resetItems(7423);
			}
			break;

		case 6669:
			if (c.getInventory().getStackable(removeId)
					|| Item.itemIsNote(removeId)) {
				for (GameItem item : c.getDuel().stakedItems) {
					if (item.id == removeId) {
						c.getDuel().fromDuel(removeId, removeSlot,
								c.getDuel().stakedItems.get(removeSlot).amount);
					}
				}

			} else {
				c.getDuel().fromDuel(removeId, removeSlot, 28);
			}
			break;

		case InterfaceConstants.DEPOSIT_BOX:
			if (c.getInventory().getStackable(removeId)) {
				c.getItems().bankItem(c.playerItems[removeSlot], removeSlot,
						c.playerItemsN[removeSlot]);
			} else {
				c.getItems().bankItem(c.playerItems[removeSlot], removeSlot,
						c.getInventory().itemAmount(c.playerItems[removeSlot]));
			}
			c.getInventory().resetItems(7423);
			break;

		}
	}

}
