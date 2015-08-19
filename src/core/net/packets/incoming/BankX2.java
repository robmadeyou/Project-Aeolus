package core.net.packets.incoming;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.util.InterfaceConstants;
import core.net.packets.PacketType;

/**
 * Bank X Items
 **/
public class BankX2 implements PacketType {
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int Xamount = c.getInStream().readDWord();
		if (Xamount == 0)
			Xamount = 1;

		if (c.getRights().equal(Rights.DEVELOPER) && Configuration.SERVER_DEBUG) {
			c.sendMessage("BankX2: - InterfaceId: " + c.xInterfaceId
					+ " removeId: " + c.xRemoveId + " slot: " + c.xRemoveSlot);
		}

		switch (c.xInterfaceId) {

		case InterfaceConstants.STORE_BANK:
			c.getItems().bankItem(c.playerItems[c.xRemoveSlot], c.xRemoveSlot,
					Xamount);
			break;

		case InterfaceConstants.WITHDRAW_BANK:
			c.getItems().fromBank(c.bankItems[c.xRemoveSlot], c.xRemoveSlot,
					Xamount);
			break;

		case 3322:
			if (c.duelStatus <= 0) {
				c.getTrade().tradeItem(c.xRemoveId, c.xRemoveSlot, Xamount);
			} else {
				c.getDuel().stakeItem(c.xRemoveId, c.xRemoveSlot, Xamount);
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				c.getTrade().fromTrade(c.xRemoveId, c.xRemoveSlot, Xamount);
			}
			break;

		case 6669:
			c.getDuel().fromDuel(c.xRemoveId, c.xRemoveSlot, Xamount);
			break;

		case InterfaceConstants.DEPOSIT_BOX:
			c.getItems().bankItem(c.playerItems[c.xRemoveSlot], c.xRemoveSlot,
					Xamount);
			c.getInventory().resetItems(7423);
			break;
		}
	}
}