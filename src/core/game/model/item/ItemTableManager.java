package core.game.model.item;

import com.google.gson.Gson;

import core.Config;
import core.game.util.Misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * ItemTableManager.java
 * 
 * Loads individual Items from JSON Files
 * 
 * @author Dragonking
 *
 */
public class ItemTableManager {

	public static final HashMap<Integer, PlayerItem> ITEMS = new HashMap<>();
	
	public static final PlayerItem forID(final int npcID) {
		if (!ITEMS.containsKey(npcID))
			return ITEMS.get(1);

		return ITEMS.get(npcID);
	}
	
	public static final PlayerItem forName(final int itemId) {
		if (!ITEMS.containsKey(itemId))
			return ITEMS.get(1);
		return ITEMS.get(itemId);
	}

	public static final void load() {
		final Gson gson = new Gson();
		final File dir = new File(Config.DATA_DIR + "json/items");

		for (final File file : dir.listFiles()) {
			try (final BufferedReader parse = new BufferedReader(
					new FileReader(file))) {
				// System.out.println("Loading: "+file.getName());
				if (!file.getName().contains("DS_Store")) {
					final PlayerItem table = gson.fromJson(parse,
							PlayerItem.class);
					ITEMS.put(table.id, table);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Misc.println("Loaded " + ITEMS.size() + " Item Definitions");
	}
}

