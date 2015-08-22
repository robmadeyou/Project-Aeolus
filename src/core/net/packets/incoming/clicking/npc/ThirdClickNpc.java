package core.net.packets.incoming.clicking.npc;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.plugin.PluginManager;

public class ThirdClickNpc {

	public static void handleClick(Player c, int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		
		if (c.getRights().equal(Rights.DEVELOPER))
			c.sendMessage("Third Click Npc: " + npcType);
		
		switch (npcType) {
		
		default:
			PluginManager.callFunc("npcClick3_" + npcType);
			break;	
		}
	}
	
}
