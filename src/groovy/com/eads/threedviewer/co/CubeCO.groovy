package com.eads.threedviewer.co

import com.eads.threedviewer.CADCubeObject
import com.eads.threedviewer.CADObject
import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.BRepPrimAPI_MakeBox
import org.jcae.opencascade.jni.TopoDS_Shape

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors', includeSuper = true)
class CubeCO extends ShapeCO {
    double x1, y1, z1

    static constraints = {
    }

    @Override
    TopoDS_Shape getShape() {
        double[] p1 = [x, y, z]
        double[] p2 = [x1, y1, z1]

        BRepPrimAPI_MakeBox box = new BRepPrimAPI_MakeBox(p1, p2)
        return box.shape()
    }

    CADObject getCADObject() {
        CADObject cadObject = id ? CADCubeObject.get(id) : new CADCubeObject()
        cadObject.name = name ?: cadObject.name
        cadObject.x = x ?: cadObject.x
        cadObject.y = y ?: cadObject.y
        cadObject.z = z ?: cadObject.z
        cadObject.x1 = x1 ?: cadObject.x1
        cadObject.y1 = y1 ?: cadObject.y1
        cadObject.z1 = z1 ?: cadObject.z1
        cadObject.type = type ?: cadObject.type
        cadObject.project = project ?: cadObject.project
        return cadObject
    }

}