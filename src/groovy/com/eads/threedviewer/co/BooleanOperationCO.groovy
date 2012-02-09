package com.eads.threedviewer.co

import com.eads.threedviewer.CADObject

class BooleanOperationCO {
    int object1
    int object2
    String name
    String operation

    CADObject getCADObject1() {
        CADObject.get(object1)
    }

    CADObject getCADObject2() {
        CADObject.get(object2)
    }
}
