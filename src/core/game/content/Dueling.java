package core.game.content;

import java.util.concurrent.CopyOnWriteArrayList;

import core.Config;
import core.Server;
import core.game.GameConstants;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.item.GameItem;
import core.game.model.item.Item;
import core.game.util.Misc;

public class Dueling {
	
	private Player c;
	
	public Dueling(Player c) {
		this.c = c;
	}
	
	/**
	* Dueling
	**/
	
	public CopyOnWriteArrayList<GameItem> otherStakedItems = new CopyOnWriteArrayList<GameItem>();
	public CopyOnWriteArrayList<GameItem> stakedItems = new CopyOnWriteArrayList<GameItem>();
	
	public void requestDuel(int id) {
		try {
			if (id == c.playerId)
				return;
			resetDuel();
			resetDuelItems();
			c.duelingWith = id;
			Player o = (Player) PlayerHandler.players[id];
			if(o == null) {
				return;
			}
			c.duelRequested = true;
			if(c.duelStatus == 0 && o.duelStatus == 0 && c.duelRequested && o.duelRequested && c.duelingWith == o.getId() && o.duelingWith == c.getId()) {
				if(c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), 1)) {			
					c.getContentManager().getDueling().openDuel();
					o.getContentManager().getDueling().openDuel();
				} else {
					c.sendMessage("You need to get closer to your opponent to start the duel.");
				}

			} else {
				c.sendMessage("Sending duel request...");
				o.sendMessage(c.playerName+":duelreq:");		
			}
		} catch (Exception e) {
			Misc.println("Error requesting duel.");
		}
	}
	
	public void openDuel() {
		Player o = (Player) PlayerHandler.players[c.duelingWith];
		if(o == null) {
			return;
		}	
		c.duelStatus = 1;
		refreshduelRules();
		refreshDuelScreen();
		c.canOffer = true;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			sendDuelEquipment(c.playerEquipment[i], c.playerEquipmentN[i], i);
		}
		c.getPA().sendFrame126("Dueling with: " + o.playerName + " (level-" + o.combatLevel + ")", 6671);
		c.getPA().sendFrame126("", 6684);
		c.getPA().sendFrame248(6575, 3321);
		c.getInventory().resetItems(3322);
	}
	
	public void sendDuelEquipment(int itemId, int amount, int slot) {
		synchronized(c) {
			if(itemId != 0) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(13824);
				c.getOutStream().writeByte(slot);
				c.getOutStream().writeWord(itemId+1);

				if (amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord(amount);
				} else {
					c.getOutStream().writeByte(amount);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
		}
	}
	
	public void refreshduelRules() {
		for(int i = 0; i < c.duelRule.length; i++) {	
			c.duelRule[i] = false;
		}
		c.getPA().sendFrame87(286, 0);
		c.duelOption = 0;
	}
	
	public void refreshDuelScreen() {
		synchronized(c) {
			Player o = (Player) PlayerHandler.players[c.duelingWith];
			if(o == null) {
				return;
			}
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6669);
			c.getOutStream().writeWord(stakedItems.toArray().length);
			int current = 0;
			for(GameItem item : stakedItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255); 
					c.getOutStream().writeDWord_v2(item.amount);	
				} else  {
					c.getOutStream().writeByte(item.amount);
				}
				if (item.id > GameConstants.ITEM_LIMIT || item.id < 0) {
					item.id = GameConstants.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(item.id + 1);
				
				current++;
			}
			
			if(current < 27) {
				for(int i = current; i < 28; i++) {
					c.getOutStream().writeByte(1);
					c.getOutStream().writeWordBigEndianA(-1);
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
			
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6670);
			c.getOutStream().writeWord(o.getContentManager().getDueling().stakedItems.toArray().length);
			current = 0;	
			for (GameItem item : o.getContentManager().getDueling().stakedItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(item.amount);
				}  else  {
					c.getOutStream().writeByte(item.amount);
				}
				if (item.id > GameConstants.ITEM_LIMIT || item.id < 0) {
					item.id = GameConstants.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(item.id + 1);
				current++;
			}
			
			if(current < 27) {
				for(int i = current; i < 28; i++) {
					c.getOutStream().writeByte(1);
					c.getOutStream().writeWordBigEndianA(-1);
				}
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}
	
	
	public boolean stakeItem(int itemID, int fromSlot, int amount) {
		
		for (int i : Config.ITEM_TRADEABLE) {
			if(i == itemID) {
				c.sendMessage("You can't stake this item.");
				return false;
			}		
		}
		if (amount <= 0)
			return false;
		Player o = (Player) PlayerHandler.players[c.duelingWith];
		if (o == null ) {
			declineDuel();
			return false;
		}
		if (o.duelStatus <= 0 || c.duelStatus <= 0) {
			declineDuel();
			o.getContentManager().getDueling().declineDuel();
			return false;
		}
		if (!c.canOffer) {
			return false;
		}
		changeDuelStuff();
		if(!Item.itemStackable[itemID]) {
			for(int a = 0; a < amount; a++) {
				if(c.getInventory().playerHasItem(itemID, 1)) {
					stakedItems.add(new GameItem(itemID, 1));	
					c.getInventory().deleteItem(itemID, c.getEquipment().getItemSlot(itemID), 1);
				}
			}		
			c.getInventory().resetItems(3214);
			c.getInventory().resetItems(3322);
			o.getInventory().resetItems(3214);
			o.getInventory().resetItems(3322);
			refreshDuelScreen();
			o.getContentManager().getDueling().refreshDuelScreen();
			c.getPA().sendFrame126("", 6684);
			o.getPA().sendFrame126("", 6684);
		}
		
		if(!c.getInventory().playerHasItem(itemID, amount)) {
			return false;
		}
		if (Item.itemStackable[itemID] || Item.itemIsNote[itemID]) {
			boolean found = false;
			for (GameItem item : stakedItems) {
				if (item.id == itemID) {
					found = true;
					item.amount += amount;
					c.getInventory().deleteItem(itemID, fromSlot, amount);
					break;
				}
			}
			if (!found) {
				c.getInventory().deleteItem(itemID, fromSlot, amount);
				stakedItems.add(new GameItem(itemID, amount));
			}
		}
		
		c.getInventory().resetItems(3214);
		c.getInventory().resetItems(3322);
		o.getInventory().resetItems(3214);
		o.getInventory().resetItems(3322);
		refreshDuelScreen();
		o.getContentManager().getDueling().refreshDuelScreen();
		c.getPA().sendFrame126("", 6684);
		o.getPA().sendFrame126("", 6684);
		return true;
	}
	
	
	public boolean fromDuel(int itemID, int fromSlot, int amount)  {
		Player o = (Player) PlayerHandler.players[c.duelingWith];
		if (o == null ) {
			declineDuel();
			return false;
		}
		if (o.duelStatus <= 0 || c.duelStatus <= 0) {
			declineDuel();
			o.getContentManager().getDueling().declineDuel();
			return false;
		}
		if(Item.itemStackable[itemID]) {
			if(c.getInventory().freeSlots() - 1 < (c.duelSpaceReq)) {
				c.sendMessage("You have too many rules set to remove that item.");
				return false;
			}
		}

		if(!c.canOffer){
			return false;
		}
		
		changeDuelStuff();
		boolean goodSpace = true;
		if(!Item.itemStackable[itemID]) {
			for(int a = 0; a < amount; a++) {
				for (GameItem item : stakedItems) {
					if(item.id == itemID) {
						if(!item.stackable) {
							if(c.getInventory().freeSlots() - 1 < (c.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							stakedItems.remove(item);	
							c.getInventory().addItem(itemID, 1);				
						}  else  {
							if(c.getInventory().freeSlots() - 1 < (c.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							if(item.amount > amount) {
								item.amount -= amount;
								c.getInventory().addItem(itemID, amount);						
							} else  {
								if(c.getInventory().freeSlots() - 1 < (c.duelSpaceReq)) {
									goodSpace = false;
									break;
								}
								amount = item.amount;
								stakedItems.remove(item);
								c.getInventory().addItem(itemID, amount);
							}
						}
						break;
					}
					o.duelStatus = 1;
					c.duelStatus = 1;					
					c.getInventory().resetItems(3214);
					c.getInventory().resetItems(3322);
					o.getInventory().resetItems(3214);
					o.getInventory().resetItems(3322);
					c.getContentManager().getDueling().refreshDuelScreen();
					o.getContentManager().getDueling().refreshDuelScreen();
					o.getPA().sendFrame126("", 6684);
				}
			}		
		}
		
		for (GameItem item : stakedItems) {
			if(item.id == itemID) {
				if(!item.stackable) {
				} else {
					if(item.amount > amount) {
						item.amount -= amount;
						c.getInventory().addItem(itemID, amount);
					} else {
						amount = item.amount;
						stakedItems.remove(item);
						c.getInventory().addItem(itemID, amount);
					}
				}
				break;
			}
		}
		o.duelStatus = 1;
		c.duelStatus = 1;					
		c.getInventory().resetItems(3214);
		c.getInventory().resetItems(3322);
		o.getInventory().resetItems(3214);
		o.getInventory().resetItems(3322);
		c.getContentManager().getDueling().refreshDuelScreen();
		o.getContentManager().getDueling().refreshDuelScreen();
		o.getPA().sendFrame126("", 6684);
		if(!goodSpace) {
			c.sendMessage("You have too many rules set to remove that item.");
			return true;
		}
        return true;
    }
	
	public void confirmDuel() {
		Player o = (Player) PlayerHandler.players[c.duelingWith];
		if(o == null) {
			declineDuel();
			return;
		}
		String itemId = "";
		for(GameItem item : stakedItems) {
			if(Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				itemId += c.getEquipment().getItemName(item.id) + " x " + Misc.format(item.amount) +"\\n";
			}  else  {
				itemId += c.getEquipment().getItemName(item.id) + "\\n";
			}
		}
		c.getPA().sendFrame126(itemId, 6516);
		itemId = "";
		for(GameItem item : o.getContentManager().getDueling().stakedItems) {
			if(Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				itemId += c.getEquipment().getItemName(item.id) + " x " + Misc.format(item.amount) +"\\n";
			} else {
				itemId += c.getEquipment().getItemName(item.id) +"\\n";
			}
		}
		c.getPA().sendFrame126(itemId, 6517);
		c.getPA().sendFrame126("", 8242);
		for(int i = 8238; i <= 8253; i++) {
			c.getPA().sendFrame126("", i);
		}
		c.getPA().sendFrame126("Hitpoints will be restored.", 8250);
		c.getPA().sendFrame126("Boosted stats will be restored.", 8238);
		if(c.duelRule[8]) {
			c.getPA().sendFrame126("There will be obstacles in the arena.", 8239);
		} 
		c.getPA().sendFrame126("", 8240);
		c.getPA().sendFrame126("", 8241);
		
		String[] rulesOption = {"Players cannot forfeit!", "Players cannot move.", "Players cannot use range.", "Players cannot use melee.", "Players cannot use magic.",  "Players cannot drink pots.",  "Players cannot eat food.", "Players cannot use prayer."};
		
		int lineNumber = 8242;
		for(int i = 0; i < 8; i++) {
			if(c.duelRule[i]) {
				c.getPA().sendFrame126(""+rulesOption[i], lineNumber);
				lineNumber ++;
			}
		}
		c.getPA().sendFrame126("", 6571);
		c.getPA().sendFrame248(6412, 197);
		//c.getPA().showInterface(6412);
	}
	
	
	public void startDuel() {
		Player o = (Player) PlayerHandler.players[c.duelingWith];
		if(o == null) {
			duelVictory();
		}
		c.headIconHints = 2;
		
		if(c.duelRule[7]){
			for(int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows 
				c.prayerActive[p] = false;
				c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);		
			}
			c.headIcon = -1;
			c.getPA().requestUpdates();
		}		
		if(c.duelRule[11]) {
			c.getEquipment().removeItem(c.playerEquipment[0], 0);
		}
		if(c.duelRule[12]) {
			c.getEquipment().removeItem(c.playerEquipment[1], 1);
		}
		if(c.duelRule[13]) {
			c.getEquipment().removeItem(c.playerEquipment[2], 2);
		}
		if(c.duelRule[14]) {
			c.getEquipment().removeItem(c.playerEquipment[3], 3);
		}
		if(c.duelRule[15]) {
			c.getEquipment().removeItem(c.playerEquipment[4], 4);
		}
		if(c.duelRule[16]) {
			c.getEquipment().removeItem(c.playerEquipment[5], 5);
		}
		if(c.duelRule[17]) {
			c.getEquipment().removeItem(c.playerEquipment[7], 7);
		}
		if(c.duelRule[18]) {
			c.getEquipment().removeItem(c.playerEquipment[9], 9);
		}
		if(c.duelRule[19]) {
			c.getEquipment().removeItem(c.playerEquipment[10], 10);
		}
		if(c.duelRule[20]) {
			c.getEquipment().removeItem(c.playerEquipment[12], 12);
		}
		if(c.duelRule[21]) {
			c.getEquipment().removeItem(c.playerEquipment[13], 13);
		}		
		c.duelStatus = 5;
		c.getPA().removeAllWindows();
		c.specAmount = 10;
		c.getEquipment().addSpecialBar(c.playerEquipment[c.playerWeapon]);
		
		if(c.duelRule[8]){	
			if(c.duelRule[1]) {
				c.getPA().movePlayer(c.duelTeleX, c.duelTeleY, 0);
			} else {
				c.getPA().movePlayer(3366 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
		} else {
			if(c.duelRule[1]) {
				c.getPA().movePlayer(c.duelTeleX, c.duelTeleY, 0);
			} else {	
				c.getPA().movePlayer(3335 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
		}

		c.getPA().createPlayerHints(10, o.playerId);
		c.getPA().showOption(3, 0, "Attack", 1);
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		for(GameItem item : o.getContentManager().getDueling().stakedItems) {
			otherStakedItems.add(new GameItem(item.id, item.amount));
		}
		c.getPA().requestUpdates();			
	}

	
	public void duelVictory() {
		Player o = (Player) PlayerHandler.players[c.duelingWith];
		if(o != null) {
			c.getPA().sendFrame126(""+o.combatLevel, 6839);
			c.getPA().sendFrame126(o.playerName, 6840);
			o.duelStatus = 0;
		} else {
			c.getPA().sendFrame126("", 6839);
			c.getPA().sendFrame126("", 6840);
		}
		c.duelStatus = 6;
		c.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = c.getPA().getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		c.getPA().refreshSkill(3);
		duelRewardInterface();
		c.getPA().showInterface(6733);
		c.getPA().movePlayer(GameConstants.DUELING_RESPAWN_X+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), GameConstants.DUELING_RESPAWN_Y+(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0);	
		c.getPA().requestUpdates();
		c.getPA().showOption(3, 0, "Challenge", 3);
		c.getPA().createPlayerHints(10, -1);
		c.canOffer = true;
		c.duelSpaceReq = 0;
		c.duelingWith = 0;
		c.getCombat().resetPlayerAttack();
		c.duelRequested = false;
	}	
    
	
	public void duelRewardInterface() {
		synchronized(c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(6822);
			c.getOutStream().writeWord(otherStakedItems.toArray().length);
			for (GameItem item : otherStakedItems) {
				if (item.amount > 254) {
					c.getOutStream().writeByte(255);					
					c.getOutStream().writeDWord_v2(item.amount);
				} else {
					c.getOutStream().writeByte(item.amount);
				}
				if (item.id > GameConstants.ITEM_LIMIT || item.id < 0) {
					item.id = GameConstants.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(item.id + 1);
			}
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void claimStakedItems() {
		for(GameItem item : otherStakedItems) {
			if(item.id > 0 && item.amount > 0) {
				if(Item.itemStackable[item.id]) {
					if(!c.getInventory().addItem(item.id, item.amount)) {
						Server.itemHandler.createGroundItem(c, item.id, c.getX(), c.getY(), item.amount, c.getId());
					}
				} else {
					int amount = item.amount;
					for(int a = 1; a <= amount; a++) {
						if(!c.getInventory().addItem(item.id, 1)) {
							Server.itemHandler.createGroundItem(c, item.id, c.getX(), c.getY(), 1, c.getId());
						}
					}
				}
			}	
		}
		for(GameItem item : stakedItems) {
			if(item.id > 0 && item.amount > 0) {
				if(Item.itemStackable[item.id]) {
					if(!c.getInventory().addItem(item.id, item.amount)) {
						Server.itemHandler.createGroundItem(c, item.id, c.getX(), c.getY(), item.amount, c.getId());
					}
				} else {
					int amount = item.amount;
					for(int a = 1; a <= amount; a++) {
						if(!c.getInventory().addItem(item.id, 1)) {
							Server.itemHandler.createGroundItem(c, item.id, c.getX(), c.getY(), 1, c.getId());
						}
					}
				}	
			}
		}	
		resetDuel();
		resetDuelItems();
		c.duelStatus = 0;
	}
	
	public void declineDuel() {
		c.getPA().removeAllWindows();
		c.canOffer = true;
		c.duelStatus = 0;
		c.duelingWith = 0;
		c.duelSpaceReq = 0;
		c.duelRequested = false;
		for(GameItem item : stakedItems) {
			if(item.amount < 1) continue;
			if(Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				c.getInventory().addItem(item.id, item.amount);
			} else  {
				c.getInventory().addItem(item.id, 1);
			}
		}
		stakedItems.clear();
		for (int i = 0; i < c.duelRule.length; i++) { 
			c.duelRule[i] = false;
		}
	}

	public void resetDuel() {
		c.getPA().showOption(3, 0, "Challenge", 3);
		c.headIconHints = 0;
		for (int i = 0; i < c.duelRule.length; i++) { 
			c.duelRule[i] = false;
		}
		c.getPA().createPlayerHints(10, -1);
		c.duelStatus = 0;
		c.canOffer = true;
		c.duelSpaceReq = 0;
		c.duelingWith = 0;
		c.getPA().requestUpdates();
		c.getCombat().resetPlayerAttack();
		c.duelRequested = false;
	}
	
	public void resetDuelItems() {
		stakedItems.clear();
		otherStakedItems.clear();
	}
	
	public void changeDuelStuff() {
		Player o = (Player) PlayerHandler.players[c.duelingWith];
		if(o == null) {
			return;
		}
		o.duelStatus = 1;
		c.duelStatus = 1;
		o.getPA().sendFrame126("", 6684);
		c.getPA().sendFrame126("", 6684);
	}
	
	
	public void selectRule(int i) { // rules
		Player o = (Player) PlayerHandler.players[c.duelingWith];
		if(o == null) {
			return;
		}
		if (!c.canOffer)
			return;
		changeDuelStuff();
		o.duelSlot = c.duelSlot;
		if(i >= 11 && c.duelSlot > -1) {
			if(c.playerEquipment[c.duelSlot] > 0) {
				if(!c.duelRule[i]) {
					c.duelSpaceReq++;	
				} else {
					c.duelSpaceReq--;
				}
			}	
			if(o.playerEquipment[o.duelSlot] > 0) {
				if(!o.duelRule[i]) {
					o.duelSpaceReq++;
				} else {
					o.duelSpaceReq--;
				}
			}
		}

		if(i >= 11) {
			if(c.getInventory().freeSlots() < (c.duelSpaceReq ) || o.getInventory().freeSlots() < (o.duelSpaceReq)) {
				c.sendMessage("You or your opponent don't have the required space to set this rule.");
				if(c.playerEquipment[c.duelSlot] > 0) {
					c.duelSpaceReq--;
				}
				if(o.playerEquipment[o.duelSlot] > 0) {
					o.duelSpaceReq--;
				}
				return;
			}
		}
		
		if(!c.duelRule[i]) {
			c.duelRule[i] = true;
			c.duelOption += c.DUEL_RULE_ID[i];
		} else {	
			c.duelRule[i] = false;
			c.duelOption -= c.DUEL_RULE_ID[i];
		}

		c.getPA().sendFrame87(286, c.duelOption);
		o.duelOption = c.duelOption;
		o.duelRule[i] = c.duelRule[i];
		o.getPA().sendFrame87(286, o.duelOption);
		
		if(c.duelRule[8]){	
			if(c.duelRule[1]) {
				c.duelTeleX = 3366 + Misc.random(12);
				o.duelTeleX = c.duelTeleX-1;
				c.duelTeleY = 3246 + Misc.random(6);
				o.duelTeleY = c.duelTeleY;
			}
		} else {
			if(c.duelRule[1]) {
				c.duelTeleX = 3335 + Misc.random(12);
				o.duelTeleX = c.duelTeleX-1;
				c.duelTeleY = 3246 + Misc.random(6);
				o.duelTeleY = c.duelTeleY;
			}
		}

	}
}
