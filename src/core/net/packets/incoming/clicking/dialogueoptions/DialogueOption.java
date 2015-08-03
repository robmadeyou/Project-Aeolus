package core.net.packets.incoming.clicking.dialogueoptions;

import core.game.content.dialogue.Dialogue;
import core.game.content.dialogue.DialogueType;
import core.game.model.entity.player.Player;

public abstract class DialogueOption extends Dialogue {

	public abstract void handleOption(Player player, DialogueType dialogueType);
	
}
