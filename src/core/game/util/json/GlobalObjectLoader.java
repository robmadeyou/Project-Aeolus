package core.game.util.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import core.Config;
import core.game.model.object.ObjectHandler;
import core.game.model.object.Objects;
import core.game.util.JsonLoader;

public class GlobalObjectLoader extends JsonLoader {

	public GlobalObjectLoader() {
		super(Config.DATA_DIR + "json/global_objects.json");
	}

	@Override
	public void load(JsonObject reader, Gson builder) {
		int index = reader.get("objectId").getAsInt();
		int x = reader.get("objectX").getAsInt();
		int y = reader.get("objectY").getAsInt();
		int height = reader.get("objectHeight").getAsInt();
		int face = reader.get("objectFace").getAsInt();
		int type = reader.get("objectType").getAsInt();
		int tick = reader.get("objectTicks").getAsInt();
		
		ObjectHandler.globalObjects.add(new Objects(index, x, y, height, face, type, tick));
		System.out.println("Created: " + ObjectHandler.globalObjects.size() + " Global Objects.");
	}

}
