package com.eads.threedviewer.co

import com.eads.threedviewer.CADCylinderObject
import com.eads.threedviewer.CADObject
import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.BRepPrimAPI_MakeCylinder
import org.jcae.opencascade.jni.TopoDS_Shape

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
        double[] axes = [x, y, z, 0, 0, 1]

        BRepPrimAPI_MakeCylinder cylinder = new BRepPrimAPI_MakeCylinder(axes, radius, height, 2 * Math.PI)
        return cylinder.shape()
    }

    CADObject findOrCreateCADObject() {
        CADObject cadObject = id ? CADCylinderObject.get(id) : new CADCylinderObject()
        cadObject.name = name ?: cadObject.name
        cadObject.x = x ?: cadObject.x
        cadObject.y = y ?: cadObject.y
        cadObject.z = z ?: cadObject.z
        cadObject.radius = radius ?: cadObject.radius
        cadObject.height = height ?: cadObject.height
        cadObject.type = type ?: cadObject.type
        cadObject.project = project ?: cadObject.project
        return cadObject
    }


}
