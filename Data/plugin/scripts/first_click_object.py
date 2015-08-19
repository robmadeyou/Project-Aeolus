__author__ = '7Winds'
from core.game.plugin import PluginManager
from core.game.model.entity.player import Player
from core import Configuration

def objectClick1_1276(c, objectType, obX, obY):
    c.sendMessage("You clicked a tree")
	
def objectClick1_2213(c, objectType, obX, obY):
	c.getActionSender().openUpBank()
	
def objectClick1_9398(c, objectType, obX, obY):
	c.getActionSender().sendString("The Bank of " + Configuration.SERVER_NAME + " - Deposit Box", 7421)
	c.getActionSender().sendFrame248(4465, 197)
	c.getInventory().resetItems(7423)