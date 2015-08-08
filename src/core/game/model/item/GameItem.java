package core.game.model.item;

import core.game.model.entity.player.Player;

public class GameItem {
	public int id, amount;
	public boolean stackable = false;

	public GameItem(Player p, int id, int amount) {
		if (p.getInventory().getStackable(id)) {
			stackable = true;
		}
	this.id = id;
	this.amount = amount;
	}
}