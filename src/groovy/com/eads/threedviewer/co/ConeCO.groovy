package com.eads.threedviewer.co

import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.BRepPrimAPI_MakeCylinder
import org.jcae.opencascade.jni.TopoDS_Shape
import org.jcae.opencascade.jni.BRepPrimAPI_MakeCone
import com.eads.threedviewer.CADObject

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors', includeSuper = true)
class ConeCO extends ShapeCO {
    double baseRadius
    double topRadius = 0
    double height
    double angle
    double epsilon = 0.001

    static constraints = {
        baseRadius(nullable: false)
        topRadius(nullable: false)
        height(nullable: false)
        angle(nullable: false)
    }


    @Override
    TopoDS_Shape getShape() {
        double[] axes = [0, 0, 0, 1, 0, 0]

        BRepPrimAPI_MakeCone cone = new BRepPrimAPI_MakeCone(axes, baseRadius, topRadius, height, 2 * Math.PI)
        return cone.shape()
    }

}
