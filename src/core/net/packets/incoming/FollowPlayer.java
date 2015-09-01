package core.net.packets.incoming;

import core.Configuration;
import core.Server;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.net.packets.PacketType;

/**
 * Follow Player
 **/
public class FollowPlayer implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player p, int packetType, int packetSize) {
		int followPlayer = p.getInStream().readLEUShort();
		if (Server.playerHandler.players[followPlayer] == null) {
			return;
		}
		p.playerIndex = 0;
		p.npcIndex = 0;
		p.mageFollow = false;
		p.usingBow = false;
		p.usingRangeWeapon = false;
		p.followDistance = 2;
		p.followPlayerId = followPlayer;

		if (p.getRights().equals(Rights.DEVELOPER)
				&& Configuration.SERVER_DEBUG) {
			p.sendMessage("FollowId: " + followPlayer);
		}
	}
}
