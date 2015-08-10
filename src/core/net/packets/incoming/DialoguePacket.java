package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;


/**
 * Dialogue
 **/
public class DialoguePacket implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		
		if(c.nextChat > 0) {
			c.getDH().sendDialogues(c.nextChat, c.talkingNpc, 0);
		} else {
			c.getDH().sendDialogues(0, -1, 0);
			c.getActionSender().removeAllWindows();
		}		
	}
}
