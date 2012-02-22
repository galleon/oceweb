package com.eads.threedviewer.co

import com.eads.threedviewer.CADObject
import com.eads.threedviewer.CADSphereObject
import com.eads.threedviewer.enums.ShapeType
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
        double[] axes = [x, y, z]

        BRepPrimAPI_MakeSphere sphere = new BRepPrimAPI_MakeSphere(axes, radius)
        return sphere.shape()
    }

    CADObject findOrCreateCADObject() {
        CADObject cadObject = id ? CADSphereObject.get(id) : new CADSphereObject()
        cadObject.name = name ?: cadObject.name
        cadObject.x = x ?: cadObject.x
        cadObject.y = y ?: cadObject.y
        cadObject.z = z ?: cadObject.z
        cadObject.radius = radius ?: cadObject.radius
        cadObject.type = type ?: cadObject.type
        cadObject.project = project ?: cadObject.project
        return cadObject
    }
}
