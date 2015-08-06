package core.game.model.object;

public class Objects {

	public int objectId;
	public int objectX;
	public int objectY;
	public int objectHeight;
	public int objectFace;
	public int objectType;
	public int objectTicks;

	public Objects(int id, int x, int y, int height, int face, int type,
			int ticks) {
		this.objectId = id;
		this.objectX = x;
		this.objectY = y;
		this.objectHeight = height;
		this.objectFace = face;
		this.objectType = type;
		this.objectTicks = ticks;
	}

	public int getId() {
		return this.objectId;
	}

	public int getX() {
		return this.objectX;
	}

	public int getY() {
		return this.objectY;
	}

	public int getHeight() {
		return this.objectHeight;
	}

	public int getFace() {
		return this.objectFace;
	}

	public int getType() {
		return this.objectType;
	}

}