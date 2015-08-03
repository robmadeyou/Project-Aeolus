package core.game.model.entity.player;

import core.net.packets.incoming.clicking.npc.FirstClickNpc;
import core.net.packets.incoming.clicking.npc.SecondClickNpc;
import core.net.packets.incoming.clicking.npc.ThirdClickNpc;
import core.net.packets.incoming.clicking.object.FirstClickObject;
import core.net.packets.incoming.clicking.object.SecondClickObject;
import core.net.packets.incoming.clicking.object.ThirdClickObject;

public class ActionsPerformed {
	
	private Player player;
	
	public ActionsPerformed(Player player) {
		this.player = player;
	}

	public void firstClickObject(int objectType, int obX, int obY) {
		FirstClickObject.handleClick(player, objectType, obX, obY);
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		SecondClickObject.handleClick(player, objectType, obX, obY);
	}

	public void thirdClickObject(int objectType, int obX, int obY) {
		ThirdClickObject.handleClick(player, objectType, obX, obY);
	}

	public void firstClickNpc(int npcType) {
		FirstClickNpc.handleClick(player, npcType);
	}

	public void secondClickNpc(int npcType) {
		SecondClickNpc.handleClick(player, npcType);
	}

	public void thirdClickNpc(int npcType) {
		ThirdClickNpc.handleClick(player, npcType);
	}
}