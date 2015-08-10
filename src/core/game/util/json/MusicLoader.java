package core.game.util.json;

import java.io.File;
import java.io.FileReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import core.Config;
import core.game.sound.MusicManager;
import core.game.sound.region.Music;

/**
 * A class which loads all music into the game.
 * @author 7Winds
 */
public class MusicLoader {
	
	/**
	 * Loads the music data from a .JSON file.
	 * 
	 * @throws Exception
	 *            If any exception happens.
	 */
	public static void load() throws Exception {
		System.out.println("Loading music...");
		
		JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(new FileReader(new File(Config.DATA_DIR + "json/music.json")));
        MusicManager.music = new Music[array.size()];
		int count = 0;
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject reader = (JsonObject) array.get(i);

			int region = -1;
			if (reader.has("region"))
				region = reader.get("region").getAsInt();
			String name = reader.get("name").getAsString();
			int song = reader.get("song").getAsInt();
			int frame = reader.get("frame").getAsInt();
			int button = reader.get("button").getAsInt();
			
			MusicManager.music[i] = new Music(region, name, song, frame, button);
			count++;
		}		
		System.out.println("Loaded: "+ count +" songs.");	
	}	
}
