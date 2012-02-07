package com.eads.threedviewer.co

import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.BRepPrimAPI_MakeBox
import org.jcae.opencascade.jni.TopoDS_Shape

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors', includeSuper = true)
class CubeCO extends ShapeCO {
    double length

    static constraints = {
        length(nullable: false)
    }

    @Override
    TopoDS_Shape getShape() {
        double[] p1 = [-length, -length, -length]
        double[] p2 = [length, length, length]

        BRepPrimAPI_MakeBox box = new BRepPrimAPI_MakeBox(p1, p2)
        return box.shape()
    }
}