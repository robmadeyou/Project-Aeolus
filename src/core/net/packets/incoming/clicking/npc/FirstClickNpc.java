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
	public static void handleClick(Player c, int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch (npcType) {
		
		default:
			PluginManager.callFunc("npcClick1_" + npcType);
			c.getDH().sendDialogues(1, npcType, npcType);
			if (c.getRights().equal(Rights.DEVELOPER))
				c.sendMessage("First Click Npc: " + npcType);
			break;	
		}
	}
	
}
