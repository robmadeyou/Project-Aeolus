package core.net.packets.incoming.clicking.dialogueoptions;

import core.game.content.dialogue.DialogueType;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;

/**
 * A class that handles 2 options within a dialogue
 * @author 7Winds
 */
public class TwoOptionDialogue extends DialogueOption {

	/**
	 * A method used to handle all the options in a dialogue with
	 * two options
	 * @param player
	 * @param dialogueType
	 */
	@Override
	public void handleOption(Player player, DialogueType dialogueType) {
		switch(dialogueType) {
		
		case TWO_OPTIONS_1:
			switch (player.dialogueAction) {

			default:
				if(player.getRights().equal(Rights.DEVELOPER))
					player.sendMessage("HandleOption2: DialogueAction: " + player.dialogueAction);
				break;
			}
			break;
			
		case TWO_OPTIONS_2:
			switch (player.dialogueAction) {

			default:
				if(player.getRights().equal(Rights.DEVELOPER))
					player.sendMessage("HandleOption2: DialogueAction: " + player.dialogueAction);
				break;
			}
			break;
			
		default:
			break;
		
		}		
	}
}
