package com.eads.threedviewer.co

import org.jcae.opencascade.jni.BRepTools
import org.jcae.opencascade.jni.TopoDS_Shape

import com.eads.threedviewer.CADObject
import com.eads.threedviewer.Project
import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.util.ShapeUtil
import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors')
abstract class ShapeCO {
    Long id
    Project project
    String name
    double x
    double y
    double z
    ShapeType type

    static constraints = {
        name(nullable: false, blank: false)
        x(nullable: false)
        y(nullable: false)
        z(nullable: false)
    }

    abstract TopoDS_Shape getShape()

    CADObject getCADObject() {
        CADObject cadObject = id ? CADObject.get(id) : new CADObject()
        cadObject.name = name ?: cadObject.name
        cadObject.x = x ?: cadObject.x
        cadObject.y = y ?: cadObject.y
        cadObject.z = z ?: cadObject.z
        cadObject.project = project ?: cadObject.project
        cadObject.type = type ?: cadObject.type
        return cadObject
    }

    byte[] getContent() {
        byte[] bytes
        BRepTools.write(shape, "temp.brep")
        File file = new File("temp.brep")
        if (file.exists()) {
            bytes = file.bytes
            file.delete()
        }
        return bytes
    }

    Map getData() {
        return ShapeUtil.getData(shape)
    }

    Map getMeshData() {
        return ShapeUtil.getMeshEdges()
    }
}
