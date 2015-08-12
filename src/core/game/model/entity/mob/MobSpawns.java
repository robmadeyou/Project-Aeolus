package core.game.model.entity.mob;

/**
 * @author 7Winds
 */
public final class MobSpawns {

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
	private WalkType walkType;

	public MobSpawns(int npcid, int xPos, int yPos, int height, WalkType walkType) {
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
	public WalkType getWalkType() {
		return walkType;
	}
}