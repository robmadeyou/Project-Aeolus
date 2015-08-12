package core.game.util.json;

import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import core.Configuration;
import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.mob.MobSpawn;
import core.game.model.entity.mob.WalkType;
import core.game.util.JsonLoader;

public class MobSpawnLoader extends JsonLoader {
	
	public MobSpawnLoader() {
		super(Configuration.DATA_DIR + "json/mob_spawns.json");
		System.out.println("Loaded: Mob Spawns.");
	}
	
	@Override
	public void load(JsonObject reader, Gson builder) {
		int npcId = reader.get("npcId").getAsInt();
		int xPos = reader.get("xPos").getAsInt();
		int yPos = reader.get("yPos").getAsInt();
		int height = reader.get("height").getAsInt();
		String walkType = Objects.requireNonNull(reader.get("walkType").getAsString());
		MobSpawn spawn = new MobSpawn(npcId, xPos, yPos, height, WalkType.valueOf(walkType));
		MobHandler.newNPC(spawn);
	}
}
