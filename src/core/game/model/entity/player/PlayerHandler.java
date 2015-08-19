package core.game.model.entity.player;

import java.util.stream.IntStream;

import core.Configuration;
import core.Server;
import core.game.GameConstants;
import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.player.save.PlayerSave;
import core.game.util.Misc;
import core.game.util.Stream;

public class PlayerHandler {

	public static Object lock = new Object();
	public static Player players[] = new Player[GameConstants.MAX_PLAYERS];
	public static String messageToAll = "";
	
	public static boolean updateAnnounced;
	public static boolean updateRunning;
	public static int updateSeconds;
	public static long updateStartTime;
	private boolean kickAllPlayers = false;

	static {	
		IntStream.range(0, GameConstants.MAX_PLAYERS).forEach(player -> {
			players[player] = null;
		});
	}

	public boolean newPlayerClient(Player player1) {
		int slot = -1;
		for (int i = 1; i < GameConstants.MAX_PLAYERS; i++) {
			if ((players[i] == null) || players[i].disconnected) {
				slot = i;
				break;
			}
		}
		if (slot == -1)
			return false;
		player1.handler = this;
		player1.playerId = slot;
		players[slot] = player1;
		players[slot].isActive = true;
		if (Configuration.SERVER_DEBUG)
			Misc.println("Player Slot " + slot + " slot 0 " + players[0] + " Player Hit " + players[slot]);
		return true;
	}

	public void destruct() {
		for (int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
			if (players[i] == null)
				continue;
			players[i].destruct();
			players[i] = null;
		}
	}

	public static int getPlayerCount() {
                int count = 0;
		for(int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
                    if(players[i] != null) {
                        count++;
                    }
                }
                return count;
	}

	
	public static boolean isPlayerOn(String playerName) {
		// synchronized (PlayerHandler.players) {
		for (int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
			if (players[i] != null) {
				if (players[i].playerName.equalsIgnoreCase(playerName)) {
					return true;
				}
			}
		}
		return false;
		// }
	}

	public void process() {
		synchronized (lock) {
			if (kickAllPlayers) {				
				IntStream.range(0, GameConstants.MAX_PLAYERS).forEach(player -> {
					if (players[player] != null) {
						players[player].disconnected = true;
					}
				});				
			}
			for (int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
				if (players[i] == null || !players[i].isActive || !players[i].initialized)
					continue;
				try {
					if (players[i].disconnected
							&& (System.currentTimeMillis() - players[i].logoutDelay > 10000 || players[i].properLogout || kickAllPlayers)) {
						if (players[i].isTrading) {
							Player o = PlayerHandler.players[players[i].tradeWith];
							if (o != null) {
								o.getTrade().declineTrade();
							}
						}
						if (players[i].duelStatus == 5) {
							Player o = PlayerHandler.players[players[i].duelingWith];
							if (o != null) {
								o.getDuel().duelVictory();
							}
						} else if (players[i].duelStatus <= 4 && players[i].duelStatus >= 1) {
							Player o = PlayerHandler.players[players[i].duelingWith];
							if (o != null) {
								o.getDuel().declineDuel();
							}
						}
						Player o = PlayerHandler.players[i];
						if (PlayerSave.saveGame(o)) {
							System.out.println("Game saved for player " + players[i].playerName);
						} else {
							System.out.println("Could not save for " + players[i].playerName);
						}
						removePlayer(players[i]);
						players[i] = null;
						continue;
					}
					players[i].preProcessing();
					players[i].processQueuedPackets();
					players[i].process();
					players[i].postProcessing();
					players[i].getNextPlayerMovement();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
				if (players[i] == null || !players[i].isActive || !players[i].initialized)
					continue;
				try {
					players[i].update();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
				if (players[i] == null || !players[i].isActive || !players[i].initialized)
					continue;
				try {
					players[i].clearUpdateFlags();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (updateRunning && !updateAnnounced) {
				updateAnnounced = true;
				Server.UpdateServer = true;
			}
			if (updateRunning && (System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000))) {
				kickAllPlayers = true;
			}
		}
	}

	public void updateNPC(Player plr, Stream str) {
		// synchronized(plr) {
		updateBlock.currentOffset = 0;

		str.createFrameVarSizeWord(65);
		str.initBitAccess();

		str.writeBits(8, plr.npcListSize);
		int size = plr.npcListSize;
		plr.npcListSize = 0;		
		IntStream.range(0, size).forEach(npc -> {
			if (plr.RebuildNPCList == false && plr.withinDistance(plr.npcList[npc]) == true) {
				plr.npcList[npc].updateNPCMovement(str);
				plr.npcList[npc].appendNPCUpdateBlock(updateBlock);
				plr.npcList[plr.npcListSize++] = plr.npcList[npc];
			} else {
				int id = plr.npcList[npc].npcId;
				plr.npcInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		});		
		IntStream.range(0, MobHandler.maxNPCs).forEach(npc -> {
			if (MobHandler.npcs[npc] != null) {
				int id = MobHandler.npcs[npc].npcId;
				if (plr.RebuildNPCList == false && (plr.npcInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {

				} else if (plr.withinDistance(MobHandler.npcs[npc]) == false) {

				} else {
					plr.addNewNPC(MobHandler.npcs[npc], str, updateBlock);
				}
			}
		});
		plr.RebuildNPCList = false;

		if (updateBlock.currentOffset > 0) {
			str.writeBits(14, 16383);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
		// }
	}

	private Stream updateBlock = new Stream(new byte[GameConstants.BUFFER_SIZE]);

	public void updatePlayer(Player plr, Stream str) {
		// synchronized(plr) {
		updateBlock.currentOffset = 0;
		if (updateRunning && !updateAnnounced) {
			str.createFrame(114);
			str.writeWordBigEndian(updateSeconds * 50 / 30);
		}
		plr.updateThisPlayerMovement(str);
		boolean saveChatTextUpdate = plr.isChatTextUpdateRequired();
		plr.setChatTextUpdateRequired(false);
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setChatTextUpdateRequired(saveChatTextUpdate);
		str.writeBits(8, plr.playerListSize);
		int size = plr.playerListSize;
		if (size >= 79)
			size = 79;
		plr.playerListSize = 0;	
		IntStream.range(0, size).forEach(player -> {
			if (!plr.didTeleport && !plr.playerList[player].didTeleport && plr.withinDistance(plr.playerList[player])) {
				plr.playerList[player].updatePlayerMovement(str);
				plr.playerList[player].appendPlayerUpdateBlock(updateBlock);
				plr.playerList[plr.playerListSize++] = plr.playerList[player];
			} else {
				int id = plr.playerList[player].playerId;
				plr.playerInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		});
		for (int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive || players[i] == plr)
				continue;
			int id = players[i].playerId;
			if ((plr.playerInListBitmap[id >> 3] & (1 << (id & 7))) != 0)
				continue;
			if (!plr.withinDistance(players[i]))
				continue;
			plr.addNewPlayer(players[i], str, updateBlock);
		}

		if (updateBlock.currentOffset > 0) {
			str.writeBits(11, 2047);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else
			str.finishBitAccess();

		str.endFrameVarSizeWord();
		// }
	}
	
	public static Player getPlayer(String name) {
		for (int d = 0; d < GameConstants.MAX_PLAYERS; d++) {
			if (PlayerHandler.players[d] != null) {
				Player p = PlayerHandler.players[d];
				if (p.playerName.equalsIgnoreCase(name)) {
					return p;
				}
			}
		}
		return null;
	}

	private void removePlayer(Player plr) {
		if (plr.privateChat != 2) {
			for (int i = 1; i < GameConstants.MAX_PLAYERS; i++) {
				if (players[i] == null || players[i].isActive == false)
					continue;
				Player o = PlayerHandler.players[i];
				if (o != null) {
					o.getActionSender().updatePM(plr.playerId, 0);
				}
			}
		}
		plr.destruct();
	}

}
