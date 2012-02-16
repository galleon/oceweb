package com.eads.threedviewer.co

import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.BRepPrimAPI_MakeCylinder
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.CADObject
import com.eads.threedviewer.CADCubeObject
import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.CADCylinderObject

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

        BRepPrimAPI_MakeCylinder cylinder = new BRepPrimAPI_MakeCylinder(axes, radius, height, 2 * Math.PI)
        return cylinder.shape()
    }

    CADObject getCADObject() {
        CADObject cadObject = new CADCylinderObject(name: name, project: project, x: x, y: y, z: z, radius: radius, height: height, content: content, type: ShapeType.CYLINDER)
        return cadObject
    }


}
