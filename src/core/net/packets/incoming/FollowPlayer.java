package core.net.packets.incoming;

import core.Server;
import core.game.model.entity.player.Player;
import core.net.packets.PacketType;

/**
 * Follow Player
 **/
public class FollowPlayer implements PacketType {
    
    @SuppressWarnings("static-access")
	@Override
    public void processPacket(Player player, int packetType, int packetSize) {
            int followPlayer = player.getInStream().readUnsignedWordBigEndian();
            if(Server.playerHandler.players[followPlayer] == null) {
                    return;
            }
            player.playerIndex = 0;
            player.npcIndex = 0;
            player.mageFollow = false;
            player.usingBow = false;
            player.usingRangeWeapon = false;
            player.followDistance = 1;
            player.followId = followPlayer;
    }      
}
