package com.eads.threedviewer.co

import org.jcae.opencascade.jni.BRepPrimAPI_MakeCylinder
import org.jcae.opencascade.jni.TopoDS_Shape
import org.jcae.opencascade.jni.BRepPrimAPI_MakeCone

import com.eads.threedviewer.CADConeObject
import com.eads.threedviewer.CADObject
import com.eads.threedviewer.enums.ShapeType
import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors', includeSuper = true)
class ConeCO extends ShapeCO {
    double baseRadius
    double height
    double angle
    double epsilon = 0.001

    static constraints = {
        baseRadius(nullable: false)
        height(nullable: false)
        angle(nullable: false)
    }


    @Override
    TopoDS_Shape getShape() {
        double[] axes = [0, 0, 0, 1, 0, 0]

        BRepPrimAPI_MakeCone cone = new BRepPrimAPI_MakeCone(axes, baseRadius, 0, height, 2 * Math.PI)
        return cone.shape()
    }

    CADObject getCADObject() {
        CADObject cadObject = new CADConeObject(name: name, project: project, x: x, y: y, z: z, basRadius: baseRadius, height: height, content: content, type: ShapeType.CONE)
        return cadObject
    }


}
