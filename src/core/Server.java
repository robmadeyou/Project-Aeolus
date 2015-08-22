package core;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import core.game.event.task.Task2;
import core.game.event.task.TaskScheduler;
import core.game.model.entity.mob.MobDrop;
import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.item.ItemHandler;
import core.game.model.object.ObjectHandler;
import core.game.model.object.ObjectManager;
import core.game.model.shop.ShopHandler;
import core.game.plugin.PluginManager;
import core.game.util.json.MobSpawnLoader;
import core.game.util.json.MusicLoader;
import core.game.util.json.NpcDefinitionLoader;
import core.game.util.json.WeaponDelayLoader;
import core.game.world.StillGraphicsManager;
import core.game.world.World;
import core.game.world.clipping.region.ObjectDef;
import core.game.world.clipping.region.Region;
import core.net.PipelineFactory;
import core.net.packets.incoming.PlayerManager;

/**
 * The core of the game.
 */
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
	public static boolean UPDATE_SERVER = false;

	/**
	 * Calls in which the server was last saved.
	 */
	public static long lastMassSave = System.currentTimeMillis();

	/**
	 * Calls the usage of CycledEvents.
	 */
	private static long sleepTime;

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
	public static MobHandler npcHandler = new MobHandler();

	/**
	 * Handles global shops.
	 */
	public static ShopHandler shopHandler = new ShopHandler();

	/**
	 * Handles global objects.
	 */
	public static ObjectHandler objectHandler;
	public static ObjectManager objectManager = new ObjectManager();
	
	public static World playerWorld = null;

	/**
	 * Handles the task scheduler.
	 */
	private static final TaskScheduler scheduler = new TaskScheduler();

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
	
	public static void initializeComponents() throws Exception {
		
		new PluginManager();
		ObjectDef.loadConfig();
		Region.load();
		playerWorld = World.getSingleton();
		playerWorld.setupPlayerRegions();
		new MusicLoader();
		MusicLoader.load();
		objectHandler = new ObjectHandler();
		ItemHandler.loadItemDefinitions();
		new WeaponDelayLoader().load();
		new NpcDefinitionLoader().load();		
		new MobSpawnLoader().load();
		new MobDrop();
		MobDrop.load();

	}

	/**
	 * Starts the server.
	 * @throws Exception 
	 */
	public static void main(java.lang.String args[])
			throws Exception {

		logger.info("Creating Game server...");

		initializeComponents();
		
		bind();

		playerManager = PlayerManager.getSingleton();
		playerManager.setupRegionPlayers();
		stillGraphicsManager = new StillGraphicsManager();
	
		logger.info("Server Initialized.");

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
		logger.info("Server bound to port: " + serverlistenerPort);
	}
}