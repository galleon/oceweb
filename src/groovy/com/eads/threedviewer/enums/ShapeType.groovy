package com.eads.threedviewer.enums

public enum ShapeType {
    CONE("Cone"), CYLINDER("Cylinder"), CUBE("Cube"), SPHERE("Sphere"), FILE("File"), COMPOUND("Compound"), EXPLODE("Explode"), MESH("Mesh")

    private final String name

    ShapeType(String name) {
        this.name = name
    }

    String getKey() {
        return name()
    }

    String getValue() {
        return name
    }

    String toString() {
        return key
    }
}
