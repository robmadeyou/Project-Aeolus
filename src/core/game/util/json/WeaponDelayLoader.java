package core.game.util.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import core.Configuration;
import core.game.model.item.ItemHandler;
import core.game.model.item.WeaponDelay;
import core.game.util.JsonLoader;

public class WeaponDelayLoader extends JsonLoader {

	public WeaponDelayLoader() {
		super(Configuration.DATA_DIR + "json/equipment/weapon_delays.json");
		System.out.println("Loaded: " + ItemHandler.weaponDelay.size() + " weapon delays.");
	}

	@Override
	public void load(JsonObject reader, Gson builder) {
		int id = reader.get("id").getAsInt();
		int delay = reader.get("delay").getAsInt();
		ItemHandler.weaponDelay.add(new WeaponDelay(id, delay));
	}

}
