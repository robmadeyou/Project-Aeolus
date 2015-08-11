__author__ = 'Project_X'
from core.game.plugin import PluginManager
from core.game.model.entity.player import Player

def clickButton_33209(c):
    c.sendMessage("It worked")

def clickButton_58253(c):
    c.getPA().showInterface(15106)
    c.getItems().writeBonus()

def clickButton_59004(c):
    c.getPA().removeAllWindows()

def clickButton_70212(c):
    c.getActionSender().showInterface(15456)

def clickButton_9154(c):
    c.logout()

# Brightness 1
def clickButton_74201(c):
    c.getPA().sendFrame36(505, 1)
    c.getPA().sendFrame36(506, 0)
    c.getPA().sendFrame36(507, 0)
    c.getPA().sendFrame36(508, 0)
    c.getPA().sendFrame36(166, 1)

# Brightness 2
def clickButton_74203(c):
    c.getPA().sendFrame36(505, 0)
    c.getPA().sendFrame36(505, 0)
    c.getPA().sendFrame36(506, 1)
    c.getPA().sendFrame36(507, 0)
    c.getPA().sendFrame36(508, 0)
    c.getPA().sendFrame36(166, 2)

# Brightness 3
def clickButton_74204(c):
    c.getPA().sendFrame36(505, 0)
    c.getPA().sendFrame36(506, 0)
    c.getPA().sendFrame36(507, 1)
    c.getPA().sendFrame36(508, 0)
    c.getPA().sendFrame36(166, 3)

# Brightness 4
def clickButton_74205(c):
    c.getPA().sendFrame36(505, 0)
    c.getPA().sendFrame36(506, 0)
    c.getPA().sendFrame36(507, 0)
    c.getPA().sendFrame36(508, 1)
    c.getPA().sendFrame36(166, 4)

# Character Equipment Screen
def clickButton_108005(c):
    c.getActionSender().showInterface(15106)

# Items on Death Screen
def clickButton_108006(c):
    c.getActionSender().ItemsOnDeath(c)

def clickButton_59163(c):
    c.getActionSender().closeAllWindows()

# Swapping from Quest Tab
def clickButton_114119(c):
    c.setSidebarInterface(2, 29265) # Swap to Achievements

def clickButton_113228(c):
    c.setSidebarInterface(2, 29265) # Swap to Achievements

def clickButton_114118(c):
    c.setSidebarInterface(2, 638) # Swap to Quests

def clickButton_114083(c):
    c.setSidebarInterface(2, 638) # Swap to Quests

def clickButton_114087(c):
    c.setSidebarInterface(2, 29300) # Swap to Minigames

def clickButton_114086(c):
    c.setSidebarInterface(2, 29300) # Swap to Minigames