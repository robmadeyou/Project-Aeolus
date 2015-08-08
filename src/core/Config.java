package core;

/**
 * Class that contains all the main configurations of the server.
 */
public class Config {
	
	/**
	 * Location of the data
	 */
	public static final String DATA_DIR = "./Data/";

	/**
	 * True enables all the debug messages, which is very helpful in a 
	 * debug case scenario.
	 */
	public static boolean SERVER_DEBUG = false;

	/**
	 * The name of the server
	 */
	public static final String SERVER_NAME = "Project Aeolus";

	/**
	 * The welcome message/messages displayed upon login
	 */
	public static final String WELCOME_MESSAGE = "Welcome to a highly optimized PI by 7Winds.";

	/**
	 * Forums link, if any
	 */
	public static final String FORUMS = "your forums here";
	
	/**
	 * Toggles the ability to log player chat messages
	 */
	public static final boolean logChat = false;
	
	/**
	 * Toggles the ability for players to swear
	 */
	public static final boolean enableCensor = true;
	
	/**
	 * Toggles the use of sound effects
	 */
	public static boolean enableSound = true;
	
	/**
	 * Toggles the use of regional music
	 */
	public static boolean enableMusic = true;
	
	/**
	 * Toggles the ability for admins to trade
	 */
	public static final boolean ADMIN_CAN_TRADE = false;
	
	/**
	 * Toggles the ability for admins to sell items
	 */
	public static final boolean ADMIN_CAN_SELL_ITEMS = false;
	
	/**
	 * Toggles the ability for admins to drop items
	 */
	public static final boolean ADMIN_DROP_ITEMS = false;

	/**
	 * How many squares away from origin a player respawns after dying in the dueling arena
	 */
	public static final int RANDOM_DUELING_RESPAWN = 5;

	/**
	 * The start of the wilderness that blocks the ability for a player to teleport
	 */
	public static final int NO_TELEPORT_WILD_LEVEL = 20;

	/**
	 * How long the skull lasts for. (seconds)
	 */
	public static final int SKULL_TIMER = 1200;

	/**
	 * How long in (milliseconds) a player cannot teleport for
	 * 20000 represents 20 seconds
	 */
	public static final int TELEBLOCK_DELAY = 20000;

	/**
	 * Toggles the ability to have single and multi zones
	 */
	public static final boolean SINGLE_AND_MULTI_ZONES = true;

	/**
	 * The difference between 2 players levels within a wilderness
	 */
	public static final boolean COMBAT_LEVEL_DIFFERENCE = true;

	/**
	 * Toggles the ability for items to have their level requirements
	 */
	public static final boolean itemRequirements = true;

	/**
	 * Toggles the ability for prayer points to be used
	 */
	public static final boolean PRAYER_POINTS_REQUIRED = true;

	/**
	 * Toggles the use of prayer levels
	 */
	public static final boolean PRAYER_LEVEL_REQUIRED = true;

	/**
	 * Toggles the use of magic levels
	 */
	public static final boolean MAGIC_LEVEL_REQUIRED = true;

	/**
	 * Toggles the use of runes
	 */
	public static final boolean RUNES_REQUIRED = true;

	/**
	 * Toggles the use of proper arrows
	 */
	public static final boolean CORRECT_ARROWS = true;

	/**
	 * Toggles the ability for a crystal bow to degrade
	 */
	public static final boolean CRYSTAL_BOW_DEGRADES = true;
	
	/**
	 * An array of items that cannot be sold
	 */
	public static final int[] ITEM_SELLABLE = {

	};

	/**
	 * An array of items that cannot be traded
	 */
	public static final int[] ITEM_TRADEABLE = {

	};

	/**
	 * An array of items that cannot be dropped
	 */
	public static final int[] UNDROPPABLE_ITEMS = {

	};

	/**
	 * An array of fun items
	 */
	public static final int[] FUN_WEAPONS = {

	};
	
	/**
	 * An array of items that are rare
	 */
	public static final int[] ITEM_RARE = {4151, 11732, 6737, 11694, 11692, 11690, 11696, 11688, 6585, 4700, 4702, 4704, 4706, 4708, 4710, 4712,
        4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4736, 4738, 4740, 4742, 4744, 11286, 11724,
		   11726, 11718, 11720, 11722, 11995, 11994, 15019, 15023, 15020, 15021, 15022, 12009, 12011, 11989, 11990,
		   11999, 11988, 11987, 11986, 12000, 15030, 13263, 10556, 10557, 10558, 10559};

	/**
	 * An array of undead npcs, used for the magic spell on undead npcs
	 */
    public static final int[] UNDEAD_NPCS = { 90, 91, 92, 93, 94, 103, 104, 73, 74, 75, 76, 77 };

    /**
	 * The servers main cycle time in (milliseconds)
	 */
	public static final int CYCLE_TIME = 600;

	/**
	 * Cheap hack for a worlds list fix
	 */
	public static final boolean WORLD_LIST_FIX = false;	
}