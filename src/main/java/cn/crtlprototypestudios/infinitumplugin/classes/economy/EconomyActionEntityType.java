package cn.crtlprototypestudios.infinitumplugin.classes.economy;

public enum EconomyActionEntityType {
    Player("Player"),
    This("This Player"),
    Vault("Vault"),
    Faction("Faction");
    public final String label;
    private EconomyActionEntityType(String label){
        this.label = label;
    }
    public static EconomyActionEntityType fromLabel(String label) {
        for (EconomyActionEntityType type : EconomyActionEntityType.values()) {
            if (type.label.equals(label)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String n = super.toString();
        n.toCharArray()[0] = Character.toUpperCase(n.toCharArray()[0]);
        return n;
    }
}
