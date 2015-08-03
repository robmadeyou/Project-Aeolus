package core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import core.game.model.entity.npc.NPCHandler;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.item.ItemHandler;
import core.game.model.item.ItemTableManager;
import core.game.model.object.ObjectHandler;
import core.game.model.object.ObjectManager;
import core.game.model.shop.ShopHandler;
import core.game.plugin.PluginManager;
import core.game.task.Task2;
import core.game.task.TaskScheduler;
import core.game.util.Misc;
import core.game.util.json.NpcDefinitionLoader;
import core.game.world.StillGraphicsManager;
import core.net.PipelineFactory;
import core.net.packets.incoming.PlayerManager;

/**
 * The core of the game.
 */
@SuppressWarnings("all")
public class Server {

	/**
	 * A logger used to print to the output stream
	 */
	private static final Logger logger = Logger.getLogger(Server.class
			.getName());

	/**
	 * Calls to manage the players on the server.
	 */
	public static PlayerManager playerManager = null;
	private static StillGraphicsManager stillGraphicsManager = null;

	/**
	 * Sleep mode of the server.
	 */
	public static boolean sleeping;

	/**
	 * Calls the rate in which an event cycles.
	 */
	public static final int cycleRate;

	/**
	 * Server updating.
	 */
	public static boolean UpdateServer = false;

	/**
	 * Calls in which the server was last saved.
	 */
	public static long lastMassSave = System.currentTimeMillis();

	/**
	 * Calls the usage of CycledEvents.
	 */
	private static long cycleTime, cycles, totalCycleTime, sleepTime;

	/**
	 * Used for debugging the server.
	 */
	private static DecimalFormat debugPercentFormat;

	/**
	 * Forced shutdowns.
	 */
	public static boolean shutdownServer = false, shutdownClientHandler;

	/**
	 * Used to identify the server port.
	 */
	public static int serverlistenerPort;

	/**
	 * Calls the usage of player items.
	 */
	public static ItemHandler itemHandler = new ItemHandler();

	/**
	 * Handles logged in players.
	 */
	public static PlayerHandler playerHandler = new PlayerHandler();

	/**
	 * Handles global NPCs.
	 */
	public static NPCHandler npcHandler = new NPCHandler();

	/**
	 * Handles global shops.
	 */
	public static ShopHandler shopHandler = new ShopHandler();

	/**
	 * Handles global objects.
	 */
	public static ObjectHandler objectHandler = new ObjectHandler();
	public static ObjectManager objectManager = new ObjectManager();

	/**
	 * Handles the task scheduler.
	 */
	private static final TaskScheduler scheduler = new TaskScheduler();

	private static final PluginManager pluginManager = new PluginManager();

	/**
	 * Gets the task scheduler.
	 */
	public static TaskScheduler getTaskScheduler() {
		return scheduler;
	}

	/**
	 * Used to assign variables, will want to remove this eventually.
	 */
	static {
		serverlistenerPort = 43594;
		cycleRate = 600;
		shutdownServer = false;
		sleepTime = 0;
	}

	/**
	 * Starts the server.
	 */
	public static void main(java.lang.String args[])
			throws NullPointerException, IOException {
		if(Config.SERVER_DEBUG)
		System.setOut(new Misc.TimestampLogger(System.out, Config.DATA_DIR + "/logs/out.log"));
		System.setErr(new Misc.TimestampLogger(System.err, Config.DATA_DIR + "/logs/error/error.log"));

		ItemTableManager.load();
		new NpcDefinitionLoader().load();
		npcHandler.build();
		
		long startTime = System.currentTimeMillis();
		bind();

		playerManager = PlayerManager.getSingleton();
		playerManager.setupRegionPlayers();
		stillGraphicsManager = new StillGraphicsManager();

		/**
		 * Successfully loaded the server.
		 */
		long endTime = System.currentTimeMillis();
		long elapsed = endTime - startTime;
		System.out.println("Server Initiated in " + elapsed + " milliseconds.");

		/**
		 * Main server tick.
		 */
		scheduler.schedule(new Task2() {
			@Override
			protected void execute() {
				itemHandler.process();
				playerHandler.process();
				npcHandler.process();
				shopHandler.process();
				objectManager.process();
			}
		});
	}
	/**
	 * Player instance
	 */
	private Player p;
	
	/**
	 * Logging execution.
	 */
	public static boolean playerExecuted = false;

	/**
	 * Gets the sleep mode timer and puts the server into sleep mode.
	 */
	public static long getSleepTimer() {
		return sleepTime;
	}

	/**
	 * Gets the Graphics manager.
	 */
	public static StillGraphicsManager getStillGraphicsManager() {
		return stillGraphicsManager;
	}

	/**
	 * Gets the Player manager.
	 */
	public static PlayerManager getPlayerManager() {
		return playerManager;
	}

	/**
	 * Gets the Object manager.
	 */
	public static ObjectManager getObjectManager() {
		return objectManager;
	}

	/**
	 * Java connection. Ports.
	 */
	private static void bind() {
		ServerBootstrap serverBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory(
				new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(serverlistenerPort));
	}
}