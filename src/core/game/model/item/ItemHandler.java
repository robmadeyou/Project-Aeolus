package core.game.model.item;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sun.istack.internal.logging.Logger;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;

/**
* Handles ground items
**/
public class ItemHandler {
	
	public static final Logger logger = Logger.getLogger(ItemHandler.class);

	public List<GroundItem> items = new ArrayList<GroundItem>();
	public static List<WeaponDelay> weaponDelay = new ArrayList<>();
	
	public static final int HIDE_TICKS = 100;
	
	public ItemHandler() {		

	}
	
	/**
     * Parse the item definitions.
     * 
     * @throws Exception
     *         if any errors occur while parsing this file.
     */
    public static void loadItemDefinitions() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
    	final Gson gson = new Gson();
		final long start = System.currentTimeMillis();
		logger.info("Loading item definitions...");
		try (final FileReader reader = new FileReader(Configuration.DATA_DIR + "json/item_definitions.json")) {
			ItemDefinition.setDefinitions(gson.fromJson(reader, ItemDefinition[].class));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to load item definitions!");
			System.exit(0);
		} finally {
			logger.info("Loaded " + ItemDefinition.getDefinitions().length + " item definitions in " + (System.currentTimeMillis() - start) + "ms.");
		}
    }
	
	/**
	* Adds item to list
	**/	
	public void addItem(GroundItem item) {
		items.add(item);
	}
	
	/**
	* Removes item from list
	**/	
	public void removeItem(GroundItem item) {
		items.remove(item);
	}
	
	/**
	* Item amount
	**/	
	public int itemAmount(int itemId, int itemX, int itemY) {
		for(GroundItem groundItem : items) {
			if(groundItem.getItemId() == itemId && groundItem.getItemX() == itemX && groundItem.getItemY() == itemY) {
				return groundItem.getItemAmount();
			}
		}
		return 0;
	}
	
	
	/**
	* Item exists
	**/	
	public boolean itemExists(int itemId, int itemX, int itemY) {
		for(GroundItem groundItem : items) {
			if(groundItem.getItemId() == itemId && groundItem.getItemX() == itemX && groundItem.getItemY() == itemY) {
				return true;
			}
		}
		return false;
	}
	
	/**
	* Reloads any items if you enter a new region
	**/
	public void reloadItems(Player p) {
		for(GroundItem i : items) {
			if(p != null){
				if (p.getItems().tradeable(i.getItemId()) || i.getName().equalsIgnoreCase(p.playerName)) {
					if (p.distanceToPoint(i.getItemX(), i.getItemY()) <= 60) {
						if(i.hideTicks > 0 && i.getName().equalsIgnoreCase(p.playerName)) {
							p.getItems().removeGroundItem(i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
							p.getItems().createGroundItem(i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
						}
						if(i.hideTicks == 0) {
							p.getItems().removeGroundItem(i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
							p.getItems().createGroundItem(i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
						}
					}
				}	
			}
		}
	}
	
	public void process() {
		ArrayList<GroundItem> toRemove = new ArrayList<GroundItem>();
		for (int index = 0; index < items.size(); index++) {			
			if (items.get(index) != null) {
				GroundItem groundItem = items.get(index);
				if(groundItem.hideTicks > 0) {
					groundItem.hideTicks--;
				}
				if(groundItem.hideTicks == 1) { // item can now be seen by others
					groundItem.hideTicks = 0;
					createGlobalItem(groundItem);
					groundItem.removeTicks = HIDE_TICKS;
				}
				if(groundItem.removeTicks > 0) {
					groundItem.removeTicks--;
				}
				if(groundItem.removeTicks == 1) {
					groundItem.removeTicks = 0;
					toRemove.add(groundItem);
					//removeGlobalItem(i, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
				}			
			}		
		}
		
		for (int index = 0; index < toRemove.size(); index++) {
			GroundItem i = toRemove.get(index);
			removeGlobalItem(i, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());	
		}
		/*for(GroundItem i : items) {
			if(i.hideTicks > 0) {
				i.hideTicks--;
			}
			if(i.hideTicks == 1) { // item can now be seen by others
				i.hideTicks = 0;
				createGlobalItem(i);
				i.removeTicks = HIDE_TICKS;
			}
			if(i.removeTicks > 0) {
				i.removeTicks--;
			}
			if(i.removeTicks == 1) {
				i.removeTicks = 0;
				removeGlobalItem(i, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
			}
		}*/
	}	
	
	/**
	* Creates the ground item 
	**/
	public int[][] brokenBarrows = {{4708,4860},{4710,4866},{4712,4872},{4714,4878},{4716,4884},
	{4720,4896},{4718,4890},{4720,4896},{4722,4902},{4732,4932},{4734,4938},{4736,4944},{4738,4950},
	{4724,4908},{4726,4914},{4728,4920},{4730,4926},{4745,4956},{4747,4926},{4749,4968},{4751,4994},
	{4753,4980},{4755,4986},{4757,4992},{4759,4998}};
	public void createGroundItem(Player c, int itemId, int itemX, int itemY, int itemAmount, int playerId) {
		if(itemId > 0) {
			if (itemId >= 2412 && itemId <= 2414) {
				c.sendMessage("The cape vanishes as it touches the ground.");
				return;
			}
			if (itemId > 4705 && itemId < 4760) {
				for (int index = 0; index < brokenBarrows.length; index++) {
					if (brokenBarrows[index][0] == itemId) {
						itemId = brokenBarrows[index][1];
						break;
					}
				}
			}
			if (!c.getInventory().getStackable(itemId) && itemAmount > 0) {
				for (int index = 0; index < itemAmount; index++) {
					c.getItems().createGroundItem(itemId, itemX, itemY, 1);
					GroundItem item = new GroundItem(itemId, itemX, itemY, 1, c.playerId, HIDE_TICKS, PlayerHandler.players[playerId].playerName);
					addItem(item);
				}	
			} else {
				c.getItems().createGroundItem(itemId, itemX, itemY, itemAmount);
				GroundItem item = new GroundItem(itemId, itemX, itemY, itemAmount, c.playerId, HIDE_TICKS, PlayerHandler.players[playerId].playerName);
				addItem(item);
			}
		}
	}
	
	
	/**
	* Shows items for everyone who is within 60 squares
	**/
	public void createGlobalItem(GroundItem groundItem) {
		for (Player p : PlayerHandler.players){
			if(p != null) {
			Player person = (Player)p;
				if(person != null){
					if(person.playerId != groundItem.getItemController()) {
						if (!person.getItems().tradeable(groundItem.getItemId()) && person.playerId != groundItem.getItemController())
							continue;
						if (person.distanceToPoint(groundItem.getItemX(), groundItem.getItemY()) <= 60) {
							person.getItems().createGroundItem(groundItem.getItemId(), groundItem.getItemX(), groundItem.getItemY(), groundItem.getItemAmount());
						}
					}
				}
			}
		}
	}
			

			
	/**
	* Removing the ground item
	**/
	
	public void removeGroundItem(Player p, int itemId, int itemX, int itemY, boolean add){
		for(GroundItem groundItem : items) {
			if(groundItem.getItemId() == itemId && groundItem.getItemX() == itemX && groundItem.getItemY() == itemY) {
				if(groundItem.hideTicks > 0 && groundItem.getName().equalsIgnoreCase(p.playerName)) {
					if(add) {
						if (!p.getItems().specialCase(itemId)) {
							if(p.getInventory().addItem(groundItem.getItemId(), groundItem.getItemAmount())) {   
								removeControllersItem(groundItem, p, groundItem.getItemId(), groundItem.getItemX(), groundItem.getItemY(), groundItem.getItemAmount());
								break;
							}
						} else {
							//c.getItems().handleSpecialPickup(itemId);
							removeControllersItem(groundItem, p, groundItem.getItemId(), groundItem.getItemX(), groundItem.getItemY(), groundItem.getItemAmount());
							break;
						}
					} else {
						removeControllersItem(groundItem, p, groundItem.getItemId(), groundItem.getItemX(), groundItem.getItemY(), groundItem.getItemAmount());
						break;
					}
				} else if (groundItem.hideTicks <= 0) {
					if(add) {
						if(p.getInventory().addItem(groundItem.getItemId(), groundItem.getItemAmount())) {  
							removeGlobalItem(groundItem, groundItem.getItemId(), groundItem.getItemX(), groundItem.getItemY(), groundItem.getItemAmount());
							break;
						}
					} else {
						removeGlobalItem(groundItem, groundItem.getItemId(), groundItem.getItemX(), groundItem.getItemY(), groundItem.getItemAmount());
						break;
					}
				}
			}
		}
	}
	
	/**
	* Remove item for just the item controller (item not global yet)
	**/
	
	public void removeControllersItem(GroundItem groundItem, Player p, int itemId, int itemX, int itemY, int itemAmount) {
		p.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount);
		removeItem(groundItem);
	}
	
	/**
	* Remove item for everyone within 60 squares
	**/
	
	public void removeGlobalItem(GroundItem groundItem, int itemId, int itemX, int itemY, int itemAmount) {
		for (Player p : PlayerHandler.players){
			if(p != null) {
			Player person = (Player)p;
				if(person != null){
					if (person.distanceToPoint(itemX, itemY) <= 60) {
						person.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount);
					}
				}
			}
		}
		removeItem(groundItem);
	}	
}
