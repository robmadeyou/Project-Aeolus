package core.net.packets.incoming.commands;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;

/**
 * An interface {@link Command} used to identify classes that implement this interface as a command.
 * @author 7Winds
 */
public interface Command {
	
	public void execute(Player c, String[] command);
	
	public Rights getRights();
}
