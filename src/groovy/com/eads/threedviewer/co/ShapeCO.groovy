package com.eads.threedviewer.co

import com.eads.threedviewer.CADObject
import com.eads.threedviewer.Project
import com.eads.threedviewer.util.ShapeUtil
import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.BRepTools
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.enums.ShapeType

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors')
abstract class ShapeCO {
    Project project
    String name
    double x
    double y
    double z
    String type

    static constraints = {
        name(nullable: false, blank: false)
        x(nullable: false)
        y(nullable: false)
        z(nullable: false)
    }

    abstract TopoDS_Shape getShape()

    CADObject getCADObject() {
        CADObject cadObject = new CADObject(name: name, project: project, x: x, y: y, z: z, content: content)
        if (type == ShapeType.CONE.toString()) {
            cadObject.type = ShapeType.CONE
        }
        if (type == ShapeType.CYLINDER.toString()) {
            cadObject.type = ShapeType.CYLINDER
        }
        if (type == ShapeType.CUBE.toString()) {
            cadObject.type = ShapeType.CUBE
        }
        if (type == ShapeType.SPHERE.toString()) {
            cadObject.type = ShapeType.SPHERE
        }
        if (type == ShapeType.FILE.toString()) {
            cadObject.type = ShapeType.FILE
        }
        if (type == ShapeType.COMPOUND.toString()) {
            cadObject.type = ShapeType.COMPOUND
        }
        if (type == ShapeType.EXPLODE.toString()) {
            cadObject.type = ShapeType.EXPLODE
        }
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
