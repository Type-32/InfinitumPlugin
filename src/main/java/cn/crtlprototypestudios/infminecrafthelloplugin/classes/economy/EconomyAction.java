package cn.crtlprototypestudios.infminecrafthelloplugin.classes.economy;

public enum EconomyAction {
    TransferOut("Transfer Out"),
    TransferIn("Transfer In"),
    Deposit("Deposit"),
    Withdraw("Withdraw");
    public final String label;
    private EconomyAction(String label){
        this.label = label;
    }
    public static EconomyAction fromLabel(String label) {
        for (EconomyAction type : EconomyAction.values()) {
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
