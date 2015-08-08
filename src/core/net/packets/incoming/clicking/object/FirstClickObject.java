package core.net.packets.incoming.clicking.object;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.plugin.PluginManager;

public class FirstClickObject {
	
	public static void handleClick(Player c, int objectType, int obX, int obY) {
		System.out.println("Handle click is being performed");
		c.clickObjectType = 0;
		switch (objectType) {
		
		default:
			PluginManager.callFunc("objectClick1_" + objectType, c, objectType,
					obX, obY);
			if (c.getRights().equal(Rights.DEVELOPER))
			c.sendMessage("First Click Object: " + objectType + " " + objectType + " " + obX + " " + obY);
			break;
		}
	}
}
