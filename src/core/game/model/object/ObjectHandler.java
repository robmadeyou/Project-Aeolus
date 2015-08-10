package core.game.model.object;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import core.Configuration;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.Player;
import core.game.model.entity.player.PlayerHandler;
import core.game.util.JsonSaver;
import core.game.util.Misc;
import core.game.util.json.GlobalObjectLoader;

/**
 * @author Sanity
 * @author 7Winds
 */
@SuppressWarnings("all")
public class ObjectHandler {

	/**
	 * List datatype used to store all the global objects
	 */
	public static List<Objects> globalObjects = new ArrayList<Objects>();


	public ObjectHandler() {
		new GlobalObjectLoader().load();
	}

	/**
	 * Adds object to list
	 **/
	public void addObject(Objects object) {
		globalObjects.add(object);
	}

	/**
	 * Removes object from list
	 **/
	public void removeObject(Objects object) {
		globalObjects.remove(object);
	}

	/**
	 * Does object exist
	 **/
	public Objects objectExists(int objectX, int objectY, int objectHeight) {
		for (Objects o : globalObjects) {
			if (o.getX() == objectX && o.getY() == objectY && o.getHeight() == objectHeight) {
				return o;
			}
		}
		return null;
	}

	/**
	 * Update objects when entering a new region or logging in
	 **/
	public void updateObjects(Player c) {
		for (Objects o : globalObjects) {
			if (c != null) {
				if (c.heightLevel == o.getHeight() && o.objectTicks == 0) {
					if (c.distanceToPoint(o.getX(), o.getY()) <= 60) {
						c.getActionSender().object(o.getId(), o.getX(), o.getY(), o.getFace(), o.getType());
					}
				}
			}
		}
		if (c.distanceToPoint(2961, 3389) <= 60) {
			c.getActionSender().object(6552, 2961, 3389, -1, 10);
		}
	}

	/**
	 * Creates the object for anyone who is within 60 squares of the object
	 **/
	public void placeObject(Objects o) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (person != null) {
					if (person.heightLevel == o.getHeight() && o.objectTicks == 0) {
						if (person.distanceToPoint(o.getX(), o.getY()) <= 60) {
							removeAllObjects(o);
							globalObjects.add(o);
							person.getActionSender().object(o.getId(), o.getX(), o.getY(), o.getFace(), o.getType());
						}
					}
				}
			}
		}
	}

	/**
	 * Removes all global objects from the list.
	 */
	public void removeAllObjects(Objects o) {
		for (Objects s : globalObjects) {
			if (s.getX() == o.objectX && s.getY() == o.objectY && s.getHeight() == o.getHeight()) {
				globalObjects.remove(s);
				break;
			}
		}
	}

	/**
	 * The tick method for global objects, used for adding and removing global
	 * objects
	 */
	public void process() {
		for (int j = 0; j < globalObjects.size(); j++) {
			if (globalObjects.get(j) != null) {
				Objects o = globalObjects.get(j);
				if (o.objectTicks > 0) {
					o.objectTicks--;
				}
				if (o.objectTicks == 1) {
					Objects deleteObject = objectExists(o.getX(), o.getY(), o.getHeight());
					removeObject(deleteObject);
					o.objectTicks = 0;
					placeObject(o);
					removeObject(o);
				}
			}

		}
	}

	/**
	 * Serializes the global_objects.json file and stores the objects in the
	 * in the List @see globalObjects
	 */
	public void savesGlobalObjects(String filename) {
		Gson gson = new Gson();
		JsonSaver json = new JsonSaver();
		Objects object = new Objects(0, 0, 0, 0, 0, 0, 0);
			json.current().addProperty("objectId", object.getId());
			json.current().addProperty("objectX", object.getX());
			json.current().addProperty("objectY", object.getY());
			json.current().addProperty("objectHeight", object.getHeight());
			json.current().addProperty("objectFace", object.getFace());
			json.current().addProperty("objectType", object.getType());
			json.current().addProperty("objectTicks", 0);
			json.split();
			addObject(object);

		json.publish(filename);
		System.out.println("Created: " + globalObjects.size() + " Global Objects");
	}
}