__author__ = 'Project_X'
from core.game.plugin import PluginManager
from core.game.model.entity.player import Player
from core.net.packets.outgoing import ActionSender

def objectClick2_2213(c, objectType, obX, obY):
    c.getPA().openUpBank()