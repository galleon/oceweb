package com.eads.threedviewer.co

import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.BRepPrimAPI_MakeSphere
import org.jcae.opencascade.jni.TopoDS_Shape

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors', includeSuper = true)
class SphereCO extends ShapeCO {
    double radius

    static constraints = {
        radius(nullable: false)
    }

    @Override
    TopoDS_Shape getShape() {
        double[] axes = [0, 0, 0]

        BRepPrimAPI_MakeSphere sphere = new BRepPrimAPI_MakeSphere(axes, radius)
        return sphere.shape()
    }

}
