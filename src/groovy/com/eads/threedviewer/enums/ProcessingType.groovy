package com.eads.threedviewer.enums;
public enum ProcessingType {

    COUCHA("Coucha"), PORCHE("Porche"), ANTEN("Anten")

    private final String name;

    ProcessingType(String name) {
        this.name = name
    }

    public String toString() {
        return name;
    }

    public String getKey() { return name() }

    public String getValue() { return name }

    public static List<ProcessingType> list() {
        return ProcessingType.values()
    }

}
