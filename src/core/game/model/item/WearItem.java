package core.game.model.item;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;


/**
 * Wear Item
 **/
@SuppressWarnings("all")
public class WearItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.wearId = c.getInStream().readUnsignedWord();
		c.wearSlot = c.getInStream().readUnsignedWordA();
		c.interfaceId = c.getInStream().readUnsignedWordA();
		
		int oldCombatTimer = c.attackTimer;
		if (c.playerIndex > 0 || c.npcIndex > 0)
			c.getCombat().resetPlayerAttack();
		c.getEquipment().wearItem(c.wearId, c.wearSlot);
	}
}