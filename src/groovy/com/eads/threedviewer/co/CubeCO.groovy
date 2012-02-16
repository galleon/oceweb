package com.eads.threedviewer.co

import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.BRepPrimAPI_MakeBox
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.CADObject
import com.eads.threedviewer.CADCubeObject
import com.eads.threedviewer.enums.ShapeType

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
        CADObject cadObject = new CADCubeObject(name: name, project: project, x: x, y: y, z: z, x1: x1, y1: y1, z1: z1, content: content, type: ShapeType.CUBE)
        return cadObject
    }

}