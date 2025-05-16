package mk.ukim.finki.wayzi.model.enumeration;

public enum VehicleType {
    AUTOMOBILE("fa-solid fa-car"),
    MOTORBIKE("fa-solid fa-motorcycle"),
    BUS("fa-solid fa-bus"),
    VAN("fa-solid fa-van-shuttle");

    private final String iconClass;

    VehicleType(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getIconClass() {
        return this.iconClass;
    }
}
