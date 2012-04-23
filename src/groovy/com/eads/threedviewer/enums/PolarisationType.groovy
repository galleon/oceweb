package com.eads.threedviewer.enums;
public enum PolarisationType {

    VERTICAL("Vertical"), HORIZONTAL("Horizontal"), VERTICAL_HORIZONTAL("Vertical & Horizontal")

    private final String name;

    PolarisationType(String name) {
        this.name = name
    }

    public String toString() {
        return name;
    }

    public String getKey() { return name() }

    public String getValue() { return name }

    public static List<PolarisationType> list() {
        return PolarisationType.values()
    }

}
