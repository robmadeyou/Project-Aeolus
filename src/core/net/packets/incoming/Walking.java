package core.net.packets.incoming;

import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.net.packets.PacketType;

public class Walking implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {

		if (c.isBusy()) {
			if (c.isTrading) {
				c.sendMessage("You must decline the trade before trying to walk.");
			} else if (c.isBanking) {
				c.sendMessage("You must close the bank before trying to walk.");
			} else if (c.isShopping) {
				c.sendMessage("You must close the shop before trying to walk.");
			} else {
			c.sendMessage("You are busy and cannot walk.");
			}
			return;
		}
		
		if (c.duelCount != 0) {
			c.sendMessage("You must wait " + c.duelCount + " seconds before walking.");
			return;
		}
		
		if (c.canChangeAppearance) {
			c.canChangeAppearance = false;
		}

		if (c.openDuel && c.duelStatus != 5) {
			Player o = PlayerHandler.players[c.duelingWith];
			if (o != null)
				o.getDuel().declineDuel();
			c.getDuel().declineDuel();
		}
		if ((c.duelStatus >= 1 && c.duelStatus <= 4) || c.duelStatus == 6) {
			if (c.duelStatus == 6) {
				c.getDuel().claimStakedItems();
			}
			return;
		}

		if (c.teleporting) {
			c.startAnimation(65535);
			c.teleporting = false;
			c.isRunning = false;
			c.gfx0(-1);
			c.startAnimation(-1);
		}
		c.walkingToItem = false;
		c.clickNpcType = -1;
		c.clickObjectType = -1;
		if (c.tradeStatus >= 0) {
			c.tradeStatus = 0;
		}
		if (packetType == 248 || packetType == 164) {
			c.clickObjectType = 0;
			c.clickNpcType = 0;
			c.faceUpdate(0);
			c.npcIndex = 0;
			c.playerIndex = 0;
			if (c.followPlayerId > 0 || c.followMobId > 0)
				c.getActionSender().resetFollow();
		}
		if (c.canWalk == false) {
			return;
		}
		c.getActionSender().removeAllWindows();
		if (c.duelRule[1] && c.duelStatus == 5) {
			if (PlayerHandler.players[c.duelingWith] != null) {
				if (!c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.duelingWith].getX(),
						PlayerHandler.players[c.duelingWith].getY(), 1)
						|| c.attackTimer == 0) {
					c.sendMessage("Walking has been disabled in this duel!");
				}
			}
			c.playerIndex = 0;
			return;
		}

		if (c.freezeTimer > 0) {
			if (PlayerHandler.players[c.playerIndex] != null) {
				if (c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.playerIndex].getX(),
						PlayerHandler.players[c.playerIndex].getY(), 1)
						&& packetType != 98) {
					c.playerIndex = 0;
					return;
				}
			}
			if (packetType != 98) {
				c.sendMessage("A magical force stops you from moving.");
				c.playerIndex = 0;
			}
			return;
		}

		if (System.currentTimeMillis() - c.lastSpear < 4000) {
			c.sendMessage("You have been stunned.");
			c.playerIndex = 0;
			return;
		}

		if (packetType == 98) {
			c.mageAllowed = true;
		}

		if (c.duelStatus == 6) {
			c.getDuel().claimStakedItems();
			return;
		}

		if (c.duelStatus >= 1 && c.duelStatus <= 4) {
			Player o = PlayerHandler.players[c.duelingWith];
			c.duelStatus = 0;
			o.duelStatus = 0;
			o.getDuel().declineDuel();
			c.getDuel().declineDuel();
		}

		if (c.respawnTimer > 3) {
			return;
		}
		
		if (packetType == 248) {
			packetSize -= 14;
		}
		c.newWalkCmdSteps = (packetSize - 5) / 2;
		if (++c.newWalkCmdSteps > c.walkingQueueSize) {
			c.newWalkCmdSteps = 0;
			return;
		}

		c.newWalkCmdSteps = (packetSize - 5) / 2;
		if (++c.newWalkCmdSteps > c.walkingQueueSize) {
			c.newWalkCmdSteps = 0;
			return;
		}

		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;

		int firstStepX = c.getInStream().readLEShortA()
				- c.getMapRegionX() * 8;
		for (int i = 1; i < c.newWalkCmdSteps; i++) {
			c.getNewWalkCmdX()[i] = c.getInStream().readSignedByte();
			c.getNewWalkCmdY()[i] = c.getInStream().readSignedByte();
		}

		int firstStepY = c.getInStream().readLEShort()
				- c.getMapRegionY() * 8;
		c.setNewWalkCmdIsRunning(c.getInStream().readSignedByteC() == 1);
		for (int i1 = 0; i1 < c.newWalkCmdSteps; i1++) {
			c.getNewWalkCmdX()[i1] += firstStepX;
			c.getNewWalkCmdY()[i1] += firstStepY;
		}
	}
}