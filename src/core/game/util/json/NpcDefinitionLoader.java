package core.game.util.json;

import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import core.Configuration;
import core.game.model.entity.mob.MobDefinition;
import core.game.util.JsonLoader;
import core.game.util.Misc;

/**
 * The {@link JsonLoader} implementation that loads all npc definitions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDefinitionLoader extends JsonLoader {

	/**
	 * Creates a new {@link NpcDefinitionLoader}.
	 */
	public NpcDefinitionLoader() {
		super(Configuration.DATA_DIR + "./json/npc_definitions.json");
		Misc.println("Loaded " + MobDefinition.DEFINITIONS.length + " NPC Definitions");
	}

	@Override
	public void load(JsonObject reader, Gson builder) {
		int index = reader.get("id").getAsInt();
		String name = Objects.requireNonNull(reader.get("name").getAsString());
		String description = Objects.requireNonNull(reader.get("examine")
				.getAsString());
		int combatLevel = reader.get("combat").getAsInt();
		int size = reader.get("size").getAsInt();
		boolean attackable = reader.get("attackable").getAsBoolean();
		boolean aggressive = reader.get("aggressive").getAsBoolean();
		boolean retreats = reader.get("retreats").getAsBoolean();
		boolean poisonous = reader.get("poisonous").getAsBoolean();
		int respawnTime = reader.get("respawn").getAsInt();
		int maxHit = reader.get("maxHit").getAsInt();
		int hitpoints = reader.get("hitpoints").getAsInt();
		int attackSpeed = reader.get("attackSpeed").getAsInt();
		int attackAnim = reader.get("attackAnim").getAsInt();
		int defenceAnim = reader.get("defenceAnim").getAsInt();
		int deathAnim = reader.get("deathAnim").getAsInt();
		int attackBonus = reader.get("attackBonus").getAsInt();
		int meleeDefence = reader.get("defenceMelee").getAsInt();
		int rangedDefence = reader.get("defenceRange").getAsInt();
		int magicDefence = reader.get("defenceMage").getAsInt();

		MobDefinition.DEFINITIONS[index] = new MobDefinition(index, name,
				description, combatLevel, size, attackable, aggressive,
				retreats, poisonous, respawnTime, maxHit, hitpoints,
				attackSpeed, attackAnim, defenceAnim, deathAnim, attackBonus,
				meleeDefence, rangedDefence, magicDefence);
//		if (aggressive)
//			NpcAggression.AGGRESSIVE.add(index);
	}
}