package core.net.packets.incoming.clicking.object;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.plugin.PluginManager;

public class SecondClickObject {

	public static void handleClick(Player player, int objectType, int obX, int obY) {
		player.clickObjectType = 0;
		switch (objectType) {

		default:
			PluginManager.callFunc("objectClick2_" + objectType, player, objectType,
					obX, obY);
			if (player.getRights().equal(Rights.DEVELOPER))
			player.sendMessage("Second Click Object: " + objectType + " " + obX + " " + obY);
			break;
		}
	}
	
}
