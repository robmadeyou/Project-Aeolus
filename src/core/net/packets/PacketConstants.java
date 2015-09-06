package core.net.packets;

/**
 * Class which contains packet-related constants
 * @author 7Winds
 */
public class PacketConstants {
	
	//Incoming packets
	public static final int ADD_FRIEND = 188;
	
	public static final int ADD_IGNORE = 133;
	
	public static final int ATTACK_NPC = 72;
	
	public static final int BANK_X_PART_1 = 135;
	
	public static final int BANK_X_PART_2 = 208;
	
	public static final int CAMERA_MOVEMENT = 86;
	
	public static final int ENTER_REGION = 210;
	
	public static final int FOCUS_CHANGE = 3;
	
	public static final int IDLE_LOGOUT = 202;
	
	public static final int ITEM_ON_OBJECT = 192;
	
	public static final int ITEM_ON_ITEM = 53;
	
	public static final int LOADED_REGION = 121;
	
	public static final int MAGIC_ON_NPC = 131;
	
	public static final int NPC_ACTION_1 = 155;
	
	public static final int NPC_ACTION_2 = 17;
	
	public static final int NPC_ACTION_3 = 21;
	
	public static final int PLAYER_COMMAND = 103;
	
	public static final int PRIVACY_OPTIONS = 95;
	
	public static final int PRIVATE_MESSAGE = 126;
	
	public static final int REGULAR_WALK = 164;
	
	public static final int REMOVE_FRIEND = 215;
	
	public static final int WALK_ON_COMMAND = 98;
	
	/**
	 * An array of packet sizes
	 */
	public static final int PACKET_SIZES[] = { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};
}
