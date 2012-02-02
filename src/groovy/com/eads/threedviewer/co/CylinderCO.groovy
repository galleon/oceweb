package com.eads.threedviewer.co

import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.TopoDS_Shape
import org.jcae.opencascade.jni.BRepPrimAPI_MakeCylinder
import com.eads.threedviewer.CADObject

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors', includeSuper = true)
class CylinderCO extends ShapeCO {
    double radius
    double height

    static constraints = {
        radius(nullable: false)
        height(nullable: false)
    }

    @Override
    TopoDS_Shape getShape() {
        double[] axes = [0, 0, 0, 0, 0, 1]

        BRepPrimAPI_MakeCylinder cylinder = new BRepPrimAPI_MakeCylinder(axes, radius, height, 270)
        return cylinder.shape()
    }

}
