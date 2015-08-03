package core.game.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import core.game.model.entity.player.Player;

/**
 * MySQL Class
 * @author Ryan / Lmctruck30
 *
 */

public class MysqlManager {

	/** MySQL Connection */
	public static Connection conn = null;
	public static Statement statement = null;
	public static ResultSet results = null;
	
	public static String MySQLDataBase = "game";
	public static String MySQLURL = "localhost";
	public static String MySQLUser = "root";
	public static String MySQLPassword = "tsm123";
	
	/**
	 * Creates a Connection to the MySQL Database
	 */
	public synchronized static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			//conn = DriverManager.getConnection("jdbc:mysql://riotscape.com/riot_forum", "riot_forum", "ryan16");
			//Misc.println("MySQL Connected");
		} 
		catch(Exception e) {			
			//e.printStackTrace();
		}
	}
	
	public synchronized static void destroyConnection() {
		try {
			statement.close();
			conn.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	public synchronized static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = statement.executeQuery(s);
				return rs;
			} else {
				statement.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			destroyConnection();
			createConnection();
			//e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Save Sessions HighScores
	 * @param PlayerToSave The session that saves their stats
	 * @return The flag true if successful
	 */
	public synchronized static boolean saveHighScore(Player PlayerToSave) {
		try {
			query("DELETE FROM `skills` WHERE playerName = '"+PlayerToSave.playerName+"';");
			query("DELETE FROM `skillsoverall` WHERE playerName = '"+PlayerToSave.playerName+"';");
			query("INSERT INTO `skills` (`playerName`,`Attacklvl`,`Attackxp`,`Defencelvl`,`Defencexp`,`Strengthlvl`,`Strengthxp`,`Hitpointslvl`,`Hitpointsxp`,`Rangelvl`,`Rangexp`,`Prayerlvl`,`Prayerxp`,`Magiclvl`,`Magicxp`,`Cookinglvl`,`Cookingxp`,`Woodcuttinglvl`,`Woodcuttingxp`,`Fletchinglvl`,`Fletchingxp`,`Fishinglvl`,`Fishingxp`,`Firemakinglvl`,`Firemakingxp`,`Craftinglvl`,`Craftingxp`,`Smithinglvl`,`Smithingxp`,`Mininglvl`,`Miningxp`,`Herblorelvl`,`Herblorexp`,`Agilitylvl`,`Agilityxp`,`Thievinglvl`,`Thievingxp`,`Slayerlvl`,`Slayerxp`,`Farminglvl`,`Farmingxp`,`Runecraftlvl`,`Runecraftxp`) VALUES ('"+PlayerToSave.playerName+"',"+PlayerToSave.playerLevel[0]+","+PlayerToSave.playerXP[0]+","+PlayerToSave.playerLevel[1]+","+PlayerToSave.playerXP[1]+","+PlayerToSave.playerLevel[2]+","+PlayerToSave.playerXP[2]+","+PlayerToSave.playerLevel[3]+","+PlayerToSave.playerXP[3]+","+PlayerToSave.playerLevel[4]+","+PlayerToSave.playerXP[4]+","+PlayerToSave.playerLevel[5]+","+PlayerToSave.playerXP[5]+","+PlayerToSave.playerLevel[6]+","+PlayerToSave.playerXP[6]+","+PlayerToSave.playerLevel[7]+","+PlayerToSave.playerXP[7]+","+PlayerToSave.playerLevel[8]+","+PlayerToSave.playerXP[8]+","+PlayerToSave.playerLevel[9]+","+PlayerToSave.playerXP[9]+","+PlayerToSave.playerLevel[10]+","+PlayerToSave.playerXP[10]+","+PlayerToSave.playerLevel[11]+","+PlayerToSave.playerXP[11]+","+PlayerToSave.playerLevel[12]+","+PlayerToSave.playerXP[12]+","+PlayerToSave.playerLevel[13]+","+PlayerToSave.playerXP[13]+","+PlayerToSave.playerLevel[14]+","+PlayerToSave.playerXP[14]+","+PlayerToSave.playerLevel[15]+","+PlayerToSave.playerXP[15]+","+PlayerToSave.playerLevel[16]+","+PlayerToSave.playerXP[16]+","+PlayerToSave.playerLevel[17]+","+PlayerToSave.playerXP[17]+","+PlayerToSave.playerLevel[18]+","+PlayerToSave.playerXP[18]+","+PlayerToSave.playerLevel[19]+","+PlayerToSave.playerXP[19]+","+PlayerToSave.playerLevel[20]+","+PlayerToSave.playerXP[20]+");");
			query("INSERT INTO `skillsoverall` (`playerName`,`lvl`,`xp`) VALUES ('"+PlayerToSave.playerName+"',"+(PlayerToSave.getLevelForXP(PlayerToSave.playerXP[0]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[1]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[2]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[3]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[4]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[5]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[6]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[7]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[8]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[9]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[10]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[11]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[12]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[13]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[14]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[15]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[16]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[17]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[18]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[19]) + PlayerToSave.getLevelForXP(PlayerToSave.playerXP[20]))+","+((PlayerToSave.playerXP[0]) + (PlayerToSave.playerXP[1]) + (PlayerToSave.playerXP[2]) + (PlayerToSave.playerXP[3]) + (PlayerToSave.playerXP[4]) + (PlayerToSave.playerXP[5]) + (PlayerToSave.playerXP[6]) + (PlayerToSave.playerXP[7]) + (PlayerToSave.playerXP[8]) + (PlayerToSave.playerXP[9]) + (PlayerToSave.playerXP[10]) + (PlayerToSave.playerXP[11]) + (PlayerToSave.playerXP[12]) + (PlayerToSave.playerXP[13]) + (PlayerToSave.playerXP[14]) + (PlayerToSave.playerXP[15]) + (PlayerToSave.playerXP[16]) + (PlayerToSave.playerXP[17]) + (PlayerToSave.playerXP[18]) + (PlayerToSave.playerXP[19]) + (PlayerToSave.playerXP[20]))+");");
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Save Voting Point Info
	 * @param c The session's Player
	 * @return The flag if true was successful
	 */
	public static boolean saveVotingInfo(Player c) {
		try {
			query("INSERT INTO `skills` (`playerName`,`playerPass') VALUES ('"+c.playerName+"',"+c.playerPass+");");
		} catch(Exception e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static int loadVotingPoints(Player c) {
		try {
			ResultSet group = statement.executeQuery("SELECT * FROM user WHERE username = '"+c.playerName+"'");
			while(group.next()) {
				String groupp = group.getString("usergroupid");
				int mgroup = Integer.parseInt(groupp);
				if(mgroup > 0) {
					return mgroup;
				}
				return 0;
			}
		} catch(Exception e) {
			return -1;
		}
		return -1;
	}
	
	public static int loadDonationPoints(Player c) {
		try {
			ResultSet group = statement.executeQuery("SELECT * FROM user WHERE username = '"+c.playerName+"'");
			while(group.next()) {
				String groupp = group.getString("usergroupid");
				int mgroup = Integer.parseInt(groupp);
				if(mgroup > 0) {
					return mgroup;
				}
				return 0;
			}
		} catch(Exception e) {
			return -1;
		}
		return -1;
	}
	
}
