package core.game.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.Configuration;
import core.game.model.entity.player.Player;

/**
 * @author 7Winds
 * @information Class uses to censor names and words in public chat.
 * @date 3/21/15
 */
public class Censor {

	Player player;

	public Censor(Player player) {
		this.player = player;
	}

	@SuppressWarnings("unused")
	private static enum Type {
		SPAM, BADWORD, MASTER, ADMIN;
	}

	public static void performCensor(Player player, String message) {
		String chatText = Misc.textUnpack(player.getChatText(), player.getChatTextSize());
		String[] happy = { "yay", "woot", "w00t", "hooray" };

		for (String h : happy) {
			if (chatText.toLowerCase().contains(h)) {
				player.startAnimation(2109, 1);
			}
		}

		String[] funny = { "lol", "l0l", "LOL", "lmao", "lmfao", "rofl", "lawl" }; // More
																					// here...

		for (String f : funny) {
			if (chatText.contains(f)) {
				player.startAnimation(861, 1);
			}
		}

		String[] confused = { "what?", "who?", "when?", "where?", "huh?",
				"what the hell?", "what are you talking about?", "wtf?" }; // More
																			// here...

		for (String con : confused) {
			if (chatText.toLowerCase().contains(con)) {
				player.startAnimation(857, 1);
			}
		}
		for (String bad : Censor.BAD_WORDS) {
			if (chatText.toLowerCase().contains(bad)) {
				player.getChatLog().logChat(chatText);
			}
		}
		player.getChatLog().logMasterChat(chatText);
	}
	
	public void unpackText() {
		Misc.textUnpack(player.getChatText(), player.getChatTextSize());
	}

	public static final String[] INVALID_USERNAMES = { "mod", "admin" };
	public static final String[] VALID_USERNAMES = { "mod wind"};
	public static final List<String> BAD_WORDS = Arrays.asList("anal", "anus",
			"arse", "ass", "ballsack", "balls", "bastard", "bitch", "biatch",
			"bloody", "blowjob", "blow job", "bollock", "bollok", "boner",
			"boob", "bugger", "bum", "butt", "buttplug", "clitoris", "cock",
			"coon", "crap", "cunt", "dick", "dildo", "dyke", "fag", "feck",
			"fellate", "fellatio", "felching", "fuck", "f u c k",
			"fudgepacker", "fudge packer", "flange", "Goddamn", "God damn",
			"homo", "jerk", "jizz", "knobend", "knob end", "labia", "muff",
			"nigger", "nigga", "penis", "piss", "poop", "prick", "pube",
			"pussy", "queer", "scrotum", "sex", "shit", "s hit", "sh1t",
			"slut", "smegma", "spunk", "tit", "tosser", "turd", "twat",
			"vagina", "wank", "whore", "wtf");

	public List<String> censored = new ArrayList<String>();

	public Censor() {
		loadWords();
		System.out.println("Loaded: " + censored.size()
				+ " Censored Words");
	}

	private void loadWords() {
		String word = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					Configuration.DATA_DIR + "/Censor.txt"));
			while ((word = in.readLine()) != null) {
				censored.add(word);
			}
			in.close();
			in = null;
		} catch (Exception e) {
			Misc.println("Error loading censor.");
		}
	}

	public static final boolean isInvalid(String name) {
		name = name.toLowerCase();
		for (String invalidNames : INVALID_USERNAMES) {
			if (name.contains(invalidNames) && !name.equals(VALID_USERNAMES)) {
				return true;
			}
		}
		return false;
	}

	public static final boolean isBadName(String word) {
		return BAD_WORDS.stream().filter(p -> p.contains(word.toLowerCase()))
				.count() > 0;
	}
}

