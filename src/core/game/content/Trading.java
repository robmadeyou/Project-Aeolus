package core.game.content;

import java.util.concurrent.CopyOnWriteArrayList;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.model.entity.player.Rights;
import core.game.model.item.GameItem;
import core.game.model.item.Item;
import core.game.util.Misc;

public class Trading {
	
	private Player c;
	
	public Trading(Player c) {
		this.c = c;
	}
	
	/**
	* Trading
	**/	
	public CopyOnWriteArrayList<GameItem> offeredItems = new CopyOnWriteArrayList<GameItem>();
	
	public void requestTrade(int id) {
		try {
			Player o = PlayerHandler.players[id];
			if (o == null)
				return;

			else if (c.isBusy())
				return;
			else if (o.isBusy()) {
				c.sendMessage("Other player is busy at the moment.");
				return;
			} else if (System.currentTimeMillis() - c.logoutDelay < 10000) {
				c.sendMessage("You cannot trade anyone while incombat!");
				return;
			} else if (System.currentTimeMillis() - o.logoutDelay < 10000) {
				c.sendMessage("Other player is busy at the moment.");
				return;
			} else if (id == c.playerId)
				return;
			c.turnPlayerTo(o.absX, o.absY);
			c.tradeWith = id;
			if (!c.isTrading && o.tradeRequested && o.tradeWith == c.playerId) {
				c.getTrade().openTrade();
				o.getTrade().openTrade();
			} else if (!c.isTrading) {
				c.tradeRequested = true;
				c.sendMessage("Sending trade request...");
				o.sendMessage(Misc.optimizeText(c.playerName) + ":tradereq:");
			}
		} catch (Exception e) {
			Misc.println("Error requesting trade.");
		}
	}
	
	public void openTrade() {
		Player o = PlayerHandler.players[c.tradeWith];
		
		if(o == null) {
			return;
		}
		c.isTrading = true;
		c.canOffer = true;
		c.tradeStatus = 1;
		c.tradeRequested = false;
		c.getInventory().resetItems(3322);
		resetTItems(3415);
		resetOTItems(3416);
		String out = o.playerName;
		
		if(o.getRights().equal(Rights.MODERATOR)) {
			out = "@cr1@" + out;
		} 
		else if(o.getRights().equal(Rights.MODERATOR)) {
			out = "@cr2@" + out;
		}
		c.getActionSender().textOnInterface("Trading with: " + o.playerName+" who has @gre@"+o.getInventory().freeSlots()+" free slots" ,3417);
		c.getActionSender().textOnInterface("", 3431);
		c.getActionSender().textOnInterface("Are you sure you want to make this trade?", 3535);
		c.getActionSender().sendFrame248(3323, 3321);
	}	
	
	
	
	public void resetTItems(int WriteFrame) {
        synchronized(c) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(WriteFrame);
			int len = offeredItems.toArray().length;
			int current = 0;
			c.getOutStream().writeWord(len);
				for (GameItem item : offeredItems) {
					if (item.amount > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord_v2(item.amount);
					} else {
						c.getOutStream().writeByte(item.amount);
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

	public boolean fromTrade(int itemID, int fromSlot, int amount) {
		Player o = (Player) PlayerHandler.players[c.tradeWith];
		if(o == null) {
			return false;
		}
		try {
			if (!c.isTrading || !c.canOffer) {
				declineTrade();
				return false;
			}
		c.tradeConfirmed = false;
		o.tradeConfirmed = false;
			if(!c.getInventory().getStackable(itemID)) {
				for(int a = 0; a < amount; a++) {
					for (GameItem item : offeredItems) {
						if(item.id == itemID) {	
							if(!item.stackable) {	
								offeredItems.remove(item);	
								c.getInventory().addItem(itemID, 1);	
								o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);		
							} else {
								if(item.amount > amount) {
									item.amount -= amount;
									c.getInventory().addItem(itemID, amount);
									o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);								
								} else {
									amount = item.amount;
									offeredItems.remove(item);
									c.getInventory().addItem(itemID, amount);
									o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);	
								}
							}
						break;
						}
					o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);	
					c.tradeConfirmed = false;
					o.tradeConfirmed = false;
					c.getInventory().resetItems(3322);
					resetTItems(3415);
					o.getTrade().resetOTItems(3416);
					c.getActionSender().textOnInterface("", 3431);
					o.getActionSender().textOnInterface("", 3431);
					}
				}	
			}
			for (GameItem item : offeredItems) {
				if(item.id == itemID) {
					if(!item.stackable) {
					} else  {
						if(item.amount > amount) {
							item.amount -= amount;
							c.getInventory().addItem(itemID, amount);
							o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);	
						} else  {
							amount = item.amount;
							offeredItems.remove(item);
							c.getInventory().addItem(itemID, amount);
							o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);	
						}
					}
					break;
				}
			}

		o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);	
		c.tradeConfirmed = false;
		o.tradeConfirmed = false;
		c.getInventory().resetItems(3322);
		resetTItems(3415);
		o.getTrade().resetOTItems(3416);
		c.getActionSender().textOnInterface("", 3431);
		o.getActionSender().textOnInterface("", 3431);
		} catch(Exception e){}
        return true;
    }
		
	public boolean tradeItem(int itemID, int fromSlot, int amount) {
		Player o = (Player) PlayerHandler.players[c.tradeWith];
		if(o == null) {
			return false;
		}
		
		for (int i : Configuration.ITEM_TRADEABLE) {
			if(i == itemID) {
				c.sendMessage("You can't trade this item.");
				return false;
			}		
		}
		c.tradeConfirmed = false;
		o.tradeConfirmed = false;
		if(!c.getInventory().getStackable(itemID) && !Item.itemIsNote[itemID]) {
			for(int a = 0; a < amount; a++) {
				if(c.getInventory().playerHasItem(itemID, 1)) {
					offeredItems.add(new GameItem(c, itemID, 1));	
					c.getInventory().deleteItem(itemID, c.getEquipment().getItemSlot(itemID), 1);
					o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);	
				}
			}
			o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);	
			c.getInventory().resetItems(3322);
			resetTItems(3415);
			o.getTrade().resetOTItems(3416);
			c.getActionSender().textOnInterface("", 3431);
			o.getActionSender().textOnInterface("", 3431);
		}
	
        if (!c.isTrading || !c.canOffer) {
			declineTrade();
			return false;
		}
		
		if(c.getInventory().getStackable(itemID) || Item.itemIsNote[itemID]) {
			boolean inTrade = false;
			for(GameItem item : offeredItems) {
				if(item.id == itemID) {
					inTrade = true;
					item.amount += amount;
					c.getInventory().deleteItem(itemID, c.getEquipment().getItemSlot(itemID), amount);
					o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);	
					break;	
				}
			}

			if(!inTrade) {
				offeredItems.add(new GameItem(c, itemID, amount));
				c.getInventory().deleteItem(itemID, fromSlot, amount);
				o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);	
			}
		}
		o.getActionSender().textOnInterface("Trading with: " + c.playerName+" who has @gre@"+c.getInventory().freeSlots()+" free slots" ,3417);	
		c.getInventory().resetItems(3322);
		resetTItems(3415);
		o.getTrade().resetOTItems(3416);
		c.getActionSender().textOnInterface("", 3431);
		o.getActionSender().textOnInterface("", 3431);
		return true;
		}
	
	
	public void resetTrade() {
		offeredItems.clear();
		c.isTrading = false;
		c.tradeWith = 0;
		c.canOffer = true;
		c.tradeConfirmed = false;
		c.tradeConfirmed2 = false;
		c.acceptedTrade = false;
		c.getActionSender().removeAllWindows();
		c.tradeResetNeeded = false;
		c.getActionSender().textOnInterface("Are you sure you want to make this trade?", 3535);
	}
	public void declineTrade() {
		c.tradeStatus = 0;
		declineTrade(true);
	}
	

	public void declineTrade(boolean tellOther) {
		c.getActionSender().removeAllWindows();
		Player o = (Player) PlayerHandler.players[c.tradeWith];
		if (o == null) {
			return;
		}
		
		if(tellOther){
			o.getTrade().declineTrade(false);
			o.getTrade().c.getActionSender().removeAllWindows();
		}
			
		for(GameItem item : offeredItems) {
			if(item.amount < 1) {
				continue;
			}
			if(item.stackable) {
				c.getInventory().addItem(item.id, item.amount);
			} else {
				for(int i = 0; i < item.amount; i++) {
					c.getInventory().addItem(item.id, 1);
				}
			}
		}
		c.canOffer = true;
		c.tradeConfirmed = false;
		c.tradeConfirmed2 = false;
		offeredItems.clear();
		c.isTrading = true;
		c.tradeWith = 0;
	}
	
		
	public void resetOTItems(int WriteFrame) {
		synchronized(c) {
			Player o = (Player) PlayerHandler.players[c.tradeWith];
			if(o == null) {
				return;
			}	
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(WriteFrame);
			int len = o.getTrade().offeredItems.toArray().length;
			int current = 0;
			c.getOutStream().writeWord(len);
				for (GameItem item : o.getTrade().offeredItems) {
					if (item.amount > 254) {
						c.getOutStream().writeByte(255); // item's stack count. if over 254, write byte 255
						c.getOutStream().writeDWord_v2(item.amount); 
					} else {
						c.getOutStream().writeByte(item.amount);
					}
					c.getOutStream().writeWordBigEndianA(item.id + 1); // item id
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
	
	
	public void confirmScreen() {
		Player o = (Player) PlayerHandler.players[c.tradeWith];
		if(o == null) {
			return;
		}
		c.canOffer = false;
		c.getInventory().resetItems(3214);
		String SendTrade = "Absolutely nothing!";
		String SendAmount = "";
		int Count = 0;
		for (GameItem item : offeredItems) {
		    if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
				}  else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}

					if(Count == 0) {	
						SendTrade = c.getEquipment().getItemName(item.id);		
					} else {
						SendTrade = SendTrade + "\\n" + c.getEquipment().getItemName(item.id);
					}
					
						if (item.stackable) {
							SendTrade = SendTrade + " x " + SendAmount;
						}
				Count++;
		    }
		}
		
		c.getActionSender().textOnInterface(SendTrade, 3557);
		SendTrade = "Absolutely nothing!";
		SendAmount = "";
		Count = 0;
		
		for (GameItem item : o.getTrade().offeredItems) {
		    if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
				}  else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}
				//SendAmount = SendAmount;
				
					if (Count == 0) {
						SendTrade = c.getEquipment().getItemName(item.id);		
					} else {
						SendTrade = SendTrade + "\\n" + c.getEquipment().getItemName(item.id);
					}
						if (item.stackable) {
						SendTrade = SendTrade + " x " + SendAmount;
						}
				Count++;
		    }
		}
		c.getActionSender().textOnInterface(SendTrade, 3558);
		//TODO: find out what 197 does eee 3213
		c.getActionSender().sendFrame248(3443, 197);
	}
	
	
	public void giveItems() {
		Player o = (Player) PlayerHandler.players[c.tradeWith];
		if(o == null) {
			return;
		}	
		try{	
			for(GameItem item : o.getTrade().offeredItems){
				if (item.id > 0) {
					c.getInventory().addItem(item.id, item.amount);
			   }
			}
			
			c.getActionSender().removeAllWindows();
			c.tradeResetNeeded = true;
			} catch(Exception e){
			}
		}

}
