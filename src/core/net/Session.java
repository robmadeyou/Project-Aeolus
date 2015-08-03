package core.net;

import org.jboss.netty.channel.Channel;

import core.game.model.entity.player.Player;

public class Session {
	
	private final Channel channel;
	private Player player;
	
	public Session(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
