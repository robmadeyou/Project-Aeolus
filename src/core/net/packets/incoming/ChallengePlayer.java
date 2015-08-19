package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.net.packets.PacketType;

/**
 * Challenge Player
 **/
public class ChallengePlayer implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {		
		switch(packetType) {
			case 128:
			int answerPlayer = c.getInStream().readUnsignedWord();
			if(PlayerHandler.players[answerPlayer] == null) {
				return;
			}			
			
			if(c.arenas() || c.duelStatus == 5) {
				c.sendMessage("You can't challenge inside the arena!");
				return;
			}

			c.getDuel().requestDuel(answerPlayer);
			break;
		}		
	}	
}
