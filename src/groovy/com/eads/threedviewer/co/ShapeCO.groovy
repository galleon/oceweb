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
    ShapeType type

    static constraints = {
        name(nullable: false, blank: false)
        x(nullable: false)
        y(nullable: false)
        z(nullable: false)
    }

    abstract TopoDS_Shape getShape()

    CADObject getCADObject() {
        CADObject cadObject = new CADObject(name: name, project: project, x: x, y: y, z: z, content: content)
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
