package com.eads.threedviewer.co

import org.jcae.opencascade.jni.BRepPrimAPI_MakeCylinder
import org.jcae.opencascade.jni.TopoDS_Shape
import org.jcae.opencascade.jni.BRepPrimAPI_MakeCone

import com.eads.threedviewer.CADConeObject
import com.eads.threedviewer.CADObject
import com.eads.threedviewer.enums.ShapeType
import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import com.eads.threedviewer.CADSphereObject
import com.eads.threedviewer.CADCylinderObject

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
        double[] axes = [x, y, z, 1, 0, 0]
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
