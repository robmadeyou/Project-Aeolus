package core.game.model.entity;

import core.game.model.entity.UpdateFlags.UpdateFlag;
import core.game.model.entity.mob.Mob;
import core.game.model.entity.player.Player;

/**
 * An Entity class representing a Player and Npc
 */
public abstract class Entity {
	
	/**
	 * The position of this entity
	 */
	private Position position;
	
	public int slot = -1;
	
	/**
	 * Haven't implmeneted the new position system yet
	 */
	public int absX, absY, absZ;	

    public int moveX, moveY, direction;
    
    public boolean chatTextUpdateRequired = false;
    
    /** Update flags. */
    public boolean updateRequired = true, appearanceUpdateRequired = true,
            animationUpdateRequired, graphicsUpdateRequired = false,
            forcedChatUpdateRequired, faceEntityUpdateRequired,
            facePositionUpdateRequired, hitUpdateRequired = false,
            secondaryHitUpdateRequired;
    
    public String forcedChat;

	/**
	 * The entity interacting with the current entity
	 */
	private Entity interactingEntity;

	/**
	 * Creates an instance of the update flags for every entity
	 */
	private UpdateFlags updateFlags = new UpdateFlags();

	private Graphic graphic;

	/**
	 * Returns the entity interacting with the current entity
	 * 
	 * @return
	 */
	public Entity getInteractingEntity() {
		return interactingEntity;
	}

	/**
	 * Sets the interacting entity
	 * 
	 * @param entity
	 */
	public void setInteractingEntity(Entity entity) {
		this.interactingEntity = entity;
	}
	
    /**
     * Face another entity.
     * 
     * @param slot
     *        the slot of the other entity to face.
     */
    public void faceEntity(int slot) {
        entity = slot;
        updateRequired = true;
        faceEntityUpdateRequired = true;
    }
    
    /**
     * Faces the specified coordinates.
     * 
     * @param x
     *        the x coordinate to face.
     * @param y
     *        the y coordinate to face.
     */
    public void facePosition(int x, int y) {
        faceX = 2 * x + 1;
        faceY = 2 * y + 1;
        updateRequired = true;
        facePositionUpdateRequired = true;
    }
	
	/**
	 * The mobs index.
	 */
	public int index = -1;

	/**
	 * Sets the index.
	 * 
	 * @param index
	 *            The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
    public int animation = -1, animationWait = -1, gfx = -1, gfxVar2 = -1,
            entity = -1, faceX = -1, faceY = -1;
    public Hit hit;
    public Hit secondaryHit;
	
    /**
     * Resets update flags and the corresponding values for those update flags.
     */
    public void reset() {
        updateRequired = false;
        appearanceUpdateRequired = false;
        animationUpdateRequired = false;
        graphicsUpdateRequired = false;
        forcedChatUpdateRequired = false;
        faceEntityUpdateRequired = false;
        facePositionUpdateRequired = false;
        hitUpdateRequired = false;
        secondaryHitUpdateRequired = false;
        gfx = -1;
        gfxVar2 = -1;
        animation = -1;
        animationWait = -1;
        forcedChat = null;
        moveX = 0;
        moveY = 0;
        entity = 65535;
        faceX = -1;
        faceY = -1;
        hit = null;
        secondaryHit = null;
    }

	/**
	 * Gets the index.
	 * 
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}
	
	public void forcedChat(String text) {
		forcedChat = text;
		forcedChatUpdateRequired = true;
		updateRequired = true;
		setAppearanceUpdateRequired(true);
	}
	
	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	/**
	 * Returns the update flags for every entity
	 * 
	 * @return updateFlags
	 */
	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}
	
	public void gfx100(int id) {
		gfx = id;
		gfxVar2 = 6553600;
		graphicsUpdateRequired = true;
		updateRequired = true;
	}

	public void gfx0(int id) {
		gfxVar2 = id;
		gfxVar2 = 65536;
		graphicsUpdateRequired = true;
		updateRequired = true;
	}
	
    /**
     * Deals primary damage to this entity.
     * 
     * @param hit
     *        the damage to deal.
     */
    public void dealHit(Hit hit) {
        this.hit = hit.clone();
        updateRequired = true;
        hitUpdateRequired = true;

        if (this instanceof Mob) {
            ((Mob) this).HP -= hit.getDamage();
        } else if (this instanceof Player) {
            Player player = (Player) this;
            if (player.teleTimer <= 0) {
                player.playerLevel[3] -= hit.getDamage();
                player.getActionSender().refreshSkill(3);
            }
        }
    }
    
    /**
     * Deals secondary damage to this entity.
     * 
     * @param hit
     *        the damage to deal.
     */
    public void dealSecondaryHit(Hit hit) {
        secondaryHit = hit.clone();
        updateRequired = true;
        secondaryHitUpdateRequired = true;

        if (this instanceof Mob) {
            ((Mob) this).HP -= hit.getDamage();
        } else if (this instanceof Player) {
            Player player = (Player) this;

            if (player.teleTimer <= 0) {
                player.playerLevel[3] -= hit.getDamage();
                player.getActionSender().refreshSkill(3);
            }
        }
    }
    
    /**
     * Gets an entities graphics
     */
	public Graphic getGraphic() {
		return graphic;
	}

	/**
	 * Sets an entities graphics
	 * @param graphic
	 */
	public void setGraphic(Graphic graphic) {
		this.graphic = graphic;
		getUpdateFlags().flag(UpdateFlag.GRAPHICS);
	}
	
	/**
	 * Gets an entities position
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Sets a players position to a new coordinate
	 * @param position
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * The tick method for both entities
	 */
	public abstract void process();

}
