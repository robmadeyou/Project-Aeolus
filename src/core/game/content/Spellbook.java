package core.game.content;

public enum Spellbook {
    MODERN(0, 1151), ANCIENT(1, 12855), LUNAR(2, 29999);
    
    private final int id;
    private final int sidebar;
    
    private Spellbook(int id, int sidebar) {
        this.id = id;
        this.sidebar = sidebar;
    }
    
    public int getIndex() {
        return id;
    }
    
    public int getInterfaceIndex() {
        return sidebar;
    }
}