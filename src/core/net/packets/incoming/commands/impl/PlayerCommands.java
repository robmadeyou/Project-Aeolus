package core.net.packets.incoming.commands.impl;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.entity.player.Rights;
import core.net.packets.incoming.commands.Command;

/**
 * A class which will be the container of all the commands accessed by Players,
 * Administrators, and Developers.
 * 
 * @author 7Winds
 */
public class PlayerCommands implements Command {

	@Override
	public void execute(Player player, String[] command) {
		switch (command[0]) {
		case "test":
			player.sendMessage("I have access to player commands.");
			break;

		case "players":
			int amount = PlayerHandler.getPlayerCount();
			player.sendMessage(
					amount > 1 ? "There are currently " + amount + " players online." : "You are the only one online!");
			break;

		case "item":
			try {
				if (command.length == 3) {
					int newItemID = Integer.parseInt(command[1]);
					int newItemAmount = Integer.parseInt(command[2]);
					if ((newItemID <= 20200) && (newItemID >= 0)) {
						player.getInventory().addItem(newItemID, newItemAmount);
					} else {
						player.sendMessage("No such item.");
					}
				} else {
					player.sendMessage("Use as ::item 995 200");
				}
			} catch (Exception e) {
				e.getMessage();
			}
			break;

		case "empty":
			player.getDH().sendOption2("Yes, I want to empty my inventory items.",
					"No, i want to keep my inventory items.");
			player.dialogueAction = 162;
			break;
		}
	}

	@Override
	public Rights getRights() {
		return Rights.PLAYER;
	}
}
