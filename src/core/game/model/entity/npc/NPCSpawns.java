package core.game.model.entity.npc;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import core.Config;

/**
 * @author 7Winds
 */
public final class NPCSpawns {

	/**
	 * The Id of the npc. Commonly referred to as npcType.
	 */
	private int npcId;

	/**
	 * The home x-pos to the npc
	 */
	private int xPos;

	/**
	 * The home y-pos to the npc
	 */
	private int yPos;

	/**
	 * The height level where the npc is spawned
	 */
	private int height;

	/**
	 * The Walking type for the npc.
	 */
	private int walkType;

	public NPCSpawns(int npcid, int xPos, int yPos, int height, int walkType) {
		this.npcId = npcid;
		this.xPos = xPos;
		this.yPos = yPos;
		this.height = height;
		this.walkType = walkType;
	}

	/**
	 * @return npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * @return xPos
	 */
	public int getXPos() {
		return xPos;
	}

	/**
	 * @return yPos
	 */
	public int getYPos() {
		return yPos;
	}

	/**
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return walkType
	 */
	public int getWalkType() {
		return walkType;
	}

	public static final class NpcSpawnBuilder {

		protected static NPCSpawns[] deserialize() {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			try (final FileReader reader = new FileReader(
					Config.DATA_DIR + "json/npc_spawns.json")) {
				return gson.fromJson(reader, NPCSpawns[].class);
			} catch (IOException e) {
				return null;
			}
		}
	}
}