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
