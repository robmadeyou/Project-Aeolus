package core.net.packets.incoming;

import core.Configuration;
import core.Server;
import core.game.event.tick.Tick;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.net.packets.PacketType;

/**
 * Pickup Item
 **/
public class PickupItem implements PacketType {

	@Override
	public void processPacket(final Player p, int packetType, int packetSize) {
		p.walkingToItem = false;
		p.pItemY = p.getInStream().readSignedWordBigEndian();
		p.pItemId = p.getInStream().readUnsignedWord();
		p.pItemX = p.getInStream().readSignedWordBigEndian();
		if (Math.abs(p.getX() - p.pItemX) > 25
				|| Math.abs(p.getY() - p.pItemY) > 25) {
			p.resetWalkingQueue();
			return;
		}
		p.getCombat().resetPlayerAttack();
		if (p.getX() == p.pItemX && p.getY() == p.pItemY) {
			Server.itemHandler.removeGroundItem(p, p.pItemId, p.pItemX,
					p.pItemY, true);
		} else {
			p.walkingToItem = true;
			Player.schedule(p, new Tick(1) {
				@Override
				public void execute() {
					if (!p.walkingToItem)
						this.stop();
					if (p.getX() == p.pItemX && p.getY() == p.pItemY) {
						Server.itemHandler.removeGroundItem(p, p.pItemId,
								p.pItemX, p.pItemY, true);
						this.stop();
					}
				}

				@Override
				public void onStop() {
					p.walkingToItem = false;
				}
			});
		}
		
		if (p.getRights().equals(Rights.DEVELOPER) && Configuration.SERVER_DEBUG) {
			p.sendMessage("Pickup ItemId: " + p.pItemId + " ItemX: " + p.pItemX + " ItemY: " + p.pItemY);
		}
	}
}

