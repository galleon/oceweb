package com.eads.threedviewer.co

import com.eads.threedviewer.CADConeObject
import com.eads.threedviewer.CADObject
import grails.validation.Validateable
import groovy.transform.ToString
import org.jcae.opencascade.jni.BRepPrimAPI_MakeCone
import org.jcae.opencascade.jni.TopoDS_Shape

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors', includeSuper = true)
class ConeCO extends ShapeCO {
    double baseRadius
    double height

    static constraints = {
        baseRadius(nullable: false)
        height(nullable: false)
    }


    @Override
    TopoDS_Shape getShape() {
        double[] axes = [x, y, z, 0, 1, 0]
        BRepPrimAPI_MakeCone cone = new BRepPrimAPI_MakeCone(axes, baseRadius, 0, height, 2 * Math.PI)
        return cone.shape()
    }

    CADObject findOrCreateCADObject() {
        CADObject cadObject = id ? CADConeObject.get(id) : new CADConeObject()
        cadObject.name = name ?: cadObject.name
        cadObject.x = x ?: cadObject.x
        cadObject.y = y ?: cadObject.y
        cadObject.z = z ?: cadObject.z
        cadObject.baseRadius = baseRadius ?: cadObject.baseRadius
        cadObject.height = height ?: cadObject.height
        cadObject.type = type ?: cadObject.type
        cadObject.project = project ?: cadObject.project
        return cadObject
    }
}
