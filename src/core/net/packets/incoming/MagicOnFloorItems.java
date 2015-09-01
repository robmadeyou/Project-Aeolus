package core.net.packets.incoming;

import core.Server;
import core.game.model.entity.player.Player;
import core.net.packets.PacketType;


/**
 * Magic on floor items
 **/
@SuppressWarnings("all")
public class MagicOnFloorItems implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemY = c.getInStream().readLEShort();
		int itemId = c.getInStream().readUnsignedWord();
		int itemX = c.getInStream().readLEShort();
		int spellId = c.getInStream().readUShortA();

		if(!Server.itemHandler.itemExists(itemId, itemX, itemY)) {
			c.stopMovement();
			return;
		}
		c.usingMagic = true;
		if(!c.getCombat().checkMagicReqs(51)) {
			c.stopMovement();
			return;
		}
		
		if(c.goodDistance(c.getX(), c.getY(), itemX, itemY, 12)) {
			int offY = (c.getX() - itemX) * -1;
			int offX = (c.getY() - itemY) * -1;
			c.teleGrabX = itemX;
			c.teleGrabY = itemY;
			c.teleGrabItem = itemId;
			c.turnPlayerTo(itemX, itemY);
			c.teleGrabDelay = System.currentTimeMillis();
			c.startAnimation(c.MAGIC_SPELLS[51][2]);
			c.gfx100(c.MAGIC_SPELLS[51][3]);
			c.getActionSender().createPlayersStillGfx(144, itemX, itemY, 0, 72);
			c.getActionSender().createPlayersProjectile(c.getX(), c.getY(), offX, offY, 50, 70, c.MAGIC_SPELLS[51][4], 50, 10, 0, 50);
			c.getActionSender().addSkillXP(c.MAGIC_SPELLS[51][7], 6);
			c.getActionSender().refreshSkill(6);
			c.stopMovement();
		}
	}

}
