package core.game.model.entity.player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import core.Configuration;

/**
 * The Connection Check Class
 * 
 * @author Ryan
 * @author Lmctruck30 Revision by Shawn Notes by Shawn
 */
public class Punishments {

	/**
	 * Bans & mutes.
	 */
	public static ArrayList<String> bannedIps = new ArrayList<String>();
	public static ArrayList<String> bannedNames = new ArrayList<String>();
	public static ArrayList<String> mutedIps = new ArrayList<String>();
	public static ArrayList<String> mutedNames = new ArrayList<String>();
	public static ArrayList<String> loginLimitExceeded = new ArrayList<String>();
	public static ArrayList<String> starterRecieved1 = new ArrayList<String>();
	public static ArrayList<String> starterRecieved2 = new ArrayList<String>();
	public static Collection<String> bannedUid = new ArrayList<String>();

	public static void initialize() {
		banUsers();
		banIps();
		muteUsers();
		muteIps();
		banUid();
	}

	/**
	 * Adds banned users and IPs from the text file to the ban list.
	 **/
	public static void appendStarters() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					Configuration.DATA_DIR + "starters/FirstStarterRecieved.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					starterRecieved1.add(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void appendStarters2() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					Configuration.DATA_DIR + "starters/SecondStarterRecieved.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					starterRecieved2.add(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addIpToStarter1(String IP) {
		starterRecieved1.add(IP);
		addIpToStarterList1(IP);
	}

	public static void addIpToStarter2(String IP) {
		starterRecieved2.add(IP);
		addIpToStarterList2(IP);
	}

	public static void addIpToStarterList1(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(
					new FileWriter(Configuration.DATA_DIR
							+ "starters/FirstStarterRecieved.txt", true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addIpToStarterList2(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					Configuration.DATA_DIR + "starters/SecondStarterRecieved.txt",
					true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean hasRecieved1stStarter(String IP) {
		if (starterRecieved1.contains(IP)) {
			return true;
		}
		return false;
	}

	public static boolean hasRecieved2ndStarter(String IP) {
		if (starterRecieved2.contains(IP)) {
			return true;
		}
		return false;
	}

	/**
	 * Adding names to the list.
	 */
	public static void addIpToLoginList(String IP) {
		loginLimitExceeded.add(IP);
	}

	/**
	 * Removes IPs from the list.
	 */
	public static void removeIpFromLoginList(String IP) {
		loginLimitExceeded.remove(IP);
	}

	/**
	 * Clears the name from the list.
	 */
	public static void clearLoginList() {
		loginLimitExceeded.clear();
	}

	/**
	 * Checks the list for blocked IPs if a user is trying to login.
	 */
	public static boolean checkLoginList(String IP) {
		loginLimitExceeded.add(IP);
		int num = 0;
		for (String ips : loginLimitExceeded) {
			if (IP.equals(ips)) {
				num++;
			}
		}
		if (num > 5) {
			return true;
		}
		return false;
	}

	/**
	 * Removes a muted user from the muted list.
	 */
	public static void unMuteUser(String name) {
		mutedNames.remove(name);
		deleteFromFile(Configuration.DATA_DIR + "bans/UsersMuted.txt", name);
	}

	/**
	 * Removes a banned user from the banned list.
	 **/
	public static void removeNameFromBanList(String name) {
		bannedNames.remove(name.toLowerCase());
		deleteFromFile(Configuration.DATA_DIR + "bans/UsersBanned.txt", name);
	}

	public static void removeNameFromMuteList(String name) {
		bannedNames.remove(name.toLowerCase());
		deleteFromFile(Configuration.DATA_DIR + "bans/UsersMuted.txt", name);
	}

	/**
	 * Void needed to delete users from a file.
	 */

	public static void deleteFromFile(String file, String name) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			ArrayList<String> contents = new ArrayList<String>();
			while (true) {
				String line = r.readLine();
				if (line == null) {
					break;
				} else {
					line = line.trim();
				}
				if (!line.equalsIgnoreCase(name)) {
					contents.add(line);
				}
			}
			r.close();
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			for (String line : contents) {
				w.write(line, 0, line.length());
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Removes an IP address from the IPmuted list.
	 */
	public static void unIPMuteUser(String name) {
		mutedIps.remove(name);
		deleteFromFile(Configuration.DATA_DIR + "bans/IpsMuted.txt", name);
	}

	/**
	 * Removes an IP address from the IPBanned list.
	 **/
	public static void removeIpFromBanList(String IP) {
		bannedIps.remove(IP);
	}

	/**
	 * Adds a user to the banned list.
	 **/
	public static void addNameToBanList(String name) {
		bannedNames.add(name.toLowerCase());
	}

	public static void addNameToMuteList(String name) {
		mutedNames.add(name.toLowerCase());
		addUserToFile(name);
	}

	/**
	 * Adds a user to the muted list.
	 */
	public static void muteUsers() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					Configuration.DATA_DIR + "bans/UsersMuted.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					mutedNames.add(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds an IP address to the IPMuted list.
	 */
	public static void muteIps() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					Configuration.DATA_DIR + "bans/IpsMuted.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					mutedIps.add(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds an IP address to the IPBanned list.
	 **/
	public static void addIpToBanList(String IP) {
		bannedIps.add(IP);
	}

	/**
	 * Adds an IP address to the IPMuted list.
	 */
	public static void addIpToMuteList(String IP) {
		mutedIps.add(IP);
		addIpToMuteFile(IP);
	}

	/**
	 * Contains banned IP addresses.
	 **/
	public static boolean isIpBanned(String IP) {
		if (bannedIps.contains(IP)) {
			return true;
		}
		return false;
	}

	/**
	 * Contains banned users.
	 **/
	public static boolean isNamedBanned(String name) {
		if (bannedNames.contains(name.toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * Reads all users from text file then adds them all to the ban list.
	 **/
	public static void banUsers() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					Configuration.DATA_DIR + "bans/UsersBanned.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					addNameToBanList(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads all the IPs from text file then adds them all to ban list.
	 **/
	public static void banIps() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					Configuration.DATA_DIR + "bans/IpsBanned.txt"));
			String data = null;
			try {
				while ((data = in.readLine()) != null) {
					addIpToBanList(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void unUidBanUser(String name) {
		bannedUid.remove(name);
		deleteFromFile(Configuration.DATA_DIR + "bans/UUIDBans.txt", name);
	}

	static String uidForUser = null;

	public static void getUidForUser(Player c, String name) {
		File file = new File(Configuration.DATA_DIR + "characters/" + name + ".txt");
		BufferedReader reader = null;
		boolean error = false;
		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;
			int line = 0;
			int done = 0;
			while ((text = reader.readLine()) != null && done == 0) {
				text = text.trim();
				line += 1;
				if (line >= 6) {
					text = text.trim();
					int spot = text.indexOf("=");
					String token = text.substring(0, spot);
					token = token.trim();
					String token2 = text.substring(spot + 1);
					token2 = token2.trim();
					if (token.equalsIgnoreCase("UUID")) {
						uidForUser = token2;
						done = 1;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			error = true;
			c.sendMessage("Could not find the character file " + name + ".txt");
		} catch (IOException e) {
			e.printStackTrace();
			error = true;
			c.sendMessage("A problem occured while trying to read the character file for "
					+ name + ".");
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// System.out.println(macForUser);
		if (!error) {
			bannedUid.remove(uidForUser);
			deleteFromFile("./Data/bans/UUIDBans.txt", uidForUser);
			c.sendMessage("@red@Un-UUID banned user " + name
					+ " with the UUID address of " + uidForUser + ".");
		}
	}

	public static void addUidToBanList(String UUID) {
		bannedUid.add(UUID);
	}

	public static boolean isUidBanned(String UUID) {
		return bannedUid.contains(UUID);
	}

	public static void banUid() {
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					Configuration.DATA_DIR + "bans/UUIDBans.txt"));
			String data;
			try {
				while ((data = in.readLine()) != null) {
					addUidToBanList(data);
					System.out.println(data);
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeUidFromBanList(String UUID) {
		bannedUid.remove(UUID);
		deleteFromFile(Configuration.DATA_DIR + "bans/UUIDBans.txt", UUID);
	}

	public static void addUidToFile(String UUID) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					Configuration.DATA_DIR + "bans/UUIDBans.txt", true));
			try {
				out.newLine();
				out.write(UUID);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the user into the text file when using the ::ban command.
	 **/
	public static void addNameToFile(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					Configuration.DATA_DIR + "bans/UsersBanned.txt", true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the user into the text file when using the ::mute command.
	 */
	public static void addUserToFile(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					Configuration.DATA_DIR + "bans/UsersMuted.txt", true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the IP into the text file when using the ::ipban command.
	 **/
	public static void addIpToFile(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					Configuration.DATA_DIR + "bans/IpsBanned.txt", true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the IP into the text file when using the ::mute command.
	 */
	public static void addIpToMuteFile(String Name) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					Configuration.DATA_DIR + "bans/IpsMuted.txt", true));
			try {
				out.newLine();
				out.write(Name);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Needed boolean for muting.
	 */
	public static boolean isMuted(Player c) {
		// return mutedNames.contains(c.playerName) ||
		// mutedIps.contains(c.connectedFrom);
		return false;
	}

}
