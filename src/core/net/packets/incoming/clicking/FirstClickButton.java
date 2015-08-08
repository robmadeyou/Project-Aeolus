package core.net.packets.incoming.clicking;

import core.game.GameConstants;
import core.game.content.dialogue.DialogueType;
import core.game.event.tick.Tick;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.entity.player.Rights;
import core.game.model.item.GameItem;
import core.game.plugin.PluginManager;
import core.game.sound.region.MusicTab;
import core.net.packets.incoming.clicking.dialogueoptions.DialogueOption;
import core.net.packets.incoming.clicking.dialogueoptions.FiveOptionsDialogue;
import core.net.packets.incoming.clicking.dialogueoptions.FourOptionsDialogue;
import core.net.packets.incoming.clicking.dialogueoptions.ThreeOptionsDialogue;
import core.net.packets.incoming.clicking.dialogueoptions.TwoOptionDialogue;

/**
 * @author 7Winds
 * Used to organize all the clicking buttons.
 */
public class FirstClickButton {	

	/**
	 * Handles a single button click
	 * @param c
	 * @param actionButtonId
	 */
	public static void handleClick(final Player c, int actionButtonId) {
		
		DialogueOption twoOptions = new TwoOptionDialogue(), threeOptions = new ThreeOptionsDialogue(), fourOptions = new FourOptionsDialogue(), fiveOptions = new FiveOptionsDialogue();
		
		MusicTab.handleClick(c, actionButtonId);
		switch (actionButtonId) {
		
		case 89061:
			c.autoRet = !c.autoRet;
			break;
		
		/**
		 * Dialogue Options
		 */			
		case 9157: // 1st option (2)
			twoOptions.handleOption(c, DialogueType.TWO_OPTIONS_1);
			break;			
		case 9158: // 2nd option (2)
			twoOptions.handleOption(c, DialogueType.TWO_OPTIONS_2);
			break;
			
		case 9167: // 1st option (3)
			threeOptions.handleOption(c, DialogueType.THREE_OPTIONS_1);
			break;			
		case 9168: // 2nd option (3)
			threeOptions.handleOption(c, DialogueType.THREE_OPTIONS_2);
			break;			
		case 9169: // 3rd option (3)
			threeOptions.handleOption(c, DialogueType.THREE_OPTIONS_3);
			break;
			
		case 9178: // 1st option (4)
			fourOptions.handleOption(c, DialogueType.FOUR_OPTIONS_1);
			break;			
		case 9179: // 2nd option (4)
			fourOptions.handleOption(c, DialogueType.FOUR_OPTIONS_2);
			break;			
		case 9180: // 3rd option (4)
			fourOptions.handleOption(c, DialogueType.FOUR_OPTIONS_3);
			break;			
		case 9181: // 4th option (4)
			fourOptions.handleOption(c, DialogueType.FOUR_OPTIONS_4);
			break;
			
		case 9190: // 1st option (5)
			fiveOptions.handleOption(c, DialogueType.FIVE_OPTIONS_1);
		break;		
		case 9191: // 2nd option (5)
			fiveOptions.handleOption(c, DialogueType.FIVE_OPTIONS_2);
			break;			
		case 9192: // 3rd option (5)
			fiveOptions.handleOption(c, DialogueType.FIVE_OPTIONS_3);
			break;			
		case 9193: // 4th option (5)
			fiveOptions.handleOption(c, DialogueType.FIVE_OPTIONS_4);
			break;			
		case 9194: // 5th option (5)
			fiveOptions.handleOption(c, DialogueType.FIVE_OPTIONS_5);
			break;

		/** Dueling **/
		case 26065: // no forfeit
		case 26040:
			c.duelSlot = -1;
			c.getContentManager().getDueling().selectRule(0);
			break;

		case 26066: // no movement
		case 26048:
			c.duelSlot = -1;
			c.getContentManager().getDueling().selectRule(1);
			break;

		case 26069: // no range
		case 26042:
			c.duelSlot = -1;
			c.getContentManager().getDueling().selectRule(2);
			break;

		case 26070: // no melee
		case 26043:
			c.duelSlot = -1;
			c.getContentManager().getDueling().selectRule(3);
			break;

		case 26071: // no mage
		case 26041:
			c.duelSlot = -1;
			c.getContentManager().getDueling().selectRule(4);
			break;

		case 26072: // no drinks
		case 26045:
			c.duelSlot = -1;
			c.getContentManager().getDueling().selectRule(5);
			break;

		case 26073: // no food
		case 26046:
			c.duelSlot = -1;
			c.getContentManager().getDueling().selectRule(6);
			break;

		case 26074: // no prayer
		case 26047:
			c.duelSlot = -1;
			c.getContentManager().getDueling().selectRule(7);
			break;

		case 26076: // obsticals
		case 26075:
			c.duelSlot = -1;
			c.getContentManager().getDueling().selectRule(8);
			break;

		case 2158: // fun weapons
		case 2157:
			c.duelSlot = -1;
			c.getContentManager().getDueling().selectRule(9);
			break;

		case 30136: // sp attack
		case 30137:
			c.duelSlot = -1;
			c.getContentManager().getDueling().selectRule(10);
			break;

		case 53245: // no helm
			c.duelSlot = 0;
			c.getContentManager().getDueling().selectRule(11);
			break;

		case 53246: // no cape
			c.duelSlot = 1;
			c.getContentManager().getDueling().selectRule(12);
			break;

		case 53247: // no ammy
			c.duelSlot = 2;
			c.getContentManager().getDueling().selectRule(13);
			break;

		case 53249: // no weapon.
			c.duelSlot = 3;
			c.getContentManager().getDueling().selectRule(14);
			break;

		case 53250: // no body
			c.duelSlot = 4;
			c.getContentManager().getDueling().selectRule(15);
			break;

		case 53251: // no shield
			c.duelSlot = 5;
			c.getContentManager().getDueling().selectRule(16);
			break;

		case 53252: // no legs
			c.duelSlot = 7;
			c.getContentManager().getDueling().selectRule(17);
			break;

		case 53255: // no gloves
			c.duelSlot = 9;
			c.getContentManager().getDueling().selectRule(18);
			break;

		case 53254: // no boots
			c.duelSlot = 10;
			c.getContentManager().getDueling().selectRule(19);
			break;

		case 53253: // no rings
			c.duelSlot = 12;
			c.getContentManager().getDueling().selectRule(20);
			break;

		case 53248: // no arrows
			c.duelSlot = 13;
			c.getContentManager().getDueling().selectRule(21);
			break;

		case 26018:
			Player o = (Player) PlayerHandler.players[c.duelingWith];
			if (o == null) {
				c.getContentManager().getDueling().declineDuel();
				return;
			}

			if (c.duelRule[2] && c.duelRule[3] && c.duelRule[4]) {
				c.sendMessage("You won't be able to attack the player with the rules you have set.");
				break;
			}
			c.duelStatus = 2;
			if (c.duelStatus == 2) {
				c.getPA().sendFrame126("Waiting for other player...", 6684);
				o.getPA().sendFrame126("Other player has accepted.", 6684);
			}
			if (o.duelStatus == 2) {
				o.getPA().sendFrame126("Waiting for other player...", 6684);
				c.getPA().sendFrame126("Other player has accepted.", 6684);
			}

			if (c.duelStatus == 2 && o.duelStatus == 2) {
				c.canOffer = false;
				o.canOffer = false;
				c.duelStatus = 3;
				o.duelStatus = 3;
				c.getContentManager().getDueling().confirmDuel();
				o.getContentManager().getDueling().confirmDuel();
			}
			break;

		case 25120:
			if (c.duelStatus == 5) {
				break;
			}
			Player o1 = (Player) PlayerHandler.players[c.duelingWith];
			if (o1 == null) {
				c.getContentManager().getDueling().declineDuel();
				return;
			}

			c.duelStatus = 4;
			if (o1.duelStatus == 4 && c.duelStatus == 4) {
				c.getContentManager().getDueling().startDuel();
				o1.getContentManager().getDueling().startDuel();
				o1.duelCount = 4;
				c.duelCount = 4;
				Player.schedule(c, new Tick(4) {
					@Override
					public void execute() {
						if (System.currentTimeMillis() - c.duelDelay > 800
								&& c.duelCount > 0) {
							if (c.duelCount != 1) {
								c.forcedChat("" + (--c.duelCount));
								c.duelDelay = System.currentTimeMillis();
							} else {
								c.damageTaken = new int[GameConstants.MAX_PLAYERS];
								c.forcedChat("FIGHT!");
								c.duelCount = 0;
							}
						}
						if (c.duelCount == 0) {
							this.stop();
						}
					}

					@Override
					public void stop() {
					}
				});
				c.duelDelay = System.currentTimeMillis();
				o1.duelDelay = System.currentTimeMillis();
			} else {
				c.getPA().sendFrame126("Waiting for other player...", 6571);
				o1.getPA().sendFrame126("Other player has accepted", 6571);
			}
			break;

		case 152:
			c.isRunning2 = !c.isRunning2;
			int frame = c.isRunning2 == true ? 1 : 0;
			c.getPA().sendFrame36(173, frame);
			break;

		case 13092:
			Player ot = (Player) PlayerHandler.players[c.tradeWith];
			if (ot == null) {
				c.getContentManager().getTrading().declineTrade();
				c.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}
			c.getPA().sendFrame126("Waiting for other player...", 3431);
			ot.getPA().sendFrame126("Other player has accepted", 3431);
			c.goodTrade = true;
			ot.goodTrade = true;

			for (GameItem item : c.getContentManager().getTrading().offeredItems) {
				if (item.id > 0) {
					if (ot.getInventory().freeSlots() < c.getContentManager().getTrading().offeredItems
							.size()) {
						c.sendMessage(ot.playerName
								+ " only has "
								+ ot.getInventory().freeSlots()
								+ " free slots, please remove "
								+ (c.getContentManager().getTrading().offeredItems.size() - ot
										.getInventory().freeSlots()) + " items.");
						ot.sendMessage(c.playerName
								+ " has to remove "
								+ (c.getContentManager().getTrading().offeredItems.size() - ot
										.getInventory().freeSlots())
								+ " items or you could offer them "
								+ (c.getContentManager().getTrading().offeredItems.size() - ot
										.getInventory().freeSlots()) + " items.");
						c.goodTrade = false;
						ot.goodTrade = false;
						c.getPA().sendFrame126("Not enough inventory space...",
								3431);
						ot.getPA().sendFrame126(
								"Not enough inventory space...", 3431);
						break;
					} else {
						c.getPA().sendFrame126("Waiting for other player...",
								3431);
						ot.getPA().sendFrame126("Other player has accepted",
								3431);
						c.goodTrade = true;
						ot.goodTrade = true;
					}
				}
			}
			if ((Boolean) c.getAttributes().get("isTrading") && !c.tradeConfirmed && ot.goodTrade && c.goodTrade) {
				c.tradeConfirmed = true;
				if (ot.tradeConfirmed) {
					c.getContentManager().getTrading().confirmScreen();
					ot.getContentManager().getTrading().confirmScreen();
					break;
				}

			}

			break;

		case 13218:
			c.tradeAccepted = true;
			Player ot1 = (Player) PlayerHandler.players[c.tradeWith];
			if (ot1 == null) {
				c.getContentManager().getTrading().declineTrade();
				c.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}

			if ((Boolean) c.getAttributes().get("isTrading") && c.tradeConfirmed && ot1.tradeConfirmed
					&& !c.tradeConfirmed2) {
				c.tradeConfirmed2 = true;
				if (ot1.tradeConfirmed2) {
					c.acceptedTrade = true;
					ot1.acceptedTrade = true;
					c.getContentManager().getTrading().giveItems();
					ot1.getContentManager().getTrading().giveItems();
					break;
				}
				ot1.getPA().sendFrame126("Other player has accepted.", 3535);
				c.getPA().sendFrame126("Waiting for other player...", 3535);
			}

			break;
		/* Rules Interface Buttons */
		case 125011: // Click agree
			if (!c.ruleAgreeButton) {
				c.ruleAgreeButton = true;
				c.getPA().sendFrame36(701, 1);
			} else {
				c.ruleAgreeButton = false;
				c.getPA().sendFrame36(701, 0);
			}
			break;
		case 125003:// Accept
			if (c.ruleAgreeButton) {
				c.getPA().showInterface(3559);
				c.newPlayer = false;
			} else if (!c.ruleAgreeButton) {
				c.sendMessage("You need to click on you agree before you can continue on.");
			}
			break;
		case 125006:// Decline
			c.sendMessage("You have chosen to decline, Player will be disconnected from the server.");
			break;
		/* End Rules Interface Buttons */
		/* Player Options */
		case 74176:
			if (!c.mouseButton) {
				c.mouseButton = true;
				c.getPA().sendFrame36(500, 1);
				c.getPA().sendFrame36(170, 1);
			} else if (c.mouseButton) {
				c.mouseButton = false;
				c.getPA().sendFrame36(500, 0);
				c.getPA().sendFrame36(170, 0);
			}
			break;
		case 74184:
			if (!c.splitChat) {
				c.splitChat = true;
				c.getPA().sendFrame36(502, 1);
				c.getPA().sendFrame36(287, 1);
			} else {
				c.splitChat = false;
				c.getPA().sendFrame36(502, 0);
				c.getPA().sendFrame36(287, 0);
			}
			break;
		case 74180:
			if (!c.chatEffects) {
				c.chatEffects = true;
				c.getPA().sendFrame36(501, 1);
				c.getPA().sendFrame36(171, 0);
			} else {
				c.chatEffects = false;
				c.getPA().sendFrame36(501, 0);
				c.getPA().sendFrame36(171, 1);
			}
			break;
		case 74188:
			if (!c.acceptAid) {
				c.acceptAid = true;
				c.getPA().sendFrame36(503, 1);
				c.getPA().sendFrame36(427, 1);
			} else {
				c.acceptAid = false;
				c.getPA().sendFrame36(503, 0);
				c.getPA().sendFrame36(427, 0);
			}
			break;
		case 74192:
			if (!c.isRunning2) {
				c.isRunning2 = true;
				c.getPA().sendFrame36(504, 1);
				c.getPA().sendFrame36(173, 1);
			} else {
				c.isRunning2 = false;
				c.getPA().sendFrame36(504, 0);
				c.getPA().sendFrame36(173, 0);
			}
			break;

		case 74206:// area1
			c.getPA().sendFrame36(509, 1);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74207:// area2
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 1);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74208:// area3
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 1);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74209:// area4
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 1);
			break;
			
			default:
				PluginManager.callFunc("clickButton_" + actionButtonId, c);
				if (c.getRights().greaterOrEqual(Rights.DEVELOPER))
					c.sendMessage("actionButtonId: " + actionButtonId);
				break;
		}
		
		if (c.isAutoButton(actionButtonId))
			c.assignAutocast(actionButtonId);
		
	}
}
