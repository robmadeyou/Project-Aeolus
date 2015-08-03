package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.net.packets.PacketType;
import core.net.packets.incoming.commands.Command;
import core.net.packets.incoming.commands.impl.AdministratorCommands;
import core.net.packets.incoming.commands.impl.DeveloperCommands;
import core.net.packets.incoming.commands.impl.PlayerCommands;

/**
 * Command Packet used to construct a piece of data to be between the client and
 * server.
 * 
 * @author 7Winds
 **/
public class CommandPacket implements PacketType {

	private Command playerCommand = new PlayerCommands();
	private Command adminCommand = new AdministratorCommands();
	private Command developerCommand = new DeveloperCommands();

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		String cmd = c.getInStream().readString();

		String[] cmd2 = cmd.split(" ");

		if (c.getRights().greaterOrEqual(playerCommand.getRights()))
			playerCommand.execute(c, cmd2);
		if (c.getRights().greaterOrEqual(adminCommand.getRights()))
			adminCommand.execute(c, cmd2);
		if (c.getRights().greaterOrEqual(developerCommand.getRights()))
			developerCommand.execute(c, cmd2);
		else
			c.sendMessage("I do not have access to do this.");
	}
}
