package com.eads.threedviewer.co

import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.BRepPrimAPI_MakeSphere
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.CADObject
import com.eads.threedviewer.CADConeObject
import com.eads.threedviewer.enums.ShapeType

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

    CADObject getCADObject() {
        CADObject cadObject = new CADConeObject(name: name, project: project, x: x, y: y, z: z, radius: radius, content: content,
                type: ShapeType.SPHERE)
        return cadObject
    }

}
