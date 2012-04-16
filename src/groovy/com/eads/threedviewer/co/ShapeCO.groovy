package com.eads.threedviewer.co

import com.eads.threedviewer.CADObject
import com.eads.threedviewer.Project
import com.eads.threedviewer.enums.ShapeType
import com.eads.threedviewer.util.ShapeUtil
import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.BRepTools
import org.jcae.opencascade.jni.TopoDS_Shape
import com.eads.threedviewer.dto.ShapeDTO

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

    CADObject findOrCreateCADObject() {
        CADObject cadObject = id ? CADObject.get(id) : new CADObject()
        cadObject.name = name ?: cadObject.name
        cadObject.x = x ?: cadObject.x
        cadObject.y = y ?: cadObject.y
        cadObject.z = z ?: cadObject.z
        cadObject.project = project ?: cadObject.project
        cadObject.type = type ?: cadObject.type
        return cadObject
    }

    File getFile() {
        BRepTools.write(shape, "temp.brep")
        File file = new File("temp.brep")
        file.deleteOnExit()
        log.info "File created ${file.path} and it will be deleted on exit"
        return file
    }

    byte[] getContent() {
        byte[] bytes
        if (file.exists()) {
            bytes = file.bytes
        }
        return bytes
    }

    Map getData() {
        return ShapeUtil.getData(shape)
    }

    ShapeDTO getShapeDTO() {
        return new ShapeDTO(shape)
    }
}
