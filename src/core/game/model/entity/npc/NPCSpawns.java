package core.game.model.entity.npc;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import core.Config;

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

	/**
	 * The highest possible hit for the npc.
	 */
	private int maxHit;

	/**
	 * The attack level of the npc.
	 */
	private int attack;

	/**
	 * The defence level of the npc.
	 */
	private int defence;

	public NPCSpawns(int npcid, int xPos, int yPos, int height, int walkType,
			int maxHit, int attack, int defence) {
		this.npcId = npcid;
		this.xPos = xPos;
		this.yPos = yPos;
		this.height = height;
		this.walkType = walkType;
		this.maxHit = maxHit;
		this.attack = attack;
		this.defence = defence;
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

	/**
	 * @return maxHit
	 */
	public int getMaxHit() {
		return maxHit;
	}

	/**
	 * @return attack
	 */
	public int getAttack() {
		return attack;
	}

	/**
	 * @return defence
	 */
	public int getDefence() {
		return defence;
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