package core.game.content;

import core.game.model.entity.player.Player;

public class ContentManager {

	private Player c;
	
	private Trading trading = new Trading(c);
	private Dueling dueling = new Dueling(c);
	
	public ContentManager(Player c) {
		this.c = c;
	}

	public Trading getTrading() {
		return trading;
	}

	public void setTrading(Trading trading) {
		this.trading = trading;
	}

	public Dueling getDueling() {
		return dueling;
	}

	public void setDueling(Dueling dueling) {
		this.dueling = dueling;
	}
	
}
