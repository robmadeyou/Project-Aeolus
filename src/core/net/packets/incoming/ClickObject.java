package core.net.packets.incoming;

import core.game.event.task.Task;
import core.game.event.task.TaskHandler;
import core.game.model.entity.player.Player;
import core.net.packets.PacketType;

/**
 * Click Object
 */
public class ClickObject implements PacketType {

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252,
			THIRD_CLICK = 70;

	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		c.clickObjectType = c.objectX = c.objectId = c.objectY = 0;
		c.objectYOffset = c.objectXOffset = 0;
		c.getActionSender().resetFollow();
		switch (packetType) {

		case FIRST_CLICK:
			c.objectX = c.getInStream().readLEShortA();
			c.objectId = c.getInStream().readUnsignedWord();
			c.objectY = c.getInStream().readUShortA();
			c.objectDistance = 1;
			if (Math.abs(c.getX() - c.objectX) > 25
					|| Math.abs(c.getY() - c.objectY) > 25) {
				c.resetWalkingQueue();
				break;
			}			
			if (c.goodDistance(c.objectX + c.objectXOffset, c.objectY
					+ c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
				c.getActions().firstClickObject(c.objectId, c.objectX,
						c.objectY);
			} else {
				c.clickObjectType = 1;
				
				TaskHandler.submit(new Task(1, true) {
					
					@Override
					public void execute() {
						if (c.clickObjectType == 1
								&& c.goodDistance(c.objectX + c.objectXOffset,
										c.objectY + c.objectYOffset, c.getX(),
										c.getY(), c.objectDistance)) {
							c.getActions().firstClickObject(c.objectId,
									c.objectX, c.objectY);
							this.cancel();
						}
						if (c.clickObjectType > 1 || c.clickObjectType == 0)
							this.cancel();
					}
					
					@Override
					public void onCancel() {
						c.clickObjectType = 0;
					}
					
				});
			}
			break;

		case SECOND_CLICK:
			c.objectId = c.getInStream().readLEUShortA();
			c.objectY = c.getInStream().readLEShort();
			c.objectX = c.getInStream().readUShortA();
			c.objectDistance = 1;
			
			if (c.goodDistance(c.objectX + c.objectXOffset, c.objectY
					+ c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
				c.getActions().secondClickObject(c.objectId, c.objectX,
						c.objectY);
			} else {
				c.clickObjectType = 2;
				
				TaskHandler.submit(new Task(1, true) {					
					@Override
					public void execute() {
						if (c.clickObjectType == 2
								&& c.goodDistance(c.objectX + c.objectXOffset,
										c.objectY + c.objectYOffset, c.getX(),
										c.getY(), c.objectDistance)) {
							c.getActions().secondClickObject(c.objectId,
									c.objectX, c.objectY);
							this.cancel();
						}
						if (c.clickObjectType < 2 || c.clickObjectType > 2)
							this.cancel();
					}
					
					@Override
					public void onCancel() {
						c.clickObjectType = 0;
					}
					
				});
			}
			break;

		case THIRD_CLICK:
			c.objectX = c.getInStream().readLEShort();
			c.objectY = c.getInStream().readUnsignedWord();
			c.objectId = c.getInStream().readLEUShortA();

			
			if (c.goodDistance(c.objectX + c.objectXOffset, c.objectY
					+ c.objectYOffset, c.getX(), c.getY(), c.objectDistance)) {
				c.getActions().secondClickObject(c.objectId, c.objectX,
						c.objectY);
			} else {
				c.clickObjectType = 3;
				
				TaskHandler.submit(new Task(1, true) {
					
					@Override
					public void execute() {
						if (c.clickObjectType == 3
								&& c.goodDistance(c.objectX + c.objectXOffset,
										c.objectY + c.objectYOffset, c.getX(),
										c.getY(), c.objectDistance)) {
							c.getActions().thirdClickObject(c.objectId,
									c.objectX, c.objectY);
							this.cancel();
						}
						if (c.clickObjectType < 3)
							this.cancel();
					}
					
					
					@Override
					public void onCancel() {
						c.clickObjectType = 0;
					}
					
					
				});
			}
			
		}

	}

	public void handleSpecialCase(Player c, int id, int x, int y) {

	}

}
