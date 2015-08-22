package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.game.util.Misc;
import core.net.packets.PacketType;
import core.net.packets.incoming.clicking.FirstClickButton;

/**
 * Clicking most buttons
 * @param c
 * @param packetType
 * @param packetSize
 **/
public class ActionButtons implements PacketType {
	
	public static Player p;
	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		int actionButtonId = Misc.hexToInt(c.getInStream().buffer, 0, packetSize);
		//int actionButtonId = c.getInStream().readShort();
		if (c.isDead)
			return;
		FirstClickButton.handleClick(c, actionButtonId);
	}
}