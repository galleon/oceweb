package com.eads.threedviewer.co

import com.eads.threedviewer.util.ShapeUtil
import groovy.transform.ToString
import org.codehaus.groovy.grails.validation.Validateable
import org.jcae.opencascade.jni.TopoDS_Shape
import org.springframework.web.multipart.MultipartFile
import com.eads.threedviewer.CADObject
import com.eads.threedviewer.enums.ShapeType

@Validateable
@ToString(includeNames = true, includeFields = true, excludes = 'metaClass,errors', includeSuper = true)
class FileShapeCO extends ShapeCO {
    MultipartFile file

    static constraints = {
        file(nullable: false, blank: false, minSize: 1, validator: {var, obj ->
            if (!var.originalFilename.toLowerCase().endsWith(".brep")) {
                return ["content.type.not.supported", 'brep']
            }
        })
    }

    @Override
    TopoDS_Shape getShape() {
        return ShapeUtil.getShape(content)
    }

    byte[] getContent() {
        return file.bytes
    }

    CADObject getCADObject() {
        CADObject cadObject = super.getCADObject()
        cadObject.type = ShapeType.FILE
        return cadObject
    }
}