package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.net.packets.PacketType;

/**
 * Follow Player
 **/
public class FollowPlayer implements PacketType {
	
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int followPlayer = c.getInStream().readUnsignedWordBigEndian();
		if(PlayerHandler.players[followPlayer] == null) {
			return;
		}
		c.playerIndex = 0;
		c.npcIndex = 0;
		c.mageFollow = false;
		c.usingBow = false;
		c.usingRangeWeapon = false;
		c.followDistance = 1;
		c.followId = followPlayer;
	}	
}
