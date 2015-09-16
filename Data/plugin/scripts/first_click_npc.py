__author__ = '7Winds'
from core.game.plugin import PluginManager
from core.game.model.entity.player import Player
from core.game.model.shop import ShopAssistant

def npcClick1_0(c, npcId):
    print("It works!")

def npcClick1_6661(c, npcId):
    print( 'cowsaymoo' )

def npcClick1_519(c, npcId):
    assistant = ShopAssistant( c )
    assistant.openShop( 0 )