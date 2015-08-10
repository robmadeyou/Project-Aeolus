package core.game.util.log.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;
import core.game.util.Misc;

/**
 * Class that logs all player chat messages
 * @author 7Winds
 */
public class ChatLogger {

	Player c;

	public ChatLogger(Player c) {
		this.c = c;
	}

	public void logMasterChat(String message) {
		if (Configuration.logChat) {
		if (c.getRights().equal(Rights.PLAYER)) {
			try {
				BufferedWriter bItem = new BufferedWriter(new FileWriter(
						Configuration.DATA_DIR + "logs/ChatLog/Master/Player/" +
								"ChatLogs.log", true));
				try {
					bItem.newLine();
					bItem.write("Date : " + Misc.getDate2());
					bItem.newLine();
					bItem.write(c.playerName + ":");
					bItem.newLine();
					bItem.write(message);
					bItem.newLine();
					bItem.write("--------------------------------------------------");
				} finally {
					bItem.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				BufferedWriter bItem = new BufferedWriter(new FileWriter(
						Configuration.DATA_DIR + "logs/ChatLog/Master/Mod/"
								+ c.playerName + ".log", true));
				try {
					bItem.newLine();
					bItem.write("Date : " + Misc.getDate2());
					bItem.newLine();
					bItem.write(message);
					bItem.newLine();
					bItem.write("--------------------------------------------------");
				} finally {
					bItem.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	}

	public void logChat(String message) {
		if (Configuration.logChat) {
		if (c.getRights().equal(Rights.PLAYER)) {
			try {
				BufferedWriter bItem = new BufferedWriter(new FileWriter(
						Configuration.DATA_DIR + "logs/ChatLog/Badword/Player/" + "Badwords"
								+ ".log", true));
				try {
					bItem.newLine();
					bItem.write("Date : " + Misc.getDate2());
					bItem.newLine();
					bItem.write(c.playerName + ":");
					bItem.newLine();
					bItem.write(message);
					bItem.newLine();
					bItem.write("--------------------------------------------------");
				} finally {
					bItem.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				BufferedWriter bItem = new BufferedWriter(new FileWriter(
						Configuration.DATA_DIR + "logs/ChatLog/Badword/Mod/" + c.playerName
								+ ".log", true));
				try {
					bItem.newLine();
					bItem.write("Date : " + Misc.getDate2());
					bItem.newLine();
					bItem.write(message);
					bItem.newLine();
					bItem.write("--------------------------------------------------");
				} finally {
					bItem.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	}
}
