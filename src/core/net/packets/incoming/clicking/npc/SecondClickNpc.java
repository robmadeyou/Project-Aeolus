package core.net.packets.incoming.clicking.npc;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.plugin.PluginManager;

public class SecondClickNpc {

	public static void handleClick(Player c, int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch (npcType) {
		
		default:
			PluginManager.callFunc("npcClick2_" + npcType);
			if (c.getRights().equal(Rights.DEVELOPER))
				c.sendMessage("Second Click Npc: " + npcType);
			break;	
		}
	}
	
}
