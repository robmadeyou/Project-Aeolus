package core.game.model.item;

import core.game.model.entity.player.Player;

public class UseItem {
	
	public static void ItemonObject(Player c, int objectID, int objectX,
			int objectY, int itemId) {
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		switch (objectID) {
		}
	}

	public static void ItemonItem(Player c, int itemUsed, int useWith) {
		switch (itemUsed) {
		}
	}

	public static void ItemonNpc(Player c, int itemId, int npcId, int slot) {
		switch (itemId) {
		}
	}
}