package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;


/**
 * Wear Item
 **/
public class WearItem implements PacketType {

	@Override
	public void processPacket(Player p, int packetType, int packetSize) {
		p.wearId = p.getInStream().readUnsignedWord();
		p.wearSlot = p.getInStream().readUnsignedWordA();
		p.interfaceId = p.getInStream().readUnsignedWordA();
		
		if (p.playerIndex > 0 || p.npcIndex > 0)
			p.getCombat().resetPlayerAttack();
		p.getEquipment().wearItem(p.wearId, p.wearSlot);
	}
}