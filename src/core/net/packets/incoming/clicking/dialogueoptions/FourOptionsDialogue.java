package core.net.packets.incoming.clicking.dialogueoptions;

import core.game.content.dialogue.DialogueType;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;

/**
 * A class that handles 4 options within a dialogue
 * @author 7Winds
 */
public class FourOptionsDialogue extends DialogueOption {

	/**
	 * A method used to handle all the options in a dialogue with
	 * four options
	 * @param player
	 * @param dialogueType
	 */
	@Override
	public void handleOption(Player player, DialogueType dialogueType) {
		switch(dialogueType) {
		
		case FOUR_OPTIONS_1:
			switch (player.teleAction) {
			
			default:
				if (player.getRights().equal(Rights.DEVELOPER))
					player.sendMessage("HandleOption1: TeleAction: " + player.teleAction);
				break;
			}

			switch (player.dialogueAction) {
			
			default:
				if (player.getRights().equal(Rights.DEVELOPER))
					player.sendMessage("HandleOption4: DialogueAction: " + player.dialogueAction);
				break;
			}
			break;
			
		case FOUR_OPTIONS_2:
			switch (player.teleAction) {
			
			default:
				if (player.getRights().equal(Rights.DEVELOPER))
					player.sendMessage("HandleOption2: TeleAction: " + player.teleAction);
				break;
			}

			switch (player.dialogueAction) {
			
			default:
				if (player.getRights().equal(Rights.DEVELOPER))
					player.sendMessage("HandleOption4: DialogueAction: " + player.dialogueAction);
				break;
			}
			break;
			
		case FOUR_OPTIONS_3:
			switch (player.teleAction) {
			
			default:
				if (player.getRights().equal(Rights.DEVELOPER))
					player.sendMessage("HandleOption3: TeleAction: " + player.teleAction);
				break;
			}

			switch (player.dialogueAction) {
			
			default:
				if (player.getRights().equal(Rights.DEVELOPER))
					player.sendMessage("HandleOption4: DialogueAction: " + player.dialogueAction);
				break;
			}
			break;
			
		case FOUR_OPTIONS_4:
			switch (player.teleAction) {
			
			default:
				if (player.getRights().equal(Rights.DEVELOPER))
					player.sendMessage("HandleOption4: TeleAction: " + player.teleAction);
				break;
			}

			switch (player.dialogueAction) {
			
			default:
				if (player.getRights().equal(Rights.DEVELOPER))
					player.sendMessage("HandleOption4: DialogueAction: " + player.dialogueAction);
				break;
			}
			break;
			
		default:
			break;		
		}		
	}
}
