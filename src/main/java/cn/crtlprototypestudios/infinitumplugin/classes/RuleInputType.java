package cn.crtlprototypestudios.infinitumplugin.classes;

public enum RuleInputType {
    Integer("Integer"),
    Float("Float"),
    Boolean("Boolean"),
    String("String");
    public final String label;
    private RuleInputType(String label){
        this.label = label;
    }
    public static RuleInputType fromLabel(String label) {
        for (RuleInputType type : RuleInputType.values()) {
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
