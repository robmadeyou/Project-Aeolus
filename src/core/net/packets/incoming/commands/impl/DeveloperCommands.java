package core.net.packets.incoming.commands.impl;

import core.game.GameConstants;
import core.game.model.entity.Hit;
import core.game.model.entity.Hit.HitType;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.entity.player.Rights;
import core.game.model.entity.player.save.PlayerSave;
import core.net.packets.incoming.commands.Command;

/**
 * A class which will be the container of all the commands accessed by
 * Developers, but restricted to Administrators and Players.
 * 
 * @author 7Winds
 */
public class DeveloperCommands implements Command {
	@Override
	public void execute(Player player, String[] command) {
		switch (command[0]) {
		
		case "sound":
			int soundId= Integer.parseInt(command[1]);
			player.getPA().sendSound(soundId);
			break;
			
		case "music":
			int musicId = Integer.parseInt(command[1]);
			player.getPA().sendMusic(player, musicId);
			break;
		
		case "test":
			player.sendMessage("I have access to developer commands.");
			break;

		case "hit":
			player.dealHit(new Hit(1));
			break;

		case "hit2":
			player.dealHit(new Hit(1, HitType.BLOCKED));
			break;

		case "hit3":
			player.dealHit(new Hit(1, HitType.POISON));
			break;

		case "hit4":
			player.dealHit(new Hit(1, HitType.DIESEASE));
			break;

		case "gfx":
			player.gfx0(Integer.parseInt(command[1]));
			break;

		case "object":
			player.getPA().object(Integer.parseInt(command[1]), player.absX, player.absY, 0, 10);
			break;

		case "forcechatall":
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Player player2 = PlayerHandler.players[j];
					player2.forcedChat(command[1]);
					player2.forcedChatUpdateRequired = true;
					player2.updateRequired = true;
				}
			}
			break;

		case "kill":
			if (player.playerName.equalsIgnoreCase("7Winds")) {
				String nameToKill = command[0].substring(5);
				for (int i = 0; i < GameConstants.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i].playerName.equalsIgnoreCase(nameToKill)) {
							Player player2 = PlayerHandler.players[i];
							if (player2.playerName.equalsIgnoreCase("7Winds")) {
								player.sendMessage("You can't use this command on this player!");
								return;
							}
							player.sendMessage("You killed " + player2.playerName);
							player2.isDead = true;
							player2.dealHit(new Hit(99, HitType.NORMAL));
							player2.forcedChat("UGH!");
							player2.gfx0(547);
							player.startAnimation(1914);
							break;
						}
					}
				}
			}
			break;

		case "update":
			int a = Integer.parseInt(command[1]);
			PlayerHandler.updateSeconds = a;
			PlayerHandler.updateAnnounced = false;
			PlayerHandler.updateRunning = true;
			PlayerHandler.updateStartTime = System.currentTimeMillis();
			break;
			
		case "restart":
			for (Player players : PlayerHandler.players) {
				if (players != null) {	
					players.saveCharacter = true;
					PlayerSave.saveGame(players);
					continue;
				}
			}
			System.exit(0);
			break;
			
		case "interface":
			try {
				int interfaceID = Integer.parseInt(command[1]);			
				player.getPA().showInterface(interfaceID);
				System.out.println(interfaceID);
			} catch (Exception ex) {
				player.sendMessage("Wrong Syntax use ::interface ####");
			}
			break;
		
		case "attr":
			String key = command[1];
			
			Object testkey = (Boolean) player.getAttributes().get(key);
			
			player.sendMessage("Attribute : " + key + " is set to: " + testkey);
			
			break;

		case "emote":
			player.startAnimation(Integer.parseInt(command[1]));
			player.getPA().requestUpdates();
			break;
		}
	}

	@Override
	public Rights getRights() {
		return Rights.DEVELOPER;
	}
}
