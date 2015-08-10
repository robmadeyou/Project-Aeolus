package core.game;

/**
 * Class containing only constant values
 * @author 7Winds
 */
public class GameConstants {
	
	/**
	 * Used for additional events, to multiply the current xp rates
	 */
	public static final int SERVER_EXP_BONUS = 0;
	
	/**
	 * Combat rates
	 */
	public static final int MELEE_EXP_RATE = 100;
	public static final int RANGE_EXP_RATE = 100;
	public static final int MAGIC_EXP_RATE = 100;
	
	/**
	 * Skill experience multipliers.
	 */
	public static final int WOODCUTTING_EXPERIENCE = 1;
	public static final int MINING_EXPERIENCE = 1;
	public static final int SMITHING_EXPERIENCE = 1;
	public static final int FARMING_EXPERIENCE = 1;
	public static final int FIREMAKING_EXPERIENCE = 1;
	public static final int HERBLORE_EXPERIENCE = 1;
	public static final int FISHING_EXPERIENCE = 1;
	public static final int AGILITY_EXPERIENCE = 1;
	public static final int PRAYER_EXPERIENCE = 1;
	public static final int RUNECRAFTING_EXPERIENCE = 1;
	public static final int CRAFTING_EXPERIENCE = 1;
	public static final int THIEVING_EXPERIENCE = 1;
	public static final int SLAYER_EXPERIENCE = 1;
	public static final int COOKING_EXPERIENCE = 1;
	public static final int FLETCHING_EXPERIENCE = 1;
		
	/**
	 * Saves all players time unit (seconds)
	 * 240 = 4 minutes
	 */
	public static final int SAVE_TIMER = 240;

	/**
	 * How far an npc will walk away from its origin.
	 */	
	public static final int NPC_RANDOM_WALK_DISTANCE = 5;
	
	/**
	 * How far an npc will follow an entity
	 */
	public static final int NPC_FOLLOW_DISTANCE = 10;
	
	/**
	 * Modern spells
	 */
	public static final int VARROCK_X = 3210;
	public static final int VARROCK_Y = 3424;
	public static final String VARROCK = "";
	public static final int LUMBY_X = 3222;
	public static final int LUMBY_Y = 3218;
	public static final String LUMBY = "";
	public static final int FALADOR_X = 2964;
	public static final int FALADOR_Y = 3378;
	public static final String FALADOR = "";
	public static final int CAMELOT_X = 2757;
	public static final int CAMELOT_Y = 3477;
	public static final String CAMELOT = "";
	public static final int ARDOUGNE_X = 2662;
	public static final int ARDOUGNE_Y = 3305;
	public static final String ARDOUGNE = "";
	public static final int WATCHTOWER_X = 3087;
	public static final int WATCHTOWER_Y = 3500;
	public static final String WATCHTOWER = "";
	public static final int TROLLHEIM_X = 3243;
	public static final int TROLLHEIM_Y = 3513;
	public static final String TROLLHEIM = "";
	
	/**
	 * Ancient spell locations
	 */
	public static final int PADDEWWA_X = 3098;
	public static final int PADDEWWA_Y = 9884;
	public static final int SENNTISTEN_X = 3322;
	public static final int SENNTISTEN_Y = 3336;
	public static final int KHARYRLL_X = 3492;
	public static final int KHARYRLL_Y = 3471;
	public static final int LASSAR_X = 3006;
	public static final int LASSAR_Y = 3471;
	public static final int DAREEYAK_X = 3161;
	public static final int DAREEYAK_Y = 3671;
	public static final int CARRALLANGAR_X = 3156;
	public static final int CARRALLANGAR_Y = 3666;
	public static final int ANNAKARL_X = 3288;
	public static final int ANNAKARL_Y = 3886;
	public static final int GHORROCK_X = 2977;
	public static final int GHORROCK_Y = 3873;	
	
	/**
	 * The spawn locations of a new player
	 */
	public static final int START_LOCATION_X = 3087;
	public static final int START_LOCATION_Y = 3502;
	
	/**
	 * The spawn location of a player that died
	 */
	public static final int RESPAWN_X = 3087;
	public static final int RESPAWN_Y = 3502;
	
	/**
	 * The location of a player that died in the dueling arena
	 */
	public static final int DUELING_RESPAWN_X = 3362;
	public static final int DUELING_RESPAWN_Y = 3263;

	public static final int GOD_SPELL_CHARGE = 300000;
	
	/**
	 * How fast the special attack bar regenerates
	 */
	public static final int INCREASE_SPECIAL_AMOUNT = 17500;
	
	/**
	 * The capacity of a players Bank
	 */
	public static final int BANK_SIZE = 352;
	
	/**
	 * The maximum amount of players that can be held within the server
	 */
	public static final int MAX_PLAYERS = 1024;
	
	/**
	 * A security feature that tells if a players client is up-to-date
	 */
	public static final int CLIENT_VERSION = 317;

	/**
	 * The maximum amount of items
	 */
	public static final int MAXITEM_AMOUNT = Integer.MAX_VALUE;

	/**
	 * The delay upon login (a slight delay is required to properly setup the player)
	 */
	public static final int CONNECTION_DELAY = 100;

	/**
	 * The maximum amount of clients a single player can use to connect to the server
	 */
	public static final int IPS_ALLOWED = 3;
	
	/**
	 * The max amount of items
	 */
	public static final int ITEM_LIMIT = 16000;	
	
	/**
	 * The timer in which you are skulled goes away. Seconds x2 Ex. 60x2=120
	 * Skull timer would be 1 minute.
	 */
	public static final int SKULL_TIMER = 1200;
	
	/**
	 * Buffer size.
	 */
	public static final int BUFFER_SIZE = 512;
	
	/**
	 * Timeout time.
	 */
	public static final int TIMEOUT = 20;
	

	public static int MESSAGE_DELAY = 6000;
}
