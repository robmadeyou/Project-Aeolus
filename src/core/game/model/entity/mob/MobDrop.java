package core.game.model.entity.mob;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import core.Configuration;
import core.game.model.entity.mob.drop.Drop;

/**
 * A class that is used to deserialize the mob drops
 */
public class MobDrop {

	/**
	 * The directory/location of drops.json
	 */
	private static final File DROP_DIR = new File(
			Configuration.DATA_DIR + "json/npc_drops.json");

	/**
	 * Our single instance of Gson
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
			.create();

	/**
	 * Stores all the npc drops
	 */
	private static HashMap<Integer, Drop[]> npcDrops;

	/**
	 * Used to load the npc drops
	 */

	/**
	 * Returns an npc drop from a given index
	 * @param npcId
	 */
	public static Drop[] getDrops(int npcId) {
		return npcDrops.get(npcId);
	}

	private Map<Integer, ArrayList<Drop>> dropMapx = null;

	public Map<Integer, ArrayList<Drop>> getDropArray() {

		if (dropMapx == null)
			dropMapx = new LinkedHashMap<Integer, ArrayList<Drop>>();
		for (int i : npcDrops.keySet()) {
			int npcId = i;
			ArrayList<Drop> temp = new ArrayList<Drop>();
			for (Drop mainDrop : npcDrops.get(npcId)) {
				temp.add(mainDrop);
			}
			dropMapx.put(i, temp);
		}

		return dropMapx;
	}

	/**
	 * Deserializes the npc drops and converts them to objects
	 */
	public static void load() {
		final long start = System.currentTimeMillis();
		try {
			Type t = new TypeToken<HashMap<Integer, Drop[]>>() {
			}.getType();
			npcDrops = GSON.fromJson(new FileReader(DROP_DIR), t);
			System.out.println("Loaded: " + npcDrops.size()
					+ " NPC Drops in " + (System.currentTimeMillis() - start)
					+ "ms.");
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the npc drops from the hashmap
	 */
	public static HashMap<Integer, Drop[]> getDropMap() {
		return npcDrops;
	}
}
