package core.net.packets.incoming;

import core.Server;
import core.game.model.entity.player.Player;
import core.game.tick.Tick;
import core.net.packets.PacketType;

/**
 * Pickup Item
 **/
public class PickupItem implements PacketType {

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
	c.walkingToItem = false;
	c.pItemY = c.getInStream().readSignedWordBigEndian();
	c.pItemId = c.getInStream().readUnsignedWord();
	c.pItemX = c.getInStream().readSignedWordBigEndian();
	if (Math.abs(c.getX() - c.pItemX) > 25 || Math.abs(c.getY() - c.pItemY) > 25) {
		c.resetWalkingQueue();
		return;
	}
	c.getCombat().resetPlayerAttack();
	if(c.getX() == c.pItemX && c.getY() == c.pItemY) {
		Server.itemHandler.removeGroundItem(c, c.pItemId, c.pItemX, c.pItemY, true);
	} else {
		c.walkingToItem = true;
		Player.schedule(c, new Tick(1) {
			@Override
			public void execute() {
					if(!c.walkingToItem)
						this.stop();
					if(c.getX() == c.pItemX && c.getY() == c.pItemY) {
						Server.itemHandler.removeGroundItem(c, c.pItemId, c.pItemX, c.pItemY, true);
						this.stop();
					}
				}
			@Override
			public void onStop() {
					c.walkingToItem = false;
				}
			});
	}
	
	}

}
