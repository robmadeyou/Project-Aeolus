package core.net.packets.incoming.clicking.dialogueoptions;

import core.game.content.dialogue.DialogueType;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Rights;

/**
 * A class that handles all 5 options within a dialogue
 * @author 7Winds
 */
public class FiveOptionsDialogue extends DialogueOption {
	
	/**
	 * A method used to handle all the options in a dialogue with
	 * five options
	 * @param player
	 * @param dialogueType
	 */
	@Override
	public void handleOption(Player player, DialogueType dialogueType) {
		switch(dialogueType) {
		case FIVE_OPTIONS_1:
			switch (player.teleAction) {

			default:
				if (player.getRights().equal(Rights.DEVELOPER))
				player.sendMessage("Unhandled teleAction: " + player.teleAction);
				break;
			}

			switch (player.dialogueAction) {

			default:
				if(player.getRights().equal(Rights.DEVELOPER))
				player.sendMessage("Unhandled dialogueAction: "
						+ player.dialogueAction);
				break;
			}			
			break;
			
			
		case FIVE_OPTIONS_2:			
			switch (player.teleAction) {

			default:
				if(player.getRights().equal(Rights.DEVELOPER))
				player.sendMessage("Unhandled dialogueAction: "
						+ player.dialogueAction);
				break;
			}
			
			break;
			
		case FIVE_OPTIONS_3:
			switch (player.teleAction) {

			default:
				if (player.getRights().equal(Rights.DEVELOPER))
				player.sendMessage("Unhandled teleAction: " + player.teleAction);
				break;
			}

			switch (player.dialogueAction) {


			default:
				if(player.getRights().equal(Rights.DEVELOPER))
				player.sendMessage("Unhandled dialogueAction: "
						+ player.dialogueAction);
				break;
			}
			break;
		case FIVE_OPTIONS_4:
			switch (player.teleAction) {

			default:
				if (player.getRights().equal(Rights.DEVELOPER))
				player.sendMessage("Unhandled teleAction: " + player.teleAction);
				break;
			}

			switch (player.dialogueAction) {

			default:
				if(player.getRights().equal(Rights.DEVELOPER))
				player.sendMessage("Unhandled dialogueAction: "
						+ player.dialogueAction);
				break;
			}
			break;
		case FIVE_OPTIONS_5:
			switch (player.teleAction) {

			default:
				if (player.getRights().equal(Rights.DEVELOPER))
				player.sendMessage("Unhandled teleAction: " + player.teleAction);
				break;
			}
			switch (player.dialogueAction) {


			default:
				if(player.getRights().equal(Rights.DEVELOPER))
				player.sendMessage("Unhandled dialogueAction: "
						+ player.dialogueAction);
				break;
			}
			break;
		default:			
			break;		
		}		
	}
}
