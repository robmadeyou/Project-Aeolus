__author__ = '7Winds'
from core.game.plugin import PluginManager
from core.game.model.entity.player import Player

def objectClick1_1276(c, objectType, obX, obY):
    c.sendMessage("You clicked a tree")
	
def objectClick1_2213(c, objectType, obX, obY):
	c.getActionSender().openUpBank()