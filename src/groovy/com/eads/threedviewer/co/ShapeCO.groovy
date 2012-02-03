package com.eads.threedviewer.co

import org.codehaus.groovy.grails.validation.Validateable
import groovy.transform.ToString
import org.jcae.opencascade.jni.TopoDS_Shape
import occmeshextractor.OCCMeshExtractor
import com.eads.threedviewer.CADObject
import org.jcae.opencascade.jni.BRepTools
import com.eads.threedviewer.Project
import com.eads.threedviewer.util.ShapeUtil

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors')
abstract class ShapeCO {
    Project project
    String name
    double x
    double y
    double z

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
}
