package core.net.packets.incoming.clicking.npc;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.plugin.PluginManager;

/**
 * Class which handles the first click option of an npc
 */
public class FirstClickNpc {

	/**
	 * A method for handling the first option click of an npc
	 * @param c
	 * @param npcType
	 */
	public static void handleClick(Player c, int npcId) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		
		if (c.getRights().equal(Rights.DEVELOPER))
			c.sendMessage("First Click Npc: " + npcId);
		
		switch (npcId) {
		
		case 599:
			c.getActionSender().showInterface(3559);
			c.canChangeAppearance = true;
			break;
		
		default:
			PluginManager.callFunc("npcClick1_" + npcId, c, npcId);
			break;	
		}
	}
	
}
