package core.game.content;

import core.game.content.dialogue.DialogueEmotion;
import core.game.model.entity.Entity;
import core.game.model.entity.mob.Mob;
import core.game.model.entity.mob.MobHandler;
import core.game.model.entity.player.Player;

@SuppressWarnings("all")
public class DialogueHandler {

	private Player c;
	private DialogueEmotion dialogueEmotion;
	
	public DialogueHandler(Player Player) {
		this.c = Player;
	}
	
	/**
	 * Handles all talking
	 * @param dialogue The dialogue you want to use
	 * @param npcId The npc id that the chat will focus on during the chat
	 */
	public void sendDialogues(int dialogue, int npcId, int npcId2) {
		Mob npc = MobHandler.npcs[npcId2];
		
		c.talkingNpc = npcId;
		switch(dialogue) {
		case 1:			
			this.sendNpcChat1("Welcome to RuneScape.", c.talkingNpc, npc.getNpcName(npcId2));
			c.nextChat = 0;
			break;
		}
	}

	/*
	 * Information Box
	 */	
	public void sendStartInfo(String text, String text1, String text2, String text3, String title) {
		c.getActionSender().textOnInterface(title, 6180);
		c.getActionSender().textOnInterface(text, 6181);
		c.getActionSender().textOnInterface(text1, 6182);
		c.getActionSender().textOnInterface(text2, 6183);
		c.getActionSender().textOnInterface(text3, 6184);
		c.getActionSender().openBackDialogue(6179);
	}
	
	/*
	 * Options
	 */	
	public void sendOption(String s) {
		c.getActionSender().textOnInterface("Select an Option", 2470);
	 	c.getActionSender().textOnInterface(s, 2471);
		c.getActionSender().textOnInterface("Click here to continue", 2473);
		c.getActionSender().openBackDialogue(13758);
	}	
	
	public void sendOption2(String s, String s1) {
		c.getActionSender().textOnInterface("Select an Option", 2460);
		c.getActionSender().textOnInterface(s, 2461);
		c.getActionSender().textOnInterface(s1, 2462);
		c.getActionSender().openBackDialogue(2459);
	}
	
	public void sendOption3(String s, String s1, String s2) {
		c.getActionSender().textOnInterface("Select an Option", 2470);
		c.getActionSender().textOnInterface(s, 2471);
		c.getActionSender().textOnInterface(s1, 2472);
		c.getActionSender().textOnInterface(s2, 2473);
		c.getActionSender().openBackDialogue(2469);
	}
	
	public void sendOption4(String s, String s1, String s2, String s3) {
		c.getActionSender().textOnInterface("Select an Option", 2481);
		c.getActionSender().textOnInterface(s, 2482);
		c.getActionSender().textOnInterface(s1, 2483);
		c.getActionSender().textOnInterface(s2, 2484);
		c.getActionSender().textOnInterface(s3, 2485);
		c.getActionSender().openBackDialogue(2480);
	}
	
	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		c.getActionSender().textOnInterface("Select an Option", 2493);
		c.getActionSender().textOnInterface(s, 2494);
		c.getActionSender().textOnInterface(s1, 2495);
		c.getActionSender().textOnInterface(s2, 2496);
		c.getActionSender().textOnInterface(s3, 2497);
		c.getActionSender().textOnInterface(s4, 2498);
		c.getActionSender().openBackDialogue(2492);
	}

	/*
	 * Statements
	 */	
	private void sendStatement(String s) { // 1 line click here to continue chat box interface
		c.getActionSender().textOnInterface(s, 357);
		c.getActionSender().textOnInterface("Click here to continue", 358);
		c.getActionSender().openBackDialogue(356);
	}
	
	/*
	 * Npc Chatting
	 */	
	private void sendNpcChat1(String s, int ChatNpc, String name) {
		c.getActionSender().sendFrame200(4883, 591);
		c.getActionSender().textOnInterface(name, 4884);
		c.getActionSender().textOnInterface(s, 4885);
		c.getActionSender().showNpcHeadOnInterface(ChatNpc, 4883);
		c.getActionSender().openBackDialogue(4882);
	}
	
	private void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		c.getActionSender().sendFrame200(4888, 591);
		c.getActionSender().textOnInterface(name, 4889);
		c.getActionSender().textOnInterface(s, 4890);
		c.getActionSender().textOnInterface(s1, 4891);
		c.getActionSender().showNpcHeadOnInterface(ChatNpc, 4888);
		c.getActionSender().openBackDialogue(4887);
	}

	private void sendNpcChat3(String s, String s1, String s2, int ChatNpc, String name) {
		c.getActionSender().sendFrame200(4894, 591);
		c.getActionSender().textOnInterface(name, 4895);
		c.getActionSender().textOnInterface(s, 4896);
		c.getActionSender().textOnInterface(s1, 4897);
		c.getActionSender().textOnInterface(s2, 4898);
		c.getActionSender().showNpcHeadOnInterface(ChatNpc, 4894);
		c.getActionSender().openBackDialogue(4893);
	}
	
	private void sendNpcChat4(String s, String s1, String s2, String s3, int ChatNpc, String name) {
		c.getActionSender().sendFrame200(4901, 591);
		c.getActionSender().textOnInterface(name, 4902);
		c.getActionSender().textOnInterface(s, 4903);
		c.getActionSender().textOnInterface(s1, 4904);
		c.getActionSender().textOnInterface(s2, 4905);
		c.getActionSender().textOnInterface(s3, 4906);
		c.getActionSender().showNpcHeadOnInterface(ChatNpc, 4901);
		c.getActionSender().openBackDialogue(4900);
	}
	
	/*
	 * Player Chating Back
	 */	
	private void sendPlayerChat1(String s) {
		c.getActionSender().sendFrame200(969, 591);
		c.getActionSender().textOnInterface(c.playerName, 970);
		c.getActionSender().textOnInterface(s, 971);
		c.getActionSender().showPlayerHeadOnInterface(969);
		c.getActionSender().openBackDialogue(968);
	}
	
	private void sendPlayerChat2(String s, String s1) {
		c.getActionSender().sendFrame200(974, 591);
		c.getActionSender().textOnInterface(c.playerName, 975);
		c.getActionSender().textOnInterface(s, 976);
		c.getActionSender().textOnInterface(s1, 977);
		c.getActionSender().showPlayerHeadOnInterface(974);
		c.getActionSender().openBackDialogue(973);
	}
	
	private void sendPlayerChat3(String s, String s1, String s2) {
		c.getActionSender().sendFrame200(980, 591);
		c.getActionSender().textOnInterface(c.playerName, 981);
		c.getActionSender().textOnInterface(s, 982);
		c.getActionSender().textOnInterface(s1, 983);
		c.getActionSender().textOnInterface(s2, 984);
		c.getActionSender().showPlayerHeadOnInterface(980);
		c.getActionSender().openBackDialogue(979);
	}
	
	private void sendPlayerChat4(String s, String s1, String s2, String s3) {
		c.getActionSender().sendFrame200(987, 591);
		c.getActionSender().textOnInterface(c.playerName, 988);
		c.getActionSender().textOnInterface(s, 989);
		c.getActionSender().textOnInterface(s1, 990);
		c.getActionSender().textOnInterface(s2, 991);
		c.getActionSender().textOnInterface(s3, 992);
		c.getActionSender().showPlayerHeadOnInterface(987);
		c.getActionSender().openBackDialogue(986);
	}
}
