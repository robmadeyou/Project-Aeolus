package core.net.packets.incoming.clicking.object;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.plugin.PluginManager;

public class ThirdClickObject {

	public static void handleClick(Player c, int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		c.sendMessage("Object type: " + objectType);
		switch (objectType) {

		default:
			PluginManager.callFunc("objectClick3_" + objectType, c, objectType,
					obX, obY);
			if (c.getRights().equal(Rights.DEVELOPER))
			c.sendMessage("Third Click Object: " + objectType
					+ " " + objectType + " " + obX + " " + obY);
			break;
		}
	}	
}
