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
		c.getActionSender().sendFrame126(title, 6180);
		c.getActionSender().sendFrame126(text, 6181);
		c.getActionSender().sendFrame126(text1, 6182);
		c.getActionSender().sendFrame126(text2, 6183);
		c.getActionSender().sendFrame126(text3, 6184);
		c.getActionSender().sendFrame164(6179);
	}
	
	/*
	 * Options
	 */	
	public void sendOption(String s) {
		c.getActionSender().sendFrame126("Select an Option", 2470);
	 	c.getActionSender().sendFrame126(s, 2471);
		c.getActionSender().sendFrame126("Click here to continue", 2473);
		c.getActionSender().sendFrame164(13758);
	}	
	
	public void sendOption2(String s, String s1) {
		c.getActionSender().sendFrame126("Select an Option", 2460);
		c.getActionSender().sendFrame126(s, 2461);
		c.getActionSender().sendFrame126(s1, 2462);
		c.getActionSender().sendFrame164(2459);
	}
	
	public void sendOption3(String s, String s1, String s2) {
		c.getActionSender().sendFrame126("Select an Option", 2470);
		c.getActionSender().sendFrame126(s, 2471);
		c.getActionSender().sendFrame126(s1, 2472);
		c.getActionSender().sendFrame126(s2, 2473);
		c.getActionSender().sendFrame164(2469);
	}
	
	public void sendOption4(String s, String s1, String s2, String s3) {
		c.getActionSender().sendFrame126("Select an Option", 2481);
		c.getActionSender().sendFrame126(s, 2482);
		c.getActionSender().sendFrame126(s1, 2483);
		c.getActionSender().sendFrame126(s2, 2484);
		c.getActionSender().sendFrame126(s3, 2485);
		c.getActionSender().sendFrame164(2480);
	}
	
	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		c.getActionSender().sendFrame126("Select an Option", 2493);
		c.getActionSender().sendFrame126(s, 2494);
		c.getActionSender().sendFrame126(s1, 2495);
		c.getActionSender().sendFrame126(s2, 2496);
		c.getActionSender().sendFrame126(s3, 2497);
		c.getActionSender().sendFrame126(s4, 2498);
		c.getActionSender().sendFrame164(2492);
	}

	/*
	 * Statements
	 */	
	private void sendStatement(String s) { // 1 line click here to continue chat box interface
		c.getActionSender().sendFrame126(s, 357);
		c.getActionSender().sendFrame126("Click here to continue", 358);
		c.getActionSender().sendFrame164(356);
	}
	
	/*
	 * Npc Chatting
	 */	
	private void sendNpcChat1(String s, int ChatNpc, String name) {
		c.getActionSender().sendFrame200(4883, 591);
		c.getActionSender().sendFrame126(name, 4884);
		c.getActionSender().sendFrame126(s, 4885);
		c.getActionSender().sendFrame75(ChatNpc, 4883);
		c.getActionSender().sendFrame164(4882);
	}
	
	private void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		c.getActionSender().sendFrame200(4888, 591);
		c.getActionSender().sendFrame126(name, 4889);
		c.getActionSender().sendFrame126(s, 4890);
		c.getActionSender().sendFrame126(s1, 4891);
		c.getActionSender().sendFrame75(ChatNpc, 4888);
		c.getActionSender().sendFrame164(4887);
	}

	private void sendNpcChat3(String s, String s1, String s2, int ChatNpc, String name) {
		c.getActionSender().sendFrame200(4894, 591);
		c.getActionSender().sendFrame126(name, 4895);
		c.getActionSender().sendFrame126(s, 4896);
		c.getActionSender().sendFrame126(s1, 4897);
		c.getActionSender().sendFrame126(s2, 4898);
		c.getActionSender().sendFrame75(ChatNpc, 4894);
		c.getActionSender().sendFrame164(4893);
	}
	
	private void sendNpcChat4(String s, String s1, String s2, String s3, int ChatNpc, String name) {
		c.getActionSender().sendFrame200(4901, 591);
		c.getActionSender().sendFrame126(name, 4902);
		c.getActionSender().sendFrame126(s, 4903);
		c.getActionSender().sendFrame126(s1, 4904);
		c.getActionSender().sendFrame126(s2, 4905);
		c.getActionSender().sendFrame126(s3, 4906);
		c.getActionSender().sendFrame75(ChatNpc, 4901);
		c.getActionSender().sendFrame164(4900);
	}
	
	/*
	 * Player Chating Back
	 */	
	private void sendPlayerChat1(String s) {
		c.getActionSender().sendFrame200(969, 591);
		c.getActionSender().sendFrame126(c.playerName, 970);
		c.getActionSender().sendFrame126(s, 971);
		c.getActionSender().sendFrame185(969);
		c.getActionSender().sendFrame164(968);
	}
	
	private void sendPlayerChat2(String s, String s1) {
		c.getActionSender().sendFrame200(974, 591);
		c.getActionSender().sendFrame126(c.playerName, 975);
		c.getActionSender().sendFrame126(s, 976);
		c.getActionSender().sendFrame126(s1, 977);
		c.getActionSender().sendFrame185(974);
		c.getActionSender().sendFrame164(973);
	}
	
	private void sendPlayerChat3(String s, String s1, String s2) {
		c.getActionSender().sendFrame200(980, 591);
		c.getActionSender().sendFrame126(c.playerName, 981);
		c.getActionSender().sendFrame126(s, 982);
		c.getActionSender().sendFrame126(s1, 983);
		c.getActionSender().sendFrame126(s2, 984);
		c.getActionSender().sendFrame185(980);
		c.getActionSender().sendFrame164(979);
	}
	
	private void sendPlayerChat4(String s, String s1, String s2, String s3) {
		c.getActionSender().sendFrame200(987, 591);
		c.getActionSender().sendFrame126(c.playerName, 988);
		c.getActionSender().sendFrame126(s, 989);
		c.getActionSender().sendFrame126(s1, 990);
		c.getActionSender().sendFrame126(s2, 991);
		c.getActionSender().sendFrame126(s3, 992);
		c.getActionSender().sendFrame185(987);
		c.getActionSender().sendFrame164(986);
	}
}
